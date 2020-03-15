package com.example.myapp;

/**
 * user object class to create user
 */
public class UserObject {

    private String FullName;
    private String Email;

    public UserObject(String fullName, String email, String address) {
        FullName = fullName;
        Email = email;
        Address = address;
    }

    private String Address;

    public UserObject(){
    }

    public UserObject(String fullName, String email) {
        FullName = fullName;
        Email = email;
    }

    public UserObject(String address) {
        Address = address;
    }


    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }


}
