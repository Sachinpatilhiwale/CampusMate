package com.mountreach.campusmanagementsystem.Teacher_Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mountreach.campusmanagementsystem.StudentDashboard.LoginActivity;
import com.mountreach.campusmanagementsystem.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Teacher_MyprofileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 999;

    EditText etProfile, etEmail, etSelectBranch, etSelectYear, etSelectGender;
    Button btnSelectProfile, btnProfileSave, btnLogout;
    CircleImageView ivProfilePhoto;
    ImageView ivEdit;
    TextView tvRole;

    SharedPreferences sharedPreferences;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    FirebaseStorage storage;

    Uri selectedImageUri = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myprofile, container, false);

        // Initialize views
        etProfile = view.findViewById(R.id.etProfile);
        etEmail = view.findViewById(R.id.etEmail);
        etSelectBranch = view.findViewById(R.id.etSelectBranch);
        etSelectYear = view.findViewById(R.id.etSelectYear);
        etSelectGender = view.findViewById(R.id.etSelectGender);

        btnProfileSave = view.findViewById(R.id.btnProfileSave);
        btnSelectProfile = view.findViewById(R.id.btnSelectProfile);
        btnLogout = view.findViewById(R.id.btnLogout);

        ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto);
        ivEdit = view.findViewById(R.id.ivEdit);

        tvRole = view.findViewById(R.id.tvRole);

        sharedPreferences = requireActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Default state: fields disabled
        setFieldsEnabled(false);
        btnProfileSave.setVisibility(View.GONE);
        btnSelectProfile.setVisibility(View.GONE);

        // Load data from Firestore
        loadUserProfileFromFirestore();

        // Edit button
        ivEdit.setOnClickListener(v -> {
            setFieldsEnabled(true);
            btnProfileSave.setVisibility(View.VISIBLE);
            btnSelectProfile.setVisibility(View.VISIBLE);
        });

        // Open gallery when "Change Photo" clicked
        btnSelectProfile.setOnClickListener(v -> openGallery());

        // Save updated profile
        btnProfileSave.setOnClickListener(v -> saveUserProfileToFirestore());

        // Logout
        btnLogout.setOnClickListener(v -> logoutUser());

        return view;
    }

    private void setFieldsEnabled(boolean enabled) {
        etProfile.setEnabled(enabled);
        etSelectBranch.setEnabled(enabled);
        etSelectYear.setEnabled(enabled);
        etSelectGender.setEnabled(enabled);
        etEmail.setEnabled(false); // email should not be editable
    }

    private void loadUserProfileFromFirestore() {
        String uid = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : null;
        String role = sharedPreferences.getString("role", "");
        if (!role.isEmpty()) tvRole.setText("Role: " + role);

        if (uid == null) return;

        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        etProfile.setText(documentSnapshot.getString("name"));
                        etEmail.setText(documentSnapshot.getString("email"));
                        etSelectBranch.setText(documentSnapshot.getString("branch"));
                        etSelectYear.setText(documentSnapshot.getString("year"));
                        etSelectGender.setText(documentSnapshot.getString("gender"));

                        // Load profile image if exists
                        String imageUrl = documentSnapshot.getString("profileImage");
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(requireContext())
                                    .load(imageUrl)
                                    .placeholder(R.drawable.myprofile)
                                    .into(ivProfilePhoto);
                        }

                    } else {
                        Toast.makeText(getContext(), "No user data found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(),
                        "Failed to load profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if(selectedImageUri != null) {
                ivProfilePhoto.setImageURI(selectedImageUri);
                uploadProfileImageToFirebase(selectedImageUri);
            }
        }
    }

    private void uploadProfileImageToFirebase(Uri imageUri) {
        String uid = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : null;
        if(uid == null) return;

        StorageReference storageRef = storage.getReference().child("profile_images/" + uid + ".jpg");

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot ->
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Save download URL to Firestore
                            db.collection("users").document(uid)
                                    .update("profileImage", uri.toString())
                                    .addOnSuccessListener(unused ->
                                            Toast.makeText(getContext(), "Profile image updated", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(getContext(), "Failed to save image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        }))
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveUserProfileToFirestore() {
        String name = etProfile.getText().toString().trim();
        String branch = etSelectBranch.getText().toString().trim();
        String year = etSelectYear.getText().toString().trim();
        String gender = etSelectGender.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(branch) || TextUtils.isEmpty(year)
                || TextUtils.isEmpty(gender)) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : null;
        if(uid == null) return;

        db.collection("users").document(uid)
                .update("name", name, "branch", branch, "year", year, "gender", gender)
                .addOnSuccessListener(unused ->
                        Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void logoutUser() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    firebaseAuth.signOut();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    requireActivity().finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}