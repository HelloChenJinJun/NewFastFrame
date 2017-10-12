package com.example.chat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/11      21:28
 * QQ:             1981367757
 */
class DBConfig {
        private Context mContext;
        private String name = "happy_chat";//默认的数据库名称
        private int dbVersion = 1;//默认的数据库版本号
        private DBUpdateListener listener;

        public void setContext(Context context) {
                this.mContext = context;
        }


        public void setName(String dbName) {
                this.name = dbName;
        }

        public Context getContext() {
                return mContext;
        }

        public String getName() {
                return name;
        }

        int getDbVersion() {
                return dbVersion;
        }

        public void setDbVersion(int dbVersion) {
                this.dbVersion = dbVersion;
        }

        public DBUpdateListener getListener() {
                return listener;
        }

        public void setListener(DBUpdateListener listener) {
                this.listener = listener;
        }

        interface DBUpdateListener {
                void onUpdate(SQLiteDatabase db, int oldVersion, int newVersion);
        }
}
