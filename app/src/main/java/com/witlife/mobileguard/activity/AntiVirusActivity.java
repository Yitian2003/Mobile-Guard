package com.witlife.mobileguard.activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.db.dao.VirusDao;
import com.witlife.mobileguard.utils.MD5Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * anti virus
 */
public class AntiVirusActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_other)
    ImageView ivOther;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;

    List<ScanInfo> scanInfos;

    @Override
    protected void initData() {
        startScan();
    }

    private void startScan() {
        new VirusTask().execute();
    }


    @Override
    protected void initTitle() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvTitle.setText("Mobile AntiVirus");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_anti_virus;
    }

    class VirusTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            scanInfos = new ArrayList<>();

            llLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            PackageManager packageManager = getPackageManager();
            List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);

            for (PackageInfo installedPackage : installedPackages) {

                ScanInfo scanInfo = new ScanInfo();

                String packageName = installedPackage.packageName;

                ApplicationInfo applicationInfo = installedPackage.applicationInfo;

                String apkPath = applicationInfo.sourceDir;
                String md5 = MD5Utils.getFileMd5(apkPath);

                boolean isVirus = VirusDao.isVirus(AntiVirusActivity.this, md5);

                scanInfo.setIcon(applicationInfo.loadIcon(packageManager));
                scanInfo.setName(applicationInfo.loadLabel(packageManager).toString());
                scanInfo.setPackageName(packageName);
                scanInfo.setVirus(isVirus);

                scanInfos.add(scanInfo);

                if (isVirus) {

                } else {

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            llLoading.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(AntiVirusActivity.this));
            recyclerView.setAdapter(new VirusAdapter());
        }
    }

    class VirusAdapter extends RecyclerView.Adapter<VirusHolder> {

        @Override
        public VirusHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = View.inflate(AntiVirusActivity.this, R.layout.item_virus, null);

            VirusHolder holder = new VirusHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(VirusHolder holder, int position) {
            ScanInfo item = scanInfos.get(position);

            holder.ivIcon.setImageDrawable(item.getIcon());
            holder.tvName.setText(item.getName());

            if(item.isVirus){
                holder.tvStatus.setText("Dangous!");
                holder.tvStatus.setTextColor(Color.RED);
            } else {
                holder.tvStatus.setTextColor(Color.GREEN);
                holder.tvStatus.setText("Safe");
            }
        }

        @Override
        public int getItemCount() {
            return scanInfos.size();
        }
    }

    static class VirusHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_status)
        TextView tvStatus;

        public VirusHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ScanInfo {
        private String packageName;
        private Drawable icon;
        private String name;
        private boolean isVirus;

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isVirus() {
            return isVirus;
        }

        public void setVirus(boolean virus) {
            isVirus = virus;
        }
    }
}
