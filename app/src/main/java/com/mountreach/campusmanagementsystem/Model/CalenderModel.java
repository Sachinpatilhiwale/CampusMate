package com.mountreach.campusmanagementsystem.Model;

public class CalenderModel {
    private String id;
    private String title;
    private String url;

    public CalenderModel(String id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
