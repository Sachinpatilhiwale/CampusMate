package com.mountreach.campusmanagementsystem.Model;

public class DashboardItem {
    int iconResId;
    String title;

    public DashboardItem(int iconResId, String title) {
        this.iconResId = iconResId;
        this.title = title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getTitle() {
        return title;
    }
}

