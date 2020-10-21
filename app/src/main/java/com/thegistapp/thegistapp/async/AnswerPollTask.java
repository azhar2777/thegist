package com.thegistapp.thegistapp.async;

import android.content.Context;
import android.util.Log;

import com.thegistapp.thegistapp.R;
import com.thegistapp.thegistapp.consts.Consts;
import com.thegistapp.thegistapp.custom.CustomAsynkLoader;
import com.thegistapp.thegistapp.listener.AnswerPollListener;
import com.thegistapp.thegistapp.listener.VolleyCallback;
import com.thegistapp.thegistapp.model.AnswerPoll;
import com.thegistapp.thegistapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Copyright - All Rights Reserved
 * Created by Mohammad Azharuddin on 04/03/2020.
 * Email mailsahil07@gmail.com
 */
public class AnswerPollTask {
    Context mContext;
    private CustomAsynkLoader mDialog;

    private String requestString;
    private String TAG = "AnswerPollTaskTAG";
    public AnswerPollListener mListener;
    private ArrayList<AnswerPoll> answerPolls = new ArrayList<AnswerPoll>();
    public AnswerPollTask(Context mContext, String requestString){
        this.mContext = mContext;
        this.requestString = requestString;
        mDialog = new CustomAsynkLoader(mContext);
        mDialog.setTitle(R.string.app_name);
        doNetworkTask();
    }

    public void doNetworkTask(){
        if (!mDialog.isDialogShowing())
            mDialog.ShowDialog();
        Util.postWithVolley(Consts.BASE_URL + Consts.POLL_ANSWER, requestString, mContext, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                parseResponse(result);
            }
        });

    }

    private void parseResponse(String strResponse) {
        Log.v(TAG, ""+strResponse);
        try {

            if(strResponse !="") {
                Log.v("" + TAG, "" + strResponse);

                JSONObject jObj = new JSONObject(strResponse);


                AnswerPoll answerPoll = new AnswerPoll();
                answerPoll.setErrorCode(jObj.optInt("error_code"));
                answerPoll.setMessage(jObj.optString("meesage"));
                answerPolls.add(answerPoll);
                mListener.answerPollCallBack(answerPolls);
                if (mDialog.isDialogShowing())
                    mDialog.DismissDialog();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
