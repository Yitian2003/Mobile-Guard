package com.witlife.mobileguard.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.adpater.MainAdapter;
import com.witlife.mobileguard.bean.MainGridBean;
import com.witlife.mobileguard.common.Contant;
import com.witlife.mobileguard.utils.MD5Utils;
import com.witlife.mobileguard.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.gv_main)
    GridView gvMain;

    private MainAdapter adapter;
    private List<MainGridBean> mainGridList;

    @Override
    protected void initData() {
        startLogoAnimation();

        initGridView();

        setClickListener();
    }

    private void setClickListener() {
        gvMain.setOnItemClickListener(this);
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCurrentActivity();
                goToNewActivity(SettingActivity.class, null);

            }
        });
    }

    private void initGridView() {
        String[] titles = getResources().getStringArray(R.array.main_grid_title);
        TypedArray icons = getResources().obtainTypedArray(R.array.main_grid_icon);
        String[] des = getResources().getStringArray(R.array.main_grid_des);

        mainGridList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            MainGridBean mainGridItem = new MainGridBean();
            mainGridItem.setTitle(titles[i]);
            mainGridItem.setIcon(icons.getDrawable(i));
            mainGridItem.setDes(des[i]);

            mainGridList.add(mainGridItem);
        }

        adapter = new MainAdapter(this, mainGridList);
        gvMain.setAdapter(adapter);
    }

    private void startLogoAnimation() {
        //ivLogo.setRotationY(180.0f);
        ObjectAnimator animator = ObjectAnimator.ofFloat(ivLogo, "rotationY", 0, 360);
        animator.setDuration(2000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.start();
    }

    @Override
    protected void initTitle() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {
            case 0:// mobile security
                doSecurity();
                break;
            case 7:
                advancedTools();
        }
    }

    private void advancedTools() {
        startActivity(new Intent(this, AdvancedToolsActivity.class));
    }

    private void doSecurity() {

        String savedPwd = SPUtils.getString(this, Contant.PASSWORD, "");
        if (TextUtils.isEmpty(savedPwd)) {
            setupPassword();  // set up password if password is empty
        } else {
            enterPassword(); // input password to unlock
        }
    }

    private void enterPassword() {
        View view = View.inflate(this, R.layout.dialog_input_pwd, null);
        final EditText etPwd = view.findViewById(R.id.et_pwd);
        Button btnOk = view.findViewById(R.id.btn_ok);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd = SPUtils.getString(MainActivity.this, Contant.PASSWORD, "");
                String pwdInput = etPwd.getText().toString().trim();

                if (TextUtils.isEmpty(pwdInput)){
                    Toast.makeText(MainActivity.this, "Please input Password", Toast.LENGTH_SHORT).show();
                } else if (!pwd.equals(MD5Utils.MD5(pwdInput))) {
                    Toast.makeText(MainActivity.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    goToSecSetup();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void goToSecSetup() {
        boolean isFirstSetup = SPUtils.getBoolean(this, Contant.FIRST_ENTER_SEC_SETUP, true);

        if(isFirstSetup){
            goToNewActivity(SetupWizard1Activity.class, null);
        } else {
            goToNewActivity(SecurityActivity.class, null);
        }
    }

    private void setupPassword() {

        View view = View.inflate(this, R.layout.dialog_set_pwd, null);
        final EditText etPwd = view.findViewById(R.id.et_pwd);
        final EditText etPwdConfirm = view.findViewById(R.id.et_pwd_confirm);
        Button btnOk = view.findViewById(R.id.btn_ok);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd = etPwd.getText().toString();
                String pwd2 = etPwdConfirm.getText().toString();

                if(TextUtils.isEmpty(pwd.trim()) || TextUtils.isEmpty(pwd2.trim())){
                    Toast.makeText(MainActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                } else if (!pwd.equals(pwd2)) {
                    Toast.makeText(MainActivity.this, "Please Enter the Same Password", Toast.LENGTH_SHORT).show();
                } else {
                    SPUtils.putString(MainActivity.this, Contant.PASSWORD, MD5Utils.MD5(pwd));
                    dialog.dismiss();
                    goToSecSetup();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
