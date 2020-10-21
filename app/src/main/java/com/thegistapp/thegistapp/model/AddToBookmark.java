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

public class AddToBookmark implements Serializable {
    private int errorCode;


    private int bookmarkedRemoved;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getBookmarkedRemoved() {
        return bookmarkedRemoved;
    }

    public void setBookmarkedRemoved(int bookmarkedRemoved) {
        this.bookmarkedRemoved = bookmarkedRemoved;
    }

}
