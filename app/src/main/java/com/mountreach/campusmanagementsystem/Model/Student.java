package com.mountreach.campusmanagementsystem.Model;

public class Student {
    private int roll;
    private String enroll;
    private String name;
    private boolean isPresent;

    public Student(int roll, String enroll, String name, boolean isPresent) {
        this.roll = roll;
        this.enroll = enroll;
        this.name = name;
        this.isPresent = isPresent;
    }

    // getters and setters
    public int getRoll() { return roll; }
    public String getEnroll() { return enroll; }
    public String getName() { return name; }
    public boolean isPresent() { return isPresent; }
    public void setPresent(boolean present) { isPresent = present; }
}
