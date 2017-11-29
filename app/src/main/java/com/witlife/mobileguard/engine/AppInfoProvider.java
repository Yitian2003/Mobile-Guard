package com.witlife.mobileguard.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.witlife.mobileguard.bean.AppInfoBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce on 7/11/2017.
 */

public class AppInfoProvider {

    public static List<AppInfoBean> getInstalledApps(Context context){

        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);

        List<AppInfoBean> appInfoBeanList = new ArrayList<>();

        for (PackageInfo packageInfo: installedPackages){
            String packageName = packageInfo.packageName; // package name
            String name = packageInfo.applicationInfo.loadLabel(pm).toString();// application name
            Drawable icon = packageInfo.applicationInfo.loadIcon(pm); // application icon

            int uid = packageInfo.applicationInfo.uid;

            String sourceDir = packageInfo.applicationInfo.sourceDir;
            File file = new File(sourceDir);
            long size = file.length(); // application size

            AppInfoBean appInfoBean = new AppInfoBean();
            appInfoBean.setName(name);
            appInfoBean.setPackageName(packageName);
            appInfoBean.setSize(size);
            appInfoBean.setIcon(icon);
            appInfoBean.setUid(uid);

            int flags = packageInfo.applicationInfo.flags;
            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) > 0){
                // in sdcard
                appInfoBean.setInSdcard(true);
            } else {
                // in internal
                appInfoBean.setInSdcard(false);
            }

            if ((flags & ApplicationInfo.FLAG_SYSTEM) > 0){
                // system app
                appInfoBean.setUserApp(false);
            } else {
                // in internal
                appInfoBean.setUserApp(true);
            }

            appInfoBeanList.add(appInfoBean);
        }

        return appInfoBeanList;
    }
}
