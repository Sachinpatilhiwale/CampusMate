package com.mountreach.campusmanagementsystem.Model;

public class TimetableModel {
    public String subject, teacher, time, room, day, key, date;

    public TimetableModel() {} // Required for Firebase

    public TimetableModel(String subject, String teacher, String time, String room, String day, String date) {
        this.subject = subject;
        this.teacher = teacher;
        this.time = time;
        this.room = room;
        this.day = day;
        this.date = date;
    }
}