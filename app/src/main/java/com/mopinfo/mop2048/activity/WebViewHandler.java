package com.mopinfo.mop2048.activity;

import android.graphics.Bitmap;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mopinfo.mop2048.log.ILogger;
import com.mopinfo.mop2048.log.LogMannger;

/**
 * Created by feisun on 2016/9/23.
 */
public class WebViewHandler extends WebViewClient {

    private static ILogger LOGGER = LogMannger.getInstance().getLogger(WebViewHandler.class);

    private WebView mWebView;

    public WebViewHandler(WebView webView) {
        this.mWebView = webView;
        LOGGER.info("created");
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        LOGGER.debug("onPageFinished, url=" + url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

    }

    @Override
    public void onReceivedHttpError(
            WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
    }

    public void onTimer() {
        mWebView.loadUrl("about:blank");
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