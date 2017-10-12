package com.example.chat.manager;


import com.example.chat.bean.User;
import com.example.chat.db.ChatDB;
import com.example.chat.util.BmobUtils;
import com.example.chat.util.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/15      11:51
 * QQ:             1981367757
 */

public class UserCacheManager {
        private static UserCacheManager instance;
        //        好友列表缓存
        private Map<String, User> contacts;


        private volatile boolean isLogin = false;
        /**
         * 黑名单好友
         */
        private Map<String, User> blackList;
        private User mUser;


        public void setLogin(boolean login) {
                isLogin = login;
        }

        public User getUser() {
                if (!isLogin) {
                        return null;
                }
                if (mUser == null) {
                        mUser = UserManager.getInstance().getCurrentUser();
                }
                return mUser;
        }


        public static UserCacheManager getInstance() {
                if (instance == null) {
                        instance = new UserCacheManager();
                }
                return instance;
        }


        private UserCacheManager() {
                contacts = new HashMap<>();
                blackList = new HashMap<>();
        }

        public Map<String, User> getContacts() {
                if (contacts.size() == 0) {
                        List<User> contactsWithoutBlack = ChatDB.create().getContactsWithoutBlack();
                        LogUtil.e("从数据库中获取得到的用户");
                        if (contactsWithoutBlack != null) {
                                for (User user :
                                        contactsWithoutBlack) {
                                        LogUtil.e(user);
                                }
                        }
                        if (contactsWithoutBlack != null && contactsWithoutBlack.size() > 0) {
                                contacts.putAll(BmobUtils.list2map(contactsWithoutBlack));
                        }
                }
                return contacts;
        }

        public void setContactsList(Map<String, User> friends) {
                if (!isLogin) {
                        return;
                }
                if (friends != null) {
                        contacts.clear();
                        contacts.putAll(friends);
                        LogUtil.e("全部的用户列表");
                        for (User user :
                                friends.values()) {
                                LogUtil.e(user);
                        }
                }
        }

        public void addContact(User user) {
                if (!isLogin) {
                        return;
                }
                LogUtil.e("添加好友到内存总成功");
                if (user != null) {
                        LogUtil.e(user);

                        if (!contacts.containsKey(user.getObjectId())) {
                                contacts.put(user.getObjectId(), user);
                        } else {
//                                更新内存中用户数据
                                contacts.remove(user.getObjectId());
                                contacts.put(user.getObjectId(), user);
                        }
                }
        }

        public List<User> getContactsList() {
                if (!isLogin) {
                        return null;
                }
                List<User> list = new ArrayList<>();
                if (contacts == null || contacts.size() == 0) {
                        if (contacts == null) {
                                contacts = new HashMap<>();
                        }
                        List<User> contactsWithoutBlack = ChatDB.create().getContactsWithoutBlack();
                        LogUtil.e("从数据库中获取得到的用户");
                        if (contactsWithoutBlack != null) {
                                for (User user :
                                        contactsWithoutBlack) {
                                        LogUtil.e(user);
                                }
                        }
                        if (contactsWithoutBlack != null && contactsWithoutBlack.size() > 0) {
                                contacts.putAll(BmobUtils.list2map(contactsWithoutBlack));
                        }
                }
                list.addAll(BmobUtils.map2list(contacts));
                return list;
        }


        public List<User> getAllContacts() {
                if (!isLogin) {
                        return null;
                }
                List<User> list = new ArrayList<>();
                if (getContactsList().size() > 0) {
                        list.addAll(getContactsList());
                }
                if (getAllBlackUser().size() > 0) {
                        list.addAll(getAllBlackUser());
                }
                Collections.sort(list);
                return list;
        }

        public User getUser(String uid) {

                if (!isLogin) {
                        return null;
                }
                if (uid != null) {
                        if (contacts.containsKey(uid)) {
                                return contacts.get(uid);
                        } else {
                                User user = ChatDB.create().getContact(uid);
                                if (user != null) {
                                        contacts.put(user.getObjectId(), user);
                                }
                                return user;
                        }
                }
                return null;
        }


        public void clear() {
                if (contacts != null) {
                        contacts.clear();
                }
                LogUtil.e("好友内存数据清除");
                if (blackList != null) {
                        blackList.clear();
                }
                LogUtil.e("黑名单内存数据清除");
                mUser = null;
        }

        /**
         * 删除用户
         *
         * @param uid 用户名
         */
        public void deleteUser(String uid) {
                if (!isLogin) {
                        return;
                }
                LogUtil.e("sh");
                if (uid != null) {
                        if (getContacts() != null&&getContacts().containsKey(uid)) {
                                LogUtil.e("开始删除用户啦啦啦");
                                getContacts().remove(uid);
                        }
                }
        }


        public void addBlackUser(User user) {
                if (!isLogin) {
                        return;
                }
                if (user != null) {
                        if (getAllBlackMap()!=null&&getAllBlackMap().containsKey(user.getObjectId()))
                                getAllBlackMap().put(user.getObjectId(), user);
                }
        }



        public Map<String,User> getAllBlackMap(){
                if (blackList.size() == 0) {
                        List<User> blackUser = ChatDB.create().getAllBlackUser();
                        LogUtil.e("从数据库中获取得到的黑名单用户");
                        if (blackUser != null) {
                                for (User user :
                                        blackUser) {
                                        LogUtil.e(user);
                                }
                        }
                        if (blackUser != null && blackUser.size() > 0) {
                                blackList.putAll(BmobUtils.list2map(blackUser));
                        }
                }
                return blackList;
        }



        public User getBlackUser(String uid) {
                if (!isLogin) {
                        return null;
                }
                if (uid != null) {
                        if (getAllBlackMap()!=null&&getAllBlackMap().containsKey(uid)) {
                                return getAllBlackMap().get(uid);
                        }
                }
                return null;
        }

        public void deleteBlackUser(String uid) {
                if (!isLogin) {
                        return;
                }
                if (uid != null) {

                        if (getAllBlackMap()!=null&&getAllBlackMap().containsKey(uid)) {
                                getAllBlackMap().remove(uid);
                        }
                }
        }

        List<String> getAllUserId() {
                if (!isLogin) {
                        return null;
                }
                List<String> list = null;
                if (getContacts().keySet().size() > 0) {
                        list = new ArrayList<>(getContacts().keySet());
                }
                return list;
        }

        public List<User> getAllBlackUser() {
                if (!isLogin) {
                        return null;
                }
                return BmobUtils.map2list(getAllBlackMap());
        }

//        public void addInstallationId(String uid, String installationId) {
//                if (!isLogin||uid==null||installationId==null) {
//                        return;
//                }
//                installationMap.put(uid, installationId);
//        }
//
//        public String getInstallationId(String toId) {
//                if (!isLogin||toId==null) {
//                        return null;
//                }
//                return installationMap.get(toId);
//        }
}
