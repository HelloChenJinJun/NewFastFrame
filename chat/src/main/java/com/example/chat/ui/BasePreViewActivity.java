package com.example.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.adapter.ImagePageAdapter;
import com.example.chat.base.CommonImageLoader;
import com.example.chat.bean.ImageItem;
import com.example.chat.util.LogUtil;
import com.example.chat.view.PreviewViewPager;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/29      15:52
 * QQ:             1981367757
 */

public class BasePreViewActivity extends SlideBaseActivity implements View.OnClickListener, Animation.AnimationListener {

        /**
         * 现在预览的图片位置
         */
        protected int currentPosition;


        /**
         * 头部布局
         */
        protected RelativeLayout topBar;


        /**
         * 底部布局
         */
        protected RelativeLayout bottomView;


        /**
         * 已选择的图片信息
         */
        protected List<ImageItem> selectedList = new ArrayList<>();


        /**
         * 传递过来需要预览的图片信息
         */
        protected ArrayList<ImageItem> previewList = new ArrayList<>();


        protected PreviewViewPager display;


        protected ImagePageAdapter mImagePageAdapter;


        protected ImageView back;
        protected Button finish;
        protected ImageView delete;
        protected TextView description;
        private String from;

        protected CheckBox origin;
        protected CheckBox select;


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
                return R.layout.base_pre_view;
        }


        @Override
        public void initView() {
                back = (ImageView) findViewById(R.id.iv_picture_top_bar_back);
                description = (TextView) findViewById(R.id.tv_picture_top_bar_description);
                finish = (Button) findViewById(R.id.btn_picture_top_bar_finish);
                delete = (ImageView) findViewById(R.id.iv_picture_top_bar_delete);
                topBar = (RelativeLayout) findViewById(R.id.picture_top_bar);
                bottomView = (RelativeLayout) findViewById(R.id.rl_base_preview_bottom);
                origin = (CheckBox) findViewById(R.id.cb_base_preview_origin);
                select = (CheckBox) findViewById(R.id.cb_base_preview_select);
                display = (PreviewViewPager) findViewById(R.id.vp_base_preview_display);
                back.setOnClickListener(this);
                delete.setOnClickListener(this);
                select.setOnClickListener(this);
                finish.setOnClickListener(this);
                origin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                        origin.setText(getString(R.string.origin_size, previewList.get(currentPosition).getSize()));
                                } else {
                                        origin.setText(getString(R.string.origin_text));
                                }
                        }
                });
        }


        @Override
        public void initData() {
                LogUtil.e("preViewinitData");
                selectedList = CommonImageLoader.getInstance().getSelectedImages();
                currentPosition = getIntent().getIntExtra(CommonImageLoader.CURRENT_POSITION, 0);
                from = getIntent().getStringExtra(CommonImageLoader.PREVIEW_FROM);
                if (from.equals(CommonImageLoader.PREVIEW_DELETE)) {
                        LogUtil.e("可删除的预览");
                        finish.setVisibility(View.GONE);
                        delete.setVisibility(View.VISIBLE);
                        bottomView.setVisibility(View.GONE);
                        mImagePageAdapter = new ImagePageAdapter(this, selectedList);
                        description.setText(getString(R.string.preview_image_count, currentPosition + 1, selectedList.size()));
                } else if (from.equals(CommonImageLoader.PREVIEW_SELECT)) {
                        previewList = CommonImageLoader.getInstance().getCurrentImageFolder().getAllImages();
//                        isOrigin = getIntent().getBooleanExtra(CommonImageLoader.IS_ORIGIN, false);
                        LogUtil.e("可选择的预览");
                        finish.setVisibility(View.VISIBLE);
                        delete.setVisibility(View.GONE);
                        bottomView.setVisibility(View.VISIBLE);
                        notifyTextChanged();
                        mImagePageAdapter = new ImagePageAdapter(this, previewList);
                        description.setText(getString(R.string.preview_image_count, currentPosition + 1, previewList.size()));
                } else {
                        finish.setVisibility(View.GONE);
                        delete.setVisibility(View.GONE);
                        bottomView.setVisibility(View.GONE);
                        mImagePageAdapter = new ImagePageAdapter(this, selectedList);
                        description.setText(getString(R.string.preview_image_count, currentPosition + 1, selectedList.size()));
                }
                mImagePageAdapter.setOnPhotoViewClickListener(new ImagePageAdapter.OnPhotoViewClickListener() {
                        @Override
                        public void onPhotoViewClick(View view, int position) {
                                LogUtil.e("点击图片拉" + position);
                                Animation topAnimation;
                                if (topBar.getVisibility() == View.VISIBLE) {
                                        topAnimation = AnimationUtils.loadAnimation(BasePreViewActivity.this, R.anim.top_out);
                                } else {
                                        topAnimation = AnimationUtils.loadAnimation(BasePreViewActivity.this, R.anim.top_in);
                                }
                                topAnimation.setAnimationListener(BasePreViewActivity.this);
                                topBar.startAnimation(topAnimation);
                                if (from.equals(CommonImageLoader.PREVIEW_SELECT)) {
                                        Animation bottomAnimation;
                                        if (bottomView.getVisibility() == View.VISIBLE) {
                                                bottomAnimation = AnimationUtils.loadAnimation(BasePreViewActivity.this, R.anim.bottom_out);
                                        } else {
                                                bottomAnimation = AnimationUtils.loadAnimation(BasePreViewActivity.this, R.anim.bottom_in);
                                        }
                                        bottomAnimation.setAnimationListener(BasePreViewActivity.this);
                                        bottomView.startAnimation(bottomAnimation);
                                }
                        }
                });
                display.setAdapter(mImagePageAdapter);
                display.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                        @Override
                        public void onPageSelected(int position) {
                                LogUtil.e("滚动");
                                currentPosition = position;
                                if (from.equals(CommonImageLoader.PREVIEW_SELECT)) {
                                        description.setText(getString(R.string.preview_image_count, position + 1, previewList.size()));
                                        if (selectedList.contains(previewList.get(position))) {
                                                select.setChecked(true);
                                        } else {
                                                select.setChecked(false);
                                        }
                                } else {
                                        description.setText(getString(R.string.preview_image_count, position + 1, selectedList.size()));
                                }
                        }
                });
                display.setCurrentItem(currentPosition, false);
        }


        private void notifyTextChanged() {
                LogUtil.e("数据更新");
                LogUtil.e("currentPosition" + currentPosition);
                LogUtil.e("已选择的大小" + selectedList.size());
                if (selectedList.contains(previewList.get(currentPosition))) {

                        LogUtil.e("数据更新true");
                        select.setChecked(true);
                } else {
                        select.setChecked(false);
                        LogUtil.e("数据更新false");
                }
                if (selectedList.size() > 0) {
                        finish.setEnabled(true);
                        finish.setText(getString(R.string.finish_count, selectedList.size(), CommonImageLoader.getInstance().getMaxSelectedCount()));
                } else {
                        finish.setText(getString(R.string.finish));
                        finish.setEnabled(false);
                }
        }

        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                        case R.id.iv_picture_top_bar_back:
                                LogUtil.e("点击返回键");
                                finish();
                                break;
                        case R.id.btn_picture_top_bar_finish:
                                LogUtil.e("点击完成键");
                                finish();
                                break;
                        case R.id.iv_picture_top_bar_delete:
                                LogUtil.e("点击删除键");
                                showBaseDialog("提示", "确定要删除该照片吗?", "取消", "确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                                cancelBaseDialog();
                                        }
                                }, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                                dismissBaseDialog();
                                                selectedList.remove(currentPosition);
                                                if (selectedList.size() > 0) {
                                                        LogUtil.e("通知数据改变");
                                                        mImagePageAdapter.notifyDataSetChanged();
                                                        display.setCurrentItem(currentPosition, true);
                                                        description.setText(getString(R.string.preview_image_count, currentPosition + 1, selectedList.size()));
                                                } else {
                                                        onBackPressed();
                                                }
                                        }
                                });
                                break;
                        case R.id.cb_base_preview_select:
                                int size = CommonImageLoader.getInstance().getMaxSelectedCount();
                                if (select.isChecked()) {
                                        if (selectedList.size() >= size) {
                                                ToastUtils.showShortToast("最多只能添加" + size + "图片");
                                                select.setChecked(false);
                                        } else {
                                                LogUtil.e("selectedList.size()" + selectedList.size());
                                                selectedList.add(previewList.get(currentPosition));
                                                LogUtil.e("CommonImageLoaderSize" + CommonImageLoader.getInstance().getSelectedImages().size());
                                        }
                                } else {
                                        selectedList.remove(previewList.get(currentPosition));
                                }
                                if (size > 0) {
                                        finish.setEnabled(true);
                                        finish.setText(getString(R.string.finish_count, CommonImageLoader.getInstance().getSelectedImages().size(), size));
                                } else {
                                        finish.setEnabled(false);
                                        finish.setText(R.string.finish);
                                }
                                break;
                }
        }


        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
                LogUtil.e("topBar");
                if (topBar.getVisibility() == View.VISIBLE) {
                        LogUtil.e("topBarVISIBLE");
                        topBar.setVisibility(View.GONE);
                } else {
                        LogUtil.e("topBarGONE");
                        topBar.setVisibility(View.VISIBLE);
                }
                if (from.equals(CommonImageLoader.PREVIEW_SELECT)) {
                        LogUtil.e("bottomView");
                        if (bottomView.getVisibility() == View.VISIBLE) {
                                bottomView.setVisibility(View.GONE);
                                LogUtil.e("bottomViewGONE");
                        } else {
                                bottomView.setVisibility(View.VISIBLE);
                                LogUtil.e("bottomViewVISIBLE");
                        }
                }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        public static void startBasePreview(Activity activity, List<ImageItem> list, int position) {
                CommonImageLoader.getInstance().clearAllData();
                CommonImageLoader.getInstance().setSelectedImages(list);
                Intent intent = new Intent(activity, BasePreViewActivity.class);
                intent.putExtra(CommonImageLoader.PREVIEW_FROM, CommonImageLoader.PREVIEW_BASE);
                intent.putExtra(CommonImageLoader.CURRENT_POSITION, position);
                activity.startActivity(intent);
        }

        @Override
        public void updateData(Object o) {

        }
}
