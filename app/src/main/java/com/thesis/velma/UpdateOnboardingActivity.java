package com.thesis.velma;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.thesis.velma.helper.DataBaseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static com.thesis.velma.UpdateOnboardingFragment2.dateEnd;
import static com.thesis.velma.UpdateOnboardingFragment2.dateStart;

public class UpdateOnboardingActivity extends AppCompatActivity {

    private ViewPager pager;
    private SmartTabLayout indicator;
    public Button skip;
    public Button BtnAddEvent;
    public EditText event;
    Context context;

    public static DataBaseHandler db;
    String id, latPass, lngPass;
    long eventID;

    public static TextView des;
    public static EditText descrip;
    public static TextView loc;
    public static TextView locate;
    public static TextView distanceduration;
    int PLACE_PICKER_REQUEST = 1;
    Double latitude, longtiude;

    public static String geolocation;
    String modetravel = "driving";
    PlaceAutocompleteFragment autocompleteFragment;
    private CoordinatorLayout coordinatorLayout;

    public static final String PREFS_NAME = "AOP_PREFS";
    public static final String PREFS_KEY = "AOP_PREFS_String";


    String locNameA = "", locLatA = "", locLngA = "", locLocationA = "", locSDA = "", locEDA = "", locSTA  = "", locETA = "";
    String locNameB= "", locLatB= "", locLngB= "", locLocationB= "", locSDB= "", locEDB= "", locSTB= "", locETB= "";
    long diffInMinutesA, diffInMinutesB;
    String diffA, diffB;
    double latA, lngA, latB, lngB;
    ArrayList<String> myConflictEvents = new ArrayList<String>();
    ArrayList<String> myCurrentEvent = new ArrayList<>();
    Dialog dialog;
    String e_name, e_desc, e_sd, e_ed, e_st, e_et, event_ID;

    long travelduration;

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DataBaseHandler(context);

        setContentView(R.layout.activity_update_onboarding);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        context = this;

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("key");
//        latPass = bundle.getString("lat");
//        lngPass = bundle.getString("lng");


        Log.d("id received: ", id);
//        Log.d("Lat: ", latPass);
//        Log.d("Long: ", lngPass);

        String nameTitle="", description="", location="";

        Cursor c = db.getEventDetails(Long.valueOf(id));
        while (c.moveToNext()){
            nameTitle = c.getString(c.getColumnIndex("EventName"));
            description = c.getString(c.getColumnIndex("EventDescription"));
            location = c.getString(c.getColumnIndex("EventLocation"));
            eventID = c.getLong(c.getColumnIndex("EventID"));
            latPass = c.getString(c.getColumnIndex("Extra3"));
            lngPass = c.getString(c.getColumnIndex("Extra4"));
        }

        latitude = Double.valueOf(latPass.trim()).doubleValue();
        longtiude = Double.valueOf(lngPass.trim()).doubleValue();

        Log.d("LatAndLongDb: ", "" + latitude + ":" + longtiude);

        Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString("key", id);

        editor.apply();

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/avenir-next-regular.ttf");

        event = (EditText) findViewById(R.id.eventname);
        event.setTypeface(custom_font);
        event.setText(nameTitle);
        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (SmartTabLayout) findViewById(R.id.indicator);
        BtnAddEvent = (Button) findViewById(R.id.btnAddEvent);
        BtnAddEvent.setTypeface(custom_font);
        BtnAddEvent.setVisibility(View.GONE);

