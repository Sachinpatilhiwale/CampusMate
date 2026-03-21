package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mountreach.campusmanagementsystem.Adapter.LeaveAdapter;
import com.mountreach.campusmanagementsystem.Model.LeaveModel;
import com.mountreach.campusmanagementsystem.R;

import java.util.ArrayList;

public class Student_LeaveRequestActivity extends AppCompatActivity {

    EditText etRollNo, etDuration, etReason;
    Button btnSubmit;
    RecyclerView rvStatus;
    ArrayList<LeaveModel> list;
    LeaveAdapter adapter;

    DatabaseReference dbRef;
    String sName, sEmail, sBranch, sYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request);

        // --- 1. Header & Navigation ---
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Leave Portal");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // --- 2. Initialize Views ---
        etRollNo = findViewById(R.id.etRollNo);
        etDuration = findViewById(R.id.etDuration);
        etReason = findViewById(R.id.etReason);
        btnSubmit = findViewById(R.id.btnSubmit);
        rvStatus = findViewById(R.id.rvStudentStatus);

        // --- 3. Get Session Info (Privacy & Silo) ---
        SharedPreferences prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        sName = prefs.getString("name", "Student");
        sEmail = prefs.getString("email", ""); // For Privacy filtering
        sBranch = prefs.getString("branch", "N/A"); // For Silo (e.g. Computer)
        sYear = prefs.getString("year", "N/A"); // For Silo (e.g. FE)

        // Point to the specific Department/Year folder
        dbRef = FirebaseDatabase.getInstance().getReference("LeaveRequests")
                .child(sBranch).child(sYear);

        // --- 4. Setup RecyclerView ---
        rvStatus.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new LeaveAdapter(this, list, dbRef);
        rvStatus.setAdapter(adapter);

        btnSubmit.setOnClickListener(v -> submitRequest());

        // --- 5. Privacy Logic: Fetch only MY requests ---
        fetchMyStatus();
    }

    private void submitRequest() {
        String roll = etRollNo.getText().toString().trim();
        String dur = etDuration.getText().toString().trim();
        String res = etReason.getText().toString().trim();

        if (roll.isEmpty() || dur.isEmpty() || res.isEmpty()) {
            Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Model with current session data
        LeaveModel data = new LeaveModel(sName, sEmail, roll, dur, res, "pending", sBranch, sYear);

        // Push to Firebase
        dbRef.push().setValue(data).addOnSuccessListener(unused -> {
            Toast.makeText(this, "Request submitted to " + sBranch + " Dept", Toast.LENGTH_SHORT).show();
            etRollNo.setText(""); etDuration.setText(""); etReason.setText("");
        });
    }

    private void fetchMyStatus() {
        // QUERY: Filter by email so this student doesn't see others' requests
        Query myPrivacyQuery = dbRef.orderByChild("studentEmail").equalTo(sEmail);

        myPrivacyQuery.addValueEventListener(new ValueEventListener() {
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