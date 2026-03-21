package com.mountreach.campusmanagementsystem.TeacherDashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mountreach.campusmanagementsystem.Model.StudyModel;
import com.mountreach.campusmanagementsystem.R;

public class Teacher_StudyMaterialActivity extends AppCompatActivity {

    private static final int PICK_PDF_REQUEST = 100;
    private Uri pdfUri;
    private EditText edtTitle;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_study_material);

        edtTitle = findViewById(R.id.edtTitle);
        Button btnSelectPDF = findViewById(R.id.btnSelectPDF);
        Button btnUpload = findViewById(R.id.btnUpload);
        ImageView btnBack = findViewById(R.id.btnBack);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading PDF...");

        btnBack.setOnClickListener(v -> finish());
        btnSelectPDF.setOnClickListener(v -> selectPdf());
        btnUpload.setOnClickListener(v -> validateAndUpload());
    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            Toast.makeText(this, "PDF Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void validateAndUpload() {
        String title = edtTitle.getText().toString().trim();
        if (pdfUri == null) {
            Toast.makeText(this, "Please select a PDF", Toast.LENGTH_SHORT).show();
        } else if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
        } else {
            uploadToFirebase(title);
        }
    }

    private void uploadToFirebase(String title) {
        progressDialog.show();
        // Use timestamp to keep filenames unique
        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("study_materials/" + System.currentTimeMillis() + ".pdf");

        storageRef.putFile(pdfUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    saveToDatabase(title, uri.toString());
                }))
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveToDatabase(String title, String url) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("pdfs");
        String id = dbRef.push().getKey();

        StudyModel studyModel = new StudyModel(id, title, url);

        if (id != null) {
            dbRef.child(id).setValue(studyModel).addOnCompleteListener(task -> {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                    edtTitle.setText("");
                    pdfUri = null;
                }
            });
        }
    }
}