package com.witlife.mobileguard.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.witlife.mobileguard.bean.BlockListBean;
import com.witlife.mobileguard.db.BlackNumberOpenHelper;

import java.util.ArrayList;

/**
 * Created by bruce on 30/10/2017.
 */

public class BlackNumberDao {

    private final BlackNumberOpenHelper helper;
    private static BlackNumberDao blackNumberDao;


    private BlackNumberDao(Context context) {
        helper = new BlackNumberOpenHelper(context);
    }

    public static BlackNumberDao getInstance(Context context){
        if(blackNumberDao == null){
            synchronized (BlackNumberDao.class){
                if (blackNumberDao == null){
                    blackNumberDao = new BlackNumberDao(context);
                }

            }

        }
        return blackNumberDao;
    }

    public boolean add(String number, int mode){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("mode", mode);

        long insert = db.insert("blacknumber", null, values);
        db.close();

        return insert != -1;
    }
    public boolean delete(String number){

        SQLiteDatabase db = helper.getWritableDatabase();
        int delete = db.delete("blacknumber", "number=?", new String[]{number});

        db.close();
        return delete > 0;
    }
    public boolean update(String number, int mode){
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("mode", mode);

        int update = db.update("blacknumber", values, "number=?", new String[]{number});

        db.close();

        return update > 0;
    }
    public boolean find(String number){
        SQLiteDatabase db = helper.getReadableDatabase();
        boolean exist = false;

        Cursor cursor = db.query("blacknumber", new String[]{"number", "mode"}, "number=?", new String[]{number},
                null, null, null);

        if(cursor != null){

            if (cursor.moveToNext()){
                exist = true;
            }
            cursor.close();
        }
        db.close();
        return exist;
    }

    public int findMode(String number){
        SQLiteDatabase db = helper.getReadableDatabase();
        int mode = -1;

        Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "number=?", new String[]{number},
                null, null, null);

        if(cursor != null){

            if (cursor.moveToNext()){
                mode = cursor.getInt(0);
            }
            cursor.close();
        }
        db.close();
        return mode;
    }

    // query for all
    public ArrayList<BlockListBean> findAll(){
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<BlockListBean> list = new ArrayList<>();

        Cursor cursor = db.query("blacknumber", new String[]{"number", "mode"}, null, null,
                null, null, null);

        if(cursor != null){

            while(cursor.moveToNext()){
                String number = cursor.getString(0);
                int mode = cursor.getInt(1);

                BlockListBean bean = new BlockListBean();
                bean.setNumber(number);
                bean.setStatus(mode);

                list.add(bean);
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    // query part of list
    public ArrayList<BlockListBean> findPart(int index){
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<BlockListBean> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("select number, mode from blacknumber order by _id desc limit ?, 10", new String[]{index + ""});

        if(cursor != null){

            while(cursor.moveToNext()){
                String number = cursor.getString(0);
                int mode = cursor.getInt(1);

                BlockListBean bean = new BlockListBean();
                bean.setNumber(number);
                bean.setStatus(mode);

                list.add(bean);
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    public int getTotalCount(){
        SQLiteDatabase db = helper.getReadableDatabase();
        int count = 0;

        Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);

        if(cursor != null){
            if(cursor.moveToNext()){
                count = cursor.getInt(0);
            }
            cursor.close();
        }

        db.close();
        return count;
    }
}
