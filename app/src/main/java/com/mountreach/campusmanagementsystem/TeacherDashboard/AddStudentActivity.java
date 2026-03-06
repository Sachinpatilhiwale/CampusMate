package com.mountreach.campusmanagementsystem.TeacherDashboard;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mountreach.campusmanagementsystem.Database.DBHelper;
import com.mountreach.campusmanagementsystem.R;

public class AddStudentActivity extends AppCompatActivity {

    EditText etRoll, etEnroll, etName;
    Button btnAdd;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        etRoll = findViewById(R.id.etRoll);
        etEnroll = findViewById(R.id.etEnroll);
        etName = findViewById(R.id.etName);
        btnAdd = findViewById(R.id.btnAdd);

        db = new DBHelper(this);

        btnAdd.setOnClickListener(v -> {
            int roll = Integer.parseInt(etRoll.getText().toString());
            String enroll = etEnroll.getText().toString();
            String name = etName.getText().toString();

            boolean success = db.addStudent(roll, enroll, name);
            if (success) {
                Toast.makeText(this, "Student Added!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to Add!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
