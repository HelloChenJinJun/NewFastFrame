package com.example.commonlibrary.mvp.base;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.R;
import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.customview.DragFrameLayout;
import com.example.commonlibrary.customview.WrappedViewPager;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.rxbus.event.PhotoPreEvent;
import com.example.commonlibrary.utils.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/24     18:10
 */
public class ImagePreViewActivity extends BaseActivity {

    private WrappedViewPager display;
    private ViewPagerAdapter mViewPagerAdapter;
    private List<String> dataList;
    private int position;
    private List<Fragment> list;
    private DragFrameLayout mDragFrameLayout;
    private int flag;

    public static void start(Activity activity, ArrayList<String> data, int position, View view, int flag) {
        Intent intent = new Intent(activity, ImagePreViewActivity.class);
        intent.putStringArrayListExtra(Constant.DATA, data);
        intent.putExtra(Constant.POSITION, position);
        intent.putExtra(Constant.FLAG, flag);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, data.get(position));
        activity.startActivity(intent, activityOptionsCompat.toBundle());
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
        return R.layout.activity_image_preview;
    }

    @Override
    protected void initView() {
        display = findViewById(R.id.wvp_activity_image_preview_display);
        mDragFrameLayout = findViewById(R.id.dfl_activity_image_preview_drag);
        mDragFrameLayout.setOnDragListener(value -> {
        });
    }

    @Override
    protected void initData() {
        bg.setBackgroundColor(Color.TRANSPARENT);
        dataList = getIntent().getStringArrayListExtra(Constant.DATA);
        position = getIntent().getIntExtra(Constant.POSITION, 0);
        flag = getIntent().getIntExtra(Constant.FLAG, 0);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        list = new ArrayList<>();
        for (String item :
                dataList) {
            list.add(ImagePreViewFragment.newInstance(item));
        }
        mViewPagerAdapter.setTitleAndFragments(null, list);
        display.setAdapter(mViewPagerAdapter);
        display.setCurrentItem(position);
        if (Build.VERSION.SDK_INT >= 22) {
            postponeEnterTransition();
            //这个可以看做个管道  每次进入和退出的时候都会进行调用  进入的时候获取到前面传来的共享元素的信息
            //退出的时候 把这些信息传递给前面的activity
            //同时向sharedElements里面put view,跟对view添加transitionname作用一样
            setEnterSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    sharedElements.clear();
                    sharedElements.put(dataList.get(display.getCurrentItem()), ((ImagePreViewFragment) list.get(display.getCurrentItem())).getSharedElement());
                }
            });
        }

    }


    @Override
    public void onBackPressed() {
        RxBusManager.getInstance().post(new PhotoPreEvent(flag, display.getCurrentItem()));
        ActivityCompat.finishAfterTransition(this);
    }

    @Override
    public void updateData(Object o) {

    }
}
