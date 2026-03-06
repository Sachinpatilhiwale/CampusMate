package com.mountreach.campusmanagementsystem.TeacherDashboard;

import android.content.Intent;
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
import com.mountreach.campusmanagementsystem.StudentDashboard.SettingActivity;
import com.mountreach.campusmanagementsystem.Teacher_Fragment.Teacher_HomeFragment;
import com.mountreach.campusmanagementsystem.Teacher_Fragment.Teacher_MyprofileFragment;
import com.mountreach.campusmanagementsystem.Teacher_Fragment.Teacher_NoticeFragment;

public class Teacher_Dashboard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    Teacher_HomeFragment homeFragment = new Teacher_HomeFragment();
    Teacher_NoticeFragment noticeFragment = new Teacher_NoticeFragment();
    Teacher_MyprofileFragment myprofileFragmnet = new Teacher_MyprofileFragment();
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_dashboard);

        setTitle("CampusMate");

        bottomNavigationView = findViewById(R.id.homebotttomnavigationview);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.teacherhomeframelayout, homeFragment)
                .commit();

        bottomNavigationView.setSelectedItemId(R.id.bottommenuhome);

    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menusetting) {
            Intent intent = new Intent(Teacher_Dashboard.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.teacherbottommenuhome)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.teacherhomeframelayout,homeFragment).commit();
        }
        else if (item.getItemId() == R.id.teacherbottommenunotice)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.teacherhomeframelayout, noticeFragment).commit();
        }
        else if (item.getItemId() == R.id.teacherbottommenuprofile)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.teacherhomeframelayout, myprofileFragmnet).commit();
        }
        return true;
    }
}