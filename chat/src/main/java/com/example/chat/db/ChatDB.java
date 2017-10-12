package com.example.chat.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.chat.base.Constant;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.GroupChatMessage;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.bean.HappyBean;
import com.example.chat.bean.HappyContentBean;
import com.example.chat.bean.HappyContentResponse;
import com.example.chat.bean.HappyResponse;
import com.example.chat.bean.InvitationMsg;
import com.example.chat.bean.PictureBean;
import com.example.chat.bean.PictureResponse;
import com.example.chat.bean.RecentMsg;
import com.example.chat.bean.SharedMessage;
import com.example.chat.bean.TxResponse;
import com.example.chat.bean.User;
import com.example.chat.bean.WinXinBean;
import com.example.chat.manager.UserManager;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseApplication;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/9      20:08
 * QQ:             1981367757
 */
public class ChatDB {
        //      *************************    消息表的各项列名 //
        private static final String CHAT_TABLE_NAME = "chat_message";
        private static final String CHAT_ID = "_id";
        private static final String CHAT_CONVERSATION_ID = "conversationId";
        private static final String CHAT_CONVERSATION_TYPE = "conversationType";
        private static final String CHAT_NAME = "userName";
        private static final String CHAT_NICK = "nick";
        private static final String CHAT_AVATAR = "avatar";
        private static final String CHAT_CONTENT = "content";
        private static final String CHAT_TO_ID = "toId";
        private static final String CHAT_FROM_ID = "fromId";
        private static final String CHAT_READ_STATUS = "readStatus";
        private static final String CHAT_SEND_STATUS = "sendStatus";
        private static final String CHAT_MSG_TYPE = "type";
        private static final String CHAT_TIME = "time";

//        *******************************   消息表的各项列名      //


        /**
         * 创建聊天消息表的SQL语句
         */
        private static final String SQL_CREATE_CHAT_MESSAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + CHAT_TABLE_NAME + " ("
                + CHAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "// _id
                + CHAT_CONVERSATION_ID + " INTEGER, "// 会话id
                + CHAT_CONVERSATION_TYPE + " TEXT, "//会话类型
                + CHAT_NAME + " TEXT, "// 账号
                + CHAT_NICK + " TEXT, "// 昵称
                + CHAT_AVATAR + " TEXT, "// 头像
                + CHAT_CONTENT + " TEXT NOT NULL, "// 消息体
                + CHAT_TO_ID + " TEXT NOT NULL, "// 消息接受者体
                + CHAT_FROM_ID + " TEXT NOT NULL, "// 消息发送者id
                + CHAT_READ_STATUS + " INTEGER, "// 是否未读：0-未读/1-已读
                + CHAT_SEND_STATUS + " INTEGER, "// 状态
                + CHAT_MSG_TYPE + " INTEGER, "// 类型
                + CHAT_TIME + " TEXT); ";//消息创建时间

        //**********************              最近消息表的各项列名//
        private static final String RECENT_TABLE_NAME = "recent_message";
        private static final String RECENT_ID = "_id";
        private static final String RECENT_TO_ID = "uid";
        private static final String RECENT_TO_NAME = "name";
        private static final String RECENT_TO_NICK = "nick";
        private static final String RECENT_TO_AVATAR = "avatar";
        private static final String RECENT_TO_CONTENT = "content";
        private static final String RECENT_TO_TYPE = "type";
        private static final String RECENT_TO_TIME = "time";
        //**********************              最近消息表的各项列名//

        /**
         * 创建最近会话消息的消息表的SQL语句
         */
        private static final String SQL_CREATE_RECENT_MESSAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + RECENT_TABLE_NAME + " ("
                + RECENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "//
                + RECENT_TO_ID + " TEXT, "// 对方id
                + RECENT_TO_NAME + " TEXT, "// 对方的用户名
                + RECENT_TO_NICK + " TEXT, "// 对方的昵称
                + RECENT_TO_AVATAR + " TEXT, "// 对方的头像
                + RECENT_TO_CONTENT + " TEXT NOT NULL, "// 消息体
                + RECENT_TO_TYPE + " INTEGER, "// 类型--目前只有文本类型
                + RECENT_TO_TIME + " TEXT); ";


        //        ****************************  好友表的各项列名//
        private static final String CONTACTS_TABLE_NAME = "contacts";
        private static final String CONTACTS_NAME = "name";
        private static final String CONTACTS_NICK = "nick";
        private static final String CONTACTS_AVATAR = "avatar";
        private static final String CONTACTS_IS_BLACK = "isBlack";
        private static final String CONTACTS_UID = "uid";
        private static final String CONTACTS_SEX = "sex";
        private static final String CONTACTS_SORT_KEY = "sort_key";
        private static final String CONTACTS_INSTALLATION_ID = "installationId";
        private static final String CONTACTS_SIGNATURE = "signature";
        private static final String CONTACTS_PHONE = "phone";
        private static final String CONTACTS_EMAIL = "email";
        private static final String CONTACTS_BIRTHDAY = "birthday";
        private static final String CONTACTS_ADDRESS = "address";
        //        ****************************  好友表的各项列名//

        private static final String CONTACTS_LOCATION = "location";
        /**
         * 创建好友表的SQL语句
         */
        private static final String SQL_CREATE_CONTACTS_TABLE = "CREATE TABLE "
                + CONTACTS_TABLE_NAME + " ("
                + CONTACTS_NAME + " TEXT, "
                + CONTACTS_SIGNATURE + " TEXT, "
                + CONTACTS_NICK + " TEXT, "
                + CONTACTS_AVATAR + " TEXT, "
                + CONTACTS_IS_BLACK + " TEXT, "
                + CONTACTS_SEX + " TEXT, "
                + CONTACTS_INSTALLATION_ID + " TEXT, "
                + CONTACTS_SORT_KEY + " TEXT, "
                + CONTACTS_PHONE + " TEXT, "
                + CONTACTS_EMAIL + " TEXT, "
                + CONTACTS_ADDRESS + " TEXT, "
                + CONTACTS_BIRTHDAY + " TEXT, "
                + CONTACTS_LOCATION + " TEXT, "
                + CONTACTS_UID + " TEXT);";


        //        ************************好友请求表的各项列名//
        private static final String INVITATION_TABLE_NAME = "invitation";
        private static final String INVITATION_UID = "uid";
        private static final String INVITATION_NAME = "name";
        private static final String INVITATION_NICK = "nick";
        private static final String INVITATION_AVATAR = "avatar";
        private static final String INVITATION_TIME = "time";
        private static final String INVITATION_STATUS = "readStatus";
        //        ************************好友请求表的各项列名//


        /**
         * 创建好友请求表的SQL语句
         */
        private static final String SQL_CREATE_INVITATION_TABLE = "CREATE TABLE "
                + INVITATION_TABLE_NAME + " ("
                + INVITATION_UID + " TEXT, "
                + INVITATION_NAME + " TEXT, "
                + INVITATION_NICK + " TEXT, "
                + INVITATION_AVATAR + " TEXT, "
                + INVITATION_TIME + " TEXT, "
                + INVITATION_STATUS + " INTEGER); ";


        //        ************************群消息表的各项列名//
        private static final String _ID = "_id";
        private static final String GROUP_MESSAGE_TABLE = "group_message_table";
        private static final String GROUP_ID = "groupId";
        private static final String GROUP_FROM_ID = "uid";
        private static final String GROUP_CREATE_TIME = "time";
        private static final String GROUP_SEND_STATUS = "sendStatus";
        private static final String GROUP_READ_STATUS = "readStatus";
        private static final String GROUP_CONTENT = "content";
        private static final String GROUP_MSG_TYPE = "type";
        private static final String GROUP_FROM_AVATAR = "avatar";
        private static final String GROUP_FROM_NAME = "name";
        private static final String GROUP_FROM_NICK = "nick";
        //        ************************群消息表的各项列名//

        /**
         * 创建群消息表的SQL语句
         */
        private static final String SQL_CREATE_GROUP_MESSAGE_TABLE = "CREATE TABLE "
                + GROUP_MESSAGE_TABLE + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GROUP_ID + " TEXT, "
                + GROUP_FROM_ID + " TEXT, "
                + GROUP_FROM_NAME + " TEXT, "
                + GROUP_FROM_AVATAR + " TEXT, "
                + GROUP_CREATE_TIME + " TEXT, "
                + GROUP_SEND_STATUS + " INTEGER, "
                + GROUP_READ_STATUS + " INTEGER, "
                + GROUP_CONTENT + " TEXT, "
                + GROUP_FROM_NICK + " TEXT, "
                + GROUP_MSG_TYPE + " INTEGER);";

        //        ************************群结构表的各项列名//
        private static final String GROUP_TABLE = "group_table";
        private static final String GROUP_AVATAR = "group_avatar";
        private static final String GROUP_NAME = "group_name";
        private static final String GROUP_NICK = "group_nick";
        private static final String GROUP_DESCRIPTION = "group_description";
        private static final String GROUP_CREATOR_ID = "creatorId";
        private static final String GROUP_TO_ID = "group_toId";
        private static final String GROUP_NUMBER = "group_number";
        private static final String GROUP_OBJECT_ID = "objectId";
        private static final String GROUP_NOTIFICATION = "notification";
        //        ************************群结构表的各项列名//
        private static final String SQL_CREATE_GROUP_TABLE = "CREATE TABLE "
                + GROUP_TABLE + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GROUP_ID + " TEXT, "
                + GROUP_NAME + " TEXT, "
                + GROUP_AVATAR + " TEXT, "
                + GROUP_NICK + " TEXT, "
                + GROUP_OBJECT_ID + " TEXT, "
                + GROUP_CREATE_TIME + " TEXT, "
                + GROUP_CREATOR_ID + " TEXT, "
                + GROUP_NUMBER + " TEXT, "
                + GROUP_TO_ID + " TEXT, "
                + GROUP_NOTIFICATION + " TEXT, "
                + GROUP_SEND_STATUS + " INTEGER, "
                + GROUP_READ_STATUS + " INTEGER, "
                + GROUP_DESCRIPTION + " TEXT); ";


