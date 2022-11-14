package com.example.ottawamealer;

import java.util.ArrayList;

public class User {
    public String firstName, lastName, email, fullName;

    public User(){
    }
    public User(String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        fullName = firstName+" "+lastName;

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

    public String getEmail(){
        return email;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName() {
        fullName = firstName + " "+lastName;
    }

}
