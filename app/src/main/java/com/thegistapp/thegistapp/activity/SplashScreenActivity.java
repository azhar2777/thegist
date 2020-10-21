package com.thegistapp.thegistapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.thegistapp.thegistapp.R;
import com.thegistapp.thegistapp.util.KpiInfo;
import com.thegistapp.thegistapp.util.Util;

public class SplashScreenActivity extends AppCompatActivity {
    private Context mContext;

    private String TAG = "SplashScreenActivityTAG";
    SharedPreferences settings;
    String CONTROL_PREFS = "";
    String enableNightMode = "";
    KpiInfo kpiInfo;
    String mDeviceToken = "";
    Boolean isSessonstarted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mContext    =   SplashScreenActivity.this;

        if(savedInstanceState == null){
            // everything else that doesn't update UI
            Log.v(TAG, "ONCREATE called" +isSessonstarted);
            if (Util.fetchUserClass(mContext) != null && Util.fetchUserClass(mContext).getUserId() != null) {
                isSessonstarted = false;
                if (!isSessonstarted) {
                    Log.v(TAG, "Creating session");
                    kpiInfo =   new KpiInfo(mContext);
                    kpiInfo.createSesson(Util.fetchUserClass(mContext).getUserId());
                    isSessonstarted = true;
                }
            }
        }

        if(!isSessonstarted) {

            isSessonstarted = true;
        }
//


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                mDeviceToken = instanceIdResult.getToken();

                Log.v(TAG, "device_token "+mDeviceToken);
            }
        });



        CONTROL_PREFS = ""+getResources().getString(R.string.gist_control_prefs);
        settings = getSharedPreferences(CONTROL_PREFS, MODE_PRIVATE);
        enableNightMode    = settings.getString("enable_night_mode", ""+getResources().getString(R.string.ctrl_scr_night_mode_no));
        if(enableNightMode.equals(""+getResources().getString(R.string.ctrl_scr_night_mode_no))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }




        if(getIntent() !=null) {
            String notificationType = getIntent().getStringExtra("notification_type");
//            String hostName = getIntent().getData().getHost();

//            //Log.v("HostName", "host "+hostName);
//            if(this.getIntent().getData() != null) {
//                Uri uri = this.getIntent().getData();
//                try {
//                    URL url = new URL(uri.getScheme(), uri.getHost(), uri.getPath());
//                    Log.v("HostName", "host " + uri.getHost());
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//            }



            if(notificationType != null  && !notificationType.isEmpty()){
                if(notificationType.equals("Story")){
                    String strStoryId  =  getIntent().getStringExtra("story_id");
                    int storyId   =   Integer.parseInt(strStoryId);
                    if(storyId > 0){
                        Intent intentPushNotification = new Intent(mContext,  StoryListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("story_id", storyId);
                        intentPushNotification.putExtras(bundle);
                        startActivity(intentPushNotification);
                        return;
                    }
                }

            }
        }



        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
//                    Log.v(TAG,""+Util.fetchUserClass(mContext).getUserId());
                    //Intent splashIntent = new Intent(mContext, MainCategoryActivity.class);
                    if (Util.fetchUserClass(mContext) != null && Util.fetchUserClass(mContext).getUserId() != null) {
//                        if(!isSessonstarted) {
//                            kpiInfo.createSesson(Util.fetchUserClass(mContext).getUserId());
//                            isSessonstarted = true;
//                        }


                        if(Util.fetchUserClass(mContext).hasSelection()){
                            //Intent splashIntent = new Intent(mContext, NavActivity.class);
                            //Intent splashIntent = new Intent(mContext, NewsNewActivity.class);
                            Intent splashIntent = new Intent(mContext, StoryListActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putString("device_token", ""+mDeviceToken);
                            splashIntent.putExtras(bundle);

                            startActivity(splashIntent);
                            finish();
                        }
                        else{
                            Intent splashIntent = new Intent(mContext, MainCategoryActivity.class);
                            startActivity(splashIntent);
                            finish();
                        }

                    }
                    else {
                        Intent splashIntent = new Intent(mContext, WelcomeActivity.class);
                        startActivity(splashIntent);
                        finish();
                    }
                    /*Intent splashIntent = new Intent(mContext, GetStartedActivity.class);
                    startActivity(splashIntent);
                    finish();*/
                }
            }

        };

        timer.start();

    }


    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        int currentSessonId    = settings.getInt("current_sesson_id", 0);

//        if(currentSessonId > 0){
//            kpiInfo.endSesson(currentSessonId);
//        }
//
//        Log.v(TAG, "Current sesson "+currentSessonId);
    }
}