        private static final String SHARED_TABLE = "shared_table";
        private static final String SHARED_ID = "objectId";
        private static final String SHARED_CONTENT = "content";
        private static final String SHARED_BELONG_ID = "shared_belongId";
        private static final String SHARED_MSG_TYPE = "shared_msg_type";
        private static final String SHARED_CREATE_TIME = "shared_create_time";
        private static final String SHARED_COMMENT = "shared_comment";
        private static final String SHARED_LIKER = "shared_liker";
        private static final String SHARED_IMAGE_URL = "shared_image_url";
        private static final String SHARED_VISIBLE_TYPE = "shared_visible_type";
        private static final String SHARED_INVISIBLE_USER = "shared_invisible_user";
        private static final String SHARED_SEVER_CREATE_TIME = "shared_sever_create_time";
        private static final String SHARED_ADDRESS = "address";
        private static final String SHARED_URL_TITLE = "url_title";
        private static final String SHARED_URL = "shared_url_list";
        private static final String SQL_CREATE_SHARED_MESSAGE = "CREATE TABLE "
                + SHARED_TABLE + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SHARED_ID + " TEXT, "
                + SHARED_CONTENT + " TEXT, "
                + SHARED_BELONG_ID + " TEXT, "
                + SHARED_MSG_TYPE + " TEXT, "
                + SHARED_CREATE_TIME + " TEXT, "
                + SHARED_COMMENT + " TEXT, "
                + SHARED_LIKER + " TEXT, "
                + SHARED_SEVER_CREATE_TIME + " TEXT, "
                + SHARED_ADDRESS + " TEXT, "
                + SHARED_VISIBLE_TYPE + " INTEGER, "
                + SHARED_INVISIBLE_USER + " TEXT, "
                + SHARED_URL_TITLE + " TEXT, "
                + SHARED_URL + " TEXT, "
                + SHARED_IMAGE_URL + " TEXT); ";
        private static final String WEI_XIN_TABLE = "wein_xin_table";
        private static final String WEI_XIN_READ_STATUS = "read_status";
        private static final String WEI_XIN_INFO_KEY = "wei_xin_info";
        private static final String WEI_XIN_CACHE_INFO = "wei_xin_cache_info";
        private static final String SQL_CREATE_WEI_XIN_READ_STATUS = "CREATE TABLE "
                + WEI_XIN_TABLE + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WEI_XIN_INFO_KEY + " TEXT, "
                + WEI_XIN_CACHE_INFO + " TEXT, "
                + WEI_XIN_READ_STATUS + " INTEGER); ";
        private static final String HAPPY_TABLE = "happy_table";
        private static final String HAPPY_CACHE_DATA = "cache_data";
        private static final String HAPPY_READ_STATUS = "read_status";
        private static final String HAPPY_KEY = "happy_key";
        private static final String SQL_CREATE_HAPPY_MESSAGE = "CREATE TABLE "
                + HAPPY_TABLE + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HAPPY_KEY + " TEXT, "
                + HAPPY_CACHE_DATA + " TEXT, "
                + HAPPY_READ_STATUS + " INTEGER); ";


        private static final String HAPPY_CONTENT_TABLE = "happy_content_table";
        private static final String HAPPY_CONTENT_CACHE_DATA = "cache_data";
        private static final String HAPPY_CONTENT_READ_STATUS = "read_status";
        private static final String HAPPY_CONTENT_KEY = "happy_content_key";
        private static final String SQL_CREATE_HAPPY_CONTENT_MESSAGE = "CREATE TABLE "
                + HAPPY_CONTENT_TABLE + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HAPPY_CONTENT_KEY + " TEXT, "
                + HAPPY_CONTENT_CACHE_DATA + " TEXT, "
                + HAPPY_CONTENT_READ_STATUS + " INTEGER); ";


        private static final String PICTURE_TABLE = "picture_table";
        private static final String PICTURE_KEY = "picture_key";
        private static final String PICTURE_CACHE_DATA = "picture_cache_data";
        private static final String PICTURE_READ_STATUS = "read_status";
        private static final String SQL_CREATE_PICTURE_MESSAGE = "CREATE TABLE "
                + PICTURE_TABLE + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PICTURE_KEY + " TEXT, "
                + PICTURE_CACHE_DATA + " TEXT, "
                + PICTURE_READ_STATUS + " INTEGER); ";
        private static final String CRASH_TABLE = "crash_table";
        private static final String CRASH_MESSAGE = "crash_message";
        private static final String CRASH_FLAG = "crash_flag";
        private static final String SQL_CREATE_CRASH_MESSAGE_TABLE = "CREATE TABLE "
                + CRASH_TABLE + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CRASH_MESSAGE + " TEXT, "
                + CRASH_FLAG + " INTEGER); ";
        /**
         * 储存键值为UID,值为DB的map
         */
        private static Map<String, ChatDB> sMap = new HashMap<>();

        private SQLiteDatabase mDatabase;

        private ChatDB(String userId) {
                DBConfig config = new DBConfig();
                config.setName(userId);
                config.setContext(BaseApplication.getInstance());
                mDatabase = new ChatSQLiteDBHelper(config.getContext(), config.getName(), null, config.getDbVersion(), config.getListener()).getWritableDatabase();
        }

        public static ChatDB create(String userId) {
                return getInstance(userId);
        }

        public static ChatDB create() {
                return getInstance(UserManager.getInstance().getCurrentUserObjectId());
        }

        private synchronized static ChatDB getInstance(String userId) {
                if (userId == null) {
                        throw new RuntimeException("userId is NULL!!!");
                }
                ChatDB chatDB = sMap.get(userId);
                if (chatDB == null) {
                        chatDB = new ChatDB(userId);
                        sMap.put(userId, chatDB);
                }
                return chatDB;
        }

