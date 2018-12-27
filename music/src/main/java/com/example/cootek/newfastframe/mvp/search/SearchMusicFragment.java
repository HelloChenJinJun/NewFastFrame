package com.example.cootek.newfastframe.mvp.search;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.customview.CustomEditText;
import com.example.commonlibrary.customview.ToolBarOption;
import com.example.commonlibrary.customview.WrappedViewPager;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.base.MusicBaseFragment;
import com.example.cootek.newfastframe.bean.AlbumWrappedBean;
import com.example.cootek.newfastframe.bean.ArtistInfo;
import com.example.cootek.newfastframe.bean.SearchResultBean;
import com.example.cootek.newfastframe.dagger.search.DaggerSerachMusicComponent;
import com.example.cootek.newfastframe.dagger.search.SearchMusicModule;
import com.example.cootek.newfastframe.event.DragEvent;
import com.example.cootek.newfastframe.util.MusicUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/26     9:49
 */
public class SearchMusicFragment extends MusicBaseFragment<BaseBean, SearchMusicPresenter> {
    private WrappedViewPager display;
    private TabLayout mTabLayout;
    private CustomEditText customEditText;
    private List<Fragment> fragmentList;

    public static SearchMusicFragment newInstance() {
        return new SearchMusicFragment();
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_search_music;
    }

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.tb_fragment_search_music_tab);
        display = findViewById(R.id.wvp_fragment_search_music_display);
        display.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    RxBusManager.getInstance().post(new DragEvent(false));
                } else {
                    RxBusManager.getInstance().post(new DragEvent(true));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {
        DaggerSerachMusicComponent.builder().mainComponent(getMainComponent())
                .searchMusicModule(new SearchMusicModule(this)).build()
                .inject(this);
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setCustomView(getHeaderView());
        setToolBar(toolBarOption);
        root.setBackgroundColor(Color.parseColor("#3C5F78"));
    }

    private void update(SearchResultBean searchResultBean) {
        if (fragmentList == null) {
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
            fragmentList = new ArrayList<>();
            fragmentList.add(AlbumListFragment.newInstance((ArrayList<AlbumWrappedBean>) searchResultBean.getAlbumBeans()));
            fragmentList.add(SearchMusicListFragment.newInstance((ArrayList<MusicPlayBean>) searchResultBean.getMusicPlayBeanList()));
            fragmentList.add(SingerListFragment.newInstance((ArrayList<ArtistInfo>) searchResultBean.getArtistInfoList()));
            viewPagerAdapter.setTitleAndFragments(Arrays.asList("专辑", "单曲", "歌手"), fragmentList);
            mTabLayout.setupWithViewPager(display);
            display.setAdapter(viewPagerAdapter);
            display.setOffscreenPageLimit(1);
            display.setCurrentItem(1);
        } else {
            RxBusManager.getInstance().post(searchResultBean);
        }
    }

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.view_fragment_search_music, headerLayout, false);
        view.findViewById(R.id.iv_view_fragment_search_music_back).setOnClickListener(v -> getActivity().onBackPressed());
        customEditText = view.findViewById(R.id.cet_view_fragment_search_music_search);
        customEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {//搜索按键action
                String content = customEditText.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showShortToast("输入内容不能为空");
                    return true;
                }
                presenter.searchContent(content);
                return true;
            }
            return false;
        });
        customEditText.setFocusable(true);
        customEditText.setFocusableInTouchMode(true);
        customEditText.requestFocus();
        return view;
    }


    @Override
    protected void updateView() {
    }

    @Override
    public void updateData(BaseBean baseBean) {
        if (baseBean.getCode() == 200) {
            if (baseBean.getType() == MusicUtil.BASE_TYPE_SEARCH_CONTENT) {
                SearchResultBean searchResultBean = (SearchResultBean) baseBean.getData();
                update(searchResultBean);

            }
        } else {
            ToastUtils.showShortToast(baseBean.getDesc());
        }

    }
}
