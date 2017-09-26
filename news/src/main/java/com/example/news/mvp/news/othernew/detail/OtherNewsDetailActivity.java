package com.example.news.mvp.news.othernew.detail;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.news.NewsApplication;
import com.example.news.R;
import com.example.news.bean.OtherNewsDetailBean;
import com.example.news.dagger.news.othernews.detail.DaggerOtherNewsDetailComponent;
import com.example.news.dagger.news.othernews.detail.OtherNewsDetailModule;
import com.example.news.util.NewsUtil;
import com.zzhoujay.richtext.RichText;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/26      15:27
 * QQ:             1981367757
 */

public class OtherNewsDetailActivity extends BaseActivity<OtherNewsDetailBean,OtherNewsDetailPresenter>{
    private TextView title;
    private TextView content;



    @Override
    public void updateData(OtherNewsDetailBean otherNewsDetailBean) {
        if (otherNewsDetailBean != null) {
            title.setText(otherNewsDetailBean.getTitle());
            RichText.from(otherNewsDetailBean.getBody())
                    .into(content);
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
        title= (TextView) findViewById(R.id.tv_activity_other_news_detail_title);
        content= (TextView) findViewById(R.id.tv_activity_other_news_detail_content);
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
        ToolBarOption toolBarOption=new ToolBarOption();
        toolBarOption.setTitle("详情");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }

    public static void start(Context context, String postid) {
        Intent intent=new Intent(context,OtherNewsDetailActivity.class);
        intent.putExtra(NewsUtil.POST_ID,postid);
        context.startActivity(intent);
    }
}
