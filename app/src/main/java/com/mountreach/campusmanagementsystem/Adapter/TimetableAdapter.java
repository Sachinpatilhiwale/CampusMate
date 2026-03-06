package com.mountreach.campusmanagementsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mountreach.campusmanagementsystem.R;
import com.mountreach.campusmanagementsystem.Model.TimetableModel;

import java.util.ArrayList;
public class TimetableAdapter
        extends RecyclerView.Adapter<TimetableAdapter.VH> {

    Context context;
    ArrayList<TimetableModel> list;
    boolean isTeacher;

    public TimetableAdapter(Context context,
                            ArrayList<TimetableModel> list,
                            boolean isTeacher,
                            Object unused) {
        this.context = context;
        this.list = list;
        this.isTeacher = isTeacher;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_timetable, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        TimetableModel m = list.get(position);

        h.tvSubject.setText(m.subject);
        h.tvTeacher.setText(m.teacher);
        h.tvTime.setText(m.start + " - " + m.end);
        h.tvRoom.setText("Room " + m.room);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvSubject, tvTeacher, tvTime, tvRoom;

        VH(@NonNull View v) {
            super(v);
            tvSubject = v.findViewById(R.id.tvSubject);
            tvTeacher = v.findViewById(R.id.tvTeacher);
            tvTime = v.findViewById(R.id.tvTime);
            tvRoom = v.findViewById(R.id.tvRoom);
        }
    }
}
