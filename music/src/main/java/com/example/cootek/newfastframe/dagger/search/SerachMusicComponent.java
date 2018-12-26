package com.example.cootek.newfastframe.dagger.search;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.example.cootek.newfastframe.dagger.main.MainComponent;
import com.example.cootek.newfastframe.mvp.search.SearchMusicFragment;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/26     13:32
 */
@PerFragment
@Component(dependencies = MainComponent.class, modules = SearchMusicModule.class)
public interface SerachMusicComponent {
    public void inject(SearchMusicFragment searchMusicFragment);
}
