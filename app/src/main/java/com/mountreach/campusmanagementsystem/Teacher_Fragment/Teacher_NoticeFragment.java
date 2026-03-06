package com.mountreach.campusmanagementsystem.Teacher_Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mountreach.campusmanagementsystem.Database.DBHelper;
import com.mountreach.campusmanagementsystem.R;

public class Teacher_NoticeFragment extends Fragment {

    EditText etNotice;
    Button btnSend;
    DBHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_teacher__notice, container, false);

        etNotice = view.findViewById(R.id.etNotice);
        btnSend = view.findViewById(R.id.btnSend);
        db = new DBHelper(getContext());

        btnSend.setOnClickListener(v -> {
            String notice = etNotice.getText().toString().trim();

            if (notice.isEmpty()) {
                Toast.makeText(getContext(), "Enter notice", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.insertNotice(notice)) {
                Toast.makeText(getContext(), "Notice sent", Toast.LENGTH_SHORT).show();
                etNotice.setText("");
            } else {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
