package com.example.news.dagger.person;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.example.news.PersonFragment;
import com.example.news.dagger.NewsComponent;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/12     19:33
 * QQ:         1981367757
 */
@PerFragment
@Component(dependencies = NewsComponent.class, modules = PersonModule.class)
public interface PersonComponent {
    public void inject(PersonFragment personFragment);
}
