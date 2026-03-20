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
import com.mountreach.campusmanagementsystem.TeacherDashboard.Teacher_Dashboard;

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

    private void setupDropdowns() {
        etSelectBranch.setOnClickListener(v -> showListDialog("Select Branch",
                new String[]{"Computer", "IT", "ENTC", "Mechanical", "Civil", "Electrical"},
                etSelectBranch));

        etSelectYear.setOnClickListener(v -> showListDialog("Select Year",
                new String[]{"FE", "SE", "TE"},
                etSelectYear));

        etSelectGender.setOnClickListener(v -> showListDialog("Select Gender",
                new String[]{"Male", "Female", "Other"},
                etSelectGender));
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

    private void setupButtons() {
        btnRegisterStudent.setOnClickListener(v -> registerUser("Student"));
        btnRegisterTeacher.setOnClickListener(v -> registerUser("Teacher"));

        tvLoginLink.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser(String role) {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String branch = etSelectBranch.getText().toString().trim();
        String year = etSelectYear.getText().toString().trim();
        String gender = etSelectGender.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()
                || branch.isEmpty() || year.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Min 6 characters");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        String userId = firebaseAuth.getCurrentUser().getUid();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", name);
                        userMap.put("email", email);
                        userMap.put("branch", branch);
                        userMap.put("year", year);
                        userMap.put("gender", gender);
                        userMap.put("role", role);

                        db.collection("users").document(userId)
                                .set(userMap)
                                .addOnSuccessListener(unused -> {

                                    SharedPreferences.Editor editor =
                                            getSharedPreferences("loginPrefs", Context.MODE_PRIVATE).edit();
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.putString("role", role);
                                    editor.apply();

                                    Intent intent = role.equals("Teacher") ?
                                            new Intent(this, Teacher_Dashboard.class) :
                                            new Intent(this, Student_Dashboard.class);

                                    startActivity(intent);
                                    finish();
                                });

                    } else {
                        Toast.makeText(this,
                                "Error: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showListDialog(String title, String[] items, EditText target) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setItems(items, (d, i) -> target.setText(items[i]))
                .show();
    }
}