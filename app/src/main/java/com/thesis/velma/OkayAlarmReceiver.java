package com.thesis.velma;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by admin on 12/21/2016.
 */

public class OkayAlarmReceiver extends BroadcastReceiver {


    String myTitle = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(11111);

        Bundle b = intent.getExtras();

//        Toast.makeText(context, b.getString("eventid"), Toast.LENGTH_SHORT).show();
        if (intent.getAction().equalsIgnoreCase("CONFIRM")) {
            myTitle = "confirmEvent";
            //Toast.makeText(context, "You confirm", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equalsIgnoreCase("CANCEL")) {
            myTitle = "unableAttend";
//            Toast.makeText(context, "You cant attend the event", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equalsIgnoreCase("LATE")) {
            myTitle = "lateAttend";
//            Toast.makeText(context, "You'll be late", Toast.LENGTH_SHORT).show();
        }

//        String[] myfriends = b.getString("invitedfriends").split(",");

//        for (int i = 0; i <= myfriends.length - 1; i++) {
//            String[] target = myfriends[i].split("@");
//            OkHttp.getInstance(context).sendNotification(myTitle, b.getLong("eventid"), b.getString("eventname"), b.getString("eventDescription"),
//                    b.getString("eventLocation"), b.getString("eventStartDate"), b.getString("eventStartTime"), b.getString("eventEndDate"),
//                    b.getString("eventEndTime"), ".", b.getString("invitedfirends"), target[0] + "Velma", b.getString("lat"), b.getString("lng"));
//        }





    }
}


// extends BroadcastReceiver {
//
//    Context mcontext;
//    private static final String YES_ACTION = "YES_ACTION";
//    private static final String Cancel_ACTION = "CANCEL_ACTION";
//    private static final String Ill_Be_Late = "LATE_ACTION";
//
//    @Override
//    public void onReceive(final Context context, Intent intent) {
//
//        mcontext = context;
////        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
////        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
////        ringtone.play();
////
////        StopNotification(intent.getStringExtra("id"));
//
//        String action = intent.getAction();
//
//        if (YES_ACTION.equals(action)) {
//            Log.v("shuffTest", "Pressed YES");
//        } else if (Cancel_ACTION.equals(action)) {
//            Log.v("shuffTest", "Pressed NO");
//        } else if (Ill_Be_Late.equals(action)) {
//            Log.v("shuffTest", "Pressed MAYBE");
//        }
//
//    }
//
//    private void StopNotification(String notifId) {
//
//        Log.d("ID", notifId);
//        Toast.makeText(mcontext, "Id Received: " + notifId, Toast.LENGTH_LONG).show();
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mcontext);
////        int notificationId = 1;
//        notificationManager.cancel(Integer.valueOf(notifId));
//    }
//}