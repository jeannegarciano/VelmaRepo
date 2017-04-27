package com.thesis.velma;

/**
 * Created by jeanneviegarciano on 7/20/2016.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.PeopleScopes;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.PhoneNumber;
import com.google.firebase.messaging.FirebaseMessaging;
import com.thesis.velma.Entity.UsersEntity;
import com.thesis.velma.apiclient.ApiServiceUser;
import com.thesis.velma.helper.CheckInternet;
import com.thesis.velma.helper.DataBaseHandler;
import com.thesis.velma.helper.NetworkUtil;
import com.thesis.velma.helper.OkHttp;
import com.thesis.velma.helper.PeopleHelper;
import com.thesis.velma.helper.progressDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.thesis.velma.R.id.sign_in_button;


public class LoginActivity extends AppCompatActivity implements OnConnectionFailedListener, View.OnClickListener, ConnectionCallbacks {

    // These declarations are for google plus sign in
    GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;
    SignInButton signIn_btn;
    private static final int SIGN_IN_CODE = 0;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;
    private int request_code;
    ProgressDialog progress_dialog;
    // up to here

    static Context mcontext;

    //These are the declarations for getting the token for push notification implementation
    // private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;
    private boolean isReceiverRegistered;

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0001;
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE = 0003;

    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

    String[] permissions = new String[]{
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    String imei;
    TextView tagline;

    DataBaseHandler db;

    CheckInternet connectCheck;
    ImageView logo;
    TextView tag;

    public static final String ROOT_URL = "http://velma.000webhostapp.com";
    private List<UsersEntity> usersEntityList;
    String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean isFirstRun = prefs.getBoolean("isFirstRun", false);
        Boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        Boolean isLoggedOut = prefs.getBoolean("isLoggedOut", true);

        ImageView logo = (ImageView)findViewById(R.id.velmaLogo);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/avenir-next-regular.ttf");
        tagline = (TextView)findViewById(R.id.header);
        tagline.setTypeface(custom_font);
        signIn_btn = (SignInButton) findViewById(sign_in_button);
        signIn_btn.setSize(SignInButton.SIZE_STANDARD);
        signIn_btn.setOnClickListener(this);


        connectCheck = new CheckInternet(this);

        if (isFirstRun) {
            prefs.edit().putBoolean("isFirstRun", true).commit();

            if (isLoggedIn) {
                // "Landing";
                this.finish();
                prefs.edit().putBoolean("isLoggedIn", true).commit();

                Intent i = new Intent(this, LandingActivity.class);
                startActivity(i);
            } else {
                signIn_btn.setVisibility(View.VISIBLE);
            }

        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .requestServerAuthCode(getString(R.string.client_id))
                .requestScopes(Plus.SCOPE_PLUS_LOGIN, Plus.SCOPE_PLUS_PROFILE, new Scope(PeopleScopes.CONTACTS_READONLY), new Scope("https://www.googleapis.com/auth/plus.profile.emails.read"))
                .requestScopes(new Scope("https://www.googleapis.com/auth/contacts.readonly"))
                .build();

        google_api_client = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .addApi(AppIndex.API).build();


        progress_dialog = new ProgressDialog(this);
        progress_dialog.setMessage("Signing in....");

        boolean firstRun = getPreferences(Context.MODE_PRIVATE).getBoolean("itsMyFirst", true);

        if (firstRun) {
            signIn_btn.setVisibility(View.VISIBLE);

            Intent mainAppIntent = new Intent(this, TutorialActivity.class);
            startActivity(mainAppIntent);
            getPreferences(Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("itsMyFirst", false)
                    .commit();

            prefs.edit().putBoolean("isLoggedIn", false).commit();
        }


        mcontext = this;

        db = new DataBaseHandler(mcontext);
        checkPermissions();
    }


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    //region FUNCTIONS

    public void continueLogin() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = telephonyManager.getDeviceId();


    }

    public void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Denied");
        builder.setMessage("Unable to start application. Please go to Settings -> Apps/Manage Application -> " +
                "Velma -> Permissions, then enable requested permisions for security purposes. Thank you.");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginActivity.this.finish();
            }
        });
        builder.show();
    }


    private void buidNewGoogleApiClient() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(google_api_client);
        startActivityForResult(signInIntent, 0002);

    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            progressDialog.showDialog(mcontext, "Velma", "Signing In. Please wait...", false);

            GoogleSignInAccount acct = result.getSignInAccount();
            // Get account information
            String mFullName = acct.getDisplayName();
            mEmail = acct.getEmail();

            String[] topicname = mEmail.split("@");

            Log.d("Topic", topicname[0] + "Velma");
            continueLogin();

            FirebaseMessaging.getInstance().subscribeToTopic(topicname[0] + "Velma");
            new PeoplesAsync().execute(acct.getServerAuthCode());
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mcontext);
            prefs.edit().putBoolean("isLoggedIn", true).commit();
            prefs.edit().putString("FullName", mFullName).commit();
            prefs.edit().putString("Email", mEmail).commit();
            prefs.edit().putString("imei", imei).commit();

            Intent p = new Intent(LoginActivity.this, LandingActivity.class);
            Bundle pass = new Bundle();
            pass.putString("userid", imei);
            pass.putString("useremail", mEmail);
            p.putExtras(pass);
            OkHttp.getInstance(mcontext).saveProfile(mEmail, mFullName);

        }

        else
        {
            progressDialog.hideDialog();
            changeUI(false);
        }

    }

    private void changeUI(boolean signedIn) {
        if (signedIn) {
            findViewById(sign_in_button).setVisibility(View.GONE);
        } else {

            findViewById(sign_in_button).setVisibility(View.VISIBLE);
        }
    }


    //endregion

    //region LISTENERS

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case sign_in_button:

                int status = NetworkUtil.getConnectivityStatusString(mcontext);

                if (status == 0) {
                    CheckInternet.showConnectionDialog(mcontext);

                }
                else{
                    buidNewGoogleApiClient();
                }

                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        //It's okay to leave this as blank.
    }


    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {

        // Check which request we're responding to
        progress_dialog.dismiss();
        Log.d("Activity Res", "" + requestCode);
        if (requestCode == 0002) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            handleSignInResult(result);


        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {


        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    showPermissionDialog();
                }
                break;

            case MY_PERMISSIONS_REQUEST_READ_PHONE:
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    continueLogin();
                } else {
                    LoginActivity.this.finish();
                }
                break;
            case MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                    continueLogin();

                } else {
                    // no permissions granted.
                    // this.finish();
                }
                break;
        }
    }


    @Override
    public void onConnected(Bundle arg0) {

    }


    @Override
    public void onConnectionSuspended(int arg0) {
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        google_api_client.connect();
        AppIndex.AppIndexApi.start(google_api_client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(google_api_client, getIndexApiAction());
        google_api_client.disconnect();
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }


    //endregion


    //region THREADS

    class PeoplesAsync extends AsyncTask<String, Void, List<String>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<String> doInBackground(String... params) {

            List<String> nameList = new ArrayList<>();

            try {
                People peopleService = PeopleHelper.setUp(LoginActivity.this, params[0]);

                ListConnectionsResponse response = peopleService.people().connections()
                        .list("people/me")
                        // This line's really important! Here's why:
                        // http://stackoverflow.com/questions/35604406/retrieving-information-about-a-contact-with-google-people-api-java
                        .setRequestMaskIncludeField("person.names,person.emailAddresses,person.phoneNumbers")
                        .execute();
                List<Person> connections = response.getConnections();

                for (Person person : connections) {
                    if (!person.isEmpty()) {
                        List<Name> names = person.getNames();
                        List<EmailAddress> emailAddresses = person.getEmailAddresses();
                        List<PhoneNumber> phoneNumbers = person.getPhoneNumbers();

                        if (phoneNumbers != null)
                            for (PhoneNumber phoneNumber : phoneNumbers)
                                Log.d(TAG, "phone: " + phoneNumber.getValue());

                        if (emailAddresses != null)
                            for (final EmailAddress emailAddress : emailAddresses) {

                                for (final Name ename : names){
                                    Log.d(TAG, "email: " + emailAddress.getValue());

                                    RestAdapter adapter = new RestAdapter.Builder()
                                            .setEndpoint(ROOT_URL)
                                            .build();

                                    ApiServiceUser apiService = adapter.create(ApiServiceUser.class);
                                    apiService.getMyJSON(new Callback<List<UsersEntity>>() {
                                        @Override
                                        public void success(List<UsersEntity> usersEntities, Response response) {

                                            usersEntityList = usersEntities;

                                            for (int i=0; i<usersEntityList.size(); i++){
                                                int user_id = usersEntityList.get(i).getUser_id();
                                                String email = usersEntityList.get(i).getEmail();

                                                if(emailAddress.getValue().equals(email)){
                                                    db.saveContact(user_id, ename.getDisplayName(), emailAddress.getValue());
                                                }

                                                if(mEmail.equals(email)){
                                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mcontext);
                                                    prefs.edit().putString("user_id", String.valueOf(user_id)).commit();
                                                }
                                            }

                                        }

                                        @Override
                                        public void failure(RetrofitError error) {

                                        }
                                    });
                                }

                            }

                        if (names != null)
                            for (Name name : names)
                                nameList.add(name.getDisplayName());

                        progressDialog.hideDialog();
                        Intent intent = new Intent(mcontext, LandingActivity.class);
                        mcontext.startActivity(intent);
                        ((Activity) mcontext).finish();

                    } else {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mcontext);
                        prefs.edit().putBoolean("isLoggedIn", false).commit();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return nameList;
        }


        @Override
        protected void onPostExecute(List<String> nameList) {
            super.onPostExecute(nameList);

        }
    }


    //endregion
}
