package com.thegistapp.thegistapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.thegistapp.thegistapp.R;
import com.thegistapp.thegistapp.async.CreateSessonTask;
import com.thegistapp.thegistapp.async.EndSessonTask;
import com.thegistapp.thegistapp.listener.CreateSessonListener;
import com.thegistapp.thegistapp.listener.EndSessonListener;
import com.thegistapp.thegistapp.model.CreateSesson;
import com.thegistapp.thegistapp.model.EndSesson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class KpiInfo implements CreateSessonListener, EndSessonListener {
    Context mContext;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    boolean isSessionCreated = false;
    public KpiInfo(Context mContext){
        this.mContext   =   mContext;


    }

    private String TAG  =   "KPIINFO_TAG";
    public void createSesson(String userId){
        Log.v(TAG, "createSesson called");
        JSONObject objCreateSesson =   new JSONObject();
        try {
            objCreateSesson.put("action", "create_sesson");
            objCreateSesson.put("user_id", userId);

            String requestCreateSesson  =   objCreateSesson.toString();

            Log.v(TAG, ""+requestCreateSesson);

            if(!isSessionCreated) {
                CreateSessonTask createSessonTask = new CreateSessonTask(mContext, requestCreateSesson);
                createSessonTask.mListener = this;
                isSessionCreated    =   true;
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void endSesson(int currentSesonId, int postViewed){
        JSONObject objEndSesson =   new JSONObject();
        try {
            objEndSesson.put("action", "end_sesson");
            objEndSesson.put("sesson_id", currentSesonId);
            objEndSesson.put("post_viewed", postViewed);

            String requestEndSesson  =   objEndSesson.toString();

            Log.v(TAG, ""+requestEndSesson);

            EndSessonTask endSessonTask = new EndSessonTask(mContext, requestEndSesson);
            endSessonTask.mListener  =   this;

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createSessonCallBack(ArrayList<CreateSesson> createSessons) {
        if(createSessons.size() > 0){
            int errorCode   =   createSessons.get(0).getErrorCode();
            int createSessonId  =   createSessons.get(0).getCreatedSessonId();

            settings = mContext.getSharedPreferences(mContext.getResources().getString(R.string.gist_control_prefs), 0);
            editor = settings.edit();

            editor.putInt("current_sesson_id", createSessonId);
            editor.apply();
            Log.v(TAG, "CREATE SESSON Error code : "+errorCode);
        }
        else{
            Log.v(TAG, "CREATE SESSON Something went wrong");
        }
    }

    @Override
    public void endSessonCallBack(ArrayList<EndSesson> endSessons) {
        if(endSessons.size() > 0){
            int errorCode   =   endSessons.get(0).getErrorCode();
            Log.v(TAG, "END SESSON Error code : "+errorCode);
        }
        else{
            Log.v(TAG, "END SESSON Something went wrong");
        }
    }


}
