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
    public boolean checkEmail(String Email){
        if (Email.length()>5){
            return true;
        }
        else return false;
    }
    public boolean checkFirstName(String FirstName){
        if (FirstName.length()>0){
            return true;
        }
        else return false;
    }
    public boolean checkLastName(String LastName){
        if (LastName.length()>0){
            return true;
        }
        else return false;
    }
    public boolean checkUserClient(String UserType){
        if (UserType.equals("client")){
            return true;
        }
        else return false;
    }
    public String getEmail(){
        return email;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getUserType(){
        return userType;
    }

}
