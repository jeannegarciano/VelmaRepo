package com.thesis.velma;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by admin on 3/6/2017.
 */

public class ConflictEventsAdapter extends ArrayAdapter<ConflictEvents> {

    private Context mContext;
    private List<ConflictEvents> mData;


//
//    public ConflictEventsAdapter(Context context, int resource) {
//        super(context, resource);
//    }

    public ConflictEventsAdapter(@NonNull Context context, List<ConflictEvents> data) {
        super(context, R.layout.list_item, data);
        mContext = context;
        mData = data;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Nullable
    @Override
    public ConflictEvents getItem(int position) {
        return mData.get(position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (holder != null) {
            ConflictEvents events = getItem(position);
            String eventName;

            if (events != null) {
                holder.tvTime.setText(events.getmTime());
                eventName = events.getmEventName();


                if (!TextUtils.isEmpty(eventName)) {
                    holder.tvEventName.setText(events.getmEventName());



                } else {
                    holder.tvEventName.setText(" ");

                }
            }

        }
        return convertView;
    }

    private static class ViewHolder {
        TextView tvTime;
        TextView tvEventName;

        public ViewHolder(View convertView) {
            tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            tvEventName = (TextView) convertView.findViewById(R.id.tvEventName);
        }
    }

}