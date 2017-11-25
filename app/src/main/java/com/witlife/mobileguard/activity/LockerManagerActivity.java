package com.witlife.mobileguard.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.bean.AppInfoBean;
import com.witlife.mobileguard.db.dao.AppLockDao;
import com.witlife.mobileguard.engine.AppInfoProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LockerManagerActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.btn_unlock)
    Button btnUnlock;
    @BindView(R.id.btn_lock)
    Button btnLock;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lv_unlock)
    ListView lvUnlock;
    @BindView(R.id.lv_lock)
    ListView lvLock;

    private LockerManagerAdapter unlockAdapter;
    private LockerManagerAdapter lockAdapter;
    private AppLockDao dao;

    private List<AppInfoBean> unlockList;
    private List<AppInfoBean> lockList;

    @Override
    protected void initData() {
        btnUnlock.setOnClickListener(this);
        btnLock.setOnClickListener(this);

        dao = AppLockDao.getInstance(this);

        new Thread(){
            @Override
            public void run() {
                List<AppInfoBean> installedApps = AppInfoProvider.getInstalledApps(LockerManagerActivity.this);

                unlockList = new ArrayList<>();
                lockList = new ArrayList<>();

                for (AppInfoBean installedApp : installedApps) {
                    if(dao.find(installedApp.getPackageName())){
                        lockList.add(installedApp);
                    }else {
                        unlockList.add(installedApp);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTitle.setText("Unlock Apps(" + unlockList.size() + ")");

                        unlockAdapter = new LockerManagerAdapter(false);
                        lvUnlock.setAdapter(unlockAdapter);

                        lockAdapter = new LockerManagerAdapter(true);
                        lvLock.setAdapter(lockAdapter);

                    }
                });
            }
        }.start();
    }

    @Override
    protected void initTitle() {
        //btnUnlock.performClick();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_locker_manager;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_unlock:
                lvLock.setVisibility(View.GONE);
                lvUnlock.setVisibility(View.VISIBLE);

                tvTitle.setText("Unlock Apps(" + unlockList.size() + ")");

                btnUnlock.setBackgroundResource(R.drawable.tab_left_pressed);
                btnLock.setBackgroundResource(R.drawable.tab_right_default);

                break;
            case R.id.btn_lock:
                lvUnlock.setVisibility(View.GONE);             
                lvLock.setVisibility(View.VISIBLE);

                tvTitle.setText("Lock Apps(" + lockList.size() + ")");

                btnUnlock.setBackgroundResource(R.drawable.tab_left_default);
                btnLock.setBackgroundResource(R.drawable.tab_right_pressed);
                break;
        }
    }

    class LockerManagerAdapter extends BaseAdapter{

        private boolean isLock;

        public LockerManagerAdapter(boolean isLock) {
            this.isLock = isLock;
        }

        @Override
        public int getCount() {

            if (isLock){
                return lockList.size();
            } else {
                return unlockList.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (isLock){
                return lockList.get(position);
            } else {
                return unlockList.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null){
                convertView = View.inflate(LockerManagerActivity.this, R.layout.item_lock_manager, null);

                holder = new ViewHolder(convertView);
                convertView.setTag(holder);

            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            final AppInfoBean item = unlockList.get(position);

            holder.ivIcon.setImageDrawable(item.getIcon());
            holder.tvAppName.setText(item.getName());

            if(isLock){
                holder.ivLock.setImageResource(R.drawable.selector_list_unlock);
                holder.ivLock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dao.delete(item.getPackageName());
                        lockList.remove(item);
                        unlockList.add(item);
                        unlockAdapter.notifyDataSetChanged();
                        lockAdapter.notifyDataSetChanged();
                    }
                });
            }else {
                holder.ivLock.setImageResource(R.drawable.selector_list_lock);
                holder.ivLock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dao.add(item.getPackageName());
                        unlockList.remove(item);
                        lockList.add(item);
                        unlockAdapter.notifyDataSetChanged();
                        lockAdapter.notifyDataSetChanged();
                    }
                });
            }

            return convertView;
        }
    }

    class ViewHolder{
        public ImageView ivIcon;
        public TextView tvAppName;
        public ImageView ivLock;

        public ViewHolder(View view) {
            ivIcon = view.findViewById(R.id.iv_icon);
            tvAppName = view.findViewById(R.id.tv_app_name);
            ivLock = view.findViewById(R.id.iv_lock);
        }
    }
}
