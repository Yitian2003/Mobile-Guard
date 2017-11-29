package com.witlife.mobileguard.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.common.Contant;
import com.witlife.mobileguard.service.AddressService;
import com.witlife.mobileguard.service.BlockService;
import com.witlife.mobileguard.utils.SPUtils;
import com.witlife.mobileguard.utils.ServiceStatusUtils;
import com.witlife.mobileguard.view.AddressDialog;

import butterknife.BindView;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_other)
    ImageView ivOther;
    @BindView(R.id.rl_auto)
    RelativeLayout rlAuto;
    @BindView(R.id.rl_block)
    RelativeLayout rlBlock;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    @BindView(R.id.rl_address_style)
    RelativeLayout rlAddressStyle;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.btn_auto_update)
    ToggleButton btnAutoUpdate;
    @BindView(R.id.btn_block)
    ToggleButton btnBlock;
    @BindView(R.id.btn_address)
    ToggleButton btnAddress;

    private int[] icons = new int[]{R.drawable.shape_address_normal, R.drawable.shape_address_orange,
            R.drawable.shape_address_blue, R.drawable.shape_address_gray, R.drawable.shape_address_green, };

    private String[] names = new String[]{"Transparent", "Orange", "Blue", "Gray", "Green"};
    private AddressDialog dialog;

    @Override
    protected void initData() {

        initToggle();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCurrentActivity();
                goToNewActivity(MainActivity.class, null);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCurrentActivity();
                goToNewActivity(MainActivity.class, null);
            }
        });

        initAutoUpdate();

        initAddress();

        initAddressStyle();

        rlBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //btnBlock.setChecked(!btnBlock.isChecked());
                //btnBlock.setBackgroundResource(btnBlock.isChecked() ? R.drawable.on : R.drawable.off);

                if(ServiceStatusUtils.isServiceRunning(SettingActivity.this, BlockService.class)){
                    btnBlock.setBackgroundResource(R.drawable.off);
                    btnBlock.setChecked(false);
                    stopService(new Intent(SettingActivity.this, BlockService.class));
                } else {
                    btnBlock.setChecked(true);
                    btnBlock.setBackgroundResource(R.drawable.on);
                    startService(new Intent(SettingActivity.this, BlockService.class));
                }

                SPUtils.putBoolean(SettingActivity.this, Contant.BLOCK, btnBlock.isChecked());
            }
        });
    }

    private void initAddressStyle() {
        rlAddressStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AddressDialog(SettingActivity.this);
                dialog.setAdpater(new AddressStyleAdapter());
                dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SPUtils.putInt(SettingActivity.this, Contant.ADDRESS_STYLE, icons[position]);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void initAddress() {

        rlAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ServiceStatusUtils.isServiceRunning(SettingActivity.this, AddressService.class)){
                    btnAddress.setChecked(false);
                    btnAddress.setBackgroundResource(R.drawable.off);
                    stopService(new Intent(getApplicationContext(), AddressService.class));
                } else {
                    btnAddress.setChecked(true);
                    btnAddress.setBackgroundResource(R.drawable.on);
                    startService(new Intent(getApplicationContext(), AddressService.class));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean isAddressRunning = ServiceStatusUtils.isServiceRunning(getApplicationContext(), AddressService.class);
        btnAddress.setChecked(isAddressRunning);
        btnAddress.setBackgroundResource(isAddressRunning ? R.drawable.on : R.drawable.off);

        boolean isBlockRunning = ServiceStatusUtils.isServiceRunning(getApplicationContext(), AddressService.class);
        btnBlock.setChecked(isBlockRunning);
        btnBlock.setBackgroundResource(isBlockRunning ? R.drawable.on : R.drawable.off);

    }

    private void initAutoUpdate() {
        rlAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAutoUpdate.setChecked(!btnAutoUpdate.isChecked());
                btnAutoUpdate.setBackgroundResource(btnAutoUpdate.isChecked() ? R.drawable.on : R.drawable.off);

                SPUtils.putBoolean(SettingActivity.this, Contant.AUTO_UPDATE, btnAutoUpdate.isChecked());
            }
        });
    }

    private void initToggle() {
        btnAutoUpdate.setChecked(SPUtils.getBoolean(this, Contant.AUTO_UPDATE, true));
        btnAutoUpdate.setBackgroundResource(btnAutoUpdate.isChecked() ? R.drawable.on : R.drawable.off);
        btnAddress.setChecked(SPUtils.getBoolean(this, Contant.ADDRESS, false));
        btnAddress.setBackgroundResource(btnAddress.isChecked() ? R.drawable.on : R.drawable.off);
        btnBlock.setChecked(SPUtils.getBoolean(this, Contant.BLOCK, false));
        btnBlock.setBackgroundResource(btnBlock.isChecked() ? R.drawable.on : R.drawable.off);
    }

    @Override
    protected void initTitle() {
        ivBack.setVisibility(View.VISIBLE);
        ivOther.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("Setting");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    class AddressStyleAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return icons.length;
        }

        @Override
        public Object getItem(int position) {
            return icons[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(SettingActivity.this, R.layout.item_address,null);

            ImageView ivIcon = view.findViewById(R.id.iv_icon);
            TextView tvName = view.findViewById(R.id.tv_name);
            ImageView ivSelect = view.findViewById(R.id.iv_select);

            ivIcon.setBackgroundResource(icons[position]);
            tvName.setText(names[position]);
            ivSelect.setVisibility(View.INVISIBLE);

            int selectedItem = SPUtils.getInt(SettingActivity.this, Contant.ADDRESS_STYLE, R.drawable.shape_address_normal);
            if(selectedItem == icons[position]){
                ivSelect.setVisibility(View.VISIBLE);
            }

            return view;
        }
    }

}
