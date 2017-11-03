package com.witlife.mobileguard.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.common.Contant;
import com.witlife.mobileguard.utils.SPUtils;

import butterknife.BindView;

/**
 * Created by bruce on 29/10/2017.
 */

public class AddressDialog extends Dialog {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.listView)
    ListView listView;

    public AddressDialog(@NonNull final Context context) {
        super(context, R.style.AddressDialogStyle);

        setContentView(R.layout.dialog_address);
        listView = findViewById(R.id.listView);

        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();

        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(attributes);

    }

    public void setAdpater(BaseAdapter adpater){
        listView.setAdapter(adpater);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
        listView.setOnItemClickListener(listener);
    }
}
