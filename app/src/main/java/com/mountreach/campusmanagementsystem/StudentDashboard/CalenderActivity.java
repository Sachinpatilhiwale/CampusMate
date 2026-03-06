package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mountreach.campusmanagementsystem.Adapter.CalenderAdapter;
import com.mountreach.campusmanagementsystem.Adapter.StudyAdapter;
import com.mountreach.campusmanagementsystem.Model.CalenderModel;
import com.mountreach.campusmanagementsystem.Model.StudyModel;
import com.mountreach.campusmanagementsystem.R;

import java.util.ArrayList;

public class CalenderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<CalenderModel> list;
    CalenderAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calender);

        recyclerView = findViewById(R.id.recyclerCalender);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            onBackPressed();   // go back to previous screen
        });


        list = new ArrayList<>();
        adapter = new CalenderAdapter(this, list);
        recyclerView.setAdapter(adapter);

        loadData();


    }

    private void loadData() {
        list.add(new CalenderModel("1", "Academic Calender", ""));
        adapter.notifyDataSetChanged();
    }
}