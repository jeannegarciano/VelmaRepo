package com.thesis.velma;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;

/**
 * Created by admin on 3/1/2017.
 */

public class NotificationDetails extends Activity {

    final String EXTRA_VOICE_REPLY = "extra_voice_reply";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CharSequence charSequence = getMessageText(getIntent());
        String temp = charSequence.toString();
    }

    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(EXTRA_VOICE_REPLY);
        }
        return null;
    }
}
