package com.example.chat.base;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/1      22:09
 * QQ:             1981367757
 */
public class UserSharePreferences {

        private static final String IS_LOGIN = "is_login";
        private static UserSharePreferences instance;
        private static final String KEY_USER = "user";
        private SharedPreferences mSharedPreferences;
        private SharedPreferences.Editor mEditor;

        private UserSharePreferences(Context context) {
                mSharedPreferences = context.getSharedPreferences(KEY_USER, Context.MODE_PRIVATE);
                mEditor = mSharedPreferences.edit();
        }

        public static UserSharePreferences getInstance(Context context) {
                if (instance == null)
                        instance = new UserSharePreferences(context);
                return instance;
        }


        public boolean putIsLogin(boolean isLogin) {
                mEditor.putBoolean(IS_LOGIN, isLogin);
                return mEditor.commit();
        }


        public boolean isLogin() {
                return mSharedPreferences.getBoolean(IS_LOGIN, false);
        }
}
