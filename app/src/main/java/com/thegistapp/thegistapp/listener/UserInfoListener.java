/**
 * Created by Md Azharuddin on 15/08/2020.
 * Email mailsahil07@gmail.com
 * Developed by Symmetrix Systems
 */
package com.thegistapp.thegistapp.listener;



import com.thegistapp.thegistapp.model.UserInfo;

import java.util.ArrayList;


public interface UserInfoListener {
    void userInfoCallback(ArrayList<UserInfo> userInfos);
}
