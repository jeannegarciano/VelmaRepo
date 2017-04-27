package com.thesis.velma;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.thesis.velma.helper.OkHttp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import static android.content.ContentValues.TAG;
import static com.thesis.velma.LandingActivity.db;
import static com.thesis.velma.LandingActivity.useremail;

/**
 * Created by admin on 3/24/2017.
 */

public class ConflictActivity  extends AppCompatActivity implements View.OnClickListener{

    View rootView;
    EditText editEname, editDescription, editSdate, editEdate, editStime, editEtime, editFriends, editLoc;
    PlaceAutocompleteFragment searchLoc;
    Button buttonAddFriends;
    CheckBox allday;
    Context mcontext;

    public static int sYear, sMonth, sDay, sHour, sMinute;
    public static int eYear, eMonth, eDay, eHour, eMinute;
    DatePickerDialog datePickerDialog;
    long timeInMilliseconds;
    Dialog dialog;
    String modetravel = "driving";
    Double latitude, longtiude;
    public static String geolocation;

    int REQUEST_INVITE = 1;

    ArrayList<String> myContacts = new ArrayList<String>();
    public static ArrayList<String> invitedContacts = new ArrayList<String>();
    ArrayList<String> recipients = new ArrayList<>();

    String userID, allDay;
    FloatingActionButton save;
    String new_st, new_et;
    ArrayList<String> myConflictEvents = new ArrayList<String>();
    ArrayList<String> myCurrentEvent = new ArrayList<>();
    String eventID;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/avenir-next-regular.ttf");

        mcontext = this;
        editEname = (EditText) findViewById(R.id.eName);
        editEname.setTypeface(custom_font);
        editDescription = (EditText) findViewById(R.id.desc);
        editDescription.setTypeface(custom_font);
        editSdate = (EditText) findViewById(R.id.sDate);
        editSdate.setTypeface(custom_font);
        editSdate.setInputType(InputType.TYPE_NULL);
        editEdate = (EditText) findViewById(R.id.eDate);
        editEdate.setTypeface(custom_font);
        editEdate.setInputType(InputType.TYPE_NULL);
        editStime = (EditText) findViewById(R.id.sTime);
        editStime.setTypeface(custom_font);
        editStime.setInputType(InputType.TYPE_NULL);
        editEtime = (EditText) findViewById(R.id.eTime);
        editEtime.setTypeface(custom_font);
        editEtime.setInputType(InputType.TYPE_NULL);
        allday = (CheckBox) findViewById(R.id.checkAllDay);
        allday.setTypeface(custom_font);
        editLoc = (EditText) findViewById(R.id.loc);
        editLoc.setTypeface(custom_font);
        editFriends = (EditText) findViewById(R.id.friends);
        editFriends.setTypeface(custom_font);
        buttonAddFriends = (Button) findViewById(R.id.addFriends);
        buttonAddFriends.setTypeface(custom_font);
        save = (FloatingActionButton) findViewById(R.id.fabSave);

        searchLoc = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//        searchLoc.setBoundsBias(new LatLngBounds(
//                new LatLng(14.599512, 120.984222),
//                new LatLng(14.599512, 120.984222)));
        editSdate.setOnClickListener(this);
        editEdate.setOnClickListener(this);
        editStime.setOnClickListener(this);
        editEtime.setOnClickListener(this);
        buttonAddFriends.setOnClickListener(this);
        save.setOnClickListener(this);

