package com.example.chat.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.chat.R;
import com.example.chat.bean.ImageItem;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/29      16:06
 * QQ:             1981367757
 */
public class ImagePageAdapter extends PagerAdapter {


    private OnPhotoViewClickListener mOnPhotoViewClickListener;
    private List<ImageItem> data;
    private Context mContext;
    /**
     * 屏幕宽度
     */
    private int screenWidth;
    /**
     * 屏幕高度
     */
    private int screenHeight;
//        private CommonImageLoader mCommonImageLoader;

//        private ImageLoadListener listener;


    public interface OnPhotoViewClickListener {
        void onPhotoViewClick(View view, int position);
    }

    public ImagePageAdapter(Context context, List<ImageItem> previewList) {
        this.mContext = context;
        DisplayMetrics displayMetrics = CommonUtils.getScreenPix(context);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
//                mCommonImageLoader = CommonImageLoader.getInstance();
        if (previewList == null) {
            data = new ArrayList<>();
        } else {
            data = previewList;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.base_pre_view_item_layout, null);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.pv_base_pre_view_item_display);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pb_base_pre_view_item_load);
        LogUtil.e("执行到这里,可以了");
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                LogUtil.e("点击操作photoView");
                if (mOnPhotoViewClickListener != null) {
                    mOnPhotoViewClickListener.onPhotoViewClick(view, position);
                }
            }
        });
        ImageItem imageItem = data.get(position);
        String url = imageItem.getPath();
        if (url.endsWith(".gif")) {
            Glide.with(mContext).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).override(screenWidth, screenHeight).thumbnail(0.1f).into(photoView);
        } else {
            Glide.with(mContext).load(url).override(screenWidth, screenHeight).into(photoView);
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setOnPhotoViewClickListener(OnPhotoViewClickListener onPhotoViewClickListener) {
        mOnPhotoViewClickListener = onPhotoViewClickListener;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ViewGroup) object);
    }
}
