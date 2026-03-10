package com.mountreach.campusmanagementsystem.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mountreach.campusmanagementsystem.Model.ComplaintModel;
import com.mountreach.campusmanagementsystem.R;

import java.util.List;
public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ViewHolder> {

    Context context;
    List<ComplaintModel> list;

    public ComplaintAdapter(Context context, List<ComplaintModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_complaint, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ComplaintModel model = list.get(position);

        holder.tvType.setText(model.getType());
        holder.tvTitle.setText(model.getTitle());
        holder.tvDesc.setText(model.getDescription());

        if (!model.getImageUri().isEmpty()) {
            holder.imageView.setImageURI(Uri.parse(model.getImageUri()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvType, tvTitle, tvDesc;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvType = itemView.findViewById(R.id.tvType);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            imageView = itemView.findViewById(R.id.ivComplaintImage);
        }
    }
}