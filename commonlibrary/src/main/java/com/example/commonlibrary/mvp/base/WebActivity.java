package com.example.commonlibrary.mvp.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.commonlibrary.R;
import com.example.commonlibrary.SlideBaseActivity;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.Constant;

import androidx.annotation.RequiresApi;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      10:55
 * QQ:             1981367757
 */

public class WebActivity extends SlideBaseActivity implements View.OnClickListener {
    private WebView display;
    private ProgressBar mProgressBar;

    @Override
    public void initView() {
        mProgressBar = findViewById(R.id.pb_activity_web_bar);
        display = findViewById(R.id.wv_activity_web_display);
    }


    @Override
    public void initData() {
        WebSettings webSettings = display.getSettings();
        if (AppUtil.isNetworkAvailable()) {
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
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });
        display.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    if (mProgressBar.getVisibility() == View.GONE) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }


            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return super.onJsAlert(view, url, message, result);
            }
        });
        initActionBar();
        String content = getIntent().getStringExtra(Constant.DATA);
        if (content.startsWith("http")) {
            display.loadUrl(content);
        } else {
            display.loadDataWithBaseURL("", content, "text/html", "UTF-8", "");
        }
    }

    private void initActionBar() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setTitle(getIntent().getStringExtra(Constant.TITLE));
        setToolBar(toolBarOption);
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        display.onResume();
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
        return R.layout.activity_web;
    }

    @Override
    protected void onPause() {
        super.onPause();
        display.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (display != null) {
            ((ViewGroup) display.getParent()).removeView(display);
            display.destroy();
            display = null;
        }
    }


    public static void start(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, WebActivity.class);
        intent.putExtra(Constant.TITLE, title);
        intent.putExtra(Constant.DATA, url);
        activity.startActivity(intent);
    }

    @Override
    public void updateData(Object o) {

    }
}
