package com.witlife.mobileguard.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.common.Contant;
import com.witlife.mobileguard.utils.SPUtils;

/**
 * Created by bruce on 28/10/2017.
 */

public class AddressToast {

    private final WindowManager manager;
    private final View mView;
    private final WindowManager.LayoutParams params;
    private final TextView tvAddress;
    private float startY;
    private float startX;

    private Context context;

    public AddressToast(Context context) {

        this.context = context;

        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        //params.windowAnimations = com.android.internal.R.style.Animation_Toast;
        //params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
               // | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        mView = View.inflate(context, R.layout.toast_address, null);
        tvAddress = mView.findViewById(R.id.tv_address);

        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_MOVE:
                        float endX = event.getRawX();
                        float endY = event.getRawY();

                        float dx = endX - startX;
                        float dy = endY - startY;

                        params.x += (int)dx;
                        params.y += (int)dy;

                        manager.updateViewLayout(mView, params);

                        startX = endX;
                        startY = endY;

                        break;
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getRawX();
                        startY = event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }

                return false;
            }
        });
    }

    public void show(String text){
        tvAddress.setText(text);

        int icon = SPUtils.getInt(context, Contant.ADDRESS_STYLE, R.drawable.shape_address_normal);

        tvAddress.setBackgroundResource(icon);

        manager.addView(mView, params);
    }

    public void hide(){
        if(mView != null && manager != null){
            try {
                manager.removeView(mView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
