package com.mopinfo.mop2048.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.mopinfo.mop2048.R;
import com.mopinfo.mop2048.core.ResourceManager;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private WebViewHandler mWebViewHandler;

    private Handler nHandler = new Handler();
    private Runnable mTask = new Runnable() {
        public void run() {
            // Next invoke
            nHandler.postDelayed(this, 1000);
            // Trigger
            mWebViewHandler.onTimer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load resource
        ResourceManager.getInstance().Load();

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        // mWebView.getSettings().setDomStorageEnabled(true);
        mWebViewHandler = new WebViewHandler(mWebView);
        mWebView.setWebViewClient(mWebViewHandler);

    }
}
