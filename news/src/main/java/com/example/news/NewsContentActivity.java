package com.example.news;

import android.os.Build;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.AppUtil;
import com.example.news.util.NewsUtil;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/17      9:59
 * QQ:             1981367757
 */

public class NewsContentActivity extends BaseActivity{
    private ProgressBar progressBar;
    private WebView display;
    @Override
    public void updateData(Object o) {

    }

    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_news_content;
    }

    @Override
    protected void initView() {
        progressBar= (ProgressBar) findViewById(R.id.pb_activity_news_content_progress);
        display= (WebView) findViewById(R.id.wv_activity_news_content_display);

    }

    @Override
    protected void initData() {
        initWebViewConfig();
        String url=getIntent().getStringExtra(NewsUtil.URL);
        String title=getIntent().getStringExtra(NewsUtil.TITLE);
        ToolBarOption toolBarOption=new ToolBarOption();
        toolBarOption.setTitle(title);
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
        display.loadUrl(url);
    }

    private void initWebViewConfig() {
        WebSettings webSettings = display.getSettings();
        if (AppUtil.isNetworkAvailable(this)) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setAppCachePath(getCacheDir().getAbsolutePath() + "/webViewCache");
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        display.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                }
                return true;
            }
        });
        display.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return super.onJsAlert(view, url, message, result);
            }
        });
    }
}
