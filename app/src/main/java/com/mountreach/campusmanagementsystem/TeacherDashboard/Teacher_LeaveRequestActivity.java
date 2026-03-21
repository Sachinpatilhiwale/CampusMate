package com.mountreach.campusmanagementsystem.TeacherDashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import com.mountreach.campusmanagementsystem.Adapter.LeaveAdapter;
import com.mountreach.campusmanagementsystem.Model.LeaveModel;
import com.mountreach.campusmanagementsystem.R;
import java.util.ArrayList;

// ... imports ...

public class Teacher_LeaveRequestActivity extends AppCompatActivity {

    RecyclerView rv;
    ArrayList<LeaveModel> list;
    LeaveAdapter adapter;
    DatabaseReference dbRef; // Declare it here at the top

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_leave_request);

        // 1. Initialize UI
        rv = findViewById(R.id.rvLeaveRequests);
        rv.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        // 2. Get Teacher's Department/Year from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String teacherBranch = prefs.getString("branch", "");
        String teacherYear = prefs.getString("year", "");

        // --- PLACE THE CODE HERE ---
        // 3. Point to the specific Silo path
        dbRef = FirebaseDatabase.getInstance().getReference("LeaveRequests")
                .child(teacherBranch)
                .child(teacherYear);
        // ---------------------------

        // 4. Initialize Adapter with this specific dbRef
        adapter = new LeaveAdapter(this, list, dbRef);
        rv.setAdapter(adapter);

        // 5. Start listening for data
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    LeaveModel m = ds.getValue(LeaveModel.class);
                    if (m != null) {
                        m.key = ds.getKey();
                        list.add(m);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}