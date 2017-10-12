package com.example.chat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.chat.R;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.LogUtil;
import com.example.chat.util.PixelUtil;

import java.util.List;



/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/4      18:37
 * QQ:             1981367757
 */

public class ListImageView extends LinearLayout {
        private int imagePadding = PixelUtil.dp2px(2);
        private LayoutParams oneLayoutParams;
        /**
         * 多于一张图片情况的布局参数
         */
        private List<String> mImageItemList;
//        private ViewGroup.LayoutParams wrapLayoutParams;
//        private ViewGroup.LayoutParams matchLayoutParams;
        /**
         * 每行的图片个数
         */
        private int per_row_count;
        /**
         * 多图片布局的行的首图片布局
         */
        private ViewGroup.LayoutParams firstMoreLayoutParams;
        private LayoutParams moreLayoutParams;

        public ListImageView(Context context) {
                this(context, null);
        }

        public ListImageView(Context context, AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public ListImageView(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
                super.onSizeChanged(w, h, oldw, oldh);
                LogUtil.e("onSizeChanged");
        }

        private void initView() {
                setOrientation(VERTICAL);
                if (getChildCount() > 0) {
                        LogUtil.e("有缓存的VIEW，清除掉");
                        removeAllViews();
                }
                if (maxWidth == 0) {
                        LogUtil.e("111最大宽度为0，证明还没有onMeasure,这里触发onMeasure");
                        addView(new View(getContext()));
                        return;
                }
                if (mImageItemList == null || mImageItemList.size() == 0) {
                        LogUtil.e("得到的图片数据为空");
                        return;
                }
                if (mImageItemList.size() == 1) {
                        LogUtil.e("只有一个图片显示");
                        addView(createImageView(0, true));
                } else {
                        int photoCount = mImageItemList.size();
                        if (photoCount == 4) {
                                per_row_count = 2;
                        } else {
                                per_row_count = 3;
                        }
                        int rowCount = photoCount % per_row_count == 0 ? photoCount / per_row_count : photoCount / per_row_count + 1;

                        for (int i = 0; i < rowCount; i++) {
                                LinearLayout linearLayout = new LinearLayout(getContext());
                                linearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                linearLayout.setOrientation(HORIZONTAL);
                                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                                if (i != 0) {
                                        linearLayout.setPadding(0, imagePadding, 0, 0);
                                }
                                if (i == rowCount - 1) {
                                        int lastRowCount = photoCount % per_row_count;
                                        if (lastRowCount != 0) {
                                                for (int j = 0; j < lastRowCount; j++) {
                                                        linearLayout.addView(createImageView(i * per_row_count + j, false));
                                                }
                                                addView(linearLayout);
                                                continue;
                                        }
                                }
                                for (int j = 0; j < per_row_count; j++) {
                                        linearLayout.addView(createImageView(i * per_row_count + j, false));
                                }
                                LogUtil.e("这里把view添加到布局中");
                                addView(linearLayout);
                        }
                }
        }

        private void initImageLayoutParams() {
                int moreWidthAndHeight = (maxWidth - imagePadding * 2) / 3;
                int oneWidthAndHeight = (maxWidth - imagePadding) * 2 / 3;
//                wrapLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                matchLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                oneLayoutParams = new LayoutParams(oneWidthAndHeight, oneWidthAndHeight);
                firstMoreLayoutParams = new ViewGroup.LayoutParams(moreWidthAndHeight, moreWidthAndHeight);
                moreLayoutParams = new LayoutParams(moreWidthAndHeight, moreWidthAndHeight);
                moreLayoutParams.setMargins(imagePadding, 0, 0, 0);
        }


        private int maxWidth = 0;

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                LogUtil.e("onMeasure1");
//                这里获取控件的宽度，用户设置子控件的布局
                if (maxWidth == 0) {
                        int with = measureWidth(widthMeasureSpec);
                        if (with > 0) {
                                maxWidth = with;
                                bindData(mImageItemList);
                        }
                }
        }

        private int measureWidth(int widthMeasureSpec) {
                int result = 0;
                int specMode = MeasureSpec.getMode(widthMeasureSpec);
                int specSize = MeasureSpec.getSize(widthMeasureSpec);

                if (specMode == MeasureSpec.EXACTLY) {
                        LogUtil.e("EXACTLY");
                        result = specSize;
                } else if (specMode == MeasureSpec.AT_MOST) {
                        LogUtil.e("AT_MOST");
                        result = Math.min(result, specSize);
                }
                return result;
        }


        @Override
        protected void onFinishInflate() {
                super.onFinishInflate();
                LogUtil.e("数据加载完成");
        }


        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
                super.onLayout(changed, l, t, r, b);
                LogUtil.e("onLayout");
        }

        public void setImagePadding(int imagePadding) {
                this.imagePadding = imagePadding;
        }

        public void bindData(List<String> list) {
                if (list == null || list.size() == 0) {
                        LogUtil.e("添加的图片集合为空");
                        return;
                }
                mImageItemList = list;
                if (maxWidth > 0) {
                        LogUtil.e("这里是从onMeasure过来的");
                        initImageLayoutParams();
                }
                initView();
        }


        private View createImageView(final int position, boolean isOne) {
                ImageView imageView = new ImageView(getContext());
                final String url = mImageItemList.get(position);
                if (url.contains("$")) {
                        LogUtil.e("这是视频的URL");
                } else {
                        LogUtil.e("这是图片的URL");
                }
                if (isOne) {
                        imageView.setLayoutParams(oneLayoutParams);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
//                        多种图片
                        if (position % per_row_count == 0) {
//                                首图片
                                imageView.setLayoutParams(firstMoreLayoutParams);
                        } else {
                                imageView.setLayoutParams(moreLayoutParams);
                        }
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                imageView.setBackgroundColor(getResources().getColor(R.color.default_image_bg_color));
                imageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (mOnImageViewItemClickListener != null) {
                                        mOnImageViewItemClickListener.onImageClick(v, position, url);
                                }
                        }
                });
//                添加的时候就加载
                LogUtil.e("这里加载了");
                if (!url.contains("$")) {
                        Glide.with(getContext()).load(url).error(R.mipmap.default_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
                } else {
                        LogUtil.e("这里1是视频的URL，首先将其拆分");
                        List<String> urlList = CommonUtils.content2List(url);
                        if (urlList != null && urlList.size() > 0) {
                                LogUtil.e("视频URL列表");
                                for (String str : urlList
                                        ) {
                                        if (str.contains(".jpg") || str.contains(".png") || str.contains(".jpeg")) {
                                                LogUtil.e("封面图片URL" + str);
                                                Glide.with(getContext()).load(str).error(R.mipmap.default_image).thumbnail(0.1f).into(imageView);
                                        } else {
                                                LogUtil.e("视频URL" + str);
                                        }
                                }
                        }
                }
                LogUtil.e("这里返回");
                return imageView;
        }

        private OnImageViewItemClickListener mOnImageViewItemClickListener;

        public void setOnImageViewItemClickListener(OnImageViewItemClickListener onImageViewItemClickListener) {
                mOnImageViewItemClickListener = onImageViewItemClickListener;
        }

        public interface OnImageViewItemClickListener {
                void onImageClick(View view, int position, String url);
        }
}
