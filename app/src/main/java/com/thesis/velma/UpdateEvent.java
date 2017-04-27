package com.thesis.velma;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.thesis.velma.helper.DataBaseHandler;
import com.thesis.velma.helper.OkHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 3/19/2017.
 */

public class UpdateEvent extends AppCompatActivity {
    Context mcontext;
    public static DataBaseHandler db;
    int i;
    TextView title, ename, n, edescription, description, sdText, sd, edText, ed, stText, st, et, etText, fText, f, lText, l;
    TextView userT, userId, eventT, eventId;
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
    int eventid;

    ArrayList<String> myConflictEvents = new ArrayList<String>();
    ArrayList<String> myCurrentEvent = new ArrayList<>();

    String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_event_new);
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
                final Cursor c = db.conflictChecker(sDate, sTime, endDate, eTime);
                final Cursor cursor = db.compareLocationA(sDate,sTime);
                final Cursor cursorB = db.compareLocationB(sDate,sTime);
                if((cursor != null && cursor.getCount() > 0)||(cursorB != null && cursorB.getCount() > 0)){

                    if((cursor != null && cursor.getCount() > 0)&&(cursorB != null && cursorB.getCount() > 0)){
                        String resultA = getloctionA(cursor,  en, locat, sDate, sTime, endDate, eTime,lat, lng );
                        String resultB = getloctionB(cursorB, en, locat, sDate, sTime, endDate, eTime,lat, lng);
                        locationConflict(resultA,resultB, idEvent, en, des, locat, sDate, sTime, endDate, eTime, b.getString("notify"), iFriends, userEmail, lat, lng);
                    }
//                    locationConflict(resultA,resultB, unixtime, name, eventDescription, eventLocation, startDate, startTime, endDate, endTime, notify, invitedContacts, userEmail, lat, lng);
                    else if((cursorB != null && cursorB.getCount() > 0)){
                        String resultB = getloctionB(cursorB, en, locat, sDate, sTime, endDate, eTime,lat, lng );
                        String resultA="";
                        locationConflict(resultA,resultB, idEvent, en, des, locat, sDate, sTime, endDate, eTime, b.getString("notify"), iFriends, userEmail, lat, lng);
                    }
                    else if((cursor != null && cursor.getCount() > 0)){
                        String resultA = getloctionA(cursor,  en, locat, sDate, sTime, endDate, eTime,lat, lng );
                        String resultB ="";
                        locationConflict(resultA,resultB, idEvent, en, des, locat, sDate, sTime, endDate, eTime, b.getString("notify"), iFriends, userEmail, lat, lng);
                    }
                }
                //  public String getloctionA(final Cursor cursor, final String name, final String eventLocation, final String startDate, final String startTime, final String endDate, final String endTime, final String lat, final String lng )
                else if (c != null && c.getCount() > 0) {
                    timeconflict(c, idEvent, en, des, locat, sDate, sTime, endDate, eTime, b.getString("notify"), iFriends, lat, lng);
                } else {

                    saveEventFunction(idEvent,  en,  des, locat,sDate, sTime,  endDate, eTime, b.getString("notify"), iFriends,  lat,lng);


                }





