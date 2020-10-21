package com.thegistapp.thegistapp.model;

/**
 * Copyright - All Rights Reserved
 * Created by Mohammad Azharuddin on 04/03/2020.
 * Email mailsahil07@gmail.com
 */
public class StoryListData {
    int storyId;
    String storyTitle;
    String storyContent;
    String storyImage;
    int isVideo;
    String videoLink;
    String contentProviderLogo1, contentProviderLogo2, contentProviderLogo3;
    String contentProviderLink1;

    String contentProviderLink2;
    String contentProviderLink3;
    //POLL
    private int pollQuestionId;
    private String pollQuestion;
    private String pollOption1;
    private String pollOption2;

    private boolean isPhotographer;
    private String phographerName;
    private String writerName;
    private String editorName;

    private int storyReactionType;
    private int storyBookmarked;
    private int storyHearted;
    private String storyShareUrl;

    private int storyLovedCounter;

    public int getStoryBookmarked() {
        return storyBookmarked;
    }

    public void setStoryBookmarked(int storyBookmarked) {
        this.storyBookmarked = storyBookmarked;
    }

    public int getStoryHearted() {
        return storyHearted;
    }

    public void setStoryHearted(int storyHearted) {
        this.storyHearted = storyHearted;
    }

    public int getStoryReactionType() {
        return storyReactionType;
    }

    public void setStoryReactionType(int storyReactionType) {
        this.storyReactionType = storyReactionType;
    }

    public boolean isShowAd() {
        return showAd;
    }

    public void setShowAd(boolean showAd) {
        this.showAd = showAd;
    }

    private boolean showAd;

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }




    public String getStoryImage() {
        return storyImage;
    }

    public void setStoryImage(String storyImage) {
        this.storyImage = storyImage;
    }

    public int getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(int isVideo) {
        this.isVideo = isVideo;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }



    public String getContentProviderLogo1() {
        return contentProviderLogo1;
    }

    public void setContentProviderLogo1(String contentProviderLogo1) {
        this.contentProviderLogo1 = contentProviderLogo1;
    }

    public String getContentProviderLogo2() {
        return contentProviderLogo2;
    }

    public void setContentProviderLogo2(String contentProviderLogo2) {
        this.contentProviderLogo2 = contentProviderLogo2;
    }

    public String getContentProviderLogo3() {
        return contentProviderLogo3;
    }

    public void setContentProviderLogo3(String contentProviderLogo3) {
        this.contentProviderLogo3 = contentProviderLogo3;
    }



    public String getContentProviderLink1() {
        return contentProviderLink1;
    }

    public void setContentProviderLink1(String contentProviderLink1) {
        this.contentProviderLink1 = contentProviderLink1;
    }

    public String getContentProviderLink2() {
        return contentProviderLink2;
    }

    public void setContentProviderLink2(String contentProviderLink2) {
        this.contentProviderLink2 = contentProviderLink2;
    }

    public String getContentProviderLink3() {
        return contentProviderLink3;
    }

    public void setContentProviderLink3(String contentProviderLink3) {
        this.contentProviderLink3 = contentProviderLink3;
    }



    public boolean isPhotographer() {
        return isPhotographer;
    }

    public void setPhotographer(boolean photographer) {
        isPhotographer = photographer;
    }

    public String getPhographerName() {
        return phographerName;
    }

    public void setPhographerName(String phographerName) {
        this.phographerName = phographerName;
    }

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }


    public int getPollQuestionId() {
        return pollQuestionId;
    }

    public void setPollQuestionId(int pollQuestionId) {
        this.pollQuestionId = pollQuestionId;
    }

    public String getPollQuestion() {
        return pollQuestion;
    }

    public void setPollQuestion(String pollQuestion) {
        this.pollQuestion = pollQuestion;
    }

    public String getPollOption1() {
        return pollOption1;
    }

    public void setPollOption1(String pollOption1) {
        this.pollOption1 = pollOption1;
    }

    public String getPollOption2() {
        return pollOption2;
    }

    public void setPollOption2(String pollOption2) {
        this.pollOption2 = pollOption2;
    }
    //POLL



    public boolean isShowMenu() {
        return showMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    private boolean showMenu = false;

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public String getStoryContent() {
        return storyContent;
    }

    public void setStoryContent(String storyContent) {
        this.storyContent = storyContent;
    }


    public String getStoryShareUrl() {
        return storyShareUrl;
    }

    public void setStoryShareUrl(String storyShareUrl) {
        this.storyShareUrl = storyShareUrl;
    }

    public int getStoryLovedCounter() {
        return storyLovedCounter;
    }

    public void setStoryLovedCounter(int storyLovedCounter) {
        this.storyLovedCounter = storyLovedCounter;
    }
}