        Intent i = getIntent();
        new_st= i.getStringExtra("new_st");
        new_et= i.getStringExtra("new_et");
        Log.i("Event new_st", new_st);
        Log.i("Event new_et", new_et);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mcontext);

        String name = preferences.getString("name", "value");
        String description = preferences.getString("desc", "value");
        String coordinates = preferences.getString("coordinates", "value");
        String location = preferences.getString("loc", "value");
        String sd = preferences.getString("startdate", "value");
        String ed = preferences.getString("enddate", "value");
        userID = preferences.getString("user_id", null);


        editEname.setText(name);
        editDescription.setText(description);
        editLoc.setText(location);
        editSdate.setText(sd);
        editEdate.setText(ed);
        editStime.setText(new_st);
        editEtime.setText(new_et);



        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE)
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                .build();
        searchLoc.setFilter(typeFilter);

        searchLoc.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getAddress());//get place details here

                editLoc.setText("" + place.getName());
                geolocation = place.getAddress().toString();

                String v;


                latitude = place.getLatLng().latitude;
                longtiude = place.getLatLng().longitude;

                String coordinates = latitude + "," + longtiude;
                Log.i("latlang", "" + latitude + ":" + longtiude);
                Log.i("event geo", geolocation);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences
                        (getApplicationContext()).edit();
                editor.putString("coordinates", coordinates);
                editor.putString("location", geolocation);

                Log.d("latlang", "" + latitude + ":" + longtiude);

                new getDetails().execute();

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });





        datePickerDialog = new DatePickerDialog(ConflictActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String sd = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                        SimpleDateFormat sdf;
                        try {

                            sdf = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

                            Date mDate = sdf.parse(sd);
                            String strdate = sdf.format(mDate);
                            editSdate.setText(strdate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, sYear, sMonth, sDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 10000);

        AlertDialog.Builder builder = new AlertDialog.Builder(ConflictActivity.this)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String list = "";
                        for (int i = 0; i < invitedContacts.size(); i++) {
                            list = list + invitedContacts.get(i) + ", \n";
                        }
                        editFriends.setText(list);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        invitedContacts.clear();
                        dialog.dismiss();
                        // Do stuff when user neglects.
                    }
                });
        builder.setTitle("Select From Contacts");

        ListView modeList = new ListView(ConflictActivity.this);
        //modeList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        //modeList.setItemsCanFocus(false);
        // String[] stringArray = new String[] { "Bright Mode", "Normal Mode" };


        //   Cursor c = LandingActivity.db.getContacts();
//
//        while (c.moveToNext()) {
//            myContacts.add(c.getString(c.getColumnIndex("EmailAdd"))); //this adds an element to the list.
//        }

