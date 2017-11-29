package com.witlife.mobileguard.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.witlife.mobileguard.engine.ProcessInfoProvider;

public class AutoKillService extends Service {

    private AutoKillReceiver receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        receiver = new AutoKillReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
        receiver = null;
    }

    class AutoKillReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            ProcessInfoProvider.killAllProcesses(context);
        }
    }
}
