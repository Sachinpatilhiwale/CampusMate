package com.mountreach.campusmanagementsystem.Teacher_Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mountreach.campusmanagementsystem.Adapter.Teacher_DashboardAdapter;
import com.mountreach.campusmanagementsystem.Model.DashboardItem;
import com.mountreach.campusmanagementsystem.R;
import java.util.ArrayList;
import java.util.List;

public class Teacher_HomeFragment extends Fragment {
    RecyclerView dashboardRecycler;
    Teacher_DashboardAdapter adapter;
    List<DashboardItem> itemList;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher__home, container, false);

        dashboardRecycler = view.findViewById(R.id.dashboardRecycler);
        dashboardRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        itemList = new ArrayList<>();
        itemList.add(new DashboardItem(R.drawable.attendence, "Attendance"));
        itemList.add(new DashboardItem(R.drawable.calendar, "A.Y Calendar"));
        itemList.add(new DashboardItem(R.drawable.timetable, "Timetable"));
        itemList.add(new DashboardItem(R.drawable.study_material, "Study Material"));
        itemList.add(new DashboardItem(R.drawable.msbte, "MSBTE Result"));
        itemList.add(new DashboardItem(R.drawable.complaint, "Complaint"));
        itemList.add(new DashboardItem(R.drawable.ai, "AI chatbot"));
        itemList.add(new DashboardItem(R.drawable.leaverequest, "Leave Request"));

        adapter = new Teacher_DashboardAdapter(getContext(), itemList);
        dashboardRecycler.setAdapter(adapter);

        return view;
    }
}