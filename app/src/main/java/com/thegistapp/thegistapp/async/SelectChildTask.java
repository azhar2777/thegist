package com.thegistapp.thegistapp.async;

import android.content.Context;
import android.util.Log;

import com.thegistapp.thegistapp.consts.Consts;
import com.thegistapp.thegistapp.listener.SelectedChildListener;
import com.thegistapp.thegistapp.listener.VolleyCallback;
import com.thegistapp.thegistapp.model.SelectedChild;
import com.thegistapp.thegistapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Copyright - All Rights Reserved
 * Created by Mohammad Azharuddin on 04/03/2020.
 * Email mailsahil07@gmail.com
 */
public class SelectChildTask {
    Context mContext;
    private String requestString;
    private String TAG = "SelectChildTaskTAG";
    public SelectedChildListener mListener;
    private ArrayList<SelectedChild> selectedChildren = new ArrayList<SelectedChild>();
    public SelectChildTask(Context mContext, String requestString){
        this.mContext = mContext;
        this.requestString = requestString;
        getIconCategories();
    }

    public void getIconCategories(){

        Util.postWithVolley(Consts.BASE_URL + Consts.SELECT_CHILD, requestString, mContext, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                parseResponse(result);
            }
        });

    }

    private void parseResponse(String strResponse) {
        Log.v("strResponse", ""+strResponse);
        try {

            if(strResponse !="") {
                Log.v("" + TAG, "" + strResponse);

                JSONObject jObj = new JSONObject(strResponse);


                SelectedChild selectedChild = new SelectedChild();
                selectedChild.setErrorCode(jObj.optInt("error_code"));
                selectedChild.setChildCategoryData(jObj.optString("cat_data"));
                selectedChildren.add(selectedChild);
                mListener.selectedChildCallBack(selectedChildren);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
