package com.mountreach.campusmanagementsystem.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mountreach.campusmanagementsystem.Model.AttendanceModel;
import com.mountreach.campusmanagementsystem.R;

import java.util.ArrayList;

public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.ViewHolder> {

    private ArrayList<AttendanceModel> list;

    public StudentAttendanceAdapter(ArrayList<AttendanceModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceModel model = list.get(position);

        holder.tvName.setText("Name: " + model.getName());
        holder.tvRoll.setText("Roll No: " + model.getRollNo());
        holder.tvEnrollment.setText("Enrollment: " + model.getEnrollmentNo());

        // Set default radio button selection based on status
        if ("Present".equals(model.getStatus())) {
            holder.radioPresent.setChecked(true);
        } else {
            holder.radioAbsent.setChecked(true);
        }

        holder.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioPresent) {
                model.setStatus("Present");
            } else if (checkedId == R.id.radioAbsent) {
                model.setStatus("Absent");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<AttendanceModel> getAttendanceList() {
        return list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRoll, tvEnrollment;
        RadioGroup radioGroup;
        RadioButton radioPresent, radioAbsent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvRoll = itemView.findViewById(R.id.tvRoll);
            tvEnrollment = itemView.findViewById(R.id.tvEnrollment);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            radioPresent = itemView.findViewById(R.id.radioPresent);
            radioAbsent = itemView.findViewById(R.id.radioAbsent);
        }
    }
}