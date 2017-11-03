package com.witlife.mobileguard.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.witlife.mobileguard.db.dao.AddressDao;
import com.witlife.mobileguard.view.AddressToast;

public class AddressService extends Service {

    private TelephonyManager systemService;
    private AddressListener listener;
    private AddressReceiver receiver;
    private WindowManager manager;
    private TextView mView;
    private AddressToast addressToast;

    @Override
    public void onCreate() {
        super.onCreate();

        // income listener
        systemService = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new AddressListener();
        systemService.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        // outcome listener
        receiver = new AddressReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(receiver, filter);

        addressToast = new AddressToast(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        systemService.listen(listener, PhoneStateListener.LISTEN_NONE);
        listener = null;

        unregisterReceiver(receiver);
        receiver = null;
    }

    private class AddressListener extends PhoneStateListener{

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            switch (state){
                case TelephonyManager.CALL_STATE_RINGING:
                    String address = AddressDao.getAddress(getApplicationContext(), incomingNumber);
                    addressToast.show(address);

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    addressToast.hide();
                    break;
                default:
                    break;
            }
        }
    }

    class AddressReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            String number = getResultData();
            String address = AddressDao.getAddress(context, number);
            addressToast.show(address);
        }
    }
}
