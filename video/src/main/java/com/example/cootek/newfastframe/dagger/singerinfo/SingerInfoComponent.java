package com.example.cootek.newfastframe.dagger.singerinfo;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.example.cootek.newfastframe.dagger.main.MainComponent;
import com.example.cootek.newfastframe.ui.SingerInfoActivity;

import dagger.Component;

/**
 * Created by COOTEK on 2017/9/3.
 */
@PerActivity
@Component(dependencies = MainComponent.class, modules = SingerInfoModule.class)
public interface SingerInfoComponent {
    public void inject(SingerInfoActivity singerInfoActivity);
}
