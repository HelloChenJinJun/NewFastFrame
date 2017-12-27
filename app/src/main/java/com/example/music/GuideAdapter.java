package com.example.music;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/26     20:23
 * QQ:         1981367757
 */

public class GuideAdapter extends PagerAdapter {


    private List<View> viewList;



    public GuideAdapter(List<View> viewList){
        if (viewList == null) {
            this.viewList=new ArrayList<>();
        }else {
            this.viewList=viewList;
        }
    }




    @Override
    public int getCount() {
        return viewList.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((View) object));
    }
}
