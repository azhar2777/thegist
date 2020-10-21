/*
 *
 *
 *
 */

package com.thegistapp.thegistapp.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import com.thegistapp.thegistapp.R;


public class CustomImageAsynkLoader extends Dialog{

	Dialog mDialog;
	public CustomImageAsynkLoader(Context context){
		super(context);

		mDialog=new Dialog(context);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//mDialog.getWindow ().setBackgroundDrawableResource(android.R.color.transparent);

		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.colorAppMainColor)));
		mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


		mDialog.setContentView(R.layout.custom_progress_dialog_small);
		
		mDialog.setCancelable(false);
		
//		mDialog.show();
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
