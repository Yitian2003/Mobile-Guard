package com.witlife.mobileguard.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.common.Contant;
import com.witlife.mobileguard.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetupWizard2Activity extends SetupWizardBaseActivty {

    private static final int REQUEST_READ_PHONE_STATE = 111;
    @BindView(R.id.iv_bind)
    ImageView ivBind;
    @BindView(R.id.rl_bind)
    RelativeLayout rlBind;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_notice)
    TextView tvNotice;

    private String savedSim;

    @Override
    public void goNextPage() {

        savedSim = SPUtils.getString(getApplicationContext(), Contant.SIM_SERIAL_NUMBER, null);

        if (!TextUtils.isEmpty(savedSim)) {

            finish();
            removeCurrentActivity();
            goToNewActivity(SetupWizard3Activity.class, null);

            overridePendingTransition(R.anim.anim_next_enter, R.anim.anim_next_exit);
        }
    }

    @Override
    public void goPrePage() {
        finish();
        removeCurrentActivity();
        goToNewActivity(SetupWizard1Activity.class, null);

        overridePendingTransition(R.anim.anim_pre_enter, R.anim.anim_pre_exit);
    }

    @Override
    protected void initSetupPage() {

        savedSim = SPUtils.getString(getApplicationContext(), Contant.SIM_SERIAL_NUMBER, null);
        if (!TextUtils.isEmpty(savedSim)) {
            ivBind.setImageResource(R.drawable.lock);
            btnNext.setVisibility(View.VISIBLE);
            tvNotice.setVisibility(View.INVISIBLE);
        } else {
            ivBind.setImageResource(R.drawable.unlock);
            btnNext.setVisibility(View.INVISIBLE);
            tvNotice.setVisibility(View.VISIBLE);
        }

        rlBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check unbind or bind
                savedSim = SPUtils.getString(getApplicationContext(), Contant.SIM_SERIAL_NUMBER, null);

                if (TextUtils.isEmpty(savedSim)) {

                    int permission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SetupWizard2Activity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                    } else {
                        // get sim card serial number
                        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                        String sim = tm.getSimSerialNumber();

                        SPUtils.putString(getApplicationContext(), Contant.SIM_SERIAL_NUMBER, sim);
                        ivBind.setImageResource(R.drawable.lock);
                        btnNext.setVisibility(View.VISIBLE);
                        tvNotice.setVisibility(View.INVISIBLE);
                    }
                } else {
                    SPUtils.remove(getApplicationContext(), Contant.SIM_SERIAL_NUMBER);
                    ivBind.setImageResource(R.drawable.unlock);
                    btnNext.setVisibility(View.INVISIBLE);
                    tvNotice.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setup_wizard2;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;
        }
    }

}
