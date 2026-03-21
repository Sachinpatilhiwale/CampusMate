package com.mountreach.campusmanagementsystem.Model;

public class LeaveModel {
    public String studentName, studentEmail, rollNo, duration, reason, status, branch, year, key;

    public LeaveModel() {} // Required for Firebase

    public LeaveModel(String studentName, String studentEmail, String rollNo, String duration, String reason, String status, String branch, String year) {
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.rollNo = rollNo;
        this.duration = duration;
        this.reason = reason;
        this.status = status;
        this.branch = branch;
        this.year = year;
    }
}