package com.example.chat.manager;

import com.example.chat.base.Constant;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.CustomInstallation;
import com.example.chat.bean.GroupChatMessage;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.bean.ImageItem;
import com.example.chat.bean.MessageContent;
import com.example.chat.bean.post.PublicPostBean;
import com.example.chat.bean.User;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.bean.post.PublicCommentBean;
import com.example.chat.bean.post.ReplyCommentListBean;
import com.example.chat.bean.post.ShareTypeContent;
import com.example.chat.listener.AddFriendCallBackListener;
import com.example.chat.listener.OnCreateChatMessageListener;
import com.example.chat.listener.OnCreateGroupTableListener;
import com.example.chat.listener.OnCreatePublicPostListener;
import com.example.chat.listener.OnReceiveListener;
import com.example.chat.listener.OnSendPushMessageListener;
import com.example.chat.listener.OnSendTagMessageListener;
import com.example.chat.util.JsonUtil;
import com.example.chat.util.LogUtil;
import com.example.chat.util.SystemUtil;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.chat.ChatMessageEntity;
import com.example.commonlibrary.bean.chat.GroupChatEntity;
import com.example.commonlibrary.bean.chat.GroupTableEntity;
import com.example.commonlibrary.bean.chat.PostCommentEntity;
import com.example.commonlibrary.bean.chat.PublicPostEntity;
import com.example.commonlibrary.bean.chat.PublicPostEntityDao;
import com.example.commonlibrary.bean.chat.RecentMessageEntity;
import com.example.commonlibrary.bean.chat.UserEntityDao;
import com.example.commonlibrary.bean.chat.DaoSession;
import com.example.commonlibrary.utils.CommonLogger;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import rx.Subscription;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/9      12:16
 * QQ:             1981367757
 */
public class MsgManager {

    private static MsgManager instance;
    private BmobPushManager<CustomInstallation> mPushManager;
    private Gson gson;
    /**
     * 用于单例模式的双重锁定
     */
    private static final Object LOCK = new Object();

    private MsgManager() {
        mPushManager = new BmobPushManager<>();
        gson=BaseApplication.getAppComponent().getGson();
    }

