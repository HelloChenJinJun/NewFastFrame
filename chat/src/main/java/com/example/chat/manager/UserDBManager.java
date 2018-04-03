package com.example.chat.manager;

import com.example.chat.base.Constant;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.GroupChatMessage;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.bean.User;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.chat.ChatMessageEntity;
import com.example.commonlibrary.bean.chat.ChatMessageEntityDao;
import com.example.commonlibrary.bean.chat.GroupChatEntity;
import com.example.commonlibrary.bean.chat.GroupChatEntityDao;
import com.example.commonlibrary.bean.chat.GroupTableEntity;
import com.example.commonlibrary.bean.chat.GroupTableEntityDao;
import com.example.commonlibrary.bean.chat.RecentMessageEntity;
import com.example.commonlibrary.bean.chat.RecentMessageEntityDao;
import com.example.commonlibrary.bean.chat.UserEntity;
import com.example.commonlibrary.bean.chat.UserEntityDao;
import com.example.commonlibrary.bean.music.DaoMaster;
import com.example.commonlibrary.bean.music.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/26     21:02
 * QQ:         1981367757
 */

public class UserDBManager {
    private static Map<String, UserDBManager> sMap = new HashMap<>();
    private DaoSession daoSession;


    public static UserDBManager getInstance() {
        return getInstance(UserManager.getInstance().getCurrentUserObjectId());
    }




    public static UserDBManager getInstance(String uid) {
        if (uid == null) {
            throw new RuntimeException("创建数据库中用户ID不能为空!!!!!!!!!");
        }
        if (sMap.get(uid) == null) {
            synchronized (UserDBManager.class) {
                if (sMap.get(uid) == null) {
                    UserDBManager userDBManager = new UserDBManager(uid);
                    sMap.put(uid, userDBManager);
                }
            }
        }
        return sMap.get(uid);
    }


    public DaoSession getDaoSession() {
        return daoSession;
    }

