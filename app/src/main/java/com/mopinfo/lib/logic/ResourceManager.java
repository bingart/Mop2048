package com.mopinfo.lib.logic;

import com.mopinfo.lib.common.NutchException;
import com.mopinfo.mop2048.config.ConfigManager;
import com.mopinfo.lib.json.resource.UrlItem;
import com.mopinfo.lib.json.resource.UrlResponse;
import com.mopinfo.lib.log.ILogger;
import com.mopinfo.lib.log.LogMannger;

import java.util.ArrayList;
import java.util.List;

/**
 * Load resource url list from server
 * Created by mop on 2017/6/17.
 */
public class ResourceManager {

    ILogger LOGGER = LogMannger.getInstance().getLogger(ResourceManager.class);

    private static ResourceManager s;

    private String mHost;
    private String mUId;
    private List<UrlItem> mUrlItemList;

    public static ResourceManager getInstance() {
        if (s == null)
            s = new ResourceManager();
        return s;
    }

    private ResourceManager() {
        this.mHost = null;
        this.mUId = null;
        this.mUrlItemList = null;
    }

    public synchronized void setUrlList(List<UrlItem> urlItemList) {
        this.mUrlItemList = new ArrayList<UrlItem>();
        for (UrlItem item : urlItemList) {
            this.mUrlItemList.add(item);
        }
    }

    public void load(String host, String uid) {
        if (this.mHost != null || this.mUId != null) {
            return;
        }

        this.mHost = host;
        this.mUId = uid;

        // Start thread to load url
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    LOGGER.debug("load resource, host=" + mHost);
                    UrlResponse rsp = ResourceHelper.getResource(mHost, mUId);
                    if (rsp != null && rsp.getUrlItemList() != null) {
                        setUrlList(rsp.getUrlItemList());
                        LOGGER.debug("load ok, rsp.urlItemList.size=" + rsp.getUrlItemList().size());
                    }
                } catch (NutchException ex) {
                    LOGGER.error("Load error, ex=" + ex);
                }
            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    public synchronized UrlItem GetNextUrlItem() {
        if (mUrlItemList == null || mUrlItemList.size() == 0) {
            return null;
        }

        UrlItem url = mUrlItemList.get(0);
        mUrlItemList.remove(0);

        return url;
    }
}
