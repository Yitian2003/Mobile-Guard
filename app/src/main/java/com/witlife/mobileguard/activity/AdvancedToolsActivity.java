package com.witlife.mobileguard.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.utils.SmsUtils;

import java.io.File;
import java.io.FileNotFoundException;

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
        rlPhone.setOnClickListener(this);
        rlBackupSms.setOnClickListener(this);
        rlRestoreSms.setOnClickListener(this);
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
            case R.id.rl_phone:
                startActivity(new Intent(this, CommonNumberActivity.class));
                break;

            case R.id.rl_backup_sms:
                smsBackup();
                break;
            case R.id.rl_restore_sms:

                smsRestore();
                break;
        }
    }

    private void smsRestore() {

        if (!Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)) {

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Restoring...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.show();

            new Thread() {
                @Override
                public void run() {
                    try {
                        SmsUtils.smsRestore(AdvancedToolsActivity.this,
                                new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/sms.backup"),
                                new SmsUtils.OnSmsCallback() {
                                    @Override
                                    public void onGetTotalCount(int totalCount) {
                                        dialog.setMax(totalCount);
                                    }

                                    @Override
                                    public void onProgress(int progress) {
                                        dialog.setProgress(progress);
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AdvancedToolsActivity.this, "SMS restore fail!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    dialog.dismiss();

                }
            }.start();

        }else {
            Toast.makeText(this, "Can't find SdCard", Toast.LENGTH_SHORT).show();

        }
    }

    private void smsBackup() {

        if (!Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)) {

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Backuping...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.show();

            new Thread() {
                @Override
                public void run() {

                    //SmsUtils.smsBackup(AdvancedToolsActivity.this, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/sms.backup"), new SmsCallbackImpl());
                    try {
                        SmsUtils.smsBackup(AdvancedToolsActivity.this,
                                new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/sms.backup"),
                                new SmsUtils.OnSmsCallback() {
                                    @Override
                                    public void onGetTotalCount(int totalCount) {
                                        dialog.setMax(totalCount);
                                    }

                                    @Override
                                    public void onProgress(int progress) {
                                        dialog.setProgress(progress);
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AdvancedToolsActivity.this, "SMS backup fail!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    dialog.dismiss();
                }
            }.start();
        } else {
            Toast.makeText(this, "Can't find SdCard", Toast.LENGTH_SHORT).show();
        }
    }

    /*class SmsCallbackImpl implements SmsUtils.OnSmsCallback{

        @Override
        public void onGetTotalCount(int totalCount) {

        }

        @Override
        public void onProgress(int progress) {

        }
    }*/
}
