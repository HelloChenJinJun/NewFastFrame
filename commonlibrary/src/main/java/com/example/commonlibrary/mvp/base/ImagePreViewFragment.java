package com.example.commonlibrary.mvp.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import com.bumptech.glide.Glide;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.R;
import com.example.commonlibrary.utils.Constant;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/24     18:15
 */
public class ImagePreViewFragment extends BaseFragment {
    private PhotoView display;

    public static ImagePreViewFragment newInstance(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.DATA, url);
        ImagePreViewFragment imagePreViewFragment = new ImagePreViewFragment();
        imagePreViewFragment.setArguments(bundle);
        return imagePreViewFragment;
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_image_preview;
    }

    @Override
    protected void initView() {
        display = (PhotoView) findViewById(R.id.pv_fragment_image_preview_display);
        display.setOnPhotoTapListener((view1, x, y) -> {
            getActivity().onBackPressed();
        });
        display.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                display.getViewTreeObserver().removeOnPreDrawListener(this);
                getActivity().supportStartPostponedEnterTransition();
                return true;
            }
        });
    }


    private String url;

    @Override
    protected void initData() {
        url = getArguments().getString(Constant.DATA);
        Glide.with(this).load(url).into(display);
    }

    @Override
    protected void updateView() {

    }

    @Override
    public void updateData(Object o) {

    }

    public View getSharedElement() {
        return display;
    }
}
