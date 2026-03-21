package com.mountreach.campusmanagementsystem.Model;

public class ComplaintModel {
    // Add teacherMessage and studentEmail here
    public String title, description, type, studentName, studentEmail, rollNo, branch, year, status, teacherMessage, key;

    public ComplaintModel() {} // Required for Firebase

    public ComplaintModel(String title, String description, String type, String studentName,
                          String studentEmail, String rollNo, String branch, String year) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.rollNo = rollNo;
        this.branch = branch;
        this.year = year;
        this.status = "Pending";
        this.teacherMessage = ""; // Default empty
    }
}