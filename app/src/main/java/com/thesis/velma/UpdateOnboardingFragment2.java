package com.thesis.velma;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.thesis.velma.helper.DataBaseHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by cathlynjaneamodia on 14/2/2017.
 */

public class UpdateOnboardingFragment2 extends Fragment implements View.OnClickListener{
    View rootView;
    public static int sYear, sMonth, sDay, sHour, sMinute;
    public static int eYear, eMonth, eDay, eHour, eMinute;
    public static TextView dateStart;
    public static TextView dateEnd;
    public static TextView timeStart;
    public static TextView timeEnd;
    public static EditText alarming;
    DatePickerDialog datePickerDialog;
    int PLACE_PICKER_REQUEST = 1;
    long timeInMilliseconds;

    public static DataBaseHandler db;
    Context mcontext;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.update_onboarding_screen2, container, false);

        db = new DataBaseHandler(mcontext);

        // Get current date by calender

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String id = preferences.getString("key", "value");

        Log.d("success:", id);

        Cursor c = db.getEventDetails(Long.valueOf(id));

        String startDate="", endDate="", startTime="", endTime="";

        while (c.moveToNext()){
            startDate = c.getString(c.getColumnIndex("StartDate"));
            endDate = c.getString(c.getColumnIndex("EndDate"));
            startTime = c.getString(c.getColumnIndex("StartTime"));
            endTime = c.getString(c.getColumnIndex("EndTime"));
        }

        dateStart = (TextView)rootView.findViewById(R.id.startdate);
        dateStart.setText(startDate);
        dateStart.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        dateStart.setInputType(InputType.TYPE_NULL);
        dateEnd = (TextView)rootView.findViewById(R.id.enddate);
        dateEnd.setText(endDate);
        dateEnd.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        timeStart = (TextView)rootView.findViewById(R.id.starttime);
        timeStart.setText(startTime);
        timeStart.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        timeEnd = (TextView)rootView.findViewById(R.id.endtime);
        timeEnd.setText(endTime);
        timeEnd.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        alarming = (EditText) rootView.findViewById(R.id.alarm);
        alarming.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        dateStart.setOnClickListener(this);
        dateEnd.setOnClickListener(this);
        timeStart.setOnClickListener(this);
        timeEnd.setOnClickListener(this);
        alarming.setOnClickListener(this);



        datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {



                        String sd = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                        SimpleDateFormat sdf;
                        try {


                            sdf = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

                            Date mDate = sdf.parse(sd);
                            String strdate = sdf.format(mDate);
                            dateStart.setText(strdate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }






                    }
                }, sYear, sMonth, sDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 10000);



        return rootView;
    }

    @Override
    public void onClick(View view) {


        if (view == dateStart) {
            final Calendar c = Calendar.getInstance();
            sYear = c.get(Calendar.YEAR);
            sMonth = c.get(Calendar.MONTH);
            sDay = c.get(Calendar.DAY_OF_MONTH);



            datePickerDialog.show();
        }  if (view == dateEnd) {
            final Calendar c = Calendar.getInstance();
            eYear = c.get(Calendar.YEAR);
            eMonth = c.get(Calendar.MONTH);
            eDay = c.get(Calendar.DAY_OF_MONTH);
            String datetime;
            //i'll just put a static starttime kay para mmugana ang validation whether ni
            if(dateStart.getText() == null){
                dateEnd.setClickable(false);
            }else {
                datetime = dateStart.getText().toString() + " 00:00";

                //    datetime = dateStart.getText().toString() + " " + timeStart.getText().toString();

                SimpleDateFormat sdf;
                try {

                    Log.i("Event dt", datetime);
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());

                    Date mDate = sdf.parse(datetime);
                    String strdate = sdf.format(mDate);
                    Log.i("Event String", strdate);
                    Log.i("Event mDate", "" + mDate);
                    timeInMilliseconds = mDate.getTime();
                    Log.i("Date in milli :: ", "" + timeInMilliseconds);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String ed = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                                SimpleDateFormat sdf;
                                try {


                                    sdf = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

                                    Date mDate = sdf.parse(ed);
                                    String nddate = sdf.format(mDate);
                                    dateEnd.setText(nddate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, eYear, eMonth, eDay);
                datePickerDialog.getDatePicker().setMinDate(timeInMilliseconds);
                Log.i("Event datepicker", String.valueOf(System.currentTimeMillis() - 10000));
                datePickerDialog.show();
            }
        }  if (view == timeStart) {

            final Calendar cal = Calendar.getInstance();
            sHour = cal.get(Calendar.HOUR_OF_DAY);
            sMinute = cal.get(Calendar.MINUTE);

            long start = cal.getTimeInMillis();
            Log.i("Event start", ""+start);
            if(timeStart.getText()== null ){
                timeEnd.setEnabled(false);
            }
            else {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {



                                String st = hourOfDay + ":" + minute;

                                SimpleDateFormat sdf;
                                try {


                                    sdf = new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());

                                    Date mDate = sdf.parse(st);
                                    String strtime = sdf.format(mDate);
                                    timeStart.setText(strtime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, sHour, sMinute, false);


                timePickerDialog.show();
            }

        }  if (view == timeEnd) {

            // Get Current Time

            final Calendar c = Calendar.getInstance();
            eHour = c.get(Calendar.HOUR_OF_DAY);
            eMinute = c.get(Calendar.MINUTE);


            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

//
//                            if(cal.getTimeInMillis() > c.getTimeInMillis()) {
//
//                                Toast.makeText(getContext(), "Can't add time", Toast.LENGTH_SHORT).show();
//                            }else {
//                                timeEnd.setText(hourOfDay + ":" + minute);
//                            }

                            //  timeEnd.setText(hourOfDay + ":" + minute);

                            String st = hourOfDay + ":" + minute;

                            SimpleDateFormat sdf;
                            try {


                                sdf = new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());

                                Date mDate = sdf.parse(st);
                                String ndtime = sdf.format(mDate);
                                timeEnd.setText(ndtime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, eHour, eMinute, false);


            timePickerDialog.show();
        } else if (view == alarming) {

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            alertBuilder.setIcon(R.drawable.alarm);
            alertBuilder.setTitle("Alarm every: ");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.select_dialog_item);
            arrayAdapter.add("At time of event");
            arrayAdapter.add("10 minutes before the event");
            arrayAdapter.add("20 minutes before the event");
            arrayAdapter.add("30 minutes before the event");
            arrayAdapter.add("40 minutes before the event");
            arrayAdapter.add("50 minutes before the event");
            arrayAdapter.add("1 hour before the event");

            alertBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                        }
                    });

            alertBuilder.setAdapter(arrayAdapter,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            String strOS = arrayAdapter.getItem(which);
                            alarming.setText(strOS);
                            dialog.dismiss();
                        }
                    });

            final AlertDialog alertDialog = alertBuilder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                    ListView listView = alertDialog.getListView();
                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                        @Override
                        public boolean onItemLongClick(
                                AdapterView<?> parent, View view,
                                int position, long id) {
                            // TODO Auto-generated method stub
                            String strOS = arrayAdapter.getItem(position);
//                            Toast.makeText(getContext(),
//                                    "Long Press - Deleted Entry " + strOS,
//                                    Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            return true;
                        }
                    });
                }
            });

            alertDialog.show();
        }


    }

}