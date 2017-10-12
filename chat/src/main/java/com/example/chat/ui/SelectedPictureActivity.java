package com.example.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.chat.R;
import com.example.chat.adapter.ImageFoldersAdapter;
import com.example.chat.adapter.SelectedPictureAdapter;
import com.example.chat.base.CommonImageLoader;
import com.example.chat.base.Constant;
import com.example.chat.bean.ImageFolder;
import com.example.chat.bean.ImageItem;
import com.example.chat.listener.OnImageLoadListener;
import com.example.chat.util.LoadPictureUtil;
import com.example.chat.util.LogUtil;
import com.example.chat.view.ImageFolderPopupWindow;
import com.example.commonlibrary.utils.ToastUtils;

import java.io.File;
import java.util.List;



/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/29      12:01
 * QQ:             1981367757
 */

public class SelectedPictureActivity extends SlideBaseActivity implements View.OnClickListener, OnImageLoadListener, SelectedPictureAdapter.OnItemCheckClickListener {
        private ImageView back;
        private Button finish;
        private Button all;
        private Button pre;
        private RelativeLayout bottomView;
        private SelectedPictureAdapter mAdapter;
        private RecyclerView display;
        private File photoFile;
        private ImageFoldersAdapter mImageFoldersAdapter;
        private ImageFolderPopupWindow imageFolderPopupWindow;


        @Override
        public void initView() {
                back = (ImageView) findViewById(R.id.iv_picture_top_bar_back);
                finish = (Button) findViewById(R.id.btn_picture_top_bar_finish);
                all = (Button) findViewById(R.id.btn_selected_picture_all);
                pre = (Button) findViewById(R.id.btn_selected_picture_pre);
                display = (RecyclerView) findViewById(R.id.rcv_selected_picture_display);
                bottomView = (RelativeLayout) findViewById(R.id.rl_selected_picture_bottom);
                back.setOnClickListener(this);
                finish.setOnClickListener(this);
                all.setOnClickListener(this);
                pre.setOnClickListener(this);
        }


        @Override
        public void initData() {
                LogUtil.e("这里1");
                new LoadPictureUtil().getAllImageFolder(this, this);
                mAdapter = new SelectedPictureAdapter();
                mImageFoldersAdapter = new ImageFoldersAdapter();
                mAdapter.setOnItemCheckClickListener(this);
                display.setLayoutManager(new GridLayoutManager(this, 3));
                display.setItemAnimator(new DefaultItemAnimator());
                display.setAdapter(mAdapter);
        }


