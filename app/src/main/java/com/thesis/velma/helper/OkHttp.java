package com.thesis.velma.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.thesis.velma.LandingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by admin on 25/01/2017.
 */
public class OkHttp {

    public static Context mcontext;
    private static OkHttp parser;
    OkHttpClient client;
    Handler mainHandler;
    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";

    MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public OkHttp(Context context) {
        this.mcontext = context;
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(new File(mcontext.getApplicationContext().getCacheDir(), "cacheFileName"), cacheSize);
        client = new OkHttpClient.Builder().cache(cache).build();
        mainHandler = new Handler(mcontext.getMainLooper());
    }

    public static synchronized OkHttp getInstance(Context c) {
        mcontext = c;
        if (parser == null) {
            parser = new OkHttp(c);
        }
        return parser;
    }

    public void saveProfile( String useremail, final String name) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://velma.000webhostapp.com/add_user.php").newBuilder();
        urlBuilder.addQueryParameter("email", useremail);
        urlBuilder.addQueryParameter("name", name);
        String Url = urlBuilder.build().toString();

        Log.d("URL", Url);

        Request request = new Request.Builder()
                .url(Url)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // ... check for failure using `isSuccessful` before proceeding
                // Read data on the worker thread
                final String responseData = response.body().string();
                Log.d("Data", responseData);
                if (response.code() == 200) {

                    // Run view-related code back on the main thread
                    if (responseData.equalsIgnoreCase("Records added successfully.")) {

                    }
                    if (responseData.equalsIgnoreCase("User Already in Exists")) {

                    }
                } else {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mcontext, "Failed to register", Toast.LENGTH_SHORT).show();
                        }
                    });


                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mcontext);
                    prefs.edit().putBoolean("isLoggedIn", false).commit();

                }
            }
        });
    }
