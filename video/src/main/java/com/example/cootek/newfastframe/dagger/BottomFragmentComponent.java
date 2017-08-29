package com.example.cootek.newfastframe.dagger;

import com.example.cootek.newfastframe.ui.BottomFragment;
import com.example.commonlibrary.dagger.scope.PerFragment;

import dagger.Component;

/**
 * Created by COOTEK on 2017/8/14.
 */
@PerFragment
@Component(dependencies = MainComponent.class, modules = {BottomFragmentModule.class})
public interface BottomFragmentComponent {
    public void inject(BottomFragment bottomFragment);
}
