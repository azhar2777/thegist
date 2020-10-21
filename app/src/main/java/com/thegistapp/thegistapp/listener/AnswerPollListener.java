/**
 * Created by Md Azharuddin on 03/03/2020.
 * Email mailsahil07@gmail.com
 * Developed by Symmetrix Systems
 */
package com.thegistapp.thegistapp.listener;


import com.thegistapp.thegistapp.model.AnswerPoll;

import java.util.ArrayList;


public interface AnswerPollListener {
    void answerPollCallBack(ArrayList<AnswerPoll> answerPolls);
}
