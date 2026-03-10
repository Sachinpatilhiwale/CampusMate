package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.mountreach.campusmanagementsystem.Database.DBHelper;
import com.mountreach.campusmanagementsystem.R;

import java.io.ByteArrayOutputStream;

public class ComplaintActivity extends AppCompatActivity {

    Spinner spinnerComplaintType;
    EditText etTitle, etDescription;
    ImageView ivSelectedImage,btnbackward;
    Button btnChooseImage, btnCaptureImage, btnSubmit,btn;
    Uri selectedImageUri;
    DBHelper dbHelper;
    static final int PICK_IMAGE = 1;
    static final int CAPTURE_IMAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        spinnerComplaintType = findViewById(R.id.spinner_complaint_type);
        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        ivSelectedImage = findViewById(R.id.iv_selected_image);
        btnbackward = findViewById(R.id.btnbackward);
        btnChooseImage = findViewById(R.id.btn_choose_image);
        btnCaptureImage = findViewById(R.id.btn_capture_image);
        btnSubmit = findViewById(R.id.btn_submit);

        Button btnViewComplaints = findViewById(R.id.btn_view_complaints);

        btnViewComplaints.setOnClickListener(v -> {
            Intent intent = new Intent(ComplaintActivity.this, ViewComplaintsActivity.class);
            startActivity(intent);
        });

        btnbackward.setOnClickListener(v -> {
            onBackPressed();   // go back to previous screen
        });
        dbHelper = new DBHelper(this);

        String[] types = {"Infrastructure", "Faculty", "Hostel", "Library", "Canteen"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                types
        );

        spinnerComplaintType.setAdapter(adapter);

        btnChooseImage.setOnClickListener(v -> openGallery());

        btnCaptureImage.setOnClickListener(v -> openCamera());

        btnSubmit.setOnClickListener(v -> saveComplaint());
    }

    private void openGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, PICK_IMAGE);
    }

    private void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, CAPTURE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == PICK_IMAGE && data != null) {

                selectedImageUri = data.getData();
                ivSelectedImage.setImageURI(selectedImageUri);
                ivSelectedImage.setVisibility(View.VISIBLE);

            }

            if (requestCode == CAPTURE_IMAGE && data != null) {

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                selectedImageUri = getImageUri(bitmap);

                ivSelectedImage.setImageBitmap(bitmap);
                ivSelectedImage.setVisibility(View.VISIBLE);
            }
        }
    }

    private Uri getImageUri(Bitmap bitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "Complaint",
                null
        );

        return Uri.parse(path);
    }

    private void saveComplaint() {

        String type = spinnerComplaintType.getSelectedItem().toString();
        String title = etTitle.getText().toString();
        String desc = etDescription.getText().toString();

        String imageUri = "";

        if (selectedImageUri != null) {
            imageUri = selectedImageUri.toString();
        }

        if (title.isEmpty() || desc.isEmpty()) {

            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = dbHelper.insertComplaint(type, title, desc, imageUri);

        if (result) {

            Toast.makeText(this, "Complaint Submitted", Toast.LENGTH_SHORT).show();

            etTitle.setText("");
            etDescription.setText("");
            ivSelectedImage.setVisibility(View.GONE);

        } else {

            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }
}