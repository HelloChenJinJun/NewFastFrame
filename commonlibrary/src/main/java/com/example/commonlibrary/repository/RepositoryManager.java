package com.example.commonlibrary.repository;

import com.example.commonlibrary.net.db.DaoSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Retrofit;

/**
 * Created by COOTEK on 2017/7/31.
 */
@Singleton
public class RepositoryManager implements IRepositoryManager {


    private Map<String, Object> stringRetrofitMap;
    private Retrofit retrofit;
    private DaoSession daoSession;

    @Inject
    public RepositoryManager(Retrofit retrofit, DaoSession daoSession) {
        this.retrofit = retrofit;
        stringRetrofitMap = new HashMap<>();
        this.daoSession = daoSession;
    }

    @Override
    public <T> T getApi(Class<T> retrofitClass) {
        T result;
        synchronized (stringRetrofitMap) {
            result = (T) stringRetrofitMap.get(retrofitClass.getName());
            if (result == null) {
                result = retrofit.create(retrofitClass);
                stringRetrofitMap.put(retrofitClass.getName(), result);
            }
        }
        return result;
    }

    @Override
    public <T> void clearApi(Class<T> retrofitClass) {
        synchronized (stringRetrofitMap) {
            if (stringRetrofitMap.get(retrofitClass.getName()) != null) {
                stringRetrofitMap.remove(retrofitClass.getName());
            }
        }
    }

    @Override
    public void clearAllCache() {
        if (stringRetrofitMap != null) {
            synchronized (stringRetrofitMap) {
                stringRetrofitMap.clear();
            }
        }
    }

    @Override
    public DaoSession getDaoSession() {
        return daoSession;
    }

}
