package com.mopinfo.lib.json.resource;

import java.util.List;

/**
 * Created by mop on 2017/6/17.
 */
public class UrlResponse extends UrlRequest {

    private String errorCode;

    private List<UrlItem> urlItemList;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<UrlItem> getUrlItemList() {
        return urlItemList;
    }

    public void setUrlItemList(List<UrlItem> urlItemList) {
        this.urlItemList = urlItemList;
    }
}
