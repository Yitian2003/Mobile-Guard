package com.witlife.mobileguard.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.bean.AppInfoBean;
import com.witlife.mobileguard.engine.AppInfoProvider;
import com.witlife.mobileguard.view.ProgressView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppManagerActivity extends BaseActivity implements View.OnClickListener {

    private static final int TYPE_TITLE = 0;
    private static final int TYPE_NORMAL = 1;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_other)
    ImageView ivOther;
    @BindView(R.id.pv_rom)
    ProgressView pvRom;
    @BindView(R.id.pv_sdcard)
    ProgressView pvSdcard;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.tv_float_title)
    TextView tvFloatTitle;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;

    private List<AppInfoBean> intalledAppsList = new ArrayList<>();
    private ArrayList<AppInfoBean> userAppList;
    private ArrayList<AppInfoBean> systemAppList;

    private PopupWindow popupWindow;
    private AppInfoBean currentInfo;

    @Override
    protected void initData() {
        pvRom.setTitle("Internal Storage:");
        pvSdcard.setTitle("External Storage:");

        initStorageInfo();

        initAppList();

        // listview listen to float window change
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (userAppList != null && systemAppList != null) {
                    if (firstVisibleItem >= userAppList.size() + 1) {
                        tvFloatTitle.setText("System App(" + systemAppList.size() + ")");
                    } else {
                        tvFloatTitle.setText("User App(" + userAppList.size() + ")");
                    }
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // view is current item view

                currentInfo = intalledAppsList.get(position);
                if (!currentInfo.isTitle()){
                    showPopupWindow(view);
                }
            }
        });
    }

    private void showPopupWindow(View itemView) {

        if (popupWindow == null){
            View view = View.inflate(this, R.layout.popup_appinfo, null);
            view.findViewById(R.id.tv_uninstall).setOnClickListener(this);
            view.findViewById(R.id.tv_open).setOnClickListener(this);
            view.findViewById(R.id.tv_share).setOnClickListener(this);
            view.findViewById(R.id.tv_info).setOnClickListener(this);

            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);

            popupWindow.setBackgroundDrawable(new ColorDrawable()); // set a background, so the popup disappear

            popupWindow.setAnimationStyle(R.style.AddressAnimStyle);
        }

        //popupWindow.showAtLocation(llRoot, Gravity.CENTER, 0, 0);
        popupWindow.showAsDropDown(itemView, 150, -itemView.getHeight());
    }

    private void initAppList() {

        new Thread() {
            @Override
            public void run() {
                intalledAppsList = AppInfoProvider.getInstalledApps(AppManagerActivity.this);

                userAppList = new ArrayList<AppInfoBean>();
                systemAppList = new ArrayList<AppInfoBean>();

                for (AppInfoBean info : intalledAppsList) {
                    if (info.isUserApp()) {
                        userAppList.add(info);
                    } else {
                        systemAppList.add(info);
                    }
                }

                intalledAppsList.clear();

                AppInfoBean title1 = new AppInfoBean();
                title1.setTitle(true);
                title1.setTitle("User App");
                intalledAppsList.add(title1);

                intalledAppsList.addAll(userAppList);

                AppInfoBean title2 = new AppInfoBean();
                title2.setTitle(true);
                title2.setTitle("System App");
                intalledAppsList.add(title2);

                intalledAppsList.addAll(systemAppList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(new AppManagerAdapter(AppManagerActivity.this));
                    }
                });
            }
        }.start();

    }

    @Override
    protected void initTitle() {
        tvTitle.setText("App Manager");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_app_manager;
    }

    private void initStorageInfo() {
        try {
            // internal storage  inside /data
            File dataDirectory = Environment.getDataDirectory();
            long totalSpace = dataDirectory.getTotalSpace();
            long freeSpace = dataDirectory.getFreeSpace();
            long usedSpace = totalSpace - freeSpace;

            pvRom.setLeftText(Formatter.formatFileSize(this, usedSpace) + " Used");
            pvRom.setRightText(Formatter.formatFileSize(this, freeSpace) + " Free");

            int progress = (int) (usedSpace * 100 / totalSpace);
            pvRom.setProgress(progress);

            // external storage  /sdcard
            File externalStorageDir = Environment.getExternalStorageDirectory();
            long sdTotalSpace = externalStorageDir.getTotalSpace();
            long sdFreeSpace = externalStorageDir.getFreeSpace();
            long sdUsedSpace = sdTotalSpace - sdFreeSpace;

            pvSdcard.setLeftText(Formatter.formatFileSize(this, sdUsedSpace) + " Used");
            pvSdcard.setRightText(Formatter.formatFileSize(this, sdFreeSpace) + " Free");

            int sdProgress = (int) (sdUsedSpace * 100 / sdTotalSpace);
            pvSdcard.setProgress(sdProgress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_uninstall:

                if (currentInfo.isUserApp()){
                    Uri uri = Uri.parse("package:" + currentInfo.getPackageName());
                    Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, uri);
                    startActivityForResult(uninstallIntent, 100);
                } else {
                    Toast.makeText(this, "Can't uninstall system app!", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_open:

                PackageManager packageManager = getPackageManager();
                Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(currentInfo.getPackageName());

                if (launchIntentForPackage != null){
                    startActivity(launchIntentForPackage);
                } else {
                    Toast.makeText(this, "Can't find start page", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_share:
                // show share content in system
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,
                        "Share a wanderful app to you. Link:https://play.google.com/store/apps/details?id=" + currentInfo.getPackageName());
                startActivity(intent);

                break;
            case R.id.tv_info:

                Uri uri = Uri.parse("package:" + currentInfo.getPackageName());
                Intent inforIntent = new Intent();
                inforIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                inforIntent.setData(uri);
                startActivity(inforIntent);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100){
            initAppList();
        }
    }

    class AppManagerAdapter extends BaseAdapter {

        private Context context;

        public AppManagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return intalledAppsList.size();
        }

        @Override
        public Object getItem(int position) {
            return intalledAppsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {

            if (intalledAppsList.get(position).isTitle()) {
                return TYPE_TITLE;
            } else {
                return TYPE_NORMAL;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            int type = getItemViewType(position);

            if (type == TYPE_NORMAL) {
                ViewHolder holder = null;

                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_app_info, null);

                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);

                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                AppInfoBean appInfoBean = intalledAppsList.get(position);

                holder.ivIcon.setBackground(appInfoBean.getIcon());
                holder.tvName.setText(appInfoBean.getName());
                holder.tvSize.setText(Formatter.formatFileSize(context, appInfoBean.getSize()));

                if (appInfoBean.isInSdcard()) {
                    holder.tvType.setText("External Storage");
                } else {
                    holder.tvType.setText("Internal Storage");
                }

            } else if (type == TYPE_TITLE) {

                TitleViewHolder holder = null;

                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_title, null);

                    holder = new TitleViewHolder(convertView);
                    convertView.setTag(holder);

                } else {
                    holder = (TitleViewHolder) convertView.getTag();
                }

                AppInfoBean appInfoBean = intalledAppsList.get(position);

                if (position == 0) {
                    holder.tvItemTitle.setText(appInfoBean.getTitle() + "(" + userAppList.size() + ")");
                } else {
                    holder.tvItemTitle.setText(appInfoBean.getTitle() + "(" + systemAppList.size() + ")");
                }

            }

            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.iv_icon)
            ImageView ivIcon;
            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.tv_type)
            TextView tvType;
            @BindView(R.id.tv_size)
            TextView tvSize;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

        class TitleViewHolder {

            @BindView(R.id.tv_item_title)
            TextView tvItemTitle;

            TitleViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
