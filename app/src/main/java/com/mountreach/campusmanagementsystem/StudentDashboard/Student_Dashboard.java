package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mountreach.campusmanagementsystem.Student_Fragment.HomeFragment;
import com.mountreach.campusmanagementsystem.Student_Fragment.MyprofileFragment;
import com.mountreach.campusmanagementsystem.Student_Fragment.NoticeFragment;
import com.mountreach.campusmanagementsystem.R;

import java.util.Locale;

public class Student_Dashboard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    HomeFragment homeFragment = new HomeFragment();
    NoticeFragment noticeFragment = new NoticeFragment();
    MyprofileFragment myprofileFragmnet = new MyprofileFragment();
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_dashboard);

        setTitle("CampusMate");

        bottomNavigationView = findViewById(R.id.homebotttomnavigationview);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.homeframelayout, homeFragment)
                .commit();

        bottomNavigationView.setSelectedItemId(R.id.bottommenuhome);

        SharedPreferences langPrefs = getSharedPreferences("language", MODE_PRIVATE);
        String lang = langPrefs.getString("app_lang", "en");

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());


    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menusetting) {
            Intent intent = new Intent(Student_Dashboard.this,SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.bottommenuhome) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeframelayout, homeFragment).commit();
        } else if (item.getItemId() == R.id.bottommenunotice) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeframelayout, noticeFragment).commit();
        } else if (item.getItemId() == R.id.bottommenuprofile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeframelayout, myprofileFragmnet).commit();
        }
        return true;
    }
}