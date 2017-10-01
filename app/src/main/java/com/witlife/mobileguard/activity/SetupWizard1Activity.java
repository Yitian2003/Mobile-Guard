package com.witlife.mobileguard.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.witlife.mobileguard.R;

public class SetupWizard1Activity extends SetupWizardBaseActivty {


    @Override
    public void goNextPage() {
        goToNewActivity(SetupWizard2Activity.class, null);
        finish();
        removeCurrentActivity();

        overridePendingTransition(R.anim.anim_next_enter, R.anim.anim_next_exit);
    }

    @Override
    public void goPrePage() {

    }

    @Override
    protected void initSetupPage() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setup_wizard1;
    }

}
