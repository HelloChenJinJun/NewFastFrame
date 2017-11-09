package com.example.cootek.newfastframe.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.baseadapter.animator.DefaultBaseAnimator;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.BaseFragment;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.ui.DownLoadActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COOTEK on 2017/8/17.
 */

public class TestFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {

    TextView tvTestDownload;
    SuperRecyclerView display;


    private TestAdapter testAdapter;
    private int position = 0;
    private DefaultBaseAnimator itemAnimator;

    @Override
    public void updateData(Object o) {

    }

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_test_play);
    }

    @Override
    protected void initData() {
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        display.setAdapter(testAdapter = new TestAdapter());
        testAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                start();
            }
        });
        display.setItemAnimator(itemAnimator = new DefaultBaseAnimator());
        itemAnimator.setInterpolator(new LinearInterpolator());
        display.getItemAnimator().setAddDuration(1000);
        display.getItemAnimator().setRemoveDuration(1000);
        data.addAll(getData());
    }

    private void start() {
        display.post(new Runnable() {
            @Override
            public void run() {
                deal();
                display.postDelayed(this, 2000);
            }
        });
    }


    private boolean isRemove = false;

    private void deal() {
        if (isRemove) {
            isRemove = false;
            testAdapter.removeData(0);
        } else {
            testAdapter.addData(getItem(position));
            position++;
            isRemove = true;
        }

    }


    private List<String> data = new ArrayList<>();

    private String getItem(int position) {
        if (position >= 0 && position < data.size()) {
            return data.get(position);
        }
        return null;
    }

    @Override
    protected void updateView() {
        testAdapter.addData(getInitData());
    }

    private List<String> getInitData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add("初始化数据" + i);
        }
        return list;
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("测试" + i);
        }
        return list;
    }

    public static BaseFragment newInstance() {
        return new TestFragment();
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void loadMore() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onViewClicked() {
        Intent intent = new Intent(getContext(), DownLoadActivity.class);
        startActivity(intent);
    }

    private class TestAdapter extends BaseRecyclerAdapter<String, BaseWrappedViewHolder> {
        @Override
        protected int getLayoutId() {
            return R.layout.item_fragment_test_display;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, String data) {
            holder.setText(R.id.tv_item_fragment_test_display_content, data)
                    .setOnItemClickListener();
        }
    }


}
