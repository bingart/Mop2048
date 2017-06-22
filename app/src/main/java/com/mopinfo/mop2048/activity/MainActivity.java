package com.mopinfo.mop2048.activity;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.webkit.WebView;

import com.mopinfo.lib.logic.UIDHelper;
import com.mopinfo.mop2048.R;
import com.mopinfo.mop2048.config.ConfigManager;
import com.mopinfo.lib.logic.ResourceManager;
import com.mopinfo.lib.log.ILogger;
import com.mopinfo.lib.log.LogMannger;
import com.mopinfo.lib.util.DateTimeHelper;
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
        Context mContext = getApplicationContext();
        ConfigManager.getInstance();
        ConfigManager.getInstance().load(mContext);

        // Get uid and its userAgent
        mUId = UIDHelper.getUID(this, ConfigManager.getInstance().getUid());
        String userAgent = UserAgentHelper.getUserAgent(mUId);

        // Load resource
        ResourceManager.getInstance().load(
                ConfigManager.getInstance().getHost(),
                ConfigManager.getInstance().getUid());

        // Prepare webview
        mWebView = (WebView) findViewById(R.id.webView);
        if (mWebView.getSettings() != null) {
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setUserAgentString(userAgent);
            // mWebView.getSettings().setDomStorageEnabled(true);
        }
        mWebViewHandler = new WebViewHandler(mWebView);
        mWebView.setWebViewClient(mWebViewHandler);
        mWebViewHandler.start();

        String appKey = "57ea47b267e58e5c2a00074d";
        String channelID = "DemoUChannelID";
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this, appKey, channelID);
        MobclickAgent.startWithConfigure(config);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
