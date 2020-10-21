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

public class ReactToStory implements Serializable {
    private int errorCode;
    private int reactionType;

    private int storyId;
    private String message;

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }


    public int getReactionType() {
        return reactionType;
    }

    public void setReactionType(int reactionType) {
        this.reactionType = reactionType;
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


}