        /**
         * 通过用户ID判断是否是黑名单用户
         *
         * @param userId 用户ID
         * @return 是否是黑名单用户
         */
        public boolean isBlackUser(String userId) {
                boolean result = false;
                String isBlack = Constant.BLACK_NO;
                Cursor cursor = mDatabase.rawQuery("select * from " + CONTACTS_TABLE_NAME + " WHERE " + CONTACTS_UID + " = ?", new String[]{userId});
                if (cursor != null && cursor.moveToFirst()) {
                        isBlack = cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_IS_BLACK));
                        cursor.close();
                }
                if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                }
                if (isBlack.equals(Constant.BLACK_NO)) {
                        result = false;
                } else if (isBlack.equals(Constant.BLACK_YES)) {
                        result = true;
                }
                return result;
        }

        /**
         * 根据会话ID和创建时间来查询聊天消息是否存在
         *
         * @param conversationId 会话ID
         * @param createTime     创建时间
         * @return 返回结果
         */
        public boolean isExistChatMessage(String conversationId, String createTime) {
                boolean result = false;
                Cursor query = mDatabase.query(CHAT_TABLE_NAME, null, CHAT_CONVERSATION_ID + " =? AND " + CHAT_TIME + " =?", new String[]{conversationId, createTime}, null, null, null);
                if (query != null && query.moveToFirst()) {
                        result = true;
                }
                if (query != null && !query.isClosed()) {
                        query.close();
                }
                return result;
        }

        /**
         * 保存或更新一条聊天消息到数据库中
         *
         * @param message 聊天消息实体
         */
        public long saveChatMessage(ChatMessage message) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(CHAT_CONVERSATION_ID, message.getConversationId());
                        contentValues.put(CHAT_NAME, message.getBelongUserName());
                        contentValues.put(CHAT_AVATAR, message.getBelongAvatar());
                        contentValues.put(CHAT_NICK, message.getBelongNick());
                        contentValues.put(CHAT_CONTENT, message.getContent());
                        contentValues.put(CHAT_FROM_ID, message.getBelongId());
                        contentValues.put(CHAT_TO_ID, message.getToId());
                        contentValues.put(CHAT_MSG_TYPE, message.getMsgType());
                        contentValues.put(CHAT_TIME, message.getCreateTime());
                        contentValues.put(CHAT_READ_STATUS, message.getReadStatus());
                        contentValues.put(CHAT_SEND_STATUS, message.getSendStatus());
                        contentValues.put(CHAT_CONVERSATION_TYPE, message.getConversationType());
                        if (!isExistChatMessage(message.getConversationId(), message.getCreateTime())) {
                                result = mDatabase.insert(CHAT_TABLE_NAME, null, contentValues);
                        } else {
                                result = mDatabase.update(CHAT_TABLE_NAME, contentValues, CHAT_CONVERSATION_ID + " =? AND " + CHAT_TIME + " =?", new String[]{message.getConversationId(), message.getCreateTime()});
                        }
                }
                return result;
        }

        /**
         * 保存或更新用户的最新的一条会话消息
         *
         * @param recentMsg 最近消息实体
         * @return 结果
         */
        public long saveRecentMessage(RecentMsg recentMsg) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        ContentValues values = new ContentValues();
                        values.put(RECENT_TO_NAME, recentMsg.getName());
                        values.put(RECENT_TO_NICK, recentMsg.getNick());
                        values.put(RECENT_TO_AVATAR, recentMsg.getAvatar());
                        values.put(RECENT_TO_CONTENT, recentMsg.getLastMsgContent());
                        values.put(RECENT_TO_TIME, recentMsg.getTime());
                        values.put(RECENT_TO_TYPE, recentMsg.getMsgType());
                        if (!isRecentMsgExist(recentMsg.getBelongId())) {
                                values.put(RECENT_TO_ID, recentMsg.getBelongId());
                                result = mDatabase.insert(RECENT_TABLE_NAME, null, values);
                        } else if (!isRecentMsgExist(recentMsg.getBelongId(), recentMsg.getTime())) {
//                                如果该消息未存在的情况下
//                                只要更新最后一条的消息就行了
                                result = mDatabase.update(RECENT_TABLE_NAME, values, RECENT_TO_ID + " =?", new String[]{recentMsg.getBelongId()});
                        } else {
//                                否则是从定时服务那里拉数据过来的，这里只需要更新时间就行了
                                result = mDatabase.update(RECENT_TABLE_NAME, values, RECENT_TO_ID + " =? AND " + RECENT_TO_TIME + " =?", new String[]{recentMsg.getBelongId(), recentMsg.getTime()});
                        }

                }
                return result;
        }

        /**
         * 判断最近消息列表中是否有该用户的消息
         *
         * @param uid 用户ID
         * @return 结果
         */
        private boolean isRecentMsgExist(String uid) {
                boolean result = false;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(RECENT_TABLE_NAME, null, RECENT_TO_ID + " =?", new String[]{uid}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                                result = true;
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return result;
        }

        /**
         * 通过用户ID和消息的创建时间判断最近消息是否存在
         *
         * @param uid  用户ID
         * @param time 消息时间
         * @return 结果
         */
        private boolean isRecentMsgExist(String uid, String time) {
                boolean result = false;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.rawQuery("select * from " + RECENT_TABLE_NAME + " where " + RECENT_TO_ID + " =? and " + RECENT_TO_TIME + " =?", new String[]{uid, time});
                        if (cursor != null && cursor.moveToFirst()) {
                                result = true;
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return result;
        }

        /**
         * 保存邀请消息
         *
         * @param chatMessage 邀请消息
         */
        public long saveInvitationMsg(ChatMessage chatMessage) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        if (!isExistInvitationMsg(chatMessage.getBelongId(), chatMessage.getCreateTime())) {
                                ContentValues values = new ContentValues();
                                values.put(INVITATION_UID, chatMessage.getBelongId());
                                values.put(INVITATION_NAME, chatMessage.getBelongUserName());
                                values.put(INVITATION_NICK, chatMessage.getBelongNick());
                                values.put(INVITATION_AVATAR, chatMessage.getBelongAvatar());
                                values.put(INVITATION_STATUS, chatMessage.getReadStatus());
                                values.put(INVITATION_TIME, chatMessage.getCreateTime());
                                result = mDatabase.insert(INVITATION_TABLE_NAME, null, values);
                        } else {
                                result = 1;
                        }
                }
                return result;
        }

        /**
         * 通过用户ID和邀请消息的创建时间来判断消息是否存在
         *
         * @param uid  用户ID
         * @param time 创建时间
         * @return 结果
         */
        private boolean isExistInvitationMsg(String uid, String time) {
                boolean result = false;
                Cursor cursor = mDatabase.query(INVITATION_TABLE_NAME, null, INVITATION_UID + " =? AND " + INVITATION_TIME + " =? ", new String[]{uid, time}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                        result = true;
                }
                if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                }
                return result;
        }

        /**
         * 获取所有的邀请消息,按时间进行排序
         *
         * @return 消息列表
         */
        public List<InvitationMsg> getAllInvitationMsg() {
                List<InvitationMsg> invitationMsgs = new ArrayList<>();
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(INVITATION_TABLE_NAME, null, null, null, null, null, INVITATION_TIME);
                        InvitationMsg msg;
                        String currentId = UserManager.getInstance().getCurrentUserObjectId();
                        while (cursor != null && cursor.moveToNext()) {
                                msg = new InvitationMsg();
                                msg.setBelongId(cursor.getString(cursor.getColumnIndexOrThrow(INVITATION_UID)));
                                msg.setAvatar(cursor.getString(cursor.getColumnIndexOrThrow(INVITATION_AVATAR)));
                                msg.setName(cursor.getString(cursor.getColumnIndexOrThrow(INVITATION_NAME)));
                                msg.setNick(cursor.getString(cursor.getColumnIndexOrThrow(INVITATION_NICK)));
                                msg.setReadStatus(cursor.getInt(cursor.getColumnIndexOrThrow(INVITATION_STATUS)));
                                msg.setTime(cursor.getString(cursor.getColumnIndexOrThrow(INVITATION_TIME)));
                                msg.setToId(currentId);
                                invitationMsgs.add(msg);
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                } else {
                        LogUtil.e("消息数据库未打开");
                }
                return invitationMsgs;
        }

        /**
         * 添加用户到好友表中
         *
         * @param user 用户实体
         * @return 添加第几行
         */
        public long addOrUpdateContacts(User user) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        ContentValues values = new ContentValues();
                        values.put(CONTACTS_UID, user.getObjectId());
                        values.put(CONTACTS_NAME, user.getUsername());
                        values.put(CONTACTS_AVATAR, user.getAvatar());
                        values.put(CONTACTS_NICK, user.getNick());
                        values.put(CONTACTS_IS_BLACK, Constant.BLACK_NO);
                        values.put(CONTACTS_SEX, user.isSex() ? "男" : "女");
                        values.put(CONTACTS_SORT_KEY, user.getSortedKey());
                        values.put(CONTACTS_INSTALLATION_ID, user.getInstallId());
                        values.put(CONTACTS_SIGNATURE, user.getSignature());
                        values.put(CONTACTS_PHONE, user.getMobilePhoneNumber());
                        values.put(CONTACTS_EMAIL, user.getEmail());
                        values.put(CONTACTS_BIRTHDAY, user.getBirthDay());
                        values.put(CONTACTS_ADDRESS, user.getAddress());
                        if (user.getLocation() != null) {
                                values.put(CONTACTS_LOCATION, user.getLocation().getLongitude() + "&" + user.getLocation().getLatitude());
                        } else {
                                values.put(CONTACTS_LOCATION, (String) null);
                        }
                        if (!isExistFriend(user.getUsername())) {
                                result = mDatabase.insert(CONTACTS_TABLE_NAME, null, values);
                        } else {
                                result = mDatabase.update(CONTACTS_TABLE_NAME, values, CONTACTS_NAME + " =?", new String[]{user.getUsername()});
                        }
                        if (result > 0) {
                                LogUtil.e("插入或更新好友资料到数据库中成功");
                        } else {
                                LogUtil.e("插入或更新好友资料到数据库中失败");
                        }
                }
                return result;
        }

        /**
         * 查询数据库中是否有该好友
         *
         * @param name 用户实体
         * @return 成功与否
         */
        private boolean isExistFriend(String name) {
                boolean result = false;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.rawQuery("select * from " + CONTACTS_TABLE_NAME + " where " + CONTACTS_NAME + " =?", new String[]{name});
                        if (cursor != null && cursor.moveToFirst()) {
                                result = true;
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return result;
        }

        /**
         * 点击同意按钮后，更新邀请消息的状态
         *
         * @param msg        邀请消息
         * @param readStatus 读取状态
         * @return 结果
         */
        public long updateInvitationMsgStatus(InvitationMsg msg, Integer readStatus) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        if (isExistInvitationMsg(msg.getBelongId(), msg.getTime())) {
                                ContentValues values = new ContentValues();
                                values.put(INVITATION_STATUS, readStatus);
                                result = mDatabase.update(INVITATION_TABLE_NAME, values, INVITATION_UID + " =? and " + INVITATION_TIME + " =?", new String[]{msg.getBelongId(), msg.getTime()});
                        } else {
                                LogUtil.e("邀请消息不存在数据库中，所以更新失败");
                        }
                }
                return result;
        }

        /**
         * 获取所有的最近会话消息,并按时间进行排序
         *
         * @return 所有的最近消息列表
         */
        public List<RecentMsg> getAllRecentMsg() {
                List<RecentMsg> list = new ArrayList<>();
                if (mDatabase.isOpen()) {

                        Cursor cursor = mDatabase.query(RECENT_TABLE_NAME, null, null, null, null, null, RECENT_TO_TIME);
                        RecentMsg recentMsg;
                        while (cursor != null && cursor.moveToNext()) {
                                recentMsg = new RecentMsg();
                                recentMsg.setBelongId(cursor.getString(cursor.getColumnIndexOrThrow(RECENT_TO_ID)));
                                recentMsg.setLastMsgContent(cursor.getString(cursor.getColumnIndexOrThrow(RECENT_TO_CONTENT)));
                                recentMsg.setTime(cursor.getString(cursor.getColumnIndexOrThrow(RECENT_TO_TIME)));
                                recentMsg.setNick(cursor.getString(cursor.getColumnIndexOrThrow(RECENT_TO_NICK)));
                                recentMsg.setName(cursor.getString(cursor.getColumnIndexOrThrow(RECENT_TO_NAME)));
                                recentMsg.setMsgType(cursor.getInt(cursor.getColumnIndexOrThrow(RECENT_TO_TYPE)));
                                recentMsg.setAvatar(cursor.getString(cursor.getColumnIndexOrThrow(RECENT_TO_AVATAR)));
                                list.add(recentMsg);
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }

//                SB式的判断...........
                if (list.size() > 0) {
                        LogUtil.e("判断是否有黑名单");
                        List<Integer> position = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                                if (ChatDB.create().isBlackUser(list.get(i).getBelongId())) {
                                        LogUtil.e("执行到这里就一定有黑名单拉");
                                        position.add(i);
                                }
                        }
                        if (position.size() > 0) {
                                for (int i = 0; i < position.size(); i++) {
                                        list.remove(position.get(i).intValue());
                                }
                        }
                }
                return list;
        }


        /**
         * 根据双方的UID组成查询条件,查询指定最近会话未读取的消息数
         *
         * @param uid  uid
         * @param uid1 uid1
         * @return 查询结果数
         */
        public int queryRecentConversationUnreadCount(String uid, String uid1) {
                int result = 0;
                if (mDatabase.isOpen()) {
                        String fromToId = uid + "&" + uid1;
                        String toFromId = uid1 + "&" + uid;
                        Cursor cursor = mDatabase.rawQuery("select * from " + CHAT_TABLE_NAME + " where " + CHAT_CONVERSATION_ID + " IN ( '" + fromToId + "' , '" + toFromId
                                + "' ) " + "and " + CHAT_READ_STATUS + " =?", new String[]{Constant.RECEIVE_UNREAD + ""});
                        if (cursor != null && cursor.moveToFirst()) {
                                result = cursor.getCount();
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return result;
        }

        /**
         * 根据用户ID和消息创建时间来删除会话消息
         *
         * @param uid  用户ID
         * @param time 消息创建时间
         */
        public long deleteRecentMsg(String uid, String time) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        if (!isRecentMsgExist(uid, time)) {
                                LogUtil.e("数据库中没有最近消息可删");
                        } else {
                                result = mDatabase.delete(RECENT_TABLE_NAME, RECENT_TO_ID + " =? AND " + RECENT_TO_TIME + " =?", new String[]{uid, time});
                                LogUtil.e("deleteRecentMsg" + result);
                        }
                }
                if (result > 0) {
                        LogUtil.e("会话列表中删除成功");
                } else {
                        LogUtil.e("会话列表中删除失败");
                }
                return result;
        }

        /**
         * 根据用户ID查询最新的page页(每页10条消息)聊天消息(在创建时间createTime之前的聊天消息)
         *
         * @param uid        用户ID
         * @param page       页数
         * @param createTime 创建时间
         * @return 聊天消息列表
         */
        public List<ChatMessage> queryChatMessagesFromDB(String uid, int page, long createTime) {
                List<ChatMessage> list = new ArrayList<>();
                if (mDatabase.isOpen()) {
                        String fromToId = uid + "&" + UserManager.getInstance().getCurrentUserObjectId();
                        String toFromId = UserManager.getInstance().getCurrentUserObjectId() + "&" + uid;
                        String sql;
                        if (createTime > 0) {
                                sql = "SELECT * from " + CHAT_TABLE_NAME + " WHERE "
                                        + CHAT_CONVERSATION_ID + " IN ('" + fromToId + "','" + toFromId
                                        + "') " + "AND " + CHAT_TIME + " < " + createTime + " ORDER BY " + CHAT_ID + " DESC LIMIT "
                                        + page * 10;
                        } else {
                                sql = "SELECT * from " + CHAT_TABLE_NAME + " WHERE "
                                        + CHAT_CONVERSATION_ID + " IN ( '" + fromToId + "' , '" + toFromId
                                        + "' ) " + " ORDER BY " + CHAT_ID + " DESC LIMIT "
                                        + page * 10;
                        }
                        Cursor cursor = mDatabase.rawQuery(sql, null);
                        ChatMessage message;
                        while (cursor != null && cursor.moveToNext()) {
                                message = new ChatMessage();
                                message.setBelongId(cursor.getString(cursor.getColumnIndexOrThrow(CHAT_FROM_ID)));
                                message.setToId(cursor.getString(cursor.getColumnIndexOrThrow(CHAT_TO_ID)));
                                message.setConversationId(cursor.getString(cursor.getColumnIndexOrThrow(CHAT_CONVERSATION_ID)));
                                message.setBelongAvatar(cursor.getString(cursor.getColumnIndexOrThrow(CHAT_AVATAR)));
                                message.setBelongNick(cursor.getString(cursor.getColumnIndexOrThrow(CHAT_NICK)));
                                message.setBelongUserName(cursor.getString(cursor.getColumnIndexOrThrow(CHAT_NAME)));
                                message.setMsgType(cursor.getInt(cursor.getColumnIndexOrThrow(CHAT_MSG_TYPE)));
                                message.setCreateTime(cursor.getString(cursor.getColumnIndexOrThrow(CHAT_TIME)));
                                message.setSendStatus(cursor.getInt(cursor.getColumnIndexOrThrow(CHAT_SEND_STATUS)));
                                message.setContent(cursor.getString(cursor.getColumnIndexOrThrow(CHAT_CONTENT)));
                                message.setReadStatus(cursor.getInt(cursor.getColumnIndexOrThrow(CHAT_READ_STATUS)));
                                message.setConversationType(cursor.getString(cursor.getColumnIndexOrThrow(CHAT_CONVERSATION_TYPE)));
                                list.add(message);
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
//                        反转消息,使list底部为最新的消息

                        Collections.reverse(list);
                }
                return list;
        }

        public List<GroupChatMessage> queryGroupChatMessageFromDB(String groupId, int page, long createTime) {
                List<GroupChatMessage> list = new ArrayList<>();
                if (mDatabase.isOpen()) {
                        String sql;
                        if (createTime > 0) {
                                sql = "SELECT * from " + GROUP_MESSAGE_TABLE + " WHERE " + GROUP_ID + " = ?" + " AND " + GROUP_CREATE_TIME
                                        + " < "
                                        + createTime + " ORDER BY " + CHAT_ID + " DESC LIMIT "
                                        + page * 10;
                        } else {
                                sql = "SELECT * from " + GROUP_MESSAGE_TABLE + " WHERE "
                                        + GROUP_ID + " = ?" + " ORDER BY " + CHAT_ID + " DESC LIMIT "
                                        + page * 10;
                        }
                        Cursor cursor = mDatabase.rawQuery(sql, new String[]{groupId});
                        GroupChatMessage message;
                        while (cursor != null && cursor.moveToNext()) {
                                message = new GroupChatMessage();
                                message.setBelongId(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_FROM_ID)));
                                message.setBelongAvatar(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_FROM_AVATAR)));
                                message.setBelongNick(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_FROM_NICK)));
                                message.setBelongUserName(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_FROM_NAME)));
                                message.setMsgType(cursor.getInt(cursor.getColumnIndexOrThrow(GROUP_MSG_TYPE)));
                                message.setCreateTime(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_CREATE_TIME)));
                                message.setSendStatus(cursor.getInt(cursor.getColumnIndexOrThrow(GROUP_SEND_STATUS)));
                                message.setContent(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_CONTENT)));
                                message.setReadStatus(cursor.getInt(cursor.getColumnIndexOrThrow(GROUP_READ_STATUS)));
                                list.add(message);
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
//                        反转消息,使list底部为最新的消息
                        Collections.reverse(list);
                }
                return list;
        }


        /**
         * 查询聊天消息表是否有未读消息
         *
         * @return 所有的未读的聊天消息个数
         */
        public int hasUnReadChatMessage() {
                int size = 0;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(CHAT_TABLE_NAME, null, CHAT_READ_STATUS + " = ?", new String[]{Constant.RECEIVE_UNREAD + ""}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                                size = cursor.getCount();
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return size;
        }

        /**
         * 是否有未处理的邀请消息
         *
         * @return 有或无
         */
        public int hasUnReadInvitation() {
                int size = 0;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(INVITATION_TABLE_NAME, null, INVITATION_STATUS + " =?", new String[]{Constant.RECEIVE_UNREAD + ""}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                                size = cursor.getCount();
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return size;
        }

        /**
         * 删除指定用户的聊天记录
         *
         * @param uid 目标用户ID
         * @return 结果
         */
        public long deleteAllChatMessage(String uid) {
                long result = 0;
                String currentUid = UserManager.getInstance().getCurrentUserObjectId();
                String[] args = new String[]{currentUid + "&" + uid, uid + "&" + currentUid};

                if (mDatabase.isOpen()) {
                        result = mDatabase.delete(CHAT_TABLE_NAME, CHAT_CONVERSATION_ID + " in(?,?)", args);
                        LogUtil.e("deleteAllChatMessage" + result);
                }
                if (result > 0) {
                        LogUtil.e("删除聊天消息表成功");
                } else {
                        LogUtil.e("删除聊天消息表失败");
                }
                return result;
        }

        /**
         * 更新聊天消息的读取状态
         *
         * @param conversationId 会话ID
         * @param createTime     创建时间
         * @param isReaded       是否已读取
         * @return 结果
         */
        public long updateChatMessageReadStatus(String conversationId, String createTime, boolean isReaded) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        if (isExistChatMessage(conversationId, createTime)) {
                                LogUtil.e("这里执行了吗多吃点");
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(CHAT_READ_STATUS, isReaded ? Constant.READ_STATUS_READED : Constant.READ_STATUS_UNREAD);
                                result = mDatabase.update(CHAT_TABLE_NAME, contentValues, CHAT_CONVERSATION_ID + " =? AND " + CHAT_TIME + " =?", new String[]{conversationId, createTime});
                        }
                }
                if (result > 0) {
                        LogUtil.e("在数据库中更新聊天消息为已读状态成功");
                } else {
                        LogUtil.e("在数据库中更新聊天消息为已读状态失败");
                }
                LogUtil.e("result" + result);
                return result;
        }

        /**
         * 更新好友的黑名单状态
         *
         * @param username 用户名
         * @param isBlack  是否为黑名单状态
         * @return 结果
         */
        public long updateFriendBlackStatus(String username, boolean isBlack) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        if (isExistFriend(username)) {
                                ContentValues values = new ContentValues();
                                values.put(CONTACTS_IS_BLACK, isBlack ? Constant.BLACK_YES : Constant.BLACK_NO);
                                result = mDatabase.update(CONTACTS_TABLE_NAME, values, CONTACTS_NAME + " =?", new String[]{username});
                        } else {
                                result = -1;
                        }
                }
                if (result > 0) {
                        LogUtil.e("在数据库更新好友的黑名单状态成功");
                } else {
                        LogUtil.e("在数据库更新好友的黑名单状态失败");
                }
                return result;
        }

        /**
         * 根据用户ID删除最近会话
         *
         * @param objectId 用户ID
         * @return 结果
         */
        public long deleteRecentMsg(String objectId) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        if (isRecentMsgExist(objectId)) {
                                result = mDatabase.delete(RECENT_TABLE_NAME, RECENT_TO_ID + " =?", new String[]{objectId});
                        }
                }
                if (result > 0) {
                        LogUtil.e("删除指定额最近会话成功");
                } else {
                        LogUtil.e("删除指定额最近会话失败");
                }
                return result;
        }

        /**
         * 批量更新好友消息
         *
         * @param friends 好友
         */
        public boolean addOrUpdateContacts(List<User> friends) {
                boolean result = true;
                for (User user :
                        friends) {
                        if (addOrUpdateContacts(user) < 0)
                                result = false;
                }
                return result;
        }

        /**
         * 批量更新好友中黑名单
         *
         * @param list 黑名单
         */
        public boolean updateFriendsBlackStatus(List<User> list) {
                boolean result = true;
                for (User black
                        : list
                        ) {
                        if (updateFriendBlackStatus(black.getUsername(), true) < 0) {
                                result = false;
                        }
                }
                return result;
        }

        public List<User> getContactsWithoutBlack() {
                List<User> list = new ArrayList<>();
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(CONTACTS_TABLE_NAME, null, CONTACTS_IS_BLACK + " =?", new String[]{Constant.BLACK_NO}, null, null, null);
                        if (cursor != null) {
                                User user;
                                while (cursor.moveToNext()) {
                                        user = new User();
                                        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_NAME)));
                                        user.setObjectId(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_UID)));
                                        user.setAvatar(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_AVATAR)));
                                        user.setSex(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_SEX)).equals("男"));
                                        user.setNick(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_NICK)));
                                        user.setSortedKey(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_SORT_KEY)));
                                        user.setInstallId(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_INSTALLATION_ID)));
                                        user.setSignature(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_SIGNATURE)));
                                        user.setMobilePhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_PHONE)));
                                        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_EMAIL)));
                                        user.setBirthDay(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_BIRTHDAY)));
                                        user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_ADDRESS)));
                                        String location = cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_LOCATION));
                                        if (location != null && !location.equals("")) {
                                                String[] loglat = location.split("&");
                                                user.setLocation(new BmobGeoPoint(Double.parseDouble(loglat[0]), Double.parseDouble(loglat[1])));
                                        }
                                        list.add(user);
                                }
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return list;
        }

        /**
         * 获取查询首字母的数据库游标
         *
         * @return 游标
         */
        public Cursor getSortedKeyCursor() {
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(CONTACTS_TABLE_NAME, new String[]{CONTACTS_SORT_KEY}, null, null, null, null, CONTACTS_SORT_KEY);
                        if (cursor != null && cursor.moveToFirst()) {
                                LogUtil.e("sortedKey的大小:" + cursor.getCount());
                                return cursor;
                        }
                }
                return null;
        }

        /**
         * 根据用户ID查询本地数据库中是否有该好友
         *
         * @param fromId 用户ID
         * @return 结果
         */
        public boolean hasFriend(String fromId) {
                boolean result = false;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(CONTACTS_TABLE_NAME, null, CONTACTS_UID + " =?", new String[]{fromId}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                                result = true;
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return result;
        }


        /**
         * 点击最近会话进入聊天界面，要把所有消息表为接受已读
         *
         * @param user     用户实体
         * @param isReaded 是否已读
         */
        public int updateReceivedChatMessageReaded(User user, boolean isReaded) {
                int result = -1;
                String conversationId = user.getObjectId() + "&" + UserManager.getInstance().getCurrentUserObjectId();
//                String conversationId1 = UserManager.getInstance().getCurrentUserObjectId() + "&" + user.getObjectId();
                if (mDatabase.isOpen()) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(CHAT_READ_STATUS, isReaded ? Constant.READ_STATUS_READED : Constant.RECEIVE_UNREAD);
                        result = mDatabase.update(CHAT_TABLE_NAME, contentValues, CHAT_CONVERSATION_ID + " =?", new String[]{conversationId});
                }
                return result;
        }

        public List<User> getAllBlackUser() {
                List<User> list = null;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(CONTACTS_TABLE_NAME, null, CONTACTS_IS_BLACK + " =?", new String[]{Constant.BLACK_YES}, null, null, null);
                        User user;
                        if (cursor != null && cursor.moveToFirst()) {
                                list = new ArrayList<>();
                                user = new User();
                                user.setObjectId(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_UID)));
                                user.setNick(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_NICK)));
                                user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_NAME)));
                                user.setSortedKey(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_SORT_KEY)));
                                user.setAvatar(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_AVATAR)));
                                user.setSex(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_SEX)).equals("男"));
                                user.setInstallId(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_INSTALLATION_ID)));
                                user.setSignature(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_SIGNATURE)));
                                user.setBirthDay(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_BIRTHDAY)));
                                user.setMobilePhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_PHONE)));
                                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_EMAIL)));
                                user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_ADDRESS)));
                                String location = cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_LOCATION));
                                if (location != null && !location.equals("")) {
                                        String[] loglat = location.split("&");
                                        user.setLocation(new BmobGeoPoint(Double.parseDouble(loglat[0]), Double.parseDouble(loglat[1])));
                                }
                                list.add(user);
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return list;
        }


        /**
         * 保存群消息到数据库
         *
         * @param chatMessage 群消息实体
         * @return 结果
         */
        public long saveGroupChatMessage(GroupChatMessage chatMessage) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        ContentValues values = new ContentValues();
                        values.put(GROUP_FROM_AVATAR, chatMessage.getBelongAvatar());
                        values.put(GROUP_FROM_NICK, chatMessage.getBelongNick());
                        if (!isExistGroupChatMessage(chatMessage.getGroupId(), chatMessage.getCreateTime())) {
                                values.put(GROUP_ID, chatMessage.getGroupId());
                                values.put(GROUP_FROM_ID, chatMessage.getBelongId());
                                values.put(GROUP_FROM_NAME, chatMessage.getBelongUserName());
                                values.put(GROUP_CONTENT, chatMessage.getContent());
                                values.put(GROUP_MSG_TYPE, chatMessage.getMsgType());
                                values.put(GROUP_CREATE_TIME, chatMessage.getCreateTime());
                                values.put(GROUP_SEND_STATUS, chatMessage.getSendStatus());
                                values.put(GROUP_READ_STATUS, chatMessage.getReadStatus());
                                result = mDatabase.insert(GROUP_MESSAGE_TABLE, null, values);
                        } else {
//                               否则更新数据
                                LogUtil.e("更新群数据");
                                result = mDatabase.update(GROUP_MESSAGE_TABLE, values, GROUP_ID + " =? AND " + GROUP_CREATE_TIME + " =?", new String[]{chatMessage.getGroupId(), chatMessage.getCreateTime()});
                        }
                }
                if (result > 0) {
                        LogUtil.e("保存或更新群消息到数据库中成功");
                } else {
                        LogUtil.e("保存或更新群消息到数据库中失败");
                }
                return result;
        }

        /**
         * 判断是否存在群消息
         *
         * @param groupId     群ID
         * @param createdTime 消创建时间
         * @return 结果
         */
        public boolean isExistGroupChatMessage(String groupId, String createdTime) {
                boolean result = false;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(GROUP_MESSAGE_TABLE, null, GROUP_ID + " =? AND " + GROUP_CREATE_TIME + " =?", new String[]{groupId, createdTime}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                                result = true;
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return result;
        }

        /**
         * 获取所有的群结构表
         *
         * @return 结果集
         */
        public List<GroupTableMessage> getAllGroupMessage() {
                List<GroupTableMessage> list = null;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.rawQuery("select * from " + GROUP_TABLE, null);
                        if (cursor != null) {
                                list = new ArrayList<>();
                                GroupTableMessage groupTableMessage;
                                while (cursor.moveToNext()) {
                                        groupTableMessage = new GroupTableMessage();
                                        groupTableMessage.setGroupId(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_ID)));
                                        groupTableMessage.setObjectId(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_OBJECT_ID)));
                                        groupTableMessage.setCreatorId(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_CREATOR_ID)));
                                        groupTableMessage.setCreatedTime(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_CREATE_TIME)));
                                        groupTableMessage.setGroupDescription(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_DESCRIPTION)));
                                        groupTableMessage.setGroupAvatar(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_AVATAR)));
                                        groupTableMessage.setGroupName(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_NAME)));
                                        groupTableMessage.setGroupNick(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_NICK)));
                                        groupTableMessage.setToId(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_TO_ID)));
                                        groupTableMessage.setSendStatus(cursor.getInt(cursor.getColumnIndexOrThrow(GROUP_SEND_STATUS)));
                                        groupTableMessage.setReadStatus(cursor.getInt(cursor.getColumnIndexOrThrow(GROUP_READ_STATUS)));
                                        groupTableMessage.setGroupNumber(CommonUtils.string2list(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_NUMBER))));
                                        groupTableMessage.setNotification(cursor.getString(cursor.getColumnIndexOrThrow(GROUP_NOTIFICATION)));
                                        list.add(groupTableMessage);
                                }
                                if (!cursor.isClosed()) {
                                        cursor.close();
                                }
                        }
                }
                return list;
        }


        public boolean saveGroupTableMessage(List<GroupTableMessage> list) {
                boolean result = true;
                for (GroupTableMessage message :
                        list) {
                        if (saveGroupTableMessage(message) < 0) {
                                result = false;
                        }
                }
                return result;

        }

        public long saveGroupTableMessage(GroupTableMessage message) {
                long result = -1;
                if (mDatabase.isOpen()) {
//                        这里不用保存读取状态和toId
                        ContentValues values = new ContentValues();
                        values.put(GROUP_ID, message.getGroupId());
                        values.put(GROUP_TO_ID, message.getToId());
                        values.put(GROUP_AVATAR, message.getGroupAvatar());
                        values.put(GROUP_OBJECT_ID, message.getObjectId());
                        values.put(GROUP_NAME, message.getGroupName());
                        values.put(GROUP_NICK, message.getGroupNick());
                        values.put(GROUP_DESCRIPTION, message.getGroupDescription());
                        values.put(GROUP_CREATE_TIME, message.getCreatedTime());
                        values.put(GROUP_CREATOR_ID, message.getCreatorId());
                        values.put(GROUP_SEND_STATUS, message.getSendStatus());
                        values.put(GROUP_READ_STATUS, message.getReadStatus());
                        values.put(GROUP_NOTIFICATION, message.getNotification());
                        values.put(GROUP_NUMBER, CommonUtils.list2string(message.getGroupNumber()));
                        if (!hasGroupTableMessage(message.getGroupId())) {
                                result = mDatabase.insert(GROUP_TABLE, null, values);
                        } else {
                                LogUtil.e("在数据库中更新群结构消息");
                                result = mDatabase.update(GROUP_TABLE, values, GROUP_OBJECT_ID + " =?", new String[]{message.getObjectId()});
                        }
                }
                if (result > 0) {
                        LogUtil.e("插入或更新群结构消息成功");
                } else {
                        LogUtil.e("插入或更新群结构消息失败");
                }
                return result;
        }

        public int queryGroupChatMessageUnreadCount(String groupId) {
                int size = 0;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(GROUP_MESSAGE_TABLE, null, GROUP_READ_STATUS + " =? AND " + GROUP_ID + " =?", new String[]{Constant.RECEIVE_UNREAD + "", groupId}, null, null, null);
                        if (cursor != null) {
                                size = cursor.getCount();
                                cursor.close();
                        }
                }
                return size;
        }

        public long deleteAllGroupChatMessage(String groupId) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        if (isExistGroupChatMessage(groupId)) {
                                result = mDatabase.delete(GROUP_MESSAGE_TABLE, GROUP_ID + " =?", new String[]{groupId});
                        } else {
                                LogUtil.e("数据库中没有该群的消息，所以删除失败");
                                result = -1;
                        }
                }
                if (result > 0) {
                        LogUtil.e("在数据库中删除群消息成功");
                } else {
                        LogUtil.e("在数据库中删除群消息失败");
                }
                return result;
        }

        private boolean isExistGroupChatMessage(String groupId) {
                boolean result = false;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(GROUP_MESSAGE_TABLE, null, GROUP_ID + " =?", new String[]{groupId}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                                result = true;
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return result;
        }

        /**
         * 更新群消息为已读状态
         *
         * @param groupId  群ID
         * @param isReaded 是否已读取
         * @return 结果
         */
        public boolean updateReceivedGroupChatMessageReaded(String groupId, boolean isReaded) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        ContentValues values = new ContentValues();
                        values.put(GROUP_READ_STATUS, isReaded ? Constant.READ_STATUS_READED : Constant.RECEIVE_UNREAD);
                        result = mDatabase.update(GROUP_MESSAGE_TABLE, values, GROUP_ID + " =?", new String[]{groupId});
                }
                return result > 0;
        }


        private boolean hasGroupTableMessage(String id) {
                boolean result = false;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(GROUP_TABLE, null, GROUP_ID + " =?", new String[]{id}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                                result = true;
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return result;
        }

        public RecentMsg getRecentMsg(String id) {
                RecentMsg recentMsg = null;
                if (isRecentMsgExist(id)) {
                        if (mDatabase.isOpen()) {
                                Cursor cursor = mDatabase.query(RECENT_TABLE_NAME, null, RECENT_TO_ID + " =?", new String[]{id}, null, null, null);
                                if (cursor != null && cursor.moveToFirst()) {
                                        recentMsg = new RecentMsg();
                                        recentMsg.setBelongId(cursor.getString(cursor.getColumnIndexOrThrow(RECENT_TO_ID)));
                                        recentMsg.setLastMsgContent(cursor.getString(cursor.getColumnIndexOrThrow(RECENT_TO_CONTENT)));
                                        recentMsg.setTime(cursor.getString(cursor.getColumnIndexOrThrow(RECENT_TO_TIME)));
                                        recentMsg.setNick(cursor.getString(cursor.getColumnIndexOrThrow(RECENT_TO_NICK)));
                                        recentMsg.setName(cursor.getString(cursor.getColumnIndexOrThrow(RECENT_TO_NAME)));
                                        recentMsg.setMsgType(cursor.getInt(cursor.getColumnIndexOrThrow(RECENT_TO_TYPE)));
                                        recentMsg.setAvatar(cursor.getString(cursor.getColumnIndexOrThrow(RECENT_TO_AVATAR)));
                                }
                                if (cursor != null && !cursor.isClosed()) {
                                        cursor.close();
                                }
                        }
                }
                return recentMsg;
        }

        public boolean hasInvitation(String belongId, String createTime) {
                boolean result = false;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(INVITATION_TABLE_NAME, null, INVITATION_UID + " =? AND " + INVITATION_TIME + " =? ", new String[]{belongId, createTime}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                                result = true;
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return result;
        }

        public boolean saveAllSharedMessage(List<SharedMessage> data) {
                boolean result = true;
                if (data != null && data.size() > 0) {
                        if (mDatabase.isOpen()) {
                                for (SharedMessage sharedMessage : data) {
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put(SHARED_ID, sharedMessage.getObjectId());
                                        contentValues.put(SHARED_VISIBLE_TYPE, sharedMessage.getVisibleType());
                                        contentValues.put(SHARED_CONTENT, sharedMessage.getContent());
                                        contentValues.put(SHARED_IMAGE_URL, CommonUtils.list2string(sharedMessage.getImageList()));
                                        contentValues.put(SHARED_COMMENT, CommonUtils.list2Comment(sharedMessage.getCommentMsgList()));
                                        contentValues.put(SHARED_LIKER, CommonUtils.list2string(sharedMessage.getLikerList()));
                                        contentValues.put(SHARED_INVISIBLE_USER, CommonUtils.list2string(sharedMessage.getInVisibleUserList()));
                                        contentValues.put(SHARED_MSG_TYPE, sharedMessage.getMsgType());
                                        contentValues.put(SHARED_CREATE_TIME, sharedMessage.getCreateTime());
                                        contentValues.put(SHARED_SEVER_CREATE_TIME, sharedMessage.getCreatedAt());
                                        contentValues.put(SHARED_ADDRESS, sharedMessage.getAddress());
                                        contentValues.put(SHARED_URL_TITLE, sharedMessage.getUrlTitle());
                                        contentValues.put(SHARED_URL, CommonUtils.list2string(sharedMessage.getUrlList()));
                                        if (hasSharedMessage(sharedMessage.getObjectId())) {
                                                if (mDatabase.update(SHARED_TABLE, contentValues, SHARED_ID + "= ?", new String[]{sharedMessage.getObjectId()}) < 0) {
                                                        result = false;
                                                }
                                        } else {
                                                contentValues.put(SHARED_BELONG_ID, sharedMessage.getBelongId());
                                                if (mDatabase.insert(SHARED_TABLE, null, contentValues) < 0) {
                                                        result = false;
                                                }
                                        }
                                }
                        }
                }
                return result;
        }

        public List<SharedMessage> getAllSharedMessage(boolean isAll, boolean isPullRefresh, String time, int count) {
                List<SharedMessage> list = null;
                if (mDatabase.isOpen()) {
                        int id = getIDFromTime(time);
                        String sql;
                        String uid = UserManager.getInstance().getCurrentUserObjectId();
                        if (isPullRefresh) {
                                if (isAll) {
                                        sql = "SELECT * from " + SHARED_TABLE + " WHERE " + _ID + " >" + id + " ORDER BY " + _ID + " DESC LIMIT "
                                                + count;
                                } else {

                                        sql = "SELECT * from " + SHARED_TABLE + " WHERE " + _ID + " > " + id + " AND " + SHARED_BELONG_ID + " =?" + " ORDER BY " + _ID + " DESC LIMIT "
                                                + count;
                                }
                        } else {
                                if (isAll) {
                                        sql = "SELECT * from " + SHARED_TABLE + " WHERE " + _ID + " < " + id + " ORDER BY " + _ID + " DESC LIMIT "
                                                + count;
                                } else {


                                        sql = "SELECT * from " + SHARED_TABLE + " WHERE " + _ID + " > " + id + " AND " + SHARED_BELONG_ID + " =?" + " ORDER BY " + _ID + " DESC LIMIT "
                                                + count;
                                }
                        }
                        LogUtil.e("查询数据库中所有说说的sql语句" + sql);
                        Cursor cursor = null;
                        if (isAll) {
                                cursor = mDatabase.rawQuery(sql, null);
                        } else {
                                cursor = mDatabase.rawQuery(sql, new String[]{uid});
                        }
                        if (cursor != null) {
                                SharedMessage shareMessage;
                                list = new ArrayList<>();
                                while (cursor.moveToNext()) {
                                        shareMessage = new SharedMessage();
                                        shareMessage.setContent(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_CONTENT)));
                                        shareMessage.setVisibleType(cursor.getInt(cursor.getColumnIndexOrThrow(SHARED_VISIBLE_TYPE)));
                                        shareMessage.setBelongId(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_BELONG_ID)));
                                        shareMessage.setInVisibleUserList(CommonUtils.string2list(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_INVISIBLE_USER))));
                                        shareMessage.setLikerList(CommonUtils.string2list(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_LIKER))));
                                        shareMessage.setMsgType(cursor.getInt(cursor.getColumnIndexOrThrow(SHARED_MSG_TYPE)));
                                        shareMessage.setCreateTime(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_CREATE_TIME)));
                                        shareMessage.setObjectId(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_ID)));
                                        shareMessage.setImageList(CommonUtils.string2list(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_IMAGE_URL))));
                                        shareMessage.setCreateTime(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_CREATE_TIME)));
                                        shareMessage.setSeverCreateTime(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_SEVER_CREATE_TIME)));
                                        shareMessage.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_ADDRESS)));
                                        shareMessage.setUrlTitle(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_URL_TITLE)));
                                        shareMessage.setUrlList(CommonUtils.string2list(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_URL))));
                                        list.add(shareMessage);
                                }
                                if (!cursor.isClosed()) {
                                        cursor.close();
                                }
                                LogUtil.e("查询得到的说说大小为" + list.size());
                                LogUtil.e("具体的说说消息如下");
                                for (SharedMessage message :
                                        list) {
                                        LogUtil.e(message);
                                }
                        }
                }
                return list;
        }

        private int getIDFromTime(String time) {
                LogUtil.e("123获取_id之前的时间" + time);
                int result = -1;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(SHARED_TABLE, null, SHARED_SEVER_CREATE_TIME + " =?", new String[]{time}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                                LogUtil.e("查询对应时间的ID个数为：" + cursor.getCount());
                                result = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                LogUtil.e("ID值为：" + result);
                return result;
        }

        public SharedMessage getSharedMessage(String id) {
                SharedMessage sharedMessage = null;
                if (mDatabase.isOpen()) {
                        LogUtil.e("这里，数据库打开");
                        Cursor cursor = mDatabase.query(SHARED_TABLE, null, SHARED_ID + " =?", new String[]{id}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                                sharedMessage = new SharedMessage();
                                sharedMessage.setContent(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_CONTENT)));
                                sharedMessage.setVisibleType(cursor.getInt(cursor.getColumnIndexOrThrow(SHARED_VISIBLE_TYPE)));
                                sharedMessage.setInVisibleUserList(CommonUtils.string2list(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_INVISIBLE_USER))));
                                sharedMessage.setCreateTime(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_CREATE_TIME)));
                                sharedMessage.setImageList(CommonUtils.string2list(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_IMAGE_URL))));
                                sharedMessage.setBelongId(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_BELONG_ID)));
                                sharedMessage.setCommentMsgList(CommonUtils.commentMsg2List(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_COMMENT))));
                                sharedMessage.setLikerList(CommonUtils.string2list(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_LIKER))));
                                sharedMessage.setMsgType(cursor.getInt(cursor.getColumnIndexOrThrow(SHARED_MSG_TYPE)));
                                sharedMessage.setObjectId(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_ID)));
                                sharedMessage.setSeverCreateTime(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_SEVER_CREATE_TIME)));
                                sharedMessage.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_ADDRESS)));
                                sharedMessage.setUrlList(CommonUtils.string2list(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_URL))));
                                sharedMessage.setUrlTitle(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_URL_TITLE)));
                                if (!cursor.isClosed()) {
                                        cursor.close();
                                }
                        }
                } else {
                        LogUtil.e("这里，数据库还没打开");
                }
                return sharedMessage;
        }

        public boolean hasSharedMessage(String id) {
                if (id == null) {

                        LogUtil.e("说说消息ID为空");
                        return false;
                }
                boolean result = false;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(SHARED_TABLE, null, SHARED_ID + " =?", new String[]{id}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                                result = true;
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return result;
        }

        public long saveSharedMessage(SharedMessage sharedMessage) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(SHARED_COMMENT, CommonUtils.list2Comment(sharedMessage.getCommentMsgList()));
                        contentValues.put(SHARED_LIKER, CommonUtils.list2string(sharedMessage.getLikerList()));
                        if (hasSharedMessage(sharedMessage.getObjectId())) {
                                result = mDatabase.update(SHARED_TABLE, contentValues, SHARED_ID + " =?", new String[]{sharedMessage.getObjectId()});
                        } else {
                                contentValues.put(SHARED_BELONG_ID, sharedMessage.getBelongId());
                                contentValues.put(SHARED_VISIBLE_TYPE, sharedMessage.getVisibleType());
                                contentValues.put(SHARED_CONTENT, sharedMessage.getContent());
                                contentValues.put(SHARED_IMAGE_URL, CommonUtils.list2string(sharedMessage.getImageList()));
                                contentValues.put(SHARED_INVISIBLE_USER, CommonUtils.list2string(sharedMessage.getInVisibleUserList()));
                                contentValues.put(SHARED_MSG_TYPE, sharedMessage.getMsgType());
                                contentValues.put(SHARED_CREATE_TIME, sharedMessage.getCreateTime());
                                contentValues.put(SHARED_SEVER_CREATE_TIME, sharedMessage.getCreatedAt());
                                contentValues.put(SHARED_ID, sharedMessage.getObjectId());
                                contentValues.put(SHARED_ADDRESS, sharedMessage.getAddress());
                                contentValues.put(SHARED_URL_TITLE, sharedMessage.getUrlTitle());
                                contentValues.put(SHARED_URL, CommonUtils.list2string(sharedMessage.getUrlList()));
                                result = mDatabase.insert(SHARED_TABLE, null, contentValues);
                        }
                }
                if (result > 0) {
                        LogUtil.e("保存说说消息到数据库中成功");
                } else {
                        LogUtil.e("保存说说消息到数据库中失败");
                }
                return result;
        }


        public long deleteSharedMessage(String id) {
                long result = -1;
                if (hasSharedMessage(id)) {
                        if (mDatabase.isOpen()) {
                                result = mDatabase.delete(SHARED_TABLE, SHARED_ID + " =?", new String[]{id});
                        }
                }
                if (result > 0) {
                        LogUtil.e("在数据库中删除说说消息成功");
                } else {
                        LogUtil.e("在数据库中删除说说消息失败");
                }
                return result;
        }

        public long updateUserAvatar(String id, String avatar) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        ContentValues values = new ContentValues();
                        values.put(CONTACTS_AVATAR, avatar);
                        result = mDatabase.update(CONTACTS_TABLE_NAME, values, CONTACTS_UID + " =?", new String[]{id});
                }
                if (result > 0) {
                        LogUtil.e("更新用户的头像成功");
                } else {
                        LogUtil.e("更新用户的头像失败");
                }
                return result;
        }

        public long updateUserSignature(String id, String signature) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        ContentValues values = new ContentValues();
                        values.put(CONTACTS_SIGNATURE, signature);
                        result = mDatabase.update(CONTACTS_TABLE_NAME, values, CONTACTS_UID + " =?", new String[]{id});
                }
                if (result > 0) {
                        LogUtil.e("更新用户的签名成功");
                } else {
                        LogUtil.e("更新用户的签名失败");
                }
                return result;
        }

        public long updateUserNick(String id, String nick) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        ContentValues values = new ContentValues();
                        values.put(CONTACTS_NICK, nick);
                        result = mDatabase.update(CONTACTS_TABLE_NAME, values, CONTACTS_UID + " =?", new String[]{id});
                }
                if (result > 0) {
                        LogUtil.e("更新用户的昵称成功");
                } else {
                        LogUtil.e("更新用户的昵称失败");
                }
                return result;
        }

        public User getContact(String uid) {
                User user = null;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(CONTACTS_TABLE_NAME, null, CONTACTS_UID + " =?", new String[]{uid}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                                user = new User();
                                user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_NAME)));
                                user.setObjectId(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_UID)));
                                user.setAvatar(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_AVATAR)));
                                user.setSex(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_SEX)).equals("男"));
                                user.setNick(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_NICK)));
                                user.setSortedKey(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_SORT_KEY)));
                                user.setInstallId(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_INSTALLATION_ID)));
                                user.setSignature(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_SIGNATURE)));
                                user.setMobilePhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_PHONE)));
                                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_EMAIL)));
                                user.setBirthDay(cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_BIRTHDAY)));
                                String location = cursor.getString(cursor.getColumnIndexOrThrow(CONTACTS_LOCATION));
                                if (location != null && !location.equals("")) {
                                        String[] loglat = location.split("&");
                                        user.setLocation(new BmobGeoPoint(Double.parseDouble(loglat[0]), Double.parseDouble(loglat[1])));
                                }
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return user;
        }

        public boolean updateGroupChatMessageNick(String groupId, String nick) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(GROUP_FROM_NICK, nick);
                        result = mDatabase.update(GROUP_MESSAGE_TABLE, contentValues, GROUP_ID + " =? AND " + GROUP_FROM_ID + " =?", new String[]{groupId, UserManager.getInstance().getCurrentUserObjectId()});
                }
                if (result > 0) {
                        LogUtil.e("在数据库中更新群消息上的昵称成功");
                } else {
                        LogUtil.e("在数据库中更新群消息上的昵称失败");
                }
                return result > 0;
        }


        public void clearAllMessage() {
                clearAllRecentMessages();
                clearAllChatMessages();
                clearAllGroupChatMessages();
                clearAllCacheHappyData();
                clearAllCacheHappyContentData();
                clearAllPictureData();
                clearAllWeiXinData();
        }


        public boolean clearAllRecentMessages() {
                long result = -1;
                if (mDatabase.isOpen()) {
                        result = mDatabase.delete(RECENT_TABLE_NAME, null, null);
                }
                if (result > 0) {
                        LogUtil.e("删除所有的最近聊天记录成功");
                } else {
                        LogUtil.e("删除所有的最近聊天记录失败");
                }
                return result > 0;
        }


        public boolean clearAllChatMessages() {
                long result = -1;
                if (mDatabase.isOpen()) {
                        result = mDatabase.delete(CHAT_TABLE_NAME, null, null);
                }
                if (result > 0) {
                        LogUtil.e("删除所有的聊天记录成功");
                } else {
                        LogUtil.e("删除所以的聊天记录失败");
                }
                return result > 0;
        }

        public boolean clearAllGroupChatMessages() {
                long result = -1;
                if (mDatabase.isOpen()) {
                        result = mDatabase.delete(GROUP_MESSAGE_TABLE, null, null);
                }
                if (result > 0) {
                        LogUtil.e("删除所有的群消息成功");
                } else {
                        LogUtil.e("删除所有的群消息失败");
                }
                return result > 0;
        }

        public boolean saveWeiXinInfoReadStatus(String key, int readFlag) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(WEI_XIN_READ_STATUS, readFlag);
                        result = mDatabase.update(WEI_XIN_TABLE, contentValues, WEI_XIN_INFO_KEY + " =?", new String[]{key});
                }
                if (result > 0) {
                        LogUtil.e("更新或插入读取状态成功");
                } else {
                        LogUtil.e("更新或插入读取状态失败");
                }
                return result > 0;
        }


        public int getWeixinInfoReadStatus(String key) {
                int result = 0;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(WEI_XIN_TABLE, null, WEI_XIN_INFO_KEY + "= ?", new String[]{key}, null, null, null);
                        if (cursor.moveToFirst()) {
                                result = cursor.getInt(cursor.getColumnIndexOrThrow(WEI_XIN_READ_STATUS));
                        }
                        if (!cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return result;
        }

        public boolean saveHappyInfo(String key, String json) {
                if (hasHappyInfo(key)) {
                        return true;
                }
                long result = -1;
                if (mDatabase.isOpen()) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(HAPPY_KEY, key);
                        contentValues.put(HAPPY_CACHE_DATA, json);
                        contentValues.put(HAPPY_READ_STATUS, 0);
                        result = mDatabase.insert(HAPPY_TABLE, null, contentValues);
                }
                if (result > 0) {
                        LogUtil.e("保存缓存笑话成功");
                } else {
                        LogUtil.e("保存缓存笑话失败");
                }
                return result > 0;
        }

        private boolean hasHappyInfo(String key) {
                boolean result = false;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(HAPPY_TABLE, null, HAPPY_KEY + " =?", new String[]{key}, null, null, null);
                        if (cursor.moveToFirst()) {
                                result = true;
                        }
                        if (!cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return result;
        }

        private Gson mGson = new Gson();

        public List<HappyBean> getHappyInfo(String key) {
                String json = null;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(HAPPY_TABLE, null, HAPPY_KEY + " =?", new String[]{key}, null, null, null);
                        if (cursor.moveToFirst()) {
                                json = cursor.getString(cursor.getColumnIndexOrThrow(HAPPY_CACHE_DATA));
                        }
                        if (!cursor.isClosed()) {
                                cursor.close();
                        }
                }
                if (json != null) {
                        return mGson.fromJson(json, HappyResponse.class).getResult().getData();
                } else {
                        return null;
                }
        }

        public boolean saveWeiXinInfo(String key, String json) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(WEI_XIN_READ_STATUS, 0);
                        contentValues.put(WEI_XIN_CACHE_INFO, json);
                        contentValues.put(WEI_XIN_INFO_KEY, key);
                        result = mDatabase.insert(WEI_XIN_TABLE, null, contentValues);
                }
                if (result > 0) {
                        LogUtil.e("插入微信精选缓存到数据库中成功");
                } else {
                        LogUtil.e("插入微信精选到数据库中失败");

                }
                return result > 0;
        }

        public List<WinXinBean> getWeiXinInfo(String key) {
                String json = null;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(WEI_XIN_TABLE, null, WEI_XIN_INFO_KEY + " =?", new String[]{key}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                                json = cursor.getString(cursor.getColumnIndexOrThrow(WEI_XIN_INFO_KEY));
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                if (json != null) {
                        return mGson.fromJson(json, TxResponse.class).getNewslist();
                }
                return null;
        }

        public boolean savePictureInfo(String key, String json) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(PICTURE_READ_STATUS, 0);
                        contentValues.put(PICTURE_CACHE_DATA, json);
                        contentValues.put(PICTURE_KEY, key);
                        result = mDatabase.insert(PICTURE_TABLE, null, contentValues);
                }
                if (result > 0) {
                        LogUtil.e("插入图片缓存到数据库中成功");
                } else {
                        LogUtil.e("插入图片缓存到数据库中失败");

                }
                return result > 0;
        }

        public List<PictureBean> getPictureInfo(String key) {
                String json = null;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(PICTURE_TABLE, null, PICTURE_KEY + " =?", new String[]{key}, null, null, null);
                        if (cursor.moveToFirst()) {
                                json = cursor.getString(cursor.getColumnIndexOrThrow(PICTURE_CACHE_DATA));
                        }
                        if (!cursor.isClosed()) {
                                cursor.close();
                        }
                }
                if (json != null) {
                        return mGson.fromJson(json, PictureResponse.class).getResults();
                }
                return null;
        }

        public boolean clearAllWeiXinData() {
                int result = -1;
                if (mDatabase.isOpen()) {
                        result = mDatabase.delete(WEI_XIN_TABLE, null, null);
                }
                if (result > 0) {
                        LogUtil.e("清除微信精选数据成功");
                } else {
                        LogUtil.e("清除微信精选数据失败");
                }
                return result > 0;
        }

        public boolean updateHappyInfoReaded(String key, int readFlag) {
                int result = -1;
                if (mDatabase.isOpen()) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(HAPPY_READ_STATUS, readFlag);
                        result = mDatabase.update(HAPPY_TABLE, contentValues, HAPPY_KEY + " =?", new String[]{key});
                }
                if (result > 0) {
                        LogUtil.e("更新趣图读取状态成功");
                } else {
                        LogUtil.e("更新趣图读取状态失败");
                }
                return result > 0;
        }

        public boolean updatePictureReaded(String key, int readFlag) {
                int result = -1;
                if (mDatabase.isOpen()) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(PICTURE_READ_STATUS, readFlag);
                        result = mDatabase.update(PICTURE_TABLE, contentValues, PICTURE_KEY + " =?", new String[]{key});
                }
                if (result > 0) {
                        LogUtil.e("更新美女图片读取状态成功");
                } else {
                        LogUtil.e("更新美女图片读取状态失败");
                }
                return result > 0;
        }

        public List<SharedMessage> getMyAllSharedMessage(boolean isPullRefresh, String time, int count) {
                List<SharedMessage> list = null;
                if (mDatabase.isOpen()) {
                        int id = getIDFromTime(time);
                        String sql;
                        String uid = UserManager.getInstance().getCurrentUserObjectId();
                        if (isPullRefresh) {
                                sql = "SELECT * from " + SHARED_TABLE + " WHERE " + _ID + " < " + id + " AND " + SHARED_BELONG_ID + " =?" + " ORDER BY " + _ID + " DESC LIMIT "
                                        + count;
                                LogUtil.e("下拉加载的ID" + id);
                        } else {
                                LogUtil.e("加载更多的ID" + id);
                                sql = "SELECT * from " + SHARED_TABLE + " WHERE " + _ID + " > " + id + " AND " + SHARED_BELONG_ID + " =?" + " ORDER BY " + _ID + " DESC LIMIT "
                                        + count;
                        }
                        Cursor cursor = mDatabase.rawQuery(sql, new String[]{uid});
                        if (cursor != null) {
                                SharedMessage shareMessage;
                                list = new ArrayList<>();
                                while (cursor.moveToNext()) {
                                        shareMessage = new SharedMessage();
                                        shareMessage.setContent(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_CONTENT)));
                                        shareMessage.setVisibleType(cursor.getInt(cursor.getColumnIndexOrThrow(SHARED_VISIBLE_TYPE)));
                                        shareMessage.setBelongId(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_BELONG_ID)));
                                        shareMessage.setInVisibleUserList(CommonUtils.string2list(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_INVISIBLE_USER))));
                                        shareMessage.setLikerList(CommonUtils.string2list(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_LIKER))));
                                        shareMessage.setMsgType(cursor.getInt(cursor.getColumnIndexOrThrow(SHARED_MSG_TYPE)));
                                        shareMessage.setCreateTime(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_CREATE_TIME)));
                                        shareMessage.setObjectId(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_ID)));
                                        shareMessage.setImageList(CommonUtils.string2list(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_IMAGE_URL))));
                                        shareMessage.setCreateTime(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_CREATE_TIME)));
                                        shareMessage.setSeverCreateTime(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_SEVER_CREATE_TIME)));
                                        shareMessage.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_ADDRESS)));
                                        shareMessage.setUrlTitle(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_URL_TITLE)));
                                        shareMessage.setUrlList(CommonUtils.string2list(cursor.getString(cursor.getColumnIndexOrThrow(SHARED_URL))));
                                        list.add(shareMessage);
                                }
                                if (!cursor.isClosed()) {
                                        cursor.close();
                                }
                        }
                }
                return list;
        }

        public boolean clearAllPictureData() {
                int result = -1;
                if (mDatabase.isOpen()) {
                        result = mDatabase.delete(PICTURE_TABLE, null, null);
                }
                if (result > 0) {
                        LogUtil.e("清除图片数据成功");
                } else {
                        LogUtil.e("清除图片数据失败");
                }
                return result > 0;
        }

        public boolean clearAllCacheHappyData() {
                int result = -1;
                if (mDatabase.isOpen()) {
                        result = mDatabase.delete(HAPPY_TABLE, null, null);
                }
                if (result > 0) {
                        LogUtil.e("清除图片数据成功");
                } else {
                        LogUtil.e("清除图片数据失败");
                }
                return result > 0;

        }

        public boolean clearAllCacheHappyContentData() {
                int result = -1;
                if (mDatabase.isOpen()) {
                        result = mDatabase.delete(HAPPY_CONTENT_TABLE, null, null);
                }
                if (result > 0) {
                        LogUtil.e("清除数据成功");
                } else {
                        LogUtil.e("清除数据失败");
                }
                return result > 0;
        }

        public boolean saveHappyContentInfo(String key, String json) {
                if (hasHappyContentInfo(key)) {
                        return true;
                }
                long result = -1;
                if (mDatabase.isOpen()) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(HAPPY_CONTENT_KEY, key);
                        contentValues.put(HAPPY_CONTENT_CACHE_DATA, json);
                        contentValues.put(HAPPY_CONTENT_READ_STATUS, 0);
                        result = mDatabase.insert(HAPPY_CONTENT_TABLE, null, contentValues);
                }
                if (result > 0) {
                        LogUtil.e("保存缓存笑话成功");
                } else {
                        LogUtil.e("保存缓存笑话失败");
                }
                return result > 0;
        }

        private boolean hasHappyContentInfo(String key) {
                boolean result = false;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(HAPPY_CONTENT_TABLE, null, HAPPY_CONTENT_KEY + " =?", new String[]{key}, null, null, null);
                        if (cursor.moveToFirst()) {
                                result = true;
                        }
                        if (!cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return result;
        }

        public List<HappyContentBean> getHappyContentInfo(String key) {
                String json = null;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(HAPPY_CONTENT_TABLE, null, HAPPY_CONTENT_KEY + " =?", new String[]{key}, null, null, null);
                        if (cursor.moveToFirst()) {
                                json = cursor.getString(cursor.getColumnIndexOrThrow(HAPPY_CONTENT_CACHE_DATA));
                        }
                        if (!cursor.isClosed()) {
                                cursor.close();
                        }
                }
                if (json != null) {
                        return mGson.fromJson(json, HappyContentResponse.class).getResult().getData();
                } else {
                        return null;
                }
        }

        public long deleteInvitationMsg(String belongId, String time) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        if (!hasInvitation(belongId, time)) {
                                LogUtil.e("数据库中没有邀请消息可删");
                        } else {
                                result = mDatabase.delete(INVITATION_TABLE_NAME, INVITATION_UID + " =? AND " + INVITATION_TIME + " =?", new String[]{belongId, time});
                                LogUtil.e("deleteInvitation" + result);
                        }
                }
                if (result > 0) {
                        LogUtil.e("邀请列表中删除成功");
                } else {
                        LogUtil.e("邀请列表中删除失败");
                }
                return result;
        }

        public long deleteGroupTableMessage(String groupId) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        if (!hasGroupTableMessage(groupId)) {
                                LogUtil.e("数据库中没有群结构消息可删");
                        } else {
                                result = mDatabase.delete(GROUP_TABLE, GROUP_ID + " =? ", new String[]{groupId});
                                LogUtil.e("删除的群结构消息行号" + result);
                        }
                }
                if (result > 0) {
                        LogUtil.e("删除群结构消息成功");
                } else {
                        LogUtil.e("删除群结构消息失败");
                }
                return result;
        }

        public long saveOrUpdateCrashMessage(String absolutePath, boolean isUploadServer) {
                long result = -1;
                if (mDatabase.isOpen()) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(CRASH_MESSAGE, absolutePath);
                        contentValues.put(CRASH_FLAG, isUploadServer ? 1 : 0);
                        if (isExitCrashMessage(absolutePath)) {
                                result = mDatabase.insert(CRASH_TABLE, null, contentValues);
                        } else {
                                result = mDatabase.update(CRASH_TABLE, contentValues, CRASH_MESSAGE + " =?", new String[]{absolutePath});
                        }
                }
                return result;
        }

        private boolean isExitCrashMessage(String absolutePath) {
                boolean result = false;
                if (mDatabase.isOpen()) {
                        Cursor cursor = mDatabase.query(CRASH_TABLE, null, CRASH_MESSAGE + " =?", new String[]{absolutePath}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                                result = true;
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return result;
        }

        public List<String> getAllErrorMessage(boolean isUploadServerSuccess) {
                List<String> result=null;
                if (mDatabase.isOpen()) {
                        String flag=isUploadServerSuccess?"1":"0";
                        Cursor cursor=mDatabase.query(CRASH_TABLE,null,CRASH_FLAG+" =?",new String[]{flag},null,null,null);
                        if (cursor != null) {
                                result=new ArrayList<>();
                                while (cursor.moveToNext()) {
                                        result.add(cursor.getString(cursor.getColumnIndexOrThrow(CRASH_FLAG)));
                                }
                                if (!cursor.isClosed()) {
                                        cursor.close();
                                }
                        }
                }
                return result;
        }

        public boolean updateMessageAvatar(String uid, String avatar) {
                long chatResult=-1;
                long groupResult=-1;
                if (mDatabase.isOpen()) {
                        if (isExistUserChatMessage(uid)) {
                                ContentValues contentValues=new ContentValues();
                                contentValues.put(CHAT_AVATAR,avatar);
                                chatResult=mDatabase.update(CHAT_TABLE_NAME,contentValues,CHAT_FROM_ID+" =?",new String[]{uid});
                        }
                        if (chatResult > 0) {
                                LogUtil.e("在数据库中更新单聊用户的头像成功");
                        }else {
                                LogUtil.e("在数据库中更新单聊用户的头像失败");
                        }
                        if (isExistGroupTableMessage(uid)) {
                                ContentValues values=new ContentValues();
                                values.put(GROUP_FROM_AVATAR,avatar);
                                groupResult=mDatabase.update(GROUP_MESSAGE_TABLE,values,GROUP_FROM_ID+" =?",new String[]{uid});
                        }
                        if (groupResult > 0) {
                                LogUtil.e("在数据库中更新群聊用户的头像成功");
                        }else {
                                LogUtil.e("在数据库中更新群聊用户的头像失败");
                        }
                }
                return groupResult>0&&chatResult>0;
        }

        private boolean isExistGroupTableMessage(String uid) {
                boolean result=false;
                if (mDatabase.isOpen()) {
                        Cursor cursor=mDatabase.query(GROUP_MESSAGE_TABLE,null,GROUP_FROM_ID+" =?",new String[]{uid},null,null,null);
                        if (cursor != null && cursor.moveToFirst()) {
                                result=true;
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return result;
        }

        private boolean isExistUserChatMessage(String uid) {
                boolean result=false;
                if (mDatabase.isOpen()) {
                        Cursor cursor=mDatabase.query(CHAT_TABLE_NAME,null,CHAT_FROM_ID+" =?",new String[]{uid},null,null,null);
                        if (cursor != null && cursor.moveToFirst()) {
                                result=true;
                        }
                        if (cursor != null && !cursor.isClosed()) {
                                cursor.close();
                        }
                }
                return result;
        }


        /**
         * 数据库辅助类
         */
        private class ChatSQLiteDBHelper extends SQLiteOpenHelper {
                private DBConfig.DBUpdateListener mListener;

                private ChatSQLiteDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DBConfig.DBUpdateListener listener) {
                        super(context, name, factory, version);
                        this.mListener = listener;
                }

                @Override
                public void onCreate(SQLiteDatabase db) {
//                        创建所有表
                        createChatMessageTable(db);
                }

                private void createChatMessageTable(SQLiteDatabase db) {

//                        创建聊天消息表
                        db.execSQL(SQL_CREATE_CHAT_MESSAGE_TABLE);
//                        创建最近会话消息表
                        db.execSQL(SQL_CREATE_RECENT_MESSAGE_TABLE);
//                        创建好友表
                        db.execSQL(SQL_CREATE_CONTACTS_TABLE);
//                        创建好友请求表
                        db.execSQL(SQL_CREATE_INVITATION_TABLE);
//                        创建群组消息表
                        db.execSQL(SQL_CREATE_GROUP_MESSAGE_TABLE);
//                        创建群结构表
                        db.execSQL(SQL_CREATE_GROUP_TABLE);
//                        创建说说消息主体（测试）
//                        db.execSQL(SQL_CREATE_SHARE_MESSAGE);

//                        创建评论消息主体
//                        db.execSQL(SQL_CREATE_COMMENT_MESSAGE);
////                        创建点赞消息表
//                        db.execSQL(SQL_CREATE_LIKER_MESSAGE);

//                        创建新的说说消息表
                        db.execSQL(SQL_CREATE_SHARED_MESSAGE);
//                        创建微信精选新闻缓存数据表
                        db.execSQL(SQL_CREATE_WEI_XIN_READ_STATUS);
                        db.execSQL(SQL_CREATE_HAPPY_MESSAGE);
                        db.execSQL(SQL_CREATE_PICTURE_MESSAGE);
                        db.execSQL(SQL_CREATE_HAPPY_CONTENT_MESSAGE);

//                        创建crash表
                        db.execSQL(SQL_CREATE_CRASH_MESSAGE_TABLE);

                }

                @Override
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                        if (mListener != null) {
                                mListener.onUpdate(db, oldVersion, newVersion);
                        }
                }
        }
}
