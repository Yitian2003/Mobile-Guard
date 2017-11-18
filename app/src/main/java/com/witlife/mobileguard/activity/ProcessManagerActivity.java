package com.witlife.mobileguard.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.bean.ProcessInfoBean;
import com.witlife.mobileguard.common.Contant;
import com.witlife.mobileguard.engine.ProcessInfoProvider;
import com.witlife.mobileguard.service.AutoKillService;
import com.witlife.mobileguard.utils.SPUtils;
import com.witlife.mobileguard.utils.ServiceStatusUtils;
import com.witlife.mobileguard.view.ProgressView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ProcessManagerActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_other)
    ImageView ivOther;
    @BindView(R.id.pv_num)
    ProgressView pvNum;
    @BindView(R.id.pv_memory)
    ProgressView pvMemory;
    @BindView(R.id.listView)
    StickyListHeadersListView listView;
    @BindView(R.id.btn_select_all)
    Button btnSelectAll;
    @BindView(R.id.btn_reverse)
    Button btnReverse;
    @BindView(R.id.iv_arrow1)
    ImageView ivArrow1;
    @BindView(R.id.iv_arrow2)
    ImageView ivArrow2;
    @BindView(R.id.drawer)
    SlidingDrawer drawer;
    @BindView(R.id.rl_show_system)
    RelativeLayout rlShowSystem;
    @BindView(R.id.btn_show_system)
    ToggleButton btnShowSystem;
    @BindView(R.id.btn_auto_kill)
    ToggleButton btnAutoKill;
    @BindView(R.id.rl_auto_kill)
    RelativeLayout rlAutoKill;

    private int runningProcessNum;
    private int totalProcessNum;
    private long totalMemory;
    private long availMemory;

    private List<ProcessInfoBean> list;
    private ProcessAdapter adapter;
    private ArrayList<ProcessInfoBean> systemList;
    private ArrayList<ProcessInfoBean> userList;

    private boolean showSystem;

    @Override
    protected void initData() {
        pvNum.setTitle("Number of Process:");
        pvMemory.setTitle("Memory:");

        runningProcessNum = ProcessInfoProvider.getRunningProcessNum(this);
        totalProcessNum = ProcessInfoProvider.getTotalProcessNum(this);

        availMemory = ProcessInfoProvider.getAvailMemory(this);
        totalMemory = ProcessInfoProvider.getTotalMemory(this);

        updateProcessView();

        initListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!list.get(position).getPackageName().equals(getPackageName())) {
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_check);
                    checkBox.setChecked(!checkBox.isChecked());

                    list.get(position).setChecked(checkBox.isChecked());
                }

            }
        });

        drawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                ivArrow1.setImageResource(R.drawable.drawer_arrow_down);
                ivArrow2.setImageResource(R.drawable.drawer_arrow_down);

                ivArrow1.clearAnimation();
                ivArrow2.clearAnimation();

            }
        });

        drawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                ivArrow1.setImageResource(R.drawable.drawer_arrow_up);
                ivArrow2.setImageResource(R.drawable.drawer_arrow_up);

                startArrowAnim();
            }
        });

        startArrowAnim();

        showSystem = SPUtils.getBoolean(this, Contant.SHOW_SYSTEM, true);
        btnShowSystem.setChecked(showSystem);
    }

    private void startArrowAnim() {
        AlphaAnimation animation = new AlphaAnimation(0.2f, 1);
        animation.setDuration(500);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        ivArrow1.startAnimation(animation);

        AlphaAnimation animation2 = new AlphaAnimation(1, 0.2f);
        animation2.setDuration(500);
        animation2.setRepeatCount(Animation.INFINITE);
        animation2.setRepeatMode(Animation.REVERSE);
        ivArrow2.startAnimation(animation2);
    }

    private void initListView() {
        new Thread() {
            @Override
            public void run() {
                list = ProcessInfoProvider.getRunningProcesses(ProcessManagerActivity.this);

                userList = new ArrayList<>();
                systemList = new ArrayList<>();

                for (ProcessInfoBean infoBean : list) {
                    if (infoBean.isUserProcess()) {
                        userList.add(infoBean);
                    } else {
                        systemList.add(infoBean);
                    }
                }

                list.clear();
                list.addAll(userList);
                list.addAll(systemList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new ProcessAdapter();
                        listView.setAdapter(adapter);
                    }
                });
            }
        }.start();
    }

    private void updateProcessView() {
        pvNum.setLeftText("Running " + runningProcessNum);
        pvNum.setRightText("Total " + totalProcessNum);

        int percent = runningProcessNum * 100 / totalProcessNum;
        pvNum.setProgress(percent);

        pvMemory.setLeftText("Used " + Formatter.formatFileSize(this, totalMemory - availMemory));
        pvMemory.setRightText("Available " + Formatter.formatFileSize(this, availMemory));

        int memPercent = (int) ((totalMemory - availMemory) * 100 / totalMemory);
        pvMemory.setProgress(memPercent);
    }

    @Override
    protected void initTitle() {
        ivOther.setVisibility(View.VISIBLE);
        ivOther.setImageResource(R.drawable.selector_clean);
        tvTitle.setText("Process Manager");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_process_manager;
    }

    @Override
    protected void onStart() {
        super.onStart();

        showSystem = SPUtils.getBoolean(this, Contant.SHOW_SYSTEM, true);
        btnShowSystem.setChecked(showSystem);
        btnShowSystem.setBackgroundResource(showSystem ? R.drawable.on : R.drawable.off);

        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this, AutoKillService.class);
        btnAutoKill.setChecked(serviceRunning);
        btnAutoKill.setBackgroundResource(serviceRunning ? R.drawable.on : R.drawable.off);

    }

    @OnClick({R.id.iv_back, R.id.iv_other, R.id.btn_select_all, R.id.btn_reverse, R.id.rl_show_system,
            R.id.rl_auto_kill})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_other:
                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

                List<ProcessInfoBean> killedList = new ArrayList<>();
                for (ProcessInfoBean infoBean : list) {
                    if (infoBean.isChecked()) {
                        am.killBackgroundProcesses(infoBean.getPackageName());
                        killedList.add(infoBean);
                        //list.remove(infoBean); //ConcurrentModificationException
                    }
                }
                long savedMemory = 0;

                for (ProcessInfoBean infoBean : killedList) {
                    savedMemory += infoBean.getMemory();
                }

                list.removeAll(killedList);
                adapter.notifyDataSetChanged();

                String toast = String.format("Total kill %d processes and release %s memory", killedList.size(), Formatter.formatFileSize(this, savedMemory));
                Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
                /*Toast.makeText(this, "Total kill " + killedList.size() +
                        " processess and release " + Formatter.formatFileSize(this, savedMemory)
                        + " memory", Toast.LENGTH_SHORT).show();*/

                // update progressView
                runningProcessNum -= killedList.size();
                availMemory += savedMemory;
                updateProcessView();

                break;
            case R.id.btn_select_all:
                for (ProcessInfoBean infoBean : list) {
                    if (infoBean.getPackageName().equals(getPackageName())) {
                        continue;
                    }
                    infoBean.setChecked(true);
                }

                adapter.notifyDataSetChanged();

                break;
            case R.id.btn_reverse:
                for (ProcessInfoBean infoBean : list) {
                    if (infoBean.getPackageName().equals(getPackageName())) {
                        continue;
                    }
                    infoBean.setChecked(!infoBean.isChecked());
                }
                adapter.notifyDataSetChanged();
                break;

            case R.id.rl_show_system:

                showSystem = ! showSystem;
                SPUtils.putBoolean(this, Contant.SHOW_SYSTEM, showSystem);

                btnShowSystem.setChecked(showSystem);
                btnShowSystem.setBackgroundResource(showSystem ? R.drawable.on : R.drawable.off);

                adapter.notifyDataSetChanged();
                break;
            case R.id.rl_auto_kill:

                if(ServiceStatusUtils.isServiceRunning(this, AutoKillService.class)){
                    stopService(new Intent(this, AutoKillService.class));
                    btnAutoKill.setChecked(false);
                    btnAutoKill.setBackgroundResource(R.drawable.off);

                } else {
                    startService(new Intent(this, AutoKillService.class));
                    btnAutoKill.setChecked(true);
                    btnAutoKill.setBackgroundResource(R.drawable.on);
                }
                break;
        }
    }

    class ProcessAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        @Override
        public int getCount() {

            if(showSystem){
                return list.size();
            } else {
                return userList.size();
            }

        }

        @Override
        public ProcessInfoBean getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            ProcessInfoBean item = list.get(position);

            if (convertView == null) {
                convertView = View.inflate(ProcessManagerActivity.this, R.layout.item_process_info, null);

                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.ivIcon.setImageDrawable(item.getIcon());
            holder.tvName.setText(item.getName());
            holder.tvMemory.setText(Formatter.formatFileSize(ProcessManagerActivity.this, item.getMemory()));

            if (item.getPackageName().equals(getPackageName())) {
                holder.checkBox.setVisibility(View.GONE);
            } else {
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setChecked(item.isChecked());
            }

            return convertView;
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {

            TitleViewHolder holder = null;

            if (convertView == null) {
                convertView = View.inflate(ProcessManagerActivity.this, R.layout.item_title, null);

                holder = new TitleViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (TitleViewHolder) convertView.getTag();
            }

            ProcessInfoBean item = list.get(position);

            if (item.isUserProcess()) {
                holder.tvListTitle.setText("User Process(" + userList.size() + ")");
            } else {
                holder.tvListTitle.setText("System Process(" + systemList.size() + ")");
            }
            return convertView;
        }

        @Override
        public long getHeaderId(int position) {

            ProcessInfoBean infoBean = getItem(position);
            return infoBean.isUserProcess() ? 0 : 1;
        }
    }

    class ViewHolder {

        public ImageView ivIcon;
        public TextView tvName;
        public TextView tvMemory;
        public CheckBox checkBox;

        public ViewHolder(View view) {
            ivIcon = view.findViewById(R.id.iv_icon);
            tvName = view.findViewById(R.id.tv_name);
            tvMemory = view.findViewById(R.id.tv_memory);
            checkBox = view.findViewById(R.id.cb_check);
        }
    }

    class TitleViewHolder {

        public TextView tvListTitle;

        public TitleViewHolder(View view) {
            tvListTitle = view.findViewById(R.id.tv_item_title);
        }
    }
}
