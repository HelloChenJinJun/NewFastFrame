package com.example.chat.mvp.main;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.chat.R;
import com.example.chat.adapter.MenuDisplayAdapter;
import com.example.chat.base.Constant;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.GroupChatMessage;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.bean.WeatherInfoBean;
import com.example.chat.events.LocationEvent;
import com.example.chat.events.MessageInfoEvent;
import com.example.chat.events.NetStatusEvent;
import com.example.chat.events.RecentEvent;
import com.example.chat.events.RefreshMenuEvent;
import com.example.chat.listener.OnDragDeltaChangeListener;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.NewLocationManager;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.main.friends.FriendsFragment;
import com.example.chat.mvp.main.invitation.InvitationFragment;
import com.example.chat.mvp.main.recent.RecentFragment;
import com.example.chat.mvp.shareinfo.ShareInfoFragment;
import com.example.chat.service.PollService;
import com.example.chat.mvp.chat.ChatActivity;
import com.example.chat.mvp.UserDetail.UserDetailActivity;
import com.example.chat.mvp.weather.WeatherInfoActivity;
import com.example.chat.util.LogUtil;
import com.example.chat.view.MainDragLayout;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.bean.chat.SkinEntity;
import com.example.commonlibrary.bean.chat.UserEntity;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.skin.SkinManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.StatusBarUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;


import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.view.View.GONE;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/24     12:02
 * QQ:         1981367757
 */

public class HomeFragment extends BaseFragment implements OnDragDeltaChangeListener, View.OnClickListener {
    private Fragment[] mFragments = new Fragment[5];
    private int currentPosition;
    private MainDragLayout container;
    private RecyclerView menuDisplay;
    private List<String> data = new ArrayList<>();
    private MenuDisplayAdapter menuAdapter;
    private ImageView bg;
    private RoundAngleImageView avatar;
    private TextView nick;
    private TextView signature;
    private UserEntity user;
    private TextView net;
    private TextView weatherCity;
    private TextView weatherTemperature;
    private WeatherInfoBean mWeatherInfoBean = new WeatherInfoBean();
    @Override
    public void initView() {
        RecentFragment recentFragment = new RecentFragment();
        FriendsFragment contactsFragment =FriendsFragment.newInstance();
        InvitationFragment invitationFragment = new InvitationFragment();
        ShareInfoFragment shareInfoFragment = ShareInfoFragment.newInstance(UserManager.getInstance().getCurrentUserObjectId(),true);
        mFragments[0] = recentFragment;
        mFragments[1] = contactsFragment;
        mFragments[2] = invitationFragment;
        mFragments[3] = shareInfoFragment;
        container = (MainDragLayout) findViewById(R.id.dl_activity_main_drag_container);
        menuDisplay = (RecyclerView) findViewById(R.id.rev_menu_display);
        nick = (TextView) findViewById(R.id.tv_menu_nick);
        signature = (TextView) findViewById(R.id.tv_menu_signature);
        avatar = (RoundAngleImageView) findViewById(R.id.riv_menu_avatar);
        RelativeLayout headLayout = (RelativeLayout) findViewById(R.id.rl_menu_head_layout);
        bg = (ImageView) findViewById(R.id.iv_main_bg);
        net = (TextView) findViewById(R.id.tv_main_net);
        weatherCity = (TextView) findViewById(R.id.tv_menu_weather_city);
        weatherTemperature = (TextView) findViewById(R.id.tv_menu_weather_temperature);
        LinearLayout bottomLayout = (LinearLayout) findViewById(R.id.ll_menu_bottom_container);
        bg.setAlpha((float) 0.0);
        menuDisplay.setLayoutManager(new WrappedLinearLayoutManager(getActivity()));
        container.setListener(this);
        headLayout.setOnClickListener(this);
        net.setOnClickListener(this);
        bottomLayout.setOnClickListener(this);
        initActionBarView();
    }


    /**
     * 这里重新构建一个头部布局，因为封装的基类中的头部布局与滑动发生冲突
     */
    private RoundAngleImageView icon_1;
    private TextView right_1;
    private TextView title_1;
    private ImageView rightImage_1;
    protected ImageView back_1;

