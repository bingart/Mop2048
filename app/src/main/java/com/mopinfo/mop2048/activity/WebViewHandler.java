package com.mopinfo.mop2048.activity;

import android.os.Handler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mopinfo.lib.logic.ResourceManager;
import com.mopinfo.lib.json.resource.UrlItem;
import com.mopinfo.lib.log.ILogger;
import com.mopinfo.lib.log.LogMannger;

/**
 * Self runnable web view handler.
 * Created by feisun on 2016/9/23.
 */
public class WebViewHandler extends WebViewClient {

    private static ILogger LOGGER = LogMannger.getInstance().getLogger(WebViewHandler.class);

    private Handler mHandler;
    private Runnable mTask;

    private WebView mWebView;
    private WebViewState mState;
    private int mLoadTime;
    private int mLoadExpiredTime;

    public WebViewHandler(WebView webView) {

        // Prepare timer
        mHandler = new Handler();
        mTask = new Runnable() {
            @Override
            public void run() {
                // Next invoke
                mHandler.postDelayed(this, 1000);
                // Trigger
                onTimer();
            }
        };

        this.mWebView = webView;
        this.mState = WebViewState.IDLE;
        this.mLoadTime = 0;
        this.mLoadExpiredTime = 30;
        LOGGER.info("created");
    }

    public void start() {
        mHandler.postDelayed(mTask, 1000);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        LOGGER.debug("onPageFinished, url=" + url);
        // Continue wait for read page, until expired
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        LOGGER.debug("onReceivedError, url=" + request);
        this.mState = WebViewState.IDLE;
    }

    @Override
    public void onReceivedHttpError(
            WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        LOGGER.debug("onReceivedHttpError, url=" + request);
        this.mState = WebViewState.IDLE;
    }

    public void onTimer() {
        if (this.mState == WebViewState.IDLE) {
            UrlItem urlItem = ResourceManager.getInstance().GetNextUrlItem();
            if (urlItem != null) {
                mLoadExpiredTime = urlItem.getLoadExpiredTime();
                mLoadTime = 0;
                mWebView.loadUrl(urlItem.getUrl());
                this.mState = WebViewState.LOAD;
            } else {
                mWebView.loadUrl("about:blank");
            }
        } else if (this.mState == WebViewState.LOAD){
            this.mLoadTime++;
            if (this.mLoadTime >= mLoadExpiredTime) {
                this.mState = WebViewState.IDLE;
            }
        } else {
            this.mState = WebViewState.IDLE;
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // return true; //Indicates WebView to NOT load the url;
        return false; //Allow WebView to load url
    }

    @Override
    public String toString() {
        return "";
    }
}