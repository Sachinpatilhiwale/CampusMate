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

import com.mountreach.campusmanagementsystem.Model.DashboardItem;
import com.mountreach.campusmanagementsystem.R;
import com.mountreach.campusmanagementsystem.StudentDashboard.AiActivity;
import com.mountreach.campusmanagementsystem.StudentDashboard.MSBTE_Result_Activity;
import com.mountreach.campusmanagementsystem.TeacherDashboard.Teacher_A_Y_Calender;
import com.mountreach.campusmanagementsystem.TeacherDashboard.Teacher_AttendanceActivity;
import com.mountreach.campusmanagementsystem.TeacherDashboard.Teacher_ComplaintActivity;
import com.mountreach.campusmanagementsystem.TeacherDashboard.Teacher_LeaveRequestActivity;
import com.mountreach.campusmanagementsystem.TeacherDashboard.Teacher_StudyMaterialActivity;
import com.mountreach.campusmanagementsystem.TeacherDashboard.Teacher_TimeTableActivity;

import java.util.List;

public class Teacher_DashboardAdapter extends RecyclerView.Adapter<Teacher_DashboardAdapter.ViewHolder> {

    private final List<DashboardItem> itemList;
    private final Context context;

    public Teacher_DashboardAdapter(Context context, List<DashboardItem> itemList) {
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

            switch (item.getTitle()) {
                case "A.Y Calendar":
                    intent = new Intent(context, Teacher_A_Y_Calender.class);
                    break;

                case "Timetable":
                    intent = new Intent(context, Teacher_TimeTableActivity.class);
                    break;

                case "Attendance":
                    intent = new Intent(context, Teacher_AttendanceActivity.class);
                    break;

                case "Study Material":
                    intent = new Intent(context, Teacher_StudyMaterialActivity.class);
                    break;

                case "Complaint":
                    intent = new Intent(context, Teacher_ComplaintActivity.class);
                    break;

                case "MSBTE Result":
                    intent = new Intent(context, MSBTE_Result_Activity.class);
                    break;

                case "AI chatbot":
                    intent = new Intent(context, AiActivity.class);
                    break;

                case "Leave Request":
                    intent = new Intent(context, Teacher_LeaveRequestActivity.class);
                    break;

            }

            if (intent != null) context.startActivity(intent);
            else Toast.makeText(context, "Clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
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