    private void initActionBarView() {
        icon_1 = (RoundAngleImageView) findViewById(R.id.riv_header_layout_icon);
        right_1 = (TextView) findViewById(R.id.tv_header_layout_right);
        title_1 = (TextView) findViewById(R.id.tv_header_layout_title);
        rightImage_1 = (ImageView) findViewById(R.id.iv_header_layout_right);
        back_1 = (ImageView) findViewById(R.id.iv_header_layout_back);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        findViewById(R.id.header_layout_id).setPadding(0,StatusBarUtil.getStatusBarHeight(getContext()),0,0);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolBar);

    }


    @Override
    public void initData() {
        addOrReplaceFragment(mFragments[0], R.id.fl_content_container);
        currentPosition = 0;
        initMenu();
        initRxBus();
        initSkin();
        updateUserInfo(UserDBManager.getInstance().getUser(UserManager.getInstance().getCurrentUserObjectId()));
        initToolBarData();
        startSearchLiveWeather(BaseApplication.getAppComponent()
        .getSharedPreferences().getString(Constant.CITY,null));
        bindPollService(20);
    }

    private void initToolBarData() {
        ToolBarOption toolBarOption=new ToolBarOption();
        toolBarOption.setTitle("");
        toolBarOption.setAvatar(user.getAvatar());
        toolBarOption.setNeedNavigation(false);
        setToolBar(toolBarOption);
    }

    private void initSkin() {
        SkinEntity currentSkinEntity=UserDBManager
                .getInstance().getCurrentSkin();
        if (currentSkinEntity != null) {
            SkinManager.getInstance().update(currentSkinEntity.getPath());
        }
    }

    private void initMenu() {
        data.add("聊天");
        data.add("好友");
        data.add("邀请");
        if (BaseApplication.getAppComponent().getSharedPreferences()
                .getBoolean(ConstantUtil.IS_ALONE,true)) {
            data.add("动态");
        }
        menuAdapter = new MenuDisplayAdapter();
        menuAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (currentPosition != position) {
                    addOrReplaceFragment(mFragments[position]);
                    currentPosition = position;
                }
                closeMenu();
            }
        });
        menuDisplay.setAdapter(menuAdapter);
        menuAdapter.addData(data);
    }


    private void initRxBus() {
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
                    onProcessGroupTableMessage(message);
                }
            }
        }));
        addDisposable(RxBusManager.getInstance().registerEvent(UserEntity.class, this::updateUserInfo));
//        网络变化监听
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
                MsgManager.getInstance().refreshGroupChatMessage(new FindListener<GroupChatMessage>() {
                    @Override
                    public void done(List<GroupChatMessage> list, BmobException e) {
                        if (e == null) {
                            if (list != null && list.size() > 0) {
                                RxBusManager.getInstance().post(new RecentEvent(list.get(0).getGroupId(),RecentEvent.ACTION_ADD));
                                notifyMenuUpdate(0);
                            }
                        }else {
                            CommonLogger.e("查询群消息失败"+e.toString());
                        }
                    }
                });
