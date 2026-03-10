package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mountreach.campusmanagementsystem.Adapter.ComplaintAdapter;
import com.mountreach.campusmanagementsystem.Database.DBHelper;
import com.mountreach.campusmanagementsystem.Model.ComplaintModel;
import com.mountreach.campusmanagementsystem.R;

import java.util.ArrayList;

public class ViewComplaintsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ComplaintModel> list;
    ComplaintAdapter adapter;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complaints);

        recyclerView = findViewById(R.id.recyclerViewComplaints);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        dbHelper = new DBHelper(this);

        loadComplaints();
    }

    private void loadComplaints() {

        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("SELECT * FROM complaints", null);

        if (cursor.moveToFirst()) {

            do {

                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image_uri"));

                list.add(new ComplaintModel(type, title, desc, image));

            } while (cursor.moveToNext());
        }

        cursor.close();

        adapter = new ComplaintAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }
}