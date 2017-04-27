package com.thesis.velma;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.thesis.velma.helper.DataBaseHandler;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by admin on 2/26/2017.
 */

public class DateListView extends AppCompatActivity {

    String sd, ed;
    String id;
    Context context;
    public static DataBaseHandler db;
    private ConflictEventsAdapter mAdapter;
    private RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_conflict);

        context = this;
        final ListView eventListView = (ListView) findViewById(R.id.eventsList);
        mAdapter = new ConflictEventsAdapter(this, new ArrayList<ConflictEvents>());
        eventListView.setAdapter(mAdapter);
        db = new DataBaseHandler(context);
        Cursor c;
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        final ActionBar ab = getSupportActionBar();
//        ab.setHomeAsUpIndicator(R.drawable.velmalogo);
//        ab.setLogo(R.drawable.velmalogo);
//        ab.setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Suggest Time Activity");


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sd = preferences.getString("startdate", "value");
        ed = preferences.getString("enddate", "value");
        Log.i("Event list date", sd+" || " +ed);
        int flag=0;
        for(int i = 0; i < 48; i++) {
            String eventnames = "";

            c = db.getEventNames(sd, ed,(i/2),flag);

            c.moveToFirst();
            Log.i("Event c", "" + c.getCount());
            Log.i("Event datelist", sd + "  " + ed);
            if (c.getCount() > 0) {
                eventnames = " " + c.getString(c.getColumnIndex("event_name"));
                Log.i("Event firstname", "" +eventnames);
                c.moveToNext();
                while (!c.isAfterLast()) {
                    eventnames = ""+eventnames+", " + c.getString(c.getColumnIndex("event_name"));
                    // date_list[i]= date_list[i]+", "+c.getString(c.getColumnIndex("EventName"));

                    c.moveToNext();
                }


            }

            velmaTime(eventnames, (i/2),flag);
            flag++;
        }

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {




                String time=mAdapter.getItem(position).getmTime();
                Log.i("Event click", " time: "+time);
//                    Toast.makeText(getApplicationContext(),
//                            "Click ListItem Number: " + position +" "+time+" "+ v, Toast.LENGTH_LONG)
//                            .show();

                StringTokenizer newdateToken = new StringTokenizer(time, "- ");
                String new_st = newdateToken.nextToken();
                String ampm1=newdateToken.nextToken();
                String new_et= newdateToken.nextToken();
                String ampm2=newdateToken.nextToken();
                if(("pm").equals((ampm1).toLowerCase())){
                    new_st=""+(Integer.parseInt(new_st)+12);
                }
                if(("pm").equals((ampm2).toLowerCase())){
                    new_et=""+(Integer.parseInt(new_et)+12);
                }
                Log.i("Event new_st", new_st);
                Log.i("Event new_et", new_et);

                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("new_st", new_st);
                editor.putString("new_et", new_et );
                editor.apply();


                Intent i = new Intent(DateListView.this, ConflictActivity.class);
//                    i.putExtra("eventID", id);
                i.putExtra("new_st", new_st);
                i.putExtra("new_et", new_et);
                finish();
                startActivity(i);
            }

        });

    }

    private void velmaTime(String eventnames, int i,int flag){


        String stnd1="AM";
        String stnd2="AM";
        String min1="";
        String min2="";
        String i2=""+i;
        String i1=""+i;
        if(i>=12){
            stnd1="PM";
            stnd2="PM";
        }
        if((i==11&&flag%2==1)){
            stnd2="PM";
        }
        if(i==23&&(flag%2==1)){
            stnd2="AM";
        }


        if(i>=13){
            i=i-12;

        }
        i1=""+i;
        i2=""+i;
        if(i==0){
            i1="12";
            i2="12";
        }
        if((flag%2)==0){
            min1="00";
            min2="30";

        }else if(flag%2==1){
            min1="30";
            min2="00";
            if(i==12){
                i2="01";
            }else{
                i2=""+(i+1);
            }
        }


        if(i1.length()==1){
            i1="0"+i1;
        }
        if(i2.length()==1){
            i2="0"+i2;
        }
//        ConflictEvents event1 = new ConflictEvents(""+i2+":"+minute1+" -  "+temp2+":"+minute2+"", eventnames);

        ConflictEvents event1 = new ConflictEvents(""+i1+":"+min1+" "+stnd1+" -  "+i2+":"+min2+" "+stnd2+"", eventnames);
        mAdapter.add(event1);

    }


//
//    public static void main(String[] args) {
//        // TODO code application logic here
//
//        int flag = 0;
//        for (int i = 0; i < 48; i++) {
//
//
//            viewlist(flag,(i/2));
//
//            flag++;
//        }
//    }
//
//    public static void viewlist(int flag,int i) {
//        String stnd1="AM";
//        String stnd2="AM";
//        String min1="";
//        String min2="";
//        String i2=""+i;
//        String i1=""+i;
//        if(i>=12){
//            stnd1="PM";
//            stnd2="PM";
//        }
//        if((i==11&&flag%2==1)){
//            stnd2="PM";
//        }
//        if(i==23&&(flag%2==1)){
//            stnd2="AM";
//        }
//
//
//        if(i>=13){
//            i=i-12;
//
//        }
//        i1=""+i;
//        i2=""+i;
//        if(i==0){
//            i1="12";
//            i2="12";
//        }
//        if((flag%2)==0){
//            min1=":00";
//            min2=":30";
//
//        }else if(flag%2==1){
//            min1=":30";
//            min2=":00";
//            if(i==12){
//                i2="01";
//            }else{
//                i2=""+(i+1);
//            }
//        }
//
//
//        if(i1.length()==1){
//            i1="0"+i1;
//        }
//        if(i2.length()==1){
//            i2="0"+i2;
//        }
//
//        System.out.println(""+i1+""+min1+" "+stnd1+" - "+i2+""+min2+" "+stnd2);
//    }
}