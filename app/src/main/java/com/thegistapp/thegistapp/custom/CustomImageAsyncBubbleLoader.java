/*
 *
 *
 *
 */

package com.thegistapp.thegistapp.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ImageView;

import com.thegistapp.thegistapp.R;


public class CustomImageAsyncBubbleLoader extends Dialog{

	Dialog mDialog;
	public CustomImageAsyncBubbleLoader(Context context){
		super(context);

		mDialog=new Dialog(context);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow ().setBackgroundDrawableResource(android.R.color.transparent);
		
		mDialog.setContentView(R.layout.custom_progress_image_dialog);

		final ImageView bounceBallImage = (ImageView) findViewById(R.id.bounceBallImage);
		
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
