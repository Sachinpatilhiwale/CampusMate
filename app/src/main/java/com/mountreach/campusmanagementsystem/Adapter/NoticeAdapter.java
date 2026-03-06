package com.mountreach.campusmanagementsystem.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mountreach.campusmanagementsystem.Model.NoticeModel;
import com.mountreach.campusmanagementsystem.R;
import com.mountreach.campusmanagementsystem.StudentDashboard.NoticeDetailActivity;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.MyVH> {

    ArrayList<NoticeModel> list;

    public NoticeAdapter(ArrayList<NoticeModel> list) {
        this.list = list;
    }

    @Override
    public MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notice, parent, false);
        return new MyVH(v);
    }

    @Override
    public void onBindViewHolder(MyVH h, int p) {
        NoticeModel model = list.get(p);

        h.title.setText(model.getTitle());
        h.desc.setText(model.getDesc());

        h.itemView.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), NoticeDetailActivity.class);
            i.putExtra("title", model.getTitle());
            i.putExtra("desc", model.getDesc());

            if (model.getImage() != null)
                i.putExtra("image", model.getImage());


            v.getContext().startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyVH extends RecyclerView.ViewHolder {

        TextView title, desc;

        MyVH(View v) {
            super(v);
            title = v.findViewById(R.id.tvTitle);
            desc  = v.findViewById(R.id.tvDesc);
        }
    }
}
