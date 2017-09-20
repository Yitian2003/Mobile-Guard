package com.witlife.mobileguard.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.witlife.mobileguard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_other)
    ImageView ivOther;
    @BindView(R.id.rl_auto)
    RelativeLayout rlAuto;
    @BindView(R.id.rl_block)
    RelativeLayout rlBlock;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    @BindView(R.id.rl_address_style)
    RelativeLayout rlAddressStyle;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.btn_auto_update)
    ToggleButton btnAutoUpdate;
    @BindView(R.id.btn_block)
    ToggleButton btnBlock;
    @BindView(R.id.btn_address)
    ToggleButton btnAddress;

    private SharedPreferences sp;

    @Override
    protected void initData() {

        initToggle();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCurrentActivity();
                goToNewActivity(MainActivity.class, null);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCurrentActivity();
                goToNewActivity(MainActivity.class, null);
            }
        });

        rlAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAutoUpdate.setChecked(!btnAutoUpdate.isChecked());
                btnAutoUpdate.setBackgroundResource(btnAutoUpdate.isChecked() ? R.drawable.on : R.drawable.off);


                sp.edit().putBoolean("auto_update", btnAutoUpdate.isChecked()).commit();
            }
        });

        rlAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAddress.setChecked(!btnAddress.isChecked());
                btnAddress.setBackgroundResource(btnAddress.isChecked() ? R.drawable.on : R.drawable.off);


                sp.edit().putBoolean("address", btnAddress.isChecked()).commit();
            }
        });

        rlBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBlock.setChecked(!btnBlock.isChecked());
                btnBlock.setBackgroundResource(btnBlock.isChecked() ? R.drawable.on : R.drawable.off);


                sp.edit().putBoolean("block", btnBlock.isChecked()).commit();
            }
        });
    }

    private void initToggle() {
        sp = getSharedPreferences("configure", Context.MODE_PRIVATE);
        boolean isOn = sp.getBoolean("auto_update", false);
        btnAutoUpdate.setChecked(isOn);
        btnAutoUpdate.setBackgroundResource(isOn ? R.drawable.on : R.drawable.off);
        isOn = sp.getBoolean("address", false);
        btnAddress.setChecked(isOn);
        btnAddress.setBackgroundResource(isOn ? R.drawable.on : R.drawable.off);
        isOn = sp.getBoolean("block", false);
        btnBlock.setChecked(isOn);
        btnBlock.setBackgroundResource(isOn ? R.drawable.on : R.drawable.off);
    }

    @Override
    protected void initTitle() {
        ivBack.setVisibility(View.VISIBLE);
        ivOther.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("Setting");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

}
