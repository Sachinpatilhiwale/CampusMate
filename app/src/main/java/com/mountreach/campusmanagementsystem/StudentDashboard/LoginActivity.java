package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.mountreach.campusmanagementsystem.Database.DBHelper;
import com.mountreach.campusmanagementsystem.R;
import com.mountreach.campusmanagementsystem.TeacherDashboard.Teacher_Dashboard;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLoginStudent, btnLoginTeacher;
    TextView tvRegisterLink;
    ImageView ivclosedEye;
    boolean isPasswordVisible=false;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is already logged in
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            String role = sharedPreferences.getString("role", "");
            Intent intent = role.equals("Teacher")
                    ? new Intent(this, Teacher_Dashboard.class)
                    : new Intent(this, Student_Dashboard.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLoginStudent = findViewById(R.id.btnLoginStudent);
        btnLoginTeacher = findViewById(R.id.btnLoginTeacher);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);
        ivclosedEye=findViewById(R.id.ivclosedEye);
        dbHelper = new DBHelper(LoginActivity.this);

        ivclosedEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.graphics.Typeface typeface = etPassword.getTypeface();
                if (isPasswordVisible) {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivclosedEye.setImageResource(R.drawable.closedeyeicon);
                } else {

                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivclosedEye.setImageResource(R.drawable.openedeyeicon);
                }
                etPassword.setTypeface(typeface);
                etPassword.setSelection(etPassword.length());
                isPasswordVisible = !isPasswordVisible;
            }
        });


        // Login button click listeners
        btnLoginStudent.setOnClickListener(v -> loginUser("Student"));
        btnLoginTeacher.setOnClickListener(v -> loginUser("Teacher"));

        // Register link click listener
        tvRegisterLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void loginUser(String roleButton) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        String roleDB = dbHelper.checkUser(email, password);

        if (roleDB != null && roleDB.equals(roleButton)) {
            // Show success toast
            Toast.makeText(this, "Login successful as " + roleButton, Toast.LENGTH_SHORT).show();

            // Save login info
            SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putString("role", roleButton);
            editor.putString("email", email);
            editor.apply();

            // Also save in MyPrefs (optional, if timetable uses it)
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putString("user_email", email);
            prefsEditor.apply();

            // Open dashboard
            Intent intent = roleButton.equals("Teacher")
                    ? new Intent(this, Teacher_Dashboard.class)
                    : new Intent(this, Student_Dashboard.class);
            startActivity(intent);
            finish();

        } else if (roleDB != null && !roleDB.equals(roleButton)) {
            Toast.makeText(this, "Wrong role selected for this account", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }
    }

}
