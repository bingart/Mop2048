package com.mopinfo.mop2048.core;

import com.mopinfo.mop2048.common.NutchException;
import com.mopinfo.mop2048.config.ConfigManager;
import com.mopinfo.mop2048.json.url.UrlItem;
import com.mopinfo.mop2048.json.url.UrlResponse;
import com.mopinfo.mop2048.log.ILogger;
import com.mopinfo.mop2048.log.LogMannger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mop on 2017/6/17.
 */
public class ResourceManager {

    ILogger LOGGER = LogMannger.getInstance().getLogger(ResourceManager.class);

    private static ResourceManager s;

    private List<UrlItem> mUrlItemList;

    public static ResourceManager getInstance() {
        if (s == null)
            s = new ResourceManager();
        return s;
    }

    private ResourceManager() {
        this.mUrlItemList = null;
    }

    public synchronized void setUrlList(List<UrlItem> urlItemList) {
        this.mUrlItemList = new ArrayList<UrlItem>();
        for (UrlItem item : urlItemList) {
            this.mUrlItemList.add(item);
        }
    }

    public void load() {
        // Start thread to load url
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    String host = ConfigManager.getInstance().getHost();
                    String uid = ConfigManager.getInstance().getUid();
                    LOGGER.debug("load resource, host=" + host);
                    UrlResponse rsp = ResourceHelper.getResource(host, uid);
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
