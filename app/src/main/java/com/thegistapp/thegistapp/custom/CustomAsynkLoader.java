/*
 *
 *
 *
 */

package com.thegistapp.thegistapp.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.thegistapp.thegistapp.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class CustomAsynkLoader extends Dialog{

	Dialog mDialog;
	public CustomAsynkLoader(Context context){
		super(context);

		mDialog=new Dialog(context);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		mDialog.getWindow ().setBackgroundDrawableResource(android.R.color.transparent);
		mDialog.getWindow ().setBackgroundDrawableResource(R.color.colorWhite);
		LayoutInflater layoutInflater = (LayoutInflater)
				context.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = layoutInflater.inflate(R.layout.custom_progress_image_dialog, null);


		mDialog.setContentView(R.layout.custom_progress_image_dialog);
		Window window = mDialog.getWindow();
		window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


		mDialog.setCancelable(false);

	}

	public void ShowDialog(){
			mDialog.show();
	}

	public void DismissDialog(){
		mDialog.dismiss();
	}
	
	public boolean isDialogShowing(){
		return mDialog != null && mDialog.isShowing();
	}


}