//    public void fetchEvents(String userid, String useremail) {
//
//        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://velma.000webhostapp.com/fetch_velma_events.php").newBuilder();
//        urlBuilder.addQueryParameter("userid", userid);
//        urlBuilder.addQueryParameter("useremail", useremail);
//        String Url = urlBuilder.build().toString();
//
//        Log.d("URL", Url);
//
//        Request request = new Request.Builder()
//                .url(Url)
//                .build();
//
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                // ... check for failure using `isSuccessful` before proceeding
//                // Read data on the worker thread
//                final String responseData = response.body().string();
//                Log.d("DataReturn", responseData);
//                if (response.code() == 200) {
//
//
//                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyy");//yyyy-MM-dd HH:mm:ss
//                    String startdate[];
//                    String endate[];
//
//                    //7-2-20172
//
//                    try {
//                        JSONArray myarray = new JSONArray(responseData);
//
//
//                        for (int i = 0; i < myarray.length(); i++) {
//                            JSONObject myobject = myarray.getJSONObject(i);
//                            Log.d("myData", myobject.getString("EventID"));
//
//                            startdate = myobject.getString("StartDate").split("-");
//                            endate = myobject.getString("EndDate").split("-");
//
////                            LandingActivity.db.saveEvent(myobject.getString("UserID"), myobject.getLong("EventID"),
////                                    myobject.getString("EventName"), myobject.getString("EventDescription"),
////                                    myobject.getString("EventLocation"), startdate[2] + "-" + startdate[1] + "-" + startdate[0],
////                                    myobject.getString("StartTime"), endate[2] + "-" + endate[1] + "-" + endate[0],
////                                    myobject.getString("EndTime"), "", myobject.getString("InvitedFriends"));
//
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    // Run view-related code back on the main thread
//
//                } else {
//                    mainHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(mcontext, "Failed to register", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//
//                }
//            }
//        });
//
//    }

    public void saveEvent(String user_id, String event_name, String event_description, String event_location,
                          String longitude, String latitude, String start_date, String start_time,
                          String end_date, String end_time, String is_whole_day, ArrayList<String> recipients) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://velma.000webhostapp.com/add_event.php").newBuilder();
        urlBuilder.addQueryParameter("user_id", user_id);
        urlBuilder.addQueryParameter("event_name", event_name);
        urlBuilder.addQueryParameter("event_description", event_description);
        urlBuilder.addQueryParameter("event_location", event_location);
        urlBuilder.addQueryParameter("longitude", longitude);
        urlBuilder.addQueryParameter("latitude", latitude);
        urlBuilder.addQueryParameter("start_date", start_date);
        urlBuilder.addQueryParameter("start_time", start_time);
        urlBuilder.addQueryParameter("end_date", end_date);
        urlBuilder.addQueryParameter("end_time", end_time);
        urlBuilder.addQueryParameter("is_whole_day", is_whole_day);
        urlBuilder.addQueryParameter("recipients", String.valueOf(recipients));
        String Url = urlBuilder.build().toString();

        Log.d("URL", Url);

        Request request = new Request.Builder()
                .url(Url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // ... check for failure using `isSuccessful` before proceeding
                // Read data on the worker thread
                final String responseData = response.body().string();
                Log.d("Data", responseData);
                if (response.code() == 200) {

                    String readEventID = responseData.replaceAll("[^0-9]", "");
                    Log.d("Data 200: ", readEventID);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mcontext);
                    prefs.edit().putString("eventID", readEventID).commit();

                } else {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mcontext, "Failed to register", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    public void fetchEvents(String userid, String useremail) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://velma.000webhostapp.com/fetch_velma_events.php").newBuilder();
        urlBuilder.addQueryParameter("userid", userid);
        urlBuilder.addQueryParameter("useremail", useremail);
        String Url = urlBuilder.build().toString();

        Log.d("URL", Url);

        Request request = new Request.Builder()
                .url(Url)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // ... check for failure using `isSuccessful` before proceeding
                // Read data on the worker thread
                final String responseData = response.body().string();
                Log.d("DataReturn", responseData);
                if (response.code() == 200) {


                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyy");//yyyy-MM-dd HH:mm:ss
                    String startdate[];
                    String endate[];

                    //7-2-20172

                    try {
                        JSONArray myarray = new JSONArray(responseData);


                        for (int i = 0; i < myarray.length(); i++) {
                            JSONObject myobject = myarray.getJSONObject(i);
                            Log.d("myData", myobject.getString("EventID"));

                            startdate = myobject.getString("StartDate").split("-");
                            endate = myobject.getString("EndDate").split("-");

//                            LandingActivity.db.saveEvent(myobject.getString("UserID"), myobject.getLong("EventID"),
//                                    myobject.getString("EventName"), myobject.getString("EventDescription"),
//                                    myobject.getString("EventLocation"), startdate[2] + "-" + startdate[1] + "-" + startdate[0],
//                                    myobject.getString("StartTime"), endate[2] + "-" + endate[1] + "-" + endate[0],
//                                    myobject.getString("EndTime"), "", myobject.getString("InvitedFriends"),
//                                    myobject.getString("Extra2"), myobject.getString("Extra3"));


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Run view-related code back on the main thread

                } else {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mcontext, "Failed to register", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });

    }


//    public void saveEvent(Long eventid, String eventname, String eventDescription, String eventLocation,
//                          String eventStartDate, String eventStartTime, String eventEndDate,
//                          String eventEndTime, String notify, String invitedfirends) {
//
//        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://velma.000webhostapp.com/add_velma_events.php").newBuilder();
//        urlBuilder.addQueryParameter("userid", "" + LandingActivity.imei);
//        urlBuilder.addQueryParameter("eventid", "" + eventid);
//        urlBuilder.addQueryParameter("eventname", eventname);
//        urlBuilder.addQueryParameter("eventDescription", eventDescription);
//        urlBuilder.addQueryParameter("eventLocation", eventLocation);
//        urlBuilder.addQueryParameter("eventStartDate", eventStartDate);
//        urlBuilder.addQueryParameter("eventStartTime", eventStartTime);
//        urlBuilder.addQueryParameter("eventEndDate", eventEndDate);
//        urlBuilder.addQueryParameter("eventEndTime", eventEndTime);
//        urlBuilder.addQueryParameter("notify", notify);
//        urlBuilder.addQueryParameter("invitedfirends", invitedfirends);
//
//        String Url = urlBuilder.build().toString();
//
//        Log.d("URL", Url);
//
//        Request request = new Request.Builder()
//                .url(Url)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                // ... check for failure using `isSuccessful` before proceeding
//                // Read data on the worker thread
//                final String responseData = response.body().string();
//                Log.d("Data", responseData);
//                if (response.code() == 200) {
//                    // Run view-related code back on the main thread
//
//
//                    if (responseData.equalsIgnoreCase("Records added successfully.")){
//
//                        mainHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(mcontext, "event successfully added.", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//
////                        for (int i = 0; i <= OnboardingFragment3.invitedContacts.size() - 1; i++) {
////                            String[] target = OnboardingFragment3.invitedContacts.get(i).split("@");
////                            OkHttp.getInstance(context).sendNotification("Invitation", unixtime, name, eventDescription, eventLocation,
////                                    startDate, startTime, endDate, endTime, notify, invitedContacts, target[0] + "Velma");//target[0]
////                        }
//
//                    }
//                    else{
//
//
//                        mainHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
////                                Toast.makeText(mcontext, "Unable to add event. Conflict Time", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                    }
//
//
//
//
//
//
//                } else {
//                    mainHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(mcontext, "Faile to save event", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        });
//
//
//    }

    public void sendNotification(String invitationTitle, String userid, String eventid, String eventname, String eventDescription, String eventLocation,
                                 String eventStartDate, String eventStartTime, String eventEndDate,
                                 String eventEndTime, String target,String lat, String lng, String creatorEmail) {


        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://velma.000webhostapp.com/sendNotification.php").newBuilder();
        urlBuilder.addQueryParameter("invitationTitle", invitationTitle);
        urlBuilder.addQueryParameter("userid", "" + userid);
        urlBuilder.addQueryParameter("eventid", String.valueOf(eventid));
        urlBuilder.addQueryParameter("eventname", eventname);
        urlBuilder.addQueryParameter("eventDescription", eventDescription);
        urlBuilder.addQueryParameter("eventLocation", eventLocation);
        urlBuilder.addQueryParameter("eventStartDate", eventStartDate);
        urlBuilder.addQueryParameter("eventStartTime", eventStartTime);
        urlBuilder.addQueryParameter("eventEndDate", eventEndDate);
        urlBuilder.addQueryParameter("eventEndTime", eventEndTime);
//        urlBuilder.addQueryParameter("notify", notify);
//        urlBuilder.addQueryParameter("invitedfirends", invitedfirends);
        urlBuilder.addQueryParameter("target", target);
        urlBuilder.addQueryParameter("name", LandingActivity.profilename);
        urlBuilder.addQueryParameter("lat", lat);
        urlBuilder.addQueryParameter("lng", lng);
        urlBuilder.addQueryParameter("creatorEmail", creatorEmail);

        String Url = urlBuilder.build().toString();

        Log.d("URL", Url);

        Request request = new Request.Builder()
                .url(Url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // ... check for failure using `isSuccessful` before proceeding
                // Read data on the worker thread
                final String responseData = response.body().string();
                Log.d("Data", responseData);
                if (response.code() == 200) {
                    // Run view-related code back on the main thread

                } else {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mcontext, "Failed to register", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });


    }

    public void sendNotificationUpdate(String invitationTitle, Long eventid, String eventname, String eventDescription, String eventLocation,
                                       String eventStartDate, String eventStartTime, String eventEndDate,
                                       String eventEndTime,  String invitedfirends, String notify, String target,String lat, String lng) {


        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://velma.000webhostapp.com/sendNotification_update.php").newBuilder();
        urlBuilder.addQueryParameter("invitationTitle", invitationTitle);
        urlBuilder.addQueryParameter("userid", "" + LandingActivity.imei);
        urlBuilder.addQueryParameter("eventid", "" + eventid);
        urlBuilder.addQueryParameter("eventname", eventname);
        urlBuilder.addQueryParameter("eventDescription", eventDescription);
        urlBuilder.addQueryParameter("eventLocation", eventLocation);
        urlBuilder.addQueryParameter("eventStartDate", eventStartDate);
        urlBuilder.addQueryParameter("eventStartTime", eventStartTime);
        urlBuilder.addQueryParameter("eventEndDate", eventEndDate);
        urlBuilder.addQueryParameter("eventEndTime", eventEndTime);
        urlBuilder.addQueryParameter("notify", notify);
        urlBuilder.addQueryParameter("invitedfirends", invitedfirends);
        urlBuilder.addQueryParameter("target", target);
        urlBuilder.addQueryParameter("name", LandingActivity.profilename);
        urlBuilder.addQueryParameter("lat", lat);
        urlBuilder.addQueryParameter("lng", lng);

        String Url = urlBuilder.build().toString();

        Log.d("URL", Url);

        Request request = new Request.Builder()
                .url(Url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // ... check for failure using `isSuccessful` before proceeding
                // Read data on the worker thread
                final String responseData = response.body().string();
                Log.d("Data", responseData);
                if (response.code() == 200) {
                    // Run view-related code back on the main thread

                } else {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mcontext, "Failed to register", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });


    }


    public void deleteEvent(String event_id) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://velma.000webhostapp.com/delete_event.php").newBuilder();
        urlBuilder.addQueryParameter("event_id", event_id);

        String Url = urlBuilder.build().toString();

        Log.d("URL", Url);

        Request request = new Request.Builder()
                .url(Url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // ... check for failure using `isSuccessful` before proceeding
                // Read data on the worker thread
                final String responseData = response.body().string();
                Log.d("Data", responseData);
                if (response.code() == 200) {
                    // Run view-related code back on the main thread
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mcontext, "Event successfully deleted", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mcontext, "Failed to register", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

//    public void sendMessage(JSONObject robject) {
//
//
//        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://fcm.googleapis.com/fcm/send").newBuilder();
//        urlBuilder.addQueryParameter("Authorization", "AIzaSyDKV5ZLw_4bT2CUit-J567KbZhzIql7h-I");
//        urlBuilder.addQueryParameter("data", robject.toString());
//        String url = urlBuilder.build().toString();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .method("POST", RequestBody.create(null, new byte[0]))
//                .build();
//
//        Log.d("URL", request.toString());
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//                Log.d("URL", "Fail");
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                if (!response.isSuccessful()) {
//                    throw new IOException("Unexpected code " + response);
//                }
//            }
//        });
//
//
//    }

//    public void updateEvent(Long eventid, String eventname, String eventDescription, String eventLocation,
//                            String eventStartDate, String eventStartTime, String eventEndDate,
//                            String eventEndTime, String notify, String invitedfirends) {
//
//        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://velma.000webhostapp.com/update_event.php").newBuilder();
//        urlBuilder.addQueryParameter("id", "" + eventid);
//        urlBuilder.addQueryParameter("EventName", eventname);
//        urlBuilder.addQueryParameter("EventDescription", eventDescription);
//        urlBuilder.addQueryParameter("EventLocation", eventLocation);
//        urlBuilder.addQueryParameter("StartDate", eventStartDate);
//        urlBuilder.addQueryParameter("StartTime", eventStartTime);
//        urlBuilder.addQueryParameter("EndDate", eventEndDate);
//        urlBuilder.addQueryParameter("EndTime", eventEndTime);
//        urlBuilder.addQueryParameter("Extra1", notify);
//        urlBuilder.addQueryParameter("InvitedFriends", invitedfirends);
//
//        String Url = urlBuilder.build().toString();
//
//        Log.d("URL", Url);
//
//        Request request = new Request.Builder()
//                .url(Url)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                // ... check for failure using `isSuccessful` before proceeding
//                // Read data on the worker thread
//                final String responseData = response.body().string();
//                Log.d("Data", responseData);
//                if (response.code() == 200) {
//                    // Run view-related code back on the main thread
//
//                } else {
//                    mainHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(mcontext, "Failed to update the event.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        });
//    }


    public void updateEvent(int event_id, String event_name, String event_description, String event_location,
                            String longitude, String latitude, String start_date, String start_time, String end_date,
                            String end_time, String[] recipients ) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://velma.000webhostapp.com/update_event.php").newBuilder();
        urlBuilder.addQueryParameter("event_id", "" + event_id);
        urlBuilder.addQueryParameter("event_name", event_name);
        urlBuilder.addQueryParameter("event_description", event_description);
        urlBuilder.addQueryParameter("event_location", event_location);
        urlBuilder.addQueryParameter("longitude", longitude);
        urlBuilder.addQueryParameter("latitude", latitude);
        urlBuilder.addQueryParameter("start_date", start_date);
        urlBuilder.addQueryParameter("start_time", start_time);
        urlBuilder.addQueryParameter("end_date", end_date);
        urlBuilder.addQueryParameter("end_time", end_time);
        for (int i=0; i<recipients.length; i++){
            urlBuilder.addQueryParameter("recipients", recipients[i]);
        }

        String Url = urlBuilder.build().toString();

        Log.d("URL", Url);

        Request request = new Request.Builder()
                .url(Url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // ... check for failure using `isSuccessful` before proceeding
                // Read data on the worker thread
                final String responseData = response.body().string();
                Log.d("Data", responseData);
                if (response.code() == 200) {
                    // Run view-related code back on the main thread

                } else {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mcontext, "Failed to update the event.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    public void cancelNotification(String invitationTitle, Long eventid, String eventname, String eventDescription, String eventLocation,
                                   String eventStartTime, String eventEndTime, String eventStartDate, String eventEndDate, String invitedfirends, String target) {


        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://velma.000webhostapp.com/sendNotification_cancel.php").newBuilder();
        urlBuilder.addQueryParameter("invitationTitle", invitationTitle);
        urlBuilder.addQueryParameter("userid", "" + LandingActivity.imei);
        urlBuilder.addQueryParameter("eventid", "" + eventid);
        urlBuilder.addQueryParameter("eventname", eventname);
        urlBuilder.addQueryParameter("eventDescription", eventDescription);
        urlBuilder.addQueryParameter("eventLocation", eventLocation);
        urlBuilder.addQueryParameter("eventStartDate", eventStartDate);
        urlBuilder.addQueryParameter("eventStartTime", eventStartTime);
        urlBuilder.addQueryParameter("invitedfirends", invitedfirends);
        urlBuilder.addQueryParameter("target", target);
        urlBuilder.addQueryParameter("name", LandingActivity.profilename);

        String Url = urlBuilder.build().toString();

        Log.d("URL", Url);

        Request request = new Request.Builder()
                .url(Url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // ... check for failure using `isSuccessful` before proceeding
                // Read data on the worker thread
                final String responseData = response.body().string();
                Log.d("Data", responseData);
                if (response.code() == 200) {
                    // Run view-related code back on the main thread

                } else {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mcontext, "Failed to register", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });


    }

}