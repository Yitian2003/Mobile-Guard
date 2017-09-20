package com.witlife.mobileguard.common;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by bruce on 20/09/2017.
 */

public class ActivityManager {

    private static ActivityManager activityManager = new ActivityManager();

    private ActivityManager() {
    }

    public static ActivityManager getInstance(){
        return activityManager;
    }

    private Stack<Activity> activityStack = new Stack<>();

    public void add(Activity activity){
        if (activity != null){
            activityStack.add(activity);
        }
    }

    public void remove(Activity activity){
        if (activity != null){
            for (int i = activityStack.size() -1; i >= 0; i--){
                Activity currentActivity = activityStack.get(i);
                if (currentActivity.getClass().equals(activity.getClass())){
                    currentActivity.finish();;
                    activityStack.remove(i);
                }
            }
        }
    }

    public void removeCurrent(){
        Activity activity = activityStack.lastElement();
        activity.finish();
        activityStack.remove(activity);
    }

    public void removeAll(){
        for (int i = activityStack.size() -  1; i >= 0; i--){
            Activity activity = activityStack.get(i);
            activity.finish();
            activityStack.remove(activity);
        }
    }
}
