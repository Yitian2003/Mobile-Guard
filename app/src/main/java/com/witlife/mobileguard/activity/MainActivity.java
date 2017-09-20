package com.witlife.mobileguard.activity;

import android.animation.ObjectAnimator;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.adpater.MainAdapter;
import com.witlife.mobileguard.bean.MainGridBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener{

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
        for (int i = 0; i < titles.length; i++){
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
        switch (position){
            case 0:
                break;

        }
    }
}
