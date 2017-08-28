package com.example.cootek.newfastframe;

import com.example.cootek.newfastframe.dagger.RankDetailModule;
import com.example.commonlibrary.dagger.scope.PerActivity;

import dagger.Component;

/**
 * Created by COOTEK on 2017/8/16.
 */

@PerActivity
@Component(dependencies = MainComponent.class, modules ={ RankDetailModule.class})
public interface RankDetailActivityComponent {
    public void inject(RankDetailActivity rankDetailActivity);
}
