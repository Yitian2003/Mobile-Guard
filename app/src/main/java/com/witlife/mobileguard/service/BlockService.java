package com.witlife.mobileguard.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.witlife.mobileguard.db.dao.BlackNumberDao;

public class BlockService extends Service {

    private InnerSmsReceiver receiver;
    private BlackNumberDao dao;
    private TelephonyManager service;
    private BlackNumberListener listener;

    public BlockService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        dao = BlackNumberDao.getInstance(getApplicationContext());

        // block sms
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        receiver = new InnerSmsReceiver();
        registerReceiver(receiver, filter);

        // block phone
        service = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new BlackNumberListener();
        service.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        receiver = null;
    }

    class BlackNumberListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    class InnerSmsReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objs = (Object[]) intent.getExtras().get("pdus");

            for(Object obj : objs){
                SmsMessage message = SmsMessage.createFromPdu((byte[]) obj);
                String originatingAddress = message.getOriginatingAddress();
                String messageBody = message.getMessageBody();

                if(dao.find(originatingAddress)){
                    int mode = dao.findMode(originatingAddress);

                    if(mode >0){
                        abortBroadcast();
                    }
                }
            }


        }
    }
}
