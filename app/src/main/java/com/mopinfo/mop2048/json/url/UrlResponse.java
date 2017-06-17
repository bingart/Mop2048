package com.mopinfo.mop2048.json.url;

import java.util.List;

/**
 * Created by mop on 2017/6/17.
 */
public class UrlResponse extends UrlRequest {

    private List<String> urlList;

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }
}
