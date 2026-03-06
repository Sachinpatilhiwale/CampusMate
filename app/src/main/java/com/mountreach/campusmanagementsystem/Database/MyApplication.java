package com.mountreach.campusmanagementsystem.Database;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize your DBHelper globally (optional)
        DBHelper db = new DBHelper(getApplicationContext());
    }
}
