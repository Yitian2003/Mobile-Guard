package com.witlife.mobileguard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.witlife.mobileguard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdvancedToolsActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_other)
    ImageView ivOther;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    @BindView(R.id.rl_phone)
    RelativeLayout rlPhone;
    @BindView(R.id.rl_backup_sms)
    RelativeLayout rlBackupSms;
    @BindView(R.id.rl_restore_sms)
    RelativeLayout rlRestoreSms;
    @BindView(R.id.rl_locker_mager)
    RelativeLayout rlLockerMager;
    @BindView(R.id.btn_locker)
    ToggleButton btnLocker;
    @BindView(R.id.rl_turn_on)
    RelativeLayout rlTurnOn;

    @Override
    protected void initData() {
        rlAddress.setOnClickListener(this);
    }

    @Override
    protected void initTitle() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("Advanced Tools");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                removeCurrentActivity();
                onBackPressed();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_advanced_tools;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_address:

                startActivity(new Intent(this, SearchAddressActivity.class));
                break;
        }
    }
}
