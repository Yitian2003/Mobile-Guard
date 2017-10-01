package com.witlife.mobileguard.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.witlife.mobileguard.R;

public class SetupWizard4Activity extends SetupWizardBaseActivty {

    @Override
    protected void initSetupPage() {

    }

    @Override
    public void goNextPage() {
        finish();
        removeCurrentActivity();
        goToNewActivity(SetupWizard5Activity.class, null);

        overridePendingTransition(R.anim.anim_next_enter, R.anim.anim_next_exit);
    }

    @Override
    public void goPrePage() {
        finish();
        removeCurrentActivity();
        goToNewActivity(SetupWizard3Activity.class, null);

        overridePendingTransition(R.anim.anim_pre_enter, R.anim.anim_pre_exit);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setup_wizard4;
    }

}
