package com.example.music;

import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private MainAdapter mainAdapter;
    private SuperRecyclerView display;

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
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        display= (SuperRecyclerView) findViewById(R.id.srcv_activity_main_display);
    }

    @Override
    protected void initData() {
        display.setLayoutManager(new WrappedGridLayoutManager(this,2));
        mainAdapter=new MainAdapter();
        mainAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                MainItemBean item=mainAdapter.getData(position);
                ARouter.getInstance().build(item.getPath()).navigation();
            }
        });
        display.setAdapter(mainAdapter);
        display.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainAdapter.addData(getDefaultData());
            }
        },500);
    }

    private List<MainItemBean> getDefaultData() {
        List<MainItemBean> result=new ArrayList<>();
        MainItemBean mainItemBean=new MainItemBean();
        mainItemBean.setName("校园");
        mainItemBean.setPath("/news/main");
        mainItemBean.setResId(R.mipmap.ic_launcher);
        MainItemBean music=new MainItemBean();
        music.setName("音乐");
        music.setResId(R.mipmap.ic_launcher);
        music.setPath("/music/main");
        MainItemBean live=new MainItemBean();
        live.setName("直播");
        live.setResId(R.mipmap.ic_launcher);
        live.setPath("/live/main");
        MainItemBean chat=new MainItemBean();
        chat.setName("聊天");
        chat.setPath("/chat/splash");
        chat.setResId(R.mipmap.ic_launcher);
        result.add(mainItemBean);
        result.add(music);
        result.add(live);
        result.add(chat);
        return result;
    }


}
