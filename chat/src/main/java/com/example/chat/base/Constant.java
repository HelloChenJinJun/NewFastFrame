package com.example.chat.base;

import android.os.Environment;

import java.io.File;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/9      12:21
 * QQ:             1981367757
 */

public class Constant {
        /**
         * 添加为好友的标签
         */
        public static final String TAG_ADD_FRIEND = "add_friend";
        /**
         * 未读取状态
         */
        public static final Integer READ_STATUS_UNREAD = 0;
        //        读取状态
        public static final Integer READ_STATUS_READED = 2;
        public static final Integer RECEIVE_UNREAD = 1;
        public static final String PUSH_ALERT = "alert";
        //        消息标签
        public static final String MESSAGE_TAG = "tag";
        //        接受消息的标签
        public static final String TAG_BELONG_ID = "belongId";
        public static final String TAG_BELONG_AVATAR = "belongAvatar";
        public static final String TAG_BELONG_NICK = "belongNick";
        public static final String TAG_BELONG_NAME = "belongUserName";
        public static final String TAG_TO_ID = "toId";
        public static final String TAG_CREATE_TIME = "createTime";
        public static final String TAG_CONTENT = "content";
        public static final String TAG_MESSAGE_TYPE = "msgType";
        public static final String TAG_MESSAGE_SEND_STATUS = "sendStatus";
        public static final String TAG_MESSAGE_READ_STATUS = "readStatus";

        //             消息的发送状态
        public static final Integer SEND_STATUS_SUCCESS = 0;
        public static final Integer SEND_STATUS_FAILED = 1;
        public static final Integer SEND_STATUS_SENDING = 2;
        public static final Integer SEND_STATUS_START = 4;
        //        下线标签
        public static final String TAG_OFFLINE = "offline";
        //         是否为黑名单标签
        public static final String BLACK_NO = "no";
        public static final String BLACK_YES = "yes";
        public static final String TAG_ASK_READ = "ask_read";
        public static final String TAG_CONVERSATION = "conversationId";
        /**
         * 同意消息标签
         */
        public static final String TAG_AGREE = "agree";
        /**
         * 文体类型
         */
        public static final Integer TAG_MSG_TYPE_TEXT = 0;
        /**
         * 图片类型
         */
        public static final Integer TAG_MSG_TYPE_IMAGE = 1;
        /**
         * 语音类型
         */
        public static final Integer TAG_MSG_TYPE_VOICE = 2;
        /**
         * 位置类型
         */
        public static final Integer TAG_MSG_TYPE_LOCATION = 3;
        /**
         * 外部缓存数据的目录
         */
        private static final String BASE_CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "TestChat" + File.separator;
        /**
         * 声音的缓存目录
         */
        public static final String VOICE_CACHE_DIR = BASE_CACHE_DIR + "voice" + File.separator;

        public static final String IMAGE_CACHE_DIR = BASE_CACHE_DIR + "image" + File.separator;

        /**
         * 请求地图的中心截屏图回调
         */
        public static final int REQUEST_MAP = 10;
        public static final boolean DEBUG = true;
        public static final int LIMIT_CONTACTS = 100;
        public static final int REQUEST_CODE_TAKE_PICTURE = 0;
        public static final int REQUEST_CODE_SELECT_FROM_LOCAL = 1;
        public static final int REQUEST_CODE_LOCATION = 2;
        public static final String KEY = "4de21dc60adabe2dff4e81a3a592459f";
        /**
         * 系统通知ID
         */
        public static final int NOTIFY_ID = 1;
        public static final String NOTIFICATION_TAG_AGREE = "notification_agree";
        public static final String NOTIFICATION_TAG_MESSAGE = "notification_message";
        public static final String NOTIFICATION_TAG_ADD = "notification_add";
        public static final String NOTIFICATION_TAG = "notification_tag";
        public static final String NOTIFICATION_TAG_GROUP_MESSAGE = "notification_group_message";
        public static final String NEW_MESSAGE_ACTION = "new_message_action";
        public static final String NEW_MESSAGE = "new_message";
        public static final String GROUP_ID = "groupId";
        public static final String TYPE_CONVERSATION_PERSON = "person";
        public static final String TYPE_CONVERSATION_GROUP = "group";
        public static final String GROUP_AVATAR = "groupAvatar";
        public static final String GROUP_NAME = "groupName";
        public static final String GROUP_NICK = "groupNick";
        public static final String GROUP_DESCRIPTION = "groupDescription";
        public static final String GROUP_TIME = "createdTime";
        public static final String GROUP_CREATOR_ID = "creatorId";
        public static final String GROUP_NUMBER = "groupNumber";
        public static final String TYPE_CONVERSATION = "conversationType";

