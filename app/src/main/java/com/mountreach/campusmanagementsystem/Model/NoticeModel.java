package com.mountreach.campusmanagementsystem.Model;

public class NoticeModel {

    String title, desc;
    Integer image;   // Nullable

    // With image
    public NoticeModel(String title, String desc, int image) {
        this.title = title;
        this.desc = desc;
        this.image = image;
    }

    // Without image
    public NoticeModel(String title, String desc) {
        this.title = title;
        this.desc = desc;
        this.image = null;
    }

    public String getTitle() { return title; }
    public String getDesc() { return desc; }
    public Integer getImage() { return image; }
}
