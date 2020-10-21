/**
 * Created by Md Azharuddin on 12/03/2018 12:08 PM.
 * Email mailsahil07@gmail.com
 * Developed by Symmetrix Systems ( http://symmetrixsystems.com)
 */


package com.thegistapp.thegistapp.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.thegistapp.thegistapp.R;
import com.thegistapp.thegistapp.consts.Consts;
import com.thegistapp.thegistapp.custom.CustomAsynkLoader;
import com.thegistapp.thegistapp.listener.GetStoriesListener;
import com.thegistapp.thegistapp.model.GetStory;
import com.thegistapp.thegistapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetNewsTask extends AsyncTask<String, Void, String> {

    private CustomAsynkLoader mDialog;
    private Context mContext;
    private String TAG = "GetNewsTask";
    public GetStoriesListener mListener;
    private String requestString;
    boolean showLoader = true;
    private ArrayList<GetStory> stories = new ArrayList<GetStory>();

    public GetNewsTask(Context mContext, String requestString, boolean showLoader) {
        this.mContext = mContext;
        this.requestString = requestString;
        this.showLoader = showLoader;
        mDialog = new CustomAsynkLoader(mContext);
        mDialog.setTitle(R.string.app_name);



    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (showLoader == true && !mDialog.isDialogShowing())
            mDialog.ShowDialog();
    }

    @Override
    protected String doInBackground(String... params) {
        String response = "";
        Log.v("requestString", ""+requestString);
        try {
            response = Util.postMethodWay_json(
                    "" + Consts.BASE_URL + Consts.GET_STORIES, requestString, mContext);
            parseResponse(response);

        } catch (Exception e) {
            Log.v(TAG, ""+e.getLocalizedMessage());
            e.printStackTrace();
            parseResponse(response);

        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (mDialog.isDialogShowing())
            mDialog.DismissDialog();

        mListener.getStoriesCallBack(stories);
    }

    private void parseResponse(String strResponse) {

        try {

            if(strResponse !="") {
                Log.v("" + TAG, "" + strResponse);

                JSONObject jObj = new JSONObject(strResponse);

                GetStory getStory = new GetStory();
                getStory.setErrorCode(jObj.optInt("error_code"));
                getStory.setMessage(jObj.optString("meesage"));
                getStory.setStories(jObj.optString("stories"));
                getStory.setIconCategory(jObj.optString("icon_category"));
                getStory.setUserSelections(jObj.optString("user_selections"));
                getStory.setDailyQuote(jObj.optString("daily_quotes"));
                getStory.setAdData(jObj.optString("ads"));
                getStory.setShowAdAfter(jObj.optInt("show_ad_after"));
                stories.add(getStory);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
