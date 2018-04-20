package com.example.chat.dagger.person;


import com.example.chat.MainRepositoryManager;
import com.example.chat.mvp.person.PersonFragment;
import com.example.chat.mvp.person.PersonModel;
import com.example.chat.mvp.person.PersonPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/12     19:34
 * QQ:         1981367757
 */
@Module
public class PersonModule {
    private PersonFragment personFragment;

    public PersonModule(PersonFragment personFragment) {
        this.personFragment = personFragment;
    }


    @Provides
    public PersonPresenter providerPresenter(PersonModel personModel) {
        return new PersonPresenter(personFragment, personModel);
    }

    @Provides
    public PersonModel providerModel(MainRepositoryManager mainRepositoryManager) {
        return new PersonModel(mainRepositoryManager);
    }


}
