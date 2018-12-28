package com.example.video.mvp.news.othernew.detail;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.customview.ToolBarOption;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.utils.Constant;
import com.example.video.NewsApplication;
import com.example.video.R;
import com.example.video.bean.OtherNewsDetailBean;
import com.example.video.dagger.news.othernews.detail.DaggerOtherNewsDetailComponent;
import com.example.video.dagger.news.othernews.detail.OtherNewsDetailModule;
import com.example.video.util.NewsUtil;
import com.example.video.widget.rich.RichText;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/26      15:27
 * QQ:             1981367757
 */

public class OtherNewsDetailActivity extends BaseActivity<OtherNewsDetailBean, OtherNewsDetailPresenter> {
    private RichText content;
    private String url;
    private View view;


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
        content.setOnRichTextImageClickListener((view, imageUrls, position) -> {
            if (imageUrls != null && imageUrls.size() > 0) {
                //                OtherNewsDetailActivity.this.view = view;
                //                url = imageUrls.get(position);
                //                ImagePreViewActivity.start(OtherNewsDetailActivity.this, new ArrayList<>(Collections.singletonList(url)), 0, view, NewsUtil.NEWS_DETAIL_FLAG);
                Map<String, Object> map = new HashMap<>();
                map.put(Constant.POSITION, position);
                Router.getInstance().deal(new RouterRequest.Builder()
                        .provideName("chat").actionName("preview")
                        .context(OtherNewsDetailActivity.this)
                        .paramMap(map).object(imageUrls).build());
            }
        });

        //        setExitSharedElementCallback(new SharedElementCallback() {
        //            @Override
        //            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
        //                sharedElements.clear();
        //                sharedElements.put(url, view);
        //            }
        //        });
        //        presenter.registerEvent(PhotoPreEvent.class, photoPreEvent -> {
        //            if (photoPreEvent.getFlag() == NewsUtil.NEWS_DETAIL_FLAG) {
        //                index = photoPreEvent.getIndex();
        //            }
        //        });
    }

    @Override
    protected void initData() {
        DaggerOtherNewsDetailComponent.builder()
                .otherNewsDetailModule(new OtherNewsDetailModule(this))
                .newsComponent(NewsApplication.getNewsComponent())
                .build().inject(this);
        runOnUiThread(() -> presenter.getOtherNewsDetailData(getIntent().getStringExtra(NewsUtil.POST_ID)));
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
