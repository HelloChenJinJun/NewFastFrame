package com.example.cootek.newfastframe.dagger.singerlist;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.example.cootek.newfastframe.dagger.main.MainComponent;
import com.example.cootek.newfastframe.ui.fragment.SingerListFragment;

import dagger.Component;

/**
 * Created by COOTEK on 2017/9/2.
 */
@PerFragment
@Component(dependencies = MainComponent.class, modules = SingerListModule.class)
public interface SingerListComponent {
    public void inject(SingerListFragment singerListFragment);
}
