package com.example.cootek.newfastframe.ui.fragment;

import android.content.Intent;
import android.view.View;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.BaseFragment;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.VideoApplication;
import com.example.cootek.newfastframe.adapter.SingerListAdapter;
import com.example.commonlibrary.bean.music.SingerListBean;
import com.example.cootek.newfastframe.dagger.singerlist.DaggerSingerListComponent;
import com.example.cootek.newfastframe.dagger.singerlist.SingerListModule;
import com.example.cootek.newfastframe.mvp.singerlist.SingerListPresenter;
import com.example.cootek.newfastframe.ui.SingerInfoActivity;
import com.example.cootek.newfastframe.util.MusicUtil;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by COOTEK on 2017/9/2.
 */

public class SingerListFragment extends BaseFragment<Object, SingerListPresenter> {
    private SuperRecyclerView display;

    @Inject
    SingerListAdapter singerListAdapter;


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
        return R.layout.fragment_singer_list;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_singer_list);
    }

    @Override
    protected void initData() {
        DaggerSingerListComponent.builder().mainComponent(VideoApplication.getMainComponent())
                .singerListModule(new SingerListModule(this))
                .build().inject(this);
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        display.setAdapter(singerListAdapter);
        singerListAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent = new Intent(getContext(), SingerInfoActivity.class);
                intent.putExtra(MusicUtil.TING_UID, singerListAdapter.getData(position).getTingId());
                intent.putExtra(MusicUtil.SINGER_AVATAR, singerListAdapter.getData(position).getAvatar());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void updateView() {
        getData();
    }

    private void getData() {
        presenter.getSingerListData();
    }

    public static SingerListFragment newInstance() {
        return new SingerListFragment();
    }

    @Override
    public void updateData(Object object) {
        if (object instanceof SingerListBean) {
            SingerListBean bean = ((SingerListBean) object);
            singerListAdapter.addData(bean);
            singerListAdapter.notifyDataSetChanged();
        } else {
            ArrayList<SingerListBean> result = (ArrayList<SingerListBean>) object;
            singerListAdapter.addData(result);
        }
    }
}
