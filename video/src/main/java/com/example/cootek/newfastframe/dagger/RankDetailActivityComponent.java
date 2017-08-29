package com.example.cootek.newfastframe.dagger;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.example.cootek.newfastframe.ui.RankDetailActivity;

import dagger.Component;

/**
 * Created by COOTEK on 2017/8/16.
 */

@PerActivity
@Component(dependencies = MainComponent.class, modules ={ RankDetailModule.class})
public interface RankDetailActivityComponent {
    public void inject(RankDetailActivity rankDetailActivity);
}
