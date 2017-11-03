package com.witlife.mobileguard.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by bruce on 25/10/2017.
 */

public class AddressDao {

    public static String getAddress(Context context, String number){

        String address = "Unkown Number";
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getFilesDir().getAbsolutePath()
                + "/address.db", null, SQLiteDatabase.OPEN_READONLY);

        if(number.matches("^1[3-8]\\d{9}$")){
            String sql = "select location from data2 where id=(select outkey from data1 where id=?)";
            Cursor cursor = db.rawQuery(sql, new String[]{number.substring(0, 7)});

            if (cursor != null){
                if(cursor.moveToNext()){
                    address = cursor.getString(0);
                }
                cursor.close();
            }
        } else {
            switch (number.length()){
                case 3:
                    address = "Polic Number";
                    break;
                case 4:
                    address = "Emulator Number";
                    break;
                case 5:
                    address = "Supplier Customer Number";
                    break;
                case 7:
                case 8:
                    address = "Local Landline";
                    break;
                default:
                    if (number.startsWith("0") && number.length() >= 10 && number.length() <= 12){
                        Cursor cursor = db.rawQuery("select location from data2 where area=?", new String[]{
                                number.substring(1, 4)});

                        if(cursor != null && cursor.moveToNext()){
                            address = cursor.getString(0);
                        }
                        cursor.close();
                    }
                    if(address.equals("Unkwon Number")){
                        Cursor cursor = db.rawQuery("select location from data2 where area=?", new String[]{
                                number.substring(1, 5)});
                        if(cursor != null && cursor.moveToNext()){
                            address = cursor.getString(0);
                        }
                        cursor.close();
                    }
                    break;
            }
        }

        db.close();

        return address;
    }
}
