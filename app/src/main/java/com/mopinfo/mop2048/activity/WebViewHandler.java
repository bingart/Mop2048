package com.mopinfo.mop2048.activity;

import android.graphics.Bitmap;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mopinfo.mop2048.core.ResourceManager;
import com.mopinfo.mop2048.json.url.UrlItem;
import com.mopinfo.mop2048.log.ILogger;
import com.mopinfo.mop2048.log.LogMannger;

/**
 * Created by feisun on 2016/9/23.
 */
public class WebViewHandler extends WebViewClient {

    private static ILogger LOGGER = LogMannger.getInstance().getLogger(WebViewHandler.class);

    private WebView mWebView;
    private WebViewState mState;
    private int mLoadTime;
    private int mLoadExpiredTime;

    public WebViewHandler(WebView webView) {
        this.mWebView = webView;
        this.mState = WebViewState.IDLE;
        this.mLoadTime = 0;
        this.mLoadExpiredTime = 30;
        LOGGER.info("created");
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        LOGGER.debug("onPageFinished, url=" + url);
        this.mState = WebViewState.IDLE;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        LOGGER.debug("onReceivedError, url=" + request);
        this.mState = WebViewState.IDLE;
    }

    @Override
    public void onReceivedHttpError(
            WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
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