package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mountreach.campusmanagementsystem.Adapter.StudentAttendanceAdapter;
import com.mountreach.campusmanagementsystem.Database.DBHelper;
import com.mountreach.campusmanagementsystem.Model.AttendanceModel;
import com.mountreach.campusmanagementsystem.R;

import java.util.ArrayList;

public class StudentAttendanceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    StudentAttendanceAdapter adapter;
    ArrayList<AttendanceModel> attendanceList;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

        // Setup back button in header
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.attendanceRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        attendanceList = new ArrayList<>();
        dbHelper = new DBHelper(this);

        loadStudents();

        adapter = new StudentAttendanceAdapter(attendanceList);
        recyclerView.setAdapter(adapter);
    }

    private void loadStudents() {
        Cursor cursor = dbHelper.getStudents();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String roll = cursor.getString(cursor.getColumnIndexOrThrow("rollno"));
                String enrollment = cursor.getString(cursor.getColumnIndexOrThrow("enrollment"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("student_name"));

                attendanceList.add(new AttendanceModel(name, roll, enrollment));

            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}