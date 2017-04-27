package com.thesis.velma.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.thesis.velma.helper.DBInfo.DataInfo;

import static com.thesis.velma.LandingActivity.db;

/**
 * Created by admin on 03/01/2017.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    public static final int database_version = 1;

    public String CREATE_EVENTS = "CREATE TABLE " + DataInfo.TABLE_EVENTS + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + DataInfo.EVENT_ID + " TEXT, " +
            "" + DataInfo.USER_ID + " TEXT, " +
            DataInfo.EVENT_NAME + " TEXT," + DataInfo.EVENT_DESCRIPTION + " TEXT," + DataInfo.EVENT_LOCATION + " TEXT," +
            DataInfo.LONGITUDE + " TEXT," + DataInfo.LATITUDE + " TEXT," + DataInfo.START_DATE + " TEXT," +
            DataInfo.START_TIME + " TEXT," + DataInfo.END_DATE + " TEXT," + DataInfo.END_TIME + " TEXT," + DataInfo.IS_WHOLE_DAY + " TEXT," + DataInfo.ROLE + " TEXT," + DataInfo.RECIPIENTS + " TEXT)";

    public String CREATE_CONTACTS = "CREATE TABLE " + DataInfo.TABLE_CONTACTS + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DataInfo.CONTACT_ID + " TEXT," + DataInfo.CONTACT_NAME + " TEXT," + DataInfo.CONTACT_EMAIL + " TEXT)";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS);
        db.execSQL(CREATE_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    //region CONSTRUCTOR
    //***********************
    //CONSTRUCTOR
    //***********************
    public DataBaseHandler(Context context) {
        super(context, DataInfo.DATABASE_NAME, null, database_version);

    }
    //endregion

    //region METHODS

    public void saveEvent(int user_id, int event_id, String event_name, String event_description, String event_location,
                          String longitude, String latitude, String start_date, String start_time, String end_date, String end_time,
                           String is_whole_day, String role, String recipients) {

        SQLiteDatabase sql = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DataInfo.USER_ID, user_id);
        cv.put(DataInfo.EVENT_ID, event_id);
        cv.put(DataInfo.EVENT_NAME, event_name);
        cv.put(DataInfo.EVENT_DESCRIPTION, event_description);
        cv.put(DataInfo.EVENT_LOCATION, event_location);
        cv.put(DataInfo.LONGITUDE, longitude);
        cv.put(DataInfo.LATITUDE, latitude);
        cv.put(DataInfo.START_DATE, start_date);
        cv.put(DataInfo.START_TIME, start_time);
        cv.put(DataInfo.END_DATE, end_date);
        cv.put(DataInfo.END_TIME, end_time);
        cv.put(DataInfo.IS_WHOLE_DAY, is_whole_day);
        cv.put(DataInfo.ROLE, role);
        cv.put(DataInfo.RECIPIENTS, recipients);

        sql.insert(DataInfo.TABLE_EVENTS, null, cv);
    }

    public void saveContact(int user_id, String name, String email) {

        SQLiteDatabase sql = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(DataInfo.CONTACT_ID, user_id);
        cv.put(DataInfo.CONTACT_NAME, name);
        cv.put(DataInfo.CONTACT_EMAIL, email);

        sql.insert(DataInfo.TABLE_CONTACTS, null, cv);

    }

    public Cursor getEvents() {

        SQLiteDatabase sql = db.getReadableDatabase();

        Cursor c = sql.rawQuery("SELECT * FROM " + DataInfo.TABLE_EVENTS, null);

        return c;

    }

    public Cursor getContacts() {

        SQLiteDatabase sql = db.getReadableDatabase();

        Cursor c = sql.rawQuery("SELECT DISTINCT * FROM " + DataInfo.TABLE_CONTACTS, null);

        return c;

    }

    public Cursor getEventDetails(long event_id) {

        SQLiteDatabase sql = db.getReadableDatabase();

        Cursor c = sql.rawQuery("SELECT * FROM " + DataInfo.TABLE_EVENTS + " Where " + DataInfo.EVENT_ID + " = "  + event_id, null);

        return c;
    }

    public void deleteEvent(int event_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(DataInfo.TABLE_EVENTS, DataInfo.EVENT_ID + " = " + event_id, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
    public Cursor getids(){

        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + DataInfo.TABLE_EVENTS, null);

        return c;
    }
public Cursor getid(String eId){

    SQLiteDatabase sql = db.getReadableDatabase();
    Cursor c = sql.rawQuery("SELECT _id FROM " + DataInfo.TABLE_EVENTS+
            " WHERE EventID = '"+eId+"'", null);

    return c;
}
    public void deleteInvite(long eId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(DataInfo.TABLE_EVENTS, "EventID =" + eId, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    //endregion
//

//    public Cursor updateconflictChecker(String sd, String st, String ed, String et,long id) {
//
//        SQLiteDatabase sql = db.getReadableDatabase();
//        Cursor c = sql.rawQuery("SELECT _id, EventName, EventDescription,StartDate, StartTime, EndTime, EndDate  FROM " + DataInfo.TABLE_EVENTS+
//                " WHERE (((StartTime BETWEEN '"+st+"' AND '"+et+
//                "' ) AND ((StartDate BETWEEN '"+sd+"' AND '"+ed+
//                "') OR (EndDate BETWEEN '"+sd+"' AND '"+ed+"') OR ('"+sd+
//                "' BETWEEN StartDate AND EndDate))) OR ((EndTime BETWEEN '"+st+
//                "' AND '"+et+"') AND ((StartDate BETWEEN '"+sd+"' AND '"+ed+
//                "') OR (EndDate BETWEEN '"+sd+"' AND '"+ed+"') OR ('"+sd+
//                "' BETWEEN StartDate AND EndDate))) OR (('"+st+
//                "' BETWEEN StartTime AND EndTime) AND ((StartDate BETWEEN '"+sd+"' AND '"+ed+
//                "') OR (EndDate BETWEEN '"+sd+"' AND '"+ed+"') OR ('"+sd+
//                "' BETWEEN StartDate AND EndDate)))) AND (EventID != '"+id+"')", null);
//        return c;
//    }

    public Cursor conflictChecker(String sd, String st, String ed, String et) {

        SQLiteDatabase sql = db.getReadableDatabase();

        Cursor c = sql.rawQuery("SELECT _id, EventName, EventDescription,StartDate, StartTime, EndTime, EndDate  FROM " + DataInfo.TABLE_EVENTS+
                " WHERE ((StartTime BETWEEN '"+st+"' AND '"+et+
                "' ) AND ((StartDate BETWEEN '"+sd+"' AND '"+ed+
                "') OR (EndDate BETWEEN '"+sd+"' AND '"+ed+"') OR ('"+sd+
                "' BETWEEN StartDate AND EndDate))) OR ((EndTime BETWEEN '"+st+
                "' AND '"+et+"') AND ((StartDate BETWEEN '"+sd+"' AND '"+ed+
                "') OR (EndDate BETWEEN '"+sd+"' AND '"+ed+"') OR ('"+sd+
                "' BETWEEN StartDate AND EndDate))) OR (('"+st+
                "' BETWEEN StartTime AND EndTime) AND ((StartDate BETWEEN '"+sd+"' AND '"+ed+
                "') OR (EndDate BETWEEN '"+sd+"' AND '"+ed+"') OR ('"+sd+
                "' BETWEEN StartDate AND EndDate)))", null);

        return c;
    }


    public Cursor conflictCheckerDaily(String sd, String st, String ed, String et, String isWholeDay) {

        SQLiteDatabase sql = db.getReadableDatabase();

        String d1= "2000-12-01 ";
        String d2= "2000-12-02 ";



        Cursor c = sql.rawQuery("SELECT user_id , event_id, event_name, event_description, start_date, start_time, end_date, end_time FROM " + DataInfo.TABLE_EVENTS+
                " WHERE ((((start_time BETWEEN '"+st+"' AND '"+et+
                "' ) AND ((start_date BETWEEN '"+sd+"' AND '"+ed+
                "') OR (end_date BETWEEN '"+sd+"' AND '"+ed+"') OR ('"+sd+
                "' BETWEEN start_date AND end_date))) OR ((end_time BETWEEN '"+st+
                "' AND '"+et+"') AND ((start_date BETWEEN '"+sd+"' AND '"+ed+
                "') OR (end_date BETWEEN '"+sd+"' AND '"+ed+"') OR ('"+sd+
                "' BETWEEN start_date AND end_date))) OR (('"+st+
                "' BETWEEN start_time AND end_time) AND ((start_date BETWEEN '"+sd+"' AND '"+ed+
                "') OR (end_date BETWEEN '"+sd+"' AND '"+ed+"') OR ('"+sd+
                "' BETWEEN start_date AND end_date)))) AND is_whole_day = 'Daily') OR ((((('"+d1+"'|| start_time) BETWEEN ('"+d1+"'||'"+st+
                "') AND ('"+d1+"'||'"+et+"')) AND ((start_date BETWEEN '"+sd+"' AND '"+ed+
                "') OR (date(end_date,'+1 day') BETWEEN '"+sd+"' AND '"+ed+"') OR ('"+sd+
                "' BETWEEN start_date AND date(end_date,'+1 day')))) OR ((('"+d1+"'||end_time) BETWEEN ('"+d1+"'||'"+st+
                "') AND ('"+d1+"'||'"+et+"')) AND ((start_date BETWEEN '"+sd+"' AND '"+ed+
                "') OR (date(end_date,'+1 day') BETWEEN '"+sd+"' AND '"+ed+"') OR ('"+sd+
                "' BETWEEN start_date AND date(end_date,'+1 day')))) OR ((('"+d1+"'||'"+st+
                "') BETWEEN ('"+d1+"'|| start_time) AND ('"+d2+"'|| end_time)) AND ((start_date BETWEEN '"+sd+"' AND '"+ed+
                "') OR (date(end_date,'+1 day') BETWEEN '"+sd+"' AND '"+ed+"') OR ('"+sd+
                "' BETWEEN start_date AND date(end_date,'+1 day')))) OR ((('"+d1+"'||'"+st+
                "') BETWEEN ('"+d1+"'|| start_time) AND ('"+d2+"'|| end_time)) AND ((start_date BETWEEN '"+sd+"' AND '"+ed+
                "')	OR (date(end_date,'+1 day') BETWEEN '"+sd+"' AND '"+ed+"') OR ('"+sd+
                "' BETWEEN start_date AND date(end_date,'+1 day'))))) AND is_whole_day = 'Reverse Daily') OR ((((start_date ||' '||start_time) BETWEEN ('"+sd+
                " '||'"+st+"') AND ('"+ed+" '||'"+et+"')) OR ((end_date||' '|| end_time) BETWEEN ('"+sd+
                " '||'"+st+"') AND ('"+ed+" '||'"+et+"')) OR (('"+sd+
                " '||'"+st+"') BETWEEN (start_date ||' '|| start_time) AND (end_date ||' '|| end_time))) AND is_whole_day = 'All Day')", null);

        return c;
    }
    public int retrieveDayEvent() {
        String countQuery = "SELECT  * FROM " + DataInfo.TABLE_EVENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        Log.d("Velama", "" + cnt);
        cursor.close();
        return cnt;
    }

    public Cursor editConflictEvent(long id) {

        SQLiteDatabase sql = db.getReadableDatabase();

        Cursor c = sql.rawQuery("SELECT EventName, EventDescription, StartDate, EndDate FROM " + DataInfo.TABLE_EVENTS + " Where _id=" + id, null);

        return c;
    }

    public void updateEvent(int event_id, String event_name, String event_description, String event_location, String longitude,
                            String latitude, String start_date, String start_time, String end_date, String end_time, String recipients) {

        SQLiteDatabase sql = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(DataInfo.EVENT_NAME, event_name);
        cv.put(DataInfo.EVENT_DESCRIPTION, event_description);
        cv.put(DataInfo.EVENT_LOCATION, event_location);
        cv.put(DataInfo.LONGITUDE, longitude);
        cv.put(DataInfo.LATITUDE, latitude);
        cv.put(DataInfo.START_DATE, start_date);
        cv.put(DataInfo.START_TIME, start_time);
        cv.put(DataInfo.RECIPIENTS, recipients);

        sql.update(DataInfo.TABLE_EVENTS, cv, DataInfo.EVENT_ID+ " = " + event_id, null);
        sql.close();
    }

    public Cursor compareLocationA(String sd, String st) {

        SQLiteDatabase sql = db.getReadableDatabase();

        Cursor c = sql.rawQuery("SELECT * FROM " + DataInfo.TABLE_EVENTS + " WHERE (start_date = '"+sd+"' AND end_time <= '"+st+
                "') ORDER BY start_time DESC LIMIT 1", null);

        return c;
    }

    public Cursor compareLocationB(String sd, String et) {
        SQLiteDatabase sql = db.getReadableDatabase();

        Cursor c = sql.rawQuery("SELECT * FROM " + DataInfo.TABLE_EVENTS + " WHERE (start_date = '"+sd+"' AND start_time >= '"+et+
                "') ORDER BY start_time ASC LIMIT 1", null);

        return c;
    }

    public Cursor getEventNames(String sd, String ed,int i,int flag) {

        String minute1;
        String minute2;
        if((flag%2)==0) {
            minute1 = "00";
            minute2 = "30";
        }else{
            minute1 = "30";
            minute2 = "00";
        }

        String i2 =""+i;
        if(i2.length()==1){
            i2="0"+i2;
        }
        String temp2;
        if((flag%2)==0) {
            temp2 =""+(i);
        }else{
            temp2 =""+(i+1);
        }

        if(temp2.length()==1){
            temp2="0"+temp2;
        }

        if(i == 23){
            minute2 = "59";
            temp2=""+i;
        }
        Log.i("Event sd", sd);
        Log.i("Event ed", ed);
        Log.i("Event st", i2+minute1);
        Log.i("Event et", temp2+minute2);
        SQLiteDatabase sql = db.getReadableDatabase();

        Cursor c = sql.rawQuery("SELECT event_name FROM "+ DataInfo.TABLE_EVENTS+
                " WHERE ((start_date BETWEEN '"+sd+"' AND '"+ed+
                "') OR (end_date BETWEEN "+sd+" AND "+ed+
                ")) AND (('"+i2+""+minute1+"' BETWEEN start_time AND end_time) OR('"+temp2+
                ""+minute2+"' BETWEEN start_time AND end_time))", null);

        return c;
    }
//                      (substr(StartDate,7)||substr(StartDate,4,2)||substr(StartDate,1,2))

    public void deleteTable ()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DataInfo.TABLE_EVENTS, null, null);
        db.close();
    }

    public Cursor searchEvents(String startDate, String startTime, String endDate, String endTime) {

        SQLiteDatabase sql = db.getReadableDatabase();

        Log.d("Query", "SELECT * FROM " + DataInfo.TABLE_EVENTS + " Where StartDate= '" + startDate + "' AND StartTime between '" + startTime + "' AND '" + endTime + "'");
        Cursor c = sql.rawQuery("SELECT * FROM " + DataInfo.TABLE_EVENTS + " Where StartDate= '" + startDate + "' ", null);//AND StartTime between '" + startTime + "' AND '" + endTime + "'

        return c;
    }

    public Cursor getMaxId()
    {
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor c = sql.rawQuery("SELECT _id FROM " + DataInfo.TABLE_EVENTS +" ORDER BY _id DESC LIMIT 1", null);


        return c;
    }

}
