package com.witlife.mobileguard.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.receiver.AdminReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetupWizard4Activity extends SetupWizardBaseActivty {

    @BindView(R.id.iv_admin)
    ImageView ivAdmin;
    @BindView(R.id.rl_admin)
    RelativeLayout rlAdmin;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    private DevicePolicyManager systemService;
    private ComponentName componentName;

    @Override
    protected void initSetupPage() {
        systemService = (DevicePolicyManager) this.getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, AdminReceiver.class);

        rlAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (systemService.isAdminActive(componentName)) {
                    systemService.removeActiveAdmin(componentName);

                    ivAdmin.setImageResource(R.drawable.admin_inactivated);
                    btnNext.setVisibility(View.INVISIBLE);
                    tvNotice.setVisibility(View.VISIBLE);
                } else {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Activate super admin for Mobile Guard.");
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (systemService.isAdminActive(componentName)) {
            ivAdmin.setImageResource(R.drawable.admin_activated);
            btnNext.setVisibility(View.VISIBLE);
            tvNotice.setVisibility(View.GONE);
        } else {
            ivAdmin.setImageResource(R.drawable.admin_inactivated);
            btnNext.setVisibility(View.INVISIBLE);
            tvNotice.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void goNextPage() {
        if (!systemService.isAdminActive(componentName)) {
            btnNext.setVisibility(View.INVISIBLE);
            tvNotice.setVisibility(View.VISIBLE);
        } else {
            finish();
            removeCurrentActivity();
            goToNewActivity(SetupWizard5Activity.class, null);

            overridePendingTransition(R.anim.anim_next_enter, R.anim.anim_next_exit);
        }
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
