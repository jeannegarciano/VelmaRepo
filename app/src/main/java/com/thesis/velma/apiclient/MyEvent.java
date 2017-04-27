package com.thesis.velma.apiclient;

/**
 * Created by admin on 09/09/2016.
 */

public class MyEvent {

    public String name = "", eventDescription = "", eventLocation = "", startDate = "", endDate = "", startTime = "", endTime = "", notify = "", contact = "";

    public MyEvent(String name, String eventDescription, String eventLocation, String startDate, String endDate,
                   String startTime, String endTime, String notify) {
        this.name = name;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notify = notify;
    }




}
