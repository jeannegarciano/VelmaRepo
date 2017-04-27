package com.thesis.velma;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thesis.velma.helper.CheckInternet;
import com.thesis.velma.helper.DataBaseHandler;
import com.thesis.velma.helper.NetworkUtil;
import com.thesis.velma.helper.OkHttp;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by admin on 3/19/2017.
 */

public class DeleteEvent  extends AppCompatActivity {

    Context mcontext;
    public static DataBaseHandler db;
    int i;
    TextView title, ename, n, edescription, description, sdText, sd, edText, ed, stText, st, et, etText, fText, f, lText, l;
    TextView userT, userId, eventT;
    Button accept;
    String en, des, sDate, endDate, sTime, eTime, iFriends, locat, idUser, lat, lng, eventID;
    Long idEvent;
    String modetravel = "driving";
    Bundle b;
    private PendingIntent pendingIntent;


    String locNameA = "", locLatA = "", locLngA = "", locLocationA = "", locSDA = "", locEDA = "", locSTA  = "", locETA = "";
    String locNameB= "", locLatB= "", locLngB= "", locLocationB= "", locSDB= "", locEDB= "", locSTB= "", locETB= "";
    long diffInMinutesA, diffInMinutesB;
    String diffA, diffB;
    double latA, lngA, latB, lngB;
    long travelduration;
    String eventId;
    int eventid;

    ArrayList<String> myConflictEvents = new ArrayList<String>();
    ArrayList<String> myCurrentEvent = new ArrayList<>();

    String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_event_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mcontext = this;


        db = new DataBaseHandler(mcontext);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mcontext);
        //then you use
        userEmail = prefs.getString("Email", null);

        Bundle bundle = getIntent().getExtras();
        eventid = bundle.getInt("eventid");
        Log.d("ID CANCELED", String.valueOf(eventid));

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/avenir-next-regular.ttf");
        TextView mdesc = (TextView) findViewById(R.id.desc);
        mdesc.setTypeface(custom_font);
        TextView msdate = (TextView) findViewById(R.id.sDate);
        msdate.setTypeface(custom_font);
        TextView medate = (TextView) findViewById(R.id.eDate);
        medate.setTypeface(custom_font);
        TextView mstime = (TextView) findViewById(R.id.sTime);
        mstime.setTypeface(custom_font);
        TextView metime = (TextView) findViewById(R.id.eTime);
        metime.setTypeface(custom_font);
        TextView mlocation = (TextView) findViewById(R.id.loc);
        mlocation.setTypeface(custom_font);
        TextView mfriends = (TextView) findViewById(R.id.friends);
        mfriends.setTypeface(custom_font);

//        title = (TextView) findViewById(R.id.textView);
//        ename = (TextView) findViewById(R.id.eventName);
//        n = (TextView) findViewById(R.id.name);
//        edescription = (TextView) findViewById(R.id.eventDescription);
//        description = (TextView) findViewById(R.id.eventDescription1);
//        sdText = (TextView) findViewById(R.id.sdate);
//        sd = (TextView) findViewById(R.id.sdate1);
//        edText = (TextView) findViewById(R.id.edate);
//        ed = (TextView) findViewById(R.id.edate1);
//        stText = (TextView) findViewById(R.id.stime);
//        st = (TextView) findViewById(R.id.stime1);
//        etText = (TextView) findViewById(R.id.etime);
//        et = (TextView) findViewById(R.id.etime1);
//        fText = (TextView) findViewById(R.id.friends);
//        f = (TextView) findViewById(R.id.friends1);
//        accept = (Button) findViewById(R.id.acceptEvent);
//        accept.setOnClickListener(this);
//        lText = (TextView) findViewById(R.id.location);
//        l = (TextView) findViewById(R.id.location1);
//        userT = (TextView) findViewById(R.id.userIdText);
//        userId = (TextView) findViewById(R.id.userIdText1);
//        eventT = (TextView) findViewById(R.id.eventId);
//        eventId = (TextView) findViewById(R.id.eventId1);


        b = this.getIntent().getExtras();
//        i = b.getInt("ID");



        //Okay button from alarm notif

//        en = b.getString("name");
//        des = b.getString("description");
//        sDate =  b.getString("dateS");
//        endDate = b.getString("dateE");
//        sTime = b.getString("start");
//        eTime = b.getString("end");
//        iFriends = b.getString("people");
//        locat = b.getString("location");

//        n.setText(en);
//        description.setText(des);
//        sd.setText(sDate);
//        ed.setText(endDate);
//        st.setText(sTime);
//        et.setText(eTime);
//        f.setText(iFriends);
//        l.setText(locat);


        if (b!= null) {// to avoid the NullPointerException

            en = b.getString("eventname");
            des = b.getString("eventDescription");
            sDate = b.getString("eventStartDate");
            endDate = b.getString("eventEndDate");
            sTime = b.getString("eventStartTime");
            eTime = b.getString("eventEndTime");
            iFriends = b.getString("invitedfirends");
            locat = b.getString("eventLocation");
            idUser = b.getString("userid");
            idEvent = b.getLong("eventid");
            lat = b.getString("lat");
            lng = b.getString("lng");

        }

        eventId = String.valueOf(idEvent);


        Log.i("Event Accept", lat +","+ lng);
        collapsingToolbarLayout.setTitle(en);
        mdesc.setText(des);
        msdate.setText(sDate);
        medate.setText(endDate);
        mstime.setText(sTime);
        metime.setText(eTime);
        mfriends.setText(iFriends);
        mlocation.setText(locat);
//        userId.setText(idUser);
//        eventId.setText(String.valueOf(idEvent));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                LandingActivity.db.deleteEvent(idEvent);
//                OkHttp.getInstance(mcontext).deleteEvent(eventID);

         finish();
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancel(eventid);
            }
        });

    }

}