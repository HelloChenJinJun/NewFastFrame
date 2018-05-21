package com.example.chat.base;

import android.os.Environment;

import com.example.chat.bean.post.PublicCommentBean;

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
    public static final String TAG_MESSAGE_TYPE = "messageType";
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

    public static final Integer TAG_MSG_TYPE_VIDEO = 5;
    /**
     * 外部缓存数据的目录
     */
    private static final String BASE_CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "TestChat" + File.separator;
    /**
     * 声音的缓存目录
     */
    public static final String VOICE_CACHE_DIR = BASE_CACHE_DIR + "voice" + File.separator;

    public static final String IMAGE_CACHE_DIR = BASE_CACHE_DIR + "image" + File.separator;
    public static final String VIDEO_CACHE_DIR = BASE_CACHE_DIR + "video" + File.separator;
    public static final String IMAGE_COMPRESS_DIR = BASE_CACHE_DIR + "compress" + File.separator;
    /**
     * 请求地图的中心截屏图回调
     */
    public static final int REQUEST_MAP = 10;
    public static final boolean DEBUG = true;
//    查询好友的最大限制数量
    public static final int LIMIT_CONTACTS = 100;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0;
    public static final int REQUEST_CODE_SELECT_FROM_LOCAL = 1;
    public static final int REQUEST_CODE_LOCATION = 5;
    //        4de21dc60adabe2dff4e81a3a592459f
    public static final String KEY = "06cefecaee1c01cac71cb2f7de18dc9c";
    /**
     * 系统通知ID
     */
    public static final int NOTIFY_ID = 1;
    public static final String NOTIFICATION_TAG_AGREE = "notification_agree";
    public static final String NOTIFICATION_TAG_MESSAGE = "notification_message";
    public static final String NOTIFICATION_TAG_ADD = "notification_add";
    public static final String NOTIFICATION_TAG = "notification_tag";
    public static final String NOTIFICATION_TAG_GROUP_MESSAGE = "notification_group_message";
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
    public static final String GROUP_NOTIFICATION = "groupNotification";
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
    public static final String UPDATE_TIME = "update_time";
    public static final String UPDATE_TIME_SHARE = "update_time_share";
    public static final String UPDATE_TIME_COMMENT = "update_time_comment";
    public static final String LOCATION = "location";
    public static final String ADDRESS = "address";
    public static final String IS_EDIT = "IS_EDIT";
    public static final String EDIT_TYPE = "EDIT_TYPE";
    public static final int EDIT_TYPE_IMAGE = 1;
    public static final String DATA = "DATA";
    public static final int EDIT_TYPE_VIDEO = 2;
    public static final int EDIT_TYPE_SHARE = 3;
    public static final int EDIT_TYPE_TEXT = 4;


    public static final String DELTA_TIME = "delta_time";
    public static final String FROM = "FROM";
    public static final String POSITION = "POSITION";
    public static final String IS_SELECT = "IS_SELECT";
    public static final String IMAGE_PRE_VIEW = "PRE_VIEW";
    public static final String USER = "USER";
    public static final int EDIT_TYPE_VOICE = 5;




    public static final String LOGIN_STATUS = "LOGIN_STATUS";
    public static final String VIBRATE_STATUS = "VIBRATE_STATUS";
    public static final String VOICE_STATUS = "VOICE_STATUS";
    public static final String PUSH_NOTIFY = "PUSH_NOTIFY";
    public static final String BASE_URL = "http://www.quanmin.tv/";
    public static final String LAST_GROUP_MESSAGE_TIME = "last_group_message_time";
    public static final String LAST_SHARE_MESSAGE_TIME = "last_share_message_time";
    public static final String USER_DATA_LAST_UPDATE_TIME = "user_data_last_update_time";
    public static final String PUSH_STATUS = "PUSH_STATUS";
    public static final String COLUMN_NAME_CONTACTS = "contacts";
    public static final String COLUMN_NAME_ADD_BLACKLIST = "addBlack";
    public static final String COLUMN_NAME_OTHER_BLACKLIST = "otherBlack";
    public static final String NICK = "nick";
    public static final String GENDER ="gender" ;
    public static final String BIRTHDAY ="birthday" ;
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String SIGNATURE = "signature";
    public static final String AVATAR = "avatar";
    public static final int REQUEST_CODE_NORMAL = 10;
    public static final String CONTENT = "CONTENT";
    public static final String TAG_CONTENT_TYPE = "contentType";
    public static final String TIME = "time";
    public static final String TYPE_GROUP = "group";
    public static final String TYPE_PERSON = "person";

    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String IS_ONE = "IS_ONE";
    public static final String IS_CROP = "IS_CROP";
    public static final String IS_BROWSE = "is_browse";
    public static final int REQUEST_CODE_GROUP_NAME = 22;
    public static final int REQUEST_CODE_GROUP_DESCRIPTION = 23;
    public static final int REQUEST_CODE_GROUP_NOTIFICATION = 24;
    public static final String GROUP_REMIND = "reMind";
    public static final String FROM_CREATE_GROUP = "create_group";
    public static final int REQUEST_CODE_ADD_GROUP_NUMBER = 12;
    public static final int REQUEST_CODE_DELETE_GROUP_NUMBER=13;
    public static final String FROM_GROUP_INFO = "group_info";
    public static final String TITLE_WALLPAPER = "titleWallPaper";
    public static final String WALLPAPER = "wallPaper";
    public static final String CITY = "city";
    public static final String FIRST_LOGIN = "first_login";
    public static final String POST = "post";
    public static final String IS_PUBLIC = "is_public";
    public static final String PUBLIC = "public";
    public static final String REFRESH_TIME = "0000-00-00 01:00:00";
    public static final String TAG_COMMENT_ID = "commentId";
    public static final String TAG_CONTENT_URL = "contentUrl";
    public static final Integer TYPE_COMMENT = 0;
    public static final Integer TYPE_LIKE = 1;
    public static final Integer TYPE_SHARE = 2;
    public static final String TAG_ID = "id";
}
