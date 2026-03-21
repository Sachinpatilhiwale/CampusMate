package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import com.mountreach.campusmanagementsystem.Adapter.ComplaintAdapter;
import com.mountreach.campusmanagementsystem.Model.ComplaintModel;
import com.mountreach.campusmanagementsystem.R;
import java.util.ArrayList;

public class Student_ComplaintActivity extends AppCompatActivity {

    Spinner spinnerComplaintType;
    EditText etTitle, etDescription, etRollNo; // Added etRollNo
    ImageView btnbackward;
    Button btnSubmit, btnViewHistory; // Added btnViewHistory
    RecyclerView rvMyComplaints;

    DatabaseReference dbRef;
    ArrayList<ComplaintModel> list;
    ComplaintAdapter adapter;
    String sName, sEmail, sBranch, sYear, sRoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_complaint);

        // 1. Initialize Views
        spinnerComplaintType = findViewById(R.id.spinner_complaint_type);
        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etRollNo = findViewById(R.id.et_rollNo); // Matches the XML ID we added
        btnbackward = findViewById(R.id.btnbackward);
        btnSubmit = findViewById(R.id.btn_submit);
        btnViewHistory = findViewById(R.id.btn_view_complaints);
        rvMyComplaints = findViewById(R.id.rvMyComplaints);

        // 2. Setup Spinner Data (This fixes the "not showing" issue)
        String[] types = {"Infrastructure", "Faculty", "Hostel", "Library", "Canteen"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, types);
        spinnerComplaintType.setAdapter(typeAdapter);

        // 3. Get Session Info
        SharedPreferences prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        sName = prefs.getString("name", "Student");
        sEmail = prefs.getString("email", "");
        sBranch = prefs.getString("branch", "");
        sYear = prefs.getString("year", "");

        // Pre-fill roll number if available in preferences
        sRoll = prefs.getString("rollNo", "");
        etRollNo.setText(sRoll);

        // 4. Firebase Path (Silo by Branch and Year)
        dbRef = FirebaseDatabase.getInstance().getReference("Complaints").child(sBranch).child(sYear);

        // 5. Setup RecyclerView
        rvMyComplaints.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        // Note: Use the same dbRef so the adapter knows where to update status if needed
        adapter = new ComplaintAdapter(this, list, dbRef);
        rvMyComplaints.setAdapter(adapter);

        // 6. Click Listeners
        btnSubmit.setOnClickListener(v -> saveComplaintToFirebase());

        btnbackward.setOnClickListener(v -> finish());

        btnViewHistory.setOnClickListener(v -> {
            if (rvMyComplaints.getVisibility() == View.VISIBLE) {
                rvMyComplaints.setVisibility(View.GONE);
                btnViewHistory.setText("View My History");
            } else {
                rvMyComplaints.setVisibility(View.VISIBLE);
                btnViewHistory.setText("Hide History");
                fetchMyComplaints(); // Load data only when requested
            }
        });
    }

    private void saveComplaintToFirebase() {
        String title = etTitle.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String roll = etRollNo.getText().toString().trim();
        String type = spinnerComplaintType.getSelectedItem().toString();

        if (title.isEmpty() || desc.isEmpty() || roll.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Model with the local 'roll' variable
        ComplaintModel complaint = new ComplaintModel(title, desc, type, sName, sEmail, roll, sBranch, sYear);

        btnSubmit.setEnabled(false); // Prevent double clicks
        dbRef.push().setValue(complaint).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Complaint sent to " + sBranch + " Dept", Toast.LENGTH_SHORT).show();
            etTitle.setText("");
            etDescription.setText("");
            btnSubmit.setEnabled(true);
        }).addOnFailureListener(e -> {
            btnSubmit.setEnabled(true);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchMyComplaints() {
        // Privacy: Filter only complaints where studentEmail matches current user
        dbRef.orderByChild("studentEmail").equalTo(sEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ComplaintModel model = ds.getValue(ComplaintModel.class);
                    if (model != null) {
                        model.key = ds.getKey();
                        list.add(model);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Student_ComplaintActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}