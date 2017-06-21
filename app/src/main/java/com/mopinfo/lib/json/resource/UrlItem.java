package com.mopinfo.lib.json.resource;

/**
 * Created by mop on 2017/6/18.
 */
public class UrlItem {

    private String url;

    private int loadExpiredTime;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLoadExpiredTime() {
        return loadExpiredTime;
    }

    public void setLoadExpiredTime(int loadExpiredTime) {
        this.loadExpiredTime = loadExpiredTime;
    }
}
