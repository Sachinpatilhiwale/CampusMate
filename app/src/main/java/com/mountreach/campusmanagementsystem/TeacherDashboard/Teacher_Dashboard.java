package com.mountreach.campusmanagementsystem.TeacherDashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Ensure you have a toolbar in layout
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mountreach.campusmanagementsystem.R;
import com.mountreach.campusmanagementsystem.StudentDashboard.LoginActivity;
import com.mountreach.campusmanagementsystem.StudentDashboard.SettingActivity;
import com.mountreach.campusmanagementsystem.Teacher_Fragment.Teacher_HomeFragment;
import com.mountreach.campusmanagementsystem.Teacher_Fragment.Teacher_MyprofileFragment;
import com.mountreach.campusmanagementsystem.Teacher_Fragment.Teacher_NoticeFragment;

public class Teacher_Dashboard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    // Fragments
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

        // 2. Setup Bottom Navigation
        bottomNavigationView = findViewById(R.id.homebotttomnavigationview);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        // 3. Load Home Fragment by default
        if (savedInstanceState == null) {
            loadFragment(homeFragment);
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
        int id = item.getItemId();

        if (id == R.id.menusetting) {
            startActivity(new Intent(Teacher_Dashboard.this, SettingActivity.class));
            return true;
        }
//        else if (id == R.id.menulogout) { // Assuming you add a logout item to your menu
//            logoutTeacher();
//            return true;
//        }

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

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.teacherhomeframelayout, fragment)
                .commit();
    }

    private void logoutTeacher() {
        // Clear all session data so the next person logging in doesn't see this teacher's department
        SharedPreferences prefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();

        Intent intent = new Intent(Teacher_Dashboard.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}