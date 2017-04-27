package com.thesis.velma.helper;

import android.app.ProgressDialog;
import android.content.Context;

public class progressDialog {

	static ProgressDialog mDialog;
	
	public static void showDialog(Context context, String Title, String message, Boolean bool){
		mDialog = new ProgressDialog(context);
		mDialog.setCancelable(bool);
		mDialog.setMessage(message);
//		mDialog.show();
	}
	
	
	public static void hideDialog(){
		if (mDialog != null){
			mDialog.dismiss();
		}
	}
	
}
