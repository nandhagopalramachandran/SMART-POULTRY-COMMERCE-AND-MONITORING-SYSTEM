package com.example.orderpage;

public class User {
    private String name;
    private String mobileNumber;
    private String address;

    public User(String name, String mobileNumber, String address) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.address = address;
    }

    // Getters for user information
    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getAddress() {
        return address;
    }
}
