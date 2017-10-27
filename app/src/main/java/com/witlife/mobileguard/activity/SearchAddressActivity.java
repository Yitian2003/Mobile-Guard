package com.witlife.mobileguard.activity;

import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.db.dao.AddressDao;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAddressActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_other)
    ImageView ivOther;
    @BindView(R.id.input)
    EditText input;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.tv_result)
    TextView tvResult;


    @Override
    protected void initData() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = input.getText().toString().trim();
                if (TextUtils.isEmpty(number)){
                    Animation shake = AnimationUtils.loadAnimation(SearchAddressActivity.this, R.anim.shake);
                    input.startAnimation(shake);

                    vibrate();

                    Toast.makeText(SearchAddressActivity.this, "Please Input Phone Number", Toast.LENGTH_SHORT).show();
                } else {
                    String address = AddressDao.getAddress(SearchAddressActivity.this, number);
                    tvResult.setText(address);
                }
            }
        });

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String address = AddressDao.getAddress(SearchAddressActivity.this, editable.toString().trim());
                tvResult.setText(address);
            }
        });
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
    }

    @Override
    protected void initTitle() {
        tvTitle.setText("Search Title");
        ivBack.setVisibility(View.VISIBLE);

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
        return R.layout.activity_search_address;
    }
}
