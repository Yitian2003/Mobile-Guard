package com.witlife.mobileguard.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.witlife.mobileguard.R;
import com.witlife.mobileguard.bean.ProcessInfoBean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by bruce on 7/11/2017.
 */

public class ProcessInfoProvider {

    public static int getRunningProcessNum(Context context){

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return manager.getRunningAppProcesses().size();
    }

    public static int getTotalProcessNum(Context context){
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES
        | PackageManager.GET_RECEIVERS | PackageManager.GET_PROVIDERS | PackageManager.GET_SERVICES);

        int totalCount = 0;

        for(PackageInfo packageInfo : installedPackages){
            //List<String> set = new ArrayList<>();
            HashSet<String> set = new HashSet<>();

            set.add(packageInfo.applicationInfo.processName); // default process

            ActivityInfo[] activities = packageInfo.activities; // activity process

            if (activities != null){
                for (ActivityInfo info : activities) {
                    set.add(info.processName);
                }
            }

            ServiceInfo[] services = packageInfo.services; // services process

            if(services != null){
                for (ServiceInfo info : services) {
                        set.add(info.processName);
                }
            }

            ActivityInfo[] receivers = packageInfo.receivers;

            if(receivers != null){
                for (ActivityInfo receiver : receivers) {
                    set.add(receiver.processName);
                }
            }

            ProviderInfo[] providers = packageInfo.providers;

            if(providers != null){
                for (ProviderInfo info : providers) {
                    set.add(info.processName);
                }
            }

            totalCount += set.size();
        }
        return totalCount;
    }

    public static long getAvailMemory(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);

        return info.availMem;
    }

    public static long getTotalMemory(Context context){
        /*ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);*/

        try {
            BufferedReader reader = new BufferedReader(new FileReader("proc/meminfo"));

            String readLine = reader.readLine();

            StringBuffer sb = new StringBuffer();
            char[] chars = readLine.toCharArray();
            for (char c : chars) {
                if(c >= '0' && c <='9'){
                    sb.append(c);
                }
            }

            String mem = sb.toString();

            long memory = Long.parseLong(mem) * 1024;

            return memory;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<ProcessInfoBean> getRunningProcesses(Context context){

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();

        PackageManager packageManager = context.getPackageManager();

        List<ProcessInfoBean> list = new ArrayList<>();

        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            String packageName = runningAppProcess.processName; // default process is package name
            ProcessInfoBean infoBean = new ProcessInfoBean();
            infoBean.setPackageName(packageName);

            Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{runningAppProcess.pid});
            long memory = processMemoryInfo[0].getTotalPrivateDirty() * 1024;
            infoBean.setMemory(memory);

            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);

                String name = applicationInfo.loadLabel(packageManager).toString();
                Drawable icon = applicationInfo.loadIcon(packageManager);

                infoBean.setName(name);
                infoBean.setIcon(icon);

                int flags = applicationInfo.flags;

                if((flags & ApplicationInfo.FLAG_SYSTEM) > 0){
                    infoBean.setUserProcess(false);
                } else {
                    infoBean.setUserProcess(true);
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();

                infoBean.setIcon(context.getResources().getDrawable(R.mipmap.ic_launcher));
                infoBean.setName(packageName);
                infoBean.setUserProcess(false);
            }

            list.add(infoBean);
        }
        return list;
    }

    public static void killAllProcesses(Context context){
        List<ProcessInfoBean> runningProcesses = getRunningProcesses(context);
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ProcessInfoBean runningProcess : runningProcesses) {
            if (runningProcess.getPackageName().equals(context.getPackageName())){
                continue;
            }
            am.killBackgroundProcesses(runningProcess.getPackageName());
        }
    }
}
