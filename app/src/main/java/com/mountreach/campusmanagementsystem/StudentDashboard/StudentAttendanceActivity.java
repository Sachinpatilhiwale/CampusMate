package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mountreach.campusmanagementsystem.Model.AttendanceModel;
import com.mountreach.campusmanagementsystem.R;

import java.util.ArrayList;

public class StudentAttendanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

        // Setup back button in header
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

    }

}