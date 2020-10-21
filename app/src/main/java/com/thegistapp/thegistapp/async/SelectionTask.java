package com.thegistapp.thegistapp.async;

import android.content.Context;
import android.util.Log;

import com.thegistapp.thegistapp.R;
import com.thegistapp.thegistapp.consts.Consts;
import com.thegistapp.thegistapp.custom.CustomAsynkLoader;
import com.thegistapp.thegistapp.listener.SelectionsListener;
import com.thegistapp.thegistapp.listener.VolleyCallback;
import com.thegistapp.thegistapp.model.Selections;
import com.thegistapp.thegistapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Copyright - All Rights Reserved
 * Created by Mohammad Azharuddin on 04/03/2020.
 * Email mailsahil07@gmail.com
 */
public class SelectionTask {
    Context mContext;
    private CustomAsynkLoader mDialog;
    private String requestString;
    private String TAG = "SelectionTaskTAG";
    public SelectionsListener mListener;
    private ArrayList<Selections> selections = new ArrayList<Selections>();
    public SelectionTask(Context mContext, String requestString){
        this.mContext = mContext;
        this.requestString = requestString;
        mDialog = new CustomAsynkLoader(mContext);
        mDialog.setTitle(R.string.app_name);
        getIconCategories();
    }

    public void getIconCategories(){
        if (!mDialog.isDialogShowing())
            mDialog.ShowDialog();
        Util.postWithVolley(Consts.BASE_URL + Consts.SELECTIONS, requestString, mContext, new VolleyCallback() {
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


                Selections selection = new Selections();
                selection.setErrorCode(jObj.optInt("error_code"));
                selection.setIconCategoryData(jObj.optString("icon_cat_data"));
                selection.setMainCategoryData(jObj.optString("main_cat_data"));
                selections.add(selection);
                mListener.selectionsCallBack(selections);
                if (mDialog.isDialogShowing())
                    mDialog.DismissDialog();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
