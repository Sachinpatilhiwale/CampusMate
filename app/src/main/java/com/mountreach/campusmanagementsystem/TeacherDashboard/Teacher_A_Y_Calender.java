package com.mountreach.campusmanagementsystem.TeacherDashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mountreach.campusmanagementsystem.R;

public class Teacher_A_Y_Calender extends AppCompatActivity {

    private static final int PICK_PDF_REQUEST = 100;
    private Uri pdfUri;
    private EditText edtTitle;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_ay_calender); // your updated XML

        edtTitle = findViewById(R.id.edtTitle);
        emptyView = findViewById(R.id.emptyView);
        Button btnSelectPDF = findViewById(R.id.btnSelectPDF);
        Button btnUpload = findViewById(R.id.btnUpload);
        ImageView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish()); // go back

        btnSelectPDF.setOnClickListener(v -> selectPdf());

        btnUpload.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            if(pdfUri == null){
                Toast.makeText(this, "Please select a PDF", Toast.LENGTH_SHORT).show();
            } else if(title.isEmpty()){
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            } else {
                uploadPdfToFirebase(pdfUri, title);
            }
        });
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
        if(requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            pdfUri = data.getData();
            Toast.makeText(this, "PDF Selected: " + pdfUri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPdfToFirebase(Uri pdfUri, String title){
        String fileName = System.currentTimeMillis() + ".pdf";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("teacher_pdfs/" + fileName);

        storageRef.putFile(pdfUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    savePdfToDatabase(title, downloadUrl);
                }))
                .addOnFailureListener(e -> Toast.makeText(this, "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void savePdfToDatabase(String title, String downloadUrl){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("pdfs");
        String id = dbRef.push().getKey();

        PdfModel pdfModel = new PdfModel(title, downloadUrl);

        dbRef.child(id).setValue(pdfModel)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(this, "PDF Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        emptyView.setVisibility(View.GONE);
                        edtTitle.setText("");
                        pdfUri = null;
                    } else {
                        Toast.makeText(this, "Database Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Model class to store title + URL
    public static class PdfModel {
        public String title;
        public String url;

        public PdfModel() { }

        public PdfModel(String title, String url){
            this.title = title;
            this.url = url;
        }
    }
}