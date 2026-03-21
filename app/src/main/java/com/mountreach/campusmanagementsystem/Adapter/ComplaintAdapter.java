package com.mountreach.campusmanagementsystem.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.mountreach.campusmanagementsystem.Model.ComplaintModel;
import com.mountreach.campusmanagementsystem.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ComplaintModel> list;
    private DatabaseReference dbRef;

    public ComplaintAdapter(Context context, ArrayList<ComplaintModel> list, DatabaseReference dbRef) {
        this.context = context;
        this.list = list;
        this.dbRef = dbRef;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_complaint, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ComplaintModel model = list.get(position);

        holder.tvTitle.setText(model.title);
        holder.tvRollNo.setText("Roll: " + model.rollNo);
        holder.tvStudentInfo.setText("By: " + model.studentName + " (" + model.year + ")");
        holder.tvType.setText("Type: " + model.type);
        holder.tvDesc.setText(model.description);
        holder.tvStatus.setText("Status: " + model.status);

        // --- Teacher Message Logic ---
        if (!TextUtils.isEmpty(model.teacherMessage)) {
            holder.layoutMessage.setVisibility(View.VISIBLE);
            holder.tvTeacherMsg.setText("Note: " + model.teacherMessage);
        } else {
            holder.layoutMessage.setVisibility(View.GONE);
        }

        SharedPreferences prefs = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        String role = prefs.getString("role", "");
        String currentUserEmail = prefs.getString("email", "");

        // --- DELETE LOGIC ---
        if (role.equalsIgnoreCase("Teacher") ||
                (role.equalsIgnoreCase("Student") && model.studentEmail.equals(currentUserEmail))) {

            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Complaint")
                        .setMessage("Are you sure you want to remove this record?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            dbRef.child(model.key).removeValue().addOnSuccessListener(aVoid ->
                                    Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show());
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }

        // --- RESOLVE LOGIC WITH MESSAGE ---
        if (role.equalsIgnoreCase("Teacher") && model.status.equalsIgnoreCase("Pending")) {
            holder.btnResolve.setVisibility(View.VISIBLE);
            holder.btnResolve.setOnClickListener(v -> {

                // Create an input field for the message
                EditText input = new EditText(context);
                input.setHint("Enter remarks for student...");
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);

                new AlertDialog.Builder(context)
                        .setTitle("Resolve & Message")
                        .setMessage("Mark this complaint as resolved and send a note:")
                        .setView(input)
                        .setPositiveButton("Submit", (dialog, which) -> {
                            String note = input.getText().toString().trim();

                            Map<String, Object> updates = new HashMap<>();
                            updates.put("status", "Resolved");
                            updates.put("teacherMessage", note);

                            dbRef.child(model.key).updateChildren(updates)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Status Updated", Toast.LENGTH_SHORT).show());
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        } else {
            holder.btnResolve.setVisibility(View.GONE);
        }

        if (model.status.equalsIgnoreCase("Resolved")) {
            holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvRollNo, tvStudentInfo, tvType, tvDesc, tvStatus, tvTeacherMsg;
        LinearLayout layoutMessage;
        Button btnResolve;
        ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvComplaintTitle);
            tvRollNo = itemView.findViewById(R.id.tvRollNo);
            tvStudentInfo = itemView.findViewById(R.id.tvStudentInfo);
            tvType = itemView.findViewById(R.id.tvType);
            tvDesc = itemView.findViewById(R.id.tvDescription);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnResolve = itemView.findViewById(R.id.btnResolve);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            // New views for the message
            tvTeacherMsg = itemView.findViewById(R.id.tvTeacherMsg);
            layoutMessage = itemView.findViewById(R.id.layoutMessage);
        }
    }
}