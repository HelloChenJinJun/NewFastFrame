package com.example.cootek.newfastframe.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.commonlibrary.BaseFragment;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.ui.LocalMusicListActivity;
import com.example.cootek.newfastframe.ui.MusicRepositoryActivity;

/**
 * Created by COOTEK on 2017/9/1.
 */

public class MainFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout like, songList, downLoad, recent, localMusic, musicRepository;
    private ImageView play;


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
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        like = (LinearLayout) findViewById(R.id.ll_fragment_main_like);
        songList = (LinearLayout) findViewById(R.id.ll_fragment_main_songList);
        downLoad = (LinearLayout) findViewById(R.id.ll_fragment_main_down);
        recent = (LinearLayout) findViewById(R.id.ll_fragment_main_recent);
        localMusic = (LinearLayout) findViewById(R.id.ll_fragment_main_local_music);
        musicRepository = (LinearLayout) findViewById(R.id.ll_fragment_main_music_repository);
        play = (ImageView) findViewById(R.id.iv_fragment_main_play);
        like.setOnClickListener(this);
        songList.setOnClickListener(this);
        downLoad.setOnClickListener(this);
        recent.setOnClickListener(this);
        musicRepository.setOnClickListener(this);
        localMusic.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void updateView() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_fragment_main_recent) {


        } else if (i == R.id.ll_fragment_main_like) {

        } else if (i == R.id.ll_fragment_main_down) {

        } else if (i == R.id.ll_fragment_main_songList) {

        } else if (i == R.id.ll_fragment_main_local_music) {
            Intent intent = new Intent(getContext(), LocalMusicListActivity.class);
            startActivity(intent);
        } else if (i == R.id.ll_fragment_main_music_repository) {
            Intent intent = new Intent(getContext(), MusicRepositoryActivity.class);
            startActivity(intent);
        } else if (i == R.id.iv_fragment_main_play) {

        } else {

        }
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }
}
