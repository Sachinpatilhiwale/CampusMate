package com.mountreach.campusmanagementsystem.Adapter;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.FirebaseDatabase;
import com.mountreach.campusmanagementsystem.Model.TimetableModel;
import com.mountreach.campusmanagementsystem.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class TimetableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<TimetableModel> list;
    private boolean isTeacher;

    public TimetableAdapter(Context context, ArrayList<TimetableModel> list, boolean isTeacher) {
        this.context = context;
        this.list = list;
        this.isTeacher = isTeacher;
    }

    @Override
    public int getItemViewType(int position) { return isTeacher ? 1 : 0; }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_timetable_teacher, parent, false);
            return new TeacherViewHolder(v);
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.item_timetable_student, parent, false);
            return new StudentViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TimetableModel model = list.get(position);

        if (isTeacher) {
            TeacherViewHolder h = (TeacherViewHolder) holder;
            h.etSubject.setText(model.subject);
            h.etTeacher.setText(model.teacher);
            h.etTime.setText(model.time);
            h.etRoom.setText(model.room);

            // 1. Time Picker
            h.etTime.setOnClickListener(v -> {
                Calendar c = Calendar.getInstance();
                new TimePickerDialog(context, (tp, h1, m1) ->
                        h.etTime.setText(String.format("%02d:%02d", h1, m1)),
                        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
            });

            // 2. Update Logic
            h.btnUpdate.setOnClickListener(v -> {
                HashMap<String, Object> map = new HashMap<>();
                map.put("subject", h.etSubject.getText().toString());
                map.put("teacher", h.etTeacher.getText().toString());
                map.put("time", h.etTime.getText().toString());
                map.put("room", h.etRoom.getText().toString());

                FirebaseDatabase.getInstance().getReference("Timetable")
                        .child(model.day).child(model.key).updateChildren(map)
                        .addOnSuccessListener(a -> Toast.makeText(context, "Updated!", Toast.LENGTH_SHORT).show());
            });

            // 3. Delete Button Logic (Red Button)
            h.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Class")
                        .setMessage("Are you sure you want to delete " + model.subject + "?")
                        .setPositiveButton("Yes", (d, w) -> {
                            FirebaseDatabase.getInstance().getReference("Timetable")
                                    .child(model.day).child(model.key).removeValue()
                                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show());
                        })
                        .setNegativeButton("No", null)
                        .show();
            });

        } else {
            StudentViewHolder h = (StudentViewHolder) holder;
            h.tvSubject.setText(model.subject);
            h.tvTeacher.setText(model.teacher);
            h.tvTime.setText(model.time);
            h.tvRoom.setText(model.room);
        }
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject, tvTeacher, tvTime, tvRoom;
        public StudentViewHolder(View v) { super(v);
            tvSubject = v.findViewById(R.id.tvSubject);
            tvTeacher = v.findViewById(R.id.tvTeacher);
            tvTime = v.findViewById(R.id.tvTime);
            tvRoom = v.findViewById(R.id.tvRoom);
        }
    }

    public static class TeacherViewHolder extends RecyclerView.ViewHolder {
        EditText etSubject, etTeacher, etTime, etRoom;
        Button btnUpdate, btnDelete; // Added btnDelete
        public TeacherViewHolder(View v) { super(v);
            etSubject = v.findViewById(R.id.etSubject);
            etTeacher = v.findViewById(R.id.etTeacher);
            etTime = v.findViewById(R.id.etTime);
            etRoom = v.findViewById(R.id.etRoom);
            btnUpdate = v.findViewById(R.id.btnUpdate);
            btnDelete = v.findViewById(R.id.btnDelete); // Initialize delete button
        }
    }
}