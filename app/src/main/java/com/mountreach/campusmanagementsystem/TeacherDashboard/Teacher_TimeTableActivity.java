package com.mountreach.campusmanagementsystem.TeacherDashboard;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
    DatabaseReference ref;
    ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_time_table_acitivity);

        dayTabs = findViewById(R.id.dayTabs);
        rv = findViewById(R.id.rvTimetable);
        FloatingActionButton fab = findViewById(R.id.fabAdd);

        ref = FirebaseDatabase.getInstance().getReference("Timetable");
        adapter = new TimetableAdapter(this, list, true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        setupTabs();
        loadTimetable(selectedDay);
        fab.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        // 1. Inflate the custom layout
        View view = getLayoutInflater().inflate(R.layout.item_timetable_teacher, null);

        // 2. Initialize the views from the inflated layout
        EditText etSubject = view.findViewById(R.id.etSubject);
        EditText etTeacher = view.findViewById(R.id.etTeacher);
        EditText etTime = view.findViewById(R.id.etTime);
        EditText etRoom = view.findViewById(R.id.etRoom);
        Button btnAction = view.findViewById(R.id.btnUpdate); // We use the existing button in layout

        // Change button text to "Add Class" for the dialog
        btnAction.setText("Add New Class");

        // Create the AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        // 3. Set up the 24hr Time Picker for the dialog field
        etTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this, (tp, h, m) ->
                    etTime.setText(String.format("%02d:%02d", h, m)),
                    c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        });

        // 4. Handle the "Add New Class" click
        btnAction.setOnClickListener(v -> {
            String sub = etSubject.getText().toString().trim();
            String tea = etTeacher.getText().toString().trim();
            String tim = etTime.getText().toString().trim();
            String rom = etRoom.getText().toString().trim();

            if (sub.isEmpty()) {
                etSubject.setError("Subject Required");
                return;
            }

            // Generate Firebase key
            String key = ref.child(selectedDay).push().getKey();

            // Create the model
            TimetableModel m = new TimetableModel(sub, tea, tim, rom, selectedDay);
            m.key = key;

            // Save to Firebase
            if (key != null) {
                ref.child(selectedDay).child(key).setValue(m)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Class Added Successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss(); // Close the dialog
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
                    if (m != null) { m.key = ds.getKey(); m.day = day; list.add(m); }
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
            @Override public void onTabSelected(TabLayout.Tab t) { selectedDay = t.getText().toString(); loadTimetable(selectedDay); }
            @Override public void onTabUnselected(TabLayout.Tab t) {}
            @Override public void onTabReselected(TabLayout.Tab t) {}
        });
    }
}