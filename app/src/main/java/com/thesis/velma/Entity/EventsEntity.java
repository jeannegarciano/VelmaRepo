package com.thesis.velma.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cathlynjaneamodia on 10/2/2017.
 */

public class EventsEntity {

    @SerializedName("user_id")
    @Expose
    private int user_id;

    @SerializedName("event_id")
    @Expose
    private int event_id;

    @SerializedName("event_name")
    @Expose
    private String event_name;

    @SerializedName("event_description")
    @Expose
    private String event_description;

    @SerializedName("event_location")
    @Expose
    private String event_location;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("start_date")
    @Expose
    private String start_date;

    @SerializedName("start_time")
    @Expose
    private String start_time;

    @SerializedName("end_date")
    @Expose
    private String end_date;

    @SerializedName("end_time")
    @Expose
    private String end_time;

    @SerializedName("is_whole_day")
    @Expose
    private String is_whole_day;

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    public EventsEntity(int user_id, int event_id, String event_name, String event_description, String event_location, String longitude, String latitude, String start_date, String start_time, String end_date, String end_time, String is_whole_day, String role, String status, String name, String email) {
        this.setUser_id(user_id);
        this.setEvent_id(event_id);
        this.setEvent_name(event_name);
        this.setEvent_description(event_description);
        this.setEvent_location(event_location);
        this.setLongitude(longitude);
        this.setLatitude(latitude);
        this.setStart_date(start_date);
        this.setStart_time(start_time);
        this.setEnd_date(end_date);
        this.setEnd_time(end_time);
        this.setIs_whole_day(is_whole_day);
        this.setRole(role);
        this.setStatus(status);
        this.setName(name);
        this.setEmail(email);
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getEvent_location() {
        return event_location;
    }

    public void setEvent_location(String event_location) {
        this.event_location = event_location;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getIs_whole_day() {
        return is_whole_day;
    }

    public void setIs_whole_day(String is_whole_day) {
        this.is_whole_day = is_whole_day;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}