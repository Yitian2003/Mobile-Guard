package com.witlife.mobileguard.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.witlife.mobileguard.common.ActivityManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by bruce on 20/09/2017.
 */

public abstract class BaseActivity extends AppCompatActivity{

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        unbinder = ButterKnife.bind(this);

        ActivityManager.getInstance().add(this);

        initTitle();

        initData();
    }

    protected abstract void initData();

    protected abstract void initTitle();

    public abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbinder.unbind();
    }

    public void goToNewActivity(Class Activity, Bundle bundle){
        Intent intent = new Intent(this, Activity);

        if (bundle != null && bundle.size() != 0){
            intent.putExtra("data", bundle);
        }

        startActivity(intent);
    }

    public void removeCurrentActivity(){
        ActivityManager.getInstance().removeCurrent();
    }

    public void removeAll(){
        ActivityManager.getInstance().removeAll();
    }

    /*public void saveSharePreference(){
        SharedPreferences sp = getSharedPreferences("mobile_guard", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString()
    }*/
}
