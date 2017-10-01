package com.witlife.mobileguard.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.common.Contant;
import com.witlife.mobileguard.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetupWizard5Activity extends SetupWizardBaseActivty {

    @BindView(R.id.cb_protect)
    CheckBox cbProtect;

    @Override
    public void goNextPage() {
        // save to sp - enable protect or not
        SPUtils.putBoolean(this, Contant.ENABLE_PROTECT, cbProtect.isChecked());
        // save to sp - first setup finish.
        SPUtils.putBoolean(this, Contant.FIRST_ENTER_SEC_SETUP, false);

        finish();
        removeCurrentActivity();
        goToNewActivity(SecurityActivity.class, null);
    }

    @Override
    public void goPrePage() {
        SPUtils.putBoolean(this, Contant.ENABLE_PROTECT, cbProtect.isChecked());

        finish();
        removeCurrentActivity();
        goToNewActivity(SetupWizard4Activity.class, null);

        overridePendingTransition(R.anim.anim_pre_enter, R.anim.anim_pre_exit);
    }

    @Override
    protected void initSetupPage() {
        boolean isEableProtect = SPUtils.getBoolean(this, Contant.ENABLE_PROTECT, false);
        cbProtect.setChecked(isEableProtect);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setup_wizard5;
    }

}
