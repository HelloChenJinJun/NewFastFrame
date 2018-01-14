package com.example.news;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.bean.news.OtherNewsTypeBean;
import com.example.commonlibrary.bean.news.OtherNewsTypeBeanDao;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.news.adapter.PopWindowAdapter;
import com.example.news.event.TypeNewsEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/29      22:59
 * QQ:             1981367757
 */

public class AdjustNewsTypeActivity extends BaseActivity{
    private SuperRecyclerView up,down;
    private PopWindowAdapter upAdapter,downAdapter;

    @Override
    public void updateData(Object o) {

    }

    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_adjust_news_type;
    }

    @Override
    protected void initView() {
        up= (SuperRecyclerView) findViewById(R.id.srcv_activity_adjust_news_type_up_display);
        down= (SuperRecyclerView) findViewById(R.id.srcv_activity_adjust_news_type_down_display);
    }

    @Override
    protected void initData() {
        up.setLayoutManager(new WrappedGridLayoutManager(this,5));
        down.setLayoutManager(new WrappedGridLayoutManager(this,5));
        upAdapter=new PopWindowAdapter();
        downAdapter=new PopWindowAdapter();
        up.setAdapter(upAdapter);
        down.setAdapter(downAdapter);
        upAdapter.addData(NewsApplication.getNewsComponent().getRepositoryManager()
        .getDaoSession().getOtherNewsTypeBeanDao().queryBuilder().where(OtherNewsTypeBeanDao.Properties
                .HasSelected.eq(Boolean.TRUE)).build().list());
        downAdapter.addData(NewsApplication.getNewsComponent().getRepositoryManager()
                .getDaoSession().getOtherNewsTypeBeanDao().queryBuilder().where(OtherNewsTypeBeanDao.Properties
                        .HasSelected.eq(Boolean.FALSE)).build().list());
        downAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                OtherNewsTypeBean bean=downAdapter.removeData(position);
                bean.setHasSelected(true);
                NewsApplication.getNewsComponent().getRepositoryManager().getDaoSession()
                        .getOtherNewsTypeBeanDao().update(bean);
                upAdapter.addData(bean);
                TypeNewsEvent typeNewsEvent=new TypeNewsEvent(TypeNewsEvent.ADD);
                typeNewsEvent.setTypeId(bean.getTypeId());
                RxBusManager.getInstance().post(typeNewsEvent);
            }
        });
        upAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                OtherNewsTypeBean data=upAdapter.getData(position);
                if (TextUtils.isEmpty(data.getTypeId())
                        || data.getTypeId().equals("T1348647909107")
                        || data.getTypeId().equals("TYPE_DD")) {
                    ToastUtils.showShortToast("固定不可移除");
                    return;
                }
                OtherNewsTypeBean bean=upAdapter.removeData(position);
                bean.setHasSelected(false);
                NewsApplication.getNewsComponent().getRepositoryManager().getDaoSession()
                        .getOtherNewsTypeBeanDao().update(bean);
                downAdapter.addData(bean);
                TypeNewsEvent typeNewsEvent=new TypeNewsEvent(TypeNewsEvent.DELETE);
                typeNewsEvent.setTypeId(bean.getTypeId());
                RxBusManager.getInstance().post(typeNewsEvent);
            }
        });
        ToolBarOption toolBarOption=new ToolBarOption();
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setTitle("类别");
        setToolBar(toolBarOption);
    }

    public static void start(Activity activity) {
        Intent intent=new Intent(activity,AdjustNewsTypeActivity.class);
        activity.startActivity(intent);
    }

}
