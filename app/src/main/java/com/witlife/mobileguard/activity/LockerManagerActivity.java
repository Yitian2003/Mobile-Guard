package com.witlife.mobileguard.activity;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 1. listen to current app
 * 2. check if current app net to lock
 * 3. if it need to lock , jump to password input page
 * 4. after authentification, user can use app
 */
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

        /*new Thread(){
            @Override
            public void run() {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }.start();*/

        new ApplockTask().execute();
    }

    private void sort() {
        // sorting by letter
        Collections.sort(unlockList, new Comparator<AppInfoBean>() {
            @Override
            public int compare(AppInfoBean o1, AppInfoBean o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        Collections.sort(lockList, new Comparator<AppInfoBean>() {
            @Override
            public int compare(AppInfoBean o1, AppInfoBean o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    @Override
    protected void initTitle() {

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

                btnUnlock.setBackgroundResource(R.drawable.tab_left_pressed);
                btnLock.setBackgroundResource(R.drawable.tab_right_default);

                break;
            case R.id.btn_lock:
                lvUnlock.setVisibility(View.GONE);             
                lvLock.setVisibility(View.VISIBLE);

                btnUnlock.setBackgroundResource(R.drawable.tab_left_default);
                btnLock.setBackgroundResource(R.drawable.tab_right_pressed);
                break;
        }
    }

    private void updateNum(boolean isLock){
        if(isLock){
            tvTitle.setText("Lock Apps(" + lockList.size() + ")");
        } else {
            tvTitle.setText("Unlock Apps(" + unlockList.size() + ")");
        }
    }

    /**
     * first param, pass from caller method, same type as doInBackground
     * second param, progress value, same type as onProgressUpdate
     * third param, return value, same type as doInBackground return type and onPostExcute
     */
    class ApplockTask extends AsyncTask<Void, Integer, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // execute at the background, thread
        @Override
        protected Void doInBackground(Void... params) {

            List<AppInfoBean> installedApps = AppInfoProvider.getInstalledApps(LockerManagerActivity.this);

            unlockList = new ArrayList<>();
            lockList = new ArrayList<>();

            int progress = 0;
            for (AppInfoBean installedApp : installedApps) {
                if(dao.find(installedApp.getPackageName())){
                    lockList.add(installedApp);
                }else {
                    unlockList.add(installedApp);
                }
                progress++;
                publishProgress(progress); // call onProgressUpdate
            }

            sort();

            return null;
        }

        // main thread ui update
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            tvTitle.setText("Unlock Apps(" + unlockList.size() + ")");

            unlockAdapter = new LockerManagerAdapter(false);
            lvUnlock.setAdapter(unlockAdapter);

            lockAdapter = new LockerManagerAdapter(true);
            lvLock.setAdapter(lockAdapter);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            int progress = values[0];
        }
    }

    class LockerManagerAdapter extends BaseAdapter{

        private boolean isLock;

        TranslateAnimation animRight;
        TranslateAnimation animLeft;

        public LockerManagerAdapter(boolean isLock) {
            this.isLock = isLock;

            animRight = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0);
            animRight.setDuration(500);

            animLeft = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0);
            animLeft.setDuration(500);
        }

        @Override
        public int getCount() {

            updateNum(isLock);

            if (isLock){
                return lockList.size();
            } else {
                return unlockList.size();
            }
        }

        @Override
        public AppInfoBean getItem(int position) {
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

            final AppInfoBean item = getItem(position);

            holder.ivIcon.setImageDrawable(item.getIcon());
            holder.tvAppName.setText(item.getName());

            final View finalConvertView = convertView;

            if(isLock){
                holder.ivLock.setImageResource(R.drawable.selector_list_unlock);
                holder.ivLock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        animLeft.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                dao.delete(item.getPackageName());
                                lockList.remove(item);
                                unlockList.add(item);

                                sort();

                                unlockAdapter.notifyDataSetChanged();
                                lockAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        finalConvertView.startAnimation(animLeft);
                    }
                });
            }else {
                holder.ivLock.setImageResource(R.drawable.selector_list_lock);
                holder.ivLock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        animRight.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                dao.add(item.getPackageName());
                                unlockList.remove(item);
                                lockList.add(item);

                                sort();

                                unlockAdapter.notifyDataSetChanged();
                                lockAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        finalConvertView.startAnimation(animRight);
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
