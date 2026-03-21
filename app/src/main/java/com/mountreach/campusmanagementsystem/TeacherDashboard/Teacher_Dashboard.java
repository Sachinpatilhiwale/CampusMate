package com.mountreach.campusmanagementsystem.TeacherDashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mountreach.campusmanagementsystem.R;
import com.mountreach.campusmanagementsystem.StudentDashboard.SettingActivity;
import com.mountreach.campusmanagementsystem.Teacher_Fragment.Teacher_HomeFragment;
import com.mountreach.campusmanagementsystem.Teacher_Fragment.Teacher_MyprofileFragment;
import com.mountreach.campusmanagementsystem.Teacher_Fragment.Teacher_NoticeFragment;

public class Teacher_Dashboard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    // Initialize fragments
    Teacher_HomeFragment homeFragment = new Teacher_HomeFragment();
    Teacher_NoticeFragment noticeFragment = new Teacher_NoticeFragment();
    Teacher_MyprofileFragment myprofileFragment = new Teacher_MyprofileFragment();

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_dashboard);

        setTitle("CampusMate (Teacher)");

        bottomNavigationView = findViewById(R.id.homebotttomnavigationview);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        // --- Load Default Home Fragment ---
        // We use a check for savedInstanceState to prevent overlapping on rotation
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.teacherhomeframelayout, homeFragment)
                    .commit();

            // Set the default selected item in the bottom bar
            bottomNavigationView.setSelectedItemId(R.id.teacherbottommenuhome);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menusetting) {
            startActivity(new Intent(Teacher_Dashboard.this, SettingActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.teacherbottommenuhome) {
            loadFragment(homeFragment);
            return true;
        } else if (id == R.id.teacherbottommenunotice) {
            loadFragment(noticeFragment);
            return true;
        } else if (id == R.id.teacherbottommenuprofile) {
            loadFragment(myprofileFragment);
            return true;
        }

        return false;
    }

    // Helper method to keep code clean
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.teacherhomeframelayout, fragment)
                .commit();
    }
}