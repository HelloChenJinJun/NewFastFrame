package com.example.chat.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chat.R;
import com.example.chat.adapter.ChatMessageAdapter;
import com.example.chat.base.CommonImageLoader;
import com.example.chat.base.Constant;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.FaceText;
import com.example.chat.bean.GroupChatMessage;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.bean.ImageItem;
import com.example.chat.bean.User;
import com.example.chat.db.ChatDB;
import com.example.chat.events.GroupInfoEvent;
import com.example.chat.listener.AddFriendCallBackListener;
import com.example.chat.listener.OnMessageReceiveListener;
import com.example.chat.listener.OnSendMessageListener;
import com.example.chat.listener.SendFileListener;
import com.example.chat.manager.ChatNotificationManager;
import com.example.chat.manager.MessageCacheManager;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserCacheManager;
import com.example.chat.manager.UserManager;
import com.example.chat.manager.VoiceRecordManager;
import com.example.chat.receiver.PushMessageReceiver;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.FaceTextUtil;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.ToastUtils;

import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import io.reactivex.functions.Consumer;

/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/13      21:53
 * QQ:             1981367757
 */
public class ChatActivity extends SlideBaseActivity implements View.OnClickListener, TextWatcher, View.OnLongClickListener, View.OnTouchListener, ChatMessageAdapter.OnItemClickListener, OnMessageReceiveListener, SendFileListener {
        private SwipeRefreshLayout mSwipeRefreshLayout;
        private RecyclerView display;
        private Button add;
        private Button emotion;
        private EditText input;
        private Button speak;
        private Button voice;
        private Button send;
        private Button keyboard;
        private ViewPager mViewPager;
        private LinearLayout l1_more;
        private TextView location;
        private TextView picture;
        private TextView camera;
        private RelativeLayout r1_emotion;
        private LinearLayout l1_add;
        private RelativeLayout record_container;
        private ImageView record_display;
        private TextView record_tip;
        private LinearLayoutManager mLinearLayoutManager;
        private ChatMessageAdapter mAdapter;
        private String localImagePath;
        private List<FaceText> emotionFaceList;
        private GridViewAdapter gridViewAdapter;
        private GridViewAdapter mGridViewAdapter;
        private VoiceRecordManager mVoiceRecordManager;
        private String uid = "";
        private int[] voiceImageId;
        private Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                }
        };
        private User user;
        private String from;
        private String groupId;
        private GroupTableMessage mGroupTableMessage;
        private boolean exit=false;


        @Override
        public void initView() {
                initMiddleView();
                initBottomView();
        }

        private void initMiddleView() {
                record_container = (RelativeLayout) findViewById(R.id.r1_chat_middle_voice_container);
                record_display = (ImageView) findViewById(R.id.iv_chat_middle_voice_display);
                record_tip = (TextView) findViewById(R.id.tv_chat_middle_voice_tip);
                mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swl_chat_refresh);
                display = (RecyclerView) findViewById(R.id.rcy_chat_display);
        }


        private void initBottomView() {
                add = (Button) findViewById(R.id.btn_chat_bottom_add);
                emotion = (Button) findViewById(R.id.btn_chat_bottom_emotion);
                input = (EditText) findViewById(R.id.et_chat_bottom_input);
                speak = (Button) findViewById(R.id.btn_chat_bottom_speak);
                send = (Button) findViewById(R.id.btn_chat_bottom_send);
                voice = (Button) findViewById(R.id.btn_chat_bottom_voice);
                keyboard = (Button) findViewById(R.id.btn_chat_bottom_keyboard);
                l1_more = (LinearLayout) findViewById(R.id.l1_chat_bottom_more);
                mViewPager = (ViewPager) findViewById(R.id.vp_chat_bottom_emotion);
                r1_emotion = (RelativeLayout) findViewById(R.id.r1_chat_bottom_emotion);
                l1_add = (LinearLayout) findViewById(R.id.l1_chat_bottom_add);
                picture = (TextView) findViewById(R.id.tv_chat_bottom_picture);
                camera = (TextView) findViewById(R.id.tv_chat_bottom_camera);
                location = (TextView) findViewById(R.id.tv_chat_bottom_location);
                add.setOnClickListener(this);
                emotion.setOnClickListener(this);
                input.addTextChangedListener(this);
                input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                        LogUtil.e("聚焦");
                                        scrollToBottom();
                                        if (l1_more.getVisibility() == View.VISIBLE) {
                                                l1_more.setVisibility(View.GONE);
                                        }
                                }
                        }
                });
                voice.setOnClickListener(this);
                send.setOnClickListener(this);
                keyboard.setOnClickListener(this);
                location.setOnClickListener(this);
                picture.setOnClickListener(this);
                camera.setOnClickListener(this);
                speak.setOnTouchListener(this);
                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                                mSwipeRefreshLayout.setRefreshing(true);
                                BaseMessage message = mAdapter.getData(0);
                                LoadMessage(message);
                        }
                });
        }


        @Override
        public void initData() {
                from = getIntent().getStringExtra("from");
                if (from.equals("person")) {
                        user = (User) getIntent().getSerializableExtra("user");
                        uid = user.getObjectId();
                } else if (from.equals("group")) {
                        groupId = getIntent().getStringExtra("groupId");
                        mGroupTableMessage = MessageCacheManager.getInstance().getGroupTableMessage(groupId);
                }
                initActionBar();
//                 声音音量变化图片资源
                voiceImageId = new int[]{R.mipmap.chat_icon_voice1, R.mipmap.chat_icon_voice2, R.mipmap.chat_icon_voice3, R.mipmap.chat_icon_voice4,
                        R.mipmap.chat_icon_voice5, R.mipmap.chat_icon_voice6};
                initRecordManager();
                initEmotionInfo();
                mLinearLayoutManager = new LinearLayoutManager(this);
                display.setLayoutManager(mLinearLayoutManager);
//                display.setHasFixedSize(true);
                display.setItemAnimator(new DefaultItemAnimator());
//                mAdapter = new ChatAdapter(this, null);
                mAdapter = new ChatMessageAdapter();
                mAdapter.setOnItemClickListener(this);
                display.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
//                                        hideSoftInpuutView();
                                        CommonUtils.hideSoftInput(ChatActivity.this,input);
                                }
                        }
                });
                display.setAdapter(mAdapter);
                IntentFilter intentFilter = new IntentFilter(Constant.NEW_MESSAGE_ACTION);
                intentFilter.setPriority(20);
                registerReceiver(mReceiver, intentFilter);
                refreshData();
                scrollToBottom();
                registerRxBus();
        }


        private void registerRxBus() {
                RxBusManager.getInstance().registerEvent(GroupInfoEvent.class, new Consumer<GroupInfoEvent>() {
                        @Override
                        public void accept(GroupInfoEvent groupInfoEvent) {
//                                刷新过来的，更新下群结构消息
                                mGroupTableMessage = MessageCacheManager.getInstance().getGroupTableMessage(groupId);
                                if (mGroupTableMessage != null) {
                                        LogUtil.e(mGroupTableMessage);
                                }
                                int type = groupInfoEvent.getType();
                                String content = groupInfoEvent.getContent();
                                switch (type) {
                                        case GroupInfoEvent.TYPE_GROUP_NICK:
                                                List<BaseMessage> messageList = mAdapter.getData();
                                                if (messageList.size() > 0) {
                                                        for (BaseMessage message :
                                                                messageList) {
                                                                if (message.getBelongId().equals(UserManager.getInstance().getCurrentUserObjectId())) {
                                                                        message.setBelongNick(mGroupTableMessage.getGroupNick());
                                                                }
                                                        }
                                                }
                                                mAdapter.notifyDataSetChanged();
                                                break;
                                        case GroupInfoEvent.TYPE_GROUP_NAME:
                                                String title = mGroupTableMessage.getGroupName() + "(" + mGroupTableMessage.getGroupNumber().size() + ")";
                                                getCustomTitle().setText(title);
                                                break;
                                        case GroupInfoEvent.TYPE_GROUP_NOTIFICATION:
                                                LogUtil.e("这里要做群通知的界面展示" + content);
                                                break;
                                        case GroupInfoEvent.TYPE_GROUP_DESCRIPTION:
                                                LogUtil.e("这里要做群描述的界面展示" + content);
                                                break;
                                        case GroupInfoEvent.TYPE_GROUP_AVATRA:
                                                LogUtil.e("这里要做群头像的界面展示" + content);
                                                break;
                                        case GroupInfoEvent.TYPE_GROUP_NUMBER:
                                                if (groupId != null) {
                                                        LogUtil.e("这里通知成员的变化" + content);
                                                        if (content.equals(groupId)) {
                                                                exit=true;
                                                                Toast.makeText(ChatActivity.this, "你已经被提出该群", Toast.LENGTH_SHORT).show();
                                                        }
                                                }
                                                break;
                                        default:
                                                break;
                                }
                        }
                }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                                LogUtil.e("rxbus传递出现异常");
                                if (throwable != null) {
                                        LogUtil.e(throwable.getMessage());
                                }
                        }
                });
        }

        private void initActionBar() {
                String title;
                String avatar;
                if (from.equals("person")) {
                        title = user.getNick();
                        avatar = null;
                } else {
                        title = mGroupTableMessage.getGroupName() + "(" + mGroupTableMessage.getGroupNumber().size() + ")";
                        avatar = mGroupTableMessage.getGroupAvatar();
                }
                ToolBarOption toolBarOption = new ToolBarOption();
                toolBarOption.setNeedNavigation(true);
                toolBarOption.setAvatar(avatar);
                toolBarOption.setTitle(title);
                if (from.equals("group")) {
                        toolBarOption.setRightText("信息");
                        toolBarOption.setRightListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        GroupInfoActivity.start(ChatActivity.this, groupId, Constant.REQUEST_CODE_EDIT_GROUP_INFO);
                                }
                        });
                }
                setToolBar(toolBarOption);
        }
        

        private void refreshData() {
                LogUtil.e("refreshData12356");
                if (from.equals("person")) {
                        if (ChatDB.create().updateReceivedChatMessageReaded(user, true) > 0) {
                                LogUtil.e("更新该用户所发来的消息为已读状态成功");
                        } else {
                                LogUtil.e("更新该用户所发来的消息为已读状态失败");
                        }
                        mAdapter.clearAllData();
                        mAdapter.getData().addAll(ChatDB.create().queryChatMessagesFromDB(uid, 1, 0));
                } else {
                        if (ChatDB.create().updateReceivedGroupChatMessageReaded(groupId, true)) {
                                LogUtil.e("更新该群所发来的消息为已读状态成功");
                        } else {
                                LogUtil.e("更新该群所发来的消息为已读状态失败");
                        }
                        LogUtil.e("刷新群消息");
                        mAdapter.clearAllData();
                        mAdapter.getData().addAll(ChatDB.create().queryGroupChatMessageFromDB(groupId, 1, 0));
//                        mAdapter.getData().clear();
//                        mAdapter.getData().addAll(ChatDB.create().queryGroupChatMessageFromDB(groupId, 1, 0));
                        mAdapter.notifyDataSetChanged();
                }
                PushMessageReceiver.registerListener(this);
        }

        private ChatMessageReceiver mReceiver = new ChatMessageReceiver();

        @Override
        public void updateData(Object o) {

        }


        private class ChatMessageReceiver extends BroadcastReceiver {

                @Override
                public void onReceive(Context context, Intent intent) {
                        if (intent.getAction().equals(Constant.NEW_MESSAGE_ACTION)) {
                                if (intent.getStringExtra("from").equals("person") && from.equals("person")) {
                                        List<ChatMessage> list = (List<ChatMessage>) intent.getSerializableExtra(Constant.NEW_MESSAGE);
                                        processNewMessage(list);
                                } else if (intent.getStringExtra("from").equals("group")) {
                                        LogUtil.e("这里实时广播接收到实时群消息拉");
                                        GroupChatMessage message = (GroupChatMessage) intent.getSerializableExtra(Constant.NEW_MESSAGE);
                                        if (from.equals("group") && message.getGroupId().equals(groupId)) {
                                                if (MsgManager.getInstance().saveRecentAndChatGroupMessage(message)) {

                                                        boolean isRefresh=intent.getBooleanExtra("isRefresh",false);
                                                        if (!isRefresh&&!hasFocus) {
                                                                ChatNotificationManager.getInstance(ChatActivity.this).sendGroupMessageNotification(message, ChatActivity.this);
                                                        }

                                                        onNewGroupChatMessageCome(message);
                                                }
                                        } else {
                                                LogUtil.e("其他群消息交由主界面处理");
                                                return;
                                        }
                                } else if (intent.getStringExtra("from").equals("groupTable")) {
                                        LogUtil.e("这里聊天界面接收到群结构消息，直接返回，不取消广播，让主界面接受广播并解析");
                                        return;
                                } else if (intent.getStringExtra("from").equals("table")) {
                                        LogUtil.e("实时检测得到的群结构消息到啦,这是聊天界面");
                                        GroupTableMessage message = (GroupTableMessage) intent.getSerializableExtra(Constant.NEW_MESSAGE);
                                        if (from.equals("group") && message.getGroupId().equals(groupId)) {
//                                            自己的群界面
//                                                刷新群结构消息
                                                mGroupTableMessage = MessageCacheManager.getInstance().getGroupTableMessage(groupId);
                                                LogUtil.e(mGroupTableMessage);
                                                if (message.getGroupAvatar() != null) {
                                                        Glide.with(ChatActivity.this).load(message.getGroupAvatar()).into(getIcon());
                                                }
                                                getCustomTitle().setText(message.getGroupName() + "(" + message.getGroupNumber().size() + ")");
                                        } else {

                                                LogUtil.e("其他群结构表交由主界面处理");
                                                return;
                                        }
                                }
                        }
                        abortBroadcast();
                }
        }

        private void processNewMessage(List<ChatMessage> list) {

                for (final ChatMessage message : list
                        ) {
                        if (!message.getBelongId().equals(uid)) {
//                                如果不是该用户的消息，不接受
                                return;
                        }
                        message.setSendStatus(Constant.SEND_STATUS_SUCCESS);
                        message.setReadStatus(Constant.RECEIVE_UNREAD);
                        switch (message.getTag()) {
//                                邀请消息应该不会出现在这里
                                case Constant.TAG_AGREE:
                                        UserManager.getInstance().addNewFriend(message.getBelongId(), UserManager.getInstance().getCurrentUserObjectId(), new AddFriendCallBackListener() {
                                                @Override
                                                public void onSuccess(User user) {
                                                        LogUtil.e("在服务器上成功检测到未读的同意消息并保存到数据库中成功");
                                                        UserCacheManager.getInstance().addContact(user);
                                                        MsgManager.getInstance().saveAndUploadReceiverMessage(true, message);
                                                        onAgreeMessageCome(message);
                                                }

                                                @Override
                                                public void onFailed(BmobException e) {
                                                        LogUtil.e("保存从服务器上检测得来的同意消息失败");
                                                }
                                        });
                                        break;
                                case Constant.TAG_ADD_FRIEND:
//                                                        不处理
                                        break;
                                case Constant.TAG_ASK_READ:
                                        LogUtil.e("接收到的回执已读标签消息");
                                        LogUtil.e(message);
                                        LogUtil.e("这里更新已读消息为已读");
                                        MsgManager.getInstance().updateReadTagMsgReaded(message.getConversationId(), message.getCreateTime());
                                        if (ChatDB.create().hasFriend(message.getBelongId()) && ChatDB.create().isBlackUser(message.getBelongId())) {
                                                LogUtil.e("由于是黑名单，直接更新服务器上的已读回执消息为已读状态");
                                                MsgManager.getInstance().updateMsgReaded(false, message.getConversationId(), message.getCreateTime());
                                        } else {
                                                if (MsgManager.getInstance().uploadAndUpdateChatMessageReadStatus(message, true) > 0) {
                                                        LogUtil.e("在服务器上成功检测到未读的已读回执消息");
                                                        onAskReadMessageCome(message);
                                                } else {
                                                        LogUtil.e("保存从服务器上检测得来的回执已读消息失败");
                                                }
                                        }
                                        break;
                                default:
//                                                        默认则是聊天消息
                                        if (ChatDB.create().hasFriend(message.getBelongId()) && ChatDB.create().isBlackUser(message.getBelongId())) {
//                                                                                黑名单用户
                                                LogUtil.e("黑名单用户，不接受消息");
                                                MsgManager.getInstance().updateMsgReaded(false, message.getConversationId(), message.getCreateTime());
                                                LogUtil.e("更新服务器上黑名单发来的消息为已读");
                                        } else {
                                                if (MsgManager.getInstance().saveAndUploadReceiverMessage(true, message)) {
                                                        LogUtil.e("在服务器上成功检测到未读的聊天消息");
                                                        onNewChatMessageCome(message);
                                                } else {
                                                        LogUtil.e("在服务器上检测未读的聊天消息失败");
                                                }
                                        }
                                        break;

                        }

                }
        }

        private void initEmotionInfo() {
                List<View> list = new ArrayList<>();
                emotionFaceList = FaceTextUtil.getFaceTextList();
                for (int i = 0; i < 2; i++) {
                        list.add(getGridView(i));
                }
                mViewPager.setAdapter(new MyViewPagerAdapter(list));
        }

        private View getGridView(int i) {
                View emotionView = LayoutInflater.from(this).inflate(R.layout.emotion1, null);
                GridView gridView = (GridView) emotionView.findViewById(R.id.gv_display);
                if (i == 0) {
                        gridView.setAdapter(gridViewAdapter = new GridViewAdapter(this, emotionFaceList.subList(0, 21)));
                        gridView.setTag(gridViewAdapter);
                } else {
                        gridView.setAdapter(mGridViewAdapter = new GridViewAdapter(this, emotionFaceList.subList(21, emotionFaceList.size())));
                        gridView.setTag(mGridViewAdapter);
                }
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                GridViewAdapter gridViewAdapter = (GridViewAdapter) parent.getTag();
                                FaceText faceText = (FaceText) gridViewAdapter.getItem(position);
                                String content = faceText.getText();
                                if (input != null) {
                                        int startIndex = input.getSelectionStart();
                                        CharSequence content1 = input.getText().insert(startIndex, content);
                                        input.setText(FaceTextUtil.toSpannableString(ChatActivity.this.getApplicationContext(), content1.toString()));
//                                        重新定位光标位置
                                        CharSequence info = input.getText();
                                        if (info instanceof Spannable) {
                                                Spannable spannable = (Spannable) info;
                                                Selection.setSelection(spannable, startIndex + content.length());
                                        }
                                }
                        }
                });
                return emotionView;
        }

        private void initRecordManager() {
                mVoiceRecordManager = VoiceRecordManager.getInstance();
                mVoiceRecordManager.setOnVoiceChangeListener(new VoiceRecordManager.OnVoiceChangerListener() {
                        /**
                         * 范围：0~5
                         * @param value  值
                         */
                        @Override
                        public void onVoiceColumnChange(int value) {
                                record_display.setImageResource(voiceImageId[value]);
                        }

                        @Override
                        public void onRecordTimeChange(int time, String localVoicePath) {
                                if (time > VoiceRecordManager.MAX_RECORD_TIME) {
                                        time = VoiceRecordManager.MAX_RECORD_TIME;
//                                这里做一个机制，防止错误重复发多次语音
                                        speak.setPressed(false);
                                        speak.setClickable(false);
                                        sendVoiceMessage(localVoicePath, time);
                                        handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                        speak.setClickable(true);
                                                }
                                        }, 1000);
                                }
                        }
                });
        }

        /**
         * 发送声音文件
         *
         * @param localPath  声音文件的当地存储路径
         * @param recordTime 录制的时间
         */
        private void sendVoiceMessage(String localPath, int recordTime) {
                if (exit) {
                        Toast.makeText(this, "已经被提出该群，不能发送消息", Toast.LENGTH_SHORT).show();
                        return;
                }

                if (isBlack) {
                        ToastUtils.showShortToast("对方为黑名单，不能发送消息");
                        return;
                }

                MsgManager manager = MsgManager.getInstance();
                String id;
                final boolean result;
                if (from.equals("person")) {
                        id = uid;
                        result = true;
                } else {
                        result = false;
                        id = groupId;
                }
                manager.sendVoiceMessage(result, id, localPath, recordTime, new SendFileListener() {
                        @Override
                        public void onProgress(int progress) {

                        }

                        @Override
                        public void onStart(BaseMessage message) {
                                mAdapter.addData(message);
//                                mAdapter.addMessages(message);
                                LogUtil.e("语音文件");
                                if (result) {
                                        LogUtil.e(((ChatMessage) message));
                                } else {
                                        LogUtil.e(((GroupChatMessage) message));
                                }
                                scrollToBottom();
                        }

                        @Override
                        public void onSuccess() {
                                LogUtil.e("发送语音成功");
                                mAdapter.notifyDataSetChanged();
                                scrollToBottom();
                        }

                        @Override
                        public void onFailed(BmobException e) {
                                LogUtil.e("发送语音失败" + e.getMessage() + e.getErrorCode());
                                mAdapter.notifyDataSetChanged();
                                scrollToBottom();
                        }
                });
        }

        /**
         * 加载该消息之前的10调消息
         *
         * @param message 聊天消息
         */
        private void LoadMessage(BaseMessage message) {
                List<BaseMessage> list = new ArrayList<>();
                if (from.equals("person")) {
                        if (message != null) {
                                list.addAll(ChatDB.create().queryChatMessagesFromDB(uid, 1, Long.valueOf(message.getCreateTime())));
                        } else {
                                list.addAll(ChatDB.create().queryChatMessagesFromDB(uid, 1, 0));
                        }
                } else {
                        if (message != null) {
                                list.addAll(ChatDB.create().queryGroupChatMessageFromDB(groupId, 1, Long.valueOf(message.getCreateTime())));
                        } else {
                                list.addAll(ChatDB.create().queryGroupChatMessageFromDB(groupId, 1, 0));
                        }
                }
                int size = list.size();
                LogUtil.e("向上拉取的消息大小为" + size);
                mAdapter.addData(0, list);
//                mAdapter.addMessages(list);
                mLinearLayoutManager.scrollToPositionWithOffset(size, 0);
                mSwipeRefreshLayout.setRefreshing(false);
        }


        private boolean isBlack=false;


        @Override
        protected void onResume() {
                super.onResume();
                if (uid != null) {
                        isBlack=ChatDB.create().isBlackUser(uid);
                }
                PushMessageReceiver.registerListener(this);
        }

        @Override
        protected boolean isNeedHeadLayout() {
                return true;
        }

        @Override
        protected boolean isNeedEmptyLayout() {
                return false;
        }

        @Override
        protected int getContentLayout() {
                return R.layout.activity_chat;
        }

        @Override
        protected void onPause() {
                super.onPause();
                PushMessageReceiver.unRegisterListener(this);
        }

        private void scrollToBottom() {
                mLinearLayoutManager.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, 0);
        }

        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                        case R.id.btn_chat_bottom_add:
                                if (l1_more.getVisibility() == View.GONE) {
                                        l1_more.setVisibility(View.VISIBLE);
                                        l1_add.setVisibility(View.VISIBLE);
                                        r1_emotion.setVisibility(View.GONE);
                                        CommonUtils.hideSoftInput(this,input);
                                } else if (l1_add.getVisibility() == View.VISIBLE) {
                                        l1_more.setVisibility(View.GONE);
                                } else {
                                        r1_emotion.setVisibility(View.GONE);
                                        l1_add.setVisibility(View.VISIBLE);
                                }
                                break;
                        case R.id.btn_chat_bottom_emotion:
                                if (speak.getVisibility() == View.VISIBLE) {
                                        keyboard.setVisibility(View.GONE);
                                        speak.setVisibility(View.GONE);
                                        input.setVisibility(View.VISIBLE);
                                        send.setVisibility(View.VISIBLE);
                                }
                                if (l1_more.getVisibility() == View.GONE) {
                                        l1_more.setVisibility(View.VISIBLE);
                                        l1_add.setVisibility(View.GONE);
                                        r1_emotion.setVisibility(View.VISIBLE);
                                        CommonUtils.hideSoftInput(this,input);
                                } else if (r1_emotion.getVisibility() == View.VISIBLE) {
                                        l1_more.setVisibility(View.GONE);
                                } else {
                                        l1_add.setVisibility(View.GONE);
                                        r1_emotion.setVisibility(View.VISIBLE);
                                        CommonUtils.hideSoftInput(this,input);
                                }
                                break;
                        case R.id.btn_chat_bottom_voice:
                                if (l1_more.getVisibility() == View.VISIBLE) {
                                        l1_more.setVisibility(View.GONE);
                                }
                                keyboard.setVisibility(View.VISIBLE);
                                speak.setVisibility(View.VISIBLE);
                                input.setVisibility(View.GONE);
                                voice.setVisibility(View.GONE);
                                CommonUtils.hideSoftInput(this,input);
                                break;
                        case R.id.btn_chat_bottom_keyboard:
                                keyboard.setVisibility(View.GONE);
                                voice.setVisibility(View.VISIBLE);
                                speak.setVisibility(View.GONE);
                                input.setVisibility(View.VISIBLE);
                                break;
                        case R.id.btn_chat_bottom_send:
                                if (l1_more.getVisibility() == View.VISIBLE) {
                                        l1_more.setVisibility(View.GONE);
                                }
                                if (TextUtils.isEmpty(input.getText().toString().trim())) {
                                        ToastUtils.showShortToast(getString(R.string.chat_input_empty));
                                        input.setText("");
                                        return;
                                }
                                sendTextMessage(input.getText().toString().trim());
                                input.setText("");
                                break;
                        case R.id.tv_chat_bottom_camera:
