package com.example.news.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.imageloader.glide.GlideImageLoaderConfig;
import com.example.news.R;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/26      17:05
 * QQ:             1981367757
 */

public class OtherNewPhotoSetAdapter extends PagerAdapter{
    private List<String>  imageList=new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }




    public void setImageList(List<String> imageList) {
        if (imageList!=null) {
            this.imageList = imageList;
        }
    }




    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
            View view= LayoutInflater.from(container.getContext())
                    .inflate(R.layout.item_activity_other_new_photo_set,null);
        PhotoView photoView=(PhotoView)view.findViewById(R.id.pv_item_activity_other_new_photo_set_display);
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view,position);
                }
            }
        });
        BaseApplication
                .getAppComponent().getImageLoader().loadImage(container.getContext()
        ,new GlideImageLoaderConfig.Builder().imageView(photoView)
        .url(imageList.get(position)).centerInside().build());
        container.addView(view);
        return view;
    }



    public interface OnItemClickListener{
        public void onItemClick(View view,int position);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
