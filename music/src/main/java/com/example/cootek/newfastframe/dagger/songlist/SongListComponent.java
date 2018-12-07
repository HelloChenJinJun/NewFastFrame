package com.example.cootek.newfastframe.dagger.songlist;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.example.cootek.newfastframe.dagger.main.MainComponent;
import com.example.cootek.newfastframe.mvp.songlist.SongListFragment;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/7     11:14
 */
@PerFragment
@Component(dependencies = MainComponent.class, modules = SongListModule.class)
public interface SongListComponent {
    public void inject(SongListFragment songListFragment);
}
