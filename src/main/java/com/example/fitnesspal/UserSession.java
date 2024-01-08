package com.example.fitnesspal;
public class UserSession {
    private static UserSession instance;
    private int loggedInUserId;
    private String loggedInUsername;

    private UserSession() {
        // private constructor to enforce singleton pattern
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public int getLoggedInUserId() {
        return loggedInUserId;
    }

    public void setLoggedInUserId(int userId) {
        loggedInUserId = userId;
    }

    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    public void setLoggedInUsername(String username) {
        loggedInUsername = username;
    }
}
