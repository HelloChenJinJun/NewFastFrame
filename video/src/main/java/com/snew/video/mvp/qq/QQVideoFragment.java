package com.snew.video.mvp.qq;

import android.os.Bundle;
import android.view.View;

import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.cusotomview.WrappedViewPager;
import com.google.android.material.tabs.TabLayout;
import com.snew.video.R;
import com.snew.video.base.VideoBaseFragment;
import com.snew.video.bean.QQVideoTabBean;
import com.snew.video.mvp.search.SearchVideoActivity;
import com.snew.video.util.VideoUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/12     15:58
 */
public class QQVideoFragment extends VideoBaseFragment {

    private WrappedViewPager display;
    private TabLayout mTabLayout;


    public static QQVideoFragment newInstance(int videoUrlType) {
        Bundle bundle = new Bundle();
        bundle.putInt(VideoUtil.VIDEO_URL_TYPE, videoUrlType);
        QQVideoFragment qqVideoFragment = new QQVideoFragment();
        qqVideoFragment.setArguments(bundle);
        return qqVideoFragment;
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
        return R.layout.fragment_qq_video;
    }

    @Override
    protected void initView() {
        display = (WrappedViewPager) findViewById(R.id.wcp_fragment_qq_video_display);
        mTabLayout = (TabLayout) findViewById(R.id.tb_fragment_qq_video_display);
    }


    private int type;

    @Override
    protected void initData() {
        type = getArguments().getInt(VideoUtil.VIDEO_URL_TYPE, VideoUtil.VIDEO_URL_TYPE_QQ);
        init();
        ToolBarOption toolBarOption = new ToolBarOption();
        if (type == VideoUtil.VIDEO_URL_TYPE_QQ) {
            toolBarOption.setTitle("腾讯视频");
        } else {
            toolBarOption.setTitle("天天更新");
        }
        toolBarOption.setNeedNavigation(false);
        toolBarOption.setRightResId(R.drawable.ic_news_search);
        toolBarOption.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchVideoActivity.start(getActivity());
            }
        });
        setToolBar(toolBarOption);
    }

    private void init() {
        List<QQVideoTabBean> tabBeanList;
        tabBeanList = getDefaultData();
        List<Fragment> baseFragments = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        for (QQVideoTabBean item :
                tabBeanList) {
            titleList.add(item.getTitle());
            baseFragments.add(QQVideoListFragment.newInstance(type, item.getType()));
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.setTitleAndFragments(titleList, baseFragments);
        mTabLayout.setupWithViewPager(display);
        display.setAdapter(viewPagerAdapter);
    }

    private List<QQVideoTabBean> getDefaultData() {
        List<QQVideoTabBean> list = new ArrayList<>();
        if (type == VideoUtil.VIDEO_URL_TYPE_QQ) {
            QQVideoTabBean one = new QQVideoTabBean();
            one.setTitle("电影");
            one.setType(VideoUtil.VIDEO_TYPE_QQ_CAMERA);
            QQVideoTabBean two = new QQVideoTabBean();
            two.setType(VideoUtil.VIDEO_TYPE_QQ_TV);
            two.setTitle("连续剧");
            list.add(one);
            list.add(two);
            QQVideoTabBean three = new QQVideoTabBean();
            three.setType(VideoUtil.VIDEO_TYPE_QQ_CARTOON);
            three.setTitle("动漫");
            list.add(three);
            QQVideoTabBean four = new QQVideoTabBean();
            four.setType(VideoUtil.VIDEO_TYPE_QQ_VARIETY);
            four.setTitle("综艺");
            list.add(four);
            QQVideoTabBean five = new QQVideoTabBean();
            five.setType(VideoUtil.VIDEO_TYPE_QQ_RECORD);
            five.setTitle("记录片");
            list.add(five);
            QQVideoTabBean six = new QQVideoTabBean();
            six.setType(22);
            six.setTitle("音乐");
            list.add(six);
        } else {
            QQVideoTabBean one = new QQVideoTabBean();
            one.setTitle("电影");
            one.setType(1);
            QQVideoTabBean two = new QQVideoTabBean();
            two.setTitle("连续剧");
            two.setType(2);
            QQVideoTabBean three = new QQVideoTabBean();
            three.setTitle("综艺");
            three.setType(3);
            QQVideoTabBean four = new QQVideoTabBean();
            four.setTitle("动漫");
            four.setType(4);
            list.add(one);
            list.add(two);
            list.add(three);
            list.add(four);
        }
        return list;
    }

    @Override
    protected void updateView() {
        display.setCurrentItem(0);
    }

    @Override
    public void updateData(Object o) {

    }
}
