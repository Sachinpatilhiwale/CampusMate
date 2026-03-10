package com.mountreach.campusmanagementsystem.Model;

public class ComplaintModel {

    String type;
    String title;
    String description;
    String imageUri;

    public ComplaintModel(String type, String title, String description, String imageUri) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.imageUri = imageUri;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUri() {
        return imageUri;
    }
}