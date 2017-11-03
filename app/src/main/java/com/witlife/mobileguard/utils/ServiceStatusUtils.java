package com.witlife.mobileguard.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;

import java.util.List;


/**
 * Created by bruce on 27/10/2017.
 */

public class ServiceStatusUtils {
    public static boolean isServiceRunning(Context context, Class<? extends Service> clazz){

        // PackageManager, TelephonyManager, DevicePolicyManager, Vibrator, SmsManager, LocationManger
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> services = manager.getRunningServices(Integer.MAX_VALUE);
        for(ActivityManager.RunningServiceInfo info : services){
            if(clazz.getName().equals(info.service.getClassName())){
                return true;
            }
        }
        return false;
    }
}
