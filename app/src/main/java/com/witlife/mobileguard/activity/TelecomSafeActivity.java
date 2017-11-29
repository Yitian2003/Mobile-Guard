package com.witlife.mobileguard.activity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.adpater.BlockListAdapter;
import com.witlife.mobileguard.bean.BlockListBean;
import com.witlife.mobileguard.db.dao.BlackNumberDao;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TelecomSafeActivity extends BaseActivity {

    private static final int ADD_NUMBER = 1221;
    private static final String NEW_LIST_ITEM = "new_list_item";
    private static final String UPDATE_LIST_ITEM = "update_list_item";

    private static final int UPDATE_NUMBER = 1222;
    private static final String POSITION = "position";
    private static final String IS_UPDATE = "is_update";

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
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;

    private ArrayList<BlockListBean> listBeans;
    private BlockListAdapter adapter;
    private BlackNumberDao blackNumberDao;

    private boolean isLoading = false;

    @Override
    protected void initData() {
        listBeans = new ArrayList<BlockListBean>();
        blackNumberDao = BlackNumberDao.getInstance(TelecomSafeActivity.this);

        showList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TelecomSafeActivity.this, UpdateBlockListActivity.class);
                intent.putExtra(UPDATE_LIST_ITEM, listBeans.get(position));
                intent.putExtra(POSITION, position);
                intent.putExtra(IS_UPDATE, true);

                startActivityForResult(intent, UPDATE_NUMBER);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    int lastVisiblePosition = listView.getLastVisiblePosition();

                    if (lastVisiblePosition == listBeans.size() - 1 && !isLoading) {
                        if (listBeans.size() < blackNumberDao.getTotalCount()) {
                            showList();
                        } else {
                            Toast.makeText(TelecomSafeActivity.this, "No more data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private void showList() {

        llLoading.setVisibility(View.VISIBLE);
        isLoading = true;

        new Thread() {
            @Override
            public void run() {
                /*try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                SystemClock.sleep(500);

                // listBeans.size() is the index of next page
                listBeans.addAll(blackNumberDao.findPart(listBeans.size()));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (adapter == null) {
                            adapter = new BlockListAdapter(TelecomSafeActivity.this, listBeans);

                            listView.setAdapter(adapter);
                            listView.setEmptyView(ivEmpty);

                            adapter.registerDataSetObserver(new DataSetObserver() {
                                @Override
                                public void onChanged() {
                                    if (listBeans.size() < 8) {
                                        autoShowList();
                                    }
                                }
                            });
                        } else {
                            adapter.notifyDataSetChanged();
                        }

                        llLoading.setVisibility(View.GONE);
                        isLoading = false;
                    }
                });
            }
        }.start();

    }

    private void autoShowList() {
        blackNumberDao = BlackNumberDao.getInstance(TelecomSafeActivity.this);
        findViewById(R.id.ll_loading).setVisibility(View.VISIBLE);
        isLoading = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(500);

                // listBeans.size() is the index of next page
                listBeans.addAll(blackNumberDao.findPart(listBeans.size()));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        llLoading.setVisibility(View.GONE);
                        /*if (listBeans.size() <= 0) {
                            ivEmpty.setVisibility(View.VISIBLE);
                        }*/
                    }
                });
            }
        }).start();
    }

    @Override
    protected void initTitle() {
        tvTitle.setText("Manager Block List");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ivOther.setBackgroundResource(R.drawable.selector_menu_add);

        // add new number
        ivOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelecomSafeActivity.this, UpdateBlockListActivity.class);
                intent.putExtra(IS_UPDATE, false);
                startActivityForResult(intent, ADD_NUMBER);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_telecom_safe;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NUMBER && resultCode == RESULT_OK && data != null) {
            BlockListBean bean = (BlockListBean) data.getSerializableExtra(NEW_LIST_ITEM);
            listBeans.add(0, bean);
            adapter.notifyDataSetChanged();

        } else if (requestCode == UPDATE_NUMBER && resultCode == RESULT_OK && data != null) {
            BlockListBean bean = (BlockListBean) data.getSerializableExtra(UPDATE_LIST_ITEM);
            int position = data.getIntExtra(POSITION, -1);
            listBeans.set(position, bean);
            adapter.notifyDataSetChanged();
        }
    }
}
