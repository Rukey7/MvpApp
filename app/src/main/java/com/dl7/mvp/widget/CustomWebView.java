package com.dl7.mvp.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Message;
import android.text.Html;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.dl7.mvp.R;


/**
 * Created by long on 2016/1/5.
 * Web控件
 */
public class CustomWebView extends WebView {

    private ProgressBar mProgressBar;
    private OnWebViewListener mWebListener;


    public CustomWebView(Context context) {
        this(context, null);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(Context context) {
        // 顶部显示的进度条
        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 7, 0, 0));
        Drawable drawable = context.getResources().getDrawable(R.drawable.layer_web_progress_bar);
        mProgressBar.setProgressDrawable(drawable);
        addView(mProgressBar);

        WebSettings webSettings = this.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);   // 是能放大缩小
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);//隐藏
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        //webSettings.setUseWideViewPort(true);
        this.setWebViewClient(mWebViewClientBase);
        this.setWebChromeClient(mWebChromeClientBase);
        setDownloadListener(new DownloadListener());
        this.onResume();
    }

    /**
     * 加载HTML数据
     * @param htmlData
     */
    public void loadHtmlData(String htmlData) {
        String data = Html.fromHtml(htmlData).toString();
        loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
    }

    public void loadHtmlString(String htmlStr) {
        loadData(htmlStr, "text/html; charset=UTF-8", null);
    }

    private WebViewClientBase mWebViewClientBase = new WebViewClientBase();

    private class WebViewClientBase extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if( url.startsWith("http:") || url.startsWith("https:") ) {
                return false;
            }
            // Otherwise allow the OS to handle things like tel, mailto, etc.
            view.stopLoading();
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (mWebListener != null) {
                mWebListener.onError();
            }
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
        }
    }

    private WebChromeClientBase mWebChromeClientBase = new WebChromeClientBase();

    private class WebChromeClientBase extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (mWebListener != null) {
                mWebListener.onProgressChanged(newProgress);
            }
            if (newProgress == 100) {
                mProgressBar.setVisibility(GONE);
            } else {
                if (mProgressBar.getVisibility() == GONE)
                    mProgressBar.setVisibility(VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
            super.onReceivedTouchIconUrl(view, url, precomposed);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }
    }

    public class DownloadListener implements android.webkit.DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            getContext().startActivity(intent);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) mProgressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        mProgressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setWebListener(OnWebViewListener webListener) {
        mWebListener = webListener;
    }

    public interface OnWebViewListener {
        void onProgressChanged(int progress);
        void onError();
    }
}
