package com.thesis.velma;//package velmalatest.garciano.com.velmalatest;

//}
//
//
//import android.app.Activity;
//import android.app.DatePickerDialog;
//import android.app.TimePickerDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.TimePicker;
//import android.widget.Toast;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GoogleApiAvailability;
//import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
//import com.google.android.gms.common.GooglePlayServicesRepairableException;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.Places;
//import com.google.android.gms.location.places.ui.PlacePicker;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.LatLngBounds;
//import com.google.api.client.extensions.android.http.AndroidHttp;
//import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.util.DateTime;
//import com.google.api.client.util.ExponentialBackOff;
//import com.google.api.services.calendar.CalendarScopes;
//import com.google.api.services.calendar.model.Event;
//import com.google.api.services.calendar.model.EventAttendee;
//import com.google.api.services.calendar.model.EventDateTime;
//import com.google.api.services.calendar.model.EventReminder;
//import com.google.api.services.calendar.model.Events;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.List;
//
//import okhttp3.FormBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import pub.devrel.easypermissions.AfterPermissionGranted;
//import pub.devrel.easypermissions.EasyPermissions;
//
///**
// * Created by admin on 8/1/2016.
// */
//public class AddEventActivity extends AppCompatActivity implements View.OnClickListener,  EasyPermissions.PermissionCallbacks{
//    private EditText title;
//    private EditText dateStart;
//    private EditText dateEnd;
//    private EditText timeStart;
//    private EditText timeEnd;
////    private AutoCompleteTextView eventLocation;
//    private EditText eventLocation;
//    int PLACE_PICKER_REQUEST = 1;
//    private static final int SIGN_IN_CODE = 0;
//    private int sYear, sMonth, sDay, sHour, sMinute;
//    private int eYear, eMonth, eDay, eHour, eMinute;
////    private PlacesAutoCompleteAdapter mPlacesAdapter;
//    private static final int PLACE_PICKER_FLAG = 1;
//    private PlacePicker.IntentBuilder builder;
//    private EditText alarming;
//    private EditText contacts;
//    private AlertDialog alert;
//    private EditText people;
//    private Button save;
//    private Button friendsTrial;
//
//    private String titleString;
//    private String sdateString;
//    private String edateString;
//    private String tstartString;
//    private String estartString;
//    private String locationString;
//    private String invitesString;
//
//
//    protected GoogleApiClient google_api_client;
//    GoogleApiAvailability google_api_availability;
//    private int request_code;
//    private ConnectionResult connection_result;
//    private boolean is_intent_inprogress;
//    private boolean is_signInBtn_clicked;
//
//    //Add event to google calendar initializations
//    GoogleAccountCredential mCredential;
//    static final int REQUEST_ACCOUNT_PICKER = 1000;
//    static final int REQUEST_AUTHORIZATION = 1001;
//    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
//    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
//
//    private static final String BUTTON_TEXT = "Call Google Calendar API";
//    private static final String PREF_ACCOUNT_NAME = "accountName";
//    private static final String[] SCOPES = { CalendarScopes.CALENDAR };
//
//
//
//    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
//            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
//    protected GoogleApiClient mGoogleApiClient;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////        buidNewGoogleApiClient();
//
////        google_api_client =  new GoogleApiClient.Builder(this)
////                .addConnectionCallbacks(this)
////                .addOnConnectionFailedListener(this)
////                .addApi(Plus.API, Plus.PlusOptions.builder().build())
////                .addScope(Plus.SCOPE_PLUS_LOGIN)
////                .addScope(Plus.SCOPE_PLUS_PROFILE)
////                .build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .build();
//
//        setContentView(R.layout.activity_event_form);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setElevation(0);
//
//        builder = new PlacePicker.IntentBuilder();
//
//        title = (EditText)findViewById(R.id.eventname);
//        title.setHintTextColor(getResources().getColor(R.color.white));
//        dateStart = (EditText)findViewById(R.id.startdate);
//        dateStart.setHintTextColor(getResources().getColor(R.color.colorPrimary));
////        dateStart.setInputType(InputType.TYPE_NULL);
//        dateEnd = (EditText)findViewById(R.id.enddate);
//        dateEnd.setHintTextColor(getResources().getColor(R.color.colorPrimary));
//        timeStart = (EditText)findViewById(R.id.starttime);
//        timeStart.setHintTextColor(getResources().getColor(R.color.colorPrimary));
//        timeEnd = (EditText)findViewById(R.id.endtime);
//        timeEnd.setHintTextColor(getResources().getColor(R.color.colorPrimary));
////        eventLocation = (AutoCompleteTextView) findViewById(R.id.location);
//        eventLocation = (EditText)findViewById(R.id.location);
//        eventLocation.setHintTextColor(getResources().getColor(R.color.colorPrimary));
////        mPlacesAdapter = new PlacesAutoCompleteAdapter(this, android.R.layout.simple_list_item_1,
////                mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);
////        eventLocation.setOnItemClickListener(mAutocompleteClickListener);
////        eventLocation.setAdapter(mPlacesAdapter);
////        alarming = (EditText) findViewById(R.id.alarm);
////        alarming.setHintTextColor(getResources().getColor(R.color.colorPrimary));
//        people = (EditText)findViewById(R.id.invitepeople);
//        people.setHintTextColor(getResources().getColor(R.color.colorPrimary));
//        save = (Button)findViewById(R.id.sumbitevent);
//        friendsTrial=(Button)findViewById(R.id.frnd_button);
//        dateStart.setOnClickListener(this);
//        dateEnd.setOnClickListener(this);
//        timeStart.setOnClickListener(this);
//        timeEnd.setOnClickListener(this);
////        alarming.setOnClickListener(this);
//        people.setOnClickListener(this);
//        save.setOnClickListener(this);
////        friendsTrial.setOnClickListener(this);
//        eventLocation.setOnClickListener(this);
//
//        mCredential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
//    }
//
////    private void buidNewGoogleApiClient(){
////
////        google_api_client =  new GoogleApiClient.Builder(this)
////                .addConnectionCallbacks(this)
////                .addOnConnectionFailedListener(this)
////                .addApi(Plus.API, Plus.PlusOptions.builder().build())
////                .addScope(Plus.SCOPE_PLUS_LOGIN)
////                .addScope(Plus.SCOPE_PLUS_PROFILE)
////                .build();
////    }
//
//
//    @Override
//    public void onClick(View view) {
//        if(view == dateStart) {
//            final Calendar c = Calendar.getInstance();
//            sYear = c.get(Calendar.YEAR);
//            sMonth = c.get(Calendar.MONTH);
//            sDay = c.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
//                    new DatePickerDialog.OnDateSetListener() {
//
//                        @Override
//                        public void onDateSet(DatePicker view, int year,
//                                              int monthOfYear, int dayOfMonth) {
//
//                            dateStart.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//
//                        }
//                    },sYear, sMonth, sDay);
//            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 10000);
//            datePickerDialog.show();
//        } else if(view == dateEnd) {
//            final Calendar c = Calendar.getInstance();
//            eYear = c.get(Calendar.YEAR);
//            eMonth = c.get(Calendar.MONTH);
//            eDay = c.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
//                    new DatePickerDialog.OnDateSetListener() {
//
//                        @Override
//                        public void onDateSet(DatePicker view, int year,
//                                              int monthOfYear, int dayOfMonth) {
//
//                            dateEnd.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//
//                        }
//                    }, eYear, eMonth, eDay);
//            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 10000);
//            datePickerDialog.show();
//        }
//        else if (view == timeStart) {
//
//            final Calendar c = Calendar.getInstance();
//            sHour = c.get(Calendar.HOUR_OF_DAY);
//            sMinute = c.get(Calendar.MINUTE);
//
//
//                // Launch Time Picker Dialog
//                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
//                        new TimePickerDialog.OnTimeSetListener() {
//
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay,
//                                                  int minute) {
//
//                                timeStart.setText(hourOfDay + ":" + minute);
//                            }
//                        }, sHour, sMinute, false);
//
//            timePickerDialog.show();
//
//        }
//
//    else if (view == timeEnd) {
//
//        // Get Current Time
//        final Calendar c = Calendar.getInstance();
//        eHour = c.get(Calendar.HOUR_OF_DAY);
//        eMinute = c.get(Calendar.MINUTE);
//
//        // Launch Time Picker Dialog
//        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
//                new TimePickerDialog.OnTimeSetListener() {
//
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay,
//                                          int minute) {
//
//                        timeEnd.setText(hourOfDay + ":" + minute);
//                    }
//                }, eHour, eMinute, false);
//        timePickerDialog.show();
//    }
//        else if(view == eventLocation){
//
//            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//            Intent intent;
//
//            try {
//                intent = builder.build(this);
//                startActivityForResult(intent, PLACE_PICKER_REQUEST);
//            } catch (GooglePlayServicesRepairableException e) {
//                e.printStackTrace();
//            } catch (GooglePlayServicesNotAvailableException e) {
//                e.printStackTrace();
//            }
//        }
//
////        else if (view == alarming) {
////
////            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
////                    AddEventActivity.this);
////            alertBuilder.setIcon(R.drawable.alarm);
////            alertBuilder.setTitle("Alarm every: ");
////            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
////                    AddEventActivity.this,
////                    android.R.layout.select_dialog_item);
////            arrayAdapter.add("At time of event");
////            arrayAdapter.add("10 minutes before the event");
////            arrayAdapter.add("20 minutes before the event");
////            arrayAdapter.add("30 minutes before the event");
////            arrayAdapter.add("40 minutes before the event");
////            arrayAdapter.add("50 minutes before the event");
////            arrayAdapter.add("1 hour before the event");
////
////            alertBuilder.setNegativeButton("Cancel",
////                    new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog,
////                                            int which) {
////                            dialog.dismiss();
////                        }
////                    });
////
////            alertBuilder.setAdapter(arrayAdapter,
////                    new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog,
////                                            int which) {
////                            String strOS = arrayAdapter.getItem(which);
////                            alarming.setText(strOS);
////                            dialog.dismiss();
////                        }
////                    });
////
////            final AlertDialog alertDialog = alertBuilder.create();
////            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
////
////                @Override
////                public void onShow(DialogInterface dialog) {
////                    // TODO Auto-generated method stub
////                    ListView listView = alertDialog.getListView();
////                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
////
////                        @Override
////                        public boolean onItemLongClick(
////                                AdapterView<?> parent, View view,
////                                int position, long id) {
////                            // TODO Auto-generated method stub
////                            String strOS = arrayAdapter.getItem(position);
////                            Toast.makeText(getApplicationContext(),
////                                    "Long Press - Deleted Entry " + strOS,
////                                    Toast.LENGTH_SHORT).show();
////                            alertDialog.dismiss();
////                            return true;
////                        }
////                    });
////                }
////            });
////
////            alertDialog.show();
////        }
//
////        else if(view == people)
////        {
////                Toast.makeText(this, "G+ Friend List", Toast.LENGTH_LONG).show();
////                Plus.PeopleApi.loadVisible(google_api_client, null)
////                        .setResultCallback(this);
////
////        }
//
////        else if(view == friendsTrial)
////        {
////
////            Toast.makeText(this, "G+ Friend List", Toast.LENGTH_LONG).show();
////            Plus.PeopleApi.loadVisible(google_api_client, null)
////                    .setResultCallback(this);
////        }
//
//        else if(view == save)
//        {
//               if(TextUtils.isEmpty(title.getText().toString().trim()))
//               {
//                   title.setError("Input event title");
//               }
//
//               else if(TextUtils.isEmpty(dateStart.getText().toString().trim()))
//               {
//                   dateStart.setError("Input start date");
//               }
//
//               else if(TextUtils.isEmpty(dateEnd.getText().toString().trim()))
//               {
//                   dateEnd.setError("Input end date");
//               }
//
//               else if(TextUtils.isEmpty(timeStart.getText().toString().trim()))
//               {
//                   timeStart.setError("Input start time");
//               }
//
//               else if(TextUtils.isEmpty(timeEnd.getText().toString().trim()))
//               {
//                   timeEnd.setError("Input end time");
//               }
//
//               else if(TextUtils.isEmpty(eventLocation.getText().toString().trim()))
//               {
//                   eventLocation.setError("Input event location");
//               }
//
//         else {
//
////                   intentToReview.putExtra("title_summary", title.getText().toString());
////                   intentToReview.putExtra("start_date", dateStart.getText().toString());
////                   intentToReview.putExtra("end_date", dateEnd.getText().toString());
////                   intentToReview.putExtra("start_time", timeStart.getText().toString());
////                   intentToReview.putExtra("end_time", timeEnd.getText().toString());
////                   intentToReview.putExtra("location_summary", eventLocation.getText().toString());
////                   intentToReview.putExtra("invite_summary", people.getText().toString());
//
//
//                   //this code will transfer all inputted data to SummaryActivity
////                   startActivity(intentToReview);
//
//                   //getResultsFromApi() will let you choose a google plus account
////            getResultsFromApi();
//
//                   OkHttpClient client = new OkHttpClient();
//                   RequestBody body = new FormBody.Builder()
//                           .add("topic","gjeannevie@gmail.com")
//                           .add("message","Invitation")
//                           .add("title","You are invited")
////                .add("from","Jeanne")
//                           .build();
//
//                   Request request = new Request.Builder()
//                           //  .addHeader("Accept", "application/json")// .addHeader("Content-Type", "text/html")
//                           // .url("http://192.168.197.1/fcmphp/register.php?")
//                           .url("http://dev2-commit.mybudgetload.com:8282/mpa_api/send_notification.asp")
//                           .post(body)
//                           .build();
//                   Log.d("sd", "sd" + request);
//                   try {
//                       client.newCall(request).execute();
//                   } catch (IOException e) {
//                       e.printStackTrace();
//                   }
//               }
//        }
//}
//    private void getResultsFromApi() {
//
//        if(mCredential != null) {
////            if (!isGooglePlayServicesAvailable()) {
////                acquireGooglePlayServices();
////                Log.d("here", "here");
////            } else
//            if (mCredential.getSelectedAccountName() == null) {
//
//                chooseAccount();
//                Log.d("here", "two");
//            }
//// else if (!isDeviceOnline()) {
////                mOutputText.setText("No network connection available.");
////                Log.d("here", "three");
////            }
//              else {
//                new MakeRequestTask(mCredential).execute();
//                Log.d("here", "four");
//            }
//        }
//    }
//
//    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
//    private void chooseAccount() {
//        if (EasyPermissions.hasPermissions(
//                this, android.Manifest.permission.GET_ACCOUNTS)) {
//            String accountName = getPreferences(Context.MODE_PRIVATE)
//                    .getString(PREF_ACCOUNT_NAME, null);
//            if (accountName != null) {
//                mCredential.setSelectedAccountName(accountName);
//                getResultsFromApi();
//            } else {
//                // Start a dialog from which the user can choose an account
//                startActivityForResult(
//                        mCredential.newChooseAccountIntent(),
//                        REQUEST_ACCOUNT_PICKER);
//            }
//        } else {
//            // Request the GET_ACCOUNTS permission via a user dialog
//            EasyPermissions.requestPermissions(
//                    this,
//                    "This app needs to access your Google account (via Contacts).",
//                    REQUEST_PERMISSION_GET_ACCOUNTS,
//                    android.Manifest.permission.GET_ACCOUNTS);
//        }
//    }
//
////    @Override
////    protected void onActivityResult(
////            int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////        switch(requestCode) {
////            case REQUEST_GOOGLE_PLAY_SERVICES:
////                if (resultCode == RESULT_OK) {
////                    getResultsFromApi();
////                }
////                break;
////            case REQUEST_ACCOUNT_PICKER:
////                if (resultCode == RESULT_OK && data != null &&
////                        data.getExtras() != null) {
////                    String accountName =
////                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
////                    if (accountName != null) {
////                        SharedPreferences settings =
////                                getPreferences(Context.MODE_PRIVATE);
////                        SharedPreferences.Editor editor = settings.edit();
////                        editor.putString(PREF_ACCOUNT_NAME, accountName);
////                        editor.apply();
////                        mCredential.setSelectedAccountName(accountName);
////                        getResultsFromApi();
////                    }
////                }
////                break;
////            case REQUEST_AUTHORIZATION:
////                if (resultCode == RESULT_OK) {
////                    getResultsFromApi();
////                }
////                break;
////        }
////    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if(requestCode == PLACE_PICKER_REQUEST)
//        {
//            if(resultCode == Activity.RESULT_OK)
//            {
//                Place place = PlacePicker.getPlace(data,this);
//                String address = String.format("%s", place.getAddress());
//                eventLocation.setText(address);
//            }
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        EasyPermissions.onRequestPermissionsResult(
//                requestCode, permissions, grantResults, this);
//    }
//
//    @Override
//    public void onPermissionsGranted(int requestCode, List<String> perms) {
//        //Do nothing
//    }
//
//    @Override
//    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        //Do nothing
//    }
//
//    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
//        private com.google.api.services.calendar.Calendar mService = null;
//        private Exception mLastError = null;
//
//        public MakeRequestTask(GoogleAccountCredential credential) {
//            HttpTransport transport = AndroidHttp.newCompatibleTransport();
//            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//            mService = new com.google.api.services.calendar.Calendar.Builder(
//                    transport, jsonFactory, credential)
//                    .setApplicationName("Google Calendar API Android Quickstart")
//                    .build();
//        }
//
//        protected List<String> doInBackground(Void... params) {
//            try {
//                return getDataFromApi();
//            } catch (Exception e) {
//                mLastError = e;
//                cancel(true);
//                return null;
//            }
//        }
//        private List<String> getDataFromApi() throws IOException {
//
//            Toast.makeText(getApplicationContext(), "getDataFromApi", Toast.LENGTH_SHORT).show();
//            // List the next 10 events from the primary calendar.
//            DateTime now = new DateTime(System.currentTimeMillis());
//            List<String> eventStrings = new ArrayList<String>();
//            Events events = mService.events().list("primary")
//                    .setMaxResults(10)
//                    .setTimeMin(now)
//                    .setOrderBy("startTime")
//                    .setSingleEvents(true)
//                    .execute();
////            List<Event> items = events.getItems();
////
////            for (Event event : items) {
////                DateTime start = event.getStart().getDateTime();
////                if (start == null) {
////                    // All-day events don't have start times, so just use
////                    // the start date.
////                    start = event.getStart().getDate();
////                }
////                eventStrings.add(
////                        String.format("%s (%s)", event.getSummary(), start));
////            }
//            Event event = new Event()
//                    .setSummary("Add Event")
//                    .setLocation("Cebu City")
//                    .setDescription("FINALLLY");
//
//            DateTime startDateTime = new DateTime("2016-09-17T18:10:00+06:00");
//            EventDateTime start = new EventDateTime()
//                    .setDateTime(startDateTime)
//                    .setTimeZone("Asia/Dhaka");
//            event.setStart(start);
//
//            DateTime endDateTime = new DateTime("2016-09-19T18:40:00+06:00");
//            EventDateTime end = new EventDateTime()
//                    .setDateTime(endDateTime)
//                    .setTimeZone("Asia/Dhaka");
//            event.setEnd(end);
//
//            String[] recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=2"};
//            event.setRecurrence(Arrays.asList(recurrence));
//
//            EventAttendee[] attendees = new EventAttendee[]{
//                    new EventAttendee().setEmail("gjeannevie@gmail.com"),
//                    new EventAttendee().setEmail("reytabs1993@gmail.com"),
//            };
//            event.setAttendees(Arrays.asList(attendees));
//
//            EventReminder[] reminderOverrides = new EventReminder[]{
//                    new EventReminder().setMethod("email").setMinutes(24 * 60),
//                    new EventReminder().setMethod("popup").setMinutes(10),
//            };
//            Event.Reminders reminders = new Event.Reminders()
//                    .setUseDefault(false)
//                    .setOverrides(Arrays.asList(reminderOverrides));
//            event.setReminders(reminders);
//
//            String calendarId = "yuchristianne@gmail.com";
//            try {
//                event = mService.events().insert(calendarId, event).execute();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.printf("Event created: %s\n", event.getHtmlLink());
//
//            return eventStrings;
//        }
//
//
//
////    @Override
////    protected void onStart() {
////        super.onStart();
////        mGoogleApiClient.connect();
////        google_api_client.connect();
////    }
////
////    @Override
////    protected void onStop() {
////        mGoogleApiClient.disconnect();
////        google_api_client.disconnect();
////        super.onStop();
////    }
////
////    protected void onResume(){
////        super.onResume();
////        if (google_api_client.isConnected()) {
////            google_api_client.connect();
////        }
////    }
////
////    @Override
////    protected void onActivityResult(int requestCode, int responseCode,
////                                    Intent intent) {
////        // Check which request we're responding to
////        if (requestCode == SIGN_IN_CODE) {
////            request_code = requestCode;
////
////            if (!google_api_client.isConnecting()) {
////                google_api_client.connect();
////            }
////        }
////
////    }
//
//
////    private AdapterView.OnItemClickListener mAutocompleteClickListener
////            = new AdapterView.OnItemClickListener() {
////        @Override
////        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////            final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mPlacesAdapter.getItem(position);
////            final String placeId = String.valueOf(item.placeId);
////            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
////                    .getPlaceById(mGoogleApiClient, placeId);
////            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
////
////        }
////    };
////    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
////            = new ResultCallback<PlaceBuffer>() {
////        @Override
////        public void onResult(PlaceBuffer places) {
////            if (!places.getStatus().isSuccess()) {
////                Log.e("place", "Place query did not complete. Error: " +
////                        places.getStatus().toString());
////                return;
////            }
////            // Selecting the first object buffer.
////            final Place place = places.get(0);
////
////        }
////    };
//
//
////    @Override
////    public void onConnected(Bundle bundle) {
////
////    }
////
////    @Override
////    public void onConnectionSuspended(int i) {
////        google_api_client.connect();
////    }
////
////    @Override
////    public void onConnectionFailed(ConnectionResult connectionResult) {
////        if (!connectionResult.hasResolution()) {
////            google_api_availability.getErrorDialog(this, connectionResult.getErrorCode(),request_code).show();
////            return;
////        }
////
////        if (!is_intent_inprogress) {
////
////            connection_result = connectionResult;
////
////        }
////    }
////
////    @Override
////    public void onResult(People.LoadPeopleResult peopleData) {
////        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
////            PersonBuffer personBuffer = peopleData.getPersonBuffer();
////            ArrayList<String> list = new ArrayList<String>();
////            ArrayList<String> img_list= new ArrayList<String>();
////            try {
////                int count = personBuffer.getCount();
////
////                for (int i = 0; i < count; i++) {
////                    list.add(personBuffer.get(i).getDisplayName());
////                    img_list.add(personBuffer.get(i).getImage().getUrl());
////                }
////                Intent intent = new Intent(AddEventActivity.this,FriendActivity.class);
////                intent.putStringArrayListExtra("friendsName",list);
////                intent.putStringArrayListExtra("friendsPic",img_list);
////                startActivity(intent);
////            } finally {
////                personBuffer.release();
////            }
////        } else {
////            Log.e("circle error", "Error requesting visible circles: " + peopleData.getStatus());
////        }
//    }