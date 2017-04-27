package com.thesis.velma;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.thesis.velma.helper.OkHttp;

public class cancelEvent extends AppCompatActivity {

    Context context;

    int id;
    String i;
    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decline_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       Bundle b = getIntent().getExtras();

        Log.d(TAG, "Received: " +b.getLong("unix")+b.getString("people"));

        for (int i = 0; i <= b.getString("people").length() - 1; i++) {
            String[] target = OnboardingFragment3.invitedContacts.get(i).split("@");
            OkHttp.getInstance(context).cancelNotification("Cancel", b.getLong("unix"), b.getString("name"),
                    b.getString("description"), b.getString("location"), b.getString("start"), b.getString("end"), b.getString("dateS"),
                    b.getString("dateE") ,b.getString("people"), target[0] + "Velma");//target[0]
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);

    }

}
