package com.witlife.mobileguard.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.witlife.mobileguard.R;

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

            }
        });
    }

    @Override
    protected void initTitle() {
        tvTitle.setText("Search Title");
        ivBack.setVisibility(View.VISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_address;
    }
}
