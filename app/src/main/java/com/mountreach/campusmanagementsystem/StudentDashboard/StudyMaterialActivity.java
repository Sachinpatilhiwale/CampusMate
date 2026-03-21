package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mountreach.campusmanagementsystem.Adapter.StudyAdapter;
import com.mountreach.campusmanagementsystem.Model.StudyModel;
import com.mountreach.campusmanagementsystem.R;

import java.util.ArrayList;

public class StudyMaterialActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    StudyAdapter adapter;
    ArrayList<StudyModel> list;
    TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_material);

        recyclerView = findViewById(R.id.recyclerStudy);
        emptyView = findViewById(R.id.emptyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new StudyAdapter(this, list);
        recyclerView.setAdapter(adapter);

        fetchData();
    }

    private void fetchData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("pdfs");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    StudyModel model = data.getValue(StudyModel.class);
                    if (model != null) list.add(model);
                }

                adapter.notifyDataSetChanged();

                if (list.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    emptyView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}