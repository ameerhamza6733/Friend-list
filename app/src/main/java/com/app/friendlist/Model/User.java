package com.app.friendlist.Model;

public class User {
    private String email;
    private String phoneNumber;
    private String displayName;

    private String uID;
    private String password;

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getuID() {
        return uID;
    }




    public String getDisplayName() {
        return displayName;
    }



    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
