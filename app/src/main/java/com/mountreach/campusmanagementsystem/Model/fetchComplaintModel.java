
package com.mountreach.campusmanagementsystem.Model;

public class fetchComplaintModel {

    String name, complaint, status, date;

    public fetchComplaintModel(String name, String complaint, String status, String date) {
        this.name = name;
        this.complaint = complaint;
        this.status = status;
        this.date = date;
    }

    public String getName() { return name; }
    public String getComplaint() { return complaint; }
    public String getStatus() { return status; }
    public String getDate() { return date; }
}