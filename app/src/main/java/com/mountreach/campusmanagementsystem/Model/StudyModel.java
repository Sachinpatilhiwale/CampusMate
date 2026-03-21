package com.mountreach.campusmanagementsystem.Model;

public class StudyModel {
    private String id;
    private String title;
    private String url;

    // Empty constructor required for Firebase
    public StudyModel() {}

    public StudyModel(String id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getUrl() { return url; }
}