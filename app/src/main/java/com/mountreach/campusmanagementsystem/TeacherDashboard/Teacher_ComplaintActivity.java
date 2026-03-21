package com.mountreach.campusmanagementsystem.TeacherDashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mountreach.campusmanagementsystem.Adapter.ComplaintAdapter;
import com.mountreach.campusmanagementsystem.Model.ComplaintModel;
import com.mountreach.campusmanagementsystem.R;

import java.util.ArrayList;

public class Teacher_ComplaintActivity extends AppCompatActivity {

    RecyclerView rvTeacherComplaints;
    DatabaseReference dbRef;
    ArrayList<ComplaintModel> list;
    ComplaintAdapter adapter;
    String tBranch, tYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_complaint);

        // 1. Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // 2. Init Views
        rvTeacherComplaints = findViewById(R.id.rv_teacher_complaints);
        rvTeacherComplaints.setLayoutManager(new LinearLayoutManager(this));

        // 3. Get Teacher's Session Info (Siloed by Branch/Year)
        SharedPreferences prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        tBranch = prefs.getString("branch", ""); // e.g., Computer
        tYear = prefs.getString("year", "");     // e.g., TY

        // 4. Firebase Path (Points to current Teacher's silo)
        dbRef = FirebaseDatabase.getInstance().getReference("Complaints")
                .child(tBranch)
                .child(tYear);

        // 5. Setup Adapter
        list = new ArrayList<>();
        adapter = new ComplaintAdapter(this, list, dbRef);
        rvTeacherComplaints.setAdapter(adapter);

        // 6. Fetch Data
        fetchComplaints();
    }

    private void fetchComplaints() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ComplaintModel model = ds.getValue(ComplaintModel.class);
                    if (model != null) {
                        model.key = ds.getKey(); // Crucial for delete/resolve logic
                        list.add(model);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Teacher_ComplaintActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}