package com.mountreach.campusmanagementsystem.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.mountreach.campusmanagementsystem.Model.LeaveModel;
import com.mountreach.campusmanagementsystem.R;
import java.util.ArrayList;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.ViewHolder> {

    Context context;
    ArrayList<LeaveModel> list;
    DatabaseReference dbRef;

    public LeaveAdapter(Context context, ArrayList<LeaveModel> list, DatabaseReference dbRef) {
        this.context = context;
        this.list = list;
        this.dbRef = dbRef;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_leave_request, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaveModel model = list.get(position);

        holder.tvStudentName.setText(model.studentName);
        holder.tvRollNo.setText("Roll No: " + model.rollNo);
        holder.tvYearInfo.setText(model.year + " (" + model.branch + ")");
        holder.tvDuration.setText("Days: " + model.duration);
        holder.tvReason.setText("Reason: " + model.reason);
        holder.tvStatus.setText("Status: " + model.status);

        // ROLE LOGIC: Only show buttons to Teachers for "pending" requests
        SharedPreferences prefs = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        String role = prefs.getString("role", "");

        if (role.equalsIgnoreCase("Teacher") && model.status.equalsIgnoreCase("pending")) {
            holder.layoutButtons.setVisibility(View.VISIBLE);
        } else {
            holder.layoutButtons.setVisibility(View.GONE);
        }

        // Color Status
        if (model.status.equalsIgnoreCase("Approved")) holder.tvStatus.setTextColor(0xFF4CAF50);
        else if (model.status.equalsIgnoreCase("Rejected")) holder.tvStatus.setTextColor(0xFFF44336);

        holder.btnApprove.setOnClickListener(v -> dbRef.child(model.key).child("status").setValue("Approved"));
        holder.btnReject.setOnClickListener(v -> dbRef.child(model.key).child("status").setValue("Rejected"));
    }

    @Override public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName, tvRollNo, tvYearInfo, tvDuration, tvReason, tvStatus;
        Button btnApprove, btnReject;
        LinearLayout layoutButtons;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvRollNo = itemView.findViewById(R.id.tvRollNo);
            tvYearInfo = itemView.findViewById(R.id.tvYearInfo);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvReason = itemView.findViewById(R.id.tvReason);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
            layoutButtons = itemView.findViewById(R.id.layoutButtons);
        }
    }
}