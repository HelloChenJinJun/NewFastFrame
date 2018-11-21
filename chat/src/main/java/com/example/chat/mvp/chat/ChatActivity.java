package com.example.chat.mvp.chat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.R;
import com.example.chat.adapter.ChatMessageAdapter;
import com.example.chat.adapter.EmotionViewAdapter;
import com.example.chat.base.Constant;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.FaceText;
import com.example.chat.bean.GroupChatMessage;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.bean.ImageItem;
import com.example.chat.bean.MessageContent;
import com.example.chat.dagger.chat.ChatActivityModule;
import com.example.chat.dagger.chat.DaggerChatActivityComponent;
import com.example.chat.events.MessageInfoEvent;
import com.example.chat.events.NetStatusEvent;
import com.example.chat.events.RecentEvent;
import com.example.chat.events.RefreshMenuEvent;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.manager.VoiceRecordManager;
import com.example.chat.mvp.UserInfoTask.UserInfoActivity;
import com.example.chat.mvp.group.groupInfo.GroupInfoActivity;
import com.example.chat.mvp.map.MapActivity;
import com.example.chat.mvp.photoSelect.PhotoSelectActivity;
import com.example.chat.mvp.preview.PhotoPreViewActivity;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.FaceTextUtil;
import com.example.chat.util.FileUtil;
import com.example.chat.util.LogUtil;
import com.example.chat.util.SoftHideBoardUtil;
import com.example.chat.util.SystemUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.adapter.CommonPagerAdapter;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.bean.chat.GroupTableEntity;
import com.example.commonlibrary.bean.chat.UserEntity;
import com.example.commonlibrary.cusotomview.BaseDialog;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.imageloader.glide.GlideImageLoaderConfig;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.commonlibrary.utils.PermissionPageUtils;
import com.example.commonlibrary.utils.PermissionUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/13      21:53
 * QQ:             1981367757
 */
