/*
 *
 * Login.java of CleanerApp for Properhands.
 *
 * Copyright (C) ProperHands Pte. Ltd - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Developed by vGrow Tech (http://vgrow.tech)
 * vijendra created/modified on 4/7/16 11:47 PM
 *
 */

package com.thegistapp.thegistapp.model;

import java.io.Serializable;

public class GetStory implements Serializable {
    private int errorCode;
    private String message;
    private String stories;
    private String userSelections;
    private String dailyQuote;
    private String iconCategory;
    private String adData;

    public int getShowAdAfter() {
        return showAdAfter;
    }

    public void setShowAdAfter(int showAdAfter) {
        this.showAdAfter = showAdAfter;
    }

    private int showAdAfter;

    public String getAdData() {
        return adData;
    }

    public void setAdData(String adData) {
        this.adData = adData;
    }



    public String getDailyQuote() {
        return dailyQuote;
    }

    public void setDailyQuote(String dailyQuote) {
        this.dailyQuote = dailyQuote;
    }


    public String getUserSelections() {
        return userSelections;
    }

    public void setUserSelections(String userSelections) {
        this.userSelections = userSelections;
    }

    public String getIconCategory() {
        return iconCategory;
    }

    public void setIconCategory(String iconCategory) {
        this.iconCategory = iconCategory;
    }


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStories() {
        return stories;
    }

    public void setStories(String stories) {
        this.stories = stories;
    }
}
