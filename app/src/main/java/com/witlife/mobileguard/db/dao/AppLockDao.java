package com.witlife.mobileguard.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.witlife.mobileguard.db.AppLockOpenHelper;

import java.util.ArrayList;
import java.util.List;

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

    public boolean add(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("package", name);

        long insert = db.insert("applock", null, values);

        db.close();

        return insert != -1;
    }

    public boolean delete(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        int delete = db.delete("applock", "package=?", new String[]{name});

        db.close();
        return delete > 0;
    }

    public boolean find(String name){
        SQLiteDatabase db = helper.getReadableDatabase();
        boolean exist = false;

        Cursor cursor = db.query("applock", new String[]{"package"}, "package=?", new String[]{name}, null, null, null);

        if (cursor != null){
            if(cursor.moveToNext()){
                exist = true;
            }
            cursor.close();
        }
        db.close();
        return exist;
    }

    public List<String> findAll(){
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query("applock", new String[]{"package"}, null, null, null, null, null);
        List<String> list = new ArrayList<>();

        if(cursor != null){
            while (cursor.moveToNext()){
                String name = cursor.getString(0);
                list.add(name);
            }
            cursor.close();
        }
        db.close();

        return list;
    }
}
