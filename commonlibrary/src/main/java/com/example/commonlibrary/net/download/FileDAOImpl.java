package com.example.commonlibrary.net.download;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.commonlibrary.BaseApplication;

import java.util.List;


/**
 * 数据库访问实现类
 */
public class FileDAOImpl implements FileDAO {
    private FileInfoDao fileInfoDao;
    private FileDAOImpl() {
        // 创建数据库
        fileInfoDao= BaseApplication.getAppComponent().getDaoSession()
                .getFileInfoDao();
    }

    private static class HolderClass {
        private static final FileDAOImpl instance = new FileDAOImpl();
    }

    public static FileDAOImpl getInstance() {
        return HolderClass.instance;
    }

    @Override
    public void insert(FileInfo info) {
        fileInfoDao.insertOrReplace(info);
    }

    @Override
    public void delete(String url) {
        fileInfoDao.deleteByKey(url);
    }

    @Override
    public void update(FileInfo info) {
        fileInfoDao.update(info);
    }

    @Override
    public FileInfo query(String fileUrl) {
        List<FileInfo>  result=fileInfoDao.queryBuilder().where(FileInfoDao.Properties.Url.eq(fileUrl)).build()
                .list();
        if (result.size() != 0) {
            return result.get(0);
        }
        return null;
    }



    @Override
    public boolean isExists(String url) {
      return fileInfoDao.queryBuilder().where(FileInfoDao.Properties.Url.eq(url))
               .buildCount().count()>0;
    }
}
