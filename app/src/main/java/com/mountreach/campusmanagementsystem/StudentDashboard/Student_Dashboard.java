package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.mountreach.campusmanagementsystem.Student_Fragment.HomeFragment;
import com.mountreach.campusmanagementsystem.Student_Fragment.MyprofileFragment;
import com.mountreach.campusmanagementsystem.Student_Fragment.NoticeFragment;
import com.mountreach.campusmanagementsystem.R;

import java.util.Locale;

public class Student_Dashboard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    HomeFragment homeFragment = new HomeFragment();
    NoticeFragment noticeFragment = new NoticeFragment();
    MyprofileFragment myprofileFragmnet = new MyprofileFragment();

    BottomNavigationView bottomNavigationView;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_home) {
                bottomNavigationView.setSelectedItemId(R.id.bottommenuhome);

            } else if (item.getItemId() == R.id.nav_profile) {
                bottomNavigationView.setSelectedItemId(R.id.bottommenuprofile);

            } else if (item.getItemId() == R.id.nav_logout) {

                SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
                prefs.edit().clear().apply();

                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.menusetting) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.bottommenuhome) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.homeframelayout, homeFragment)
                    .commit();

        } else if (item.getItemId() == R.id.bottommenunotice) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.homeframelayout, noticeFragment)
                    .commit();

        } else if (item.getItemId() == R.id.bottommenuprofile) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.homeframelayout, myprofileFragmnet)
                    .commit();
        }

        return true;
    }
}