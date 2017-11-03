package com.witlife.mobileguard.bean;

import java.io.Serializable;

/**
 * Created by bruce on 30/10/2017.
 */

public class BlockListBean implements Serializable {
    private String number;
    private int mode;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getStatus() {
        return mode;
    }

    public void setStatus(int mode) {
        this.mode = mode;
    }
}
