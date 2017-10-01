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
import com.witlife.mobileguard.common.Contant;
import com.witlife.mobileguard.utils.SPUtils;

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

                SPUtils.putBoolean(SettingActivity.this, Contant.AUTO_UPDATE, btnAutoUpdate.isChecked());
            }
        });

        rlAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAddress.setChecked(!btnAddress.isChecked());
                btnAddress.setBackgroundResource(btnAddress.isChecked() ? R.drawable.on : R.drawable.off);

                SPUtils.putBoolean(SettingActivity.this, Contant.ADDRESS, btnAddress.isChecked());
            }
        });

        rlBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnBlock.setChecked(!btnBlock.isChecked());
                btnBlock.setBackgroundResource(btnBlock.isChecked() ? R.drawable.on : R.drawable.off);

                SPUtils.putBoolean(SettingActivity.this, Contant.BLOCK, btnBlock.isChecked());
            }
        });
    }

    private void initToggle() {
        btnAutoUpdate.setChecked(SPUtils.getBoolean(this, Contant.AUTO_UPDATE, true));
        btnAutoUpdate.setBackgroundResource(btnAutoUpdate.isChecked() ? R.drawable.on : R.drawable.off);
        btnAddress.setChecked(SPUtils.getBoolean(this, Contant.ADDRESS, false));
        btnAddress.setBackgroundResource(btnAddress.isChecked() ? R.drawable.on : R.drawable.off);
        btnBlock.setChecked(SPUtils.getBoolean(this, Contant.BLOCK, false));
        btnBlock.setBackgroundResource(btnBlock.isChecked() ? R.drawable.on : R.drawable.off);
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
