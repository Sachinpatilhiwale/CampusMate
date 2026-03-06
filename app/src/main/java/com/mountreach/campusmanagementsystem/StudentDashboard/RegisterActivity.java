package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.mountreach.campusmanagementsystem.Database.DBHelper;
import com.mountreach.campusmanagementsystem.R;
import com.mountreach.campusmanagementsystem.TeacherDashboard.Teacher_Dashboard;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword, etSelectBranch, etSelectYear, etSelectGender;
    Button btnRegisterStudent, btnRegisterTeacher;
    TextView tvLoginLink;
    ImageView ivclosedEye;
    boolean isPasswordVisible = false;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupDropdowns();
        setupPasswordToggle();
        setupButtons();
    }

    // ------------------ INITIALIZE VIEWS ------------------
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

        dbHelper = new DBHelper(RegisterActivity.this);
    }

    // ------------------ DROPDOWN SETUP ------------------
    private void setupDropdowns() {
        etSelectBranch.setOnClickListener(v -> showListDialog(
                "Select Branch",
                new String[]{"Computer Engineering", "IT Engineering", "ENTC Engineering",
                        "Mechanical Engineering", "Civil Engineering", "Electrical Engineering"},
                etSelectBranch
        ));

        etSelectYear.setOnClickListener(v -> showListDialog(
                "Select Year",
                new String[]{"FE (First Year)", "SE (Second Year)", "TE (Third Year)"},
                etSelectYear
        ));

        etSelectGender.setOnClickListener(v -> showListDialog(
                "Select Gender",
                new String[]{"Male", "Female", "Other"},
                etSelectGender
        ));
    }

    // ------------------ PASSWORD TOGGLE ------------------
    private void setupPasswordToggle() {
        ivclosedEye.setOnClickListener(v -> {
            int start = etPassword.getSelectionStart();
            int end = etPassword.getSelectionEnd();
            android.graphics.Typeface typeface = etPassword.getTypeface();
            if (isPasswordVisible) {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivclosedEye.setImageResource(R.drawable.closedeyeicon);
            } else {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivclosedEye.setImageResource(R.drawable.openedeyeicon);
            }
            etPassword.setTypeface(typeface);
            etPassword.setSelection(start, end);
            isPasswordVisible = !isPasswordVisible;
        });
    }

    // ------------------ BUTTON ACTIONS ------------------
    private void setupButtons() {

        btnRegisterStudent.setOnClickListener(v -> registerUser("Student"));

        btnRegisterTeacher.setOnClickListener(v -> registerUser("Teacher"));

        tvLoginLink.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    // ------------------ REGISTER USER ------------------
    private void registerUser(String role) {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String branch = etSelectBranch.getText().toString().trim();
        String year = etSelectYear.getText().toString().trim();
        String gender = etSelectGender.getText().toString().trim();

        // Validate fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() ||
                branch.isEmpty() || year.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert user login details
        boolean inserted = dbHelper.insertUser(name, email, password, role);

        if (!inserted) {
            Toast.makeText(this, "Registration failed. Email already exists.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert user profile details
        dbHelper.insertUserProfile(name, email, branch, year, gender);

        // Save Login session
        SharedPreferences.Editor editor = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE).edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("email", email);
        editor.putString("role", role);
        editor.apply();

        Toast.makeText(this, role + " Registered Successfully", Toast.LENGTH_SHORT).show();

        // Navigate to respective dashboard
        Intent intent = role.equals("Teacher") ?
                new Intent(this, Teacher_Dashboard.class) :
                new Intent(this, Student_Dashboard.class);

        startActivity(intent);
        finish();
    }

    // ------------------ GENERIC LIST DIALOG ------------------
    private void showListDialog(String title, String[] items, EditText targetEditText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setItems(items, (dialog, which) -> targetEditText.setText(items[which]));
        builder.show();
    }
}
