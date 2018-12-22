package com.example.video.mvp.news.othernew.detail;

import android.content.Context;
import android.content.Intent;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.video.NewsApplication;
import com.example.video.R;
import com.example.video.bean.OtherNewsDetailBean;
import com.example.video.dagger.news.othernews.detail.DaggerOtherNewsDetailComponent;
import com.example.video.dagger.news.othernews.detail.OtherNewsDetailModule;
import com.example.video.util.NewsUtil;
import com.example.video.widget.rich.RichText;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/26      15:27
 * QQ:             1981367757
 */

public class OtherNewsDetailActivity extends BaseActivity<OtherNewsDetailBean, OtherNewsDetailPresenter> {
    private RichText content;


    @Override
    public void updateData(OtherNewsDetailBean otherNewsDetailBean) {
        if (otherNewsDetailBean != null) {
            updateTitle(otherNewsDetailBean.getTitle());
            content.setRichText(otherNewsDetailBean.getBody());
        }
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_other_news_detail;
    }

    @Override
    protected void initView() {
        content = findViewById(R.id.tv_activity_other_news_detail_content);
    }

    @Override
    protected void initData() {
        DaggerOtherNewsDetailComponent.builder()
                .otherNewsDetailModule(new OtherNewsDetailModule(this))
                .newsComponent(NewsApplication.getNewsComponent())
                .build().inject(this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenter.getOtherNewsDetailData(getIntent().getStringExtra(NewsUtil.POST_ID));
            }
        });
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("详情");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }

    public static void start(Context context, String postid) {
        Intent intent = new Intent(context, OtherNewsDetailActivity.class);
        intent.putExtra(NewsUtil.POST_ID, postid);
        context.startActivity(intent);
    }
}
