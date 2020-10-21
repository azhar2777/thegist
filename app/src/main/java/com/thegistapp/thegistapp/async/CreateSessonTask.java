package com.thegistapp.thegistapp.async;

import android.content.Context;
import android.util.Log;

import com.thegistapp.thegistapp.consts.Consts;
import com.thegistapp.thegistapp.listener.CreateSessonListener;
import com.thegistapp.thegistapp.listener.VolleyCallback;
import com.thegistapp.thegistapp.model.CreateSesson;
import com.thegistapp.thegistapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Copyright - All Rights Reserved
 * Created by Mohammad Azharuddin on 04/03/2020.
 * Email mailsahil07@gmail.com
 */
public class CreateSessonTask {
    Context mContext;

    private String requestString;
    private String TAG = "ReactStoryTask";
    public CreateSessonListener mListener;
    private ArrayList<CreateSesson> createSessons = new ArrayList<CreateSesson>();
    public CreateSessonTask(Context mContext, String requestString){
        this.mContext = mContext;
        this.requestString = requestString;

        doNetworkTask();
    }

    public void doNetworkTask(){

        Util.postWithVolley(Consts.START_USER_SESSON, requestString, mContext, new VolleyCallback() {
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


                CreateSesson createSesson = new CreateSesson();
                createSesson.setErrorCode(jObj.optInt("error_code"));
                createSesson.setCreatedSessonId(jObj.optInt("created_sesson_id"));

                createSessons.add(createSesson);
                mListener.createSessonCallBack(createSessons);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
