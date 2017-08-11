package com.example.cootek.newfastframe.dagger;

import com.example.cootek.newfastframe.MainActivity;
import com.example.cootek.newfastframe.MainComponent;
import com.example.cootek.newfastframe.scope.PerActivity;


import dagger.Component;

/**
 * Created by COOTEK on 2017/8/11.
 */

@PerActivity
@Component(dependencies = MainComponent.class, modules = {MainActivityModule.class})
public interface MainActivityComponent {
    public void inject(MainActivity mainActivity);
}
