package com.thegistapp.thegistapp.model;

/**
 * Copyright - All Rights Reserved
 * Created by Mohammad Azharuddin on 04/03/2020.
 * Email mailsahil07@gmail.com
 */
public class MainCategoryData {
    int id ;
    String catName, catImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }
}
