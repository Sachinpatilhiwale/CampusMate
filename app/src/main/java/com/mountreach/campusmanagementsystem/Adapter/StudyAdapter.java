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

import com.mountreach.campusmanagementsystem.Model.StudyModel;
import com.mountreach.campusmanagementsystem.R;
import com.mountreach.campusmanagementsystem.StudentDashboard.PdfViewActivity;

import java.util.ArrayList;

public class StudyAdapter extends RecyclerView.Adapter<StudyAdapter.ViewHolder> {

    Context context;
    ArrayList<StudyModel> list;

    public StudyAdapter(Context context, ArrayList<StudyModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_study_material, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudyModel m = list.get(position);

        holder.txtTitle.setText(m.getTitle());
        holder.txtDesc.setText("Academic Calendar");
        holder.txtSubject.setText("2025 - 2026");

        holder.btnOpenPDF.setOnClickListener(v -> {
            Intent intent = new Intent(context, PdfViewActivity.class);
            intent.putExtra("pdf_name", "ay_2025_26_academic_calendar.pdf");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtDesc, txtSubject;
        ImageView btnOpenPDF, pdfIcon;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            txtSubject = itemView.findViewById(R.id.txtSubject);
            btnOpenPDF = itemView.findViewById(R.id.btnOpenPDF);
            pdfIcon = itemView.findViewById(R.id.pdfIcon);
        }
    }
}