        des = (TextView) findViewById(R.id.eventName);
        BtnAddEvent.setTypeface(custom_font);
        descrip = (EditText) findViewById(R.id.name);
        descrip.setTypeface(custom_font);
        descrip.setText(description);
        loc = (TextView) findViewById(R.id.location);
        loc.setTypeface(custom_font);
        locate = (TextView) findViewById(R.id.locationText);
        locate.setTypeface(custom_font);
        locate.setText(location);
        locate.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        distanceduration = (TextView) findViewById(R.id.distanceduration);
        distanceduration .setTypeface(custom_font);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        autocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(14.599512, 120.984222),
                new LatLng(14.599512, 120.984222)));


        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE)
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                .build();
        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getAddress());//get place details here

                locate.setText("" + place.getAddress());
                geolocation = place.getAddress().toString();

                latitude = place.getLatLng().latitude;
                longtiude = place.getLatLng().longitude;


                Log.d("latlangNew", "" + latitude + ":" + longtiude);

                new UpdateOnboardingActivity.getDetails().execute();

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new UpdateOnboardingFragment2();
                    case 1:
                        return new UpdateOnboardingFragment3();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        pager.setAdapter(adapter);

        indicator.setViewPager(pager);

        indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    BtnAddEvent.setVisibility(View.GONE);
                } else if (position == 1) {
                    BtnAddEvent.setVisibility(View.VISIBLE);
                }

            }

        });



        BtnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setMessage("Are you sure you want to update?")
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                db = new DataBaseHandler(context);
                final String name = event.getText().toString();
                final String eventDescription = descrip.getText().toString();
                final String eventLocation = locate.getText().toString();
                final String startDate = dateStart.getText().toString();
                final String endDate = dateEnd.getText().toString();
                final String startTime = UpdateOnboardingFragment2.timeStart.getText().toString();
                final String endTime = UpdateOnboardingFragment2.timeEnd.getText().toString();
                final String notify = UpdateOnboardingFragment2.alarming.getText().toString();
                final String invitedContacts = UpdateOnboardingFragment3.mtxtinvited.getText().toString();
                // If the user will input new event location
                final String lat = ""+latitude;
                final String lng = ""+longtiude;

                //Toast.makeText(getBaseContext(), startDate + ":" + endDate, Toast.LENGTH_SHORT).show();


                Log.i("StarTime", startDate + " " + startTime);
                Log.i("EndTime", endDate + " " + endTime);
                Log.d("Ambot aning Update: ", name);



                                if (name.isEmpty()) {
                                    event.setError("Enter event name");
                                }
                                if (eventDescription.isEmpty()) {
                                    descrip.setError("Enter description");
                                }
                                if(eventLocation.isEmpty()) {
                                    Snackbar snackbar = Snackbar
                                            .make(coordinatorLayout, "Please add location", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                                if (startDate.isEmpty() || endDate.isEmpty() || startTime.isEmpty() || endTime.isEmpty
                                        ()) {
                                    Snackbar snackbar = Snackbar
                                            .make(coordinatorLayout, "Please add date and time details",
                                                    Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                                StringTokenizer sd = new StringTokenizer(startDate, "-");
                                StringTokenizer ed = new StringTokenizer(endDate, "-");
                                StringTokenizer st = new StringTokenizer(startTime, ":");
                                StringTokenizer et = new StringTokenizer(endTime, ":");

                                String sdate = sd.nextToken() + sd.nextToken() + sd.nextToken();
                                String edate = ed.nextToken() + ed.nextToken() + ed.nextToken();
                                String stime = st.nextToken() + st.nextToken();
                                String etime = et.nextToken() + et.nextToken();

                                int sdd = Integer.parseInt(sdate);
                                int edd = Integer.parseInt(edate);
                                int stt = Integer.parseInt(stime);
                                int ett = Integer.parseInt(etime);
                                if (sdd > edd) {
                                    Snackbar snackbar = Snackbar
                                            .make(coordinatorLayout, "Invalid input: Start date is greater than end date",
                                                    Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                } else if (stt > ett) {
                                    Snackbar snackbar = Snackbar
                                            .make(coordinatorLayout, "Invalid input: Start time is greater than end time",
                                                    Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                } else {

                                    Random r = new Random();




                                   // final Cursor c = db.updateconflictCheckerA(startDate, startTime, endDate, endTime,eventID);
                                    final Cursor cursor = db.compareLocationA(startDate, startTime);
                                    final Cursor cursorB = db.compareLocationB(startDate, endTime);

//
//                                    if ((cursor != null && cursor.getCount() > 0) || (cursorB != null && cursorB.getCount() > 0)) {
//
//                                        if ((cursor != null && cursor.getCount() > 0) && (cursorB != null && cursorB.getCount() > 0)) {
//                                            String resultA = getloctionA(cursor, name, eventLocation, startDate, startTime, endDate, endTime, lat, lng);
//                                            String resultB = getloctionB(cursorB, name, eventLocation, startDate, startTime, endDate, endTime, lat, lng);
//                                            locationConflict(resultA, resultB, eventID, name, eventDescription, eventLocation, startDate, startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//                                        } else if ((cursorB != null && cursorB.getCount() > 0)) {
//                                            String resultB = getloctionB(cursorB, name, eventLocation, startDate, startTime, endDate, endTime, lat, lng);
//                                            String resultA = "";
//                                            locationConflict(resultA, resultB, eventID, name, eventDescription, eventLocation, startDate, startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//                                        } else if ((cursor != null && cursor.getCount() > 0)) {
//                                            String resultA = getloctionA(cursor, name, eventLocation, startDate, startTime, endDate, endTime, lat, lng);
//                                            String resultB = "";
//                                            locationConflict(resultA, resultB, eventID, name, eventDescription, eventLocation, startDate, startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//                                        }
//                                    } else if ((cursor != null && cursor.getCount() > 0)) {
//                                        getloctionA(cursor, name, eventLocation, startDate, startTime, endDate, endTime, lat, lng);
//
//                                    } else if (c != null && c.getCount() > 0) {
//                                        timeconflict(c, eventID, name, eventDescription, eventLocation, startDate, startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//                                    } else {
//
//                                        saveEventFunction(eventID, name, eventDescription, eventLocation, startDate, startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//
//
//                                    }
                                }




//                    String[] mydates = startDate.split("-");
//                    String[] mytimes = startTime.split(":");
//
//
//                    //HARDCODED VALUES 10:51
//                    Calendar calNow = Calendar.getInstance();
//                    Calendar calSet = (Calendar) calNow.clone();
//
//
//                    Log.d("Calendar.YEAR", "" + Integer.parseInt(mydates[2]));
//                    Log.d("Calendar.MONTH", "" + Integer.parseInt(mydates[1]));
//                    Log.d("Calendar.DATE", "" + Integer.parseInt(mydates[0]));
//                    Log.d("Calendar.HOUR_OF_DAY", "" + Integer.parseInt(mytimes[0]));
//                    Log.d("Calendar.MINUTE", "" + Integer.parseInt(mytimes[1]));
//
//                    String date = "" + mydates[0] + "-" + mydates[1] + "-" + mydates[2];
//
//                    int AM_PM;
//                    if (Integer.parseInt(mytimes[0]) < 12) {
//                        AM_PM = 0;
//                    } else {
//                        AM_PM = 1;
//                    }
//
////                calSet.set(Calendar.YEAR, Integer.parseInt(mydates[2]));
////                calSet.set(Calendar.MONTH, Integer.parseInt(mydates[1])-1);
////                calSet.set(Calendar.DATE, Integer.parseInt(mydates[0]));
////                calSet.set(Calendar.HOUR, Integer.parseInt(mytimes[0]));
////                calSet.set(Calendar.MINUTE, Integer.parseInt(mytimes[1]));
////                calSet.set(Calendar.SECOND, 0);
////                calSet.set(Calendar.MILLISECOND, 0);
////                calSet.set(Calendar.AM_PM, AM_PM);
//
//                    calSet.setTimeInMillis(System.currentTimeMillis());
//                    calSet.clear();
//                    calSet.set(Integer.parseInt(mydates[2]), Integer.parseInt(mydates[1]) - 1, Integer.parseInt(mydates[0]), Integer.parseInt(mytimes[0]), Integer.parseInt(mytimes[1]) - 30);
//
//                    int count = LandingActivity.db.retrieveDayEvent();
////                LandingActivity.db.close();
//
//
//                    Intent myIntent = new Intent(context, AlarmReceiver.class);
//
//
//                    myIntent.putExtra("unix", unixtime);
//                    myIntent.putExtra("name", name);
//                    myIntent.putExtra("description", eventDescription);
//                    myIntent.putExtra("location", eventLocation);
//                    myIntent.putExtra("start", startTime);
//                    myIntent.putExtra("end", endTime);
//                    myIntent.putExtra("dateS", startDate);
//                    myIntent.putExtra("dateE", endDate);
//                    myIntent.putExtra("people", invitedContacts);
//
//                    Log.d("MyData", name);
//
//                    pendingIntent = PendingIntent.getBroadcast(context, count, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    Log.d("Count", "" + count);
//
//                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
//


//
//                    Log.i("Event laaa", "im in tho");
//                    if ((cursor != null && cursor.getCount() > 0)) {
//                        cursor.moveToFirst();
//                        locNameA = cursor.getString(cursor.getColumnIndex("EventName"));
//                        locLocationA = cursor.getString(cursor.getColumnIndex("EventLocation"));
//                        locLatA = cursor.getString(cursor.getColumnIndex("Extra3"));
//                        locLngA = cursor.getString(cursor.getColumnIndex("Extra4"));
//                        locSDA = cursor.getString(cursor.getColumnIndex("StartDate"));
//                        locEDA = cursor.getString(cursor.getColumnIndex("EndDate"));
//                        locSTA = cursor.getString(cursor.getColumnIndex("StartTime"));
//                        locETA = cursor.getString(cursor.getColumnIndex("EndTime"));
//                        Log.i("Event update", "if cursor a");
//                        Log.i("Event laaa", "im in tho2");
//                        String locA_DT = locSDA + " " + locETA;
//                        String cur_A = startDate + " " + startTime;
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
//
//                        try {
//                            Date locADT = simpleDateFormat.parse(locA_DT);
//                            Date curA = simpleDateFormat.parse(cur_A);
//
//                            long durationA = curA.getTime() - locADT.getTime();
//                            diffInMinutesA = TimeUnit.MILLISECONDS.toMinutes(durationA);
//
//
//                            System.out.println("locSDT : " + simpleDateFormat.format(locADT));
//                            System.out.println("curA : " + simpleDateFormat.format(curA));
//                            System.out.println("diff:" + diffInMinutesA);
//                            Log.i("diffInMins :", "" + diffInMinutesA);
//                            diffA = "" + diffInMinutesA;
//                            Log.i("Event A Diff", diffA);
//
//                            String llA = locLatA + "," + locLngA;
//
//                            Log.i("Event laaa", "im in tho3");
//                            latA = Double.parseDouble(locLatA);
//                            lngA = Double.parseDouble(locLngA);
//                            Log.i("EVENT A STRING", llA);
//
//                            Log.i("Event laaa", "im in tho4");
//                            try {
//                                latA = Double.parseDouble(locLatA);
//                                lngA = Double.parseDouble(locLngA);
//
//                                Log.i("EventLAT A", "" + latA + " , " + lngA);
//                            } catch (NumberFormatException e) {
//                                Log.i("EventLAT A", "" + e);
//                            }
//
//                            Log.i("Event laaa", "im in tho5");
//                            new getLocationDetails().execute(latA, lngA);
//
//                        } catch (ParseException ex) {
//                            System.out.println("Exception " + ex);
//                        }
//                        Log.i("Event after", "wa");
//                        Log.i("Event before", locNameA + ": " + locLocationA);
//                    }
//
//                    if ((cursorB != null && cursorB.getCount() > 0)) {
//                        cursorB.moveToFirst();
//                        locNameB = cursorB.getString(cursorB.getColumnIndex("EventName"));
//                        locLocationB = cursorB.getString(cursorB.getColumnIndex("EventLocation"));
//                        locLatB = cursorB.getString(cursorB.getColumnIndex("Extra3"));
//                        locLngB = cursorB.getString(cursorB.getColumnIndex("Extra4"));
//                        locSDB = cursorB.getString(cursorB.getColumnIndex("StartDate"));
//                        locEDB = cursorB.getString(cursorB.getColumnIndex("EndDate"));
//                        locSTB = cursorB.getString(cursorB.getColumnIndex("StartTime"));
//                        locETB = cursorB.getString(cursorB.getColumnIndex("EndTime"));
//                        Log.i("Event after", locNameB + ": " + locLocationB);
//                        Log.i("Event update", "if cursor b");
//
//                        Log.i("Event laaa", "im in tho1");
//                        String locB_DT = locSDB + " " + locSTB;
//                        String cur_B = startDate + " " + endTime;
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
//
//
//                        try {
//                            Date locBDT = simpleDateFormat.parse(locB_DT);
//                            Date curB = simpleDateFormat.parse(cur_B);
//
//
//                            long durationB = locBDT.getTime() - curB.getTime();
//                            diffInMinutesB = TimeUnit.MILLISECONDS.toMinutes(durationB);
//
//
//                            System.out.println("locSDT : " + simpleDateFormat.format(locBDT));
//                            System.out.println("curB : " + simpleDateFormat.format(curB));
//                            System.out.println("diff:" + diffInMinutesB);
//
//                            Log.i("diffInMins :", "" + diffInMinutesB);
//                            diffB = "" + diffInMinutesB;
//                            Log.i("Event B DIff", diffB);
//
//
//                            String llB = locLatB + "," + locLngB;
//                            Log.i("EVENT B STRING", "" + llB);
////                            double latB = Double.parseDouble(locLatB);
////                            double lngB = Double.parseDouble(locLngB);
//
//                            latB = Double.parseDouble(locLatB);
//                            lngB = Double.parseDouble(locLngB);
//
//                            try {
//                                latB = Double.parseDouble(locLatB);
//                                lngB = Double.parseDouble(locLngB);
//
//                                Log.i("Event laaa", "im in thoB3");
//
//                                Log.i("EventLAT B", "" + latB + " , " + lngB);
//                            } catch (NumberFormatException e) {
//                                Log.i("EventLAT B", "" + e);
//                            }
//
//                            Log.i("Event laaa", "im in thoB4");
//                            new getLocationDetails().execute(latB, lngB);
//
//
//                        } catch (ParseException ex) {
//                            System.out.println("Exception " + ex);
//                        }
//
//                        Log.i("Event before", "wa");
//                    }
//
//                    else if (c != null && c.getCount() > 0) {
//                        Log.i("Event update", "else timeconflict");
//                        timeconflict(c, unixtime, name, eventDescription, eventLocation, startDate, startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//                    } else {
//                        Toast.makeText(context, "Save Event on click way conflict ky mana ug check", Toast.LENGTH_SHORT).show();
//                        saveEventFunction(unixtime, name, eventDescription, eventLocation, startDate, startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//
//
//
//                    }


                            }
                        })
                        .setPositiveButton("No", null)
                        .show();



//                    new AlertDialog.Builder(context)
//                            .setMessage("Are you sure you want to update event?")
//                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    LandingActivity.db.updateEvent(unixtime, name, eventDescription, eventLocation, startDate, startTime, endDate, endTime, notify, invitedContacts, Double.valueOf(lat), Double.valueOf(lng));
//                                    OkHttp.getInstance(getBaseContext()).updateEvent(unixtime, name, eventDescription, eventLocation, startDate, startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//
//                                    for (int i = 0; i <= OnboardingFragment3.invitedContacts.size() - 1; i++) {
//                                        String[] target = OnboardingFragment3.invitedContacts.get(i).split("@");
//                                        OkHttp.getInstance(context).sendNotificationUpdate("Update",eventID, name, eventDescription, eventLocation,
//                                                startDate, startTime, endDate, endTime, notify, invitedContacts,target[0]+"Velma");//target[0]
//                                    }
//
//                                    Intent intent = new Intent(UpdateOnboardingActivity.this, LandingActivity.class);
//                                    setResult(RESULT_OK, intent);
//                                    finish();
//                                    startActivity(intent);
//
//                                }
//                            })
//                            .setPositiveButton("No", null)
//                            .show();

       //         }


            }
        });


    }

    class getDetails extends AsyncTask<Void, Void, String> {

        protected String getASCIIContentFromEntity(HttpEntity entity)
                throws IllegalStateException, IOException {
            InputStream in = entity.getContent();
            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n > 0) {
                byte[] b = new byte[4096];
                n = in.read(b);
                if (n > 0)
                    out.append(new String(b, 0, n));
            }
            return out.toString();
        }

        @Override
        protected String doInBackground(Void... params) {

            String text = null;
            String coordinates = latitude + "," + longtiude;
            Log.d("Coordinates", coordinates);
            try {
                String regAPIURL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + LandingActivity.origlatitude + "," + LandingActivity.origlongitude;
                regAPIURL = regAPIURL + "&destinations=" + URLEncoder.encode(coordinates);
                regAPIURL = regAPIURL + "&mode=" + URLEncoder.encode(modetravel);
                regAPIURL = regAPIURL + "traffic_model="+ URLEncoder.encode("pessimistic");
                regAPIURL = regAPIURL + "&key=AIzaSyDWjoAbJf9uDrLCFAM_fCSWxP0muVEGbOA";
                Log.d("URI", regAPIURL);
                HttpGet httpGet = new HttpGet(regAPIURL);
                HttpParams httpParameters = new BasicHttpParams();
                int timeoutConnection = 60000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                int timeoutSocket = 60000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);

            } catch (Exception e) {
                text = null;
            }

            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Result", s);


            if (s != null) {


                try {


                    String distance = new JSONObject(s)
                            .getJSONArray("rows")
                            .getJSONObject(0)
                            .getJSONArray("elements")
                            .getJSONObject(0)
                            .getJSONObject("distance").getString("text");

                    String duration = new JSONObject(s)
                            .getJSONArray("rows")
                            .getJSONObject(0)
                            .getJSONArray("elements")
                            .getJSONObject(0)
                            .getJSONObject("duration").getString("text");

                    distanceduration.setText("Distance : " + distance + ": Duration : " + duration);
                    distanceduration.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
    }

    public void timeconflict(final Cursor c, final long eventID, final String name, final String eventDescription, final String eventLocation, final String startDate, final String startTime, final String endDate, final String endTime, final String notify, final String invitedContacts, final String lat, final String lng){
        Log.i("Event con", "2");
        final String flag = "update";

        myCurrentEvent.add(name);
        myCurrentEvent.add(eventDescription);
        myCurrentEvent.add(eventLocation);
        myCurrentEvent.add(startDate);
        myCurrentEvent.add(endDate);
        myCurrentEvent.add(startTime);
        myCurrentEvent.add(endTime);
        myCurrentEvent.add(notify);
        myCurrentEvent.add(invitedContacts);
        c.moveToFirst();
        while (!c.isAfterLast()) {

//            Toast.makeText(OnboardingActivity.this, "Conflict in: " + c.getString(1) + "   " + c.getString(2) + "  " + c.getString(3) + "  " + c.getString(4), Toast.LENGTH_LONG).show();
//            Log.i("Event log", c.getString(0) + " >> " + c.getString(1) + " >> " + c.getString(2) + " >> " + c.getString(3) + " >> " + c.getString(4) + " >> ");


            c.moveToNext();
        }


        final AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Event Time Conflict")
                .setIcon(R.drawable.time)
                .setMessage("The event you created ago has the following conflict(s).")
                .setPositiveButton("Add Anyway", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        saveEventFunction(eventID, name, eventDescription, eventLocation, startDate, startTime, endDate, endTime, notify, invitedContacts, lat, lng);
                        Toast.makeText(context, "Save Event time con add anyway", Toast.LENGTH_SHORT).show();



                        dialog.dismiss();


                    }


                }).setNeutralButton("Suggest",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                                editor.putString("name", name);
                                editor.putString("desc", eventDescription);
                                editor.putString("loc", eventLocation);
                                editor.putString("startdate", startDate);
                                editor.putString("enddate", endDate);
                                editor.putString("invitedfriends", invitedContacts);
                                editor.putString("flag",flag);
                                editor.putString("key",""+eventID);
                                editor.apply();
                                Intent in = new Intent(UpdateOnboardingActivity.this, DateListView.class);
//
                                startActivity(in);
//
                            }
                        }

                );

        //    builder.setTitle("Select Event Priority");


