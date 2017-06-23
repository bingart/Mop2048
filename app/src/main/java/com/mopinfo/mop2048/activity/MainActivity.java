package com.mopinfo.mop2048.activity;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.mopinfo.lib.logic.UIDHelper;
import com.mopinfo.lib.logic.VersionManager;
import com.mopinfo.mop2048.R;
import com.mopinfo.mop2048.config.ConfigManager;
import com.mopinfo.lib.logic.ResourceManager;
import com.mopinfo.lib.log.ILogger;
import com.mopinfo.lib.log.LogMannger;
import com.mopinfo.lib.util.UserAgentHelper;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends AppCompatActivity {

    private static ILogger LOGGER = LogMannger.getInstance().getLogger(MainActivity.class);

    private WebView mWebView;
    private WebViewHandler mWebViewHandler;

    private String mUId;

    private Handler mHandler;
    private Runnable mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load configuration
        Context context = getApplicationContext();
        ConfigManager.getInstance().load(context);

        // Check version
        VersionManager.getInstance().open(ConfigManager.getInstance().getAppVersionHost(), this);
        if (!VersionManager.getInstance().isNewVersionFound()) {
            // Server version ignored or local apk file N/A, check server
            VersionManager.getInstance().start();
        }

        // UMeng
        String appKey = ConfigManager.getInstance().getAppKey();
        String channelID = ConfigManager.getInstance().getChannelID();
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this, appKey, channelID);
        MobclickAgent.startWithConfigure(config);

        // Get uid and its userAgent
        mUId = UIDHelper.getUID(this, ConfigManager.getInstance().getUid());
        String userAgent = UserAgentHelper.getUserAgent(mUId);

        // Load resource
        ResourceManager.getInstance().load(
                this, ConfigManager.getInstance().getAppResourceHost(), mUId);

        // Webview
        mWebView = (WebView) findViewById(R.id.webView);
        if (mWebView.getSettings() != null) {
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setUserAgentString(userAgent);
            // mWebView.getSettings().setDomStorageEnabled(true);
        }
        mWebViewHandler = new WebViewHandler(mWebView);
        mWebView.setWebViewClient(mWebViewHandler);
        mWebViewHandler.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        // UMeng
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // UMeng
        MobclickAgent.onPause(this);
    }
}