        @Override
        protected void onResume() {
                super.onResume();
                if (CommonImageLoader.getInstance().getImageFolders().size() > 0) {
                        LogUtil.e("onResume绑定数据");
                        mAdapter.notifyDataSetChanged();
                        notifyTextChanged();
                }
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
                return R.layout.selected_picture;
        }

        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                        case R.id.iv_picture_top_bar_back:
                                CommonImageLoader.getInstance().getSelectedImages().clear();
                                finish();
                                break;
                        case R.id.btn_picture_top_bar_finish:
                                setResult(Activity.RESULT_OK);
                                finish();
                                break;
                        case R.id.btn_selected_picture_all:
                                List<ImageFolder> list = CommonImageLoader.getInstance().getImageFolders();
                                if (list == null || list.size() == 0) {
                                        ToastUtils.showShortToast("当前没有任何照片");
                                        return;
                                }
                                if (imageFolderPopupWindow == null) {
                                        LogUtil.e("创建下拉窗口123");
                                        imageFolderPopupWindow = new ImageFolderPopupWindow(this, mImageFoldersAdapter) {
                                                @Override
                                                public void onItemClick(View view, int position, long id) {
                                                        mImageFoldersAdapter.setCurrentSelectedPosition(position);
                                                        CommonImageLoader.getInstance().setCurrentImageFolderPosition(position);
                                                        ImageFolder imageFolder = mImageFoldersAdapter.getData(position);
                                                        if (imageFolder != null) {
                                                                mAdapter.clearAllData();
                                                                ImageItem imageItem = new ImageItem();
                                                                imageItem.setItemType(ImageItem.ITEM_CAMERA);
                                                                mAdapter.addData(imageItem);
                                                                mAdapter.addData(imageFolder.getAllImages());
                                                                all.setText(imageFolder.getName());
                                                        }
                                                        imageFolderPopupWindow.dismiss();
                                                }
                                        };
                                        imageFolderPopupWindow.showAtLocation(bottomView, Gravity.NO_GRAVITY, 0, 0);
                                } else {
                                        if (imageFolderPopupWindow.isShowing()) {
                                                imageFolderPopupWindow.dismiss();
                                        } else {
                                                imageFolderPopupWindow.showAtLocation(bottomView, Gravity.NO_GRAVITY, 0, 0);
                                                mImageFoldersAdapter.notifyDataSetChanged();
                                        }
                                }
                                break;
                        case R.id.btn_selected_picture_pre:
                                Intent intent = new Intent(this, BasePreViewActivity.class);
                                intent.putExtra(CommonImageLoader.PREVIEW_FROM, CommonImageLoader.PREVIEW_SELECT);
                                startActivity(intent);
                                break;
                }
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                        switch (requestCode) {
                                case Constant.REQUEST_CODE_TAKE_PHOTO:
                                        if (data == null) {
//                                                由于拍照指定了uri，所以返回的数据为null
                                                ImageItem imageItem = new ImageItem();
                                                imageItem.setPath(photoFile.getAbsolutePath());
                                                CommonImageLoader.getInstance().getSelectedImages().add(imageItem);
                                                setResult(Activity.RESULT_OK);
                                                finish();
                                        }
                                        break;
                        }
                }
                super.onActivityResult(requestCode, resultCode, data);

        }


        private void notifyTextChanged() {
                int selectedSize = CommonImageLoader.getInstance().getSelectedImages().size();
                if (selectedSize > 0) {
                        pre.setEnabled(true);
                        pre.setText(getString(R.string.bottom_preview_count, selectedSize));
                        finish.setEnabled(true);
                        finish.setText(getString(R.string.finish_count, selectedSize, CommonImageLoader.getInstance().getMaxSelectedCount()));
                } else {
                        pre.setEnabled(false);
                        pre.setText(getString(R.string.bottom_preview));
                        finish.setEnabled(false);
                        finish.setText(getString(R.string.finish));
                }
        }

        @Override
        public void onImageLoaded(List<ImageFolder> imageFolderList) {
                LogUtil.e("加载数据完成");
                if (imageFolderList.size() > 0) {
                        LogUtil.e("存入内存");
                        CommonImageLoader.getInstance().setImageFolders(imageFolderList);
                        LogUtil.e("图片文件夹个数:" + CommonImageLoader.getInstance().getImageFolders().size());
                        LogUtil.e("现在选择的图片文件夹中图片的个数" + CommonImageLoader.getInstance().getCurrentImageFolder().getAllImages().size());
                        LogUtil.e("加载数据完成" + imageFolderList.size());
                        mAdapter.clearAllData();
                        mAdapter.addData(CommonImageLoader.getInstance().getCurrentImageFolder().getAllImages());
                        ImageItem imageItem = new ImageItem();
                        imageItem.setItemType(ImageItem.ITEM_CAMERA);
                        mAdapter.addData(0, imageItem);
                        mImageFoldersAdapter.addData(CommonImageLoader.getInstance().getImageFolders());
                }
                notifyTextChanged();
        }

        @Override
        public void onItemCheck(CheckBox checkBox, int position) {
                notifyTextChanged();
        }

        @Override
        public void onItemClick(View view, int position) {
                if (position != 0) {




                        LogUtil.e("位置是多少" + position);
                        Intent intent = new Intent(SelectedPictureActivity.this, BasePreViewActivity.class);
                        intent.putExtra(CommonImageLoader.CURRENT_POSITION, (position - 1));
                        intent.putExtra(CommonImageLoader.PREVIEW_FROM, CommonImageLoader.PREVIEW_SELECT);
                        startActivity(intent);
                } else {
                        photoFile = CommonImageLoader.getInstance().takePhoto(this, Constant.REQUEST_CODE_TAKE_PHOTO);
                }
        }

        @Override
        public void onBackPressed() {
                if (imageFolderPopupWindow != null && imageFolderPopupWindow.isShowing()) {
                        imageFolderPopupWindow.dismiss();
                } else {
                        super.onBackPressed();
                        CommonImageLoader.getInstance().clearAllData();
                }
        }

        @Override
        public void updateData(Object o) {

        }
}
