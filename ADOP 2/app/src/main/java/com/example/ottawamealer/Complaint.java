package com.example.ottawamealer;

public class Complaint {
    private String cookName;
    private String complaint;

    public Complaint(){

    }

    public Complaint(String cookName, String complaint) {
        this.cookName = cookName;
        this.complaint = complaint;
    }


    public String getCookName() {
        return cookName;
    }

    public void setCookName(String cookName) {
        this.cookName = cookName;
    }

    public String getComplaint() {return complaint;}

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }




}