//                final Cursor c = db.conflictChecker(sDate, sTime, endDate, eTime);
//                final Cursor cursor = db.compareLocationA(sDate,sTime);
//
//
//
//
//                Log.i("Event laaa", "im in tho");
//                if ((cursor != null && cursor.getCount() > 0)) {
//                    cursor.moveToFirst();
//                    locNameA = cursor.getString(cursor.getColumnIndex("EventName"));
//                    locLocationA = cursor.getString(cursor.getColumnIndex("EventLocation"));
//                    locLatA = cursor.getString(cursor.getColumnIndex("Extra2"));
//                    locLngA = cursor.getString(cursor.getColumnIndex("Extra3"));
//                    locSDA = cursor.getString(cursor.getColumnIndex("StartDate"));
//                    locEDA = cursor.getString(cursor.getColumnIndex("EndDate"));
//                    locSTA = cursor.getString(cursor.getColumnIndex("StartTime"));
//                    locETA = cursor.getString(cursor.getColumnIndex("EndTime"));
//
//                    Log.i("Event laaa", "im in tho2");
//                    String locA_DT = locSDA + " " + locETA;
//                    String cur_A = sDate + " " + sTime;
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
//
//                    try {
//                        Date locADT = simpleDateFormat.parse(locA_DT);
//                        Date curA = simpleDateFormat.parse(cur_A);
//
//                        long durationA = curA.getTime() - locADT.getTime();
//                        diffInMinutesA = TimeUnit.MILLISECONDS.toMinutes(durationA);
//
//
//                        System.out.println("locSDT : " + simpleDateFormat.format(locADT));
//                        System.out.println("curA : " + simpleDateFormat.format(curA));
//                        System.out.println("diff:" + diffInMinutesA);
//                        Log.i("diffInMins :", "" + diffInMinutesA);
//                        diffA = "" + diffInMinutesA;
//                        Log.i("Event A Diff", diffA);
//
//                        String llA = locLatA + "," + locLngA;
//
//                        Log.i("Event laaa", "im in tho3");
//                        latA = Double.parseDouble(locLatA);
//                        lngA = Double.parseDouble(locLngA);
//                        Log.i("EVENT A STRING", llA);
//
//                        Log.i("Event laaa", "im in tho4");
//                        try {
//                            latA = Double.parseDouble(locLatA);
//                            lngA = Double.parseDouble(locLngA);
//
//                            Log.i("EventLAT A", "" + latA + " , " + lngA);
//                        } catch (NumberFormatException e) {
//                            Log.i("EventLAT A", "" + e);
//                        }
//
//                        Log.i("Event laaa", "im in tho5");
//                        new getLocationDetails().execute(latA, lngA);
//
//                    } catch (ParseException ex) {
//                        System.out.println("Exception " + ex);
//                    }
//                    Log.i("Event after", "wa");
//                    Log.i("Event before", locNameA + ": " + locLocationA);
//
//
//
//                }
//                else if (c != null && c.getCount() > 0) {
//
//                    timeconflict(c, idEvent, en, des, locat, sDate, sTime, endDate, eTime, b.getString("notify"), iFriends, lat, lng);
//
//                }else {

            }
        });

    }


    public void timeconflict(final Cursor c, final long idEvent, final String en, final String des, final String locat, final String sDate, final String sTime, final String endDate, final String eTime, final String notify, final String iFriends, final String lat, final String lng){
        c.moveToFirst();
        while (!c.isAfterLast()) {

//            Toast.makeText(OnboardingActivity.this, "Conflict in: " + c.getString(1) + "   " + c.getString(2) + "  " + c.getString(3) + "  " + c.getString(4), Toast.LENGTH_LONG).show();
//            Log.i("Event log", c.getString(0) + " >> " + c.getString(1) + " >> " + c.getString(2) + " >> " + c.getString(3) + " >> " + c.getString(4) + " >> ");


            c.moveToNext();
        }


        final AlertDialog.Builder builder = new AlertDialog.Builder(mcontext)
                .setTitle("Event Time Conflict")
                .setIcon(R.drawable.time)
                .setMessage("The event you created ago has the following conflict(s).")
                .setPositiveButton("Add Anyway", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                saveEventFunction(idEvent, en, des, locat, sDate, sTime, endDate, eTime, b.getString("notify"), iFriends, lat, lng);
                                dialog.dismiss();


                            }

                        }


                );

        ListView modeList = new ListView(mcontext);
        myConflictEvents.clear();
        myConflictEvents.add(myCurrentEvent.get(0));
        c.moveToFirst();
        eventID = c.getString(0);
        Log.i("Event con", "5");

        while (!c.isAfterLast()) {

            StringTokenizer display1 = new StringTokenizer(c.getString(c.getColumnIndex("StartTime")), ":");
            String s_hour = display1.nextToken();
            String s_min = display1.nextToken();
            StringTokenizer display2 = new StringTokenizer(c.getString(c.getColumnIndex("EndTime")), ":");
            String e_hour = display2.nextToken();
            String e_min = display2.nextToken();
            String sdisplay = "";
            String edisplay = "";
            if (s_hour.length() == 1) {
                s_hour = "0" + s_hour;
            }
            if (s_min.length() == 1) {
                s_min = "0" + s_min;
            }
            if (Integer.parseInt(s_hour) > 12) {
                s_hour = "" + (Integer.parseInt(s_hour) - 12);

                sdisplay = s_hour + ":" + s_min + " PM";

            } else if (Integer.parseInt(s_hour) == 12) {
                sdisplay = s_hour + ":" + s_min + " PM";
            } else {
                sdisplay = s_hour + ":" + s_min + " AM";
            }
            if (e_hour.length() == 1) {
                e_hour = "0" + e_hour;
            }
            if (e_min.length() == 1) {
                e_min = "0" + e_min;
            }
            if (Integer.parseInt(e_hour) > 12) {
                e_hour = "" + (Integer.parseInt(e_hour) - 12);

                edisplay = e_hour + ":" + e_min + " PM";

            } else if (Integer.parseInt(e_hour) == 12) {
                edisplay = e_hour + ":" + e_min + " PM";
            } else {
                edisplay = e_hour + ":" + e_min + " AM";
            }


            Log.i("Event conn", " " + c.getString(c.getColumnIndex("EventName")));
            Log.i("Event id", eventID);

            myConflictEvents.add(c.getString(c.getColumnIndex("EventName")) + "   " + sdisplay + " to " + edisplay); //this adds an element to the list.
            c.moveToNext();
        }

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(mcontext, android.R.layout.simple_list_item_1, android.R.id.text1, myConflictEvents);
        modeList.setAdapter(modeAdapter);

        builder.setView(modeList);

        builder.create();
        builder.show();
    }



    public void saveEventFunction(final long idEvent, final String en, final String des, final String locat, final String sDate, final String sTime, final String endDate, final String eTime, final String notify, final String iFriends, final String lat, final String lng){
        String[] mydates = sDate.split("-");
        String[] mytimes = sTime.split(":");

        int count;
        Cursor c = db.getMaxId();
        c.moveToFirst();
        if(c.getCount() == 0)
        {
            count = 1;
        }
        else {
            count = Integer.parseInt(c.getString(0));
            count +=1;
        }

        Log.d("Count", "" + count);

        AlarmManager alarmManager = (AlarmManager) mcontext.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(mcontext, AlarmReceiver.class);
        myIntent.putExtra("unix",  idEvent);
        myIntent.putExtra("name", en);
        myIntent.putExtra("description", des);
        myIntent.putExtra("location", locat);
        myIntent.putExtra("start", sTime);
        myIntent.putExtra("end", eTime);
        myIntent.putExtra("dateS", sDate);
        myIntent.putExtra("dateE", endDate);
        myIntent.putExtra("people", iFriends);
        Log.d("Event name", en);
        pendingIntent = PendingIntent.getBroadcast(mcontext, count+1, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();
        calSet.setTimeInMillis(System.currentTimeMillis());
        calSet.set(Calendar.YEAR, Integer.parseInt(mydates[0]));
        calSet.set(Calendar.MONTH, Integer.parseInt(mydates[1])-1);
        calSet.set(Calendar.DATE, Integer.parseInt(mydates[2]));
        calSet.set(Calendar.HOUR_OF_DAY, Integer.parseInt(mytimes[0]));
        calSet.set(Calendar.MINUTE, Integer.parseInt(mytimes[1]));
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);
        //Logs for checking values received.
        Log.d("AcceptActivityAlarm1", "" + calSet.getTimeInMillis());
        Log.d("AcceptActivityAlarm2", "" + System.currentTimeMillis());

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);

