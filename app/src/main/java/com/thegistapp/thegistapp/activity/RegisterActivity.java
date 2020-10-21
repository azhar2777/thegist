package com.thegistapp.thegistapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.thegistapp.thegistapp.R;
import com.thegistapp.thegistapp.async.RegisterUserTask;
import com.thegistapp.thegistapp.listener.RegisterUserListener;
import com.thegistapp.thegistapp.model.RegisterUser;
import com.thegistapp.thegistapp.model.UserClass;
import com.thegistapp.thegistapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, RegisterUserListener {

    private Context mContext;
    private String TAG = "RegisterActivityTAG";
    private TextView tvStep1, tvStep1Text, tvNameTap, tvEmailTap, tvLocationTap;
    private EditText etName, etEmail, etCityName;

    private ConstraintLayout clRegFooter;
    private String mDeviceToken="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext    =   RegisterActivity.this;

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                mDeviceToken = instanceIdResult.getToken();

                Log.v(TAG, "device_token " + mDeviceToken);
            }
        });

        initView();
    }

    private void initView() {
        tvNameTap   =   (TextView) findViewById(R.id.tv_name_tap);
        tvEmailTap   =   (TextView) findViewById(R.id.tv_email_tap);
        tvLocationTap   =   (TextView) findViewById(R.id.tv_location_tap);

        etName      =   (EditText)findViewById(R.id.et_name);
        etEmail      =   (EditText)findViewById(R.id.et_email);
        etCityName      =   (EditText)findViewById(R.id.et_city_name);

        clRegFooter =   (ConstraintLayout)findViewById(R.id.cl_reg_footer);

        tvNameTap.setOnClickListener(this);
        tvEmailTap.setOnClickListener(this);
        clRegFooter.setOnClickListener(this);

        tvLocationTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etCityName.setVisibility(View.VISIBLE);
                etCityName.requestFocus();
            }
        });

        /*try {
            //===== IP =======
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo wifiinfo = wm.getConnectionInfo();
            byte[] myIPAddress = BigInteger.valueOf(wifiinfo.getIpAddress()).toByteArray();
// you must reverse the byte array before conversion. Use Apache's commons library
            //ArrayUtils.reverse(myIPAddress);



            Log.v(TAG, myIPAddress.toString());
            InetAddress myInetIP = InetAddress.getByAddress(myIPAddress);
            String myIP = myInetIP.getHostAddress();

            Log.v(TAG, "IP is "+myIP);
            //===== IP =======
        }
        catch (Exception e){
            e.printStackTrace();
        }*/






    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_name_tap :
               etName.setVisibility(View.VISIBLE);
               etName.requestFocus();

                break;

            case R.id.tv_email_tap :
                etEmail.setVisibility(View.VISIBLE);
                etEmail.requestFocus();

                break;
//            case R.id.tv_location_tap :
//                Toast.makeText(mContext, "Loaction", Toast.LENGTH_SHORT).show();
//                etCityName.setVisibility(View.VISIBLE);
//
//                break;
            case R.id.cl_reg_footer :
                registerUser();

                break;
            default:
                break;
        }
    }

    // ========== Register User =====
    private void  registerUser(){
        String name= etName.getText().toString();
        String email = etEmail.getText().toString();
        String cityName = etCityName.getText().toString();
        String mDeviceId = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if(name.equals("")){
            Util.showMessageWithOk(mContext, ""+getString(R.string.reg_name_error));
            return;
        }
        if(email.equals("")){
            Util.showMessageWithOk(mContext, ""+getString(R.string.reg_email_error));
            return;
        }
        if(cityName.equals("")){
            Util.showMessageWithOk(mContext, ""+getString(R.string.reg_city_error));
            return;
        }



        JSONObject objRegister = new JSONObject();
        try {
            objRegister.put("action", "create_user");
            objRegister.put("name", ""+name);
            objRegister.put("email", ""+email);
            objRegister.put("city_name", ""+cityName);
            objRegister.put("device_token", ""+mDeviceToken);
            objRegister.put("device_id", ""+mDeviceId);




            String requestRegister =    objRegister.toString();
            Log.v(TAG, requestRegister);

            RegisterUserTask registerUserTask = new RegisterUserTask(mContext, requestRegister);
            registerUserTask.mListener  =   this;

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void registerUserCallBack(ArrayList<RegisterUser> registerUsers) {
        if(registerUsers.size() > 0){
            int errorCode   =   registerUsers.get(0).getErrorCode();
            if(errorCode == 0){
                String userData =   registerUsers.get(0).getUserData();
                try {
                    JSONObject objUser  =   new JSONObject(userData);
                    Log.v(TAG, "User name: "+objUser.optString("name"));

                    int userId = objUser.optInt("id");
                    if(userId > 0){
                        UserClass userClass   =   new UserClass();
                        userClass.setUserId(""+userId);
                        userClass.setUserFullName(""+objUser.optString("name"));
                        userClass.setUserEmoji("");
                        userClass.setLoggedIn(true);
                        userClass.setHasSelection(false);

                        userClass.setFontSize(""+objUser.optString("fontsoze"));
                        if(objUser.optInt("send_notification") == 1){
                            userClass.setNotification(true);
                        }
                        else{
                            userClass.setNotification(false);
                        }

                        if(objUser.optInt("hd_images") == 1){
                            userClass.setHDImages(true);
                        }
                        else{
                            userClass.setHDImages(false);
                        }

                        Util.saveUserClass(mContext, userClass);

                        Intent intent = new Intent(mContext, MainCategoryActivity.class);
                        startActivity(intent);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /**/
            }
            else if(errorCode ==1){
                Util.showMessageWithOk(mContext, ""+getString(R.string.register_invalid_post));
                return;
            }
            else if(errorCode ==2){
                Util.showMessageWithOk(mContext, ""+getString(R.string.register_invalid_name));
                return;
            }
            else if(errorCode ==3){
                Util.showMessageWithOk(mContext, ""+getString(R.string.register_invalid_email));
                return;
            }
            else if(errorCode ==4){
                Util.showMessageWithOk(mContext, ""+getString(R.string.register_failed));
                return;
            }
            else{
                Util.showMessageWithOk(mContext, ""+getString(R.string.something_went_wrong)+"\n"+getString(R.string.please_try_again));
                return;
            }
        }
        else{
            Util.showMessageWithOk(mContext, ""+getString(R.string.something_went_wrong)+"\n"+getString(R.string.please_try_again));
            return;

        }
    }
}
