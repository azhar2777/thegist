package com.thegistapp.thegistapp.async;

import android.content.Context;
import android.util.Log;

import com.thegistapp.thegistapp.consts.Consts;
import com.thegistapp.thegistapp.listener.UserSelectionsListener;
import com.thegistapp.thegistapp.listener.VolleyCallback;
import com.thegistapp.thegistapp.model.UserSelections;
import com.thegistapp.thegistapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Copyright - All Rights Reserved
 * Created by Mohammad Azharuddin on 04/03/2020.
 * Email mailsahil07@gmail.com
 */
public class UserSelectionTask {
    Context mContext;
    private String requestString;
    private String TAG = "UserSelectionTaskTAG";
    public UserSelectionsListener mListener;
    private ArrayList<UserSelections> selections = new ArrayList<UserSelections>();
    public UserSelectionTask(Context mContext, String requestString){
        this.mContext = mContext;
        this.requestString = requestString;
        setUserSelection();
    }

    public void setUserSelection(){

        Util.postWithVolley(Consts.BASE_URL + Consts.SET_USER_SELECTION, requestString, mContext, new VolleyCallback() {
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


                UserSelections selection = new UserSelections();
                selection.setErrorCode(jObj.optInt("error_code"));
                selection.setUserId(jObj.optInt("user_id"));
                selection.setMessage(jObj.optString("meesage"));
                selections.add(selection);
                mListener.userSelectionsCallBack(selections);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
