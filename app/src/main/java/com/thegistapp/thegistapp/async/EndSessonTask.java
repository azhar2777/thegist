package com.thegistapp.thegistapp.async;

import android.content.Context;
import android.util.Log;

import com.thegistapp.thegistapp.consts.Consts;
import com.thegistapp.thegistapp.listener.EndSessonListener;
import com.thegistapp.thegistapp.listener.VolleyCallback;
import com.thegistapp.thegistapp.model.EndSesson;
import com.thegistapp.thegistapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Copyright - All Rights Reserved
 * Created by Mohammad Azharuddin on 04/03/2020.
 * Email mailsahil07@gmail.com
 */
public class EndSessonTask {
    Context mContext;

    private String requestString;
    private String TAG = "ReactStoryTask";
    public EndSessonListener mListener;
    private ArrayList<EndSesson> endSessons = new ArrayList<EndSesson>();
    public EndSessonTask(Context mContext, String requestString){
        this.mContext = mContext;
        this.requestString = requestString;

        doNetworkTask();
    }

    public void doNetworkTask(){

        Util.postWithVolley(Consts.END_USER_SESSON, requestString, mContext, new VolleyCallback() {
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


                EndSesson endSesson = new EndSesson();
                endSesson.setErrorCode(jObj.optInt("error_code"));

                endSessons.add(endSesson);
                mListener.endSessonCallBack(endSessons);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
