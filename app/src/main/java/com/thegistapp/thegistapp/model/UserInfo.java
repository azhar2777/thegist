package com.thegistapp.thegistapp.model;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int errorCode;
    private String userDetails;
    private String message;
    private double userRatingPoints;
    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(String userDetails) {
        this.userDetails = userDetails;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getUserRatingPoints() {
        return userRatingPoints;
    }

    public void setUserRatingPoints(double userRatingPoints) {
        this.userRatingPoints = userRatingPoints;
    }
}
