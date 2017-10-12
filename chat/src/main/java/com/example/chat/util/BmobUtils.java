package com.example.chat.util;

import com.example.chat.bean.GroupTableMessage;
import com.example.chat.bean.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/11      23:43
 * QQ:             1981367757
 */
public class BmobUtils {
        public static Map<String, User> list2map(List<User> black) {
                Map<String, User> map = new HashMap<>();
                for (User user :
                        black) {
                        map.put(user.getObjectId(), user);
                }
                return map;
        }

        public static List<User> map2list(Map<String, User> contacts) {
                List<User> list = new ArrayList<>();
                if (contacts != null) {
                        for (Map.Entry<String, User> entry : contacts.entrySet()
                                ) {
                                list.add(entry.getValue());
                        }
                }
                return list;
        }


        public static Map<String, GroupTableMessage> list_map(List<GroupTableMessage> list) {
                Map<String, GroupTableMessage> map = new HashMap<>();
                for (GroupTableMessage message :
                        list) {
                        map.put(message.getGroupId(), message);
                }
                return map;
        }


        public static List<GroupTableMessage> map_list(Map<String, GroupTableMessage> groupList) {
                List<GroupTableMessage> list = null;
                if (groupList != null) {
                        list = new ArrayList<>();
                        for (Map.Entry<String, GroupTableMessage> entry : groupList.entrySet()
                                ) {
                                list.add(entry.getValue());
                        }
                }
                return list;
        }
}
