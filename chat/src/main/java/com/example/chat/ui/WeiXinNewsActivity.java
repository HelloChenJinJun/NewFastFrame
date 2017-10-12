package com.example.chat.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.chat.R;
import com.example.chat.bean.WinXinBean;
import com.example.chat.manager.UserManager;
import com.example.chat.util.CommonUtils;
import com.example.chat.view.fab.FloatingActionButton;
import com.example.chat.view.fab.FloatingActionsMenu;
import com.example.commonlibrary.cusotomview.ToolBarOption;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      10:55
 * QQ:             1981367757
 */

public class WeiXinNewsActivity extends SlideBaseActivity implements View.OnClickListener {
    private FloatingActionsMenu mFloatingActionsMenu;
    private FloatingActionButton browser;
    private FloatingActionButton share;
    private WebView display;
    private ProgressBar mProgressBar;
    private String url;
    private String title;
    private WinXinBean bean;


    @Override
    public void initView() {
        mFloatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.fam_wei_xin_news_menu);
        browser = (FloatingActionButton) findViewById(R.id.fab_wei_xin_news_browser);
        share = (FloatingActionButton) findViewById(R.id.fab_wei_xin_news_share);
        display = (WebView) findViewById(R.id.wv_wei_xin_news_display);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_wei_xin_news_load);
        findViewById(R.id.rl_wei_xin_news_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mFloatingActionsMenu.isExpanded()) {
                    mFloatingActionsMenu.collapse();
                    return true;
                } else {
                    return false;
                }
            }
        });
        browser.setOnClickListener(this);
        share.setOnClickListener(this);
    }


    @Override
    public void initData() {
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        WebSettings webSettings = display.getSettings();
        if (CommonUtils.isNetWorkAvailable()) {
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
        if (getIntent().getSerializableExtra("bean") == null) {
            title = getIntent().getStringExtra("title");
            url = getIntent().getStringExtra("url");
        } else {
            bean = (WinXinBean) getIntent().getSerializableExtra("bean");
            title = bean.getTitle();
            url = bean.getUrl();
        }
        display.loadUrl(url);
    }

    private void initActionBar() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setAvatar(UserManager.getInstance().getCurrentUser().getAvatar());
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setTitle(title);
        setToolBar(toolBarOption);
    }

    @Override
    public void onClick(View v) {
        if (mFloatingActionsMenu.isExpanded()) {
            mFloatingActionsMenu.collapse();
        }
        switch (v.getId()) {
            case R.id.fab_wei_xin_news_share:
                Intent intent = new Intent(this, EditShareMessageActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, bean.getTitle() + "," + bean.getUrl());
                intent.putExtra("share_info", bean);
                intent.putExtra("type", "wei_xin");
                intent.putExtra("destination", "url");
                intent.setType("text/plain");
                startActivity(intent);
                break;
            case R.id.fab_wei_xin_news_browser:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;
        }
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
        return R.layout.activity_wei_xin_news_layout;
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


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    public static void start(Context context, String title, String url) {
        Intent intent = new Intent(context, WeiXinNewsActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    public void updateData(Object o) {

    }
}
