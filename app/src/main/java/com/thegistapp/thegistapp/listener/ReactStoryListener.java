/**
 * Created by Md Azharuddin on 03/03/2020.
 * Email mailsahil07@gmail.com
 * Developed by Symmetrix Systems
 */
package com.thegistapp.thegistapp.listener;


import com.thegistapp.thegistapp.model.ReactToStory;

import java.util.ArrayList;


public interface ReactStoryListener {
    void reactStoryCallBack(ArrayList<ReactToStory> reactToStories);
}