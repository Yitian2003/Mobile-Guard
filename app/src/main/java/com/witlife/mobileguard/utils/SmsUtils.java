package com.witlife.mobileguard.utils;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.Telephony;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce on 14/11/2017.
 */

public class SmsUtils {

    public static void smsBackup(Context context, File file, OnSmsCallback callback) throws Exception {

        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms"),
                new String[]{"address", "date", "read", "type", "body"},
                null, null, null);

        int totalCount = cursor.getCount();
        //dialog.setMax(totalCount);
        callback.onGetTotalCount(totalCount);

        List<SmsInfo> list = new ArrayList<>();

        if (cursor != null) {

            int progress = 0;
            while (cursor.moveToNext()) {
                SmsInfo info = new SmsInfo();

                info.address = cursor.getString((cursor.getColumnIndex("address")));
                info.date = cursor.getString((cursor.getColumnIndex("date")));
                info.read = cursor.getString((cursor.getColumnIndex("read")));
                info.type = cursor.getString((cursor.getColumnIndex("type")));
                info.body = cursor.getString((cursor.getColumnIndex("body")));

                list.add(info);

                progress++;
                //int percent = progress * 100 /totalCount;
                //dialog.setProgress(progress);
                callback.onProgress(progress);

                SystemClock.sleep(100);
            }
            cursor.close();
        }

        Gson mGson = new Gson();
        String json = mGson.toJson(list);

        //Log.d("Json: ", json);
        FileOutputStream out = new FileOutputStream(file);

        out.write(json.getBytes());
        out.flush();
        out.close();
    }

    public static class SmsInfo {
        public String address;
        public String date;
        public String read;
        public String type;
        public String body;
    }

    public interface OnSmsCallback {
        void onGetTotalCount(int totalCount);

        void onProgress(int progress);
    }

    public static void smsRestore(Context context, File file, OnSmsCallback callback) throws Exception {
        Gson gson = new Gson();

        Type type = new TypeToken<List<SmsInfo>>() {
        }.getType();
        List<SmsInfo> list = gson.fromJson(new FileReader(file), type);

        callback.onGetTotalCount(list.size());

        ContentResolver resolver = context.getContentResolver();

        int progress = 0;
        for (SmsInfo info : list) {

            Cursor cursor = resolver.query(Uri.parse("content://sms"), null,
                    "address=? and date=? and body=? and type=? and read=?",
                    new String[]{info.address, info.date, info.body, info.type, info.read}, null);

            if(cursor != null){
                if(cursor.moveToNext()){
                    continue;
                }
                cursor.close();
            }

            ContentValues values = new ContentValues();
            values.put("address", info.address);
            values.put("date", info.date);
            values.put("body", info.body);
            values.put("type", info.type);
            values.put("read", info.read);

            resolver.insert(Uri.parse("content://sms/inbox"), values);

            progress++;
            callback.onProgress(progress);

            SystemClock.sleep(100);
        }
    }
}
