package com.witlife.mobileguard.adpater;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.activity.TelecomSafeActivity;
import com.witlife.mobileguard.bean.BlockListBean;
import com.witlife.mobileguard.db.dao.BlackNumberDao;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bruce on 30/10/2017.
 */

public class BlockListAdapter extends BaseAdapter {

    private final ArrayList<BlockListBean> listBeans;
    private Context context;


    public BlockListAdapter(Context context, ArrayList<BlockListBean> listBeans) {
        this.context = context;
        this.listBeans = listBeans;
    }

    @Override
    public int getCount() {
        return listBeans.size();
    }

    @Override
    public BlockListBean getItem(int position) {
        return listBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_blacklist, null);

            holder = new ViewHolder();
            holder.tvNumber = convertView.findViewById(R.id.tv_number);
            holder.tvMode = convertView.findViewById(R.id.tv_mode);
            holder.ivDelete = convertView.findViewById(R.id.iv_delete);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final BlockListBean listBean = listBeans.get(position);

        holder.tvNumber.setText(listBean.getNumber());

        switch (listBean.getStatus()){
            case 0:
                holder.tvMode.setText("Block Callin");
                break;
            case 1:
                holder.tvMode.setText("Block SMS");
                break;
            case 2:
                holder.tvMode.setText("Block Callin & SMS");
                break;
        }

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Delete Record")
                        .setMessage("Do you want to delete " + listBean.getNumber() + " ?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (BlackNumberDao.getInstance(context).delete(listBean.getNumber())){
                                    Toast.makeText(context, "Delete Success!", Toast.LENGTH_SHORT).show();

                                    listBeans.remove(listBean);
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context, "Delete Fail!", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();

                            }
                        }).show();
            }
        });

        return convertView;
    }

    private static class ViewHolder{
        public TextView tvNumber;
        public TextView tvMode;
        public ImageView ivDelete;
    }
}
