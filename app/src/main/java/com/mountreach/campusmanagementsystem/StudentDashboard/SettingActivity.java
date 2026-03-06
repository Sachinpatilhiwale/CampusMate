package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.mountreach.campusmanagementsystem.Database.DBHelper;
import com.mountreach.campusmanagementsystem.R;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    Switch switchDark;
    LinearLayout layoutLanguage, layoutEmail, layoutAbout, layoutHelp,layoutShare;
    TextView txtSignOut;

    ImageView imgWhatsApp, imgInstagram, imgFacebook;

    SharedPreferences sharedPreferences, langPrefs;
    SharedPreferences.Editor editor, langEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Load theme before UI
        sharedPreferences = getSharedPreferences("theme", MODE_PRIVATE);
        boolean darkMode = sharedPreferences.getBoolean("darkMode", false);

        if (darkMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Init Views
//        switchDark = findViewById(R.id.switchDark);
//        layoutLanguage = findViewById(R.id.layoutLanguage);
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutAbout = findViewById(R.id.layoutAbout);
        layoutHelp = findViewById(R.id.layoutHelp);
        layoutShare = findViewById(R.id.layoutShare);
        txtSignOut = findViewById(R.id.txtSignOut);

        imgWhatsApp = findViewById(R.id.imgWhatsApp);
        imgInstagram = findViewById(R.id.imgInstagram);
        imgFacebook = findViewById(R.id.imgFacebook);

        editor = sharedPreferences.edit();

        // Dark Mode Switch State
        switchDark.setChecked(darkMode);

        // 🌙 DARK MODE TOGGLE
        switchDark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("darkMode", true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("darkMode", false);
            }
            editor.apply();
        });

        // 🌐 LANGUAGE SELECTION
        layoutLanguage.setOnClickListener(v -> showLanguageDialog());

        // 📧 EMAIL SUPPORT
        layoutEmail.setOnClickListener(v -> {
            Intent email = new Intent(Intent.ACTION_SENDTO);
            email.setData(Uri.parse("mailto:sachinhiwale9552@gmail.com"));
            startActivity(email);
        });

        layoutAbout.setOnClickListener(v -> {

        });

        layoutHelp.setOnClickListener(v -> {

        });

        layoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String downloadLink = "https://drive.google.com/file/d/1IVfQn17AjF0GMUz2JLkIpH-IpCMuGl-7/view?usp=drive_link";
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Download this app: " + downloadLink);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });

        txtSignOut.setOnClickListener(v -> {
            new AlertDialog.Builder(SettingActivity.this)
                    .setTitle("Delete Account")
                    .setMessage("Are you sure you want to delete your account permanently?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        // Get logged-in email
                        SharedPreferences loginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                        String email = loginPrefs.getString("email", null);

                        if(email != null) {
                            DBHelper dbHelper = new DBHelper(SettingActivity.this);
                            boolean deleted = dbHelper.deleteUser(email);

                            if(deleted) {
                                Toast.makeText(SettingActivity.this, "Account deleted successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SettingActivity.this, "Failed to delete account.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Clear SharedPreferences
                        SharedPreferences.Editor editor = loginPrefs.edit();
                        editor.clear();
                        editor.apply();

                        // Go to login screen
                        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        // ************************************
        //           SOCIAL LINKS
        // ************************************

        // 📲 WHATSAPP
        imgWhatsApp.setOnClickListener(v -> {
            String number = "9588454311";

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wa.me/" + number));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        // 📸 INSTAGRAM
        imgInstagram.setOnClickListener(v -> {
            String username = "sachinpatil_03"; // Ex: sachin_hiwale

            Uri uri = Uri.parse("http://instagram.com/_u/" + username);
            Intent insta = new Intent(Intent.ACTION_VIEW, uri);
            insta.setPackage("com.instagram.android");

            try {
                startActivity(insta);
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://instagram.com/" + username)));
            }
        });

        // 👍 FACEBOOK
        imgFacebook.setOnClickListener(v -> {
            String pageId = "100080939854971"; // ex: mountreach.official

            try {
                Intent fbIntent = new Intent(Intent.ACTION_VIEW);
                fbIntent.setData(Uri.parse("fb://page/" + pageId));
                startActivity(fbIntent);
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/" + pageId)));
            }
        });

    }


    // 🌍 Language Popup
    private void showLanguageDialog() {
        String[] languages = {"English", "हिंदी", "मराठी"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Language");
        builder.setItems(languages, (dialog, which) -> {
            switch (which) {
                case 0: setLanguage("en"); break;
                case 1: setLanguage("hi"); break;
                case 2: setLanguage("mr"); break;
            }
        });
        builder.show();
    }


    // 🔥 Apply Language
    private void setLanguage(String langCode) {

        langPrefs = getSharedPreferences("language", MODE_PRIVATE);
        langEditor = langPrefs.edit();
        langEditor.putString("app_lang", langCode);
        langEditor.apply();

        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        recreate(); // refresh
    }
}
