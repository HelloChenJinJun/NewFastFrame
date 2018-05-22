package com.example.cootek.newfastframe.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.skin.LoadSkinListener;
import com.example.commonlibrary.skin.SkinManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.SkinUtil;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.MusicService;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.VideoApplication;
import com.example.cootek.newfastframe.adapter.LocalMusicAdapter;
import com.example.cootek.newfastframe.dagger.listener.DaggerLocalMusicActivityComponent;
import com.example.cootek.newfastframe.dagger.listener.LocalMusicActivityModule;
import com.example.cootek.newfastframe.event.MusicStatusEvent;
import com.example.cootek.newfastframe.mvp.main.MainPresenter;
import com.example.cootek.newfastframe.ui.fragment.SlideMusicBaseActivity;
import com.example.cootek.newfastframe.util.MusicUtil;
import com.example.cootek.newfastframe.util.PinYinUtil;
import com.example.cootek.newfastframe.view.IndexView;
import com.example.cootek.newfastframe.view.OnDragDeltaChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/11/5      20:09
 * QQ:             1981367757
 */

public class LocalMusicActivity extends SlideMusicBaseActivity<List<MusicPlayBean>, MainPresenter> implements View.OnClickListener, IndexView.MyLetterChangeListener, OnDragDeltaChangeListener {
    private SuperRecyclerView display;
    @Inject
    LocalMusicAdapter adapter;
    private WrappedLinearLayoutManager manager;
    private LinearLayout header,searchHeader;
    private int prePosition=-1;
    private int preDetailPosition=-1;
    private TextView index;
    @Override
    public void updateData(List<MusicPlayBean> o) {
        Collections.sort(o, new Comparator<MusicPlayBean>() {
            @Override
            public int compare(MusicPlayBean o1, MusicPlayBean o2) {
                return PinYinUtil.getPinYin(o1.getSongName()).compareTo(PinYinUtil.getPinYin(o2.getSongName()));
            }
        });
        adapter.addData(o);
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
        return R.layout.activity_local_music;
    }

