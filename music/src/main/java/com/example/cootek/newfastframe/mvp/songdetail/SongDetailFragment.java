package com.example.cootek.newfastframe.mvp.songdetail;

import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.bean.music.MusicPlayBeanDao;
import com.example.commonlibrary.cusotomview.WrappedViewPager;
import com.example.commonlibrary.manager.music.MusicPlayerManager;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.rxbus.event.PlayStateEvent;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.base.MusicBaseFragment;
import com.example.cootek.newfastframe.mvp.songlist.SongListFragment;
import com.example.cootek.newfastframe.util.MusicUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/7     17:36
 */
public class SongDetailFragment extends MusicBaseFragment {


    private WrappedViewPager mWrappedViewPager;

    private MusicPlayBean bean;


    private TabLayout mTabLayout;
    private List<Fragment> mFragmentList;


    public static SongDetailFragment newInstance() {
        return new SongDetailFragment();
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
        return R.layout.fragment_song_detail;
    }

    @Override
    protected void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tl_fragment_song_detail_tab);
        mWrappedViewPager = (WrappedViewPager) findViewById(R.id.wvp_fragment_song_detail_display);

    }

    @Override
    protected boolean needStatusPadding() {
        return false;
    }

    @Override
    protected void initData() {
        addDisposable(RxBusManager.getInstance().registerEvent(PlayStateEvent.class, playStateEvent -> {
            if (playStateEvent.getPlayState() == MusicPlayerManager.PLAY_STATE_PREPARED) {
                bean = MusicPlayerManager.getInstance().getMusicPlayBean();
                if (bean != null && mFragmentList != null && mFragmentList.size() > 0) {
                    for (Fragment item :
                            mFragmentList) {
                        ((SongListFragment) item).onRefresh();
                    }
                }
            }
        }));

    }

    @Override
    protected void updateView() {
        if (bean == null) {
            bean = getAppComponent().getDaoSession()
                    .getMusicPlayBeanDao().queryBuilder().where(MusicPlayBeanDao
                            .Properties.SongUrl.eq(MusicPlayerManager.getInstance().getUrl())).build().unique();
        }
        if (bean != null) {
            dealData();
        }
    }


    private void dealData() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(SongListFragment.newInstance(MusicUtil.FROM_BOTTOM_ALBUM, bean.getAlbumId() + ""));
        mFragmentList.add(SongListFragment.newInstance(MusicUtil.FROM_RECOMMEND, bean.getSongId() + ""));
        List<String> titleList = new ArrayList<>();
        titleList.add("专辑");
        titleList.add("推荐");
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.setTitleAndFragments(titleList, mFragmentList);
        mTabLayout.setupWithViewPager(mWrappedViewPager);
        mWrappedViewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void updateData(Object o) {

    }
}
