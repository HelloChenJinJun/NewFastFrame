package com.example.commonlibrary.repository;

import org.greenrobot.greendao.AbstractDaoSession;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * Created by COOTEK on 2017/8/11.
 */

public class BaseRepositoryManager<D extends AbstractDaoSession> implements IRepositoryManager<D> {
    protected Retrofit retrofit;
    private D daoSession;
    private Map<String, Object> stringRetrofitMap;

    public BaseRepositoryManager(Retrofit retrofit, D abstractDaoSession) {
        this.retrofit = retrofit;
        this.daoSession = abstractDaoSession;
        stringRetrofitMap = new HashMap<>();
    }

    @Override
    public <T> T getApi(Class<T> retrofitClass) {
        T result = null;
        if (stringRetrofitMap != null) {
            synchronized (stringRetrofitMap) {
                result = (T) stringRetrofitMap.get(retrofitClass.getName());
                if (result == null) {
                    result = retrofit.create(retrofitClass);
                    stringRetrofitMap.put(retrofitClass.getName(), result);
                }
            }
        }
        return result;
    }

    @Override
    public <T> void clearApi(Class<T> retrofitClass) {
        if (stringRetrofitMap != null) {
            synchronized (stringRetrofitMap) {
                if (stringRetrofitMap.containsKey(retrofitClass.getName())) {
                    stringRetrofitMap.remove(retrofitClass.getName());
                }
            }
        }
    }

    @Override
    public D getDaoSession() {
        return daoSession;
    }

    @Override
    public void clearAllCache() {
        if (stringRetrofitMap != null) {
            synchronized (stringRetrofitMap) {
                stringRetrofitMap.clear();
            }
        }

    }
}
