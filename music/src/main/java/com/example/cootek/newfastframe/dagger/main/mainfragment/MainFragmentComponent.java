package com.example.cootek.newfastframe.dagger.main.mainfragment;

import com.example.cootek.newfastframe.dagger.main.MainComponent;
import com.example.cootek.newfastframe.ui.fragment.LocalListFragment;
import com.example.commonlibrary.dagger.scope.PerFragment;

import dagger.Component;

/**
 * Created by COOTEK on 2017/8/13.
 */
@PerFragment
@Component(dependencies = MainComponent.class, modules = {MainFragmentModule.class})
public interface MainFragmentComponent {
    public void inject(LocalListFragment mainFragment);
}

