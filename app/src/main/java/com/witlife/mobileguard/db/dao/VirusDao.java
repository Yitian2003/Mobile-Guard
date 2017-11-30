package com.witlife.mobileguard.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by bruce on 30/11/2017.
 */

public class VirusDao {

    public static boolean isVirus(Context context, String md5){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getFilesDir().getAbsolutePath()
        + "/antivirus.db", null, SQLiteDatabase.OPEN_READONLY);

        Cursor cursor = db.query("datable", null, "md5=?", new String[]{md5}, null, null, null);

        boolean isVirus = false;

        if(cursor != null){
            if(cursor.moveToNext()){
                isVirus = true;
            }
            cursor.close();
        }
        db.close();
        return isVirus;
    }
}
