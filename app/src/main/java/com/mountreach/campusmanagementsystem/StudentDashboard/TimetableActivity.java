package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.os.Bundle;
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
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        rv = findViewById(R.id.rvTimetable);
        dayTabs = findViewById(R.id.dayTabs);
        ref = FirebaseDatabase.getInstance().getReference("Timetable");

        adapter = new TimetableAdapter(this, list, false);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        setupTabs();
        loadTimetable("Mon");

    }

    private void loadTimetable(String day) {
        ref.child(day).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot s) {
                list.clear();
                for (DataSnapshot ds : s.getChildren()) {
                    TimetableModel m = ds.getValue(TimetableModel.class);
                    if (m != null) list.add(m);
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
            @Override public void onTabSelected(TabLayout.Tab t) { loadTimetable(t.getText().toString()); }
            @Override public void onTabUnselected(TabLayout.Tab t) {}
            @Override public void onTabReselected(TabLayout.Tab t) {}
        });
    }
}