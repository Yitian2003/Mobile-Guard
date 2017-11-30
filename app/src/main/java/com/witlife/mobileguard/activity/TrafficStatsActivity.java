package com.witlife.mobileguard.activity;

import android.net.TrafficStats;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
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
    /*@BindView(R.id.listView)
    ListView listView;*/
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;

    private List<AppInfoBean> installedAppsWithTraffic;

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

            List<AppInfoBean> installedApps = AppInfoProvider.getInstalledApps(TrafficStatsActivity.this);

            installedAppsWithTraffic = new ArrayList<>();

            for (AppInfoBean installedApp : installedApps) {
                long uidRxBytes = TrafficStats.getUidRxBytes(installedApp.getUid()); // receive traffic for app
                long uidTxBytes = TrafficStats.getUidTxBytes(installedApp.getUid()); // send traffic for app*/
                installedApp.setReceiveSize(uidRxBytes);
                installedApp.setSendSize(uidTxBytes);

                if(installedApp.getReceiveSize() > 0 || installedApp.getSendSize() > 0){
                    installedAppsWithTraffic.add(installedApp);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            llLoading.setVisibility(View.GONE);

            recyclerView.setAdapter(new TrafficRecyclerAdapter());
            recyclerView.setLayoutManager(new LinearLayoutManager(TrafficStatsActivity.this));
            //listView.setAdapter(new TrafficAdapter());

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
            return installedAppsWithTraffic.size();
        }

        @Override
        public AppInfoBean getItem(int position) {
            return installedAppsWithTraffic.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            AppInfoBean item = installedAppsWithTraffic.get(position);

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

    class TrafficRecyclerAdapter extends RecyclerView.Adapter<TrafficViewHolder> {

        @Override
        public TrafficViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = View.inflate(TrafficStatsActivity.this, R.layout.item_traffic, null);

            TrafficViewHolder holder = new TrafficViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(TrafficViewHolder holder, int position) {
            AppInfoBean item = installedAppsWithTraffic.get(position);

            holder.ivIcon.setImageDrawable(item.getIcon());
            holder.tvName.setText(item.getName());
            holder.tvReceive.setText("Received: " + Formatter.formatFileSize(TrafficStatsActivity.this, item.getReceiveSize()));
            holder.tvSend.setText("Send: " + Formatter.formatFileSize(TrafficStatsActivity.this, item.getSendSize()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return installedAppsWithTraffic.size();
        }
    }

    static class  TrafficViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_receive)
        TextView tvReceive;
        @BindView(R.id.tv_send)
        TextView tvSend;

        public TrafficViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