public class ChatActivity extends SlideBaseActivity<BaseMessage, ChatPresenter> implements View.OnClickListener, TextWatcher, View.OnLongClickListener, View.OnTouchListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SuperRecyclerView display;
    private EditText input;
    private Button speak;
    private Button voice;
    private Button send;
    private Button keyboard;
    private ViewPager mViewPager;
    private LinearLayout l1_more;
    private RelativeLayout r1_emotion;
    private LinearLayout l1_add;
    private RelativeLayout record_container;
    private ImageView record_display;
    private TextView record_tip;
    private WrappedLinearLayoutManager mLinearLayoutManager;
    private ChatMessageAdapter mAdapter;
    private List<FaceText> emotionFaceList;
    private VoiceRecordManager mVoiceRecordManager;
    private String uid = null;
    private int[] voiceImageId;
    private UserEntity userEntity;
    private String from;
    private String groupId;
    private GroupTableEntity groupTableEntity;
    private String videoPath;


    @Override
    public void initView() {
        initMiddleView();
        initBottomView();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SoftHideBoardUtil.assistActivity(this, isHide -> {
            if (!isHide) {
                scrollToBottom();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        from = getIntent().getStringExtra(Constant.FROM);
        if (from.equals(Constant.TYPE_PERSON)) {
            uid = getIntent().getStringExtra(Constant.ID);
            userEntity = UserDBManager.getInstance().getUser(uid);
        } else if (from.equals(Constant.TYPE_GROUP)) {
            groupId = getIntent().getStringExtra(Constant.ID);
            groupTableEntity = UserDBManager.getInstance().getGroupTableEntity(groupId);
        }
        initActionBar();
        refreshData();
        scrollToBottom();
    }

    private void initMiddleView() {
        record_container = (RelativeLayout) findViewById(R.id.r1_chat_middle_voice_container);
        record_display = (ImageView) findViewById(R.id.iv_chat_middle_voice_display);
        record_tip = (TextView) findViewById(R.id.tv_chat_middle_voice_tip);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swl_chat_refresh);
        display = (SuperRecyclerView) findViewById(R.id.srcv_chat_display);
    }


    private void initBottomView() {
        Button add = (Button) findViewById(R.id.btn_chat_bottom_add);
        Button emotion = (Button) findViewById(R.id.btn_chat_bottom_emotion);
        input = (EditText) findViewById(R.id.et_chat_bottom_input);
        speak = (Button) findViewById(R.id.btn_chat_bottom_speak);
        send = (Button) findViewById(R.id.btn_chat_bottom_send);
        voice = (Button) findViewById(R.id.btn_chat_bottom_voice);
        keyboard = (Button) findViewById(R.id.btn_chat_bottom_keyboard);
        l1_more = (LinearLayout) findViewById(R.id.l1_chat_bottom_more);
        mViewPager = (ViewPager) findViewById(R.id.vp_chat_bottom_emotion);
        r1_emotion = (RelativeLayout) findViewById(R.id.r1_chat_bottom_emotion);
        l1_add = (LinearLayout) findViewById(R.id.l1_chat_bottom_add);
        TextView picture = (TextView) findViewById(R.id.tv_chat_bottom_picture);
        TextView video = (TextView) findViewById(R.id.tv_chat_bottom_video);
        TextView location = (TextView) findViewById(R.id.tv_chat_bottom_location);
        add.setOnClickListener(this);
        emotion.setOnClickListener(this);
        input.addTextChangedListener(this);
        input.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                scrollToBottom();
                if (l1_more.getVisibility() == View.VISIBLE) {
                    l1_more.setVisibility(View.GONE);
                }
            }
        });
        voice.setOnClickListener(this);
        send.setOnClickListener(this);
        keyboard.setOnClickListener(this);
        location.setOnClickListener(this);
        picture.setOnClickListener(this);
        video.setOnClickListener(this);
        speak.setOnTouchListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            BaseMessage message = mAdapter.getData(0);
            LoadMessage(message);
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }


    @Override
    public void initData() {
        DaggerChatActivityComponent.builder()
                .chatActivityModule(new ChatActivityModule(this))
                .chatMainComponent(getChatMainComponent())
                .build().inject(this);
        from = getIntent().getStringExtra(Constant.FROM);
        if (from.equals(Constant.TYPE_PERSON)) {
            uid = getIntent().getStringExtra(Constant.ID);
            userEntity = UserDBManager.getInstance().getUser(uid);
        } else if (from.equals(Constant.TYPE_GROUP)) {
            groupId = getIntent().getStringExtra(Constant.ID);
            groupTableEntity = UserDBManager.getInstance().getGroupTableEntity(groupId);

        }
        initActionBar();
//                 声音音量变化图片资源
        voiceImageId = new int[]{R.mipmap.chat_icon_voice1, R.mipmap.chat_icon_voice2, R.mipmap.chat_icon_voice3, R.mipmap.chat_icon_voice4,
                R.mipmap.chat_icon_voice5, R.mipmap.chat_icon_voice6};
        initRecordManager();
        initEmotionInfo();
        mLinearLayoutManager = new WrappedLinearLayoutManager(this);
        display.setLayoutManager(mLinearLayoutManager);
        mAdapter = new ChatMessageAdapter();
        mAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {

            }

            public void onItemChildClick(int position, View view, int id) {
                BaseMessage baseMessage = mAdapter.getData(position);
                if (id == R.id.iv_item_activity_chat_send_retry
                        ) {
                    reSendMessage(baseMessage);
                } else if (id == R.id.iv_item_activity_chat_send_avatar) {
                    UserInfoActivity.start(ChatActivity.this, UserManager.getInstance()
                            .getCurrentUserObjectId());
                } else if (id == R.id.iv_item_activity_chat_receive_avatar) {
                    UserInfoActivity.start(ChatActivity.this, baseMessage.getBelongId());
                } else if (id == R.id.rl_chat_send_location_item_content
                        || id == R.id.rl_chat_receive_location_item_content) {
                    MessageContent messageContent = gson.fromJson(baseMessage.getContent(), MessageContent.class);
                    MapActivity.start(ChatActivity.this, true
                            , messageContent.getLongitude()
                            , messageContent.getLatitude()
                            , messageContent.getAddress(), Constant.REQUEST_MAP);
                } else if (id == R.id.iv_item_activity_chat_send_image
                        || id == R.id.iv_item_activity_chat_receive_image) {
                    ArrayList<ImageItem> list = new ArrayList<>();
                    int temp = 0;
                    int currentPosition = 0;
                    for (int i = 0; i < mAdapter.getData().size(); i++) {
                        BaseMessage item = mAdapter.getData(i);
                        if (item.getContentType().equals(Constant.TAG_MSG_TYPE_IMAGE)) {
                            MessageContent messageContent = gson.fromJson(item.getContent(), MessageContent
                                    .class);
                            if (item.equals(baseMessage)) {
                                currentPosition = temp;
                            }
                            ImageItem imageItem = new ImageItem();
                            imageItem.setPath(messageContent.getUrlList().get(0));
                            list.add(imageItem);
                            temp++;
                        }
                    }
                    PhotoPreViewActivity.start(ChatActivity.this, currentPosition, list, false);
                }
            }
        });

        display.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                JZVideoPlayer jzvd = view.findViewById(R.id.js_item_activity_chat_receive_video);
                if (jzvd == null) {
                    jzvd = view.findViewById(R.id.js_item_activity_chat_send_video);
                }
                if (jzvd != null && JZUtils.dataSourceObjectsContainsUri(jzvd.dataSourceObjects, JZMediaManager.getCurrentDataSource())) {
                    JZVideoPlayer.releaseAllVideos();
                }
            }
        });


        display.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    CommonUtils.hideSoftInput(ChatActivity.this, input);
                }
            }
        });
        display.setAdapter(mAdapter);
        refreshData();
        scrollToBottom();
        registerRxBus();
    }

    private void reSendMessage(BaseMessage data) {
        data.setSendStatus(Constant.SEND_STATUS_SENDING);
        mAdapter.addData(data);
        if (data instanceof ChatMessage) {
            presenter.sendChatMessage(((ChatMessage) data));
        } else {
            presenter.sendGroupChatMessage(((GroupChatMessage) data));
        }
    }


    private void registerRxBus() {
        addDisposable(RxBusManager.getInstance().registerEvent(MessageInfoEvent.class, messageInfoEvent -> {
            if (messageInfoEvent.getMessageType() == MessageInfoEvent.TYPE_PERSON) {
                onProcessNewMessages(messageInfoEvent.getChatMessageList());
            } else if (messageInfoEvent.getMessageType() == MessageInfoEvent.TYPE_GROUP_CHAT) {
                for (GroupChatMessage message : messageInfoEvent.getGroupChatMessageList()
                        ) {
                    onProcessGroupChatMessage(message);
                }
            } else if (messageInfoEvent.getMessageType() == MessageInfoEvent.TYPE_GROUP_TABLE) {
                for (GroupTableMessage message : messageInfoEvent.getGroupTableMessageList()
                        ) {
//                    onProcessGroupTableMessage(message);
                }
            }
        }));

        presenter.registerEvent(NetStatusEvent.class, netStatusEvent -> {
            if (netStatusEvent.isConnected()) {
                ToastUtils.showShortToast("断线重连.......");
                for (BaseMessage message :
                        mAdapter.getData()) {
                    if (message.getSendStatus().equals(Constant.SEND_STATUS_FAILED)
                            && message.getBelongId().equals(UserManager.getInstance()
                            .getCurrentUserObjectId())) {
                        reSendMessage(message);
                    }
                }
            }
        });


        if (from.equals(Constant.TYPE_GROUP)) {
//            addDisposable(RxBusManager.getInstance().registerEvent(GroupInfoEvent.class, groupInfoEvent -> {
//                //                                刷新过来的，更新下群结构消息
//                groupTableEntity = UserDBManager.getInstance().getGroupTableEntity(groupId);
//                int type = groupInfoEvent.getType();
//                String content = groupInfoEvent.getContent();
//                switch (type) {
//                    case GroupInfoEvent.TYPE_GROUP_NAME:
//                        String title = groupTableEntity.getGroupName() + "(" + groupTableEntity.getGroupNumber().size() + ")";
//                        updateTitle(title);
//                        break;
//                    case GroupInfoEvent.TYPE_GROUP_NOTIFICATION:
//                        LogUtil.e("这里要做群通知的界面展示" + content);
//                        break;
//                    case GroupInfoEvent.TYPE_GROUP_DESCRIPTION:
//                        LogUtil.e("这里要做群描述的界面展示" + content);
//                        break;
//                    case GroupInfoEvent.TYPE_GROUP_AVATAR:
//                        LogUtil.e("这里要做群头像的界面展示" + content);
//                        BaseApplication
//                                .getAppComponent()
//                                .getImageLoader().loadImage(ChatActivity.this,new GlideImageLoaderConfig
//                                .Builder().url(content).imageView(getIcon()).build());
//                        break;
//                    case GroupInfoEvent.TYPE_GROUP_NUMBER:
//                        if (groupId != null) {
//                            LogUtil.e("这里通知成员的变化" + content);
//                            if (content.equals(groupId)) {
//                                Toast.makeText(ChatActivity.this, "你已经被提出该群", Toast.LENGTH_SHORT).show();
//                                finish();
//                            }
//                        }
//                        break;
//                    default:
//                        break;
//                }
//            }));
        }
    }


    private void onProcessGroupChatMessage(GroupChatMessage message) {
        if (message.getGroupId().equals(groupId)) {
            UserDBManager.getInstance()
                    .updateGroupChatReadStatus(groupId, message.getBelongId()
                            , message.getCreateTime(), Constant.READ_STATUS_READED);
            mAdapter.addData(message);
            scrollToBottom();
        }
    }

    private void onProcessNewMessages(List<ChatMessage> chatMessageList) {
        for (ChatMessage chatMessage :
                chatMessageList) {
            if (!uid.equals(chatMessage.getBelongId())) continue;
            int messageType = chatMessage.getMessageType();
            switch (messageType) {
                case ChatMessage.MESSAGE_TYPE_NORMAL:
                    //                在聊天界面接收到消息直接更新为已读状态
                    UserDBManager.getInstance().updateMessageReadStatus(chatMessage
                                    .getConversationId(), chatMessage.getCreateTime()
                            , Constant.READ_STATUS_READED);
                    mAdapter.addData(chatMessage);
                    scrollToBottom();
                    break;
                case ChatMessage.MESSAGE_TYPE_READED:
//                    更新状态
                    int size=mAdapter.getData().size();
                    for (int i = 0; i < size; i++) {
                        BaseMessage item=mAdapter.getData(i);
                        if (chatMessage.equals(item)) {
                            item.setReadStatus(Constant.READ_STATUS_READED);
                            mAdapter.notifyItemChanged(mAdapter.getItemUpCount()+i);
                            break;
                        }
                    }
                    break;
            }
        }
    }

    private void initActionBar() {
        String title;
        String avatar;
        if (from.equals(Constant.TYPE_PERSON)) {
            title = userEntity.getName();
            avatar = null;
        } else {
            title = groupTableEntity.getGroupName() + "(" + groupTableEntity.getGroupNumber().size() + ")";
            avatar = groupTableEntity.getGroupAvatar();
        }
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setAvatar(avatar);
        toolBarOption.setTitle(title);
        if (from.equals(Constant.TYPE_GROUP)) {
            toolBarOption.setRightText("信息");
            toolBarOption.setRightListener(v ->
                    GroupInfoActivity.start(ChatActivity.this, groupId));
        }
        setToolBar(toolBarOption);
    }


    private void refreshData() {
        if (from.equals(Constant.TYPE_PERSON)) {
            if (UserDBManager.getInstance().updateMessageReadStatusForUser(userEntity.getUid(), Constant.READ_STATUS_READED)) {
                RxBusManager.getInstance().post(new RecentEvent(uid, RecentEvent.ACTION_ADD));
                RxBusManager.getInstance().post(new RefreshMenuEvent(0));
            }
            mAdapter.refreshData(UserDBManager.getInstance().getAllChatMessageById(uid, 0L));
        } else {
            if (UserDBManager.getInstance().updateGroupChatReadStatus(groupId, Constant.READ_STATUS_READED)) {
                RxBusManager.getInstance().post(new RecentEvent(groupId, RecentEvent.ACTION_ADD));
                RxBusManager.getInstance().post(new RefreshMenuEvent(0));
            }
            mAdapter.refreshData(UserDBManager.getInstance().getAllGroupChatMessageById(groupId, 0L));
        }
    }


    @Override
    public void updateData(BaseMessage baseMessage) {
        mAdapter.addData(baseMessage);

    }

    public static void start(Activity activity, String from, String id) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra(Constant.FROM, from);
        intent.putExtra(Constant.ID, id);
        activity.startActivity(intent);
    }


    private void initEmotionInfo() {
        List<View> list = new ArrayList<>();
        emotionFaceList = FaceTextUtil.getFaceTextList();
        for (int i = 0; i < 2; i++) {
            list.add(getGridView(i));
        }
        mViewPager.setAdapter(new CommonPagerAdapter(list));
    }

    private View getGridView(int i) {
        View emotionView = LayoutInflater.from(this).inflate(R.layout.view_activity_chat_emotion, null);
        SuperRecyclerView superRecyclerView = emotionView.findViewById(R.id.srcv_view_activity_chat_emotion_display);
        superRecyclerView.setLayoutManager(new WrappedGridLayoutManager(this, 7));
        EmotionViewAdapter emotionViewAdapter = new EmotionViewAdapter();
        superRecyclerView.setAdapter(emotionViewAdapter);
        emotionViewAdapter.addData(i == 0 ? emotionFaceList.subList(0, 21) : emotionFaceList.subList(21, emotionFaceList.size()));
        emotionViewAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                FaceText faceText = emotionViewAdapter.getData(position);
                String content = faceText.getText();
                if (input != null) {
                    int startIndex = input.getSelectionStart();
                    CharSequence content1 = input.getText().insert(startIndex, content);
                    input.setText(FaceTextUtil.toSpannableString(ChatActivity.this.getApplicationContext(), content1.toString()));
//                                        重新定位光标位置
                    CharSequence info = input.getText();
                    if (info != null) {
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
                    MessageContent messageContent = new MessageContent();
                    List<String> urlList = new ArrayList<>();
                    urlList.add(localVoicePath);
                    messageContent.setUrlList(urlList);
                    messageContent.setVoiceTime(time);
                    sendMessage(messageContent, Constant.TAG_MSG_TYPE_VOICE);
                    Flowable.timer(1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> speak.setClickable(true));
                }
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
        if (from.equals(Constant.TYPE_PERSON)) {
            if (message != null) {
                list.addAll(UserDBManager.getInstance().getAllChatMessageById(uid, message.getCreateTime()));
            } else {
                list.addAll(UserDBManager.getInstance().getAllChatMessageById(uid, 0L));
            }
        } else {
            if (message != null) {
                list.addAll(UserDBManager.getInstance().getAllGroupChatMessageById(groupId, message.getCreateTime()));
            } else {
                list.addAll(UserDBManager.getInstance().getAllGroupChatMessageById(groupId, 0));
            }
        }
        int size = list.size();
        LogUtil.e("向上拉取的消息大小为" + size);
        mAdapter.addData(0, list);
        mLinearLayoutManager.scrollToPositionWithOffset(size, 0);
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


    private void scrollToBottom() {
        mLinearLayoutManager.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, 0);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_chat_bottom_add) {
            if (l1_more.getVisibility() == View.GONE) {
                l1_more.setVisibility(View.VISIBLE);
                l1_add.setVisibility(View.VISIBLE);
                r1_emotion.setVisibility(View.GONE);
                CommonUtils.hideSoftInput(this, input);
            } else if (l1_add.getVisibility() == View.VISIBLE) {
                l1_more.setVisibility(View.GONE);
            } else {
                r1_emotion.setVisibility(View.GONE);
                l1_add.setVisibility(View.VISIBLE);
            }

        } else if (i == R.id.btn_chat_bottom_emotion) {
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
                CommonUtils.hideSoftInput(this, input);
            } else if (r1_emotion.getVisibility() == View.VISIBLE) {
                l1_more.setVisibility(View.GONE);
            } else {
                l1_add.setVisibility(View.GONE);
                r1_emotion.setVisibility(View.VISIBLE);
                CommonUtils.hideSoftInput(this, input);
            }

        } else if (i == R.id.btn_chat_bottom_voice) {
            if (l1_more.getVisibility() == View.VISIBLE) {
                l1_more.setVisibility(View.GONE);
            }
            keyboard.setVisibility(View.VISIBLE);
            speak.setVisibility(View.VISIBLE);
            input.setVisibility(View.GONE);
            voice.setVisibility(View.GONE);
            CommonUtils.hideSoftInput(this, input);

        } else if (i == R.id.btn_chat_bottom_keyboard) {
            keyboard.setVisibility(View.GONE);
            voice.setVisibility(View.VISIBLE);
            speak.setVisibility(View.GONE);
            input.setVisibility(View.VISIBLE);

        } else if (i == R.id.btn_chat_bottom_send) {
            if (l1_more.getVisibility() == View.VISIBLE) {
                l1_more.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(input.getText().toString().trim())) {
                ToastUtils.showShortToast(getString(R.string.chat_input_empty));
                input.setText("");
                return;
            }
            MessageContent messageContent = new MessageContent();
            messageContent.setContent(input.getText().toString().trim());
            sendMessage(messageContent, Constant.TAG_MSG_TYPE_TEXT);
        } else if (i == R.id.tv_chat_bottom_picture) {
            PhotoSelectActivity.start(this, null, true, false, null);
        } else if (i == R.id.tv_chat_bottom_location) {
            MapActivity.start(this, false, null, null, null, Constant.REQUEST_MAP);
        } else if (i == R.id.tv_chat_bottom_video) {
            videoPath = SystemUtil.recorderVideo(this, SystemUtil.REQUEST_CODE_VIDEO_RECORDER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ConstantUtil.REQUEST_CODE_ONE_PHOTO:
                    if (data != null) {
                        String path = data.getStringExtra(ConstantUtil.PATH);
                        MessageContent messageContent = new MessageContent();
                        List<String> urlList = new ArrayList<>();
                        urlList.add(path);
                        messageContent.setUrlList(urlList);
                        sendMessage(messageContent, Constant.TAG_MSG_TYPE_IMAGE);
                    }
                    break;
                case SystemUtil.REQUEST_CODE_VIDEO_RECORDER:
                    Bitmap bitmap = SystemUtil.getVideoThumbnail(videoPath, DensityUtil.getScreenWidth(this), DensityUtil.getScreenHeight(this)
                            , MediaStore.Images.Thumbnails.MINI_KIND);
                    String thumbImage = SystemUtil.bitmapToFile(bitmap);
                    MessageContent messageContent = new MessageContent();
                    List<String> urlList = new ArrayList<>();
                    urlList.add(thumbImage);
                    urlList.add(videoPath);
                    messageContent.setUrlList(urlList);
                    sendMessage(messageContent, Constant.TAG_MSG_TYPE_VIDEO);
                    break;
                case Constant.REQUEST_MAP:
                    if (data != null) {
                        String localPath = data.getStringExtra(ConstantUtil.PATH);
                        String longitude = data.getStringExtra(Constant.LONGITUDE);
                        String latitude = data.getStringExtra(Constant.LATITUDE);
                        String address = data.getStringExtra(Constant.ADDRESS);
                        MessageContent messageContent1 = new MessageContent();
                        List<String> urlList1 = new ArrayList<>();
                        urlList1.add(localPath);
                        messageContent1.setUrlList(urlList1);
                        messageContent1.setLongitude(longitude);
                        messageContent1.setLatitude(latitude);
                        messageContent1.setAddress(address);
                        sendMessage(messageContent1, Constant.TAG_MSG_TYPE_LOCATION);
                    }
                    break;
            }
        }
    }

    private Gson gson = BaseApplication.getAppComponent().getGson();

    private void sendMessage(MessageContent messageContent, Integer contentType) {
        BaseMessage baseMessage;
        if (from.equals(Constant.TYPE_PERSON)) {
            baseMessage = MsgManager.getInstance()
                    .createChatMessage(gson.toJson(messageContent), uid, contentType);
            baseMessage.setSendStatus(Constant.SEND_STATUS_SENDING);
            mAdapter.addData(baseMessage);
            presenter.sendChatMessage(((ChatMessage) baseMessage));
        } else {
            baseMessage = MsgManager.getInstance()
                    .createGroupChatMessage(gson.toJson(messageContent), groupId, contentType);
            baseMessage.setSendStatus(Constant.SEND_STATUS_SENDING);
            mAdapter.addData(baseMessage);
            presenter.sendGroupChatMessage(((GroupChatMessage) baseMessage));
        }
        input.setText("");
        CommonUtils.hideSoftInput(this, input);
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
                if (!FileUtil.isExistSDCard()) {
                    ToastUtils.showShortToast("需要SD卡支持!");
                    return false;
                }
                PermissionUtil.requestPermission(new PermissionUtil.RequestPermissionCallBack() {
                    @Override
                    public void onRequestPermissionSuccess() {
                        speak.setPressed(true);
                        record_container.setVisibility(View.VISIBLE);
                        record_tip.setText(R.string.chat_middle_voice_tip);
                        mVoiceRecordManager.startRecording(uid);
                    }

                    @Override
                    public void onRequestPermissionFailure() {
                        ToastUtils.showShortToast("需要授予录音权限才能录音");

                        showBaseDialog("权限界面操作", "是否需要打开权限界面", "取消", "确定"
                                , v12 -> cancelBaseDialog(), v1 -> {
                                    dismissBaseDialog();
                                   PermissionPageUtils.jumpPermissionPage(ChatActivity.this);
                                });
                    }
                },new RxPermissions(this), Manifest.permission.RECORD_AUDIO,Manifest.permission
                .WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return !(checkSelfPermission(Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_DENIED);
                }else {
                    return true;
                }
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
                        MessageContent messageContent = new MessageContent();
                        List<String> urlList = new ArrayList<>();
                        urlList.add(mVoiceRecordManager.getVoiceFilePath());
                        messageContent.setUrlList(urlList);
                        messageContent.setVoiceTime(recordTime);
                        sendMessage(messageContent, Constant.TAG_MSG_TYPE_VOICE);
                    } else {
                        ToastUtils.showShortToast("录制时间过短");
                    }
                }
                return true;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        mVoiceRecordManager.setOnVoiceChangeListener(null);
        super.onDestroy();
    }
}
