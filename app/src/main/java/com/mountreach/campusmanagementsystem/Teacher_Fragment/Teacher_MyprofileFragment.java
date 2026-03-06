package com.mountreach.campusmanagementsystem.Teacher_Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mountreach.campusmanagementsystem.Database.DBHelper;
import com.mountreach.campusmanagementsystem.R;
import com.mountreach.campusmanagementsystem.StudentDashboard.LoginActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class Teacher_MyprofileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 999;
    EditText etProfile, etEmail, etSelectBranch, etSelectYear, etSelectGender;
    Button btnSelectProfile, btnProfileSave, btnLogout;
    CircleImageView ivProfilePhoto;
    ImageView ivEdit;
    DBHelper dbHelper;
    Uri selectedImageUri = null;
    SharedPreferences sharedPreferences;
    TextView tvRole; // declare

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher__myprofile, container, false);
        dbHelper = new DBHelper(requireContext());
        sharedPreferences = requireActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);

        etProfile = view.findViewById(R.id.etProfile);
        etEmail = view.findViewById(R.id.etEmail);
        etSelectBranch = view.findViewById(R.id.etSelectBranch);
        etSelectYear = view.findViewById(R.id.etSelectYear);
        etSelectGender = view.findViewById(R.id.etSelectGender);
        btnProfileSave = view.findViewById(R.id.btnProfileSave);
        btnSelectProfile = view.findViewById(R.id.btnSelectProfile);
        ivEdit = view.findViewById(R.id.ivEdit);
        ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto);
        btnLogout = view.findViewById(R.id.btnLogout);
        tvRole = view.findViewById(R.id.tvRole);

        // default disabled
        setFieldsEnabled(false);
        btnProfileSave.setVisibility(View.GONE);
        btnSelectProfile.setVisibility(View.GONE);

        loadUserProfile();

        ivEdit.setOnClickListener(v -> {
            setFieldsEnabled(true);
            btnProfileSave.setVisibility(View.VISIBLE);
            btnSelectProfile.setVisibility(View.VISIBLE);
        });

        btnProfileSave.setOnClickListener(v -> {
            saveUserProfile();
            setFieldsEnabled(false);
            btnProfileSave.setVisibility(View.GONE);
            btnSelectProfile.setVisibility(View.GONE);
        });

        btnSelectProfile.setOnClickListener(v -> openGallery());

        String[] optionsBranch = {"IT", "CO", "AIML", "EX", "EE", "CE", "ME"};
        etSelectBranch.setOnClickListener(v -> showSingleChoiceDialog("Select Branch", optionsBranch, etSelectBranch));

        String[] optionsYear = {"1st", "2nd", "3rd"};
        etSelectYear.setOnClickListener(v -> showSingleChoiceDialog("Select Year", optionsYear, etSelectYear));

        String[] optionsGender = {"Male", "Female"};
        etSelectGender.setOnClickListener(v -> showSingleChoiceDialog("Select Gender", optionsGender, etSelectGender));

        btnLogout.setOnClickListener(v -> logoutUser());

        return view;
    }

    private void setFieldsEnabled(boolean enabled) {
        etProfile.setEnabled(enabled);
        etSelectBranch.setEnabled(enabled);
        etSelectYear.setEnabled(enabled);
        etSelectGender.setEnabled(enabled);
        // Email should not be editable normally
        etEmail.setEnabled(false);
    }

    private void showSingleChoiceDialog(String title, String[] options, EditText target) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(title);
        builder.setItems(options, (dialog, which) -> target.setText(options[which]));
        builder.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if(selectedImageUri != null) {
                ivProfilePhoto.setImageURI(selectedImageUri);
            }
        }
    }

    private void saveUserProfile() {
        String name = etProfile.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String branch = etSelectBranch.getText().toString().trim();
        String year = etSelectYear.getText().toString().trim();
        String gender = etSelectGender.getText().toString().trim();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.updateUserProfile(email, name, branch, year, gender);

        if(success) {
            Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserProfile() {
        String email = sharedPreferences.getString("email", "");
        if(email.isEmpty()) return;
        String role = sharedPreferences.getString("role", ""); // get role

        if (!role.isEmpty()) {
            tvRole.setText("Role: " + role);
        }

        Cursor cursor = dbHelper.getUserByEmail(email);
        if(cursor != null && cursor.moveToFirst()) {
            etProfile.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            etEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            etSelectBranch.setText(cursor.getString(cursor.getColumnIndexOrThrow("branch")));
            etSelectYear.setText(cursor.getString(cursor.getColumnIndexOrThrow("year")));
            etSelectGender.setText(cursor.getString(cursor.getColumnIndexOrThrow("gender")));
            cursor.close();
        }
    }

    private void logoutUser() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
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