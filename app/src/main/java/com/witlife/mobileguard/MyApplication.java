package com.witlife.mobileguard;

import android.app.Application;

import com.witlife.mobileguard.bean.Model;

/**
 * Created by bruce on 19/09/2017.
 */

public class MyApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();

        Model.getInstance().init(this);
    }
}
