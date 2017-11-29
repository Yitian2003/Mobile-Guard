package com.witlife.mobileguard.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.db.dao.CommonNumberDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonNumberActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_other)
    ImageView ivOther;
    @BindView(R.id.elv_list)
    ExpandableListView elvList;

    private List<CommonNumberDao.GroupInfo> groupInfoList;

    @Override
    protected void initData() {

        groupInfoList = CommonNumberDao.getCommonNumbers(this);

        elvList.setAdapter(new CommonNumberAdapter());

        elvList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + groupInfoList.get(groupPosition).childInfoList.get(childPosition).number));

                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    protected void initTitle() {
        tvTitle.setText("Public Phone Number");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_number;
    }

    class CommonNumberAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return groupInfoList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return groupInfoList.get(groupPosition).childInfoList.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupInfoList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return groupInfoList.get(groupPosition).childInfoList.get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            TextView view = new TextView(CommonNumberActivity.this);

            view.setText(groupInfoList.get(groupPosition).name);
            //view.setBackgroundColor(getResources().getColor(R.color.divider));
            view.setTextColor(Color.BLACK);
            view.setPadding(15,15, 15, 15);
            view.setTextSize(22);

            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView view = new TextView(CommonNumberActivity.this);

            CommonNumberDao.ChildInfo childInfo = groupInfoList.get(groupPosition).childInfoList.get(childPosition);
            view.setText(childInfo.name + "\n" + childInfo.number);
            view.setBackgroundColor(Color.WHITE);
            view.setPadding(15,15, 15, 15);
            view.setTextSize(18);

            return view;
        }

        // child can be click, false - disable, true - enable
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
