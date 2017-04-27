package com.thesis.velma;

/**
 * Created by admin on 12/21/2016.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import java.util.Random;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = "MyActivity";
    Context mcontext;
    //private static final String YES_ACTION = "com.thesis.velma.YES";
    public static final String ACTION_DEMAND = "com.androidweardocs.ACTION_DEMAND";
    public static final String EXTRA_MESSAGE = "com.androidweardocs.EXTRA_MESSAGE";
    public static final String EXTRA_VOICE_REPLY = "com.androidweardocs.EXTRA_VOICE_REPLY";

    @Override
    public void onReceive(final Context context, Intent intent) {

        mcontext = context;
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        ringtone.play();

        // sendNotification(intent.getStringExtra("unix"), intent.getStringExtra("name"), intent.getStringExtra("description"), intent.getStringExtra("location"), intent.getStringExtra("start"), intent.getStringExtra("end"), intent.getStringExtra("dateS"), intent.getStringExtra("dateE"), intent.getStringExtra("people"));
        sendNotification(intent);

        ringtone.stop();

        // showNotification("" + intent.getLongExtra("unix", 0), intent.getStringExtra("name"),
        //         intent.getStringExtra("description"), intent.getStringExtra("location"), intent.getStringExtra("start"),
        //         intent.getStringExtra("end"), intent.getStringExtra("dateS"), intent.getStringExtra("dateE"), intent.getStringExtra("people"));
    }

    private void showNotification(String unixtime, String eventname, String des, String loc, String timeStart, String timeEnd, String dateStart, String dateEnd, String invitedContacts) {
        String received;

        String eventDescription = "Please click to open velma app";
        final String EXTRA_VOICE_REPLY = "extra_voice_reply";
        int notificationId = new Random().nextInt();

        Intent okayIntent = new Intent(mcontext, acceptEvent.class);
        Bundle okayBundle = new Bundle();
        okayBundle.putInt("ID", notificationId);
        okayBundle.putString("unix", unixtime);
        okayBundle.putString("eventid", "0");
        okayBundle.putString("eventname", eventname);
        okayBundle.putString("eventDescription", des);
        okayBundle.putString("eventLocation", loc);
        okayBundle.putString("eventStartTime", timeStart);
        okayBundle.putString("eventEndTime", timeEnd);
        okayBundle.putString("eventStartDate", dateStart);
        okayBundle.putString("eventEndDate", dateEnd);
        okayBundle.putString("notify", "");
        okayBundle.putString("invitedfirends", invitedContacts);
        okayIntent.putExtras(okayBundle);


        Intent cancelIntent = new Intent(mcontext, declineEvent.class);
        Bundle cancelBundle = new Bundle();
        cancelBundle.putInt("ID", notificationId);
        cancelBundle.putString("unix", unixtime);
        cancelBundle.putString("name", eventname);
        cancelBundle.putString("description", des);
        cancelBundle.putString("location", loc);
        cancelBundle.putString("start", timeStart);
        cancelBundle.putString("end", timeEnd);
        cancelBundle.putString("dateS", dateStart);
        cancelBundle.putString("dateE", dateEnd);
        cancelBundle.putString("people", invitedContacts);
        cancelIntent.putExtras(cancelBundle);


        String replyLabel = "My Input";
        String[] replyChoices = mcontext.getResources().getStringArray(R.array.reply_choices);

        android.support.v4.app.NotificationCompat.BigTextStyle bigStyle = new android.support.v4.app.NotificationCompat.BigTextStyle();
        bigStyle.bigText(eventDescription);

        RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_VOICE_REPLY)
                .setLabel(replyLabel)
                .setChoices(replyChoices)
                .build();
        android.support.v4.app.NotificationCompat.Action action = new android.support.v4.app.NotificationCompat.Action.Builder(R.drawable.ic_reply_white_24dp, "My Action", PendingIntent.getActivity(mcontext, 0111, okayIntent, 0))
                .addRemoteInput(remoteInput)
                .build();

        Intent intent = new Intent(mcontext, LandingActivity.class);

        // Creating a pending intent and wrapping our intent
        PendingIntent lateintent = PendingIntent.getActivity(mcontext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        Notification notification = new android.support.v4.app.NotificationCompat.Builder(mcontext)
                .setStyle(inboxStyle)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.drawable.velmalogo)
                .setContentTitle(eventname)
                .setAutoCancel(true)
                //.addAction(R.drawable.ic_check_circle_blue_500_18dp, "Okay", pendingIntentYes)
                //.addAction(R.drawable.ic_cancel_blue_500_18dp, "Cancel Event", pendingIntentNo)
                //.addAction(R.drawable.ic_assignment_late_blue_500_18dp, "I'll be coming late", pendingIntentMaybe)
                .setContentTitle("You have an event in " + loc + " at " + timeStart + " until " + timeEnd)
                .addAction(R.drawable.ic_check_circle_blue_500_18dp, "Okay", PendingIntent.getActivity(mcontext, 0111, okayIntent, PendingIntent.FLAG_UPDATE_CURRENT)) // #0
                .addAction(R.drawable.ic_cancel_blue_500_18dp, "Cancel Event", PendingIntent.getActivity(mcontext, 0111, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT))// #1
                .addAction(R.drawable.ic_assignment_late_blue_500_18dp, "I'll be coming late", lateintent)
                .setContentText("You have an event in " + loc + " at " + timeStart + " until " + timeEnd).setStyle(bigStyle)
                .extend(new android.support.v4.app.NotificationCompat.WearableExtender().setHintShowBackgroundOnly(true))
                //.extend(new android.support.v4.app.NotificationCompat.WearableExtender().addAction(action))
                .build();

        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

//        notification.contentIntent = PendingIntent.getActivity(mcontext, 0,
//                new Intent(mcontext, LandingActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mcontext);
        notificationManager.notify(notificationId, notification);


    }

    //  private void sendNotification(String unixtime, String eventname, String des, String loc,
    //                                String timeStart, String timeEnd, String dateStart, String dateEnd, String invitedContacts) {

    private void sendNotification(Intent myintent) {
        String[] replyChoices = mcontext.getResources().getStringArray(R.array.reply_choices);

        Bundle bundle = new Bundle();

        // bundle.putString("unix", unixtime);
        bundle.putLong("eventid", myintent.getLongExtra("unix", 0));
        bundle.putString("eventname", myintent.getStringExtra("name"));
        bundle.putString("eventDescription", myintent.getStringExtra("description"));
        bundle.putString("eventLocation", myintent.getStringExtra("location"));
        bundle.putString("eventStartTime", myintent.getStringExtra("start"));
        bundle.putString("eventEndTime", myintent.getStringExtra("end"));
        bundle.putString("eventStartDate", myintent.getStringExtra("dateS"));
        bundle.putString("eventEndDate", myintent.getStringExtra("dateE"));
        bundle.putString("notify", "");
        bundle.putString("invitedfriends", myintent.getStringExtra("people"));
        bundle.putString("lat", myintent.getStringExtra("lat"));
        bundle.putString("lng", myintent.getStringExtra("lng"));

        String eventname = myintent.getStringExtra("name");
        String location = myintent.getStringExtra("location");
        String timeStart = myintent.getStringExtra("start");
        String timeEnd = myintent.getStringExtra("end");

//        Toast.makeText(mcontext, location, Toast.LENGTH_SHORT).show();

        //  Log.d("Data", ""+ myintent.getStringExtra("location"));
        //  Log.d("Data", ""+ myintent.getStringExtra("start"));


        Intent intent = new Intent(mcontext, LandingActivity.class);
        intent.putExtra("fromNotification", "book_ride");
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent intentConfirm = new Intent(mcontext, OkayAlarmReceiver.class);
        intentConfirm.setAction("CONFIRM");
        intentConfirm.putExtras(bundle);
        intentConfirm.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent intentCancel = new Intent(mcontext, OkayAlarmReceiver.class);
        intentCancel.setAction("CANCEL");
        intentCancel.putExtras(bundle);
        intentCancel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent intentLate = new Intent(mcontext, OkayAlarmReceiver.class);
        intentLate.setAction("LATE");
        intentLate.putExtras(bundle);
        intentLate.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

//
//        // Create an Intent for the demand
//        Intent demandIntent = new Intent(mcontext, DemandIntentReceiver.class)
//                .putExtra(EXTRA_MESSAGE, "Reply icon selected.")
//                .setAction(ACTION_DEMAND);
//
//        // Create a pending intent using the local broadcast receiver
//        PendingIntent demandPendingIntent =
//                PendingIntent.getBroadcast(mcontext, 0, demandIntent, 0);
//
//        // Create RemoteInput object for a voice reply (demand)
//        String replyLabel = mcontext.getResources().getString(R.string.app_name);
//        RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_VOICE_REPLY)
//                .setLabel(replyLabel)
//                .setChoices(replyChoices)
//                .build();
//
//        // Create a wearable action
//        NotificationCompat.Action replyAction =
//                new NotificationCompat.Action.Builder(R.drawable.ic_reply_white_24dp,
//                        eventname, demandPendingIntent)
//                        .addRemoteInput(remoteInput)
//                        .build();
//
//        // Create a wearable extender for a notification
//        NotificationCompat.WearableExtender wearableExtender =
//                new NotificationCompat.WearableExtender()
//                        .addAction(replyAction);


//This Intent will be called when Notification will be clicked by user.
        PendingIntent pendingIntent = PendingIntent.getActivity(mcontext, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

//This Intent will be called when Confirm button from notification will be
//clicked by user.
        PendingIntent pendingIntentConfirm = PendingIntent.getBroadcast(mcontext, 0, intentConfirm, PendingIntent.FLAG_CANCEL_CURRENT);
//This Intent will be called when Cancel button from notification will be
//clicked by user.
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(mcontext, 1, intentCancel, PendingIntent.FLAG_CANCEL_CURRENT);

        PendingIntent pendingIntentLate = PendingIntent.getBroadcast(mcontext, 2, intentLate, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mcontext)
                .setSmallIcon(R.drawable.logonew)
                .setContentTitle(eventname)
                .setAutoCancel(true)
                .setContentText(location + " at " + timeStart + " until " + timeEnd)
                .setSound(defaultSoundUri)
                .extend(new android.support.v4.app.NotificationCompat.WearableExtender().setHintShowBackgroundOnly(true))
                .setContentIntent(pendingIntent);


        notificationBuilder.addAction(R.drawable.ic_add_white_18dp, "Confirm", pendingIntentConfirm);
        notificationBuilder.addAction(R.drawable.ic_delete_white_24dp, "Cancel", pendingIntentCancel);
        notificationBuilder.addAction(R.drawable.ic_directions_run_white_24dp, "Late", pendingIntentLate);


        NotificationManager notificationManager =
                (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(11111 /* ID of notification */, notificationBuilder.build());
    }

}