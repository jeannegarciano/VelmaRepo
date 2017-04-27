package com.thesis.velma;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by admin on 8/16/2016.
 */
public class TutorialFragment3 extends Fragment {

    View rootView;
    int PLACE_PICKER_REQUEST;

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

        rootView = inflater.inflate(R.layout.tutorial_screen3, container, false);



        return rootView;


    }
}

