package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.mountreach.campusmanagementsystem.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class PdfViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String pdfName = getIntent().getStringExtra("pdf_name");

        try {
            File file = new File(getCacheDir(), pdfName);

            if (!file.exists()) {
                InputStream is = getAssets().open(pdfName);
                FileOutputStream fos = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }

                fos.close();
                is.close();
            }

            Uri uri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    file
            );

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);

            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
