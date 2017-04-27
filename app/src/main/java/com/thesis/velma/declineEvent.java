package com.thesis.velma;

import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

public class declineEvent extends AppCompatActivity {

    int id;
    Long eventid;
    TextView d;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decline_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        d = (TextView) findViewById(R.id.deleteText);
//
//
        Bundle bundle = getIntent().getExtras();
        eventid = bundle.getLong("eventid");
        Log.d("ID CANCELED", String.valueOf(eventid));
//
//        value = Long.toString(eventid);
//        d.setText(value);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);


    }

}
