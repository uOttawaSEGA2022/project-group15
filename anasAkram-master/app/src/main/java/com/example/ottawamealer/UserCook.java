package com.example.ottawamealer;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserCook {
    private String firstName, lastName, email, cookStatus = "active", fullName, endDate ;
    boolean hasVoidCheck = false;

    public UserCook() {
    }

    public UserCook(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        fullName = firstName+" "+lastName;

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getFullName() {
        return fullName;
    }

    public void setFullName() {
        fullName = firstName + " "+lastName;
    }


    public String getCookStatus() {
        return cookStatus;
    }

    public void setCookStatus(String cookStatus) {
        this.cookStatus = cookStatus;
    }

    public void setCookStatusActive(){
        cookStatus = "active";
    }
    public void cookSuspended(){
        cookStatus = "temporarily suspended";
    }
    public void cookBanned(){
        cookStatus = "indefinitely suspended";
    }


    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean hasVoidCheck() {
        return hasVoidCheck;
    }
}
