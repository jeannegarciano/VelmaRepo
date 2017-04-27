package com.thesis.velma;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import static android.content.ContentValues.TAG;

/**
 * Created by cathlynjaneamodia on 14/2/2017.
 */

public class UpdateOnboardingFragment1 extends Fragment implements View.OnClickListener {

    //    private static final int RESULT_OK = 0;
    View rootView;
    public static TextView des;
    public static EditText descrip;
    public static TextView loc;
    public static TextView locate;
    public static TextView distanceduration;
    int PLACE_PICKER_REQUEST = 1;
    Double latitude, longtiude;

    public static String geolocation;
    Spinner spintravel;
    String modetravel = "driving";
    PlaceAutocompleteFragment autocompleteFragment;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (rootView == null) {

            rootView = inflater.inflate(R.layout.update_onboarding_screen1, container, false);


            des = (TextView) rootView.findViewById(R.id.eventName);
            descrip = (EditText) rootView.findViewById(R.id.name);
            loc = (TextView) rootView.findViewById(R.id.location);
            locate = (TextView) rootView.findViewById(R.id.locationText);
            locate.setHintTextColor(getResources().getColor(R.color.colorPrimary));
            spintravel = (Spinner) rootView.findViewById(R.id.spinner);
            distanceduration = (TextView) rootView.findViewById(R.id.distanceduration);
            autocompleteFragment = (PlaceAutocompleteFragment) getActivity().
                    getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


            autocompleteFragment.setBoundsBias(new LatLngBounds(
                    new LatLng(14.599512, 120.984222),
                    new LatLng(14.599512, 120.984222)));


            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE)
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                    .build();
            autocompleteFragment.setFilter(typeFilter);

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    // TODO: Get info about the selected place.
                    Log.i(TAG, "Place: " + place.getAddress());//get place details here

                    locate.setText("" + place.getAddress());
                    geolocation = place.getAddress().toString();

                    latitude = place.getLatLng().latitude;
                    longtiude = place.getLatLng().longitude;


                    Log.d("latlang", "" + latitude + ":" + longtiude);

                    new UpdateOnboardingFragment1.getDetails().execute();

                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }
            });

        }

        spintravel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                modetravel = spintravel.getSelectedItem().toString().toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        return rootView;


    }


    //region Listeners


    @Override
    public void onClick(View view) {
        //       if (view == locate) {
//            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//            Intent intent;
//
//            try {
//                intent = builder.build(getActivity());
//                startActivityForResult(intent, PLACE_PICKER_REQUEST);
//            } catch (GooglePlayServicesRepairableException e) {
//                e.printStackTrace();
//            } catch (GooglePlayServicesNotAvailableException e) {
//                e.printStackTrace();
//            }
//            Intent intent = new Intent(getContext(), LocationActivity.class);
//            startActivityForResult(intent, PLACE_PICKER_REQUEST);
//
//        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle res = data.getExtras();
                String place = res.getString("Place");
                String address = res.getString("Address");
                // Log.d("FIRST", "result:" + result);
//                Place place = PlacePicker.getPlace(data, getActivity());
//                String address = String.format("%s", place.getAddress());
                locate.setText(place + "" + address);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    //endregion

    //region THREADS

    class getDetails extends AsyncTask<Void, Void, String> {

        protected String getASCIIContentFromEntity(HttpEntity entity)
                throws IllegalStateException, IOException {
            InputStream in = entity.getContent();
            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n > 0) {
                byte[] b = new byte[4096];
                n = in.read(b);
                if (n > 0)
                    out.append(new String(b, 0, n));
            }
            return out.toString();
        }

        @Override
        protected String doInBackground(Void... params) {

            String text = null;
            String coordinates = latitude + "," + longtiude;
            Log.d("Coordinates", coordinates);
            try {
                String regAPIURL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + LandingActivity.origlatitude + "," + LandingActivity.origlongitude;
                regAPIURL = regAPIURL + "&destinations=" + URLEncoder.encode(coordinates);
                regAPIURL = regAPIURL + "&mode=" + URLEncoder.encode(modetravel);
                regAPIURL = regAPIURL + "&key=AIzaSyDWjoAbJf9uDrLCFAM_fCSWxP0muVEGbOA";
                Log.d("URI", regAPIURL);
                HttpGet httpGet = new HttpGet(regAPIURL);
                HttpParams httpParameters = new BasicHttpParams();
                int timeoutConnection = 60000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                int timeoutSocket = 60000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);

            } catch (Exception e) {
                text = null;
            }

            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Result", s);


            if (s != null) {


                try {


                    String distance = new JSONObject(s)
                            .getJSONArray("rows")
                            .getJSONObject(0)
                            .getJSONArray("elements")
                            .getJSONObject(0)
                            .getJSONObject("distance").getString("text");

                    String duration = new JSONObject(s)
                            .getJSONArray("rows")
                            .getJSONObject(0)
                            .getJSONArray("elements")
                            .getJSONObject(0)
                            .getJSONObject("duration").getString("text");

                    distanceduration.setText("Distance : " + distance + ": Duration : " + duration);
                    distanceduration.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
    }

    //endregion
}


