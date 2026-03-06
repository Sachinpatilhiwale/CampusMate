package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mountreach.campusmanagementsystem.R;

public class NoticeDetailActivity extends AppCompatActivity {

    TextView tvTitle, tvDesc,tvImageDesc;
    ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        tvTitle = findViewById(R.id.tvDetailTitle);
        tvDesc = findViewById(R.id.tvDetailDesc);
        img = findViewById(R.id.imgDetail);
        tvImageDesc = findViewById(R.id.tvImageDesc);

        tvTitle.setText(getIntent().getStringExtra("title"));
        tvDesc.setText(getIntent().getStringExtra("desc"));
        img.setImageResource(getIntent().getIntExtra("image",0));

        int imgId = getIntent().getIntExtra("image", 0);
        String imgDesc = getIntent().getStringExtra("imgDesc");


        if (imgId != 0) {
            img.setVisibility(View.VISIBLE);
            img.setImageResource(imgId);

            if (imgDesc != null && !imgDesc.isEmpty()) {
                tvImageDesc.setVisibility(View.VISIBLE);
                tvImageDesc.setText(imgDesc);
            }
        } else {
            img.setVisibility(View.GONE);
        }
    }
}
