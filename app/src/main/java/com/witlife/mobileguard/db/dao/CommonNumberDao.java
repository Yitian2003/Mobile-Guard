package com.witlife.mobileguard.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce on 14/11/2017.
 */

public class CommonNumberDao {

    public static List<GroupInfo> getCommonNumbers(Context context){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getFilesDir().getAbsolutePath() + "/commonnum.db",
                null, SQLiteDatabase.OPEN_READONLY);

        Cursor cursor = db.query("classlist", new String[]{"name", "idx"}, null, null, null, null, null);

        List<GroupInfo> infoList = new ArrayList<>();

        if(cursor != null){
            while (cursor.moveToNext()){

                GroupInfo info = new GroupInfo();

                info.name = cursor.getString(0);
                info.idx = cursor.getString(1);
                info.childInfoList = getChildrenList(info.idx, db);

                infoList.add(info);
            }
            cursor.close();
        }
        db.close();

        return infoList;
    }

    private static List<ChildInfo> getChildrenList(String idx, SQLiteDatabase db){
        Cursor cursor = db.query("table" + idx, new String[]{"number", "name"}, null, null, null, null, null);

        List<ChildInfo> list = new ArrayList<>();

        if (cursor != null){
            while (cursor.moveToNext()){
                ChildInfo info = new ChildInfo();
                info.name = cursor.getString(1);
                info.number = cursor.getString(0);

                list.add(info);
            }
            cursor.close();
        }
        return list;
    }

    public static class GroupInfo {
        public String name;
        public String idx;

        public List<ChildInfo> childInfoList;
    }

    public static class ChildInfo {
        public String name;
        public String number;
    }
}
