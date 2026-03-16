package com.mountreach.campusmanagementsystem.TeacherDashboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mountreach.campusmanagementsystem.Adapter.ComplaintAdapter;
import com.mountreach.campusmanagementsystem.Model.ComplaintModel;
import com.mountreach.campusmanagementsystem.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class ViewComplaintsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ComplaintModel> list;
    ComplaintAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complaints2);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        adapter = new ComplaintAdapter(this,list);

        recyclerView.setAdapter(adapter);

        loadComplaints();
    }

    private void loadComplaints(){

        new Thread(() -> {

            try{

                URL url = new URL("http://10.111.235.231/complaint_api/get_complaints.php");

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(url.openStream())
                );

                String json = br.readLine();

                JSONArray array = new JSONArray(json);

                for(int i=0;i<array.length();i++){

                    JSONObject obj = array.getJSONObject(i);

                    list.add(new ComplaintModel(
                            obj.getString("name"),
                            obj.getString("complaint"),
                            obj.getString("status"),
                            obj.getString("date")
                    ));
                }

                runOnUiThread(() -> adapter.notifyDataSetChanged());

            }catch(Exception e){
                e.printStackTrace();
            }

        }).start();
    }
}