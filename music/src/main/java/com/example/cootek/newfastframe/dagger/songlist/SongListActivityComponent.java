package com.example.cootek.newfastframe.dagger.songlist;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.example.cootek.newfastframe.dagger.main.MainComponent;
import com.example.cootek.newfastframe.ui.SongListActivity;

import dagger.Component;

/**
 * Created by COOTEK on 2017/8/16.
 */

@PerActivity
@Component(dependencies = MainComponent.class, modules = {SongListModule.class})
public interface SongListActivityComponent {
    public void inject(SongListActivity rankDetailActivity);
}
