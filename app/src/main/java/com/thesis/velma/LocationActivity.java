package com.thesis.velma;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.thesis.velma.Adapter.LocationAdapter;
import com.thesis.velma.helper.LocationList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

public class LocationActivity extends AppCompatActivity {

    ArrayList<LocationList> locList;
    LocationAdapter myadapter;
    ListView listview;
    EditText txtsearch;
    String searchkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listview = (ListView) findViewById(R.id.listview);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                listview.setEnabled(false);
                String placename = ((TextView) view.findViewById(R.id.txtplace)).getText().toString();
                String placeaddress = ((TextView) view.findViewById(R.id.txtaddress)).getText().toString();

                Intent intent = getIntent();
                intent.putExtra("Place", placename);
                intent.putExtra("Address", placeaddress);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        txtsearch = (EditText) findViewById(R.id.txtsearch);

        txtsearch
                .setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        boolean handled = false;
                        if (actionId == EditorInfo.IME_ACTION_SEND) {
                            if (txtsearch.getText() != null) {
                                searchkey = txtsearch.getText().toString();
                                new fetchLocation().execute();
                            }

                            handled = true;
                        }
                        return handled;
                    }
                });


        new fetchLocation().execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }

//region THREADS

    class fetchLocation extends AsyncTask<Void, Void, String> {


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
        protected String doInBackground(Void... voids) {
            String text;

            if (searchkey == null) {
                searchkey = "restaurant";
            }

            try {
                String regAPIURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
                regAPIURL = regAPIURL + "location=10.3157,123.8854";
                regAPIURL = regAPIURL + "&keyword=" + URLEncoder.encode(searchkey);
                regAPIURL = regAPIURL + "&radius=1000";
                regAPIURL = regAPIURL + "&key=AIzaSyDIvaOIx1k7XLGI5p0LXQPz_H-aoNieGHI";
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
            // Log.d("Data", s);

            String name = null, firstAddress = null;

            if (s != null) {


                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray arrayresult = jsonObject.getJSONArray("results");
                    Log.d("Array", "" + arrayresult);

                    locList = new ArrayList<LocationList>();
                    for (int i = 0; i < arrayresult.length(); i++) {
                        LocationList mylist = new LocationList();
                        name = arrayresult.getJSONObject(i).getString("name");
                        firstAddress = arrayresult.getJSONObject(i).getString("vicinity");
                        Log.d("Data", name + ":" + firstAddress);
                        mylist.setPlacename(name);
                        mylist.setPlaceaddress(firstAddress);
                        locList.add(mylist);
                    }


                    myadapter = new LocationAdapter(getBaseContext(), R.layout.list_location, locList);
                    listview.setAdapter(null);
                    listview.setAdapter(myadapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }
    }


//endregion

//region FUNCTIONS

//endregion


}
