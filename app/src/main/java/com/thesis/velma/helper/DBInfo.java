package com.thesis.velma.helper;

import android.provider.BaseColumns;

/**
 * Created by admin on 03/01/2017.
 */

public class DBInfo {

    public static class DataInfo implements BaseColumns {


        //DataBase Name
        public static final String DATABASE_NAME = "velma_app";

        //TableNames
        public static final String TABLE_EVENTS = "events";
        public static final String TABLE_CONTACTS = "contacts";


        //FieldName
        public static final String EVENT_ID = "event_id";
        public static final String USER_ID = "user_id";
        public static final String EVENT_NAME = "event_name";
        public static final String EVENT_DESCRIPTION = "event_description";
        public static final String EVENT_LOCATION = "event_location";
        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";
        public static final String START_DATE = "start_date";
        public static final String START_TIME = "start_time";
        public static final String END_DATE = "end_date";
        public static final String END_TIME = "end_time";
        public static final String IS_WHOLE_DAY = "is_whole_day";
        public static final String ROLE = "role";
        public static final String RECIPIENTS = "recipients";

        public static final String CONTACT_ID = "contact_user_id";
        public static final String CONTACT_NAME = "contact_name";
        public static final String CONTACT_EMAIL = "contact_email";

    }
}