//                                File imageFile = FileUtil.newFile(FileUtil.newDir(Constant.IMAGE_CACHE_DIR).getAbsolutePath() + System.currentTimeMillis() + ".jpg");
//                                localImagePath = imageFile.getAbsolutePath();
                                localImagePath = CommonImageLoader.getInstance().takePhoto(this, Constant.REQUEST_CODE_TAKE_PICTURE).getAbsolutePath();
                                break;
                        case R.id.tv_chat_bottom_picture:
                                CommonImageLoader.getInstance().pickPhoto(this, Constant.REQUEST_CODE_SELECT_FROM_LOCAL);
                                break;
                        case R.id.tv_chat_bottom_location:
                                Intent locationIntent = new Intent(this, MapActivity.class);
                                startActivityForResult(locationIntent, Constant.REQUEST_MAP);
                                break;
                }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == Activity.RESULT_OK) {
                        switch (requestCode) {
                                case Constant.REQUEST_CODE_TAKE_PICTURE:
                                        LogUtil.e("拍照得到的图片路径" + localImagePath);
                                        LogUtil.e("图片是否存在" + new File(localImagePath).exists());
                                        LogUtil.e("图片的大小为" + new File(localImagePath).length());
                                        sendImageMessage(localImagePath);
                                        break;
                                case Constant.REQUEST_CODE_SELECT_FROM_LOCAL:
                                        if (data != null) {
                                                Uri uri = data.getData();
                                                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                                                if (cursor != null && cursor.moveToFirst()) {
                                                        int index = cursor.getColumnIndex("_data");
                                                        String path = cursor.getString(index);
                                                        if (path != null) {
                                                                LogUtil.e("获取的到的图片路径" + path);
                                                                sendImageMessage(path);
                                                        } else {
                                                                ToastUtils.showShortToast("找不到你要的图片");
                                                        }
                                                }
                                                if (cursor != null && !cursor.isClosed()) {
                                                        cursor.close();
                                                }
                                        }
                                        break;
                                case Constant.REQUEST_CODE_LOCATION:
                                        break;
                                case Constant.REQUEST_MAP:
                                        if (data != null) {
                                                String localPath = data.getStringExtra("localPath");
                                                String longitude = data.getStringExtra("longitude");
                                                String latitude = data.getStringExtra("latitude");
                                                String address = data.getStringExtra("address");
                                                LogUtil.e("localPath:" + localPath + "    longitude:" + longitude + "      latitude:" + latitude + "     address:" + address);
                                                sendLocationChatMessage(localPath, latitude, longitude, address);
                                        }
                                        break;
                                case Constant.REQUEST_CODE_EDIT_GROUP_INFO:
//                                        refreshData();
//                                        String nick = data.getStringExtra("nick");
//                                        List<BaseMessage> list = mAdapter.getAllData();
//                                        for (BaseMessage message :
//                                                list) {
//                                                if (message.getBelongId().equals(UserCacheManager.getInstance().getUser().getObjectId())) {
//                                                        message.setBelongNick(nick);
//                                                }
//                                        }
//                                        通知数据刷新，因为群昵称改变
                                        mAdapter.clearAllData();
                                        mAdapter.getData().addAll(ChatDB.create().queryGroupChatMessageFromDB(groupId, 1, 0));
                                        mAdapter.notifyDataSetChanged();
                                        break;

                        }
                }
        }

        /**
         * 发送地址消息
         *
         * @param localPath 图片
         * @param latitude  纬度
         * @param longitude 经度
         * @param address   地址
         */
        private void sendLocationChatMessage(String localPath, String latitude, String longitude, String address) {
                if (exit) {
                        Toast.makeText(this, "已经被提出该群，不能发送消息", Toast.LENGTH_SHORT).show();
                        return;
                }
                if (isBlack) {
                        ToastUtils.showShortToast("对方为黑名单，不能发送消息");
                        return;
                }
                if (l1_more.getVisibility() == View.VISIBLE) {
                        l1_more.setVisibility(View.GONE);
                        l1_add.setVisibility(View.GONE);
                        r1_emotion.setVisibility(View.GONE);
                }
                String id;
                boolean result;
                if (from.equals("person")) {
                        id = uid;
                        result = true;
                } else {
                        result = false;
                        id = groupId;
                }
                MsgManager msgManager = MsgManager.getInstance();
                String content = localPath + "," + latitude + "," + longitude + "," + address;
                msgManager.sendLocationMessage(result, id, content,
                        new SendFileListener() {
                                @Override
                                public void onProgress(int progress) {
                                }

                                @Override
                                public void onStart(BaseMessage message) {
                                        mAdapter.addData(message);
                                        scrollToBottom();
                                }

                                @Override
                                public void onSuccess() {
                                        mAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailed(BmobException e) {
                                        mAdapter.notifyDataSetChanged();
                                }
                        });

        }

        /**
         * 发送图片消息
         *
         * @param localImagePath 图片的本地地址
         */
        private void sendImageMessage(String localImagePath) {
                if (exit) {
                        Toast.makeText(this, "已经被提出该群，不能发送消息", Toast.LENGTH_SHORT).show();
                        return;
                }
                if (isBlack) {
                        ToastUtils.showShortToast("对方为黑名单，不能发送消息");
                        return;
                }

                if (l1_more.getVisibility() == View.VISIBLE) {
                        l1_more.setVisibility(View.GONE);
                        l1_add.setVisibility(View.GONE);
                        r1_emotion.setVisibility(View.GONE);
                }
                String id;
                boolean result;
                if (from.equals("person")) {
                        result = true;
                        id = uid;
                } else {
                        result = false;
                        id = groupId;
                }
                final MsgManager msgManager = MsgManager.getInstance();
                msgManager.sendImageMessage(result, id, localImagePath, new SendFileListener() {
                        @Override
                        public void onProgress(int progress) {
//                                这里可以做图片上传的操作
                        }

                        @Override
                        public void onStart(BaseMessage message) {


                                LogUtil.e("onStart");
                                LogUtil.e("图片消息");
                                mAdapter.addData(message);
                                scrollToBottom();
                                File file = new File(message.getContent());
                                while (file.exists() && file.length() == 0) {
                                        try {
                                                Thread.sleep(200);
                                        } catch (InterruptedException e) {
                                                e.printStackTrace();
                                                LogUtil.e("异常");
                                        }
                                }
                        }

                        @Override
                        public void onSuccess() {
                                LogUtil.e("发送图片成功");
                                mAdapter.notifyDataSetChanged();
                                scrollToBottom();
                        }

                        @Override
                        public void onFailed(BmobException e) {
                                mAdapter.notifyDataSetChanged();
                                scrollToBottom();
                                LogUtil.e("发送图片消息失败" + e.getMessage() + e.getErrorCode());
                        }
                });
        }

        /**
         * 发送文本消息
         *
         * @param content 内容
         */
        private void sendTextMessage(final String content) {
                if (exit) {
                        Toast.makeText(this, "已经被提出该群，不能发送消息", Toast.LENGTH_SHORT).show();
                        return;
                }
                if (isBlack) {
                        ToastUtils.showShortToast("对方为黑名单，不能发送消息");
                        return;
                }
                MsgManager manager = MsgManager.getInstance();
                BaseMessage baseMessage;
                String id;
                if (from.equals("person")) {
                        id = uid;
                        baseMessage = manager.createChatMessage(content, id, Constant.TAG_MSG_TYPE_TEXT);
                } else {
                        id = groupId;
                        LogUtil.e("群ID为" + id);
                        baseMessage = manager.createGroupChatMessage(content, id, Constant.TAG_MSG_TYPE_TEXT);
                }
                mAdapter.addData(baseMessage);
                scrollToBottom();
                manager.sendTextMessage(false, baseMessage,
                        new OnSendMessageListener() {
                                @Override
                                public void onSending() {
                                        mAdapter.notifyDataSetChanged();
                                        scrollToBottom();
                                        LogUtil.e("发送中.......");
                                }

                                @Override
                                public void onSuccess() {
////                                        这里不需要通知数据的改变,因为在创建该消息的时候已经默认为发送成功状态
//                                        finalBaseMessage.setSendStatus(Constant.SEND_STATUS_SUCCESS);
                                        LogUtil.e("发送成功");
                                        mAdapter.notifyDataSetChanged();
                                        scrollToBottom();
                                }

                                @Override
                                public void onFailed(BmobException e) {
                                        LogUtil.e("发送失败" + e.getMessage() + e.getErrorCode());
                                        mAdapter.notifyDataSetChanged();
                                        scrollToBottom();
                                }
                        }
                );
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scrollToBottom();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                        voice.setVisibility(View.GONE);
                        send.setVisibility(View.VISIBLE);
                } else {
                        voice.setVisibility(View.VISIBLE);
                        send.setVisibility(View.GONE);
                }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public boolean onLongClick(View v) {
                return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                                if (!CommonUtils.isSupportSdcard()) {
                                        ToastUtils.showShortToast("需要SD卡支持!");
                                        return false;
                                }
                                speak.setPressed(true);
                                record_container.setVisibility(View.VISIBLE);
                                record_tip.setText(R.string.chat_middle_voice_tip);
                                mVoiceRecordManager.startRecording(uid);
                                return true;
                        case MotionEvent.ACTION_MOVE:
                                if (event.getY() < 0) {
                                        record_tip.setText(R.string.chat_middle_voice_tip1);
                                        record_tip.setTextColor(Color.RED);
                                } else {
                                        record_tip.setText(R.string.chat_middle_voice_tip);
                                        record_tip.setTextColor(Color.WHITE);
                                }
                                return true;
                        case MotionEvent.ACTION_UP:
                                speak.setPressed(false);
                                record_container.setVisibility(View.INVISIBLE);
                                if (event.getY() < 0) {
                                        mVoiceRecordManager.cancelRecord();
                                } else {
                                        int recordTime = mVoiceRecordManager.stopRecord();
                                        if (recordTime > 1) {
                                                sendVoiceMessage(mVoiceRecordManager.getVoiceFilePath(), recordTime);
                                        } else {
                                                ToastUtils.showShortToast("录制时间过短");
                                        }
                                }
                                return true;
                }
                return false;
        }

        @Override
        public void onPictureClick(final View view, final String contentUrl, int position) {
                LogUtil.e("点击了图片");
                List<BaseMessage> list = mAdapter.getData();
                List<BaseMessage> imageList = new ArrayList<>();
                for (BaseMessage baseMessage :
                        list) {
                        if (baseMessage.getMsgType().equals(Constant.TAG_MSG_TYPE_IMAGE)) {
                                imageList.add(baseMessage);
                        }
                }
                List<ImageItem> result = new ArrayList<>();
                int picturePosition = 0;
                LogUtil.e("contentUrl" + contentUrl);
                for (int i = 0; i < imageList.size(); i++) {
                        String content = imageList.get(i).getContent();
                        LogUtil.e("content" + content);
                        if (content.contains("&")) {
                                content = content.split("&")[0];
                        }
                        if (content.equals(contentUrl)) {
                                picturePosition = i;
                                LogUtil.e("相等position" + i);
                        }
                        ImageItem imageItem = new ImageItem();
                        imageItem.setPath(content);
                        result.add(imageItem);
                }
                LogUtil.e("position" + picturePosition);
                BasePreViewActivity.startBasePreview(this, result, picturePosition);
//                ImageDisplayActivity.start(ChatActivity.this, view, contentUrl);
        }

        @Override
        public void onAvatarClick(View view, int position, boolean isRight) {
                Intent intent = new Intent(this, UserInfoActivity.class);
                if (from.equals("person")) {
                        intent.putExtra("uid", isRight ? UserManager.getInstance().getCurrentUserObjectId() : user.getObjectId());
                } else {
                        GroupChatMessage message = (GroupChatMessage) mAdapter.getData(position);
                        User user = new User();
                        user.setObjectId(message.getBelongId());
                        user.setNick(message.getBelongNick());
                        user.setUsername(message.getBelongUserName());
                        user.setAvatar(message.getBelongAvatar());
                        user.setSex(false);
                        if (!isRight) {
                                intent.putExtra("uid", message.getBelongId());
                                intent.putExtra("user", user);
                        } else {
                                intent.putExtra("uid", UserManager.getInstance().getCurrentUserObjectId());
                        }
                }
                startActivity(intent);
        }

        @Override
        public void onMessageClick(View view, int position) {
                Toast.makeText(this, "1消息主体点击暂无操作", Toast.LENGTH_SHORT).show();
                BaseMessage chatMessage = mAdapter.getData(position);
                if (chatMessage != null && chatMessage.getMsgType().equals(Constant.TAG_MSG_TYPE_LOCATION)) {
//                        这里点击了地址显示图片，进入MapActivity
                        Intent intent = new Intent(this, MapActivity.class);
                        String[] content = chatMessage.getContent().split(",");
                        for (int i = 0; i < content.length; i++) {
                                LogUtil.e("点击地图的内容:" + content[i]);
                        }

                        String latitude = null;
                        String longitude = null;
                        String address = null;
                        if (content.length == 5) {
                                latitude = content[2];
                                longitude = content[3];
                                address = content[4];
                        } else if (content.length == 4) {
                                latitude = content[1];
                                longitude = content[2];
                                address = content[3];
                        }
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude", longitude);
                        intent.putExtra("address", address);
                        intent.putExtra("destination", "browse");
                        startActivity(intent);
                }
        }

        @Override
        public void onItemResendClick(View view, final BaseMessage baseMessage, int position) {
//                重发消息
                Integer msgType = baseMessage.getMsgType();
                String id;
                final MsgManager msgManager = MsgManager.getInstance();
                if (msgType.equals(Constant.TAG_MSG_TYPE_TEXT)) {
                        msgManager.resendTextChatMessage(baseMessage,
                                new OnSendMessageListener() {

                                        @Override
                                        public void onSending() {
                                                mAdapter.notifyDataSetChanged();
                                                LogUtil.e("发送中.........");
                                        }

                                        @Override
                                        public void onSuccess() {
                                                baseMessage.setSendStatus(Constant.SEND_STATUS_SUCCESS);
                                                mAdapter.notifyDataSetChanged();
                                                LogUtil.e("重发消息成功");
                                        }

                                        @Override
                                        public void onFailed(BmobException e) {
                                                mAdapter.notifyDataSetChanged();
                                                LogUtil.e("重发消息失败" + e.getMessage() + e.getErrorCode());
                                        }
                                });
                } else if (msgType.equals(Constant.TAG_MSG_TYPE_IMAGE)) {

                        msgManager.resendImageChatMessage(baseMessage, this);

                } else if (msgType.equals(Constant.TAG_MSG_TYPE_VOICE)) {

                        msgManager.resendVoiceChatMessage(baseMessage, this);

                } else if (msgType.equals(Constant.TAG_MSG_TYPE_LOCATION)) {

                        msgManager.resendLocationChatMessage(baseMessage, this);
                } else {
//                        未知类型
                }
        }


        @Override
        protected void onDestroy() {
                if (mReceiver != null) {
                        unregisterReceiver(mReceiver);
                        mReceiver = null;
                }
                PushMessageReceiver.unRegisterListener(this);
                mVoiceRecordManager.setOnVoiceChangeListener(null);
                RxBusManager.getInstance().unSubscrible(this);
                super.onDestroy();
        }

        @Override
        public void onNewChatMessageCome(ChatMessage message) {
                LogUtil.e("聊天界面");
//                在聊天界面接收到消息直接更新为已读状态
                ChatDB.create().updateChatMessageReadStatus(message.getConversationId(), message.getCreateTime()
                        , true);
                mAdapter.addData(message);
                scrollToBottom();
        }

        @Override
        public void onNewGroupChatMessageCome(GroupChatMessage message) {

                ChatDB.create().updateReceivedGroupChatMessageReaded(groupId, true);
                mAdapter.addData(message);
                scrollToBottom();
        }


        private volatile boolean hasFocus = true;

        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
                this.hasFocus = hasFocus;
        }

        @Override
        public void onAskReadMessageCome(ChatMessage chatMessage) {
                LogUtil.e("请求读取的消息来了没有12");

                if (from.equals("person")) {
                        List<BaseMessage> list = mAdapter.getData();
                        for (BaseMessage message :
                                list) {
                                if (((ChatMessage) message).getConversationId().equals(chatMessage.getConversationId()) && message.getCreateTime().equals(chatMessage.getCreateTime())) {
                                        LogUtil.e("找到要求读取的消息拉");
                                        message.setReadStatus(Constant.READ_STATUS_READED);
                                        break;
                                }
                        }
                }
                mAdapter.notifyDataSetChanged();
//                scrollToBottom();
        }

        @Override
        public void onNetWorkChanged(boolean isConnected) {
                if (!isConnected) {
                        Toast.makeText(this, "网络连接失败", Toast.LENGTH_SHORT).show();
                }
        }

        @Override
        public void onAddFriendMessageCome(ChatMessage chatMessage) {
//                不处理

        }

        @Override
        public void onAgreeMessageCome(ChatMessage chatMessage) {
                LogUtil.e("邀请消息到chatActivity");
                mAdapter.addData(chatMessage);
                scrollToBottom();
        }

        @Override
        public void onOffline() {
                Toast.makeText(this, "你的帐号已在其他地方登录，所以被迫下线", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
        }

        @Override
        public void onGroupTableMessageCome(GroupTableMessage message) {

        }

//        重新发的回调

        @Override
        public void onProgress(int progress) {
        }
//        重新发的回调

        @Override
        public void onStart(BaseMessage message) {
                mAdapter.notifyDataSetChanged();

        }
//        重新发的回调

        @Override
        public void onSuccess() {
                mAdapter.notifyDataSetChanged();
        }
//        重新发的回调

        @Override
        public void onFailed(BmobException e) {
                mAdapter.notifyDataSetChanged();
        }

        private class GridViewAdapter extends BaseAdapter {
                private List<FaceText> mFaceTextList = new ArrayList<>();
                private Context mContext;

                GridViewAdapter(Context context, List<FaceText> faceTextList) {
                        this.mContext = context;
                        if (faceTextList != null && faceTextList.size() > 0) {
                                mFaceTextList.clear();
                                mFaceTextList.addAll(faceTextList);
                        }
                }

                @Override
                public int getCount() {
                        return mFaceTextList.size();
                }

                @Override
                public Object getItem(int position) {
                        return mFaceTextList.get(position);
                }

                @Override
                public long getItemId(int position) {
                        return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                        ViewHolder viewHolder;
                        if (convertView == null) {
                                viewHolder = new ViewHolder();
                                convertView = LayoutInflater.from(mContext).inflate(R.layout.emtion_item, parent, false);
                                viewHolder.display = (ImageView) convertView.findViewById(R.id.iv_emotion_item_display);
                                convertView.setTag(viewHolder);
                        } else {
                                viewHolder = (ViewHolder) convertView.getTag();
                        }
                        FaceText item = mFaceTextList.get(position);
                        viewHolder.display.setImageDrawable(mContext.getResources().getDrawable(mContext.getResources().getIdentifier(item.getText().substring(1), "mipmap", mContext.getPackageName())));
                        return convertView;
                }
        }

        private class ViewHolder {
                ImageView display;
        }

        private class MyViewPagerAdapter extends PagerAdapter {
                private List<View> mViews = new ArrayList<>();

                private MyViewPagerAdapter(List<View> list) {
                        if (list != null && list.size() > 0) {
                                mViews.clear();
                                mViews.addAll(list);
                        }
                }

                @Override
                public int getCount() {
                        return mViews.size();
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                        return view == object;
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                        container.addView(mViews.get(position));
                        return mViews.get(position);
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                        container.removeView(mViews.get(position));
                }
        }


}
