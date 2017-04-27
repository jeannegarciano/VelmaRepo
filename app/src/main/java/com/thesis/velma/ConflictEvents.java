package com.thesis.velma;

/**
 * Created by admin on 3/6/2017.
 */

public class ConflictEvents {

    private String mTime;
    private String mEventName;

    public ConflictEvents(String mTime, String mEventName) {
        this.mTime = mTime;
        this.mEventName = mEventName;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmEventName() {
        return mEventName;
    }

    public void setmEventName(String mEventName) {
        this.mEventName = mEventName;
    }
}