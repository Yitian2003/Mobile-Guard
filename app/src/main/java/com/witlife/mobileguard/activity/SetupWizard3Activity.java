package com.witlife.mobileguard.activity;

import android.Manifest;
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
import android.webkit.PermissionRequest;
import android.widget.EditText;
import android.widget.Toast;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.common.Contant;
import com.witlife.mobileguard.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetupWizard3Activity extends SetupWizardBaseActivty {

    private static final int REQUEST_CONTACT = 1200;
    private static final int REQUEST_CONTACT_PHONE = 1201;

    @BindView(R.id.et_number)
    EditText etNumber;

    @Override
    public void goNextPage() {
        String number = etNumber.getText().toString().trim();
        if (TextUtils.isEmpty(number)){
            Toast.makeText(this, "Please Input Telephone Number", Toast.LENGTH_SHORT).show();
        } else {
            saveToSp(number);

            finish();
            removeCurrentActivity();
            goToNewActivity(SetupWizard4Activity.class, null);

            overridePendingTransition(R.anim.anim_next_enter, R.anim.anim_next_exit);
        }
    }

    private void saveToSp(String number) {
        SPUtils.putString(this, Contant.RELEVANT_MOBILE, number);
    }

    @Override
    public void goPrePage() {

        String number = etNumber.getText().toString().trim();
        if (TextUtils.isEmpty(number)) {
            saveToSp(number);
        }

        finish();
        removeCurrentActivity();
        goToNewActivity(SetupWizard2Activity.class, null);

        overridePendingTransition(R.anim.anim_pre_enter, R.anim.anim_pre_exit);
    }

    @Override
    protected void initSetupPage() {
        String number = SPUtils.getString(this, Contant.RELEVANT_MOBILE, "");
        if(!TextUtils.isEmpty(number)){
            etNumber.setText(number);
        }

    }

    public void selectContact(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQUEST_CONTACT);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setup_wizard3;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CONTACT && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
            if (permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT_PHONE);
                Toast.makeText(this, "Please grant permission to read your contacts", Toast.LENGTH_SHORT).show();
            } else {
                Cursor cursor = getContentResolver()
                        .query(uri, new String[]{ContactsContract.Contacts._ID}, null, null, null);

                cursor.moveToNext();
                String contactId = cursor.getString(0);

                Cursor cursor1 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{contactId}, null);

                if (cursor1 != null && cursor1.moveToNext()){
                    String number = cursor1.getString(0);
                    number = number.trim()
                            .replaceAll(" ", "")
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
