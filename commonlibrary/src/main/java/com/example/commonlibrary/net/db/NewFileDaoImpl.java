package com.example.commonlibrary.net.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.net.entity.FileInfo;

import org.greenrobot.greendao.query.Query;

/**
 * Created by COOTEK on 2017/8/3.
 */

public class NewFileDaoImpl implements NewFileDAO {


    private static class HolderClass {
        private static final NewFileDaoImpl instance = new NewFileDaoImpl();
    }

    public static NewFileDaoImpl getInstance() {
        return NewFileDaoImpl.HolderClass.instance;
    }

    @Override
    public void insert(NewFileInfo info) {
        BaseApplication.getAppComponent().getDaoSesion().getNewFileInfoDao().insert(info);

    }

    @Override
    public void delete(String url) {
        NewFileInfo newFileInfo = new NewFileInfo();
        newFileInfo.setUrl(url);
        BaseApplication.getAppComponent().getDaoSesion().getNewFileInfoDao().delete(newFileInfo);

    }

    @Override
    public void update(NewFileInfo info) {
        BaseApplication.getAppComponent().getDaoSesion().getNewFileInfoDao().update(info);
    }

    @Override
    public NewFileInfo query(String url) {
        Query<NewFileInfo> query = BaseApplication.getAppComponent().getDaoSesion().getNewFileInfoDao().queryBuilder().where(NewFileInfoDao.Properties.Url.eq(url)).build();
        if (query.list() != null) {
            return query.list().get(0);
        }
        return null;
    }


    public NewFileInfo queryPkg(String pkgName) {
        Query<NewFileInfo> query = BaseApplication.getAppComponent().getDaoSesion().getNewFileInfoDao().queryBuilder()
                .where(NewFileInfoDao.Properties.Name.eq(pkgName)).build();
        return query.list() != null ? query.list().get(0) : null;
    }

    @Override
    public boolean isExists(String url) {
        return query(url) != null;
    }
}
