package com.witlife.mobileguard.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.bean.BlockListBean;
import com.witlife.mobileguard.db.dao.BlackNumberDao;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateBlockListActivity extends BaseActivity {

    private static final String NEW_LIST_ITEM = "new_list_item";
    private static final int REQUEST_CONTACT = 1445;
    private static final String UPDATE_LIST_ITEM = "update_list_item";
    private static final String POSITION = "position";
    private static final String IS_UPDATE = "is_update";

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_other)
    ImageView ivOther;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.btn_update)
    Button btnUpdate;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.radio_mode)
    RadioGroup radioMode;
    @BindView(R.id.rb_call)
    RadioButton rbCall;
    @BindView(R.id.rb_sms)
    RadioButton rbSms;
    @BindView(R.id.rb_both)
    RadioButton rbBoth;
    @BindView(R.id.btn_contact)
    Button btnContact;

    private String number;
    private BlackNumberDao blackNumberDao;

    private Intent intent;
    private boolean isUpdate = false;
    private BlockListBean blockBean;

    @Override
    protected void initData() {

        if (!isUpdate){
            btnContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setData(ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, REQUEST_CONTACT);
                }
            });
        } else {
            // set up input textbox and radio button
             blockBean = (BlockListBean) intent.getSerializableExtra(UPDATE_LIST_ITEM);

             // phone number not editable
             etNumber.setText(blockBean.getNumber());
             etNumber.setEnabled(false);

             switch (blockBean.getStatus())
             {
                 case 0:
                     rbCall.setChecked(true);
                     break;
                 case 1:
                     rbSms.setChecked(true);
                     break;
                 case 2:
                     rbBoth.setChecked(true);
                     break;
             }

             btnContact.setVisibility(View.GONE);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = etNumber.getText().toString().trim();
                int mode = 0;

                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(UpdateBlockListActivity.this, "Input Number Can't Be Empty", Toast.LENGTH_SHORT).show();

                } else {
                    int checkRadioButton = radioMode.getCheckedRadioButtonId();

                    switch (checkRadioButton) {
                        case R.id.rb_call:
                            mode = 0;
                            break;
                        case R.id.rb_sms:
                            mode = 1;
                            break;
                        case R.id.rb_both:
                            mode = 2;
                            break;
                    }

                    blackNumberDao = BlackNumberDao.getInstance(UpdateBlockListActivity.this);

                    if (!isUpdate){
                        boolean isAdded = blackNumberDao.add(number, mode);

                        if (isAdded) {
                            Toast.makeText(UpdateBlockListActivity.this, "Save Success!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();

                            blockBean = new BlockListBean();
                            blockBean.setNumber(number);
                            blockBean.setStatus(mode);
                            intent.putExtra(NEW_LIST_ITEM, blockBean);
                            setResult(RESULT_OK, intent);

                            finish();
                        } else {
                            Toast.makeText(UpdateBlockListActivity.this, "Save Fail!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        boolean update = blackNumberDao.update(number, mode);

                        if (update){
                            Toast.makeText(UpdateBlockListActivity.this, "Update Success!", Toast.LENGTH_SHORT).show();

                            blockBean = new BlockListBean();
                            blockBean.setNumber(number);
                            blockBean.setStatus(mode);
                            int position = intent.getIntExtra(POSITION, -1);
                            intent.putExtra(UPDATE_LIST_ITEM, blockBean);
                            intent.putExtra(POSITION, position);
                            setResult(RESULT_OK, intent);

                            finish();
                        }else {
                            Toast.makeText(UpdateBlockListActivity.this, "Update Fail!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void initTitle() {
        intent = getIntent();
        isUpdate = intent.getBooleanExtra(IS_UPDATE, false);

        if (!isUpdate){
            tvTitle.setText("Add Block Number");
        } else {
            tvTitle.setText("Update Block Number");
        }

        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_block_list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CONTACT && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

            if(permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT);

                Toast.makeText(this, "Please grant permission to read you contacts", Toast.LENGTH_SHORT).show();
            } else {
                Cursor cursor = getContentResolver().query(uri, new String[]{ContactsContract.Contacts._ID}, null, null, null);

                if(cursor != null && cursor.moveToNext()){
                    String contactId = cursor.getString(0);

                    Cursor cursor1 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            new String[]{contactId}, null);

                    if(cursor1 != null && cursor1.moveToNext()){
                        String number = cursor1.getString(0);

                        number = number.trim().replaceAll(" ","")
                                .replaceAll("-", "")
                                .replaceAll("\\(", "")
                                .replaceAll("\\)", "");
                        etNumber.setText(number);
                    } else {
                        etNumber.setText("");
                    }

                    cursor1.close();
                    cursor.close();
                }
            }
        }
    }
}
