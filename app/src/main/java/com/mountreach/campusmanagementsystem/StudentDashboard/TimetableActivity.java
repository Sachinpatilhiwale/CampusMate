package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.*;
import com.mountreach.campusmanagementsystem.Adapter.TimetableAdapter;
import com.mountreach.campusmanagementsystem.Model.TimetableModel;
import com.mountreach.campusmanagementsystem.R;
import java.util.ArrayList;

public class TimetableActivity extends AppCompatActivity {
    RecyclerView rv;
    TabLayout dayTabs;
    ArrayList<TimetableModel> list = new ArrayList<>();
    TimetableAdapter adapter;
    DatabaseReference baseRef;

    // Filtering variables
    String myBranch, myYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        // 1. Get Student's Branch and Year from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        myBranch = prefs.getString("branch", "General");
        myYear = prefs.getString("year", "FE");

//        // 2. Set Header Text (Optional, if you have a tvHeader in activity_timetable.xml)
//        TextView tvHeader = findViewById(R.id.tvHeader);
//        if (tvHeader != null) {
//            tvHeader.setText(myBranch + " (" + myYear + ") Timetable");
//        }

        rv = findViewById(R.id.rvTimetable);
        dayTabs = findViewById(R.id.dayTabs);

        // 3. Point the Reference to the Student's specific Department/Year
        // Path: Timetable -> IT -> TE
        baseRef = FirebaseDatabase.getInstance().getReference("Timetable")
                .child(myBranch)
                .child(myYear);

        adapter = new TimetableAdapter(this, list, false);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        setupTabs();
        loadTimetable("Mon");
    }

    private void loadTimetable(String day) {
        // We look inside the specific day of the student's department folder
        baseRef.child(day).addValueEventListener(new ValueEventListener() {
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
        });
    }

    private void setupTabs() {
        String[] days = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        for (String d : days) dayTabs.addTab(dayTabs.newTab().setText(d));

        dayTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab t) {
                loadTimetable(t.getText().toString());
            }
            @Override public void onTabUnselected(TabLayout.Tab t) {}
            @Override public void onTabReselected(TabLayout.Tab t) {}
        });
    }
}