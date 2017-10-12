package com.example.chat.manager;

import com.example.chat.bean.GroupNumberInfo;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.db.ChatDB;
import com.example.chat.util.BmobUtils;
import com.example.chat.util.ChatUtil;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/15      12:02
 * QQ:             1981367757
 */

public class MessageCacheManager {
        private static MessageCacheManager instance;
        private Map<String, GroupTableMessage> groupList;
        private List<String> allGroupId;
        private Map<String, String> userTime;
        private Map<String, String> groupMap;
        private Map<String, String> mUserDataTimeMap;
        private volatile boolean isLogin = false;


        public void setLogin(boolean login) {
                isLogin = login;
        }

        public static MessageCacheManager getInstance() {
                if (instance == null) {
                        instance = new MessageCacheManager();
                }
                return instance;
        }


        private MessageCacheManager() {
                groupList = new HashMap<>();
                allGroupId = new ArrayList<>();
                userTime = new HashMap<>();
                groupMap = new HashMap<>();
                mUserDataTimeMap = new HashMap<>();
                shareMap = new HashMap<>();
                groupNumberMap = new HashMap<>();

        }

        public String getLastGroupMessageTime(String groupId) {
                if (!isLogin) {
                        return null;
                }
                if (groupMap.get(groupId) == null) {
                        String time = BaseApplication.getAppComponent().getSharedPreferences()
                                .getString(ChatUtil.LAST_GROUP_MESSAGE_TIME, "0000-00-00 01:00:00");
                        groupMap.put(groupId, time);
                }
                return groupMap.get(groupId);
        }


        public GroupTableMessage getGroupTableMessage(String groupId) {
                if (!isLogin) {
                        return null;
                }
                if (getAllGroupTableMap().containsKey(groupId)) {
                        return getAllGroupTableMap().get(groupId);
                }
                return null;
        }

        public void addGroupTableMessage(List<GroupTableMessage> list) {
                if (list != null) {
                        for (GroupTableMessage message :
                                list) {
                                addGroupTableMessage(message);
                        }
                }
        }


        public void addGroupTableMessage(GroupTableMessage message) {
                if (!isLogin || message == null) {
                        return;
                }
                if (!getAllGroupTableMap().containsKey(message.getGroupId())) {
                        getAllGroupTableMap().put(message.getGroupId(), message);
                }
        }

        public List<String> getAllGroupId() {
                if (!isLogin) {
                        return null;
                }
                if (allGroupId.size()==0) {
                        if (getAllGroupTableMap() != null) {
                                for (GroupTableMessage groupTableMessage :
                                        getAllGroupTableMessage()) {
                                        allGroupId.add(groupTableMessage.getGroupId());
                                }
                        }
                }
                return allGroupId;
        }

        public List<GroupTableMessage> getAllGroupTableMessage() {
                if (!isLogin) {
                        return null;
                }
                if (getAllGroupTableMap() != null) {
                        return BmobUtils.map_list(getAllGroupTableMap());
                }
                return null;
        }



        public Map<String,GroupTableMessage >  getAllGroupTableMap(){
                if (!isLogin) {
                        return null;
                }
                if (groupList.size()==0) {
                        List<GroupTableMessage> list = ChatDB.create().getAllGroupMessage();
                        if ( list.size() > 0) {
                                groupList.putAll(BmobUtils.list_map(list));
                        }
                }
                return groupList;
        }


        public String getLastShareMessageTime(String uid) {
                if (!isLogin) {
                        return null;
                }
                if (userTime.get(uid) == null) {
                        String time = (String) BaseApplication.getAppComponent().getSharedPreferences().getString(ChatUtil.LAST_SHARE_MESSAGE_TIME, "0000-00-00 01:00:00");
                        userTime.put(uid, time);
                }
                return userTime.get(uid);
        }

        public void setLastShareMessageTime(String uid, String lastShareMessageTime) {
                if (!isLogin) {
                        return;
                }
                userTime.put(uid, lastShareMessageTime);
                BaseApplication.getAppComponent().getSharedPreferences().edit().putString(ChatUtil.LAST_SHARE_MESSAGE_TIME,lastShareMessageTime)
                        .apply();


        }

        public void setLastGroupMessageTime(String groupId, String lastGroupMessageTime) {
                if (!isLogin) {
                        return;
                }
                groupMap.put(groupId, lastGroupMessageTime);
                BaseApplication.getAppComponent().getSharedPreferences().edit().putString(ChatUtil.LAST_GROUP_MESSAGE_TIME,lastGroupMessageTime)
                        .apply();
                LogUtil.e("保存最新的群消息时间到外部存储中");
        }

        private Map<String, List<String>> shareMap;

        public void saveShareMessageCache(String id, List<String> list) {
                if (!isLogin) {
                        return;
                }
                shareMap.put(id, list);
        }

        public List<String> getShareMessageCache(String id) {
                if (!isLogin) {
                        return null;
                }
                return shareMap.get(id);
        }


        private Map<String, List<GroupNumberInfo>> groupNumberMap;

        public void setAllGroupNumberInfo(String groupId, List<GroupNumberInfo> list) {
                if (!isLogin) {
                        return;
                }
                groupNumberMap.put(groupId, list);
        }


        public List<GroupNumberInfo> getAllGroupNumberInfo(String groupId) {
                if (!isLogin) {
                        return null;
                }
                return groupNumberMap.get(groupId);
        }


        public void setUserDataLastUpdateTime(String objectId, String userDataLastUpdateTime) {
                if (!isLogin) {
                        return;
                }
                mUserDataTimeMap.put(objectId, userDataLastUpdateTime);
                BaseApplication.getAppComponent().getSharedPreferences().edit().putString(ChatUtil.USER_DATA_LAST_UPDATE_TIME,userDataLastUpdateTime)
                        .apply();
        }



        public String getUserDataLastUpdateTime(String objectId) {
                if (!isLogin) {
                        return null;
                }
                if (!mUserDataTimeMap.containsKey(objectId)) {
                        String time =  BaseApplication.getAppComponent().getSharedPreferences().getString(ChatUtil.USER_DATA_LAST_UPDATE_TIME,"0000-00-00 01:00:00");
                        mUserDataTimeMap.put(objectId, time);
                }
                return mUserDataTimeMap.get(objectId);
        }


        public void clear() {
                if (groupList != null) {
                        LogUtil.e("有群结构消息，群结构消息清除 ");
                        groupList.clear();
                }
                if (shareMap != null) {
                        LogUtil.e("有缓存的说说图片或视频，这里清除");
                        shareMap.clear();
                }
                LogUtil.e("临时好友内容数据清除");
                LogUtil.e("用户已退出，缓存数据已清除");
                LogUtil.e("退出时时间置为0");
                if (groupMap != null) {
                        groupMap.clear();
                }
                if (userTime != null) {
                        userTime.clear();
                }
                if (groupNumberMap != null) {
                        groupNumberMap.clear();
                }
                if (allGroupId != null) {
                        allGroupId.clear();
                }

                if (mUserDataTimeMap != null) {
                        mUserDataTimeMap.clear();
                }
        }

        public void deleteGroupTableMessage(String groupId) {
                if (groupId == null) {
                        return;
                }
                if (getAllGroupTableMap().containsKey(groupId)) {
                        LogUtil.e("删除缓存的群结构消息成功");
                        getAllGroupTableMap().remove(groupId);
                }
        }
}
