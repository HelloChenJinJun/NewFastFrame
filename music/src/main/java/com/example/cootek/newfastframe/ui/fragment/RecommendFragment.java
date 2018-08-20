package com.example.cootek.newfastframe.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemChildClickListener;
import com.example.commonlibrary.BaseFragment;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.VideoApplication;
import com.example.cootek.newfastframe.adapter.RecommendAlbumAdapter;
import com.example.cootek.newfastframe.adapter.RecommendRadioAdapter;
import com.example.cootek.newfastframe.adapter.RecommendSongListAdapter;
import com.example.cootek.newfastframe.bean.RecommendSongBean;
import com.example.cootek.newfastframe.dagger.recommend.DaggerRecommendComponent;
import com.example.cootek.newfastframe.dagger.recommend.RecommendModule;
import com.example.cootek.newfastframe.mvp.recommend.RecommendPresenter;
import com.example.cootek.newfastframe.ui.SongListActivity;
import com.example.cootek.newfastframe.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by COOTEK on 2017/9/1.
 */

public class RecommendFragment extends BaseFragment<RecommendSongBean, RecommendPresenter> implements View.OnClickListener {
    private RelativeLayout songMore;
    private RelativeLayout radioMore;
    private RelativeLayout albumMore;
    private SuperRecyclerView songDisplay;
    private SuperRecyclerView radioDisplay;
    private SuperRecyclerView albumDisplay;
    @Inject
    RecommendRadioAdapter recommendRadioAdapter;
    @Inject
    RecommendSongListAdapter recommendSongListAdapter;
    @Inject
    RecommendAlbumAdapter recommendAlbumAdapter;

    @Override
    public void updateData(RecommendSongBean recommendSongBean) {
        if (recommendSongBean.getResult() != null) {
            if (recommendSongBean.getResult().getRadio() != null && recommendSongBean.getResult().getRadio()
                    .getResult() != null && recommendSongBean.getResult().getRadio().getResult().size() > 0) {
                List<RecommendSongBean.ResultBeanXXXXXXXXXXXXXXXX.RadioBean.ResultBeanXXXXXXXXXXX> list =
                        new ArrayList<>();
                int size = recommendSongBean.getResult().getRadio().getResult().size();
                for (int i = 0; i < size; i++) {
                    list.add(recommendSongBean.getResult().getRadio().getResult().get(i));
                    if (i == 5) {
                        break;
                    }
                }
                recommendRadioAdapter.addData(list);
            }

            if (recommendSongBean.getResult().getDiy() != null && recommendSongBean.getResult().getDiy()
                    .getResult() != null && recommendSongBean.getResult().getDiy().getResult().size() > 0) {
                List<RecommendSongBean.ResultBeanXXXXXXXXXXXXXXXX.DiyBean.ResultBeanXXXXXXXXX> list =
                        new ArrayList<>();
                int size = recommendSongBean.getResult().getDiy().getResult().size();
                for (int i = 0; i < size; i++) {
                    list.add(recommendSongBean.getResult().getDiy().getResult().get(i));
                    if (i == 5) {
                        break;
                    }
                }
                recommendSongListAdapter.addData(list);
            }
            if (recommendSongBean.getResult().getMix_1() != null && recommendSongBean.getResult().getMix_1()
                    .getResult() != null && recommendSongBean.getResult().getMix_1().getResult().size() > 0) {
                List<RecommendSongBean.ResultBeanXXXXXXXXXXXXXXXX.Mix1Bean.ResultBean> list =
                        new ArrayList<>();
                int size = recommendSongBean.getResult().getMix_1().getResult().size();
                for (int i = 0; i < size; i++) {
                    list.add(recommendSongBean.getResult().getMix_1().getResult().get(i));
                    if (i == 5) {
                        break;
                    }
                }
                recommendAlbumAdapter.addData(list);
            }
        }
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
    protected boolean needStatusPadding() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView() {
        radioMore = (RelativeLayout) findViewById(R.id.rl_fragment_recommend_radio_more);
        songMore = (RelativeLayout) findViewById(R.id.rl_fragment_recommend_song_list_more);
        radioDisplay = (SuperRecyclerView) findViewById(R.id.srcv_fragment_recommend_radio);
        songDisplay = (SuperRecyclerView) findViewById(R.id.srcv_fragment_recommend_song_list);
        albumDisplay = (SuperRecyclerView) findViewById(R.id.srcv_fragment_recommend_album);
        albumMore = (RelativeLayout) findViewById(R.id.rl_fragment_recommend_album_more);
        albumMore.setOnClickListener(this);
        radioMore.setOnClickListener(this);
        songMore.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        DaggerRecommendComponent.builder().mainComponent(VideoApplication.getMainComponent())
                .recommendModule(new RecommendModule(this)).build().inject(this);
        songDisplay.setLayoutManager(new WrappedGridLayoutManager(getContext(), 3));
        radioDisplay.setLayoutManager(new WrappedGridLayoutManager(getContext(), 3));
        albumDisplay.setLayoutManager(new WrappedGridLayoutManager(getContext(), 3));
        radioDisplay.setNestedScrollingEnabled(false);
        songDisplay.setNestedScrollingEnabled(false);
        albumDisplay.setNestedScrollingEnabled(false);
        songDisplay.setAdapter(recommendSongListAdapter);
        radioDisplay.setAdapter(recommendRadioAdapter);
        albumDisplay.setAdapter(recommendAlbumAdapter);
        recommendSongListAdapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {
            @Override
            public void onItemChildClick(int position, View view, int id) {
                RecommendSongBean.ResultBeanXXXXXXXXXXXXXXXX.DiyBean.ResultBeanXXXXXXXXX data = recommendSongListAdapter.getData(position);
                Intent intent = new Intent(getContext(), SongListActivity.class);
                intent.putExtra(MusicUtil.FROM, MusicUtil.FROM_SONG_MENU);
                intent.putExtra(MusicUtil.LIST_ID, data.getListid());
                startActivity(intent);
            }
        });
        recommendRadioAdapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {
            @Override
            public void onItemChildClick(int position, View view, int id) {
                RecommendSongBean.ResultBeanXXXXXXXXXXXXXXXX.RadioBean.ResultBeanXXXXXXXXXXX data = recommendRadioAdapter.getData(position);
                Intent intent = new Intent(getContext(), SongListActivity.class);
                intent.putExtra(MusicUtil.FROM, MusicUtil.FROM_RADIO);
                intent.putExtra(MusicUtil.RADIO_ID, data.getAlbum_id());
                startActivity(intent);
            }
        });
        recommendAlbumAdapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {
            @Override
            public void onItemChildClick(int position, View view, int id) {
                RecommendSongBean.ResultBeanXXXXXXXXXXXXXXXX.Mix1Bean.ResultBean data = recommendAlbumAdapter.getData(position);
                Intent intent = new Intent(getContext(), SongListActivity.class);
                intent.putExtra(MusicUtil.FROM, MusicUtil.FROM_ALBUM);
                intent.putExtra(MusicUtil.ALBUM_ID, data.getType_id());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void updateView() {
        presenter.getRecommendData();
    }

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_fragment_recommend_radio_more) {

        } else if (id == R.id.rl_fragment_recommend_song_list_more) {

        } else if (id == R.id.rl_fragment_recommend_album_more) {

        }
    }
}
