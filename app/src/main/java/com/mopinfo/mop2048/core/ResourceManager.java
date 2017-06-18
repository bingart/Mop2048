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
 * Created by feisun on 2017/6/17.
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

    public void Load() {
        // Start thread to load url
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    UrlResponse rsp = ResourceHelper.getResource(
                            ConfigManager.getInstance().getHost(),
                            ConfigManager.getInstance().getUid());
                    setUrlList(rsp.getUrlItemList());
                } catch (NutchException ex) {
                    LOGGER.error("Load error, ex=" + ex);
                }
            }
        };
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
