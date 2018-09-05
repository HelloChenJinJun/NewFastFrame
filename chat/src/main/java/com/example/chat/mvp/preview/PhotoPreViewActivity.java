package com.example.chat.mvp.preview;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.chat.R;
import com.example.chat.adapter.PhotoPreViewAdapter;
import com.example.chat.base.Constant;
import com.example.chat.bean.ImageItem;
import com.example.chat.events.PhotoPreViewEvent;
import com.example.chat.base.SlideBaseActivity;
import com.example.commonlibrary.cusotomview.WrappedViewPager;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/20     20:16
 * QQ:         1981367757
 */

public class PhotoPreViewActivity extends SlideBaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, Animation.AnimationListener {

    private RelativeLayout topContainer;
    private TextView number;
    private WrappedViewPager display;
    private PhotoPreViewAdapter adapter;
    private int totalSize;
    private boolean[] checkArray;
    private CheckBox checkBox;

    @Override
    public void updateData(Object o) {

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
    protected boolean needStatusPadding() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_photo_preview;
    }

    @Override
    protected void initView() {
        display = (WrappedViewPager) findViewById(R.id.vp_activity_photo_preview_display);
        topContainer = (RelativeLayout) findViewById(R.id.rl_activity_photo_preview_top_container);
        StatusBarUtil.setStatusPadding(topContainer);
        number = (TextView) findViewById(R.id.tv_activity_photo_preview_number);
        checkBox = (CheckBox) findViewById(R.id.cb_activity_photo_preview_check);
        checkBox.setOnCheckedChangeListener(this);
        findViewById(R.id.iv_activity_photo_preview_finish).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        List<ImageItem> list = (List<ImageItem>) getIntent().getSerializableExtra(Constant.IMAGE_PRE_VIEW);
        if (getIntent().getBooleanExtra(Constant.IS_SELECT, false)) {
            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
        }
        checkArray = new boolean[list.size()];
        for (int i = 0; i < checkArray.length; i++) {
            checkArray[i] = list.get(i).isCheck();
        }
        totalSize = list.size();
        adapter = new PhotoPreViewAdapter(this, list);
        adapter.setOnPhotoViewClickListener((view, position) -> {
            Animation topAnimation;
            if (topContainer.getVisibility() == View.VISIBLE) {
                topAnimation = AnimationUtils.loadAnimation(PhotoPreViewActivity.this, R.anim.top_out);
            } else {
                topAnimation = AnimationUtils.loadAnimation(PhotoPreViewActivity.this, R.anim.top_in);
            }
            topAnimation.setAnimationListener(PhotoPreViewActivity.this);
            topContainer.startAnimation(topAnimation);
        });
        display.setAdapter(adapter);
        display.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                number.setText((position + 1) + "/" + totalSize);
                checkBox.setChecked(checkArray[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        int position=getIntent().getIntExtra(Constant.POSITION,0);
        display.setCurrentItem(position);
        number.setText((position + 1) + "/" + totalSize);
//        if (position == 0) {
//            number.setText((position + 1) + "/" + totalSize);
//            checkBox.setChecked(checkArray[position]);
//        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        checkArray[display.getCurrentItem()] = b;
        if (!b) {
            RxBusManager.getInstance().post(new PhotoPreViewEvent(display.getCurrentItem(), PhotoPreViewEvent.TYPE_DELETE, adapter.getData(display.getCurrentItem())));
        } else {
            RxBusManager.getInstance().post(new PhotoPreViewEvent(display.getCurrentItem(), PhotoPreViewEvent.TYPE_ADD, adapter.getData(display.getCurrentItem())));
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_activity_photo_preview_finish) {
            finish();
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (topContainer.getVisibility() == View.VISIBLE) {
            topContainer.setVisibility(View.GONE);
        } else {
            topContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public static void start(Activity activity, int position, ArrayList<ImageItem> imageItemList, boolean isSelect) {
        Intent intent = new Intent(activity, PhotoPreViewActivity.class);
        intent.putExtra(Constant.POSITION, position);
        intent.putExtra(Constant.IMAGE_PRE_VIEW, imageItemList);
        intent.putExtra(Constant.IS_SELECT, isSelect);
        activity.startActivity(intent);
    }
}