    private UserDBManager(String uid) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(BaseApplication.getInstance(), uid, null);
        Database database = devOpenHelper.getWritableDb();
        DaoMaster master = new DaoMaster(database);
        daoSession = master.newSession();
    }


    public void addOrUpdateContacts(List<User> friend) {
        if (friend == null) return;
        List<UserEntity> userEntityList = new ArrayList<>();
        for (User user :
                friend) {
            userEntityList.add(UserManager.getInstance().cover(user));
        }
        daoSession.getUserEntityDao()
                .insertOrReplaceInTx(userEntityList);
    }


    public void addOrUpdateBlack(List<User> list, int blackType) {
        if (list == null) return;
        List<UserEntity> userEntityList = new ArrayList<>();
        for (User user :
                list) {
            userEntityList.add(UserManager.getInstance().cover(user, false, true, blackType));
        }
        daoSession.getUserEntityDao()
                .insertOrReplaceInTx(userEntityList);
    }

    public void addOrUpdateGroupTable(List<GroupTableMessage> list) {
        if (list == null) return;
        List<GroupTableEntity> groupTableEntities = new ArrayList<>();
        for (GroupTableMessage groupTableMessage :
                list) {
            groupTableEntities.add(MsgManager.getInstance().cover(groupTableMessage));
        }
        daoSession.getGroupTableEntityDao()
                .insertOrReplaceInTx(groupTableEntities);
    }

    public void updateUserInfo() {
        daoSession.getUserEntityDao().update(UserManager.getInstance().cover(UserManager.getInstance().getCurrentUser()));

    }

    public boolean hasMessage(String conversationId, Long time) {
        return daoSession.getChatMessageEntityDao().queryBuilder()
                .where(ChatMessageEntityDao.Properties.ConversationId.eq(conversationId)
                        , ChatMessageEntityDao.Properties.CreatedTime.eq(time)
                ,ChatMessageEntityDao.Properties.MessageType.notEq(ChatMessage.MESSAGE_TYPE_READED)).build().list().size() > 0;
    }



    public boolean hasReadMessage(String conversationId,Long time){
        return daoSession.getChatMessageEntityDao().queryBuilder()
                .where(ChatMessageEntityDao.Properties.ConversationId.eq(conversationId)
                        , ChatMessageEntityDao.Properties.CreatedTime.eq(time)
                        ,ChatMessageEntityDao.Properties.MessageType.eq(ChatMessage.MESSAGE_TYPE_READED)
                ).build().list().size() > 0;
    }

    public void addChatMessage(ChatMessage message) {
        daoSession.getChatMessageEntityDao().insert(MsgManager.getInstance().cover(message));
    }


    public void addOrUpdateUser(User user) {
        daoSession.getUserEntityDao().insertOrReplace(UserManager.getInstance()
                .cover(user));
    }


    public void addOrUpdateUser(UserEntity userEntity){
        daoSession.getUserEntityDao().insertOrReplace(userEntity);
    }



    public void addOrUpdateRecentMessage(BaseMessage message) {
        daoSession.getRecentMessageEntityDao().insertOrReplace(MsgManager.getInstance().coverRecentMessage(message));
    }

    public void updateMessageReadStatus(String conversationId, Long createTime, Integer readStatusReaded) {
        ChatMessageEntity chatMessageEntity = getChatMessage(conversationId, createTime);
        chatMessageEntity.setReadStatus(readStatusReaded);
        daoSession.getChatMessageEntityDao()
                .update(chatMessageEntity);
    }


    public ChatMessageEntity getChatMessage(String conversationId, Long createdTime) {
        return daoSession.getChatMessageEntityDao()
                .queryBuilder().where(ChatMessageEntityDao.Properties.ConversationId.eq(conversationId)
                        , ChatMessageEntityDao.Properties.CreatedTime.eq(createdTime)).build().list().get(0);
    }

    public long getUnReadMessageSize() {
        return getUnReadChatMessageSize() + getUnReadGroupChatMessageSize();
    }


    public long getUnReadChatMessageSize() {
        return daoSession.getChatMessageEntityDao()
                .queryBuilder().where(ChatMessageEntityDao.Properties.ReadStatus.eq(Constant.RECEIVE_UNREAD)
                ,ChatMessageEntityDao.Properties.MessageType.in(ChatMessage.MESSAGE_TYPE_AGREE,ChatMessage.MESSAGE_TYPE_NORMAL))
                .count();
    }


    public long getUnReadChatMessageSize(String id) {
        return daoSession.getChatMessageEntityDao()
                .queryBuilder().where(ChatMessageEntityDao.Properties.ReadStatus.eq(Constant.RECEIVE_UNREAD)
                        , ChatMessageEntityDao.Properties.BelongId.eq(id)
                ,ChatMessageEntityDao.Properties.MessageType
                .in(ChatMessage.MESSAGE_TYPE_NORMAL,ChatMessage.MESSAGE_TYPE_AGREE))
                .count();
    }

    public long getUnReadGroupChatMessageSize(String id) {
        return daoSession.getGroupChatEntityDao()
                .queryBuilder().where(GroupChatEntityDao.Properties.ReadStatus.eq(Constant.RECEIVE_UNREAD)
                        , GroupChatEntityDao.Properties.GroupId.eq(id))
                .count();
    }


    public long getUnReadGroupChatMessageSize() {
        return daoSession.getGroupChatEntityDao()
                .queryBuilder().where(GroupChatEntityDao.Properties.ReadStatus.eq(Constant.RECEIVE_UNREAD))
                .count();
    }

    public long getAddInvitationMessageSize() {
        return daoSession.getChatMessageEntityDao().queryBuilder().where(ChatMessageEntityDao
                .Properties.MessageType.eq(ChatMessage.MESSAGE_TYPE_ADD)
        ,ChatMessageEntityDao.Properties.ToId.eq(UserManager
                .getInstance().getCurrentUserObjectId())
                ,ChatMessageEntityDao.Properties.ReadStatus.eq(Constant.RECEIVE_UNREAD)).count();
    }

    public RecentMessageEntity getRecentMessage(String id) {
        List<RecentMessageEntity> result = daoSession.getRecentMessageEntityDao().queryBuilder()
                .where(RecentMessageEntityDao.Properties.Id.eq(id)).build().list();
        return result.size() == 0 ? null : result.get(0);
    }

    public void addOrUpdateGroupChatMessage(GroupChatMessage groupChatMessage) {
        GroupChatEntity groupChatMessage1=getGroupChatMessage(groupChatMessage);
        if (groupChatMessage1!=null){
            daoSession.getGroupChatEntityDao().update(groupChatMessage1);
        }else {
            daoSession.getGroupChatEntityDao().insert(MsgManager.getInstance().cover(groupChatMessage));
        }

    }



    public GroupChatEntity getGroupChatMessage(GroupChatMessage groupChatMessage){
        List<GroupChatEntity>  list=daoSession.getGroupChatEntityDao().queryBuilder()
                .where(GroupChatEntityDao.Properties.GroupId.eq(groupChatMessage
                                .getGroupId()),GroupChatEntityDao
                                .Properties.BelongId.eq(groupChatMessage)
                        ,GroupChatEntityDao.Properties.CreatedTime.eq(groupChatMessage.getCreateTime())).list();
       return list.size()==0?null:list.get(0);
    }

    public UserEntity getUser(String id) {
        if (id.equals(UserManager.getInstance().getCurrentUserObjectId())){
            return UserManager.getInstance().cover(UserManager.getInstance().getCurrentUser());
        }
        List<UserEntity> list = daoSession.getUserEntityDao().queryBuilder().where(UserEntityDao.Properties
                .Uid.eq(id)).build().list();
        return list.size() == 0 ? null : list.get(0);

    }

    public GroupTableEntity getGroupTableEntity(String id) {
        List<GroupTableEntity> list = daoSession.getGroupTableEntityDao().queryBuilder().where(GroupTableEntityDao.Properties
                .GroupId.eq(id)).build().list();
        return list.size() == 0 ? null : list.get(0);
    }

    public List<RecentMessageEntity> getAllRecentMessage() {
        return daoSession.getRecentMessageEntityDao().queryBuilder()
                .list();
    }

    public void deleteRecentMessage(String id) {
        daoSession.getRecentMessageEntityDao().deleteByKey(id);
    }

    public void addOrUpdateGroupChatMessage(List<GroupChatMessage> groupChatMessageList) {
        if (groupChatMessageList.size() == 0) {
            return;
        }
        List<GroupChatEntity> list = new ArrayList<>(groupChatMessageList.size());
        for (GroupChatMessage item :
                groupChatMessageList) {
            list.add(MsgManager.getInstance().cover(item));
        }
        daoSession.getGroupChatEntityDao().insertOrReplaceInTx(list);
    }

    public void addOrUpdateRecentMessage(List<GroupChatMessage> groupChatMessageList) {

    }

    public List<String> getAllFriendId() {
        List<UserEntity> userEntityList = daoSession
                .getUserEntityDao()
                .queryBuilder().where(UserEntityDao.Properties
                        .IsStranger.eq(Boolean.FALSE)).build().list();
        List<String> list = new ArrayList<>(userEntityList.size());
        for (UserEntity item :
                userEntityList) {
            list.add(item.getUid());
        }
        return list;
    }

    public List<String> getAllGroupId() {
        List<GroupTableEntity> groupTableEntities = daoSession
                .getGroupTableEntityDao()
                .queryBuilder().build().list();
        List<String> list = new ArrayList<>(groupTableEntities.size());
        for (GroupTableEntity item :
                groupTableEntities) {
            list.add(item.getGroupId());
        }
        return list;
    }

    public void deleteGroupTableMessage(String groupId) {
        daoSession.getGroupTableEntityDao()
                .deleteByKey(groupId);
    }

    public void addOrUpdateGroupTable(GroupTableMessage groupTableMessage) {
        daoSession
                .getGroupTableEntityDao()
                .insertOrReplace(MsgManager.getInstance()
                        .cover(groupTableMessage));
    }

    public void deleteChatMessage(ChatMessage msg) {
        daoSession.getChatMessageEntityDao().queryBuilder()
                .where(ChatMessageEntityDao.Properties.ConversationId.eq(msg.getConversationId())
                        , ChatMessageEntityDao.Properties.CreatedTime.eq(msg.getCreateTime()))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public void addOrUpdateChatMessage(ChatMessage chatMessage) {
        ChatMessageEntity chatMessageEntity=getChatMessage(chatMessage);
        if (chatMessageEntity != null) {
           ChatMessageEntity entity= MsgManager.getInstance().cover(chatMessage);
            entity.setId(chatMessageEntity.getId());
            daoSession.getChatMessageEntityDao()
                    .update(entity);
        }else {
            daoSession.getChatMessageEntityDao()
                    .insert(MsgManager.getInstance().cover(chatMessage));
        }
    }



    public ChatMessageEntity getChatMessage(ChatMessage chatMessage){
        List<ChatMessageEntity>  list=daoSession.getChatMessageEntityDao().queryBuilder()
                .where(ChatMessageEntityDao
                                .Properties.ConversationId.eq(chatMessage.getConversationId())
                        ,ChatMessageEntityDao.Properties.CreatedTime.eq(chatMessage.getCreateTime())
                ,ChatMessageEntityDao.Properties.MessageType.eq(chatMessage.getMessageType())).list();
        return list.size()==0?null:list.get(0);
    }

    public List<ChatMessageEntity> getAllChatMessage(int messageTypeAdd) {
        return daoSession
                .getChatMessageEntityDao()
                .queryBuilder()
                .where(ChatMessageEntityDao.Properties
                        .MessageType.eq(messageTypeAdd)).build().list();
    }

    public void deleteChatMessage(ChatMessageEntity msg) {
        if (msg.getId() == 0L) {
            daoSession.getChatMessageEntityDao().queryBuilder()
                    .where(ChatMessageEntityDao.Properties.ConversationId.eq(msg.getConversationId())
                            , ChatMessageEntityDao.Properties.CreatedTime.eq(msg.getCreatedTime()))
                    .buildDelete().executeDeleteWithoutDetachingEntities();
        } else {
            daoSession.getChatMessageEntityDao()
                    .delete(msg);
        }
    }

    public void addOrUpdateUser(List<UserEntity> userEntityList) {
        daoSession.getUserEntityDao().insertOrReplaceInTx(userEntityList);
    }

    public List<UserEntity> getAllFriend() {
        return daoSession.getUserEntityDao()
                .queryBuilder().where(UserEntityDao.Properties.IsStranger.eq(Boolean.FALSE))
                .build().list();
    }

    public ChatMessageEntity getChatMessageForType(String id, int type) {
        List<ChatMessageEntity> list = daoSession.getChatMessageEntityDao()
                .queryBuilder().where(ChatMessageEntityDao.Properties
                        .BelongId.eq(id), ChatMessageEntityDao
                        .Properties.MessageType.eq(type)).build().list();
        return list.size() == 0 ? null : list.get(0);
    }

    public boolean updateMessageReadStatusForUser(String uid, int readStatus) {
        List<ChatMessageEntity> list = daoSession
                .getChatMessageEntityDao().queryBuilder()
                .where(ChatMessageEntityDao.Properties.BelongId.eq(uid)
                        , ChatMessageEntityDao.Properties.MessageType.in(ChatMessage.MESSAGE_TYPE_NORMAL
                        ,ChatMessage.MESSAGE_TYPE_AGREE)
                ,ChatMessageEntityDao.Properties.ReadStatus.eq(Constant.RECEIVE_UNREAD))
                .build().list();
        if (list.size()>0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setReadStatus(readStatus);
            }
        }
        daoSession.getChatMessageEntityDao().updateInTx(list);
        return list.size()>0;
    }

    public List<BaseMessage> getAllChatMessageById(String uid,long time) {
        String currentUserId=UserManager.getInstance().getCurrentUserObjectId();
        List<ChatMessageEntity>  chatMessageEntityList=
                daoSession.getChatMessageEntityDao().queryBuilder()
                .where(ChatMessageEntityDao.Properties.ConversationId.in(uid+"&"+
                                currentUserId,currentUserId+"&"+uid)
                ,ChatMessageEntityDao.Properties.MessageType.in(ChatMessage.MESSAGE_TYPE_NORMAL,ChatMessage.MESSAGE_TYPE_AGREE)
                ,ChatMessageEntityDao.Properties.CreatedTime.gt(time))
                        .orderAsc(ChatMessageEntityDao.Properties.CreatedTime)
                        .limit(10)
                .build().list();
        List<BaseMessage> result=new ArrayList<>(chatMessageEntityList.size());
        for (ChatMessageEntity item :
                chatMessageEntityList) {
            result.add(MsgManager.getInstance().cover(item));
        }
        return result;
    }

    public boolean updateGroupChatReadStatus(String groupId, Integer readStatus) {
        List<GroupChatEntity> list = daoSession
                .getGroupChatEntityDao().queryBuilder()
                .where(GroupChatEntityDao.Properties.GroupId.eq(groupId)
                        ,GroupChatEntityDao.Properties.ReadStatus.eq(Constant.RECEIVE_UNREAD))
                .build().list();
        if (list.size()>0) {
            for (GroupChatEntity item :
                    list) {
                item.setSendStatus(readStatus);
            }
            daoSession.getGroupChatEntityDao().updateInTx(list);
        }
        return list.size()>0;
    }

    public List<BaseMessage> getAllGroupChatMessageById(String groupId, long time) {
        List<GroupChatEntity>  groupChatEntities=
                daoSession.getGroupChatEntityDao().queryBuilder()
                        .where(GroupChatEntityDao.Properties.GroupId.eq(groupId)
                                ,GroupChatEntityDao.Properties.CreatedTime.gt(time))
                        .orderAsc(GroupChatEntityDao.Properties.CreatedTime)
                        .limit(10)
                        .build().list();
        List<BaseMessage> result=new ArrayList<>(groupChatEntities.size());
        for (GroupChatEntity item :
                groupChatEntities) {
            result.add(MsgManager.getInstance().cover(item));
        }
        return result;
    }

    public void deleteChatMessage(String conversationId, int messageType) {
        daoSession.getChatMessageEntityDao()
                .queryBuilder().where(ChatMessageEntityDao.Properties.ConversationId
        .eq(conversationId),ChatMessageEntityDao.Properties
        .MessageType.eq(messageType)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public void updateGroupChatReadStatus(String groupId, String belongId, Long createTime, Integer readStatus) {
        List<GroupChatEntity> list = daoSession
                .getGroupChatEntityDao().queryBuilder()
                .where(GroupChatEntityDao.Properties.GroupId.eq(groupId)
                        ,GroupChatEntityDao.Properties.BelongId
                        .eq(belongId),GroupChatEntityDao
                        .Properties.CreatedTime.eq(createTime)
                        ,GroupChatEntityDao.Properties.ReadStatus.eq(Constant.RECEIVE_UNREAD))
                .build().list();
        if (list.size()>0) {
            for (GroupChatEntity item :
                    list) {
                item.setSendStatus(readStatus);
            }
            daoSession.getGroupChatEntityDao().updateInTx(list);
        }

    }

    public List<UserEntity> getAllAddBlack() {
        return daoSession.getUserEntityDao().queryBuilder()
                .where(UserEntityDao.Properties.IsBlack.eq(Boolean.TRUE)
                ,UserEntityDao.Properties.BlackType.eq(UserEntity.BLACK_TYPE_ADD))
                .build().list();
    }

    public List<GroupTableEntity> getAllGroupTableMessage() {
        List<GroupTableEntity >  list=daoSession
                .getGroupTableEntityDao().queryBuilder()
                .build().list();
        return list;
    }

    public boolean isStranger(String uid) {
        List<UserEntity> list = daoSession.getUserEntityDao().queryBuilder().where(UserEntityDao.Properties
                .Uid.eq(uid)).build().list();
        return list.size() == 0 || list.get(0).isStranger();
    }

    public boolean isFriend(String uid) {
        return daoSession.getUserEntityDao().queryBuilder().where(UserEntityDao.Properties
                        .Uid.eq(uid),UserEntityDao.Properties.IsStranger.eq(Boolean.FALSE)
                ,UserEntityDao.Properties.IsBlack.eq(Boolean.FALSE)).build().list().size()>0;
    }


    public boolean isAddBlack(String uid) {
        return daoSession.getUserEntityDao().queryBuilder().where(UserEntityDao.Properties
                        .Uid.eq(uid),UserEntityDao.Properties.IsStranger.eq(Boolean.FALSE)
                ,UserEntityDao.Properties.IsBlack.eq(Boolean.FALSE)
                ,UserEntityDao.Properties.BlackType.eq(UserEntity.BLACK_TYPE_ADD)).build().list().size()>0;
    }

}
