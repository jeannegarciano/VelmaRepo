package com.thesis.velma;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by admin on 5/23/2016.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();

        //registerToken(token);

        Log.d("onTokenRefresh", FirebaseInstanceId.getInstance().getToken());
    }

    private void registerToken(String token) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token",token)
                .build();

        Request request = new Request.Builder()
              //  .addHeader("Accept", "application/json")// .addHeader("Content-Type", "text/html")
                .url("http://192.168.197.1/fcmphp/register.php?")
                .post(body)
                .build();
       Log.d("sd", "sd" + request);
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create http cliient object to send request to server
//
//        OkHttpClient Client = new OkHttpClient();
//
//        // Create URL string
//        HttpURLConnection connection = null;
//        String dataUrl = "http://localhost/fcmphp/register.php?Token=asdasdasd";
//
//        //Log.i("httpget", URL);
//
//        try
//        {
//            java.net.URL url = new URL(dataUrl);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setUseCaches(true);
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
//
//            DataOutputStream wr = new DataOutputStream(
//                    connection.getOutputStream());
//            wr.writeBytes(dataUrl);
//            wr.flush();
//            wr.close();
//
//            InputStream is = connection.getInputStream();
//            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//            String line;
//            StringBuffer response = new StringBuffer();
//            while ((line = rd.readLine()) != null) {
//                response.append(line);
//                response.append('\r');
//            }
//            rd.close();
//            String responseStr = response.toString();
//            Log.d("Server response",responseStr);
//        }
//        catch(Exception ex)
//        {
//            Log.d("Server response", "ASDASD");
//        }
   }
}