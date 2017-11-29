package com.witlife.mobileguard.activity;

import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.bean.AppInfoBean;
import com.witlife.mobileguard.engine.AppInfoProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrafficStatsActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_other)
    ImageView ivOther;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;

    private List<AppInfoBean> installedApps;

    @Override
    protected void initData() {
        /*long totalRxBytes = TrafficStats.getTotalRxBytes(); // receive traffic
        long totalTxBytes = TrafficStats.getTotalTxBytes(); // send traffic

        long mobileRxBytes = TrafficStats.getMobileRxBytes(); // receive traffic for mobile
        long mobileTxBytes = TrafficStats.getMobileTxBytes(); // send traffic for mobile

        long uidRxBytes = TrafficStats.getUidRxBytes(1000); // receive traffic for app
        long uidTxBytes = TrafficStats.getUidTxBytes(1000); // send traffic for app*/
        new TrafficTask().execute();

    }

    class TrafficTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            llLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            installedApps = AppInfoProvider.getInstalledApps(TrafficStatsActivity.this);

            for (AppInfoBean installedApp : installedApps) {
                long uidRxBytes = TrafficStats.getUidRxBytes(installedApp.getUid()); // receive traffic for app
                long uidTxBytes = TrafficStats.getUidTxBytes(installedApp.getUid()); // send traffic for app*/
                installedApp.setReceiveSize(uidRxBytes);
                installedApp.setSendSize(uidTxBytes);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            llLoading.setVisibility(View.GONE);
            listView.setAdapter(new TrafficAdapter());
        }
    }

    @Override
    protected void initTitle() {
        tvTitle.setText("Traffic Controll Manager");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_traffic_stats;
    }

    class TrafficAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return installedApps.size();
        }

        @Override
        public AppInfoBean getItem(int position) {
            return installedApps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            AppInfoBean item = installedApps.get(position);

            if (convertView == null) {
                convertView = View.inflate(TrafficStatsActivity.this, R.layout.item_traffic, null);

                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.ivIcon.setImageDrawable(item.getIcon());
            holder.tvName.setText(item.getName());
            holder.tvReceive.setText("Received: " + Formatter.formatFileSize(TrafficStatsActivity.this, item.getReceiveSize()));
            holder.tvSend.setText("Sent: " + Formatter.formatFileSize(TrafficStatsActivity.this, item.getSendSize()));

            return convertView;
        }

        class ViewHolder {

            @BindView(R.id.iv_icon)
            ImageView ivIcon;
            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.tv_receive)
            TextView tvReceive;
            @BindView(R.id.tv_send)
            TextView tvSend;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
