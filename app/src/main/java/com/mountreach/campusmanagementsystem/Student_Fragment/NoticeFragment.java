package com.mountreach.campusmanagementsystem.Student_Fragment;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mountreach.campusmanagementsystem.Adapter.NoticeAdapter;
import com.mountreach.campusmanagementsystem.Database.DBHelper;
import com.mountreach.campusmanagementsystem.Model.NoticeModel;
import com.mountreach.campusmanagementsystem.R;

import java.util.ArrayList;

public class NoticeFragment extends Fragment {

    RecyclerView rvNotices;
    ArrayList<NoticeModel> list;
    NoticeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_notice, container, false);

        rvNotices = v.findViewById(R.id.rvNotices);
        rvNotices.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        list = new ArrayList<>();

        list.add(new NoticeModel("Unit Test 1 Timetable",
                "Hello Student this is our Unit Test Time Table",
                R.drawable.timetable));

        list.add(new NoticeModel("Independence Day 2k25",
                "Good Morning Students",R.drawable.msbte));

        list.add(new NoticeModel("NBA Visit Instructions",
                "One of the best visits"));

        list.add(new NoticeModel("Rhythm 2k25 Event",
                "Biggest event of Academic year"));

        adapter = new NoticeAdapter(list);
        rvNotices.setAdapter(adapter);

        return v;
    }
}
