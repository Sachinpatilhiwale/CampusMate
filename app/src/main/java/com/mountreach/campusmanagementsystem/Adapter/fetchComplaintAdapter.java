package com.mountreach.campusmanagementsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mountreach.campusmanagementsystem.Model.ComplaintModel;
import com.mountreach.campusmanagementsystem.Model.fetchComplaintModel;
import com.mountreach.campusmanagementsystem.R;

import java.util.ArrayList;

public class fetchComplaintAdapter extends RecyclerView.Adapter<fetchComplaintAdapter.ViewHolder>{

    Context context;
    ArrayList<fetchComplaintModel> list;

    public fetchComplaintAdapter(Context context, ArrayList<fetchComplaintModel> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_fetchcomplaint,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        fetchComplaintModel model = list.get(position);

        holder.tvName.setText(model.getName());
        holder.tvComplaint.setText(model.getComplaint());
        holder.tvStatus.setText("Status: "+model.getStatus());
        holder.tvDate.setText(model.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName,tvComplaint,tvStatus,tvDate;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvComplaint = itemView.findViewById(R.id.tvComplaint);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}