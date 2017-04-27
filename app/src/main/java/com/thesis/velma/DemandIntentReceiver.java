package com.thesis.velma;

/**
 * Created by admin on 3/10/2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by michaelHahn on 11/7/15.
 */
public class DemandIntentReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(AlarmReceiver.ACTION_DEMAND)) {
            String message =
                    intent.getStringExtra(AlarmReceiver.EXTRA_MESSAGE);
            Log.v("MyTag", "Extra message from intent = " + message);
            Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
            CharSequence reply = remoteInput.getCharSequence(AlarmReceiver.EXTRA_VOICE_REPLY);
            Log.v("MyTag", "User reply from wearable: " + reply);

            // Broadcast message to wearable activity for display or any other purpose
            String replyString = reply.toString();
            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("reply", replyString);
            LocalBroadcastManager.getInstance(context).sendBroadcast(messageIntent);

        }
    }
}

