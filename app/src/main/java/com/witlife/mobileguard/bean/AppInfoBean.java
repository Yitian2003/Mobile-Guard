package com.witlife.mobileguard.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by bruce on 7/11/2017.
 */

public class AppInfoBean {

    private String packageName;
    private String name;
    private Drawable icon;
    private long size;

    private boolean isInSdcard;
    private boolean isUserApp;

    private boolean isTitle;
    private String title;

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isUserApp() {
        return isUserApp;
    }

    public void setUserApp(boolean userApp) {
        isUserApp = userApp;
    }

    public boolean isInSdcard() {
        return isInSdcard;
    }

    public void setInSdcard(boolean inSdcard) {
        isInSdcard = inSdcard;
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
