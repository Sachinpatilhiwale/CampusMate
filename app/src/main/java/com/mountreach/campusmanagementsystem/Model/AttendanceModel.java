package com.mountreach.campusmanagementsystem.Model;

public class AttendanceModel {

    private String name;
    private String rollNo;
    private String enrollmentNo;
    private String status; // Present / Absent

    public AttendanceModel(String name, String rollNo, String enrollmentNo) {
        this.name = name;
        this.rollNo = rollNo;
        this.enrollmentNo = enrollmentNo;
        this.status = "Absent";
    }

    public String getName() { return name; }
    public String getRollNo() { return rollNo; }
    public String getEnrollmentNo() { return enrollmentNo; }
    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }
}