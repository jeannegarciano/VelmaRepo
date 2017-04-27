package com.thesis.velma.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.thesis.velma.R;
import com.thesis.velma.helper.LocationList;

import java.util.ArrayList;

/**
 * Created by admin on 04/01/2017.
 */

public class LocationAdapter extends ArrayAdapter<LocationList> {


    private ArrayList<LocationList> locationLists;
    Context mcontext;

    public LocationAdapter(Context context, int textViewResourceId,
                           ArrayList<LocationList> locationLists) {
        super(context, textViewResourceId, locationLists);

        this.locationLists = locationLists;
        this.mcontext = context;

    }

    private static class ViewHolder {

        private TextView placename;
        private TextView placeaddress;

        public ViewHolder(View v) {
            placename = (TextView) v.findViewById(R.id.txtplace);
            placeaddress = (TextView) v.findViewById(R.id.txtaddress);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.list_location, parent,
                    false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        LocationList mylist = getItem(position);

        holder.placename.setText(mylist.getPlacename());
        holder.placeaddress.setText(mylist.getPlaceaddress());

        return convertView;
    }

}
