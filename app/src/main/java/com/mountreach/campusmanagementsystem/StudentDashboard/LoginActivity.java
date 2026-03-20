package com.mountreach.campusmanagementsystem.StudentDashboard;

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

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLoginStudent, btnLoginTeacher;
    TextView tvRegisterLink;
    ImageView ivclosedEye;
    boolean isPasswordVisible = false;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // --- Check if already logged in ---
        SharedPreferences prefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        String role = prefs.getString("role", "");
        if (isLoggedIn && !role.isEmpty() && FirebaseAuth.getInstance().getCurrentUser() != null) {
            // User is already logged in, go directly to dashboard
            Intent intent = role.equals("Teacher") ?
                    new Intent(this, Teacher_Dashboard.class) :
                    new Intent(this, Student_Dashboard.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        initViews();
        setupPasswordToggle();
        setupButtons();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLoginStudent = findViewById(R.id.btnLoginStudent);
        btnLoginTeacher = findViewById(R.id.btnLoginTeacher);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);
        ivclosedEye = findViewById(R.id.ivclosedEye);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
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
        btnLoginStudent.setOnClickListener(v -> loginUser());
        btnLoginTeacher.setOnClickListener(v -> loginUser());

        tvRegisterLink.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
    }

    private void loginUser() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Enter email");
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Enter password");
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        String userId = firebaseAuth.getCurrentUser().getUid();

                        db.collection("users").document(userId)
                                .get()
                                .addOnSuccessListener(doc -> {

                                    String role = doc.getString("role");

                                    // Save login info for persistent login
                                    SharedPreferences.Editor editor =
                                            getSharedPreferences("loginPrefs", Context.MODE_PRIVATE).edit();

                                    editor.putBoolean("isLoggedIn", true);
                                    editor.putString("role", role);
                                    editor.apply();

                                    // Go to dashboard
                                    Intent intent = role.equals("Teacher") ?
                                            new Intent(this, Teacher_Dashboard.class) :
                                            new Intent(this, Student_Dashboard.class);

                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Failed to get user info: " + e.getMessage(),
                                                Toast.LENGTH_LONG).show());

                    } else {
                        Toast.makeText(this,
                                "Login Failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}