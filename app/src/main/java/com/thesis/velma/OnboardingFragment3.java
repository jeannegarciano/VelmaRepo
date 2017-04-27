package com.thesis.velma;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.PersonBuffer;

import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by admin on 8/10/2016.
 */
public class OnboardingFragment3 extends Fragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, ResultCallback<People.LoadPeopleResult> {

    GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;
    SignInButton signIn_btn;
    private static final int SIGN_IN_CODE = 0;
    private static final int PROFILE_PIC_SIZE = 120;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;
    private int request_code;
    ProgressDialog progress_dialog;


    View rootView;
    public static TextView text;
    public static EditText mtxtinvited;
    public static Button inviteFriends;

    int REQUEST_INVITE = 1;

    ArrayList<String> myContacts = new ArrayList<String>();
    public static ArrayList<String> invitedContacts = new ArrayList<String>();

    Dialog dialog;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.onboarding_screen3, container, false);

        //buidNewGoogleApiClient();
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/avenir-next-regular.ttf");

        text = (TextView) rootView.findViewById(R.id.inviteTitle);
        text.setTypeface(custom_font);
        mtxtinvited = (EditText) rootView.findViewById(R.id.invitedlist);
        mtxtinvited.setTypeface(custom_font);
        inviteFriends = (Button) rootView.findViewById(R.id.invitepeople);
        inviteFriends.setTypeface(custom_font);
        inviteFriends.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String list = "";
                        for (int i = 0; i < invitedContacts.size(); i++) {
                            list = list + invitedContacts.get(i) + ", \n";
                        }
                        mtxtinvited.setText(list);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        invitedContacts.clear();
                        dialog.dismiss();
                        // Do stuff when user neglects.
                    }
                });
        builder.setTitle("Select From Contacts");

        ListView modeList = new ListView(getContext());
        //modeList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        //modeList.setItemsCanFocus(false);
        // String[] stringArray = new String[] { "Bright Mode", "Normal Mode" };


        Cursor c = LandingActivity.db.getContacts();

        while (c.moveToNext()) {
//            myContacts.add(c.getString(c.getColumnIndex("EmailAdd"))); //this adds an element to the list.
        }

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, myContacts);
        modeList.setAdapter(modeAdapter);

        builder.setView(modeList);

        dialog = builder.create();


        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                //Toast.makeText(getContext(), ((TextView) view).getText(),
                //        Toast.LENGTH_SHORT).show();

                int flag=0;
                for(int i=0 ; i<invitedContacts.size() ; i++){
                    if(invitedContacts.get(i).equals(((TextView) view).getText().toString()))
                        flag=1;
                }
                if(flag==0){
                    invitedContacts.add(((TextView) view).getText().toString());
                }
            }
        });


        return rootView;


    }


    //region Thread


    //endregion


    //region Not Working

    private void buidNewGoogleApiClient() {

        google_api_client = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Plus.PeopleApi.loadVisible(google_api_client, null).setResultCallback(this);
//        Toast.makeText(getContext(), "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        google_api_client.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
//        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
        if (!result.hasResolution()) {
            google_api_availability.getErrorDialog(getActivity(), result.getErrorCode(), request_code).show();
            return;
        }

        if (!is_intent_inprogress) {

            connection_result = result;

            if (is_signInBtn_clicked) {

                resolveSignInError();
            }
        }

    }


    private void resolveSignInError() {
//        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        if (connection_result.hasResolution()) {
            try {
                is_intent_inprogress = true;
                connection_result.startResolutionForResult(getActivity(), SIGN_IN_CODE);
                Log.d("resolve error", "sign in error resolved");
            } catch (IntentSender.SendIntentException e) {
                is_intent_inprogress = false;
                google_api_client.connect();
            }
        }
    }

    @Override
    public void onResult(People.LoadPeopleResult peopleData) {

//        Toast.makeText(getContext(), "" + peopleData.getStatus(), Toast.LENGTH_SHORT).show();
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            ArrayList<String> list = new ArrayList<String>();
            ArrayList<String> img_list = new ArrayList<String>();
            try {
                int count = personBuffer.getCount();

                for (int i = 0; i < count; i++) {
                    list.add(personBuffer.get(i).getDisplayName());
                    img_list.add(personBuffer.get(i).getImage().getUrl());
                }
                Intent intent = new Intent(getActivity(), FriendActivity.class);
                intent.putStringArrayListExtra("friendsName", list);
                intent.putStringArrayListExtra("friendsPic", img_list);
                startActivity(intent);
            } finally {
                personBuffer.release();
            }
        } else {
            Log.e("circle error", "Error requesting visible circles: " + peopleData.getStatus());
        }
    }


   /*
    Perform background operation asynchronously, to load user profile picture with new dimensions from the modified url
    */

    private class LoadProfilePic extends AsyncTask<String, Void, Bitmap> {
        ImageView bitmap_img;

        public LoadProfilePic(ImageView bitmap_img) {
            this.bitmap_img = bitmap_img;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap new_icon = null;
            try {
                InputStream in_stream = new java.net.URL(url).openStream();
                new_icon = BitmapFactory.decodeStream(in_stream);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return new_icon;
        }

        protected void onPostExecute(Bitmap result_img) {

            bitmap_img.setImageBitmap(result_img);
        }
    }

    //endregion

    @Override
    public void onClick(View view) {
        if (view == inviteFriends) {

            dialog.show();
//            Toast.makeText(getActivity(), "G+ Friend List", Toast.LENGTH_LONG).show();
//            Plus.PeopleApi.loadVisible(google_api_client, null)
//                    .setResultCallback(this);
//            Toast.makeText(getActivity(), "G+ Friend List", Toast.LENGTH_LONG).show();

//            Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
//                    .setMessage("Join now")
//                    //.setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
//                    //.setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
//                    .setCallToActionText("Join")
//                    .build();
//            startActivityForResult(intent, REQUEST_INVITE);

        }

    }


}
