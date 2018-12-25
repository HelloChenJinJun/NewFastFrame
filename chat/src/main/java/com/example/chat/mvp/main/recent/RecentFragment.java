package com.example.chat.mvp.main.recent;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.adapter.RecentListAdapter;
import com.example.chat.base.AppBaseFragment;
import com.example.chat.base.ConstantUtil;
import com.example.chat.bean.ChatMessage;
import com.example.chat.events.DragLayoutEvent;
import com.example.chat.events.MessageInfoEvent;
import com.example.chat.events.RecentEvent;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.chat.ChatActivity;
import com.example.chat.service.PollService;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.bean.chat.RecentMessageEntity;
import com.example.commonlibrary.bean.chat.SkinEntity;
import com.example.commonlibrary.baseadapter.decoration.ListViewDecoration;
import com.example.commonlibrary.customview.ToolBarOption;
import com.example.commonlibrary.customview.swipe.CustomSwipeRefreshLayout;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.rxbus.event.NetStatusEvent;
import com.example.commonlibrary.skin.SkinManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.List;

import androidx.appcompat.widget.PopupMenu;


/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/13      12:18
 * QQ:             1981367757
 */


/**
 * 最近会话列表fragment
 */
public class RecentFragment extends AppBaseFragment implements CustomSwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private RecentListAdapter mAdapter;
    private CustomSwipeRefreshLayout mSwipeRefreshLayout;
    private WrappedLinearLayoutManager mLinearLayoutManager;
    private SuperRecyclerView display;
    private TextView net;

    public static RecentFragment newInstance() {
        return new RecentFragment();
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
    protected boolean needStatusPadding() {
        return true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_recent;
    }

    @Override
    public void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_recent_display);
        net = (TextView) findViewById(R.id.tv_fragment_recent_net);
        net.setOnClickListener(this);
        mSwipeRefreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.refresh_recent_container);
        display.setLayoutManager(mLinearLayoutManager = new WrappedLinearLayoutManager(getActivity()));
        display.addItemDecoration(new ListViewDecoration());
        mSwipeRefreshLayout.setOnRefreshListener(this);
        display.setAdapter(mAdapter = new RecentListAdapter());
        mAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                RecentMessageEntity msg;
                msg = mAdapter.getData(position);
                if (msg.getType() == RecentMessageEntity.TYPE_GROUP) {
                    ChatActivity.start(getActivity(), ConstantUtil.TYPE_GROUP, msg.getId());
                } else {
                    ChatActivity.start(getActivity(), ConstantUtil.TYPE_PERSON, msg.getId());
                }
            }


            @Override
            public boolean onItemLongClick(int position, View view) {
                showPopMenu(view, position);
                return true;
            }
        });
    }


    private void onProcessNewMessages(List<ChatMessage> list) {
        for (ChatMessage chatMessage :
                list) {
            int messageType = chatMessage.getMessageType();
            switch (messageType) {
                case ChatMessage.MESSAGE_TYPE_ADD:
                    break;
                case ChatMessage.MESSAGE_TYPE_AGREE:
                    RxBusManager.getInstance().post(new RecentEvent(chatMessage.getBelongId(), RecentEvent.ACTION_ADD));
                    break;
                case ChatMessage.MESSAGE_TYPE_READED:
                    LogUtil.e("接收到的回执已读标签消息");
                    break;
                default:
                    RxBusManager.getInstance().post(new RecentEvent(chatMessage.getBelongId(), RecentEvent.ACTION_ADD));
                    break;
            }
        }
    }


    public void notifyNewIntentCome(Intent intent) {
        String from = intent.getStringExtra(ConstantUtil.NOTIFICATION_TAG);
        if (from == null) {
            ToastUtils.showShortToast("系统通知");
            return;
        }
        if (from.equals(ConstantUtil.NOTIFICATION_TAG_GROUP_MESSAGE)) {
            Intent chat = new Intent(getActivity(), ChatActivity.class);
            chat.putExtra(ConstantUtil.ID, getActivity().getIntent().getStringExtra(ConstantUtil.GROUP_ID));
            startActivity(chat);
        }
    }


    public void showPopMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.item_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_recent_delete) {
                RecentMessageEntity msg = mAdapter.getData(position);
                UserDBManager.getInstance().deleteRecentMessage(msg.getId());
                mAdapter.removeData(msg);
            } else {
                ToastUtils.showShortToast("置顶");
            }
            return true;
        });
        popupMenu.setGravity(Gravity.END);
        popupMenu.show();
    }


    @Override
    public void initData() {
        if (mLinearLayoutManager.findViewByPosition(0) != null) {
            mLinearLayoutManager.findViewByPosition(0).setVisibility(View.GONE);
        }
        registerRxBus();
        initSkin();
        initToolBarData();

        getIcon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBusManager.getInstance().post(new DragLayoutEvent());
            }
        });
    }

    private void initToolBarData() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("聊天");
        toolBarOption.setAvatar(UserManager.getInstance().getCurrentUser().getAvatar());
        toolBarOption.setNeedNavigation(false);
        setToolBar(toolBarOption);
    }

    private void initSkin() {
        SkinEntity currentSkinEntity = UserDBManager
                .getInstance().getCurrentSkin();
        if (currentSkinEntity != null) {
            SkinManager.getInstance().update(currentSkinEntity.getPath());
        }
    }


    private void registerRxBus() {

        addDisposable(RxBusManager.getInstance().registerEvent(RecentEvent.class, recentEvent -> {
            RecentMessageEntity recentMessageEntity = UserDBManager
                    .getInstance().getRecentMessage(recentEvent.getId());
            if (recentEvent.getAction() == RecentEvent.ACTION_ADD) {
                mAdapter.addData(0, recentMessageEntity);
            } else {
                mAdapter.removeData(recentMessageEntity);
            }
        }));
        addDisposable(RxBusManager.getInstance().registerEvent(MessageInfoEvent.class, messageInfoEvent -> {
            if (messageInfoEvent.getMessageType() == MessageInfoEvent.TYPE_PERSON) {
                onProcessNewMessages(messageInfoEvent.getChatMessageList());
            }
        }));
        addDisposable(RxBusManager.getInstance().registerEvent(NetStatusEvent.class, netStatusEvent -> {
            if (netStatusEvent.isConnected()) {
                //                        这里判断网络的连接类型
                if (netStatusEvent.getType() == ConnectivityManager.TYPE_WIFI) {
                    CommonLogger.e("wife类型的");
                    bindPollService(10);
                } else {
                    CommonLogger.e("非wifi类型");
                    bindPollService(15);
                }
                net.setVisibility(View.GONE);
            } else {
                net.setVisibility(View.VISIBLE);
            }
        }));
    }

    private void bindPollService(int second) {
        Intent intent = new Intent(getActivity(), PollService.class);
        intent.putExtra(ConstantUtil.TIME, second);
        getActivity().startService(intent);
    }


    @Override
    protected void updateView() {
        mAdapter.addData(UserDBManager.getInstance().getAllRecentMessage());
    }


    @Override
    public void onResume() {
        super.onResume();
        onHiddenChanged(false);
    }


    @Override
    public void onRefresh() {
        mAdapter.refreshData(UserDBManager.getInstance().getAllRecentMessage());
        MsgManager.getInstance().getPersonChatInfo(getContext());
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void updateData(Object o) {

    }

    @Override
    public void onClick(View v) {
        Intent settingIntent = new Intent();
        settingIntent.setAction(Settings.ACTION_WIFI_SETTINGS);
        startActivity(settingIntent);

    }


    @Override
    public void onDestroy() {
        Intent intent = new Intent(getActivity(), PollService.class);
        getActivity().stopService(intent);
        super.onDestroy();
    }


}
