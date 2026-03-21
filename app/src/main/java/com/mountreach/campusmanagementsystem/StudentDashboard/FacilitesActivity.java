package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mountreach.campusmanagementsystem.Adapter.FacilityItemAdapter;
import com.mountreach.campusmanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class FacilitesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FacilityItemAdapter facilityAdapter;
    private List<FacilityItem> facilityList;
    private TextView tvViewYourBookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilites);

        // RecyclerView setup
        recyclerView = findViewById(R.id.facilities_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Data list
        facilityList = new ArrayList<>();
        facilityList.add(new FacilityItem("Gym", "6AM-8AM, 6PM-8PM", R.drawable.gym, "gymgard"));
        facilityList.add(new FacilityItem("Auditorium", "10AM-1PM, 2PM-6PM", R.drawable.auditoriam, "auditoriumgard"));
        facilityList.add(new FacilityItem("Tennis Court", "6AM-8PM, 7PM-9PM", R.drawable.auditoriam, "tenniscourtgard"));

        // Adapter
        facilityAdapter = new FacilityItemAdapter(this, facilityList);
        recyclerView.setAdapter(facilityAdapter);

        // View bookings click
        tvViewYourBookings = findViewById(R.id.tvViewYourBookings);
        tvViewYourBookings.setOnClickListener(v -> {
            Intent intent = new Intent(FacilitesActivity.this, ComplaintActivity.class);
            startActivity(intent);
        });
    }
}