    public static MsgManager getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new MsgManager();
                }
            }
        }
        return instance;
    }

    /**
     * 发送标签消息
     * 不保存在本地数据库    ,发送完后上传消息到服务器上面
     *
     * @param targetId 对方的ID
     * @param messageType      消息的类型
     * @param listener 回调
     */
    public void sendTagMessage(final String targetId, int messageType, final OnSendTagMessageListener listener) {
        final ChatMessage msg = createTagMessage(targetId, messageType);
//                                  在这里发送完同意请求后，把消息转为对方发送的消息
        if (messageType==ChatMessage.MESSAGE_TYPE_AGREE) {
            UserDBManager.getInstance().addOrUpdateRecentMessage(msg);
            LogUtil.e("保存同意消息到聊天消息表中");
//                                    这里将发送的欢迎消息转为对方发送
            ChatMessage chatMessage=new ChatMessage();
            chatMessage.setToId(msg.getBelongId());
            chatMessage.setMessageType(msg.getMessageType());
            chatMessage.setConversationId(targetId+"&"+UserManager
                    .getInstance().getCurrentUserObjectId());
            chatMessage.setBelongId(msg.getToId());
            chatMessage.setCreateTime(msg.getCreateTime());
            chatMessage.setSendStatus(msg.getSendStatus());
            chatMessage.setReadStatus(Constant.RECEIVE_UNREAD);
            chatMessage.setContentType(Constant.TAG_MSG_TYPE_TEXT);
            chatMessage.setContent(msg.getContent());
            UserDBManager.getInstance()
                    .addOrUpdateChatMessage(chatMessage);
        }
        saveMessageToService(msg, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null) {
                    listener.onSuccess(msg);
                    findInstallation(targetId, new FindListener<CustomInstallation>() {
                        @Override
                        public void done(List<CustomInstallation> list, BmobException e) {
                            if (e==null&&list!=null&&list.size()>0){
                                sendJsonMessage(list.get(0).getInstallationId(), createJsonMessage(msg),null);
                            }
                        }
                    });
                }else {
                    listener.onFailed(e);
                }
            }
        });


    }

    private void findInstallation(String uid, FindListener<CustomInstallation> listener) {
        BmobQuery<CustomInstallation> query = new BmobQuery<>();
        query.addWhereEqualTo("uid",uid);
        query.findObjects(listener);
    }

    /**
     * 上传消息到服务器中
     *
     * @param msg 消息
     */
    private void saveMessageToService(final ChatMessage msg,SaveListener<String> listener) {
        if (msg.getMessageType()==ChatMessage.MESSAGE_TYPE_READED) {
//            添加这一步验证主要是为了防止Bmob有时候上传多个回执消息的Bug,
            findReadTag(msg.getConversationId(), msg.getCreateTime(), new FindListener<ChatMessage>() {
                @Override
                public void done(List<ChatMessage> list, BmobException e) {
                    if (list == null || list.size() == 0) {
                        msg.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    LogUtil.e("保存消息到服务器上成功");
                                } else {
                                    LogUtil.e("保存消息到服务器上失败:" + e.toString());
                                }
                                if (listener != null) {
                                    listener.done(s, e);
                                }
                            }
                        });
                    }else {
                        if (listener != null) {
                            listener.done(null, e);
                        }
                    }
                }
            });
        } else {
            msg.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        LogUtil.e("保存消息到服务器上成功");
                    } else {
                        LogUtil.e("保存消息到服务器上失败:" + e.toString());
                    }

                    if (listener != null) {
                        listener.done(s, e);
                    }
                }

            });
        }
    }



    /**
     * 创建json
     *
     * @param message 消息
     * @return 放回json
     */
    private JSONObject createJsonMessage(ChatMessage message) {
        try {
            JSONObject result = new JSONObject();
            result.put(Constant.TAG_MESSAGE_READ_STATUS, message.getReadStatus());
            result.put(Constant.TAG_MESSAGE_SEND_STATUS, message.getSendStatus());
            result.put(Constant.TAG_CONTENT, message.getContent());
            result.put(Constant.TAG_CONVERSATION, message.getConversationId());
            result.put(Constant.TAG_CONTENT_TYPE,message.getContentType());
            result.put(Constant.TAG_MESSAGE_TYPE, message.getMessageType());
            result.put(Constant.TAG_CREATE_TIME, message.getCreateTime());
            result.put(Constant.TAG_BELONG_ID, message.getBelongId());
            result.put(Constant.TAG_TO_ID, message.getToId());
            CommonLogger.e("组装后的json:" + result.toString());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建标签实体消息类  目前有的标签类型消息：1、邀请消息2、同意消息3、回执消息
     *
     * @param targetId 对方ID
     * @return 标签实体类
     */
    public ChatMessage createTagMessage(String targetId,int messageType) {
        return createTagMessage(targetId,UserManager.getInstance().getCurrentUserObjectId()+"&"+targetId
        ,System.currentTimeMillis(),messageType);
    }

    public ChatMessage createTagMessage(String targetId,String conversationId,Long time,int messageType) {
        User user = UserManager.getInstance().getCurrentUser();
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setToId(targetId);
        chatMessage.setMessageType(messageType);
        chatMessage.setConversationId(conversationId);
        chatMessage.setBelongId(user.getObjectId());
        chatMessage.setCreateTime(time);
        chatMessage.setSendStatus(Constant.SEND_STATUS_SUCCESS);
        chatMessage.setReadStatus(Constant.READ_STATUS_UNREAD);
        chatMessage.setContentType(Constant.TAG_MSG_TYPE_TEXT);
        if (messageType==ChatMessage.MESSAGE_TYPE_AGREE) {
            MessageContent messageContent=new MessageContent();
            messageContent.setContent("你们已经成为好友可以聊天啦啦啦");
            chatMessage.setContent(gson.toJson(messageContent));
        }
        return chatMessage;

    }


    /**
     * 根据用户id到后台服务器去查找用户
     *
     * @param targetId     对方用户ID
     * @param findListener 回调
     */
    private void getUserById(String targetId, FindListener<User> findListener) {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", targetId);
        query.findObjects(findListener);
    }

    /**
     * 从json数据中解析和组成接受的聊天消息(不是标签消息)   并保存和上传到服务器上和发回执消息
     *
     * @param json     json数据
     * @param listener 回调
     */
    public void createReceiveMsg(String json,  OnReceiveListener listener) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            final ChatMessage message = new ChatMessage();
            message.setContentType(JsonUtil.getInt(jsonObject, Constant.TAG_CONTENT_TYPE));
            message.setToId(JsonUtil.getString(jsonObject, Constant.TAG_TO_ID));
            message.setBelongId(JsonUtil.getString(jsonObject, Constant.TAG_BELONG_ID));
            message.setCreateTime(JsonUtil.getLong(jsonObject, Constant.TAG_CREATE_TIME));
            message.setMessageType(JsonUtil.getInt(jsonObject, Constant.TAG_MESSAGE_TYPE));
            message.setConversationId(JsonUtil.getString(jsonObject, Constant.TAG_CONVERSATION));
            message.setReadStatus(Constant.RECEIVE_UNREAD);
            message.setSendStatus(Constant.SEND_STATUS_SUCCESS);
            message.setContent(JsonUtil.getString(jsonObject, Constant.TAG_CONTENT));
            dealReceiveChatMessage(message,listener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void dealReceiveGroupChatMessage(GroupChatMessage groupChatMessage){
        UserDBManager.getInstance().addOrUpdateGroupChatMessage(groupChatMessage);
        UserDBManager.getInstance().addOrUpdateRecentMessage(groupChatMessage);
    }


    public void dealReceiveGroupChatMessage(List<GroupChatMessage> groupChatMessageList){
        UserDBManager.getInstance().addOrUpdateGroupChatMessage(groupChatMessageList);
        if (groupChatMessageList.size()>0) {
            UserDBManager.getInstance().addOrUpdateRecentMessage(groupChatMessageList.get(0));
        }
    }

    public void dealReceiveChatMessage(ChatMessage message,final OnReceiveListener listener) {
        message.setReadStatus(Constant.RECEIVE_UNREAD);
        message.setSendStatus(Constant.SEND_STATUS_SUCCESS);
        switch (message.getMessageType()) {
            case ChatMessage.MESSAGE_TYPE_AGREE:
                LogUtil.e("接收到同意消息");
                if (!UserDBManager.getInstance().hasMessage(message.getConversationId(), message.getCreateTime())) {
                    UserManager.getInstance().addNewFriend(message.getBelongId(), message.getToId(), new AddFriendCallBackListener() {
                        @Override
                        public void onSuccess(User user) {
                            updateMsgReaded(false, message.getConversationId(), message.getCreateTime());
                            UserDBManager.getInstance().addChatMessage(message);
                            UserDBManager.getInstance().addOrUpdateRecentMessage(message);
                            listener.onSuccess(message);
                        }

                        @Override
                        public void onFailed(BmobException e) {
                            listener.onFailed(e);
                        }
                    });
                }else {
                    updateMsgReaded(false, message.getConversationId(), message.getCreateTime());
                }
                break;
            case ChatMessage.MESSAGE_TYPE_ADD:
                if (!UserDBManager.getInstance().hasMessage(message.getConversationId(), message.getCreateTime())) {
                    UserDBManager.getInstance().addChatMessage(message);
//                    这里获取邀请列表的用户数据
                    UserManager.getInstance().findUserById(message.getBelongId(),null);
                    updateMsgReaded(false, message.getConversationId(), message.getCreateTime());
                    listener.onSuccess(message);
                }else {
                    updateMsgReaded(false, message.getConversationId(), message.getCreateTime());
                }
                break;
            case ChatMessage.MESSAGE_TYPE_READED:
                if (!UserDBManager.getInstance().hasReadMessage(message.getConversationId(),message.getCreateTime())) {
                    UserDBManager.getInstance().addChatMessage(message);
                    updateMsgReaded(true, message.getConversationId(), message.getCreateTime());
                    UserDBManager.getInstance().updateMessageReadStatus(message.getConversationId(),message.getCreateTime(),Constant.READ_STATUS_READED);
                    listener.onSuccess(message);
                }
                break;
            default:
                //                                聊天消息
//                                接收到的消息有种情况，1、推送接收到的消息，已经在检测得到了，所以推送的就不要了
                if (!UserDBManager.getInstance().hasMessage(message.getConversationId(), message.getCreateTime())){
                    UserDBManager.getInstance().addChatMessage(message);
                    UserDBManager.getInstance().addOrUpdateRecentMessage(message);
                    updateMsgReaded(false,message.getConversationId(), message.getCreateTime());
                    sendAskReadMsg(message.getConversationId(), message.getCreateTime());
                    listener.onSuccess(message);
                }else {
                    updateMsgReaded(false, message.getConversationId(), message.getCreateTime());
                }
                break;
        }
    }

    public GroupChatMessage createReceiveGroupChatMsg(JSONObject jsonObject) {
        GroupChatMessage message = new GroupChatMessage();
        message.setGroupId(JsonUtil.getString(jsonObject, Constant.GROUP_ID));
        message.setContent(JsonUtil.getString(jsonObject, Constant.TAG_CONTENT));
        message.setReadStatus(Constant.RECEIVE_UNREAD);
        message.setSendStatus(Constant.SEND_STATUS_SUCCESS);
        message.setCreateTime(JsonUtil.getLong(jsonObject, Constant.TAG_CREATE_TIME));
        message.setBelongId(JsonUtil.getString(jsonObject, Constant.TAG_BELONG_ID));
        message.setContentType(JsonUtil.getInt(jsonObject, Constant.TAG_CONTENT_TYPE));
        message.setObjectId(JsonUtil.getString(jsonObject, Constant.ID));
        return message;
    }




    /**
     * 根据会话ID和消息的创建时间来发送一个回执已读消息
     *
     * @param conversationId 会话id
     * @param createTime     消息创建时间
     */
    private void sendAskReadMsg(final String conversationId, final Long createTime) {
        final ChatMessage chatMessage = createTagMessage(conversationId.split("&")[0],conversationId, createTime,ChatMessage.MESSAGE_TYPE_READED);
        saveMessageToService(chatMessage, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    findInstallation(conversationId.split("&")[0], new FindListener<CustomInstallation>() {
                        @Override
                        public void done(List<CustomInstallation> list, BmobException e) {
                            if (e==null&&list!=null&&list.size()>0){
                                sendJsonMessage(list.get(0).getInstallationId(), createJsonMessage(chatMessage),null);
                            }
                        }
                    });
                }else {
                    CommonLogger.e("保存到服务器已读类型消息失败"+e.toString());
                }
            }
        });
    }



    /**
     * 在服务器上面更新聊天消息的已读状态(首先根据用户ID在服务器上查询获取该消息,然后再更新该消息)
     *
     * @param isReadedMessage 查询的标志
     * @param id         会话ID或者是belongID
     * @param createTime 创建时间
     */
    public void updateMsgReaded(boolean isReadedMessage, String id, Long createTime) {
        queryMsg(isReadedMessage, id, createTime, new FindListener<ChatMessage>() {
                    @Override
                    public void done(List<ChatMessage> list, BmobException e) {
                        if (e == null) {
                            if (list != null && list.size() > 0) {
                                final ChatMessage chatMessage = list.get(0);
                                chatMessage.setReadStatus(Constant.READ_STATUS_READED);
                                chatMessage.update(new UpdateListener() {
                                                       @Override
                                                       public void done(BmobException e) {
                                                           if (e == null) {
                                                               LogUtil.e("更新服务器上的消息已读状态成功11111");
                                                           } else {
                                                               LogUtil.e("更新服务器上的消息已读消息失败" + e.toString());
                                                           }
                                                       }
                                                   }
                                );
                            }
                        } else {
                            LogUtil.e("查找聊天消息失败" + e.toString());
                        }
                    }
                }
        );
    }

    /**
     * 根据是否是标签和ID值(conversation 或  belongId)和创建时间来查询消息
     *
     * @param isReadedMessage   是否是标签消息
     * @param id           会话id或者是用户ID
     * @param createTime   创建时间
     * @param findListener 找到消息的回调
     */
    private void queryMsg(boolean isReadedMessage , String id, Long createTime, FindListener<ChatMessage> findListener) {
        BmobQuery<ChatMessage> query = new BmobQuery<>();
        query.addWhereEqualTo("conversationId", id);
        query.addWhereEqualTo("createTime", createTime);
        if (isReadedMessage){
            query.addWhereEqualTo("messageType",ChatMessage.MESSAGE_TYPE_READED);
        }
        query.findObjects(findListener);
    }











    public void queryGroupChatMessage(String groupId, FindListener<GroupChatMessage> listener) {
        if (groupId == null) {
            return;
        }
        List<String> list = new ArrayList<>();
        list.add(groupId);
        queryGroupChatMessage(list, listener);
    }


    public void queryGroupChatMessage(List<String> groupIdList, final FindListener<GroupChatMessage> listener) {
        if (groupIdList != null && groupIdList.size() > 0) {
            LogUtil.e("群id列表如下");
            for (int i = 0; i < groupIdList.size(); i++) {
                String groupId = groupIdList.get(i);
                LogUtil.e(groupId);
                BmobQuery<GroupChatMessage> query = new BmobQuery<>("g" + groupId);
                final RecentMessageEntity recentMsg=UserDBManager.getInstance().getRecentMessage(groupId);
                long lastTime;
                if (recentMsg == null) {
                    lastTime = 0;
                } else {
                    lastTime = recentMsg.getCreatedTime();
                }
                query.addWhereGreaterThan("updatedAt", new BmobDate(new Date(lastTime)));
                query.order("-updatedAt");
                query.findObjectsByTable(new QueryListener<JSONArray>() {
                    @Override
                    public void done(JSONArray jsonArray, BmobException e) {
                        if (e == null) {
                            LogUtil.e("群消息解析");
                            LogUtil.e("jsonArray：" + jsonArray.toString());
                            List<GroupChatMessage> list = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    GroupChatMessage groupChatMessage = MsgManager.getInstance().createReceiveGroupChatMsg(jsonObject);
                                    list.add(groupChatMessage);
                                } catch (JSONException item) {
                                    item.printStackTrace();
                                }
                            }
                            dealReceiveGroupChatMessage(list);
                            listener.done(list, null);
                        } else {
                            LogUtil.e("查询断网期间的群消息失败：" + e.toString());
                            listener.done(null, e);
                        }
                    }


                });
            }
        }
    }










    /**
     * 根据内容和目标ID创建消息实体
     *
     * @param content 内容
     * @param uid     用户ID
     * @return 消息实体
     */
    public ChatMessage createChatMessage(String content, String uid, int contentType) {
        User currentUser = UserManager.getInstance().getCurrentUser();
        ChatMessage message = new ChatMessage();
        message.setBelongId(currentUser.getObjectId());
        message.setToId(uid);
        message.setConversationId(currentUser.getObjectId() + "&" + uid);
//                 默认设置消息发送成功
        message.setSendStatus(Constant.SEND_STATUS_SUCCESS);
        message.setReadStatus(Constant.READ_STATUS_UNREAD);
        message.setContent(content);
        message.setCreateTime(System.currentTimeMillis());
        message.setContentType(contentType);
        message.setMessageType(ChatMessage.MESSAGE_TYPE_NORMAL);
        return message;
    }

    /**
     * 给其他设备发送下线通知(除了本设备)
     *
     * @param customInstallation 设备列表
     * @param listener           推送监听
     */
    void sendOfflineNotificationMsg(CustomInstallation customInstallation, PushListener listener) {
        JSONObject offlineJsonObject = createOfflineJsonObject();
        sendJsonMessage(customInstallation.getInstallationId(), offlineJsonObject, listener);
    }

    /**
     * 创建下载通知的jsonObject
     *
     * @return 下载通知的jsonObject
     */
    private JSONObject createOfflineJsonObject() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constant.MESSAGE_TAG, Constant.TAG_OFFLINE);
            jsonObject.put(Constant.TAG_BELONG_ID, UserManager.getInstance().getCurrentUserObjectId());
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 推送消息
     *
     * @param installationId 推送的设备ID
     * @param jsonObject     推送的jsonObject
     */
    private void sendJsonMessage(String installationId, JSONObject jsonObject, PushListener listener) {
        BmobQuery<CustomInstallation> query = new BmobQuery<>();
        query.addWhereEqualTo("installationId", installationId);
        mPushManager.setQuery(query);
        mPushManager.pushMessage(jsonObject, listener);
    }

    /**
     * 发送建群消息
     * <p>
     * 主要流程：
     * 1、构建群结构消息，保存到服务器上
     * 2、从服务器上获取该消息，并把该消息的id设置为群id,即groupId
     * 3、推送群结构消息到每个 成员中
     * 4、为每个成员在服务器上保存群结构消息，是为了防止推送失败时，对方不能获取到服务器上的群结构消息，也就不能定时拉取到群数据
     * 5、群主发送群欢迎消息给每个成员
     *
     * @param groupName        群组姓名
     * @param groupDescription 群组描述
     * @param contacts         群组成员(还没包括群主)
     */
    public void sendCreateGroupMessage(String groupName, final String groupDescription, final List<String> contacts, final OnCreateGroupTableListener listener) {
//                这里要把群主消息加进来
        contacts.add(0, UserManager.getInstance().getCurrentUser().getObjectId());
        GroupTableMessage message = createGroupTableMessage(groupName, groupDescription, contacts);
        message.setReadStatus(Constant.READ_STATUS_READED);
        message.save(new SaveListener<String>() {
                         @Override
                         public void done(String s, BmobException e) {
                             if (e == null) {
                                 message.setObjectId(s);
                                 message.setGroupId(message.getObjectId());
                                 message.update(new UpdateListener() {
                                     @Override
                                     public void done(BmobException e) {
                                         if (e == null) {
                                             UserDBManager.getInstance().addOrUpdateGroupTable(message);
//            这里先上传再推送，因为对方可能接收到推送信息的时候无法即时获取到服务器上的群结构消息,也就查询信息失败
                                             //  todo  群结构的解决
                                             uploadChatTableMessage(message, new QueryListListener<BatchResult>() {
                                                 @Override
                                                 public void done(List<BatchResult> list, BmobException e) {

                                                     if (e == null) {
                                                         LogUtil.e("批量保存群结构消息成功");
                                                         MessageContent messageContent=new MessageContent();
                                                         messageContent.setContent("大家好");
                                                         sendGroupChatMessage(createGroupChatMessage(gson.toJson(messageContent)
                                                                 , message.getGroupId(), Constant.TAG_MSG_TYPE_TEXT), new OnCreateChatMessageListener() {
                                                             @Override
                                                             public void onSuccess(BaseMessage baseMessage) {
                                                                 UserDBManager.getInstance().addOrUpdateGroupChatMessage((GroupChatMessage) baseMessage);

                                                                 UserDBManager.getInstance().addOrUpdateRecentMessage(baseMessage);

                                                                 UserDBManager
                                                                         .getInstance().addOrUpdateGroupTable(message);

                                                                 listener.done(message, null);
                                                             }

                                                             @Override
                                                             public void onFailed(String errorMsg, BaseMessage baseMessage) {
                                                                 UserDBManager
                                                                         .getInstance().addOrUpdateGroupTable(message);
                                                                 listener.done(message, null);                                                             }
                                                         });
                                                     } else {
                                                         listener.done(null, e);
                                                         LogUtil.e("批量保存群结构消息失败" + e.toString());
                                                     }
                                                     listener.done(null, e);
                                                 }
                                             });
                                         } else {
                                             LogUtil.e("更新群主的群结构消息失败" + e.toString());
                                             listener.done(null, e);
                                         }
                                     }

                                 });
                             } else {
                                 LogUtil.e("保存群主所建的群结构消息到服务器上失败" + e.toString());
                                 listener.done(null,e);
                             }

                         }
                     }
        );
    }


    private void uploadChatTableMessage(final GroupTableMessage groupTableMessage, QueryListListener<BatchResult> listener) {
        List<String> groupNumber = groupTableMessage.getGroupNumber();
        GroupTableMessage message;
        List<String> copy = new ArrayList<>(groupNumber);
        if (copy.contains(UserManager.getInstance().getCurrentUserObjectId())) {
            copy.remove(UserManager.getInstance().getCurrentUserObjectId());
        }
        List<BmobObject> groupTableMessageList = new ArrayList<>();
        for (int i = 0; i < copy.size(); i++) {
            message = new GroupTableMessage();
            message.setSendStatus(Constant.SEND_STATUS_SUCCESS);
            message.setReadStatus(Constant.READ_STATUS_UNREAD);
            message.setGroupDescription(groupTableMessage.getGroupDescription());
            message.setGroupId(groupTableMessage.getGroupId());
            message.setCreatedTime(groupTableMessage.getCreatedTime());
            message.setToId(copy.get(i));
            message.setGroupNumber(groupNumber);
            message.setGroupAvatar(groupTableMessage.getGroupAvatar());
            message.setGroupName(groupTableMessage.getGroupName());
            message.setNotification(groupTableMessage.getNotification());
            message.setCreatorId(groupTableMessage.getCreatorId());
            message.setRemind(message.getRemind());
            groupTableMessageList.add(message);
        }
        new BmobBatch().insertBatch(groupTableMessageList).doBatch(listener);
    }

    private GroupTableMessage createGroupTableMessage(String groupName, String groupDescription, List<String> contacts) {
        User currentUser = UserManager.getInstance().getCurrentUser();
        GroupTableMessage message = new GroupTableMessage();
        message.setGroupName(groupName);
        message.setGroupDescription(groupDescription);
        message.setGroupAvatar(currentUser.getAvatar() == null ? "" : currentUser.getAvatar());
        message.setCreatorId(UserManager.getInstance().getCurrentUserObjectId());
        message.setGroupNumber(contacts);
        message.setCreatedTime(System.currentTimeMillis());
        message.setReadStatus(Constant.READ_STATUS_UNREAD);
        message.setSendStatus(Constant.SEND_STATUS_SUCCESS);
        message.setToId(UserManager.getInstance().getCurrentUserObjectId());
        message.setNotification("");
        message.setRemind(Boolean.TRUE);
        return message;
    }



    public void queryGroupTableMessage(String uid, FindListener<GroupTableMessage> findListener) {
        BmobQuery<GroupTableMessage> query = new BmobQuery<>();
        query.addWhereEqualTo(Constant.TAG_TO_ID, uid);
        query.findObjects(findListener);
    }

    public GroupChatMessage createGroupChatMessage(String content, String groupId, int contentType) {
        GroupChatMessage message = new GroupChatMessage();
        message.setGroupId(groupId);
        message.setContent(content);
        message.setReadStatus(Constant.READ_STATUS_UNREAD);
        message.setContentType(contentType);
        message.setBelongId(UserManager.getInstance().getCurrentUserObjectId());
        message.setSendStatus(Constant.SEND_STATUS_SUCCESS);
        message.setCreateTime(System.currentTimeMillis());
        return message;
    }







    public GroupTableMessage createReceiveGroupTableMsg(String json) {
        GroupTableMessage message = gson.fromJson(json, GroupTableMessage.class);
        LogUtil.e("实时监听到的群结构消息如下1");
        if (message != null) {
            LogUtil.e(message);
        }
        return message;
    }




    public User createUserFromJsonObject(JSONObject jsonObject) {
        User user;
        user = gson.fromJson(jsonObject.toString(), User.class);
        LogUtil.e(user);
        return user;
    }




    private void findReadTag(String conversationId, long createTime, FindListener<ChatMessage> findListener) {
        BmobQuery<ChatMessage> query = new BmobQuery<>();
        query.addWhereEqualTo("conversationId", conversationId);
        query.addWhereEqualTo("createTime", createTime);
        query.addWhereEqualTo("messageType", ChatMessage.MESSAGE_TYPE_READED);
        query.findObjects(findListener);
    }


    public void getAllDefaultAvatarFromServer(final FindListener<String> findListener) {
        BmobQuery bmobQuery = new BmobQuery("sys_data");
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        bmobQuery.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    List<String> avatarList = null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        LogUtil.e(jsonArray.toString());
                        avatarList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                JSONObject avatarJson = jsonObject.getJSONObject("avatar");
                                String avatar = avatarJson.getString("url");
                                if (avatar != null) {
                                    avatarList.add(avatar);
                                }
                            } catch (JSONException item) {
                                item.printStackTrace();
                            }
                        }
                    }
                    if (avatarList == null) {
                        LogUtil.e("服务器上面没有头像数据");
                    }
                    findListener.done(avatarList, null);
                } else {
                    findListener.done(null, e);
                }
            }

        });
    }


    public void updateGroupMessage(final String groupId, final String name, final String content, final UpdateListener listener) {
        GroupTableMessage groupTableMessage=new GroupTableMessage();
        groupTableMessage.setObjectId(groupId);
        switch (name){
            case Constant.GROUP_AVATAR:
            groupTableMessage.setGroupAvatar(content);
            break;
            case Constant.GROUP_NOTIFICATION:
                groupTableMessage.setNotification(content);
                break;
            case Constant.GROUP_DESCRIPTION:
                groupTableMessage.setGroupDescription(content);
                break;
            case Constant.GROUP_NAME:
                groupTableMessage.setGroupName(content);
                break;
            case Constant.GROUP_REMIND:
                groupTableMessage.setRemind(Boolean.parseBoolean(content));
                break;
                default:
                    break;
        }
        groupTableMessage.update(listener);
    }


    public void getAllDefaultWallPaperFromServer(final FindListener<String> findListener) {
        BmobQuery bmobQuery = new BmobQuery("sys_data");
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        bmobQuery.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    List<String> avatarList = null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        LogUtil.e(jsonArray.toString());
                        avatarList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                JSONObject avatarJson = jsonObject.getJSONObject("wallpaper");
                                String avatar = avatarJson.getString("url");
                                if (avatar != null) {
                                    avatarList.add(avatar);
                                }
                            } catch (JSONException item) {
                                item.printStackTrace();
                            }
                        }
                    }
                    if (avatarList == null) {
                        LogUtil.e("服务器上面没有背景数据");
                    }
                    findListener.done(avatarList, null);
                } else {
                    findListener.done(null, e);
                }
            }


        });
    }

    public void getAllDefaultTitleWallPaperFromServer(final FindListener<String> findListener) {
        BmobQuery bmobQuery = new BmobQuery("sys_data");
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        bmobQuery.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    List<String> avatarList = null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        LogUtil.e(jsonArray.toString());
                        avatarList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {


                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                JSONObject avatarJson = jsonObject.getJSONObject("twallpaper");
                                String avatar = avatarJson.getString("url");
                                if (avatar != null) {
                                    avatarList.add(avatar);
                                }
                            } catch (JSONException item) {
                                item.printStackTrace();
                            }
                        }
                    }
                    if (avatarList == null) {
                        LogUtil.e("服务器上面没有背景数据");
                    }
                    findListener.done(avatarList, null);
                } else {
                    findListener.done(null, e);
                }
            }

        });
    }





    public void getAllPostData(boolean isRefresh, String uid, String time, FindListener<PublicPostBean> findCallback) {
        BmobQuery<PublicPostBean> query = new BmobQuery<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long currentTime = 0;
        try {
            currentTime = simpleDateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LogUtil.e("现在的时间:" + currentTime);
        BmobDate bmobDate;
        if (isRefresh) {
            bmobDate = new BmobDate(new Date(currentTime));
            String key=Constant.UPDATE_TIME_SHARE;
            if (uid != null) {
                key=key+uid;
            }
            String updateTime=BaseApplication
                    .getAppComponent()
                    .getSharedPreferences()
                    .getString(key,null);
            if (updateTime != null&&!time.equals("0000-00-00 01:00:00")) {
//                这里有个Bug,WhereGreaterThan 也查出相等时间的消息,所以在这里加上一秒的时间
                long resultTime=TimeUtil.getTime(updateTime,"yyyy-MM-dd HH:mm:ss")+1000;
                query.addWhereGreaterThan("updatedAt", new BmobDate(new Date(resultTime)));
            }else {
                query.addWhereGreaterThan("updatedAt", bmobDate);
            }
            query.addWhereGreaterThanOrEqualTo("createdAt", bmobDate);
        } else {
            currentTime -= 1000;
            LogUtil.e("减一秒后的时间" + currentTime);
            bmobDate = new BmobDate(new Date(currentTime));
            query.addWhereLessThan("createdAt", bmobDate);
        }
        if (uid != null) {
            User user=new User();
            user.setObjectId(uid);
            query.addWhereEqualTo("author",new BmobPointer(user));
        }
        query.order("-createdAt");
        query.include("author");
        query.setLimit(10);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);
        query.findObjects(findCallback);
    }

    public Subscription sendPublicPostMessage(int type, String content, String location, final ArrayList<ImageItem> imageList,
                                              String videoPath, String shotScreen, final PublicPostBean postBean, final OnCreatePublicPostListener listener) {
        final PublicPostBean publicPostBean = new PublicPostBean();
        publicPostBean.setMsgType(type);
        publicPostBean.setSendStatus(Constant.SEND_STATUS_SUCCESS);
        final PostDataBean postDataBean = new PostDataBean();
        postDataBean.setContent(content);
        publicPostBean.setLocation(location);
        publicPostBean.setAuthor(UserManager.getInstance().getCurrentUser());
        final Gson gson = BaseApplication
                .getAppComponent().getGson();
        if (type==Constant.EDIT_TYPE_SHARE) {
            ShareTypeContent shareTypeContent = new ShareTypeContent();
            User user = postBean.getAuthor();
            shareTypeContent.setAvatar(user.getAvatar());
            shareTypeContent.setNick(user.getNick());
            shareTypeContent.setUid(user.getObjectId());
            shareTypeContent.setSex(user.isSex());
            shareTypeContent.setAddress(user.getAddress());
            shareTypeContent.setPid(postBean.getObjectId());
            shareTypeContent.setLikeCount(postBean.getLikeCount());
            shareTypeContent.setCommentCount(postBean.getCommentCount());
            shareTypeContent.setShareCount(postBean.getShareCount());
            shareTypeContent.setCreateAt(postBean.getCreatedAt());
            shareTypeContent.setPostDataBean(gson.fromJson(postBean.getContent(), PostDataBean.class));
            postDataBean.setShareContent(shareTypeContent);
            postDataBean.setShareType(postBean.getItemViewType());
        }else if (type==Constant.EDIT_TYPE_IMAGE) {
            List<String> photoUrls = new ArrayList<>();
            if (imageList != null && imageList.size() > 0) {
                CommonLogger.e("发送的全部path为:");
                for (ImageItem imageItem :
                        imageList) {
                    photoUrls.add(SystemUtil.sizeCompress(imageItem.getPath(), 1080, 1920));
                    CommonLogger.e(imageItem.getPath());
                }
                postDataBean.setImageList(photoUrls);
            }
        }else if (type==Constant.EDIT_TYPE_VIDEO){
            List<String> photoUrls = new ArrayList<>();
            photoUrls.add(shotScreen);
            photoUrls.add(videoPath);
            postDataBean.setImageList(photoUrls);
        }
        publicPostBean.setContent(gson.toJson(postDataBean));
        listener.onSuccess(publicPostBean);
        return null;
    }

    public Subscription getCommentListData(String postId, FindListener<PublicCommentBean> listener, boolean isPullRefresh, String time) {
        PublicPostBean publicPostBean = new PublicPostBean();
        publicPostBean.setObjectId(postId);
        BmobQuery<PublicCommentBean> query = new BmobQuery<>();
        query.addWhereEqualTo(Constant.POST, publicPostBean);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long currentTime = 0;
        try {
            currentTime = simpleDateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BmobDate bmobDate=new BmobDate(new Date(currentTime));
        if (isPullRefresh) {
            if (!time.equals("0000-00-00 01:00:00")) {
                query.addWhereGreaterThan("updatedAt", bmobDate);
            }
            query.addWhereGreaterThanOrEqualTo("createdAt", bmobDate);
        } else {
            currentTime -= 1000;
            LogUtil.e("减一秒后的时间" + currentTime);
            bmobDate = new BmobDate(new Date(currentTime));
            query.addWhereLessThan("createdAt", bmobDate);
        }
        query.order("-createdAt");
        query.include("user,post");
        query.setLimit(10);
        return query.findObjects(listener);
    }

    public void getCommentListDetailData(String publicId, FindListener<ReplyCommentListBean> listener) {
        BmobQuery<ReplyCommentListBean> query = new BmobQuery<>();
        query.addWhereEqualTo("publicId", publicId);
        query.setLimit(50);
        query.findObjects(listener);
    }


    public PublicCommentBean cover(PostCommentEntity entity) {
        DaoSession daoSession=UserDBManager.getInstance().getDaoSession();
        PublicCommentBean posterComments = new PublicCommentBean();
        posterComments.setObjectId(entity.getCid());
        posterComments.setContent(entity.getContent());
        posterComments.setCreatedTime(TimeUtil.getTime(entity.getCreatedTime(), "yyyy-MM-dd HH:mm:ss"));
        posterComments.setUpdatedAt(TimeUtil.getTime(entity.getUpdatedTime(), "yyyy-MM-dd HH:mm:ss"));
        PublicPostEntity publicPostEntity = null;
        publicPostEntity =
                daoSession
                        .getPublicPostEntityDao()
                        .queryBuilder().where(PublicPostEntityDao.Properties.Pid.eq(entity.getPid())).build().list().get(0);
        User user;
        if (!publicPostEntity.getUid().equals(UserManager.getInstance().getCurrentUserObjectId())) {
            user = UserManager.getInstance().cover(daoSession
                    .getUserEntityDao()
                    .queryBuilder()
                    .where(UserEntityDao.Properties.Uid.eq(publicPostEntity.getUid())).build().list().get(0));
        } else {
            user = UserManager.getInstance().getCurrentUser();
        }
        PublicPostBean publicPostBean = MsgManager.getInstance().cover(publicPostEntity, user);
        posterComments.setPost(publicPostBean);
        User commentUser;
        if (!entity.getUid().equals(UserManager.getInstance().getCurrentUserObjectId())) {
            commentUser = UserManager.getInstance().cover(daoSession
                    .getUserEntityDao()
                    .queryBuilder()
                    .where(UserEntityDao.Properties.Uid.eq(entity.getUid())).build().list().get(0));
        } else {
            commentUser = UserManager.getInstance().getCurrentUser();
        }
        posterComments.setUser(commentUser);
        return posterComments;
    }


    public PublicPostBean cover(PublicPostEntity posterMessageEntity, User user) {
        PublicPostBean posterMessage = new PublicPostBean();
        posterMessage.setAuthor(user);
        posterMessage.setCreateTime(TimeUtil.getTime(posterMessageEntity.getCreatedTime(), "yyyy-MM-dd HH:mm:ss"));
        posterMessage.setUpdatedAt(TimeUtil.getTime(posterMessageEntity.getUpdatedTime(), "yyyy-MM-dd HH:mm:ss"));
        posterMessage.setCommentCount(posterMessageEntity.getCommentCount());
        posterMessage.setLikeCount(posterMessageEntity.getLikeCount());
        posterMessage.setContent(posterMessageEntity.getContent());
        posterMessage.setLocation(posterMessageEntity.getLocation());
        posterMessage.setLikeList(posterMessageEntity.getLikeList());
        posterMessage.setMsgType(posterMessageEntity.getMsgType());
        posterMessage.setShareCount(posterMessageEntity.getShareCount());
        return posterMessage;
    }

    public PublicPostEntity cover(PublicPostBean bean) {
        PublicPostEntity entity = new PublicPostEntity();
        entity.setSendStatus(bean.getSendStatus());
        entity.setCommentCount(bean.getCommentCount());
        entity.setLikeCount(bean.getLikeCount());
        entity.setContent(bean.getContent());
        entity.setLocation(bean.getLocation());
        entity.setLikeList(bean.getLikeList());
        entity.setPid(bean.getObjectId());
        entity.setUid(bean.getAuthor().getObjectId());
        entity.setMsgType(bean.getMsgType());
        entity.setShareCount(bean.getShareCount());
        entity.setCreatedTime(TimeUtil.getTime(bean.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
        if (bean.getUpdatedAt() != null) {
            entity.setUpdatedTime(TimeUtil.getTime(bean.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
        }
        return entity;
    }

    public Subscription reSendPublicPostBean(PublicPostBean data, OnCreatePublicPostListener listener) {
        PostDataBean bean =BaseApplication.getAppComponent().getGson().fromJson(data.getContent(), PostDataBean.class);
        if (data.getMsgType() == Constant.EDIT_TYPE_IMAGE) {
            List<String> imageList = bean.getImageList();
            if (imageList != null && imageList.size() > 0) {

                BmobFile.uploadBatch(imageList.toArray(new String[]{}), new UploadBatchListener() {
                    @Override
                    public void onSuccess(List<BmobFile> list2, List<String> list1) {
                        if (imageList.size() == list1.size()) {
                            CommonLogger.e("11全部上传图片成功");
                            bean.setImageList(list1);
                            data.setContent(BaseApplication.getAppComponent().getGson().toJson(bean));
                            data.setSendStatus(Constant.SEND_STATUS_SUCCESS);
                            data.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    bean.setImageList(imageList);
                                    data.setContent(BaseApplication.getAppComponent().getGson().toJson(bean));
                                    if (e == null) {
                                        data.setObjectId(s);
                                        listener.onSuccess(data);
                                    } else {
                                        listener.onFailed(e.getMessage(), e.getErrorCode(), data);
                                    }
                                }

                            });
                        } else {
                            CommonLogger.e("目前得到的URL集合为:");
                            for (String url :
                                    list1) {
                                CommonLogger.e(url);
                            }
                        }
                    }


                    @Override
                    public void onProgress(int i, int i1, int i2, int i3) {

                    }

                    @Override
                    public void onError(int i, String s) {
                        listener.onFailed(s, i, data);
                    }
                });
                return null;
            }
        } else if (data.getMsgType() == Constant.EDIT_TYPE_VIDEO) {
            List<String> stringList = new ArrayList<>(bean.getImageList());
            BmobFile.uploadBatch(bean.getImageList().toArray(new String[]{}), new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> list2, List<String> list1) {
                    if (bean.getImageList().size() == list1.size()) {
                        CommonLogger.e("全部上传视频成功");
                        bean.setImageList(list1);
                        data.setContent(BaseApplication.getAppComponent().getGson().toJson(bean));
                        data.setSendStatus(Constant.SEND_STATUS_SUCCESS);
                        data.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                bean.setImageList(stringList);
                                data.setContent(BaseApplication.getAppComponent().getGson().toJson(bean));
                                if (e == null) {
                                    data.setObjectId(s);
                                    listener.onSuccess(data);
                                } else {
                                    listener.onFailed(e.getMessage(), e.getErrorCode(), data);
                                }
                            }

                        });
                    } else {
                        CommonLogger.e("目前得到的URL集合为:");
                        for (String url :
                                list1) {
                            CommonLogger.e(url);
                        }
                    }
                }


                @Override
                public void onProgress(int i, int i1, int i2, int i3) {

                }

                @Override
                public void onError(int i, String s) {
                    listener.onFailed(s, i, data);
                }
            });
            return null;

        } else if (data.getMsgType() == Constant.EDIT_TYPE_TEXT
                ) {
            data.setSendStatus(Constant.SEND_STATUS_SUCCESS);
            return data.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        listener.onSuccess(data);
                    } else {
                        listener.onFailed(e.getMessage(), e.getErrorCode(), data);
                    }
                }
            });
        } else if (data.getMsgType() == Constant.EDIT_TYPE_SHARE) {
            data.setSendStatus(Constant.SEND_STATUS_SUCCESS);
            return data.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        PublicPostBean temp = new PublicPostBean();
                        temp.increment("shareCount");
                        temp.update(bean.getShareContent().getPid(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e != null) {
                                    CommonLogger.e("更新转发个数失败");
                                } else {
                                    CommonLogger.e("更新转发个数成功");
                                    int origin = bean.getShareContent().getShareCount();
                                    bean.getShareContent().setShareCount(origin + 1);
                                    data.setContent(BaseApplication.getAppComponent().getGson().toJson(bean));
                                }
                                listener.onSuccess(data);
                            }
                        });
                    } else {
                        listener.onFailed(e.getMessage(), e.getErrorCode(), data);
                    }
                }
            });
        }
        return null;
    }

    public Subscription updatePublicPostBean(PublicPostBean data, OnCreatePublicPostListener listener) {
        PostDataBean bean = BaseApplication.getAppComponent().getGson().fromJson(data.getContent(), PostDataBean.class);
        if (data.getMsgType() == Constant.EDIT_TYPE_IMAGE
                ) {
            int size = bean.getImageList().size();
            List<String> upLoad = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                String item = bean.getImageList().get(i);
                if (item.startsWith("http")
                        || item.startsWith(Constant.IMAGE_COMPRESS_DIR)) {
//                    没有改变
                } else {
                    upLoad.add(bean.getImageList().get(i));
                }
            }
            List<String> resultList = new ArrayList<>(bean.getImageList());
            if (upLoad.size() > 0) {
                BmobFile.uploadBatch(upLoad.toArray(new String[]{}), new UploadBatchListener() {
                    @Override
                    public void onSuccess(List<BmobFile> list, List<String> list1) {
                        if (upLoad.size() == list1.size()) {
                            int size = list.size();
                            for (int i = 0; i < size; i++) {
                                CommonLogger.e("上传文件本地" + list.get(i).getLocalFile().getAbsolutePath()
                                        + "云端:" + list.get(i).getUrl());
                                for (int j = 0; j < upLoad.size(); j++) {
                                    if (upLoad.get(j).equals(list.get(i).getLocalFile().getAbsolutePath())) {
                                        bean.getImageList().set(bean.getImageList().indexOf(upLoad.get(j)), list.get(i).getUrl());
                                    }
                                }
                            }
                            if (!data.getSendStatus().equals(Constant.SEND_STATUS_SUCCESS)) {
                                data.setSendStatus(Constant.SEND_STATUS_SUCCESS);
                            }
                            data.setContent(BaseApplication.getAppComponent().getGson().toJson(bean));
                            data.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    bean.setImageList(resultList);
                                    data.setContent(BaseApplication.getAppComponent().getGson().toJson(bean));
                                    if (e == null) {
                                        listener.onSuccess(data);
                                    } else {
                                        listener.onFailed(e.getMessage(), e.getErrorCode(), data);
                                    }
                                }
                            });

                        }
                    }

                    @Override
                    public void onProgress(int i, int i1, int i2, int i3) {

                    }

                    @Override
                    public void onError(int i, String s) {
                        listener.onFailed(s, i, data);
                    }
                });
                return null;
            } else {
                data.setContent(BaseApplication.getAppComponent().getGson().toJson(bean));
                if (!data.getSendStatus().equals(Constant.SEND_STATUS_SUCCESS)) {
                    data.setSendStatus(Constant.SEND_STATUS_SUCCESS);
                }
                return data.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            listener.onSuccess(data);
                        } else {
                            listener.onFailed(e.getMessage(), e.getErrorCode(), data);
                        }
                    }
                });
            }
        } else if (data.getMsgType() == Constant.EDIT_TYPE_TEXT
                || data.getMsgType() == Constant.EDIT_TYPE_SHARE
                || data.getMsgType() == Constant.EDIT_TYPE_VIDEO) {
            data.setContent(BaseApplication.getAppComponent().getGson().toJson(bean)
            );
            if (!data.getSendStatus().equals( Constant.SEND_STATUS_SUCCESS)) {
                data.setSendStatus(Constant.SEND_STATUS_SUCCESS);
            }
            return data.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        listener.onSuccess(data);
                    } else {
                        listener.onFailed(e.getMessage(), e.getErrorCode(), data);
                    }
                }
            });
        } else {
            return null;
        }
    }

    public GroupTableEntity cover(GroupTableMessage groupTableMessage) {
        GroupTableEntity groupTableEntity=new GroupTableEntity();
        groupTableEntity.setCreatedTime(groupTableMessage.getCreatedTime());
        groupTableEntity.setCreatorId(groupTableMessage.getCreatorId());
        groupTableEntity.setGroupAvatar(groupTableMessage.getGroupAvatar());
        groupTableEntity.setGroupDescription(groupTableMessage.getGroupDescription());
        groupTableEntity.setGroupId(groupTableMessage.getGroupId());
        groupTableEntity.setGroupName(groupTableMessage.getGroupName());
        groupTableEntity.setGroupNumber(groupTableMessage.getGroupNumber());
        groupTableEntity.setNotification(groupTableMessage.getNotification());
        groupTableEntity.setReadStatus(groupTableMessage.getReadStatus());
        groupTableEntity.setSendStatus(groupTableMessage.getSendStatus());
        groupTableEntity.setToId(groupTableMessage.getToId());
        return groupTableEntity;
    }

    public ChatMessageEntity cover(ChatMessage message) {
        ChatMessageEntity chatMessageEntity=new ChatMessageEntity();
        chatMessageEntity.setBelongId(message.getBelongId());
        chatMessageEntity.setContent(message.getContent());
        chatMessageEntity.setContentType(message.getContentType());
        chatMessageEntity.setConversationId(message.getConversationId());
        chatMessageEntity.setCreatedTime(message.getCreateTime());
        chatMessageEntity.setMessageType(message.getMessageType());
        chatMessageEntity.setReadStatus(message.getReadStatus());
        chatMessageEntity.setSendStatus(message.getSendStatus());
        chatMessageEntity.setToId(message.getToId());
        return chatMessageEntity;
    }

    public RecentMessageEntity coverRecentMessage(BaseMessage message) {
        RecentMessageEntity recentMessageEntity=new RecentMessageEntity();
        if (message instanceof ChatMessage) {
            recentMessageEntity.setType(RecentMessageEntity.TYPE_PERSON);
            if (message.getBelongId().equals(UserManager.getInstance()
            .getCurrentUserObjectId())) {
                recentMessageEntity.setId(((ChatMessage) message).getToId());
            }else {
                recentMessageEntity.setId(message.getBelongId());
            }
        } else if (message instanceof GroupChatMessage) {
            recentMessageEntity.setType(RecentMessageEntity.TYPE_GROUP);
            recentMessageEntity.setId(((GroupChatMessage) message).getGroupId());
        }
        recentMessageEntity.setContent(message.getContent());
        recentMessageEntity.setSendStatus(message.getSendStatus());
        recentMessageEntity.setContentType(message.getContentType());
        recentMessageEntity.setCreatedTime(message.getCreateTime());
        return recentMessageEntity;
    }

    public GroupChatEntity cover(GroupChatMessage groupChatMessage) {
        GroupChatEntity groupChatEntity=new GroupChatEntity();
        groupChatEntity.setBelongId(groupChatMessage.getBelongId());
        groupChatEntity.setContent(groupChatMessage.getContent());
        groupChatEntity.setCreatedTime(groupChatMessage.getCreateTime());
        groupChatEntity.setGroupId(groupChatMessage.getGroupId());
        groupChatEntity.setContentType(groupChatMessage.getContentType());
        groupChatEntity.setSendStatus(groupChatMessage.getSendStatus());
        groupChatEntity.setReadStatus(groupChatMessage.getReadStatus());
        return groupChatEntity;
    }

    public void refreshGroupChatMessage(FindListener<GroupChatMessage> findListener) {
        MsgManager.getInstance().queryGroupChatMessage(UserDBManager.getInstance().getAllGroupId(),findListener);


    }

    public ChatMessage cover(ChatMessageEntity chatMessageEntity) {
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setReadStatus(chatMessageEntity.getReadStatus());
        chatMessage.setContentType(chatMessageEntity.getContentType());
        chatMessage.setContent(chatMessageEntity.getContent());
        chatMessage.setSendStatus(chatMessageEntity.getSendStatus());
        chatMessage.setCreateTime(chatMessageEntity.getCreatedTime());
        chatMessage.setBelongId(chatMessageEntity.getBelongId());
        chatMessage.setConversationId(chatMessageEntity.getConversationId());
        chatMessage.setMessageType(chatMessageEntity.getMessageType());
        chatMessage.setToId(chatMessageEntity.getToId());
        return chatMessage;
    }

    public GroupChatMessage cover(GroupChatEntity item) {
        GroupChatMessage groupChatMessage=new GroupChatMessage();
        groupChatMessage.setContentType(item.getContentType());
        groupChatMessage.setContent(item.getContent());
        groupChatMessage.setGroupId(item.getGroupId());
        groupChatMessage.setBelongId(item.getBelongId());
        groupChatMessage.setCreateTime(item.getCreatedTime());
        groupChatMessage.setReadStatus(item.getReadStatus());
        groupChatMessage.setSendStatus(item.getSendStatus());
        return groupChatMessage;
    }

    public Subscription sendChatMessage(ChatMessage message, OnCreateChatMessageListener listener) {
        MessageContent messageContent=gson.fromJson(message.getContent(),MessageContent.class);
        int contentType=message.getContentType();
        if (contentType!=Constant.TAG_MSG_TYPE_TEXT){
            List<String>  urlList=messageContent.getUrlList();
            BmobFile.uploadBatch(urlList.toArray(new String[]{}), new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> list, List<String> list1) {
                    if (urlList.size()==list1.size()){
                        messageContent.setUrlList(list1);
                        message.setSendStatus(Constant.SEND_STATUS_SUCCESS);
                        message.setContent(gson.toJson(messageContent));
                        message.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                messageContent.setUrlList(urlList);
                                message.setContent(gson.toJson(messageContent));
                                if (e == null) {
                                    findInstallation(message.getToId(), new FindListener<CustomInstallation>() {
                                        @Override
                                        public void done(List<CustomInstallation> list, BmobException e) {
                                            if (e == null && list != null
                                                    && list.size() > 0) {
                                                sendJsonMessage(list.get(0).getInstallationId(), createJsonMessage(message),null);
                                            }
                                        }
                                    });
                                    listener.onSuccess(message);
//
                                }else {
                                    listener.onFailed(e.toString(),message);
                                }
                            }
                        });
                    }
                }

                @Override
                public void onProgress(int i, int i1, int i2, int i3) {

                }

                @Override
                public void onError(int i, String s) {
                    listener.onFailed(s+i,message);
                }
            });
            return null;
        }else {
            message.setSendStatus(Constant.SEND_STATUS_SUCCESS);
            return message.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        findInstallation(message.getToId(), new FindListener<CustomInstallation>() {
                            @Override
                            public void done(List<CustomInstallation> list, BmobException e) {
                                if (e == null && list != null
                                        && list.size() > 0) {
                                    sendJsonMessage(list.get(0).getInstallationId(), createJsonMessage(message),null);
                                }
                            }
                        });
                        listener.onSuccess(message);
                    }else {
                        listener.onFailed(e.toString(),message);
                    }
                }
            });
        }
    }

    public Subscription sendGroupChatMessage(GroupChatMessage message, OnCreateChatMessageListener listener) {
        MessageContent messageContent=gson.fromJson(message.getContent(),MessageContent.class);
        int contentType=message.getContentType();
        if (contentType!=Constant.TAG_MSG_TYPE_TEXT){
            List<String>  urlList=messageContent.getUrlList();
            BmobFile.uploadBatch(urlList.toArray(new String[]{}), new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> list, List<String> list1) {
                    if (urlList.size()==list1.size()){
                        messageContent.setUrlList(list1);
                        message.setSendStatus(Constant.SEND_STATUS_SUCCESS);
                        message.setContent(gson.toJson(messageContent));
                        message.setTableName("g"+message.getGroupId());
                        message.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                messageContent.setUrlList(urlList);
                                message.setContent(gson.toJson(messageContent));
                                if (e == null) {
                                    listener.onSuccess(message);
                                }else {
                                    listener.onFailed(e.toString(),message);
                                }
                            }
                        });
                    }
                }

                @Override
                public void onProgress(int i, int i1, int i2, int i3) {

                }

                @Override
                public void onError(int i, String s) {
                    listener.onFailed(s+i,message);
                }
            });
            return null;
        }else {
            message.setTableName("g"+message.getGroupId());
            message.setSendStatus(Constant.SEND_STATUS_SUCCESS);
            return message.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        listener.onSuccess(message);
                    }else {
                        listener.onFailed(e.toString(),message);
                    }
                }
            });
        }
    }

    public void exitGroup(String groupId,String uid,UpdateListener updateListener) {
        GroupTableEntity groupTableEntity= UserDBManager.getInstance()
                .getGroupTableEntity(groupId);
        GroupTableMessage groupTableMessage=new GroupTableMessage();
        groupTableMessage.setObjectId(groupTableEntity.getGroupId());
        groupTableEntity.setGroupNumber(groupTableEntity.getGroupNumber());
        groupTableMessage.getGroupNumber().remove(uid);
        groupTableMessage.update(updateListener);

    }

    public GroupTableMessage cover(GroupTableEntity
                                   entity) {
        return null;
    }
}

