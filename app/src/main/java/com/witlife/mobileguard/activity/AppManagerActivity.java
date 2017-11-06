package com.witlife.mobileguard.activity;

import android.content.Context;
import android.os.Environment;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.bean.AppInfoBean;
import com.witlife.mobileguard.engine.AppInfoProvider;
import com.witlife.mobileguard.view.ProgressView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppManagerActivity extends BaseActivity {

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
    private List<AppInfoBean> intalledAppsList = new ArrayList<>();

    @Override
    protected void initData() {
        pvRom.setTitle("Internal Storage:");
        pvSdcard.setTitle("External Storage:");

        initStorageInfo();

        initAppList();
    }

    private void initAppList() {

        new Thread() {
            @Override
            public void run() {
                intalledAppsList = AppInfoProvider.getInstalledApps(AppManagerActivity.this);

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
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null){
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

            if (appInfoBean.isInSdcard()){
                holder.tvType.setText("External Storage");
            } else {
                holder.tvType.setText("Internal Storage");
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
    }
}
