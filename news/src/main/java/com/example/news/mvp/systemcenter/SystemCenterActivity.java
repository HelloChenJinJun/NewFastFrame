package com.example.news.mvp.systemcenter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.news.CenterAdapter;
import com.example.news.R;
import com.example.news.ScoreQueryActivity;
import com.example.news.bean.CenterBean;
import com.example.news.mvp.consume.ConsumeQueryActivity;
import com.example.news.mvp.systeminfo.SystemInfoLoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/17     18:07
 * QQ:         1981367757
 */

public class SystemCenterActivity extends BaseActivity {
    private SuperRecyclerView display;
    private CenterAdapter centerAdapter;

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
    protected int getContentLayout() {
        return R.layout.activity_system_center;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_system_center_display);
    }

    @Override
    protected void initData() {
        centerAdapter = new CenterAdapter();
        display.setLayoutManager(new WrappedGridLayoutManager(this, 3));
        display.setAdapter(centerAdapter);
        centerAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (position == 0) {
                    ScoreQueryActivity.start(SystemCenterActivity.this);
                } else if (position == 1) {
                    ConsumeQueryActivity.start(SystemCenterActivity.this);
                }
            }
        });
        display.post(new Runnable() {
            @Override
            public void run() {
                centerAdapter.addData(getDefaultData());
            }
        });
    }

    private List<CenterBean> getDefaultData() {
        List<CenterBean> data = new ArrayList<>();
        CenterBean item = new CenterBean();
        item.setResId(R.mipmap.ic_launcher);
        item.setTitle("成绩查询");
        data.add(item);
        CenterBean consumerQuery=new CenterBean();
        consumerQuery.setTitle("消费查询");
        consumerQuery.setResId(R.mipmap.ic_launcher);
        data.add(consumerQuery);
        for (int i = 0; i < 5; i++) {
            CenterBean temp = new CenterBean();
            temp.setResId(R.mipmap.ic_launcher);
            temp.setTitle("标题" + i);
            data.add(temp);
        }
        return data;
    }

    public static void start(Activity activity) {
        Intent intent=new Intent(activity,SystemCenterActivity.class);
        activity.startActivity(intent);
    }
}
