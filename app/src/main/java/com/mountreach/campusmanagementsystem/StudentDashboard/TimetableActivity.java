package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.mountreach.campusmanagementsystem.Adapter.TimetableAdapter;
import com.mountreach.campusmanagementsystem.Database.DBHelper;
import com.mountreach.campusmanagementsystem.Model.TimetableModel;
import com.mountreach.campusmanagementsystem.R;

import java.util.ArrayList;

public class TimetableActivity extends AppCompatActivity {

    TabLayout dayTabs;
    RecyclerView rv;
    ArrayList<TimetableModel> list = new ArrayList<>();
    TimetableAdapter adapter;
    DBHelper dbHelper;
    String selectedDay = "Mon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        dayTabs = findViewById(R.id.dayTabs);
        rv = findViewById(R.id.rvTimetable);

        dbHelper = new DBHelper(this);

        adapter = new TimetableAdapter(this, list, false, null);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        // 🔥 INSERT SAMPLE DATA ONCE (IMPORTANT)
        insertSampleData();

        setupTabs();
        loadTimetable(selectedDay);

        TextView tvYear = findViewById(R.id.tvYear);

// Get logged-in email from SharedPreferences (same key used in MyProfileFragment)
        SharedPreferences prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String email = prefs.getString("email", null);

        if (email != null) {
            String year = dbHelper.getUserYear(email);
            if (year != null && !year.isEmpty()) {
                tvYear.setText(formatYear(year)); // Optional formatting
            } else {
                tvYear.setText("Year not found");
            }
        } else {
            tvYear.setText("Not logged in");
        }


    }

    private String formatYear(String year) {
        switch (year) {
            case "1": return "1st Year";
            case "2": return "2nd Year";
            case "3": return "3rd Year";
            case "4": return "4th Year";
            default: return year + " Year";
        }
    }


    private void setupTabs() {
        String[] days = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        for (String d : days) {
            dayTabs.addTab(dayTabs.newTab().setText(d));
        }

        dayTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedDay = tab.getText().toString();
                loadTimetable(selectedDay);
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadTimetable(String day) {
        list.clear();

        Cursor c = dbHelper.getTimetableByDay(day);
        if (c != null && c.moveToFirst()) {
            do {
                list.add(new TimetableModel(
                        c.getLong(c.getColumnIndexOrThrow("id")),
                        c.getString(c.getColumnIndexOrThrow("subject")),
                        c.getString(c.getColumnIndexOrThrow("teacher")),
                        c.getString(c.getColumnIndexOrThrow("start_time")),
                        c.getString(c.getColumnIndexOrThrow("end_time")),
                        c.getString(c.getColumnIndexOrThrow("day")),
                        c.getString(c.getColumnIndexOrThrow("room"))
                ));
            } while (c.moveToNext());

            c.close();
        }

        adapter.notifyDataSetChanged();
    }

    // 🔥 Insert demo timetable ONLY if table empty
    private void insertSampleData() {

        boolean isInserted = getSharedPreferences("timetable_prefs", MODE_PRIVATE)
                .getBoolean("sample_inserted", false);

        if (!isInserted) {

            // Monday – 2 classes
            dbHelper.insertTimetable(
                    "Physics",
                    "Dr. Rao",
                    "10:30",
                    "11:30",
                    "Mon",
                    "Lab 2"
            );

            dbHelper.insertTimetable(
                    "Chemistry",
                    "Dr. Mehta",
                    "11:30",
                    "12:30",
                    "Mon",
                    "Room 204"
            );

            // Tuesday
            dbHelper.insertTimetable(
                    "Maths",
                    "Prof. Sharma",
                    "09:30",
                    "10:30",
                    "Tue",
                    "201"
            );
            // Wednesday
            dbHelper.insertTimetable(
                    "Maths",
                    "Prof. Sharma",
                    "09:30",
                    "10:30",
                    "Wed",
                    "202"
            );

            // mark inserted
            getSharedPreferences("timetable_prefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("sample_inserted", true)
                    .apply();
        }
    }
}
