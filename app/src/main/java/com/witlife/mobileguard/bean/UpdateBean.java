package com.witlife.mobileguard.bean;

/**
 * Created by bruce on 19/09/2017.
 */

public class UpdateBean {
    /**
     * versionName : 2.0
     * versionCode : 2
     * des : Fix some bugs, update new UI feature
     * url : http://www.baidu.com
     */

    private String versionName;
    private int versionCode;
    private String des;
    private String url;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
