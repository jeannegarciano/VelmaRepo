package com.thesis.velma.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class CheckInternet {

    Context mContext;
    static AlertDialog.Builder alertDialog;

    // Constructor
    public CheckInternet(Context context) {
        this.mContext = context;
    }

    public static void showConnectionDialog(Context context) {

        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Connection");
        alertDialog.setMessage("Unable to perform action. Please check your internet connection.");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();


    }

}
