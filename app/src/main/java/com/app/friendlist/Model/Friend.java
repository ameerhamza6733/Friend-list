package com.app.friendlist.Model;

public class Friend extends User {
    private boolean registerUser;

    public Friend() {
    }

    public boolean isRegisterUser() {
        return registerUser;
    }

    public void setRegisterUser(boolean registerUser) {
        this.registerUser = registerUser;
    }
}
