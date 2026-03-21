package com.mountreach.campusmanagementsystem.Model;

public class AttendanceModel {
    public String name;
    public String roll;
    public String status; // "Present" or "Absent"

    public AttendanceModel() {} // Needed for Firebase

    public AttendanceModel(String name, String roll, String status){
        this.name = name;
        this.roll = roll;
        this.status = status;
    }
}