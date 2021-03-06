package com.witlife.mobileguard.receiver;

import android.animation.ObjectAnimator;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.service.LocationService;

public class SmsReceiver extends BroadcastReceiver {

    private DevicePolicyManager deviceManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        deviceManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(context, AdminReceiver.class);

        Object[] objs = (Object[]) intent.getExtras().get("pdus");

        for(Object obj : objs){
            SmsMessage message = SmsMessage.createFromPdu((byte[
                    ]) obj);
            String originatingAddress = message.getOriginatingAddress();// incoming number
            String messageBody = message.getDisplayMessageBody(); // message content

            if("#location#".equals(messageBody)){
                context.startService(new Intent(context, LocationService.class));

                abortBroadcast();
            } else if("#alarm#".equals(messageBody)){
                MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                player.setVolume(1, 1);
                player.setLooping(true);
                player.start();

                abortBroadcast();
            } else if(("#wipedata#").equals(messageBody)){
                // wipe data
                if(deviceManager.isAdminActive(componentName)){
                    deviceManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
                }
                abortBroadcast();
            } else if(("#lockscreen#").equals(messageBody)){
                // remotely lock screen
                if(deviceManager.isAdminActive(componentName)){
                    deviceManager.lockNow();
                }

                abortBroadcast();
            }
        }
    }
}
