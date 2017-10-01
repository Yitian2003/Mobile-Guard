package com.witlife.mobileguard.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.common.Contant;
import com.witlife.mobileguard.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SecurityActivity extends BaseActivity {

    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.iv_protect)
    ImageView ivProtect;
    @BindView(R.id.rl_protect)
    RelativeLayout rlProtect;
    @BindView(R.id.ll_reset)
    LinearLayout llReset;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_other)
    ImageView ivOther;

    @Override
    protected void initData() {
        String number = SPUtils.getString(this, Contant.RELEVANT_MOBILE, "");
        final boolean isProtect = SPUtils.getBoolean(this, Contant.ENABLE_PROTECT, true);

        tvNumber.setText(number);
        ivProtect.setImageResource(isProtect ? R.drawable.lock : R.drawable.unlock);

        llReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                removeCurrentActivity();
                goToNewActivity(SetupWizard1Activity.class, null);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                removeCurrentActivity();
                goToNewActivity(MainActivity.class, null);
            }
        });

        rlProtect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isProtect = SPUtils.getBoolean(getApplicationContext(), Contant.ENABLE_PROTECT, true);
                ivProtect.setImageResource(!isProtect ? R.drawable.lock : R.drawable.unlock);
                SPUtils.putBoolean(getApplicationContext(), Contant.ENABLE_PROTECT, !isProtect);
            }
        });
    }

    @Override
    protected void initTitle() {
        ivBack.setVisibility(View.VISIBLE);
        ivOther.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("Mobile Security");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_security;
    }

}
