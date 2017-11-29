package com.witlife.mobileguard.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by bruce on 7/11/2017.
 */

public class ProcessInfoBean {

    private String packageName;
    private String name;
    private Drawable icon;
    private long memory;

    private boolean isUserProcess;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isUserProcess() {
        return isUserProcess;
    }

    public void setUserProcess(boolean userProcess) {
        isUserProcess = userProcess;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }
}
