package com.witlife.mobileguard.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.bean.MainGridBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bruce on 20/09/2017.
 */

public class MainAdapter extends BaseAdapter {

    private final Context context;
    private final List<MainGridBean> mainGridList;

    public MainAdapter(Context context, List<MainGridBean> mainGridList) {
        this.context = context;
        this.mainGridList = mainGridList;
    }

    @Override
    public int getCount() {
        return mainGridList.size();
    }

    @Override
    public Object getItem(int i) {
        return mainGridList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MainGridBean item = mainGridList.get(i);

        view = View.inflate(context, R.layout.item_main, null);
        ViewHolder holder = new ViewHolder(view);

        holder.ivIcon.setImageDrawable(item.getIcon());
        holder.tvTitle.setText(item.getTitle());
        holder.tvDes.setText(item.getDes());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_des)
        TextView tvDes;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
