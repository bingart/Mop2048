package com.mopinfo.mop2048.activity;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.mopinfo.mop2048.R;
import com.mopinfo.mop2048.config.ConfigManager;
import com.mopinfo.mop2048.core.ResourceManager;
import com.mopinfo.mop2048.log.ILogger;
import com.mopinfo.mop2048.log.LogMannger;

public class MainActivity extends AppCompatActivity {

    private static ILogger LOGGER = LogMannger.getInstance().getLogger(MainActivity.class);

    private Context mContext;

    private WebView mWebView;
    private WebViewHandler mWebViewHandler;

    private Handler mHandler;
    private Runnable mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load configuration
        mContext = getApplicationContext();
        ConfigManager.getInstance();
        ConfigManager.getInstance().load(mContext);

        // Load resource
        ResourceManager.getInstance().load();

        // Prepare webview
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        // mWebView.getSettings().setDomStorageEnabled(true);
        mWebViewHandler = new WebViewHandler(mWebView);
        mWebView.setWebViewClient(mWebViewHandler);

        // Prepare timer
        mHandler = new Handler();
        mTask = new Runnable() {
            @Override
            public void run() {
                // Next invoke
                mHandler.postDelayed(this, 1000);
                // Trigger
                mWebViewHandler.onTimer();
            }
        };

        mHandler.postDelayed(mTask, 1000);
    }
}
