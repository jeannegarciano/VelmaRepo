package com.thesis.velma.apiclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.thesis.velma.Entity.EventsEntity;
import com.thesis.velma.LandingActivity;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by cathlynjaneamodia on 10/2/2017.
 */

public interface ApiService {

    @GET("/events.php")
    public void getMyJSON(Callback<List<EventsEntity>> response);
}