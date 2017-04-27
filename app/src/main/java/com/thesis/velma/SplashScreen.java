package com.thesis.velma;

/**
 * Created by jeanneviegarciano on 7/20/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

public class SplashScreen extends AppCompatActivity {

    GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;
    private static final int SIGN_IN_CODE = 0;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;
    private int request_code;

    Context mcontext;
    boolean isFirstRun;


    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
////        getSupportActionBar().hide();
//
//        buidNewGoogleApiClient();
        setContentView(R.layout.activity_splash_screen);

        mcontext = this;

//        Countdown _tik;
//        _tik=new Countdown(5000,5000,this,LandingActivity.class);
//        _tik.start();
        // StartAnimations();


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mcontext);
        //prefs.edit().putBoolean("isFirstRun", true).commit();
        Boolean isFirstRun = prefs.getBoolean("isFirstRun", false);
        Boolean isLoggedIn = prefs.getBoolean("isLoggedIn", true);
        Toast.makeText(mcontext, "" + isFirstRun, Toast.LENGTH_LONG).show();

        if (isFirstRun) {
//maybe you want to check it by getting the sharedpreferences. Use this instead if (locked)
// if (prefs.getBoolean("locked", locked) {\
            prefs.edit().putBoolean("isFirstRun", true).commit();
            //Toast.makeText(mcontext,"Login",Toast.LENGTH_SHORT).show();

//             Intent i = new Intent(SplashScreen.this,LoginActivity.class);
//             startActivity(i);

            if (isLoggedIn) {
                // "Landing";
                this.finish();
                Intent i = new Intent(SplashScreen.this, LandingActivity.class);
                startActivity(i);
            } else {
                this.finish();
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);
                // "Login";
            }


        } else {

            this.finish();
            Intent i = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(i);
//            Intent a = new Intent(SplashScreen.this,TutorialActivity.class);
//            startActivity(a);

            //  Toast.makeText(mcontext,"Tutorial",Toast.LENGTH_SHORT).show();


            //startActivity(new Intent(mcontext, Tag1.class));
            //buidNewGoogleApiClient();
            // Intent i = new Intent(SplashScreen.this,LoginActivity.class);
            // startActivity(i);
        }

//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        boolean first = prefs.getBoolean("key_first_launch", false);
//
//        if (first)
//        {
//            Intent i = new Intent(SplashScreen.this,TutorialActivity.class);
//            startActivity(i);
//        }
//
//        else{
////
//            Intent i = new Intent(SplashScreen.this,LoginActivity.class);
//            startActivity(i);
//        }

    }

//    private void StartAnimations() {
//        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
//        anim.reset();
//        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
//        l.clearAnimation();
//        l.startAnimation(anim);
//
//        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
//        anim.reset();
//        ImageView iv = (ImageView) findViewById(R.id.logo);
//        iv.clearAnimation();
//        iv.startAnimation(anim);
//
//    }


}
