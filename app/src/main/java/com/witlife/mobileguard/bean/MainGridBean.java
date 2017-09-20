package com.witlife.mobileguard.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by bruce on 20/09/2017.
 */

public class MainGridBean {

    private String title;
    private Drawable icon;
    private String des;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
