package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mountreach.campusmanagementsystem.R;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword, etSelectBranch, etSelectYear, etSelectGender;
    Button btnRegisterStudent, btnRegisterTeacher;
    TextView tvLoginLink;
    ImageView ivclosedEye;
    boolean isPasswordVisible = false;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupDropdowns();
        setupPasswordToggle();
        setupButtons();
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etSelectBranch = findViewById(R.id.etSelectBranch);
        etSelectYear = findViewById(R.id.etSelectYear);
        etSelectGender = findViewById(R.id.etSelectGender);
        btnRegisterStudent = findViewById(R.id.btnRegisterStudent);
        btnRegisterTeacher = findViewById(R.id.btnRegisterTeacher);
        tvLoginLink = findViewById(R.id.tvLoginLink);
        ivclosedEye = findViewById(R.id.ivclosedEye);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private void registerUser(String role) {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String branch = etSelectBranch.getText().toString().trim();
        String year = etSelectYear.getText().toString().trim();
        String gender = etSelectGender.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || branch.isEmpty() || year.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = firebaseAuth.getCurrentUser().getUid();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("uid", userId);
                        userMap.put("name", name);
                        userMap.put("email", email);
                        userMap.put("branch", branch);
                        userMap.put("year", year);
                        userMap.put("gender", gender);
                        userMap.put("role", role);

                        db.collection("users").document(userId).set(userMap)
                                .addOnSuccessListener(unused -> {
                                    // 1. Sign out the user immediately so they have to log in manually
                                    firebaseAuth.signOut();

                                    // 2. Clear any old local preferences to ensure a clean login
                                    getSharedPreferences("loginPrefs", MODE_PRIVATE).edit().clear().apply();

                                    Toast.makeText(this, "Registration Successful! Please Login.", Toast.LENGTH_LONG).show();

                                    // 3. Redirect to LoginActivity
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                });
                    } else {
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupDropdowns() {
        etSelectBranch.setOnClickListener(v -> showListDialog("Select Branch",
                new String[]{"Computer", "IT", "ENTC", "Mechanical", "Civil", "Electrical"}, etSelectBranch));
        etSelectYear.setOnClickListener(v -> showListDialog("Select Year",
                new String[]{"FE", "SE", "TE"}, etSelectYear));
        etSelectGender.setOnClickListener(v -> showListDialog("Select Gender",
                new String[]{"Male", "Female", "Other"}, etSelectGender));
    }

    private void setupButtons() {
        btnRegisterStudent.setOnClickListener(v -> registerUser("Student"));
        btnRegisterTeacher.setOnClickListener(v -> registerUser("Teacher"));
        tvLoginLink.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void showListDialog(String title, String[] items, EditText target) {
        new AlertDialog.Builder(this).setTitle(title).setItems(items, (d, i) -> target.setText(items[i])).show();
    }

    private void setupPasswordToggle() {
        ivclosedEye.setOnClickListener(v -> {
            int cursor = etPassword.getSelectionStart();
            if (isPasswordVisible) {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivclosedEye.setImageResource(R.drawable.closedeyeicon);
            } else {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivclosedEye.setImageResource(R.drawable.openedeyeicon);
            }
            etPassword.setSelection(cursor);
            isPasswordVisible = !isPasswordVisible;
        });
    }
}