//        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(createEvent.this, android.R.layout.simple_list_item_1, android.R.id.text1, myContacts);
//        modeList.setAdapter(modeAdapter);
//
//        builder.setView(modeList);

        dialog = builder.create();


        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                //Toast.makeText(getContext(), ((TextView) view).getText(),
                //        Toast.LENGTH_SHORT).show();

                int flag=0;
                for(int i=0 ; i<invitedContacts.size() ; i++){
                    if(invitedContacts.get(i).equals(((TextView) view).getText().toString()))
                        flag=1;
                }
                if(flag==0){
                    invitedContacts.add(((TextView) view).getText().toString());
                }
            }
        });


    }

    @Override
    public void onClick(View view) {


        if (view == editSdate) {
            final Calendar c = Calendar.getInstance();
            sYear = c.get(Calendar.YEAR);
            sMonth = c.get(Calendar.MONTH);
            sDay = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog.show();
        }  if (view == editEdate) {
            final Calendar c = Calendar.getInstance();
            eYear = c.get(Calendar.YEAR);
            eMonth = c.get(Calendar.MONTH);
            eDay = c.get(Calendar.DAY_OF_MONTH);
            String datetime;
            //i'll just put a static starttime kay para mmugana ang validation whether ni
            if(editSdate.getText() == null){
                editEdate.setClickable(false);
            }else {
                datetime = editSdate.getText().toString() + " 00:00";

                //    datetime = dateStart.getText().toString() + " " + timeStart.getText().toString();

                SimpleDateFormat sdf;
                try {

                    Log.i("Event dt", datetime);
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());

                    Date mDate = sdf.parse(datetime);
                    String strdate = sdf.format(mDate);
                    Log.i("Event String", strdate);
                    Log.i("Event mDate", "" + mDate);
                    timeInMilliseconds = mDate.getTime();
                    Log.i("Date in milli :: ", "" + timeInMilliseconds);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                DatePickerDialog datePickerDialog = new DatePickerDialog(ConflictActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String ed = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                                SimpleDateFormat sdf;
                                try {


                                    sdf = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

                                    Date mDate = sdf.parse(ed);
                                    String nddate = sdf.format(mDate);
                                    editEdate.setText(nddate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, eYear, eMonth, eDay);
                datePickerDialog.getDatePicker().setMinDate(timeInMilliseconds);
                Log.i("Event datepicker", String.valueOf(System.currentTimeMillis() - 10000));
                datePickerDialog.show();
            }
        }


        if(allday.isChecked()) {

            allDay = "All Day";
        }else{
            allDay = "Daily";
        }



        if (view == editStime) {

            final Calendar cal = Calendar.getInstance();
            sHour = cal.get(Calendar.HOUR_OF_DAY);
            sMinute = cal.get(Calendar.MINUTE);

            long start = cal.getTimeInMillis();
            Log.i("Event start", ""+start);
            if(editStime.getText()== null ){
                editEtime.setEnabled(false);
            }
            else {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(ConflictActivity.this,

                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {



                                String st = hourOfDay + ":" + minute;

                                SimpleDateFormat sdf;
                                try {


                                    sdf = new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());

                                    Date mDate = sdf.parse(st);
                                    String strtime = sdf.format(mDate);
                                    editStime.setText(strtime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, sHour, sMinute, false);


                timePickerDialog.show();
            }

        }  if (view == editEtime) {

            // Get Current Time

            final Calendar c = Calendar.getInstance();
            eHour = c.get(Calendar.HOUR_OF_DAY);
            eMinute = c.get(Calendar.MINUTE);


            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(ConflictActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

//
//                            if(cal.getTimeInMillis() > c.getTimeInMillis()) {
//
//                                Toast.makeText(getContext(), "Can't add time", Toast.LENGTH_SHORT).show();
//                            }else {
//                                timeEnd.setText(hourOfDay + ":" + minute);
//                            }

                            //  timeEnd.setText(hourOfDay + ":" + minute);

                            String st = hourOfDay + ":" + minute;

                            SimpleDateFormat sdf;
                            try {


                                sdf = new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());

                                Date mDate = sdf.parse(st);
                                String ndtime = sdf.format(mDate);
                                editEtime.setText(ndtime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, eHour, eMinute, false);


            timePickerDialog.show();
        }




        if(view == buttonAddFriends )
        {
            dialog.show();

        }

        if(view == save){


            String name = editEname.getText().toString();
            String eventDescription = editDescription.getText().toString();
            String eventLocation = editLoc.getText().toString();
            String lat = " "+latitude;
            String lng = " "+longtiude;
            String startDate = editSdate.getText().toString();
            String startTime = editStime.getText().toString();
            String endDate= editEdate.getText().toString();
            String endTime = editEtime.getText().toString();
            String eventAllDay = allDay;




            if(name.isEmpty() || eventLocation.isEmpty() || eventDescription.isEmpty() ||
                    startDate.isEmpty() || endDate.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {

                if (name.isEmpty()) {
                    editEname.setError("Enter event name");
                }
                if (eventDescription.isEmpty()) {
                    editDescription.setError("Enter description");
                }
                if (eventLocation.isEmpty()) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mcontext);
                    builder1.setMessage("Enter Location");
                    builder1.setCancelable(true);
                    builder1.setNeutralButton(
                            "Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });



                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                if (startDate.isEmpty()) {
                    editSdate.setError("Enter start date");
                }
                if (endDate.isEmpty()) {
                    editEdate.setError("Enter end date");
                }
                if (startTime.isEmpty()) {
                    editStime.setError("Enter start time");
                }
                if (endTime.isEmpty()) {
                    editEtime.setError("Enter end time");
                }
            }

            else if(!name.isEmpty() || !eventLocation.isEmpty() || !eventDescription.isEmpty() ||
                    !startDate.isEmpty() || !endDate.isEmpty() || !startTime.isEmpty() || !endTime.isEmpty()) {
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
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mcontext);
                    builder1.setMessage("Start date is greater than end date");
                    builder1.setCancelable(true);
                    builder1.setNeutralButton(
                            "Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });



                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else if (stt > ett) {
                    if (eventAllDay.equals("Daily")) {

                        allDay = "Reverse Daily";
                    }else{
                        allDay = "All Day";
                    }
                }


                eventAllDay=allDay;
                Log.i("Event allDay", eventAllDay);
                Log.i("Event name", name);
                Log.i("Event coord", lat + "," + lng);
                Log.i("Event loc: ", eventLocation);
                Log.i("Event SD", startDate);
                Log.i("Event ED: ", endDate);
                Log.i("Event ST: ", startTime);
                Log.i("Event ET: ", endTime);
                Log.i("Event email: ", useremail);
                Cursor con = db.conflictCheckerDaily(startDate, startTime, endDate, endTime, eventAllDay);

                Cursor all = LandingActivity. db.getids();
                Log.i("Event all count", "" + all.getCount());


                if (con != null && con.getCount() > 0) {

                    con.moveToFirst();
                    while (con.moveToNext()) {

                        Log.i("Event nameCON :", con.getString(con.getColumnIndex("event_name")));
                        Log.i("Event coordCON :" ,con.getString(con.getColumnIndex("latitude")) +","+ con.getString(con.getColumnIndex("longitude")));
                        Log.i("Event locCON : ", con.getString(con.getColumnIndex("event_location")));
                        Log.i("Event SDCON :", con.getString(con.getColumnIndex("start_date")));
                        Log.i("Event EDCON : ", con.getString(con.getColumnIndex("end_date")));
                        Log.i("Event STCON : ", con.getString(con.getColumnIndex("start_time")));
                        Log.i("Event ETCON : ", con.getString(con.getColumnIndex("end_time")));

                    }

                    timeconflict(con, name, eventDescription, eventLocation, startDate,
                            startTime, endDate, endTime, useremail, lat, lng);
                }

                else
                {


                    saveEventFunction(userID, name, eventDescription, eventLocation, startDate, startTime,
                            endDate,  endTime,  lat, lng, allDay, recipients);
                }
            }
        }
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
//
//                    distanceduration.setText("Distance : " + distance + ": Duration : " + duration);
//                    distanceduration.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void timeconflict(final Cursor con, final String name, final String
            eventDescription, final String eventLocation, final String startDate, final String startTime, final
                             String endDate, final String endTime, final String
                                     userEmail, final String lat, final String lng) {
        Log.i("Event con", "2");
        final String flag="original";

        myCurrentEvent.add(name);
        myCurrentEvent.add(eventDescription);
        myCurrentEvent.add(eventLocation);
        myCurrentEvent.add(startDate);
        myCurrentEvent.add(endDate);
        myCurrentEvent.add(startTime);
        myCurrentEvent.add(endTime);
//        myCurrentEvent.add(notify);
//        myCurrentEvent.add(invitedContacts);
        con.moveToFirst();
        while (!con.isAfterLast()) {
//
////            Toast.makeText(OnboardingActivity.this, "Conflict in: " + c.getString(1) + "   " +
//            c.getString(2) + "  " + c.getString(3) + "  " + c.getString(4), Toast.LENGTH_LONG).show();
////            Log.i("Event log", c.getString(0) + " >> " + c.getString(1) + " >> " + c.getString(2) + " >> " +
//            c.getString(3) + " >> " + c.getString(4) + " >> ");


            con.moveToNext();
        }


        final AlertDialog.Builder builder = new AlertDialog.Builder(mcontext)
                .setTitle("Event Time Conflict")
                .setIcon(R.drawable.time)
                .setMessage("The event you created ago has the following conflict(s).")
                .setPositiveButton("Add Anyway", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        saveEventFunction(unixtime, name, eventDescription, eventLocation, startDate,
//                                startTime, endDate, endTime, notify, invitedContacts, userEmail, lat, lng);

                        dialog.dismiss();


                    }


                }).setNeutralButton("Suggest",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor =
                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                                editor.putString("name", name);
                                editor.putString("desc", eventDescription);
                                editor.putString("loc", eventLocation);
                                editor.putString("startdate", startDate);
                                editor.putString("enddate", endDate);
//                                editor.putString("invitedfriends", invitedContacts);
//                                editor.putString("flag", flag);
                                editor.putLong("key",0);
                                editor.apply();
                                Intent in = new Intent(ConflictActivity.this, DateListView.class);
                                finish();
                                startActivity(in);

                            }
                        }

                );

        ListView modeList = new ListView(mcontext);
        myConflictEvents.clear();
        myConflictEvents.add(myCurrentEvent.get(0));
        con.moveToFirst();
        eventID= con.getString(0);
        Log.i("Event con", "5");

        while (!con.isAfterLast()) {

            StringTokenizer display1 = new StringTokenizer(con.getString(con.getColumnIndex("start_time")),
                    ":");
            String s_hour = display1.nextToken();
            String s_min = display1.nextToken();
            StringTokenizer display2 = new StringTokenizer(con.getString(con.getColumnIndex("end_time")),
                    ":");
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


            Log.i("Event conn", " " + con.getString(con.getColumnIndex("event_name")));
            Log.i("Event id", eventID);

            myConflictEvents.add(con.getString(con.getColumnIndex("event_name")) + "   " + sdisplay + " to " + edisplay); //this adds an element to the list.
            con.moveToNext();
        }

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(mcontext,
                android.R.layout.simple_list_item_1, android.R.id.text1, myConflictEvents);
        modeList.setAdapter(modeAdapter);

        builder.setView(modeList);

        builder.create();
        builder.show();
    }


    public void saveEventFunction(String user_id, String name, String eventDescription, String eventLocation, String startDate, String startTime,
                                  String endDate, String endTime, String lat, String lng, String eventAllDay, ArrayList<String> recipients) {

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

        AlarmManager alarmManager = (AlarmManager) mcontext.getSystemService
                (Context.ALARM_SERVICE);
        Intent myIntent = new Intent(mcontext, AlarmReceiver.class);
        myIntent.putExtra("userID", user_id);
        myIntent.putExtra("name", name);
        myIntent.putExtra("description", eventDescription);
        myIntent.putExtra("location", eventLocation);
        myIntent.putExtra("start", startTime);
        myIntent.putExtra("end", endTime);
        myIntent.putExtra("dateS", startDate);
        myIntent.putExtra("dateE", endDate);
        myIntent.putExtra("people", recipients);
        Log.d("MyData", name);
        pendingIntent = PendingIntent.getBroadcast(mcontext, count+1, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();
        calSet.setTimeInMillis(System.currentTimeMillis());

//
//        Log.d("Calendar.YEAR", "" + Integer.parseInt(mydates[2]));
//        Log.d("Calendar.MONTH", "" + Integer.parseInt(mydates[1]));
//        Log.d("Calendar.DATE", "" + Integer.parseInt(mydates[0]));
//        Log.d("Calendar.HOUR_OF_DAY", "" + Integer.parseInt(mytimes[0]));
//        Log.d("Calendar.MINUTE", "" + Integer.parseInt(mytimes[1]));

        String date = "" + mydates[0] + "-" + mydates[1] + "-" + mydates[2];
//        mytimes[0]= ""+(Integer.parseInt(mytimes[0]));
//        int AM_PM=0;
//        if (Integer.parseInt(mytimes[0]) == 12) {
//            AM_PM = 1;
//        } else if(Integer.parseInt(mytimes[0]) > 12) {
//            mytimes[0]= ""+(Integer.parseInt(mytimes[0])-12);
//            AM_PM = 1;
//        }

        calSet.set(Calendar.YEAR, Integer.parseInt(mydates[0]));
        calSet.set(Calendar.MONTH, Integer.parseInt(mydates[1])-1);
        calSet.set(Calendar.DATE, Integer.parseInt(mydates[2]));
        calSet.set(Calendar.HOUR_OF_DAY, Integer.parseInt(mytimes[0]));
        calSet.set(Calendar.MINUTE, Integer.parseInt(mytimes[1]));
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);
//        calSet.set(Calendar.AM_PM, AM_PM);

//        calSet.setTimeInMillis(System.currentTimeMillis());
//        calSet.clear();
//        calSet.set(Integer.parseInt(mydates[2]), Integer.parseInt(mydates[1]) - 1, Integer.parseInt
        //   (mydates[0]), Integer.parseInt(mytimes[0]), Integer.parseInt(mytimes[1]));

        Log.d("AlaramJeanne", "" + calSet.getTimeInMillis());
        Log.d("AlaramJeanne1", "" + System.currentTimeMillis());

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(),
                pendingIntent);

        //  ArrayList<String> recipients = new ArrayList<>();


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mcontext);
        String sharedPrefUserId = sharedPreferences.getString("user_id", null);

        OkHttp.getInstance(getBaseContext()).saveEvent(sharedPrefUserId, name, eventDescription, eventLocation, lng, lat, startDate, startTime, endDate, endTime, eventAllDay, recipients);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mcontext);
        eventID = prefs.getString("eventID", null);
        Log.i("Event ID: ", eventID);

//
        LandingActivity.db.saveEvent(Integer.valueOf(sharedPrefUserId), Integer.parseInt(eventID), name, eventDescription,
                eventLocation, lng, lat, startDate, startTime, endDate, endTime, null, "Creator", null);

        Intent i = new Intent(ConflictActivity.this, LandingActivity.class);
        finish();
        startActivity(i);

    }
}
