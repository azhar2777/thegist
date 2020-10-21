/*
 *
 *
 *
 */

package com.thegistapp.thegistapp.model;

import java.io.Serializable;

public class UserClass implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private String userFullName;
	private String UserId;
	private String userEmoji;
	private boolean isLoggedIn;
	private boolean hasSelection;
	String userCity;
	String userState;

	private String fontSize;
	private boolean isNotification;
	private boolean isHDImages;

	public boolean isHasSelection() {
		return hasSelection;
	}

	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	public boolean isNotification() {
		return isNotification;
	}

	public void setNotification(boolean notification) {
		isNotification = notification;
	}

	public boolean isHDImages() {
		return isHDImages;
	}

	public void setHDImages(boolean HDImages) {
		isHDImages = HDImages;
	}

	public boolean hasSelection() {
		return hasSelection;
	}

	public void setHasSelection(boolean hasSelection) {
		this.hasSelection = hasSelection;
	}



	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	private String userEmail;





	public String getUserFullName() {
		return userFullName;
	}
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public String getUserEmoji() {
		return userEmoji;
	}

	public void setUserEmoji(String userEmoji) {
		this.userEmoji = userEmoji;
	}



}
