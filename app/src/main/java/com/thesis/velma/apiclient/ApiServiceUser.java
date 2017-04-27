package com.thesis.velma.apiclient;

import com.thesis.velma.Entity.UsersEntity;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by cathlynjaneamodia on 28/3/2017.
 */

public interface ApiServiceUser {

    @GET("/users.php")
    public void getMyJSON(Callback<List<UsersEntity>> response);
}
