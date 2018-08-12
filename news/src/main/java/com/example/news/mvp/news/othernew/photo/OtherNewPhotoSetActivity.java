package com.example.news.mvp.news.othernew.photo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.commonlibrary.BaseActivity;
import com.example.news.NewsApplication;
import com.example.news.R;
import com.example.news.adapter.OtherNewPhotoSetAdapter;
import com.example.news.bean.PhotoSetBean;
import com.example.news.dagger.news.othernews.photo.DaggerOtherNewPhotoSetComponent;
import com.example.news.dagger.news.othernews.photo.OtherNewPhotoSetModule;
import com.example.news.util.NewsUtil;
import com.example.news.widget.WrappedViewPager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/26      16:39
 * QQ:             1981367757
 */

public class OtherNewPhotoSetActivity extends BaseActivity<PhotoSetBean, OtherNewPhotoSetPresenter> implements OtherNewPhotoSetAdapter.OnItemClickListener {
    private ImageView back;
    private TextView index;
    private LinearLayout bottomContainer;
    private TextView title, content;
    private WrappedViewPager display;
    private List<PhotoSetBean.PhotosEntity> photoSet;

    private boolean isHide=false;

    @Inject
    OtherNewPhotoSetAdapter otherNewPhotoSetAdapter;

    @Override
    protected boolean needStatusPadding() {
        return false;
    }

    @Override
    public void updateData(PhotoSetBean photoSetBean) {
        if (photoSetBean != null && photoSetBean.getPhotos() != null && photoSetBean.getPhotos().size() > 0) {
            photoSet = photoSetBean.getPhotos();
            List<String> imageList = new ArrayList<>();
            for (PhotoSetBean.PhotosEntity item :
                    photoSetBean.getPhotos()) {
                imageList.add(item.getImgurl());
            }
            otherNewPhotoSetAdapter.setImageList(imageList);
            otherNewPhotoSetAdapter.setOnItemClickListener(this);
            display.setAdapter(otherNewPhotoSetAdapter);
            display.setCurrentItem(0);
            content.setText(photoSetBean.getPhotos().get(0).getNote());
            title.setText(photoSetBean.getSetname());
            index.setText("1/" + photoSetBean.getPhotos().size());
        }
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_other_new_photo_set;
    }

    @Override
    protected void initView() {
        display = (WrappedViewPager) findViewById(R.id.wvp_activity_other_new_photo_set_display);
        back = (ImageView) findViewById(R.id.iv_activity_other_new_photo_set_back);
        index = (TextView) findViewById(R.id.tv_activity_other_new_photo_index);
        bottomContainer = (LinearLayout) findViewById(R.id.ll_activity_other_new_photo_set_bottom);
        title = (TextView) findViewById(R.id.tv_activity_other_new_photo_set_title);
        content = (TextView) findViewById(R.id.tv_activity_other_new_photo_set_content);
    }


    @Override
    protected void initData() {
        DaggerOtherNewPhotoSetComponent.builder().otherNewPhotoSetModule(new OtherNewPhotoSetModule(this))
                .newsComponent(NewsApplication.getNewsComponent())
                .build().inject(this);
        display.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                index.setText((position + 1) + "/" +photoSet.size());
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenter.getOtherNewPhotoSetData(getIntent().getStringExtra(NewsUtil.PHOTO_SET_ID));
            }
        });
    }


    public static void start(Context context, String id) {
        Intent intent = new Intent(context, OtherNewPhotoSetActivity.class);
        intent.putExtra(NewsUtil.PHOTO_SET_ID, id);
        context.startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {
        isHide=!isHide;
        if (isHide) {
            back.animate().translationY(-back.getBottom()).setDuration(300).start();
            index.animate().translationY(-index.getBottom()).setDuration(300).start();
            bottomContainer.animate().translationY(bottomContainer.getHeight()).setDuration(300).start();
        }else {
            back.animate().translationY(0).setDuration(300).start();
            index.animate().translationY(0).setDuration(300).start();
            bottomContainer.animate().translationY(0).setDuration(300).start();
        }
    }
}