////////////////////////////////////////////////////////////////////////////////////
        ListView modeList = new ListView(context);
        myConflictEvents.clear();
        myConflictEvents.add(myCurrentEvent.get(0));
        c.moveToFirst();
        event_ID = c.getString(0);
        Log.i("Event con", "5");

        while (!c.isAfterLast()) {


            StringTokenizer display1 = new StringTokenizer(c.getString(c.getColumnIndex("StartTime")),":");
            String s_hour = display1.nextToken();
            String s_min = display1.nextToken();
            StringTokenizer display2 = new StringTokenizer(c.getString(c.getColumnIndex("EndTime")),":");
            String e_hour = display2.nextToken();
            String e_min = display2.nextToken();
            String sdisplay="";
            String edisplay="";
            if(s_hour.length() == 1){
                s_hour = "0"+s_hour;
            }
            if(s_min.length() == 1){
                s_min = "0"+s_min;
            }
            if( Integer.parseInt(s_hour) > 12){
                s_hour = ""+(Integer.parseInt(s_hour)-12);

                sdisplay=s_hour +":"+ s_min +" PM";

            }else if(Integer.parseInt(s_hour)==12){
                sdisplay=s_hour +":"+ s_min +" PM";
            }else {
                sdisplay=s_hour +":"+ s_min +" AM";
            } if(e_hour.length() == 1){
                e_hour = "0"+e_hour;
            }
            if(e_min.length() == 1){
                e_min = "0"+e_min;
            }
            if( Integer.parseInt(e_hour) > 12){
                e_hour = ""+(Integer.parseInt(e_hour)-12);

                edisplay=e_hour +":"+ e_min +" PM";

            }else if(Integer.parseInt(e_hour)==12){
                edisplay=e_hour +":"+ e_min +" PM";
            }else {
                edisplay=e_hour +":"+ e_min +" AM";
            }


            Log.i("Event conn", " " + c.getString(c.getColumnIndex("EventName")));
//            Log.i("Event id", eventID);

            myConflictEvents.add(c.getString(c.getColumnIndex("EventName"))+"    "+sdisplay+" to "+edisplay); //this adds an element to the list.
            c.moveToNext();
        }

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, myConflictEvents);
        modeList.setAdapter(modeAdapter);

        builder.setView(modeList);

        builder.create();
        builder.show();
    }

    public void saveEventFunction(final long eventID, final String name, final String eventDescription, final String eventLocation, final String startDate, final String startTime, final String endDate, final String endTime, final String notify, final String invitedContacts, final String lat, final String lng){


        String[] mydates = startDate.split("-");
        String[] mytimes = startTime.split(":");

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

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra("unix", eventID);
        myIntent.putExtra("name", name);
        myIntent.putExtra("description", eventDescription);
        myIntent.putExtra("location", eventLocation);
        myIntent.putExtra("start", startTime);
        myIntent.putExtra("end", endTime);
        myIntent.putExtra("dateS", startDate);
        myIntent.putExtra("dateE", endDate);
        myIntent.putExtra("people", invitedContacts);
        Log.d("MyData", name);
        pendingIntent = PendingIntent.getBroadcast(context, count+1, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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

        Log.d("AlaramJeanne", "" + calSet.getTimeInMillis());
        Log.d("AlaramJeanne1", "" + System.currentTimeMillis());

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
                     Toast.makeText(context, "Save Event function", Toast.LENGTH_SHORT).show();

//                    LandingActivity.db.updateEvent(Long.valueOf(id), name, eventDescription, eventLocation, startDate, startTime, endDate, endTime,invitedContacts,notify, lat, lng);
//
//                    OkHttp.getInstance(context).deleteEvent(""+ this.eventID);
//                    OkHttp.getInstance(getBaseContext()).saveEvent(eventID, name, eventDescription, eventLocation, startDate, startTime, endDate, endTime, invitedContacts, notify, LandingActivity.useremail, lat, lng);



//                        for (int i = 0; i <= OnboardingFragment3.invitedContacts.size() - 1; i++) {
//                            String[] target = OnboardingFragment3.invitedContacts.get(i).split("@");
//                            OkHttp.getInstance(context).sendNotificationUpdate("Update", eventID, name, eventDescription, eventLocation,
//                                    startDate, startTime, endDate, endTime, notify, invitedContacts, target[0] + "Velma", lat, lng);
//                        }

                        Intent intent = new Intent(UpdateOnboardingActivity.this, LandingActivity.class);
                        setResult(RESULT_OK, intent);
                        finish();
                        startActivity(intent);


    }



    public String getloctionA(final Cursor cursor, final String name, final String eventLocation, final
    String startDate, final String startTime, final String endDate, final String endTime, final String lat,
                              final String lng )
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                    java.util.Locale.getDefault());

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
    public String getloctionB(final Cursor cursorB, final String name, final String eventLocation, final
    String startDate, final String startTime, final String endDate, final String endTime, final String lat,
                              final String lng )
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                java.util.Locale.getDefault());


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

        String regAPIURL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + latlng + "&destinations=" + latitude + "," + longtiude +
                "&mode=driving&traffic_model=pessimistic&&key=AIzaSyDWjoAbJf9uDrLCFAM_fCSWxP0muVEGbOA";

        JSONObject js =readJsonFromUrl(regAPIURL);
        text=js.toString();
        Log.i("getjjson: text",text);
        String s = jason(text);
        Log.i("getjjson: s",s);
        return s;

    }


    public static JSONObject readJsonFromUrl(String regAPIURL) throws IOException, JSONException
    {

        InputStream is = new URL(regAPIURL).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName
                    ("UTF-8")));
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
//    public void locationConflict(String ra,String rb,final long unixtime,final String name,final String
//            eventDescription,final String eventLocation,final String startDate,final String startTime,final String
//                                         endDate,final String endTime,final String notify,final String invitedContacts, final String lat,final String lng ){
//        //final Cursor c = db.updateconflictChecker(startDate, startTime, endDate, endTime,unixtime);
//        Log.i("Event loc", "check");
//
//        if(!ra.isEmpty()&&!rb.isEmpty()){
//
//            StringTokenizer a =new StringTokenizer(ra,"^");
//            String distancea=a.nextToken();
//            String durationa=a.nextToken();
//            String durationinmina=a.nextToken();
//
//            Log.i("locationCon duration: ",durationa);
//
//            StringTokenizer b =new StringTokenizer(rb,"^");
//            String distanceb=b.nextToken();
//            String durationb=b.nextToken();
//            String durationinminb=b.nextToken();
//
//            Log.i("locationCon duration: ",durationb);
////            Array[] hours =new Array[50];
//
//
//            int mm = Integer.parseInt(durationinmina)/60;
//
//            long timeInHours = diffInMinutesA/60;
//            long timeInMinutes = diffInMinutesA%60;
//            String time;
//            if(timeInHours==0&&timeInMinutes!=0){
//                time=timeInMinutes+" minutes";
//            }else if(timeInHours!=0&&timeInMinutes==0){
//                time=timeInHours+" hour(s)";
//            }else if(timeInHours!=0&&timeInMinutes!=0){
//                time=timeInHours+" hour(s) and "+timeInMinutes+" minutes";
//            }else{
//                time=timeInMinutes+" minutes";
//            }
//
//            int mmb = Integer.parseInt(durationinminb)/60;
//
//            long timeInHoursb = diffInMinutesB/60;
//            long timeInMinutesb = diffInMinutesB%60;
//            String timeb;
//            if(timeInHoursb==0&&timeInMinutesb!=0){
//                timeb=timeInMinutesb+" minutes";
//            }else if(timeInHoursb!=0&&timeInMinutesb==0){
//                timeb=timeInHoursb+" hour(s)";
//            }else if(timeInHoursb!=0&&timeInMinutesb!=0){
//                timeb=timeInHoursb+" hour(s) and "+timeInMinutesb+" minutes";
//            }else{
//                timeb=timeInMinutesb+" minutes";
//            }
//            if (((diffInMinutesA != 0) && (mm >= diffInMinutesA))&&((diffInMinutesB != 0) && (mmb
//                    >= diffInMinutesB))) {
//
//
//
//
//                StringTokenizer x = new StringTokenizer(locLocationA, ",");
//                String add_a = x.nextToken();
//
//                StringTokenizer y  = new StringTokenizer(locLocationB, ",");
//                String add_b = y.nextToken();
//
//
//                new AlertDialog.Builder(context)
//                        .setTitle("Event Location Conflict")
//                        .setIcon(R.drawable.location)
//                        .setMessage("Previous event: " +locNameA+ " @ " + add_a + "\n"+ "Next event: "
//                                +locNameB+ " @ "
//                                +add_b+ "\n" + "Needed Travel: "+durationa+ " and " + durationb+ " for " +
//                                distancea +" and "
//                                + distanceb+ "\n"+ "You only have: " + time + " and "+  timeb+ ". \n \n Do you want to add anyway?")
//////
//                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setNegativeButton("Add Anyway", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//
//                                if (c != null && c.getCount() > 0) {
//
//                                    timeconflict(c, unixtime, name, eventDescription, eventLocation, startDate,
//                                            startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//
//                                } else {
//                                    saveEventFunction(unixtime, name, eventDescription, eventLocation,
//                                            startDate, startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//                                }
//                            }
//                        })
//                        .setIcon(R.drawable.location)
//                        .show();
//
//
//            }
//            else if ((diffInMinutesA != 0) && (mm >= diffInMinutesA)) {
//
//                Log.i("Event loc", "check");
//
//                StringTokenizer z = new StringTokenizer(eventLocation, ",");
//                String address = z.nextToken();
//
//                StringTokenizer x = new StringTokenizer(locLocationA, ",");
//                String add_a = x.nextToken();
//
//
//
//
//                new AlertDialog.Builder(context)
//                        .setTitle("Event Location Conflict")
//                        .setIcon(R.drawable.location)
//                        .setMessage("The Distance between " + add_a + " to " + address + " is " +
//                                distancea + ". It takes " + durationa + " to get to event " + name + " from event " + locNameA + " but you only have " + time + " travel time. \n\nDo you want to add it anyway?")
//                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setNegativeButton("Add Anyway", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//
//                                if (c != null && c.getCount() > 0) {
//
//                                    timeconflict(c, unixtime, name, eventDescription, eventLocation, startDate,
//                                            startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//
//                                } else {
//                                    saveEventFunction(unixtime, name, eventDescription, eventLocation,
//                                            startDate, startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//                                }
//                            }
//                        })
//                        .setIcon(R.drawable.location)
//                        .show();
//
//
//            }
//            else if ((diffInMinutesB != 0) && (mmb >= diffInMinutesB)) {
//
//                Log.i("Event loooc", "check");
//
//                StringTokenizer z = new StringTokenizer(eventLocation, ",");
//                String address = z.nextToken();
//
//
//                StringTokenizer y  = new StringTokenizer(locLocationB, ",");
//                String add_b = y.nextToken();
//
//
//                new AlertDialog.Builder(context)
//                        .setTitle("Event Location Conflict")
//                        .setIcon(R.drawable.location)
//                        .setMessage("The Distance between " + add_b + " to " + address + " is " +
//                                distanceb + ". It takes " + durationb + " to get to event " + name + " from event " + locNameB + " but you only have " + timeb + " travel time. \n\n Do you want to add it anyway?")
//                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setNegativeButton("Add Anyway", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//
//                                if (c != null && c.getCount() > 0) {
//
//                                    timeconflict(c, unixtime, name, eventDescription, eventLocation, startDate,
//                                            startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//
//                                } else {
//                                    saveEventFunction(unixtime, name, eventDescription, eventLocation,
//                                            startDate, startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//                                }
//                            }
//                        })
//                        .setIcon(R.drawable.location)
//                        .show();
//
//
//            }else if (c != null && c.getCount() > 0) {
//
//                timeconflict(c, unixtime, name, eventDescription, eventLocation, startDate, startTime,
//                        endDate, endTime, notify, invitedContacts, lat, lng);
//
//            }
//            else {
//                saveEventFunction(unixtime, name, eventDescription, eventLocation, startDate,
//                        startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//            }
//        }
//        else if(!ra.isEmpty()){
//            StringTokenizer s =new StringTokenizer(ra,"^");
//            String distance=s.nextToken();
//            String durationa=s.nextToken();
//            String durationinmina=s.nextToken();
//            Log.i("locationCon duration: ",durationa);
//
//            int mm = Integer.parseInt(durationinmina)/60;
//
//            long timeInHours = diffInMinutesA/60;
//            long timeInMinutes = diffInMinutesA%60;
//            String time;
//            if(timeInHours==0&&timeInMinutes!=0){
//                time=timeInMinutes+" minutes";
//            }else if(timeInHours!=0&&timeInMinutes==0){
//                time=timeInHours+" hour(s)";
//            }else if(timeInHours!=0&&timeInMinutes!=0){
//                time=timeInHours+" hour(s) and "+timeInMinutes+" minutes";
//            }else{
//                time=timeInMinutes+" minutes";
//            }
//            if ((diffInMinutesA != 0) && (mm >= diffInMinutesA)) {
//
//                Log.i("Event loc", "check");
//
//                StringTokenizer z = new StringTokenizer(eventLocation, ",");
//                String address = z.nextToken();
//
//                StringTokenizer x = new StringTokenizer(locLocationA, ",");
//                String add_a = x.nextToken();
//
//                new AlertDialog.Builder(context)
//                        .setTitle("Event Location Conflict")
//                        .setIcon(R.drawable.location)
//                        .setMessage("The Distance between " + add_a + " to " + address + " is " + distance
//                                + ". It takes " + durationa + " to get to event " + name + " from event " + locNameA + " but you only have " + time + " travel time. \n\n Do you want to add it anyway?")
//                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setNegativeButton("Add Anyway", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//
//                                if (c != null && c.getCount() > 0) {
//
//                                    timeconflict(c, unixtime, name, eventDescription, eventLocation, startDate,
//                                            startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//
//                                } else {
//                                    saveEventFunction(unixtime, name, eventDescription, eventLocation,
//                                            startDate, startTime, endDate, endTime, notify, invitedContacts,lat, lng);
//                                }
//                            }
//                        })
//                        .setIcon(R.drawable.location)
//                        .show();
//
//
//            }else if (c != null && c.getCount() > 0) {
//
//                timeconflict(c, unixtime, name, eventDescription, eventLocation, startDate, startTime,
//                        endDate, endTime, notify, invitedContacts, lat, lng);
//
//            }else {
//                saveEventFunction(unixtime, name, eventDescription, eventLocation, startDate,
//                        startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//            }
//
//        }
//        else if(!rb.isEmpty()){
//            StringTokenizer s =new StringTokenizer(rb,"^");
//            String distanceb=s.nextToken();
//            String durationb=s.nextToken();
//            String durationinminb=s.nextToken();
//            Log.i("locationCon duration: ",durationb);
//            StringTokenizer st =new StringTokenizer(durationb," ");
//            int mm = Integer.parseInt(durationinminb)/60;
//            long timeInHours = diffInMinutesB/60;
//            long timeInMinutes = diffInMinutesB%60;
//            String time;
//            if(timeInHours==0&&timeInMinutes!=0){
//                time=timeInMinutes+" minutes";
//            }else if(timeInHours!=0&&timeInMinutes==0){
//                time=timeInHours+" hours";
//            }else if(timeInHours!=0&&timeInMinutes!=0){
//                time=timeInHours+" hours and "+timeInMinutes+" minutes";
//            }else{
//                time=timeInMinutes+" minutes";
//            }
//            if ((diffInMinutesB != 0) && (mm >= diffInMinutesB)) {
//
//                Log.i("Event loc", "check");
//
//                StringTokenizer z = new StringTokenizer(eventLocation, ",");
//                String address = z.nextToken();
//
//                StringTokenizer x = new StringTokenizer(locLocationB, ",");
//                String add_b = x.nextToken();
//
//                new AlertDialog.Builder(context)
//                        .setTitle("Event Location Conflict")
//                        .setIcon(R.drawable.location)
//                        .setMessage("The Distance between " + add_b + " to " + address + " is " +
//                                distanceb + ". It takes " + durationb + " to get to event " + name + " from event " + locNameA + " but you only have " + time + " travel time. \n\n Do you want to add it anyway?")
//                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setNegativeButton("Add Anyway", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//
//                                if (c != null && c.getCount() > 0) {
//
//                                    timeconflict(c, unixtime, name, eventDescription, eventLocation, startDate,
//                                            startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//
//                                } else {
//                                    saveEventFunction(unixtime, name, eventDescription, eventLocation,
//                                            startDate, startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//                                }
//                            }
//                        })
//                        .setIcon(R.drawable.location)
//                        .show();
//
//
//            }else if (c != null && c.getCount() > 0) {
//
//                timeconflict(c, unixtime, name, eventDescription, eventLocation, startDate, startTime,
//                        endDate, endTime, notify, invitedContacts, lat, lng);
//
//            }else {
//                saveEventFunction(unixtime, name, eventDescription, eventLocation, startDate,
//                        startTime, endDate, endTime, notify, invitedContacts, lat, lng);
//            }
//        }
//    }

}

