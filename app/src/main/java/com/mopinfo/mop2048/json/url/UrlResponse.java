package com.mopinfo.mop2048.json.url;

import java.util.List;

/**
 * Created by mop on 2017/6/17.
 */
public class UrlResponse extends UrlRequest {

    private List<UrlItem> urlItemList;

    public List<UrlItem> getUrlItemList() {
        return urlItemList;
    }

    public void setUrlItemList(List<UrlItem> urlItemList) {
        this.urlItemList = urlItemList;
    }
}
