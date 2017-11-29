package com.witlife.mobileguard.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.view.accessibility.AccessibilityEvent;

import com.witlife.mobileguard.activity.EnterPasswordActivity;
import com.witlife.mobileguard.common.Contant;
import com.witlife.mobileguard.db.dao.AppLockDao;

/**
 * app lock - accessibility service
 */

public class ApplockService extends android.accessibilityservice.AccessibilityService {

    private ApplockReceiver receiver;
    private AppLockDao dao;

    private String skipPackage;

    @Override
    public void onCreate() {
        super.onCreate();

        dao = AppLockDao.getInstance(this);

        receiver = new ApplockReceiver();
        IntentFilter intentFilter = new IntentFilter(Contant.ACTION_SKIP_PACKAGE);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            String packageName = event.getPackageName().toString();

            if(dao.find(packageName) && !packageName.equals(skipPackage)){

                // call startActivity from outside of an activity context requires the Flag_activity_new_task flag
                Intent intent = new Intent(this, EnterPasswordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("package", packageName);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        receiver = null;
    }

    class ApplockReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            skipPackage = intent.getStringExtra("package");
        }
    }
}
