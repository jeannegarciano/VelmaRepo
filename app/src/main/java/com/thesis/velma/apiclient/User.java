package com.thesis.velma.apiclient;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 9/17/2016.
 */
public class User {
    //    private String token;
    private String email;
    private String name;


    public User() {

    }

    public User(String email, String name) {

        this.email = email;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("name", name);

        return result;
    }
}