//        //Saving to local database
//        LandingActivity.db.saveEvent(LandingActivity.imei, idEvent, en, des, locat, sDate, sTime,
//                endDate, eTime, b.getString("notify"), iFriends, userEmail, lat, lng);
//
//        //Saving to mysql database
//        OkHttp.getInstance(getBaseContext()).saveEvent(idEvent, en, des, locat, sDate, sTime, endDate,
//                eTime, b.getString("notify"), iFriends, userEmail, lat, lng);


        Log.d("AcceptDetail", "" + LandingActivity.imei + " Id Event: " + idEvent + "" +
                en + "" + des + "" + locat + "" +
                sDate + "" + sTime + "" + endDate + "" +
                eTime + "" + b.getString("notify") + "" + iFriends);

        Intent i = new Intent(UpdateEvent.this, LandingActivity.class);
        startActivity(i);
        finish();

        Log.d("Data1", "" + LandingActivity.imei + " Id Event: " + idEvent + "" +
                en + "" + des + "" + locat + "" +
                sDate + "" + sTime + "" + endDate + "" +
                eTime + "" + b.getString("notify") + "" + iFriends+ "" +lat+ ""+lng);
        Log.d("Data2", "" + LandingActivity.imei + "" + b.getLong("eventid") + "" +
                b.getString("eventname") + "" + b.getString("eventDescription") + "" + b.getString("eventLocation") + "" +
                b.getString("eventStartDate") + "" + b.getString("eventStartTime") + "" + b.getString("eventEndDate") + "" +
                b.getString("eventEndTime") + "" + b.getString("notify") + "" + b.getString("invitedfriends"));

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(eventid);
    }


    public String getloctionA(final Cursor cursor, final String name, final String eventLocation, final String startDate, final String startTime, final String endDate, final String endTime, final String lat, final String lng )
    {String disdur="";
        Log.i("Event locA", "1");

        if ((cursor != null && cursor.getCount() > 0)) {
            cursor.moveToFirst();
            locNameA = cursor.getString(cursor.getColumnIndex("EventName"));
            locLocationA = cursor.getString(cursor.getColumnIndex("EventLocation"));
            locLatA = cursor.getString(cursor.getColumnIndex("Extra3"));
            locLngA = cursor.getString(cursor.getColumnIndex("Extra4"));
            locSDA = cursor.getString(cursor.getColumnIndex("StartDate"));
            locEDA = cursor.getString(cursor.getColumnIndex("EndDate"));
            locSTA = cursor.getString(cursor.getColumnIndex("StartTime"));
            locETA = cursor.getString(cursor.getColumnIndex("EndTime"));

            Log.i("Event laaa", "im in tho2");
            String locA_DT = locSDA + " " + locETA;
            String cur_A = startDate + " " + startTime;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());

            try {
                Date locADT = simpleDateFormat.parse(locA_DT);
                Date curA = simpleDateFormat.parse(cur_A);

                long durationA = curA.getTime() - locADT.getTime();
                diffInMinutesA = TimeUnit.MILLISECONDS.toMinutes(durationA);


                System.out.println("locSDT : " + simpleDateFormat.format(locADT));
                System.out.println("curA : " + simpleDateFormat.format(curA));
                System.out.println("diff:" + diffInMinutesA);
                Log.i("diffInMins :", "" + diffInMinutesA);
                diffA = "" + diffInMinutesA;
                Log.i("Event A Diff", diffA);

                String llA = locLatA + "," + locLngA;
                Log.i("EVENT A STRING", llA);
                Log.i("Event laaa", "im in tho3");


                Log.i("Event laaa", "im in tho5");
                // new getLocationDetails().execute(latA, lngA);
                disdur=  getjjson(llA);
                Log.i("json method: ",disdur);


            } catch (ParseException ex) {
                System.out.println("Exception " + ex);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("Event after", "wa");
            Log.i("Event before", locNameA + ": " + locLocationA);
        }

        return disdur;

    }
    public String getloctionB(final Cursor cursorB, final String name, final String eventLocation, final String startDate, final String startTime, final String endDate, final String endTime, final String lat, final String lng )
    {
        Log.i("Event locA", "1");
        String disdur="";

        cursorB.moveToFirst();
        locNameB = cursorB.getString(cursorB.getColumnIndex("EventName"));
        locLocationB = cursorB.getString(cursorB.getColumnIndex("EventLocation"));
        locLatB = cursorB.getString(cursorB.getColumnIndex("Extra3"));
        locLngB = cursorB.getString(cursorB.getColumnIndex("Extra4"));
        locSDB = cursorB.getString(cursorB.getColumnIndex("StartDate"));
        locEDB = cursorB.getString(cursorB.getColumnIndex("EndDate"));
        locSTB = cursorB.getString(cursorB.getColumnIndex("StartTime"));
        locETB = cursorB.getString(cursorB.getColumnIndex("EndTime"));
        Log.i("Event after", locNameB + ": " + locLocationB);

        Log.i("Event laaa", "im in tho1");
        String locB_DT = locSDB + " " + locSTB;
        String cur_B = startDate + " " + endTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());


        try {
            Date locBDT = simpleDateFormat.parse(locB_DT);
            Date curB = simpleDateFormat.parse(cur_B);


            long durationB = locBDT.getTime() - curB.getTime();
            diffInMinutesB = TimeUnit.MILLISECONDS.toMinutes(durationB);


            System.out.println("locSDT : " + simpleDateFormat.format(locBDT));
            System.out.println("curB : " + simpleDateFormat.format(curB));
            System.out.println("diff:" + diffInMinutesB);

            Log.i("diffInMins :", "" + diffInMinutesB);
            diffB = "" + diffInMinutesB;
            Log.i("Event B DIff", diffB);


            String llB = locLatB + "," + locLngB;
            Log.i("EVENT B STRING", "" + llB);

            Log.i("Event laaa", "im in thoB4");
            disdur=  getjjson(llB);
            Log.i("json method: ",disdur);


        } catch (ParseException ex) {
            System.out.println("Exception " + ex);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return disdur;

    }
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public String getjjson(String latlng) throws IOException, JSONException {
        Log.i("Event latlngLOG", "here");

        Log.i("Event latlngLOG", latlng);

        String text = null;

        String regAPIURL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + latlng + "&destinations=" + lat + "," + lng + "&mode=driving&key=AIzaSyDWjoAbJf9uDrLCFAM_fCSWxP0muVEGbOA";

        JSONObject js =readJsonFromUrl(regAPIURL);
        text=js.toString();
        Log.i("getjjson: text",text);
        String s = jason(text);
        Log.i("getjjson: s",s);
        return s;

    }


    public static JSONObject readJsonFromUrl(String regAPIURL) throws IOException, JSONException {

        InputStream is = new URL(regAPIURL).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public String jason(String s){
        String durationplusdistance="";
        if (s != null) {


            try {


                String distance = new JSONObject(s)
                        .getJSONArray("rows")
                        .getJSONObject(0)
                        .getJSONArray("elements")
                        .getJSONObject(0)
                        .getJSONObject("distance").getString("text");

                String durationinmin = new JSONObject(s)
                        .getJSONArray("rows")
                        .getJSONObject(0)
                        .getJSONArray("elements")
                        .getJSONObject(0)
                        .getJSONObject("duration").getString("value");

                String duration = new JSONObject(s)
                        .getJSONArray("rows")
                        .getJSONObject(0)
                        .getJSONArray("elements")
                        .getJSONObject(0)
                        .getJSONObject("duration").getString("text");
                Log.i("ResurltLOGDUR: ", duration);


                durationplusdistance = distance+"^"+duration+"^"+durationinmin;

                Log.i("ResurltLOGDUR: ", duration);



//
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return durationplusdistance;
    }
    public void locationConflict(String ra,String rb,final long unixtime,final String name,final String eventDescription,final String eventLocation,final String startDate,final String startTime,final String endDate,final String endTime,final String notify,final String invitedContacts,final String userEmail,final String lat,final String lng ){
        final Cursor c = db.conflictChecker(startDate, startTime, endDate, endTime);
        Log.i("Event loc", "check");

        if(!ra.isEmpty()&&!rb.isEmpty()){

            StringTokenizer a =new StringTokenizer(ra,"^");
            String distancea=a.nextToken();
            String durationa=a.nextToken();
            String durationinmina=a.nextToken();

            Log.i("locationCon duration: ",durationa);

            final StringTokenizer b =new StringTokenizer(rb,"^");
            String distanceb=b.nextToken();
            String durationb=b.nextToken();
            String durationinminb=b.nextToken();

            Log.i("locationCon duration: ",durationb);
//            Array[] hours =new Array[50];


            int mm = Integer.parseInt(durationinmina)/60;

            long timeInHours = diffInMinutesA/60;
            long timeInMinutes = diffInMinutesA%60;
            String time;
            if(timeInHours==0&&timeInMinutes!=0){
                time=timeInMinutes+" minutes";
            }else if(timeInHours!=0&&timeInMinutes==0){
                time=timeInHours+" hour(s)";
            }else if(timeInHours!=0&&timeInMinutes!=0){
                time=timeInHours+" hour(s) and "+timeInMinutes+" minutes";
            }else{
                time=timeInMinutes+" minutes";
            }

            int mmb = Integer.parseInt(durationinminb)/60;

            long timeInHoursb = diffInMinutesB/60;
            long timeInMinutesb = diffInMinutesB%60;
            String timeb;
            if(timeInHoursb==0&&timeInMinutesb!=0){
                timeb=timeInMinutesb+" minutes";
            }else if(timeInHoursb!=0&&timeInMinutesb==0){
                timeb=timeInHoursb+" hour (s)";
            }else if(timeInHoursb!=0&&timeInMinutesb!=0){
                timeb=timeInHoursb+" hours and "+timeInMinutesb+" minutes";
            }else{
                timeb=timeInMinutesb+" minutes";
            }
            if (((diffInMinutesA != 0) && (mm >= diffInMinutesA))&&((diffInMinutesB != 0) && (mmb >= diffInMinutesB))) {




                StringTokenizer x = new StringTokenizer(locLocationA, ",");
                String add_a = x.nextToken();

                StringTokenizer y  = new StringTokenizer(locLocationB, ",");
                String add_b = y.nextToken();


                new AlertDialog.Builder(mcontext)
                        .setTitle("Event Location Conflict")
                        .setIcon(R.drawable.location)
                        .setMessage("Previous event: " +locNameA+ " @ " + add_a + "\n"+ "Next event: " +locNameB+ " @ "
                                +add_b+ "\n" + "Needed Travel: "+durationa+ " and " + durationb+ " for " + distancea +" and "
                                + distanceb+ "\n"+ "You only have: " + time + " and "+  timeb+ ". \n \n Do you want to add anyway?")
//                        .setMessage("The Distance between " + locLocationA + " to " + eventLocation + " is " + distancea + ". It takes " + durationa + " to get to event " + name + " from event " + locNameA + " but you only have " + time + " travel time .\n The Distance between " +
//                                locNameA + " to " + eventLocation + " is " + distancea + ". It takes " + durationa + " to get to event " + name + " from event " + locNameA + " but you only have " + time + " travel time .Do you want to add it anyway?")
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("Add Anyway", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                if (c != null && c.getCount() > 0) {

                                    timeconflict(c, idEvent, en, des, locat, sDate, sTime, endDate, eTime, notify, iFriends, lat, lng);

                                } else {
                                    saveEventFunction(idEvent,  en,  des, locat,sDate, sTime,  endDate, eTime, notify, iFriends,  lat,lng);
                                }
                            }
                        })
                        .setIcon(R.drawable.location)
                        .show();


            }
            else if ((diffInMinutesA != 0) && (mm >= diffInMinutesA)) {

                Log.i("Event loc", "check");

                StringTokenizer z = new StringTokenizer(eventLocation, ",");
                String address = z.nextToken();

                StringTokenizer x = new StringTokenizer(locLocationA, ",");
                String add_a = x.nextToken();




                new AlertDialog.Builder(mcontext)
                        .setTitle("Event Location Conflict")
                        .setIcon(R.drawable.location)
                        .setMessage("The Distance between " + add_a + " to " + address + " is " + distancea + ". It takes " + durationa + " to get to event " + name + " from event " + locNameA + " but you only have " + time + " travel time. \n\nDo you want to add it anyway?")
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("Add Anyway", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                if (c != null && c.getCount() > 0) {

                                    timeconflict(c, idEvent, en, des, locat, sDate, sTime, endDate, eTime, notify, iFriends, lat, lng);
                                } else {
                                    saveEventFunction(idEvent,  en,  des, locat,sDate, sTime,  endDate, eTime, notify, iFriends,  lat,lng);
                                }
                            }
                        })
                        .setIcon(R.drawable.location)
                        .show();


            }
            else if ((diffInMinutesB != 0) && (mmb >= diffInMinutesB)) {

                Log.i("Event loc", "check");

                StringTokenizer z = new StringTokenizer(eventLocation, ",");
                String address = z.nextToken();


                StringTokenizer y  = new StringTokenizer(locLocationB, ",");
                String add_b = y.nextToken();


                new AlertDialog.Builder(mcontext)
                        .setTitle("Event Location Conflict")
                        .setIcon(R.drawable.location)
                        .setMessage("The Distance between " + add_b + " to " + address + " is " + distanceb + ". It takes " + durationb + " to get to event " + name + " from event " + locNameB + " but you only have " + timeb + " travel time. \n\n Do you want to add it anyway?")
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("Add Anyway", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                if (c != null && c.getCount() > 0) {

                                    timeconflict(c, idEvent, en, des, locat, sDate, sTime, endDate, eTime, notify, iFriends, lat, lng);
                                } else {
                                    saveEventFunction(idEvent,  en,  des, locat,sDate, sTime,  endDate, eTime, notify, iFriends,  lat,lng);
                                }
                            }
                        })
                        .setIcon(R.drawable.location)
                        .show();


            }else if (c != null && c.getCount() > 0) {

                timeconflict(c, idEvent, en, des, locat, sDate, sTime, endDate, eTime, notify, iFriends, lat, lng);
            }
            else {
                saveEventFunction(idEvent,  en,  des, locat,sDate, sTime,  endDate, eTime, notify, iFriends,  lat,lng);
            }
        }
        else if(!ra.isEmpty()){
            StringTokenizer s =new StringTokenizer(ra,"^");
            String distance=s.nextToken();
            String durationa=s.nextToken();
            String durationinmina=s.nextToken();
            Log.i("locationCon duration: ",durationa);

            int mm = Integer.parseInt(durationinmina)/60;

            long timeInHours = diffInMinutesA/60;
            long timeInMinutes = diffInMinutesA%60;
            String time;
            if(timeInHours==0&&timeInMinutes!=0){
                time=timeInMinutes+" minutes";
            }else if(timeInHours!=0&&timeInMinutes==0){
                time=timeInHours+" hours";
            }else if(timeInHours!=0&&timeInMinutes!=0){
                time=timeInHours+" hours and "+timeInMinutes+" minutes";
            }else{
                time=timeInMinutes+" minutes";
            }
            if ((diffInMinutesA != 0) && (mm >= diffInMinutesA)) {

                Log.i("Event loc", "check");

                StringTokenizer z = new StringTokenizer(eventLocation, ",");
                String address = z.nextToken();


                StringTokenizer y  = new StringTokenizer(locLocationA, ",");
                String add_a = y.nextToken();

                new AlertDialog.Builder(mcontext)
                        .setTitle("Event Location Conflict")
                        .setIcon(R.drawable.location)
                        .setMessage("The Distance between " + add_a + " to " + address + " is " + distance + ". It takes " + durationa + " to get to event " + name + " from event " + locNameA + " but you only have " + time + " travel time.\n\n Do you want to add it anyway?")
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("Add Anyway", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                if (c != null && c.getCount() > 0) {

                                    timeconflict(c, idEvent, en, des, locat, sDate, sTime, endDate, eTime, notify, iFriends, lat, lng);
                                } else {
                                    saveEventFunction(idEvent,  en,  des, locat,sDate, sTime,  endDate, eTime, notify, iFriends,  lat,lng);
                                }
                            }
                        })
                        .setIcon(R.drawable.location)
                        .show();


            }else if (c != null && c.getCount() > 0) {

                timeconflict(c, idEvent, en, des, locat, sDate, sTime, endDate, eTime, notify, iFriends, lat, lng);
            }else {
                saveEventFunction(idEvent,  en,  des, locat,sDate, sTime,  endDate, eTime, notify, iFriends,  lat,lng);
            }

        }
        else if(!rb.isEmpty()){
            StringTokenizer s =new StringTokenizer(rb,"^");
            String distanceb=s.nextToken();
            String durationb=s.nextToken();
            String durationinminb=s.nextToken();
            Log.i("locationCon duration: ",durationb);
            StringTokenizer st =new StringTokenizer(durationb," ");
            int mm = Integer.parseInt(durationinminb)/60;
            long timeInHours = diffInMinutesB/60;
            long timeInMinutes = diffInMinutesB%60;
            String time;
            if(timeInHours==0&&timeInMinutes!=0){
                time=timeInMinutes+" minutes";
            }else if(timeInHours!=0&&timeInMinutes==0){
                time=timeInHours+" hours";
            }else if(timeInHours!=0&&timeInMinutes!=0){
                time=timeInHours+" hours and "+timeInMinutes+" minutes";
            }else{
                time=timeInMinutes+" minutes";
            }
            if ((diffInMinutesB != 0) && (mm >= diffInMinutesB)) {

                Log.i("Event loc", "check");

                StringTokenizer z = new StringTokenizer(eventLocation, ",");
                String address = z.nextToken();


                StringTokenizer y  = new StringTokenizer(locLocationB, ",");
                String add_b = y.nextToken();

                new AlertDialog.Builder(mcontext)
                        .setTitle("Event Location Conflict")
                        .setIcon(R.drawable.location)
                        .setMessage("The Distance between " + add_b + " to " + address + " is " + distanceb + ". It takes " + durationb + " to get to event " + name + " from event " + locNameA + " but you only have " + time + " travel time. \n\nDo you want to add it anyway?")
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("Add Anyway", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                if (c != null && c.getCount() > 0) {

                                    timeconflict(c, idEvent, en, des, locat, sDate, sTime, endDate, eTime, notify, iFriends, lat, lng);
                                } else {
                                    saveEventFunction(idEvent,  en,  des, locat,sDate, sTime,  endDate, eTime, notify, iFriends,  lat,lng);
                                }
                            }
                        })
                        .setIcon(R.drawable.location)
                        .show();


            }else if (c != null && c.getCount() > 0) {

                timeconflict(c, idEvent, en, des, locat, sDate, sTime, endDate, eTime, notify, iFriends, lat, lng);
            }else {
                saveEventFunction(idEvent,  en,  des, locat,sDate, sTime,  endDate, eTime, notify, iFriends,  lat,lng);
            }
        }
    }

}
