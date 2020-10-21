package com.thegistapp.thegistapp.custom;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.thegistapp.thegistapp.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class LoaderPopup {
    private static PopupWindow popupStoryReactions;
    private static View popupView;
    Context mContext;

    public LoaderPopup(Context mContext ) {
        this.mContext = mContext;

    }

    public static void showLoaderPopup(Context mContext, ConstraintLayout showAtView){



        popupStoryReactions = new PopupWindow(mContext);

        if (popupStoryReactions == null || !popupStoryReactions.isShowing()) {

            Log.v("showLoaderPopup", "showLoaderPopup called");
            LayoutInflater layoutInflater = (LayoutInflater)
                    mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            popupView = layoutInflater.inflate(R.layout.popup_loader, null);
            popupStoryReactions = new PopupWindow(null, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);






            popupStoryReactions.setFocusable(false);


            popupStoryReactions.setTouchable(true);
            popupStoryReactions.setOutsideTouchable(false);
            popupStoryReactions.showAtLocation(showAtView, Gravity.CENTER_HORIZONTAL, 0, 0);



            popupStoryReactions.update();
        }
        else {

            popupStoryReactions.dismiss();
        }

    }

    public static void hideLoaderPopup(){
        if (popupStoryReactions != null && popupStoryReactions.isShowing()) {
            popupStoryReactions.dismiss();
        }
    }
}
