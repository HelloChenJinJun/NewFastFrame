package com.example.chat.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.chat.R;
import com.example.chat.bean.ImageItem;
import com.example.commonlibrary.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/20     20:28
 * QQ:         1981367757
 */

public class PhotoPreViewAdapter extends PagerAdapter {

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

    public ImageItem getData(int position) {
        return data.get(position);
    }

    public interface OnPhotoViewClickListener {
        void onPhotoViewClick(View view, int position);
    }

    public PhotoPreViewAdapter(Context context, List<ImageItem> previewList) {
        this.mContext = context;
        screenWidth = DensityUtil.getScreenWidth(context);
        screenHeight = DensityUtil.getScreenHeight(context);
        if (previewList == null) {
            data = new ArrayList<>();
        } else {
            data = previewList;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_activity_photo_preview, null);
        PhotoView photoView = view.findViewById(R.id.pv_item_activity_photo_preview_display);
        photoView.setOnPhotoTapListener((view1, x, y) -> {
            if (mOnPhotoViewClickListener != null) {
                mOnPhotoViewClickListener.onPhotoViewClick(view1, position);
            }
        });
        ImageItem imageItem = data.get(position);
        String url = imageItem.getPath();
        if (url!=null) {
            if (url.endsWith(".gif")) {
                Glide.with(mContext).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).override(screenWidth, screenHeight).thumbnail(0.1f).into(photoView);
            } else {
                Glide.with(mContext).load(url).override(screenWidth, screenHeight).diskCacheStrategy(DiskCacheStrategy.RESULT).into(photoView);
            }
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
