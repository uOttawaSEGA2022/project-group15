package com.example.ottawamealer;

public class User {
    public String firstName, lastName, email, userType;

    public User(){
    }
    public User(String firstName, String lastName, String email, String userType){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userType = userType;

    }
}
