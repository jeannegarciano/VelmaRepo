package com.thesis.velma;

/**
 * Created by admin on 7/20/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;

public class Countdown extends CountDownTimer {

    private Activity _act;
    private Class _cls;
    public Countdown(long millisInFuture, long countDownInterval, Activity act, Class cls) {
        super(millisInFuture, countDownInterval);
        _act=act;
        _cls=cls;
    }
    @Override
    public void onFinish() {
        _act.startActivity(new Intent(_act,_cls));
        _act.finish();
    }
    @Override
    public void onTick(long millisUntilFinished) {
    }
}
