package com.example.chat.dagger.person;

import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.mvp.person.PersonFragment;
import com.example.commonlibrary.dagger.scope.PerFragment;
import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/12     19:33
 * QQ:         1981367757
 */
@PerFragment
@Component(dependencies = ChatMainComponent.class, modules = PersonModule.class)
public interface PersonComponent {
    public void inject(PersonFragment personFragment);
}
