package com.witlife.mobileguard.bean;

import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bruce on 19/09/2017.
 */

public class Model {

    private Context context;
    private static Model model;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    private Model(){

    }

    public static Model getInstance(){
        if(model == null){
            model = new Model();
        }
        return model;
    }

    public void init(Context context){
        this.context = context;

    }

    public ExecutorService getGlobalThreadPool(){
        return executorService;
    }
}
