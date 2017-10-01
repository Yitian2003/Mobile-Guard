package com.witlife.mobileguard.receiver;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.witlife.mobileguard.R;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] objs = (Object[]) intent.getExtras().get("pdus");

        for(Object obj : objs){
            SmsMessage message = SmsMessage.createFromPdu((byte[
                    ]) obj);
            String originatingAddress = message.getOriginatingAddress();// incoming number
            String messageBody = message.getDisplayMessageBody(); // message content

            if("#location#".equals(messageBody)){
                abortBroadcast();
            } else if("#alarm#".equals(messageBody)){
                MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                player.setVolume(1, 1);
                player.setLooping(true);
                player.start();

                abortBroadcast();
            } else if(("#wipedata#").equals(messageBody)){
                abortBroadcast();
            } else if(("#lockscreen#").equals(messageBody)){
                abortBroadcast();
            }
        }
    }
}
