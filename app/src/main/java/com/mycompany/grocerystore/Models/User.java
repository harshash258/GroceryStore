package com.mycompany.grocerystore.Models;

public class User {
    String userName;
    String userID;
    String phoneNumber;
    String profilePhotoUrl;
    String userEmail;
    String deviceId;

    public User(){}


    public String getDeviceId() {
        return deviceId;
    }

    public User(String userName, String userID, String phoneNumber, String profilePhotoUrl, String userEmail, String deviceId) {
        this.userName = userName;
        this.userID = userID;
        this.phoneNumber = phoneNumber;
        this.profilePhotoUrl = profilePhotoUrl;
        this.userEmail = userEmail;
        this.deviceId = deviceId;
    }

    public String getUserName() {
        return userName;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserID() {
        return userID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
