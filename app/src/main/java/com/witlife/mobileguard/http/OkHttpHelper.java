package com.witlife.mobileguard.http;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by bruce on 19/09/2017.
 */

public class OkHttpHelper {

    private static OkHttpHelper helper = new OkHttpHelper();
    private OkHttpClient client;

    private Handler handler;

    private OkHttpHelper(){
        client = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpHelper getInstance(){
        return helper;
    }

    public void get(String url, Callback callback){
        Request request = buildRequest(url, HttpMethodType.GET, null);
        doRequest(request, callback);
    }

    public void post(String url,Map<String, String> param, Callback callback){
        Request request = buildRequest(url, HttpMethodType.POST, param);
        doRequest(request, callback);
    }

    private Request buildRequest(String url, HttpMethodType methodType, Map<String, String> params) {
        Request.Builder builder = new Request.Builder()
                .url(url);
        if(methodType == HttpMethodType.GET){
            builder.get();
        } else {
            FormBody.Builder formBody = new FormBody.Builder();

            if (params != null){
                for (Map.Entry<String, String> entry : params.entrySet()){
                    formBody.add(entry.getKey(), entry.getValue());
                }
            }

            builder.post(formBody.build());
        }
        return builder.build();
    }

    public void doRequest(final Request request, final Callback callback){

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackFailure(callback, call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callbackSuccess(callback, call, response);
            }
        });
    }

    private void callbackSuccess(final Callback callback, final Call call, final Response response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.onResponse(call, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void callbackFailure(final Callback callback, final Call call, final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(call, e);
            }
        });
    }

    enum HttpMethodType{
        GET,
        POST
    }
}
