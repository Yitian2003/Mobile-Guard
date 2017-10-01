package com.witlife.mobileguard.activity;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.witlife.mobileguard.R;

/**
 * Created by bruce on 22/09/2017.
 */

public abstract class SetupWizardBaseActivty extends BaseActivity{

    private GestureDetector detector;

    @Override
    protected void initData() {
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                if (Math.abs(velocityX) < 100){
                    return true;
                }

                if (Math.abs(e2.getY() - e1.getY()) > Math.abs(e2.getX() - e1.getX())){
                    return true;
                }

                if(e2.getX() - e1.getX() > 100){
                    goPrePage();
                }

                if(e1.getX() - e2.getX() > 100){
                    goNextPage();
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        initSetupPage();
    }

    protected abstract void initSetupPage();

    @Override
    protected void initTitle() {

    }

    public abstract void goNextPage();

    public abstract void goPrePage();

    public void nextPage(View view){
        goNextPage();
    }

    public void prePage(View view){
        goPrePage();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
