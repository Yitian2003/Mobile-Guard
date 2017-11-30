package com.witlife.mobileguard.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.witlife.mobileguard.R;
import com.witlife.mobileguard.bean.Model;
import com.witlife.mobileguard.bean.UpdateBean;
import com.witlife.mobileguard.common.Contant;
import com.witlife.mobileguard.http.OkHttpHelper;
import com.witlife.mobileguard.utils.SPUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    public static final int TO_MAIN = 1000;
    private static final int DOWNLOAD_APK_SUCCESS = 1001;
    private static final int DOWNLOAD_APK_FAIL = 1002;

    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.rl_layout)
    RelativeLayout rlLayout;

    private Unbinder unbinder;
    private File apkFile;
    private long startTime;
    private UpdateBean updateInfo;

    private ProgressDialog progressDialog;
    private int currentVersionCode = 1;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TO_MAIN:
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                case DOWNLOAD_APK_SUCCESS:
                    Toast.makeText(SplashActivity.this, "Download Success", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    installApk();
                    finish();
                    break;

                case DOWNLOAD_APK_FAIL:
                    Toast.makeText(SplashActivity.this, "Download file fail!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    toMain();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        unbinder = ButterKnife.bind(this);

        doAnimation();

        // check whether need to automatically update
        startTime = System.currentTimeMillis();
        currentVersionCode = getCurrentVersionCode();
        if (SPUtils.getBoolean(this, Contant.AUTO_UPDATE, true)) {
            updateApk();
        } else {
            toMain();
        }

        copyDb("address.db");
        copyDb("commonnum.db");
        copyDb("antivirus.db");
    }

    private void updateApk() {

        // connect server
        if (!isConneted()) {
            Toast.makeText(this, "No internet connected", Toast.LENGTH_SHORT).show();
            toMain();
        } else {
            // http request
            String url = Contant.BASE_URL + Contant.UPDATE;
            OkHttpHelper.getInstance().get(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(SplashActivity.this, "Request Failure: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    toMain();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if (response.isSuccessful()){
                        String result = response.body().string();

                        updateInfo = new Gson().fromJson(result, UpdateBean.class);

                        compareVersionCode(updateInfo.getVersionCode());
                    } else {
                        Toast.makeText(SplashActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                        toMain();
                    }
                }
            });
        }
    }

    private void compareVersionCode(int serverVersionCode) {
        // compare versionCode
        if (currentVersionCode <  serverVersionCode){
            new AlertDialog.Builder(this)
                    .setTitle("Download New Version")
                    .setMessage(updateInfo.getDes())
                    .setCancelable(false)
                    .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            toMain();
                        }
                    })
                    .setPositiveButton("Download Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            downloadNewVersion();
                        }
                    })
                    .show();
        } else { // no update
            toMain();
        }
    }

    private boolean isConneted() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo != null){
            return networkInfo.isConnected();
        }

        return false;
    }

    private void toMain() {
        long currentTime = System.currentTimeMillis();
        long delayTime = 3000 - currentTime + startTime;

        if (delayTime < 0){
            delayTime = 0;
        }

        handler.sendEmptyMessageDelayed(TO_MAIN, delayTime);
    }

    private void downloadNewVersion() {
        // show download progress bar
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();

        // create apk in local
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            filesDir = this.getExternalFilesDir("");
        } else {
            filesDir = this.getFilesDir();
        }
        apkFile = new File(filesDir, "update.apk");

        // write inputstream
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                FileOutputStream fos = null;
                URL url = null;
                HttpURLConnection connection = null;

                try {
                    url = new URL(updateInfo.getUrl());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.connect();

                    if (connection.getResponseCode() == 200){
                        progressDialog.setMax(connection.getContentLength());
                        is = connection.getInputStream();
                        fos = new FileOutputStream(apkFile);

                        byte[] buffer = new byte[1024];
                        int len;

                        while ((len = is.read(buffer)) != -1) {
                            progressDialog.incrementProgressBy(len);
                            fos.write(buffer, 0, len );
                        }

                        handler.sendEmptyMessage(DOWNLOAD_APK_SUCCESS);
                    } else {
                        handler.sendEmptyMessage(DOWNLOAD_APK_FAIL);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(connection != null) {
                        connection.disconnect();
                    }

                    if (is != null){
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if(fos != null){
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }

    private void installApk() {
        Intent intent = new Intent("android.intent.action.INSTALL_PACKAGE");
        intent.setData(Uri.parse("file:" + apkFile.getAbsolutePath()));
        startActivity(intent);
    }

    private int getCurrentVersionCode(){
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo;
        int versionCode = -1;
        try {
            //获取包的基本信息; 参1:包名; 参2:0表示不需要额外信息
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String version = packageInfo.versionName;
            versionCode = packageInfo.versionCode;

            tvVersion.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    private void doAnimation() {
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(3000);
        rlLayout.setAnimation(animation);
        rlLayout.startAnimation(animation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbinder.unbind();
        handler.removeCallbacksAndMessages(null);
    }

    private void copyDb(String fileName){// copy database

        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        File filesDir = getFilesDir();
        File desFile = new File(filesDir, fileName);

        if(!desFile.exists()){
            AssetManager assetManager = getAssets();
            try {
                inputStream = assetManager.open(fileName);
                outputStream = new FileOutputStream(desFile);

                byte[] buffer = new byte[1024 * 8];
                int len = 0;

                while ((len = inputStream.read(buffer)) != -1){
                    outputStream.write(buffer, 0, len);
                }
                outputStream.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
