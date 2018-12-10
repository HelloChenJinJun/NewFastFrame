package com.example.cootek.newfastframe.dagger.lrc;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.example.cootek.newfastframe.dagger.main.MainComponent;
import com.example.cootek.newfastframe.mvp.lrc.LrcListFragment;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/7     18:06
 */
@PerFragment
@Component(dependencies = MainComponent.class, modules = LrcListFragmentModule.class)
public interface LrcListFragmentComponent {
    public void inject(LrcListFragment lrcListFragment);

}