        public static final int REQUEST_CODE_EDIT_SHARE_MESSAGE = 101;
        public static final int REQUEST_CODE_SELECT_VISIBILITY = 102;
        public static final int CAMERA_LAYOUT = 0;
        public static final int NORMAL_LAYOUT = 1;
        public static final int MSG_TYPE_SHARE_MESSAGE_IMAGE = 10;
        public static final int MSG_TYPE_SHARE_MESSAGE_VIDEO = 11;
        public static final int MSG_TYPE_SHARE_MESSAGE_LINK = 12;
        public static final Integer SHARE_MESSAGE_VISIBLE_TYPE_PRIVATE = 1;
        public static final Integer SHARE_MESSAGE_VISIBLE_TYPE_PUBLIC = 2;
        public static final String RESULT_CODE_SELECT_VISIBILITY = "result_code_select_visibility";
        public static final int MSG_TYPE_SHARE_MESSAGE_TEXT = 13;
        public static final String RESULT_CODE_SHARE_MESSAGE = "result_code_share_message";
        public static final String NEW_SHARE_MESSAGE_ACTION = "new_share_message_action";
        public static final String SHARE_TIME = "share_time";
        public static final String NETWORK_CONNECTION_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
        public static final String ID = "objectId";
        public static final String GROUP_TABLE_TIME = "group_table_time";
        public static final int REQUEST_CODE_REGISTER = 20;
        public static final int REQUEST_CODE_TAKE_PHOTO = 15;
        public static final int REQUEST_CODE_SELECT_PICTURE = 11;
        public static final int REQUEST_CODE_CROP = 20;
        public static final int REQUEST_CODE_SEX = 50;
        public static final int REQUEST_CODE_BIRTH = 51;
        public static final int REQUEST_CODE_PHONE = 52;
        public static final int REQUEST_CODE_EMAIL = 53;
        public static final int REQUEST_CODE_SIGNATURE = 54;
        public static final int REQUEST_CODE_NICK = 55;
        public static final int REQUEST_CODE_EDIT_USER_INFO = 56;
        public static final int REQUEST_CODE_ADDRESS = 57;
        public static final String GROUP_NOTIFICATION = "notification";
        public static final int REQUEST_CODE_EDIT_GROUP_INFO = 50;
        public static final int REQUEST_CODE_EDIT_GROUP_INFO_NICK = 60;
        public static final int REQUEST_CODE_EDIT_GROUP_INFO_DESCRIPTION = 61;
        public static final int REQUEST_CODE_EDIT_GROUP_INFO_NOTIFICATION = 62;
        public static final int REQUEST_CODE_EDIT_GROUP_INFO_GROUP_NAME = 63;
        public static final String TIAN_XING_KEY = "96af073984a8a84160cf36994e3ffa92";
        public static final String JU_HE_KEY = "3f278e3d0afa3056d3116ceee1d6e995";
        public static final String HAPPY_KEY = "happy";
        public static final String WEI_XIN_KEY = "wei_xin";
        public static final String HAPPY_CONTENT_KEY = "happy_content";
        public static final String PICTURE_KEY = "picture_key";
        public static final int REQUEST_CODE_WEATHER_INFO = 100;
        public static final int REQUEST_CODE_SELECT_WALLPAPER = 102;
        public static final int REQUEST_CODE_SELECT_TITLE_WALLPAPER = 203;
        public static final String NOTIFY_CHANGE_ACTION = "notify_share_message_change";
}
