package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mountreach.campusmanagementsystem.Adapter.StudyAdapter;
import com.mountreach.campusmanagementsystem.Model.StudyModel;
import com.mountreach.campusmanagementsystem.R;

import java.util.ArrayList;

public class StudyMaterialActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<StudyModel> list;
    StudyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_material);

        recyclerView = findViewById(R.id.recyclerStudy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            onBackPressed();   // go back to previous screen
        });


        list = new ArrayList<>();
        adapter = new StudyAdapter(this, list);
        recyclerView.setAdapter(adapter);

        loadData();
    }
    private void loadData() {
        list.add(new StudyModel("1", "Academic Calender", ""));
        adapter.notifyDataSetChanged();
    }
}
