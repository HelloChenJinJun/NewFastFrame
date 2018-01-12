package com.example.news.dagger.person;

import com.example.news.MainRepositoryManager;
import com.example.news.PersonFragment;
import com.example.news.mvp.person.PersonModel;
import com.example.news.mvp.person.PersonPresenter;

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
