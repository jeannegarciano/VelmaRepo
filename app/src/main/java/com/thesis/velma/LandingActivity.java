package com.thesis.velma;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.thesis.velma.Entity.EventsEntity;
import com.thesis.velma.apiclient.ApiService;
import com.thesis.velma.helper.CheckInternet;
import com.thesis.velma.helper.DataBaseHandler;
import com.thesis.velma.helper.NetworkUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LandingActivity extends AppCompatActivity implements CalendarPickerController, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, ResultCallback<People.LoadPeopleResult> {

    //WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener

    GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;
    private static final int SIGN_IN_CODE = 0;
    private static final int PROFILE_PIC_SIZE = 120;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;


    private boolean is_signInBtn_clicked;
    private int request_code;
    private FloatingActionButton fabButton;

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private static final int CREATE_EVENT = 0;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    FloatingActionButton fab;
    AgendaCalendarView mAgendaCalendarView;

    Calendar startdate, enddate;
    Context mcontext;
    final int CALENDAR_PERMISSION = 42;
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    public static DataBaseHandler db;
    private static final String[] LOCATION_PERMS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };
    static double origlatitude = 0, origlongitude = 0;

    private static final int LOCATION_REQUEST = 3;
    LocationManager locationManager;

    MaterialDialog dialog;
    String eventID, id;


    public static String profilename, imei, useremail;
    String[] invitedFriends = null;


    CheckInternet connectCheck;
    Long unixtime = null;
    String name, eventDescription, eventLocation,
            startDate, startTime, endDate, endTime;

    public static final String ROOT_URL = "http://velma.000webhostapp.com";
    private List<EventsEntity> eventsEntityList;

    private boolean refreshOnClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //buildNewGoogleApiClient();
        setContentView(R.layout.activity_activity_landing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAgendaCalendarView = (AgendaCalendarView) findViewById(R.id.agenda_calendar_view);
        mcontext = this;

        db = new DataBaseHandler(mcontext);
        connectCheck = new CheckInternet(this);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mcontext);
        //then you use
        profilename = prefs.getString("FullName", null);
        imei = prefs.getString("imei", null);
        useremail = prefs.getString("Email", null);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Intent myIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);

        fab = (FloatingActionButton) findViewById(R.id.fabButton);


        fab.setOnClickListener(this);


        LoadEvents();



        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();


        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                    },
                    LOCATION_REQUEST);
        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle data = intent.getExtras();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case LOCATION_REQUEST:
                getCurrentLocation();
                break;
        }
    }

    public void LoadEvents() {
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        List<CalendarEvent> eventList =  getEventList();
        minDate.set(Calendar.DAY_OF_YEAR, 1);
        maxDate.add(Calendar.YEAR, 1);


        mAgendaCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), this);
    }

    public void getCurrentLocation() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

            // Acquire a reference to the system Location Manager

            origlatitude = 10.3157007;
            origlongitude = 123.88544300000001;


// Define a listener that responds to location updates
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    //makeUseOfNewLocation(location);
                    Log.d("Latlng", "" + location);
                    origlatitude = location.getLatitude();
                    origlongitude = location.getLongitude();

                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };

// Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


        } else {
     //       Toast.makeText(getBaseContext(), "Location Failed", Toast.LENGTH_SHORT).show();
        }
    }


    // region Interface - CalendarPickerController
    @Override
    public void onDaySelected(DayItem dayItem) {

    }


    @Override
    public void onEventSelected(final CalendarEvent event) {

        if (event.getId() != 0) {

            Cursor c = db.getEventDetails(event.getId());

            while (c.moveToNext()) {

                id = c.getString(c.getColumnIndex("event_id"));

                Intent intent = new Intent(getBaseContext(), ShowEventDetails.class);
                intent.putExtra("key", id);
                Log.d("cathlyn: ", id);
                startActivity(intent);


            }

        }
    }
    @Override
    public void onScrollToDate(Calendar calendar) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        }
    }

    // endregion


    private void buildNewGoogleApiClient() {

        google_api_client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addApi(AppInvite.API)
                .enableAutoManage(this, this)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
    }


    @Override
    public void onConnected(Bundle arg0) {
        is_signInBtn_clicked = false;

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        google_api_client.connect();

    }

    private void resolveSignInError() {
        if (connection_result.hasResolution()) {
            try {
                is_intent_inprogress = true;
                connection_result.startResolutionForResult(this, SIGN_IN_CODE);
                Log.d("resolve error", "sign in error resolved");
            } catch (IntentSender.SendIntentException e) {
                is_intent_inprogress = false;
                google_api_client.connect();

            }
        }
    }


    @Override
    public void onClick(View view)
    {
        if (view == fab) {

            int status = NetworkUtil.getConnectivityStatusString(mcontext);

            if (status == 0) {
                CheckInternet.showConnectionDialog(mcontext);
            } else {

                Intent intent = new Intent(LandingActivity.this, createEvent.class);
                startActivityForResult(intent, CREATE_EVENT);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mcontext);
                final String sharedPrefUserId = prefs.getString("user_id", null);


                int status = NetworkUtil.getConnectivityStatusString(mcontext);

                if (status == 0) {
                    CheckInternet.showConnectionDialog(mcontext);
                }else {

                    refreshOnClick = true;
                    if (refreshOnClick){
                        final ProgressDialog loading = android.app.ProgressDialog.show(this,"Fetching Events","Please wait...",false,false);

                        RestAdapter adapter = new RestAdapter.Builder()
                                .setEndpoint(ROOT_URL)
                                .build();

                        ApiService apiService = adapter.create(ApiService.class);
                        apiService.getMyJSON(new Callback<List<EventsEntity>>() {
                            @Override
                            public void success(List<EventsEntity> eventsEntities, Response response) {

                                loading.dismiss();

                                eventsEntityList = eventsEntities;

                                db.deleteTable();

                                for (int i = 0; i < eventsEntityList.size(); i++) {

                                    int j=0;

                                    final ArrayList<String> recipients = new ArrayList<>();
                                    recipients.clear();

                                    int user_id = eventsEntityList.get(i).getUser_id();
                                    int event_id = eventsEntityList.get(i).getEvent_id();
                                    String event_name = eventsEntityList.get(i).getEvent_name();
                                    String event_description = eventsEntityList.get(i).getEvent_description();
                                    String event_location = eventsEntityList.get(i).getEvent_location();
                                    String longitude = eventsEntityList.get(i).getLongitude();
                                    String latitude = eventsEntityList.get(i).getLatitude();
                                    String start_date = eventsEntityList.get(i).getStart_date();
                                    String start_time = eventsEntityList.get(i).getStart_time();
                                    String end_date = eventsEntityList.get(i).getEnd_date();
                                    String end_time = eventsEntityList.get(i).getEnd_time();
                                    String is_whole_day = eventsEntityList.get(i).getIs_whole_day();
                                    String role = eventsEntityList.get(i).getRole();
                                    String status = eventsEntityList.get(i).getStatus();
                                    String name = eventsEntityList.get(i).getName();
                                    String email = eventsEntityList.get(i).getEmail();

                                    for (j=0; j<eventsEntityList.size(); j++) {

                                        int userID = eventsEntityList.get(j).getUser_id();
                                        int eventID = eventsEntityList.get(j).getEvent_id();
                                        String rec_name = eventsEntityList.get(j).getName();
                                        String rec_status = eventsEntityList.get(j).getStatus();

                                        if (event_id == eventID && !sharedPrefUserId.equals(String.valueOf(userID))) {
                                            recipients.add(rec_name + " (" + rec_status + ")\n");
                                        }
                                    }

                                    if (sharedPrefUserId.equals(String.valueOf(user_id))) {

                                        String friends = String.valueOf(recipients)
                                                .replace(",", " ")
                                                .replace("[", " ")
                                                .replace("]", " ")
                                                .trim();

                                        db.saveEvent(user_id, event_id, event_name, event_description, event_location, longitude, latitude, start_date, start_time,
                                                end_date, end_time, is_whole_day, role, friends);

                                    }

//                                    onRestart();
                                    LoadEvents();
                                    refreshOnClick = false;

                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                    }

                    return true;


                }

                return true;

            case R.id.action_notification:

                Toast.makeText(getApplicationContext(), "You clicked the notification icon", Toast.LENGTH_LONG);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }



//
//    @Override
//    protected void onRestart() {
//
//        // TODO Auto-generated method stub
//        super.onRestart();
//        Intent i = new Intent(LandingActivity.this, LandingActivity.class);  //your class
//        startActivity(i);
//        finish();
//    }




    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if (!connectionResult.hasResolution()) {
            google_api_availability.getErrorDialog(this, connectionResult.getErrorCode(), request_code).show();
            return;
        }

        if (!is_intent_inprogress) {

        }

    }


    @Override
    public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {


        if (requestCode == CREATE_EVENT)
        {
//            MyEvent myevent = null;

            if (responseCode == RESULT_OK) {
                LoadEvents();
//                Calendar minDate = Calendar.getInstance();
//                Calendar maxDate = Calendar.getInstance();
//
//                List<CalendarEvent> eventList = getEventList();
//
//                minDate.set(Calendar.DAY_OF_YEAR, 1);
//                maxDate.add(Calendar.YEAR, 1);
//
//                mAgendaCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), this);
            }

        }

    }

    private List<CalendarEvent> getEventList() {
        List<CalendarEvent> eventList = new ArrayList<>();
        Cursor cursor = db.getEvents();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString1 = "", dateString2;
        Calendar sdate = null, edate = null;


        while (cursor.moveToNext()) {

            sdate = Calendar.getInstance();
            edate = Calendar.getInstance();

            dateString1 = cursor.getString(cursor.getColumnIndex("start_date"));
            dateString2 = cursor.getString(cursor.getColumnIndex("end_date"));


            try {
                sdate.setTime(formatter.parse(dateString1));
                edate.setTime(formatter.parse(dateString2));

            } catch (ParseException e) {
                e.printStackTrace();
                Log.d("MyDate", "Err" + e.getMessage());
            }

            Log.d("MyDate", "" + sdate.get(Calendar.DATE));

            int id = cursor.getInt(cursor.getColumnIndex("event_id"));
            String name = cursor.getString(cursor.getColumnIndex("event_name"));
            String description = cursor.getString(cursor.getColumnIndex("event_description"));
            String location = cursor.getString(cursor.getColumnIndex("event_location"));

            Log.d("Kevin:", id+ " "+name);
            if (!sdate.equals(edate)){
                edate.add(Calendar.DATE, 1);
            }

            Random rnd = new Random();

            int color = Color.argb(225, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            BaseCalendarEvent event1 = new BaseCalendarEvent(
                    id,
                    color,
                    name,
                    description,
                    location,
                    sdate.getTimeInMillis(), edate.getTimeInMillis(), 0, "No");
            eventList.add(event1);
        }

        return eventList;
    }
}