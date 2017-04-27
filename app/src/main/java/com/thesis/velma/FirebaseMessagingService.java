package com.thesis.velma;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "Accepted";
    Context mcontext;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }


        Log.d("Notification1", "" + remoteMessage.getData());
        // showNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("sender"));
        showNotification(remoteMessage);
    }


    private void showNotification(RemoteMessage remoteMessage) {

        Random r = new Random();
        int dummyuniqueInt = new Random().nextInt(543254);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, LandingActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        switch (remoteMessage.getData().get("title")) {

            case "Invitation":


                Intent detailsIntent1 = new Intent(this, acceptEvent.class);
                Intent detailsIntent2 = new Intent(this, declineEvent.class);
                Bundle b = new Bundle();
                b.putString("userid", remoteMessage.getData().get("userid"));
                b.putLong("eventid", Long.parseLong(remoteMessage.getData().get("eventid")));
                b.putString("eventname", remoteMessage.getData().get("eventname"));
                b.putString("eventDescription", remoteMessage.getData().get("eventDescription"));
                b.putString("eventLocation", remoteMessage.getData().get("eventLocation"));
                b.putString("eventStartDate", remoteMessage.getData().get("eventStartDate"));
                b.putString("eventStartTime", remoteMessage.getData().get("eventStartTime"));
                b.putString("eventEndDate", remoteMessage.getData().get("eventEndDate"));
                b.putString("eventEndTime", remoteMessage.getData().get("eventEndTime"));
//                b.putString("notify", remoteMessage.getData().get("notify"));
//                b.putString("invitedfirends", remoteMessage.getData().get("invitedfirends"));
                b.putString("lat", remoteMessage.getData().get("lat"));
                b.putString("lng", remoteMessage.getData().get("lng"));
                b.putString("creatorEmail", remoteMessage.getData().get("creatorEmail"));


                Log.d("DataBundle1", remoteMessage.getData().get("eventname"));
                Log.d("DataBundle2", remoteMessage.getData().get("eventid"));
                Log.d("DataBundle2", remoteMessage.getData().get("invitedfirends"));
                Log.d("DataBundle1", remoteMessage.getData().get("creatorEmail"));

                detailsIntent1.putExtras(b);
                detailsIntent2.putExtras(b);



//        LandingActivity.db.saveEvent(remoteMessage.getData().get("userid"), Long.parseLong(remoteMessage.getData().get("eventid")),
//                remoteMessage.getData().get("eventname"), remoteMessage.getData().get("eventDescription"), remoteMessage.getData().get("eventLocation")
//                , remoteMessage.getData().get("eventStartDate"), remoteMessage.getData().get("eventStartTime"),
//                remoteMessage.getData().get("eventEndDate"), remoteMessage.getData().get("eventEndTime"), remoteMessage.getData().get("notify"),
//                remoteMessage.getData().get("invitedfirends"));

                String[] mydates = remoteMessage.getData().get("eventStartDate").split("-");
                String[] mytimes = remoteMessage.getData().get("eventStartTime").split(":");

                builder = new NotificationCompat.Builder(this)
                        .addAction(R.drawable.ic_check_circle_blue_500_18dp, "View", PendingIntent.getActivity(this, dummyuniqueInt, detailsIntent1, PendingIntent.FLAG_UPDATE_CURRENT)) // #0
                        .addAction(R.drawable.ic_cancel_blue_500_18dp, "Decline", PendingIntent.getActivity(this, dummyuniqueInt, detailsIntent2, PendingIntent.FLAG_UPDATE_CURRENT))  // #1
                        .setAutoCancel(true)
                        .setContentTitle(remoteMessage.getData().get("eventname"))
                        .setContentText(remoteMessage.getData().get("text"))
                        .setSmallIcon(R.drawable.velmalogo);

                manager.notify(0, builder.build());

                break;


            case "Update":


                Intent detailsIntent3 = new Intent(this, UpdateEvent.class);
                Bundle c = new Bundle();
                c.putString("userid", remoteMessage.getData().get("userid"));
                c.putLong("eventid", Long.parseLong(remoteMessage.getData().get("eventid")));
                c.putString("eventname", remoteMessage.getData().get("eventname"));
                c.putString("eventDescription", remoteMessage.getData().get("eventDescription"));
                c.putString("eventLocation", remoteMessage.getData().get("eventLocation"));
                c.putString("eventStartDate", remoteMessage.getData().get("eventStartDate"));
                c.putString("eventStartTime", remoteMessage.getData().get("eventStartTime"));
                c.putString("eventEndDate", remoteMessage.getData().get("eventEndDate"));
                c.putString("eventEndTime", remoteMessage.getData().get("eventEndTime"));
                c.putString("notify", remoteMessage.getData().get("notify"));
                c.putString("invitedfirends", remoteMessage.getData().get("invitedfirends"));
                c.putString("lat", remoteMessage.getData().get("lat"));
                c.putString("lng", remoteMessage.getData().get("lng"));

                Intent detailsIntent4 = new Intent(this, declineEvent.class);
                detailsIntent3.putExtras(c);
                detailsIntent4.putExtras(c);

                Log.d("Update1", remoteMessage.getData().get("eventname"));
                Log.d("Update2", remoteMessage.getData().get("eventid"));
                Log.d("Update3", remoteMessage.getData().get("invitedfirends"));

//        LandingActivity.db.saveEvent(remoteMessage.getData().get("userid"), Long.parseLong(remoteMessage.getData().get("eventid")),
//                remoteMessage.getData().get("eventname"), remoteMessage.getData().get("eventDescription"), remoteMessage.getData().get("eventLocation")
//                , remoteMessage.getData().get("eventStartDate"), remoteMessage.getData().get("eventStartTime"),
//                remoteMessage.getData().get("eventEndDate"), remoteMessage.getData().get("eventEndTime"), remoteMessage.getData().get("notify"),
//                remoteMessage.getData().get("invitedfirends"));

                String[] mydates1 = remoteMessage.getData().get("eventStartDate").split("-");
                String[] mytimes1 = remoteMessage.getData().get("eventStartTime").split(":");

                builder = new NotificationCompat.Builder(this)
                        .addAction(R.drawable.ic_check_circle_blue_500_18dp, "View", PendingIntent.getActivity(this,dummyuniqueInt, detailsIntent3, PendingIntent.FLAG_UPDATE_CURRENT)) // #0
//                        .addAction(R.drawable.ic_cancel_blue_500_18dp, "Decline", PendingIntent.getActivity(this, dummyuniqueInt, detailsIntent4, PendingIntent.FLAG_UPDATE_CURRENT))  // #1
//                        .setAutoCancel(true)
                        .setContentTitle(remoteMessage.getData().get("eventname"))
                        .setContentText("This event was updated. Click update button to view changes.")
                        .setSmallIcon(R.drawable.velmalogo);

                //HARDCODED VALUES 10:51
//        Calendar calNow = Calendar.getInstance();
//        Calendar calSet = (Calendar) calNow.clone();


//        int AM_PM;
//        if (Integer.parseInt(mytimes[0]) < 12) {
//            AM_PM = 0;
//        } else {
//            AM_PM = 1;
//        }

//        calSet.set(Calendar.YEAR, Integer.parseInt(mydates[2]));
//        calSet.set(Calendar.MONTH, Integer.parseInt(mydates[1])-1);
//        calSet.set(Calendar.DATE, Integer.parseInt(mydates[0]));
//        calSet.set(Calendar.HOUR, Integer.parseInt(mytimes[0]));
//        calSet.set(Calendar.MINUTE, Integer.parseInt(mytimes[1]));
//        calSet.set(Calendar.SECOND, 0);
//        calSet.set(Calendar.MILLISECOND, 0);
//        calSet.set(Calendar.AM_PM, AM_PM);

//        calSet.clear();
//        calSet.set(Integer.parseInt(mydates[2]), Integer.parseInt(mydates[1]) - 1, Integer.parseInt(mydates[0]), Integer.parseInt(mytimes[0]), Integer.parseInt(mytimes[1]));


//        PendingIntent mypendingintent;
//        Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
//        myIntent.putExtra("name", remoteMessage.getData().get("eventname"));
//        mypendingintent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, 0);

//        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), mypendingintent);


                manager.notify(0, builder.build());


                break;

            case "DeleteEvent":
                String deleteMessage = LandingActivity.profilename + " deleted this event from his/her calendar.";

                builder = new NotificationCompat.Builder(this)
                        .setAutoCancel(true)
                        .setContentTitle("Event title: " +remoteMessage.getData().get("eventname"))
                        .setContentText(deleteMessage)
                        .setSmallIcon(R.drawable.velmalogo);


//                LandingActivity.db.deleteEvent(Long.parseLong(remoteMessage.getData().get("eventid")));

                manager.notify(0, builder.build());


                break;

            case "Cancel":

                String cancelMessage = LandingActivity.profilename + " canceled to this event.";

                builder = new NotificationCompat.Builder(this)
                        .setAutoCancel(true)
                        .setContentTitle("Event title: " +remoteMessage.getData().get("eventname"))
                        .setContentText(cancelMessage)
                        .setSmallIcon(R.drawable.velmalogo);



                manager.notify(0, builder.build());


                break;


            case "confirmEvent":
                String confirmMessage = "Your friend will attend to this event.";

                builder = new NotificationCompat.Builder(this)
                        .setAutoCancel(true)
                        .setContentTitle("Event title: " +remoteMessage.getData().get("eventname"))
                        .setContentText(confirmMessage)
                        .setSmallIcon(R.drawable.velmalogo);

                manager.notify(0, builder.build());

                break;


            case "unableAttend":
                String unableMessage = "Your friend will not attend to this event.";
                builder = new NotificationCompat.Builder(this)
                        .setAutoCancel(true)
                        .setContentTitle("Event title: " +remoteMessage.getData().get("eventname"))
                        .setContentText(unableMessage)
                        .setSmallIcon(R.drawable.velmalogo);

                manager.notify(0, builder.build());

                break;

            case "lateAttend":

                String lateMessage = "Your friend will be late to this event.";

                builder = new NotificationCompat.Builder(this)
                        .setAutoCancel(true)
                        .setContentTitle("Event title: " +remoteMessage.getData().get("eventname"))
                        .setContentText(lateMessage)
                        .setSmallIcon(R.drawable.velmalogo);

                manager.notify(0, builder.build());

                break;

        }


    }


}
