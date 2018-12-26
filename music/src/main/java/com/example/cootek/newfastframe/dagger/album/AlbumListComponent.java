package com.example.cootek.newfastframe.dagger.album;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.example.cootek.newfastframe.dagger.main.MainComponent;
import com.example.cootek.newfastframe.mvp.search.AlbumListFragment;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/26     15:44
 */
@PerFragment
@Component(dependencies = MainComponent.class, modules = AlbumListModule.class)
public interface AlbumListComponent {
    public void inject(AlbumListFragment albumListFragment);
}
