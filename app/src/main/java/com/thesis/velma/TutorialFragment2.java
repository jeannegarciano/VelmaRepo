package com.thesis.velma;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by admin on 8/16/2016.
 */
public class TutorialFragment2 extends Fragment {

    View rootView;
    int PLACE_PICKER_REQUEST;
    TextView text;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//        Intent intent;
//
//        try {
//            intent = builder.build(getActivity());
//            startActivityForResult(intent, PLACE_PICKER_REQUEST);
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }

        rootView = inflater.inflate(R.layout.tutorial_screen2, container, false);

        text = (TextView)rootView.findViewById(R.id.welcometxt);

//        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/avenir-next-regular.ttf");

//        text.setTypeface(custom_font);

        return rootView;


    }
}

