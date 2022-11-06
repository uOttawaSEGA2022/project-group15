package com.example.ottawamealer;

public class User {
    public String firstName, lastName, email, userType, cookStatus, complaints;

    public User(){
    }
    public User(String firstName, String lastName, String email, String userType){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userType = userType;

    }
    public User(String firstName, String lastName, String email, String userType, String cookStatus, String complaints){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userType = userType;
        this.cookStatus = cookStatus;
        this.complaints = complaints;


    }
}