//                UserManager.getInstance().refreshUserInfo();
            } else {
                net.setVisibility(View.VISIBLE);
            }
        }));
        addDisposable(RxBusManager.getInstance().registerEvent(LocationEvent.class, locationEvent -> {
            if (!locationEvent.getCity().equals(mWeatherInfoBean.getCity())) {
                startSearchLiveWeather(locationEvent.getCity());
            }
        }));
        addDisposable(RxBusManager.getInstance().registerEvent(RefreshMenuEvent.class, refreshMenuEvent -> {
            if (refreshMenuEvent.getPosition() == -1) {
                menuAdapter.notifyDataSetChanged();
            }else {
                notifyMenuUpdate(refreshMenuEvent.getPosition());
            }
        }));
    }

    private void onProcessGroupTableMessage(GroupTableMessage message) {
        MsgManager.getInstance().queryGroupChatMessage(message.getGroupId(), new FindListener<GroupChatMessage>() {
            @Override
            public void done(List<GroupChatMessage> list, BmobException e) {
                if (e == null) {
                    RxBusManager.getInstance().post(new RecentEvent(list.get(0).getGroupId(),RecentEvent.ACTION_ADD));
                    notifyMenuUpdate(0);
                }else {
                    CommonLogger.e("查询群消息失败"+e.toString());
                }
            }
        });

    }

    private void onProcessGroupChatMessage(GroupChatMessage message) {
        RxBusManager.getInstance().post(new RecentEvent(message.getGroupId(),RecentEvent.ACTION_ADD));
        notifyMenuUpdate(0);
        if (!mFragments[0].isAdded()) {
            currentPosition = 0;
            addOrReplaceFragment(mFragments[0], R.id.fl_content_container);
        }

    }

    private void updateUserInfo(UserEntity user) {
        this.user = user;
        nick.setText(user.getNick());
        if (user.getSignature() == null) {
            signature.setText("^_^1设置属于你的个性签名吧^_^");
        } else {
            signature.setText(user.getSignature());
        }
        Glide.with(this).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).into(icon_1);
        ToastUtils.showShortToast("保存个人信息成功");
        Glide.with(this).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).into(avatar);
        Glide.with(this).load(UserManager.getInstance().getCurrentUser().getWallPaper()).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                container.setBackground(resource);
            }
        });
    }




    private void bindPollService(int second) {
        Intent intent = new Intent(getActivity(), PollService.class);
        intent.putExtra(Constant.TIME, second);
        getActivity().startService(intent);
    }







    public void updateTitle(String title){
        title_1.setText(title);
    }


    @Override
    public void setToolBar(ToolBarOption option) {
        if (option.getAvatar() != null) {
            icon_1.setVisibility(View.VISIBLE);
            Glide.with(this).load(option.getAvatar()).into(icon_1);
        } else {
            icon_1.setVisibility(GONE);
        }

        if (option.getRightResId() != 0) {
            right_1.setVisibility(GONE);
            rightImage_1.setVisibility(View.VISIBLE);
            rightImage_1.setImageResource(option.getRightResId());
            rightImage_1.setOnClickListener(option.getRightListener());
        } else if (option.getRightText() != null) {
            right_1.setVisibility(View.VISIBLE);
            rightImage_1.setVisibility(GONE);
            right_1.setText(option.getRightText());
            right_1.setOnClickListener(option.getRightListener());
        } else {
            right_1.setVisibility(GONE);
            rightImage_1.setVisibility(GONE);
        }
        if (option.getTitle() != null) {
            title_1.setVisibility(View.VISIBLE);
            title_1.setText(option.getTitle());
        } else {
            title_1.setVisibility(GONE);
        }
        if (option.isNeedNavigation()) {
            back_1.setVisibility(View.VISIBLE);
            back_1.setOnClickListener(v -> getActivity().finish());
        } else {
            back_1.setVisibility(GONE);
        }
    }





    private void startSearchLiveWeather(String city) {
        if (city == null) {
            NewLocationManager.getInstance().startLocation();
            return;
        }
        mWeatherInfoBean.setCity(city);
        WeatherSearchQuery query = new WeatherSearchQuery(city, WeatherSearchQuery.WEATHER_TYPE_LIVE);
        WeatherSearch weatherSearch = new WeatherSearch(getActivity());
        weatherSearch.setQuery(query);
        weatherSearch.setOnWeatherSearchListener(new WeatherSearch.OnWeatherSearchListener() {
            @Override
            public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int i) {
                if (i == 1000) {
                    if (localWeatherLiveResult != null && localWeatherLiveResult.getLiveResult() != null) {
                        LocalWeatherLive localWeatherLive = localWeatherLiveResult.getLiveResult();
                        mWeatherInfoBean.setRealTime(localWeatherLive.getReportTime());
                        mWeatherInfoBean.setWeatherStatus(localWeatherLive.getWeather());
                        mWeatherInfoBean.setTemperature(localWeatherLive.getTemperature() + "°");
                        mWeatherInfoBean.setWind(localWeatherLive.getWindDirection() + "风       " + localWeatherLive
                                .getWindPower() + "级");
                        mWeatherInfoBean.setHumidity("湿度       " + localWeatherLive.getHumidity() + "%");
                        weatherCity.setText(mWeatherInfoBean.getCity());
                        weatherTemperature.setText(mWeatherInfoBean.getTemperature());
                    } else {
                        CommonLogger.e("获取到的天气信息为空");
                    }
                } else {
                    CommonLogger.e("获取到的天气信息失败" + i);
                }
            }

            @Override
            public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {
                if (i == 1000) {
                    if (localWeatherForecastResult != null && localWeatherForecastResult.getForecastResult() != null
                            && localWeatherForecastResult.getForecastResult().getWeatherForecast() != null
                            && localWeatherForecastResult.getForecastResult().getWeatherForecast().size() > 0) {
                        LocalWeatherForecast localWeatherForecast = localWeatherForecastResult.getForecastResult();
                        mWeatherInfoBean.setForecastTime(localWeatherForecast.getReportTime());
                        mWeatherInfoBean.setForecastInfoList(localWeatherForecast.getWeatherForecast());
                    } else {
                        LogUtil.e("查询不到天气预报的结果");
                    }
                } else {
                    LogUtil.e("查询天气预报的结果失败" + i);
                }

            }
        });
        weatherSearch.searchWeatherAsyn();
    }


    public void notifyMenuUpdate(int position) {
        menuAdapter.notifyItemChanged(position);
    }

    @Override
    public void updateData(Object o) {

    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public void notifyNewIntentCome(Intent intent) {
        String from = intent.getStringExtra(Constant.NOTIFICATION_TAG);
        if (from == null) {
            ToastUtils.showShortToast("系统通知");
            return;
        }
        if (from.equals(Constant.NOTIFICATION_TAG_GROUP_MESSAGE)) {
            Intent chat = new Intent(getActivity(), ChatActivity.class);
            chat.putExtra(Constant.ID, getActivity().getIntent().getStringExtra(Constant.GROUP_ID));
            startActivity(chat);
            return;
        }

        if (from.equals(Constant.NOTIFICATION_TAG_ADD)) {
            addOrReplaceFragment(mFragments[2], R.id.fl_content_container);
            currentPosition = 2;
        } else {
            addOrReplaceFragment(mFragments[0], R.id.fl_content_container);
            currentPosition = 0;
        }
    }




    private void onProcessNewMessages(List<ChatMessage> list) {
        for (ChatMessage chatMessage :
                list) {
            int messageType = chatMessage.getMessageType();
            switch (messageType) {
                case ChatMessage.MESSAGE_TYPE_ADD:
                    notifyMenuUpdate(2);
                    if (!mFragments[2].isAdded()) {
                        currentPosition = 2;
                        addOrReplaceFragment(mFragments[2]);
                    }
                    break;
                case ChatMessage.MESSAGE_TYPE_AGREE:
                    notifyMenuUpdate(0);
                    if (!mFragments[1].isAdded()) {
                        currentPosition = 1;
                        addOrReplaceFragment(mFragments[1]);
                    }
                    RxBusManager.getInstance().post(new RecentEvent(chatMessage.getBelongId(),RecentEvent.ACTION_ADD));
                    break;
                case ChatMessage.MESSAGE_TYPE_READED:
                    LogUtil.e("接收到的回执已读标签消息");
                    break;
                default:
                    notifyMenuUpdate(0);
                    RxBusManager.getInstance().post(new RecentEvent(chatMessage.getBelongId(),RecentEvent.ACTION_ADD));
                    if (!mFragments[0].isAdded()) {
                        currentPosition = 0;
                        addOrReplaceFragment(mFragments[0], R.id.fl_content_container);
                    }
                    break;
            }
        }
    }





//        这里说明下，由于头基类封装的头部布局与侧滑发生冲突，这里我们直接在主布局上写入头部布局,所以这里弃用基类的头部布局

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main_chat;
    }


    @Override
    protected boolean needStatusPadding() {
        return false;
    }




    @Override
    public void onDrag(View view, float delta) {
        ViewHelper.setAlpha(bg, delta);
        ViewHelper.setAlpha(icon_1, (1 - delta));
    }


    @Override
    public void onCloseMenu() {
//                当侧滑完全关闭的时候调用
        if (ViewHelper.getAlpha(bg) != 0) {
            ViewHelper.setAlpha(bg, 0);
        }
        if (ViewHelper.getAlpha(icon_1) != 1) {
            ViewHelper.setAlpha(icon_1, 1);
        }
    }

    @Override
    public void onOpenMenu() {
        if (ViewHelper.getAlpha(bg) != 1) {
            ViewHelper.setAlpha(bg, 1);
        }
        if (ViewHelper.getAlpha(icon_1) != 0) {
            ViewHelper.setAlpha(icon_1, 0);
        }
        //                当侧滑完全打开的时候调用
    }

    public void closeMenu() {
        if (container.getCurrentState() == MainDragLayout.DRAG_STATE_OPEN) {
            container.closeMenu();
        }
    }




    public void openMenu() {
        if (container.getCurrentState() == MainDragLayout.DRAG_STATE_CLOSE) {
            container.openMenu();
        }
    }

    /**
     * 点击左上角的图标的回调
     *
     * @param view view
     */
    @Override
    public void onClick(View view) {
        LogUtil.e("侧滑关闭");
        int i = view.getId();
        if (i == R.id.dl_activity_main_drag_container) {
            openMenu();
        } else if (i == R.id.rl_menu_head_layout) {//                                点击进入个人信息界面之前，先实时监听个人信息界面中的说说
            UserDetailActivity.start(getActivity(), UserManager.getInstance().getCurrentUserObjectId());
        } else if (i == R.id.tv_main_net) {
            Intent settingIntent = new Intent();
            settingIntent.setAction(Settings.ACTION_WIFI_SETTINGS);
            startActivity(settingIntent);
        } else if (i == R.id.ll_menu_bottom_container) {
            Intent weatherIntent = new Intent(getContext(), WeatherInfoActivity.class);
            weatherIntent.putExtra(Constant.DATA, mWeatherInfoBean);
            startActivityForResult(weatherIntent, Constant.REQUEST_CODE_WEATHER_INFO);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_CODE_WEATHER_INFO:
                    WeatherInfoBean weatherInfoBean = (WeatherInfoBean) data.getSerializableExtra(Constant.DATA);
                    if (weatherInfoBean != null) {
                        mWeatherInfoBean = weatherInfoBean;
                        weatherCity.setText(mWeatherInfoBean.getCity());
                        weatherTemperature.setText(mWeatherInfoBean.getTemperature());
                    }
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent(getActivity(), PollService.class);
        getActivity().stopService(intent);
        super.onDestroy();
    }


    @Override
    protected void updateView() {

    }
}
