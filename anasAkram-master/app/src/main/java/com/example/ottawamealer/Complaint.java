package com.example.ottawamealer;

import java.io.Serializable;

public class Complaint implements Serializable {
    private String cookName;
    private String complaint;
    private String timeOfComplaint;

    public Complaint(){

    }
    public Complaint(String cookName, String complaint) {
        this.cookName = cookName;
        this.complaint = complaint;
        long currentTime = System.currentTimeMillis();
        long crntTimeMod = currentTime% 1000000000 ;
        timeOfComplaint = String.valueOf(crntTimeMod);

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

    public String getTimeOfComplaint() {return timeOfComplaint;}
}
