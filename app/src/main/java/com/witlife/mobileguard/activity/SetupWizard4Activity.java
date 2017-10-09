package com.witlife.mobileguard.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.receiver.AdminReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetupWizard4Activity extends SetupWizardBaseActivty {

    @BindView(R.id.iv_admin)
    ImageView ivAdmin;
    @BindView(R.id.rl_admin)
    RelativeLayout rlAdmin;
    private DevicePolicyManager systemService;
    private ComponentName componentName;

    @Override
    protected void initSetupPage() {
        systemService = (DevicePolicyManager) this.getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, AdminReceiver.class);

        rlAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (systemService.isAdminActive(componentName)){
                    systemService.removeActiveAdmin(componentName);

                    ivAdmin.setImageResource(R.drawable.admin_inactivated);
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
        if(systemService.isAdminActive(componentName)){
            ivAdmin.setImageResource(R.drawable.admin_activated);
        } else {
            ivAdmin.setImageResource(R.drawable.admin_inactivated);
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
