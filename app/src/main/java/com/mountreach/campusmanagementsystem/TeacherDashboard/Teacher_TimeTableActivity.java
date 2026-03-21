package com.mountreach.campusmanagementsystem.TeacherDashboard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.*;
import com.mountreach.campusmanagementsystem.Adapter.TimetableAdapter;
import com.mountreach.campusmanagementsystem.Model.TimetableModel;
import com.mountreach.campusmanagementsystem.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Teacher_TimeTableActivity extends AppCompatActivity {
    TabLayout dayTabs;
    RecyclerView rv;
    ArrayList<TimetableModel> list = new ArrayList<>();
    TimetableAdapter adapter;
    String selectedDay = "Mon";
    DatabaseReference ref; // This will now point to Timetable/Branch/Year
    ValueEventListener listener;

    String teacherBranch, teacherYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_time_table_acitivity);

        // 1. Get Teacher's Department Info from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        teacherBranch = prefs.getString("branch", "General");
        teacherYear = prefs.getString("year", "FE");

        dayTabs = findViewById(R.id.dayTabs);
        rv = findViewById(R.id.rvTimetable);
        FloatingActionButton fab = findViewById(R.id.fabAdd);

        // 2. IMPORTANT: Set the reference to match the Student's structure
        ref = FirebaseDatabase.getInstance().getReference("Timetable")
                .child(teacherBranch)
                .child(teacherYear);

        adapter = new TimetableAdapter(this, list, true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        setupTabs();
        loadTimetable(selectedDay);
        fab.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        View view = getLayoutInflater().inflate(R.layout.item_timetable_teacher, null);
        EditText etSubject = view.findViewById(R.id.etSubject);
        EditText etTeacher = view.findViewById(R.id.etTeacher);
        EditText etTime = view.findViewById(R.id.etTime);
        EditText etRoom = view.findViewById(R.id.etRoom);
        EditText etDate = view.findViewById(R.id.etDate);
        Button btnAction = view.findViewById(R.id.btnUpdate);
        Button btnDelete = view.findViewById(R.id.btnDelete);

        if (btnDelete != null) btnDelete.setVisibility(View.GONE);
        btnAction.setText("Add New Class");

        AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();

        etTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this, (tp, h, m) ->
                    etTime.setText(String.format("%02d:%02d", h, m)),
                    c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        });

        etDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (dp, y, m, d) ->
                    etDate.setText(d + "/" + (m + 1) + "/" + y),
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnAction.setOnClickListener(v -> {
            String sub = etSubject.getText().toString().trim();
            String tea = etTeacher.getText().toString().trim();
            String tim = etTime.getText().toString().trim();
            String rom = etRoom.getText().toString().trim();
            String dat = etDate.getText().toString().trim();

            if (sub.isEmpty()) {
                etSubject.setError("Required");
                return;
            }

            // This now saves to Timetable/Branch/Year/Day
            String key = ref.child(selectedDay).push().getKey();
            TimetableModel m = new TimetableModel(sub, tea, tim, rom, selectedDay, dat);
            m.key = key;

            if (key != null) {
                ref.child(selectedDay).child(key).setValue(m).addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Added to " + teacherBranch + " " + teacherYear, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
            }
        });
        dialog.show();
    }

    private void loadTimetable(String day) {
        if (listener != null) ref.child(selectedDay).removeEventListener(listener);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot s) {
                list.clear();
                for (DataSnapshot ds : s.getChildren()) {
                    TimetableModel m = ds.getValue(TimetableModel.class);
                    if (m != null) {
                        m.key = ds.getKey();
                        m.day = day;
                        list.add(m);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override public void onCancelled(@NonNull DatabaseError e) {}
        };
        ref.child(day).addValueEventListener(listener);
    }

    private void setupTabs() {
        String[] days = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        for (String d : days) dayTabs.addTab(dayTabs.newTab().setText(d));

        dayTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab t) {
                selectedDay = t.getText().toString();
                loadTimetable(selectedDay);
            }
            @Override public void onTabUnselected(TabLayout.Tab t) {}
            @Override public void onTabReselected(TabLayout.Tab t) {}
        });
    }
}