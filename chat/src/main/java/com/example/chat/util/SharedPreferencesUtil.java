package com.example.chat.util;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/11      22:56
 * QQ:             1981367757
 */

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 轻量的消息数据存储工具
 */
public class SharedPreferencesUtil {
        private static final String KEY_PUSH_NOTIFY = "push_notify";
        private static final String KEY_VOICE = "voice";
        private static final String KEY_VIBRATE = "vibrate";
        private static final String LATLNG_KEY = "latLng_name";
        private static final String ADDRESS_KEY = "address_name";
        private static final String IS_LOGIN = "is_login";
        private static final String ACCOUNT = "account";
        private static final String PASSWORD = "password";
        private static final String IS_REMIND = "is_remind";
        private static final String DELTA_TIME = "delta_time";
        private SharedPreferences sharePreferences;
        private SharedPreferences.Editor editor = null;


        private static final String USER_ID = "user_id";
        private static final String USER_AVATAR = "user_avatar";









        public SharedPreferencesUtil(Context context, String name) {
                sharePreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
                editor = sharePreferences.edit();
        }

        public boolean isAllowPushNotify() {
                return sharePreferences.getBoolean(KEY_PUSH_NOTIFY, true);
        }

        public boolean setIsAllowPushNotify(boolean isAllow) {
                editor.putBoolean(KEY_PUSH_NOTIFY, isAllow);
                return editor.commit();
        }


        public boolean isAllowVoice() {
                return sharePreferences.getBoolean(KEY_VOICE, true);
        }

        public boolean isAllowVibrate() {
                return sharePreferences.getBoolean(KEY_VIBRATE, true);
        }

        /**
         * 存储经纬度  格式为: 经度&纬度
         *
         * @param latLng
         * @return
         */
        public boolean putLatLng(String latLng) {
                editor.putString(LATLNG_KEY, latLng);
                return editor.commit();
        }

        public String[] getLatLng() {
                String latlng = sharePreferences.getString(LATLNG_KEY, "");
                return latlng.split("&");
        }

        public boolean putAvatar(String avatar){
                editor.putString(USER_AVATAR,avatar);
                return editor.commit();
        }

        public String getAvatar(){
                return sharePreferences.getString(USER_AVATAR,null);
        }


        public boolean putUserId(String userId){
                editor.putString(USER_ID,userId);
                return editor.commit();
        }


        public String getUserId(){
                return sharePreferences.getString(USER_ID,null);
        }


        public boolean putAddress(String address) {
                editor.putString(ADDRESS_KEY, address);
                return editor.commit();
        }

        public String getAddress() {
                return sharePreferences.getString(ADDRESS_KEY, "");
        }

        public String getLastGroupMessageTime(String groupId) {
                return sharePreferences.getString(groupId, "0000-00-00 01:00:00");
        }

        public boolean putLastGroupMessageTime(String groupId, String lastGroupMessageTime) {
                editor.putString(groupId, lastGroupMessageTime);
                return editor.commit();
        }

        public String getLastShareMessageTime(String uid) {
                return sharePreferences.getString(uid, "0000-00-00 01:00:00");
        }

        public boolean putLastShareMessageTime(String uid, String lastShareMessageTime) {
                editor.putString(uid, lastShareMessageTime);
                return editor.commit();
        }

        public boolean putLastUserDataUpdateTime(String objectId, String userDataLastUpdateTime) {
                editor.putString(objectId, userDataLastUpdateTime);
                return editor.commit();
        }

        public String getLastUserDataUpdateTime(String objectId) {
                return sharePreferences.getString(objectId, "0000-00-00 01:00:00");
        }

        public boolean isLogin() {
                return sharePreferences.getBoolean(IS_LOGIN, false);
        }

        public boolean setLogin(boolean isLogin) {
                editor.putBoolean(IS_LOGIN, isLogin);
                return editor.commit();
        }


        public boolean isRemind(String groupId) {
                return sharePreferences.getBoolean(groupId, true);
        }


        public boolean setRemind(boolean isRemind, String groupId) {
                editor.putBoolean(groupId, isRemind);
                return editor.commit();
        }

        public void setDeltaTime(long deltaTime) {
                editor.putLong(DELTA_TIME, deltaTime);
        }

        public long getDeltaTime() {
                return sharePreferences.getLong(DELTA_TIME, -1L);
        }
}
