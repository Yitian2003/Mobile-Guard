package com.witlife.mobileguard.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.common.Contant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EnterPasswordActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_other)
    ImageView ivOther;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    private String packageName;

    @Override
    protected void initData() {
        packageName = getIntent().getStringExtra("package");

        PackageManager packageManager = getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);

            Drawable icon = applicationInfo.loadIcon(packageManager);
            String name = applicationInfo.loadLabel(packageManager).toString();

            ivIcon.setImageDrawable(icon);
            tvName.setText(name);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initTitle() {
        tvTitle.setText("App Locker");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_enter_password;
    }

    @OnClick({R.id.btn_ok, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                String password = etPwd.getText().toString().trim();

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    if(password.equals("ddd"))
                    {
                        // self define broadcast
                        Intent intent = new Intent(Contant.ACTION_SKIP_PACKAGE);
                        intent.putExtra("package", packageName);
                        sendBroadcast(intent);

                        finish();
                    } else {
                        Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_cancel:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);

        finish();
    }
}
