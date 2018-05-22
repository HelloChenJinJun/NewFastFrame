package com.example.news.mvp.news.othernew.special;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.animator.DefaultBaseAnimator;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.imageloader.glide.GlideImageLoaderConfig;
import com.example.news.NewsApplication;
import com.example.news.R;
import com.example.news.adapter.SpecialNewsAdapter;
import com.example.news.bean.SpecialNewsBean;
import com.example.news.dagger.news.othernews.special.DaggerSpecialNewsComponent;
import com.example.news.dagger.news.othernews.special.SpecialNewsModule;
import com.example.news.mvp.news.othernew.detail.OtherNewsDetailActivity;
import com.example.news.mvp.news.othernew.photo.OtherNewPhotoSetActivity;
import com.example.news.util.NewsUtil;
import com.example.news.widget.flowlayout.FlowLayout;
import com.example.news.widget.flowlayout.TagAdapter;
import com.example.news.widget.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 *
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/25      18:43
 * QQ:             1981367757
 */

public class SpecialNewsActivity extends BaseActivity<List<SpecialNewsBean>,SpecialNewsPresenter> implements ISpecialNewsView<List<SpecialNewsBean>>,TagFlowLayout.OnTagClickListener {

    private SuperRecyclerView display;
    @Inject
    SpecialNewsAdapter specialNewsAdapter;
    private String specialId;
    private WrappedLinearLayoutManager wrappedLinearLayoutManager;




    @Override
    public void updateData(List<SpecialNewsBean> list) {
        updateTag(list);
        specialNewsAdapter.addData(list);
    }

    private void updateTag(List<SpecialNewsBean> list) {

        if (list != null && list.size() > 0) {
            if (list.size()==1)return;
            List<String>  list1=new ArrayList<>();
            for (SpecialNewsBean bean :
                    list) {
                if (bean.getItemViewType() == SpecialNewsBean.TYPE_HEADER) {
                    list1.add(bean.getTitle());
                }
            }
            tagFlowLayout.setAdapter(new TagAdapter<String>(list1) {
                @Override
                public View getView(FlowLayout parent, int position, String o) {
                    TextView textView= (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_tag_flow_layout_item,null);
                    textView.setText(o);
                    return textView;
                }
            });
            tagFlowLayout.setOnTagClickListener(this);
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
        return R.layout.activity_special_news;
    }

    @Override
    protected void initView() {
            display= (SuperRecyclerView) findViewById(R.id.srcv_activity_special_news_display);
    }

    @Override
    protected void initData() {
        DaggerSpecialNewsComponent.builder()
                .specialNewsModule(new SpecialNewsModule(this))
                .newsComponent(NewsApplication.getNewsComponent())
                .build().inject(this);
        specialId=getIntent().getStringExtra(NewsUtil.SPECIAL_ID);
        display.setLayoutManager(wrappedLinearLayoutManager=new WrappedLinearLayoutManager(this));
        display.addHeaderView(getHeaderView());
        display.setItemAnimator(new DefaultBaseAnimator());
        display.setAdapter(specialNewsAdapter);
        specialNewsAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                SpecialNewsBean bean=specialNewsAdapter.getData(position);
                if (NewsUtil.PHOTO_SET.equals(bean.getBean().getSkipType())) {
                    OtherNewPhotoSetActivity.start(SpecialNewsActivity.this,bean.getBean().getSkipID());
                }else {
                    OtherNewsDetailActivity.start(view.getContext(),bean.getBean().getPostid());
                }
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenter.getSpecialNewsData(specialId);
            }
        });
        ToolBarOption toolBarOption=new ToolBarOption();
        toolBarOption.setTitle(getIntent().getStringExtra(NewsUtil.TITLE));
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }

    private ImageView banner;
    private TagFlowLayout tagFlowLayout;
    private View getHeaderView() {
        View headerView= LayoutInflater.from(this)
                .inflate(R.layout.view_activity_special_news_header,null);
        banner= (ImageView) headerView.findViewById(R.id.iv_view_activity_special_news_header_banner);
        tagFlowLayout= (TagFlowLayout) headerView.findViewById(R.id.tfl_view_activity_special_news_header_flow);
        return headerView;
    }

    public static void start(Context context, String skipID, String title) {
        Intent intent=new Intent(context,SpecialNewsActivity.class);
        intent.putExtra(NewsUtil.SPECIAL_ID,skipID);
        intent.putExtra(NewsUtil.TITLE,title);
        context.startActivity(intent);
    }


    @Override
    public void updateBanner(String url) {
        BaseApplication
                .getAppComponent()
                .getImageLoader()
                .loadImage(this,new GlideImageLoaderConfig.Builder()
                .imageView(banner)
                .url(url).centerInside().cacheStrategy(GlideImageLoaderConfig.CACHE_RESULT)
                .build());
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
       String title=((TextView) ((ViewGroup)view).getChildAt(0)).getText().toString().trim();
        int adapterPosition=specialNewsAdapter.getPositionFromTitle(title);
        wrappedLinearLayoutManager.scrollToPositionWithOffset(adapterPosition,0);
        return true;
    }
}
