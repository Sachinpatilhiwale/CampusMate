package com.mountreach.campusmanagementsystem.Model;

public class TimetableModel {
    public long id;
    public String subject, teacher, start, end, day, room;

    public TimetableModel(long id, String subject, String teacher,
                          String start, String end, String day, String room) {
        this.id = id;
        this.subject = subject;
        this.teacher = teacher;
        this.start = start;
        this.end = end;
        this.day = day;
        this.room = room;
    }
}