    @Override
    protected void initView() {
            display= (SuperRecyclerView) findViewById(R.id.srcv_activity_local_music_display);
            header= (LinearLayout) findViewById(R.id.ll_activity_local_music_header);
        searchHeader= (LinearLayout) findViewById(R.id.ll_activity_local_music_header_search);
        IndexView indexView = (IndexView) findViewById(R.id.lv_activity_local_music_index);
        index= (TextView) findViewById(R.id.tv_activity_local_music_index);
        indexView.setDistance(dragLayout.getDistance());
        dragLayout.setListener(this);
        indexView.setListener(this);
        findViewById(R.id.iv_activity_local_music_search).setOnClickListener(this);
        findViewById(R.id.tv_activity_local_music_cancel).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        DaggerLocalMusicActivityComponent.builder().localMusicActivityModule(new LocalMusicActivityModule(this))
                .mainComponent(VideoApplication.getMainComponent())
                .build().inject(this);
        display.setLayoutManager(manager=new WrappedLinearLayoutManager(this));
        display.addItemDecoration(new ListViewDecoration(this));
        adapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {

            }


            @Override
            public boolean onItemLongClick(int position, View view) {
                CommonLogger.e("长按");
                return true;
            }


            @Override
            public void onItemChildClick(int position, View view, int id) {
                if (id == R.id.rl_item_activity_local_music_normal) {
                   dealItemClick(position);
                } else if (id == R.id.iv_item_activity_local_music_detail_more||id==R.id.iv_item_activity_local_music_more) {
                    if (adapter.getData(position + 1) != null
                            && adapter.getData(position + 1).getType() == MusicPlayBean.ACTION) {
                        adapter.removeData(position+1);
                        preDetailPosition=-1;
                        if (position + 1 < prePosition) {
                            prePosition--;
                        }
                    } else{
                        MusicPlayBean item=adapter.getData(position);
                        if (item == null) {
                            return;
                        }


                        MusicPlayBean newItem=new MusicPlayBean(item.getSongId(),item.getAlbumId()
                        ,item.getArtistId(),item.getSongName(),item.getAlbumName(),item.getArtistName()
                        ,item.getAlbumUrl(),item.getLrcUrl(),item.getSongUrl(),item.getDuration(),
                                item.getIsLocal(),item.getTingId(),item.getIsRecent(),0);
                        newItem.setType(MusicPlayBean.ACTION);
                        adapter.addData(position+1,newItem);
                        if (prePosition != -1 && prePosition > position) {
                            prePosition++;
                        }
                        if (preDetailPosition != -1) {
                            if (position > preDetailPosition) {
                                adapter.removeData(preDetailPosition);
                                if (prePosition != -1 && prePosition>preDetailPosition) {
                                    prePosition--;
                                }
                                preDetailPosition=position;
                            }else {
                                adapter.removeData(preDetailPosition+1);
                                if (prePosition != -1 && prePosition>preDetailPosition+1) {
                                    prePosition--;
                                }
                                preDetailPosition=position+1;
                            }
                        }else {
                            preDetailPosition=position+1;
                        }
                    }
                }
            }
        });
        display.setAdapter(adapter);
        ToolBarOption toolBarOption=new ToolBarOption();
        String title="本地音乐"+"("+getIntent().getIntExtra(MusicUtil.SONG_COUNT,0)+")";
        toolBarOption.setTitle(title);
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setRightResId(R.drawable.ic_more);
        setToolBar(toolBarOption);
        presenter.registerEvent(MusicStatusEvent.class, new Consumer<MusicStatusEvent>() {
            @Override
            public void accept(@NonNull MusicStatusEvent musicStatusEvent) throws Exception {
                if (musicStatusEvent.getCurrentStatus().equals(MusicStatusEvent.META_CHANGED)) {
                    int position=adapter.getPositionFromId(musicStatusEvent.getMusicContent().getId());
                    if (position != -1) {
                        dealItemClick(position);
                    }
                }
            }
        });
        display.post(new Runnable() {
            @Override
            public void run() {
                presenter.getAllMusic(true, true);
            }
        });
    }

    private void dealItemClick(int position) {
        List<MusicPlayBean> list=new ArrayList<>();
        for (MusicPlayBean bean :
                adapter.getData()) {
            if (bean.getType()!=MusicPlayBean.ACTION) {
                list.add(bean);
            }
        }
        MusicManager.getInstance().play(list, position, MusicService.MODE_NORMAL);
//        MusicPlayBean bean=adapter.removeData(position);
//        bean.setType(MusicPlayBean.DETAIL);
//        adapter.addData(position,bean);
        adapter.getData(position).setType(MusicPlayBean.DETAIL);
        adapter.notifyDataSetChanged();
        if (prePosition != -1) {
//            MusicPlayBean  removeBean=adapter.removeData(prePosition);
//            removeBean.setType(MusicPlayBean.NORMAL);
//            adapter.addData(prePosition,removeBean);
            adapter.getData(prePosition).setType(MusicPlayBean.NORMAL);
            adapter.notifyDataSetChanged();

        }
        prePosition=position;
    }

    public static void start(Activity activity,int count) {
        Intent intent=new Intent(activity,LocalMusicActivity.class);
        intent.putExtra(MusicUtil.SONG_COUNT,count);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id == R.id.iv_activity_local_music_search) {
            header.setVisibility(View.INVISIBLE);
            searchHeader.setVisibility(View.VISIBLE);
        } else if (id == R.id.tv_activity_local_music_cancel) {
            header.setVisibility(View.VISIBLE);
            searchHeader.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLetterChanged(String s) {
        index.setVisibility(View.VISIBLE);
        index.setText(s);
        int size=adapter.getData().size();
        for (int i = 0; i < size; i++) {
            MusicPlayBean bean= adapter.getData(i);
            if (PinYinUtil.getSortedKey(bean.getSongName()).equals(s)) {
                manager.scrollToPositionWithOffset(i,0);
                break;
            }
        }
    }

    @Override
    public void onFinished() {
        index.setVisibility(View.GONE);

    }

    @Override
    public void onDrag(View view, float delta) {


    }

    @Override
    public void onCloseMenu() {
        iSlider.unlock();
    }

    @Override
    public void onOpenMenu() {
        iSlider.lock();
    }
}
