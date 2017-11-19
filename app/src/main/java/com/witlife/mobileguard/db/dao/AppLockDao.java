package com.witlife.mobileguard.db.dao;

import android.content.Context;

import com.witlife.mobileguard.db.AppLockOpenHelper;

/**
 * Created by bruce on 19/11/2017.
 */

public class AppLockDao {

    private final AppLockOpenHelper helper;
    private static AppLockDao appLockDao;

    private AppLockDao(Context context){
        helper = new AppLockOpenHelper(context);
    }

    public static AppLockDao getInstance(Context context){
        if(appLockDao == null){
            synchronized (AppLockDao.class){
                if(appLockDao == null){
                    appLockDao = new AppLockDao(context);
                }
            }
            appLockDao = new AppLockDao(context);
        }
        return appLockDao;
    }
}
