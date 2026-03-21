package com.mountreach.campusmanagementsystem.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mountreach.campusmanagementsystem.StudentDashboard.AiActivity;
import com.mountreach.campusmanagementsystem.StudentDashboard.CalenderActivity;
import com.mountreach.campusmanagementsystem.Model.DashboardItem;
import com.mountreach.campusmanagementsystem.R;
import com.mountreach.campusmanagementsystem.StudentDashboard.ComplaintActivity;
import com.mountreach.campusmanagementsystem.StudentDashboard.FacilitesActivity;
import com.mountreach.campusmanagementsystem.StudentDashboard.Student_LeaveRequestActivity;
import com.mountreach.campusmanagementsystem.StudentDashboard.MSBTE_Result_Activity;
import com.mountreach.campusmanagementsystem.StudentDashboard.StudentAttendanceActivity;
import com.mountreach.campusmanagementsystem.StudentDashboard.StudyMaterialActivity;
import com.mountreach.campusmanagementsystem.StudentDashboard.TimetableActivity;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private final List<DashboardItem> itemList;
    private final Context context;

    public DashboardAdapter(Context context, List<DashboardItem> itemList, String branch, String year) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DashboardItem item = itemList.get(position);

        holder.title.setText(item.getTitle());
        holder.icon.setImageResource(item.getIconResId());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = null;

            // The logic inside these activities will handle department filtering
            // by reading from SharedPreferences "loginPrefs"
            switch (item.getTitle()) {
                case "A.Y Calendar":
                    intent = new Intent(context, CalenderActivity.class);
                    break;

                case "Timetable":
                    intent = new Intent(context, TimetableActivity.class);
                    break;

                case "Attendance":
                    intent = new Intent(context, StudentAttendanceActivity.class);
                    break;

                case "Study Material":
                    intent = new Intent(context, StudyMaterialActivity.class);
                    break;

                case "MSBTE Result":
                    intent = new Intent(context, MSBTE_Result_Activity.class);
                    break;

                case "Complaint":
                    intent = new Intent(context, ComplaintActivity.class);
                    break;

                case "AI chatbot":
                    intent = new Intent(context, AiActivity.class);
                    break;

                case "Leave Request":
                    intent = new Intent(context, Student_LeaveRequestActivity.class);
                    break;

                case "Facilities":
                    intent = new Intent(context, FacilitesActivity.class);
                    break;
            }

            if (intent != null) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.item_icon);
            title = itemView.findViewById(R.id.item_title);
        }
    }
}