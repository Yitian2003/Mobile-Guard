package com.witlife.mobileguard.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.witlife.mobileguard.common.Contant;

/**
 * Created by bruce on 21/09/2017.
 */

public class SPUtils {


    private static SharedPreferences sp;

    public static void putBoolean(Context context, String key, boolean value){
        sp = context.getSharedPreferences(Contant.CONFIGURE, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue){
        sp = context.getSharedPreferences(Contant.CONFIGURE, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void putString(Context context, String key, String value){
        sp = context.getSharedPreferences(Contant.CONFIGURE, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue){
        sp = context.getSharedPreferences(Contant.CONFIGURE, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void remove(Context context, String key){
        sp = context.getSharedPreferences(Contant.CONFIGURE, Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }
}
