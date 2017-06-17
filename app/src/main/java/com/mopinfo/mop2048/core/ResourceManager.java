package com.mopinfo.mop2048.core;

import com.mopinfo.mop2048.common.NutchException;
import com.mopinfo.mop2048.config.ConfigManager;
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

    private List<String> mUrlList;

    public static ResourceManager getInstance() {
        if (s == null)
            s = new ResourceManager();
        return s;
    }

    private ResourceManager() {
        this.mUrlList = null;
    }

    public synchronized void setUrlList(List<String> urlList) {
        this.mUrlList = new ArrayList<String>();
        for (String url : urlList) {
            this.mUrlList.add(url);
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
                    setUrlList(rsp.getUrlList());
                } catch (NutchException ex) {
                    LOGGER.error("Load error, ex=" + ex);
                }
            }
        };
    }

    public synchronized String GetNextUrl() {
        if (mUrlList == null || mUrlList.size() == 0) {
            return null;
        }

        String url = mUrlList.get(0);
        mUrlList.remove(0);

        return url;
    }
}
