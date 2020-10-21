/**
 * Created by Md Azharuddin on 03/03/2020.
 * Email mailsahil07@gmail.com
 * Developed by Symmetrix Systems
 */
package com.thegistapp.thegistapp.listener;


import com.thegistapp.thegistapp.model.RegisterUser;

import java.util.ArrayList;


public interface RegisterUserListener {
    void registerUserCallBack(ArrayList<RegisterUser> registerUsers);
}
