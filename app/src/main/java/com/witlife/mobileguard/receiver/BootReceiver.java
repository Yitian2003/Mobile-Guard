package com.witlife.mobileguard.receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.witlife.mobileguard.common.Contant;
import com.witlife.mobileguard.utils.SPUtils;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String savedSim = SPUtils.getString(context, Contant.SIM_SERIAL_NUMBER, null);
        boolean isEableProtect = SPUtils.getBoolean(context, Contant.ENABLE_PROTECT, false);

        if(!TextUtils.isEmpty(savedSim) && isEableProtect){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String currentSim = tm.getSimSerialNumber()+1;

            if (!savedSim.equals(currentSim)){
                String number = SPUtils.getString(context, Contant.RELEVANT_MOBILE, "");

                int permission = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS);
                if (permission != PackageManager.PERMISSION_GRANTED){

                } else {
                    Log.d("BootReceiver", "sim card change");
                    SmsManager sm = SmsManager.getDefault();
                    sm.sendTextMessage(number, null, "SIM card changed!", null, null);
                }

            }
        }
    }
}
