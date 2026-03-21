package com.mountreach.campusmanagementsystem.Student_Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mountreach.campusmanagementsystem.Adapter.DashboardAdapter;
import com.mountreach.campusmanagementsystem.Model.DashboardItem;
import com.mountreach.campusmanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView dashboardRecycler;
    DashboardAdapter adapter;
    List<DashboardItem> itemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 1. Get Student Department Info from SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        String branch = prefs.getString("branch", "General");
        String year = prefs.getString("year", "FE");

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
        itemList.add(new DashboardItem(R.drawable.facilities, "Facilities"));

        // 2. Pass branch and year to the Adapter
        adapter = new DashboardAdapter(getContext(), itemList, branch, year);
        dashboardRecycler.setAdapter(adapter);

        return view;
    }
}