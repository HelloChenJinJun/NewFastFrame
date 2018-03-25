package com.example.chat.ui;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/27      17:31
 * QQ:             1981367757
 */


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.ChatApplication;
import com.example.chat.R;
import com.example.chat.adapter.GridPictureAdapter;
import com.example.chat.adapter.VisibilityAdapter;
import com.example.chat.base.CommonImageLoader;
import com.example.chat.base.Constant;
import com.example.chat.bean.HappyBean;
import com.example.chat.bean.HappyContentBean;
import com.example.chat.bean.ImageItem;
import com.example.chat.bean.PictureBean;
import com.example.chat.bean.post.PublicPostBean;
import com.example.chat.bean.SharedMessage;
import com.example.chat.bean.User;
import com.example.chat.bean.WinXinBean;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.bean.post.ShareTypeContent;
import com.example.chat.listener.OnCreatePublicPostListener;
import com.example.chat.listener.OnCreateSharedMessageListener;
import com.example.chat.manager.LocationManager;
import com.example.chat.manager.MessageCacheManager;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserCacheManager;
import com.example.chat.mvp.ShareMessageTask.ShareMessageContacts;
import com.example.chat.mvp.ShareMessageTask.ShareMessageModel;
import com.example.chat.mvp.ShareMessageTask.ShareMessagePresenter;
import com.example.chat.mvp.commentlist.CommentListActivity;
import com.example.chat.util.LogUtil;
import com.example.chat.util.PostUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemChildClickListener;
import com.example.commonlibrary.baseadapter.swipeview.Closeable;
import com.example.commonlibrary.baseadapter.swipeview.OnSwipeMenuItemClickListener;
import com.example.commonlibrary.baseadapter.swipeview.SwipeMenu;
import com.example.commonlibrary.baseadapter.swipeview.SwipeMenuCreator;
import com.example.commonlibrary.baseadapter.swipeview.SwipeMenuItem;
import com.example.commonlibrary.baseadapter.swipeview.SwipeMenuRecyclerView;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import mabeijianxi.camera.MediaRecorderActivity;
import mabeijianxi.camera.model.MediaRecorderConfig;


/**
 * 编辑说说的界面
 */
public class EditShareMessageActivity extends SlideBaseActivity<List<SharedMessage>, ShareMessagePresenter> implements View.OnClickListener, ShareMessageContacts.View<List<SharedMessage>>, AdapterView.OnItemClickListener, OnSwipeMenuItemClickListener {
    /**
     * 编辑内容
     */
    private EditText edit;
    /**
     * 定位
     */
    private TextView location;


    //        点击选择可以看该说说的用户
    private TextView visibility;
    /**
     * 默认选择所有人可见
     */
    private int selectedVisibilityPosition = 1;
    private List<String> selectedVisibleUsers;

    private ShareMessagePresenter mShareMessagePresenter;


    private SuperRecyclerView display;
    private GridPictureAdapter adapter;
    private ArrayList<ImageItem> selectedImageList = new ArrayList<>();
    private ImageView video;
    /**
     * 视频存储路径
     */
    private String mPath;
    private String videoScreenshot;
    private boolean isVideo;
    private List<String> addressList;
    private WinXinBean mWinXinBean;
    private HappyContentBean mHappyContentBean;
    private HappyBean mHappyBean;
    private PictureBean mPictureBean;
    private ImageView urlAvatar;
    private TextView urlTitle;
    private CardView mCardView;
    private SwipeMenuRecyclerView selectedFriend;
    private List<String> invisibleUsers;
    private TextView title;
    private String from;
    private RelativeLayout visibilityContainer;
    private String type;
    private PublicPostBean postBean;


    @Override
    public void initView() {
        edit = (EditText) findViewById(R.id.et_edit_share_message_edit);
        location = (TextView) findViewById(R.id.tv_edit_share_message_location);
        visibility = (TextView) findViewById(R.id.tv_edit_share_message_visibility);
        display = (SuperRecyclerView) findViewById(R.id.rcv_edit_share_message_display);
        visibilityContainer = (RelativeLayout) findViewById(R.id.rl_edit_share_message_visibility_container);
        video = (ImageView) findViewById(R.id.iv_edit_share_message_video);
        RelativeLayout locationLayout = (RelativeLayout) findViewById(R.id.rl_edit_share_message_location);
        urlAvatar = (ImageView) findViewById(R.id.iv_edit_share_message_url_avatar);
        urlTitle = (TextView) findViewById(R.id.tv_edit_share_message_url_title);
        mCardView = (CardView) findViewById(R.id.cv_edit_share_message_url_container);
        selectedFriend = (SwipeMenuRecyclerView) findViewById(R.id.swrc_edit_share_message_visibility);
        title = (TextView) findViewById(R.id.tv_edit_share_message_visibility_title);
        visibilityContainer.setOnClickListener(this);
        video.setOnClickListener(this);
        locationLayout.setOnClickListener(this);
        mCardView.setOnClickListener(this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SEND)) {
            display.setVisibility(View.GONE);
            video.setVisibility(View.GONE);
            mCardView.setVisibility(View.VISIBLE);
            mCardView.setOnClickListener(this);
            String type = getIntent().getStringExtra("type");
            switch (type) {
                case "wei_xin":
                    mWinXinBean = (WinXinBean) getIntent().getSerializableExtra("share_info");
                    initWeiXinInfo();
                    break;
                case "happy":
                    mHappyBean = (HappyBean) getIntent().getSerializableExtra("share_info");
                    initHappyInfo();
                    break;
                case "happy_content":
                    mHappyContentBean = (HappyContentBean) getIntent().getSerializableExtra("share_info");
                    initHappyContentInfo();
                    break;
                case "picture":
                    mPictureBean = (PictureBean) getIntent().getSerializableExtra("share_info");
                    initPictureContentInfo();
                    break;
            }
            return;
        }
        mPath = intent.getStringExtra(MediaRecorderActivity.VIDEO_URI);
        videoScreenshot = intent.getStringExtra(MediaRecorderActivity.VIDEO_SCREENSHOT);
        if (videoScreenshot != null && !videoScreenshot.equals("")) {
            LogUtil.e("截图不为空");
            display.setVisibility(View.GONE);
            video.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(videoScreenshot);
            video.setImageBitmap(bitmap);
        } else {
            LogUtil.e("截图为空");
            if (isVideo) {
                LogUtil.e("视频取消拉");
                finish();
            } else {
                LogUtil.e("false");
                display.setVisibility(View.VISIBLE);
                video.setVisibility(View.GONE);
            }
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.e("onRestart");
    }


    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e("onStart");
    }


    private boolean isFirstCreate = true;

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("1122onResume:" + "初始化");
        if (!isFirstCreate) {
            if (from != null && from.equals("video") && videoScreenshot == null) {
                LogUtil.e("这是取消视频的操作");
                finish();
                return;
            }
        } else {
            isFirstCreate = false;
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

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
        return R.layout.edit_share_message;
    }

    @Override
    public void initData() {
        mShareMessagePresenter = new ShareMessagePresenter(this, new ShareMessageModel(ChatApplication
                .getChatMainComponent().getMainRepositoryManager()));
        initActionBar();
        if (LocationManager.getInstance().getLocationList() != null) {
            location.setText(LocationManager.getInstance().getLocationList().get(0));
        }
//                这里初始化图片选择器
        from = getIntent().getStringExtra("destination");
        if (from != null) {
            if (from.equals("video")) {
                initVideo();
            } else if (from.equals("url")) {
                display.setVisibility(View.GONE);
                video.setVisibility(View.GONE);
                mCardView.setVisibility(View.VISIBLE);
                mCardView.setOnClickListener(this);
                String type = getIntent().getStringExtra("type");
                switch (type) {
                    case "wei_xin":
                        mWinXinBean = (WinXinBean) getIntent().getSerializableExtra("share_info");
                        initWeiXinInfo();
                        break;
                    case "happy":
                        mHappyBean = (HappyBean) getIntent().getSerializableExtra("share_info");
                        initHappyInfo();
                        break;
                    case "happy_content":
                        mHappyContentBean = (HappyContentBean) getIntent().getSerializableExtra("share_info");
                        initHappyContentInfo();
                        break;
                    case "picture":
                        mPictureBean = (PictureBean) getIntent().getSerializableExtra("share_info");
                        initPictureContentInfo();
                }
            } else if (from.equals("image")) {
                display.setVisibility(View.VISIBLE);
                LogUtil.e("这是图片说说");
                initCommonImageLoader();
                initImageAdapter();
            } else if (from.equals("public")) {
                type=getIntent().getStringExtra("type");
                visibilityContainer.setVisibility(View.GONE);
                selectedFriend.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
                if (type!=null) {
                    if (type.equals("image")) {
                        display.setVisibility(View.VISIBLE);
                        initCommonImageLoader();
                        initImageAdapter();
                    }else if (type.equals("video")){
                        initVideo();
                        display.setVisibility(View.GONE);
                    }else if (type.equals("share")){
//                        todo 编辑分享消息的上传
                        postBean= (PublicPostBean) getIntent().getSerializableExtra("data");
                        Gson gson=BaseApplication
                                .getAppComponent().getGson();
                        if (postBean.getMsgType() == PostUtil.LAYOUT_TYPE_SHARE) {
                            PostDataBean bean=gson.fromJson(postBean.getContent(),PostDataBean.class);
                            ShareTypeContent shareTypeContent=bean.getShareContent();
                            PublicPostBean publicPostBean=new PublicPostBean();
                            User author=new User();
                            author.setAvatar(shareTypeContent.getAvatar());
                            author.setNick(shareTypeContent.getNick());
                            author.setObjectId(shareTypeContent.getUid());
                            author.setSex(shareTypeContent.isSex());
                            author.setAddress(shareTypeContent.getAddress());
                            publicPostBean.setAuthor(author);
                            publicPostBean.setContent(gson.toJson(shareTypeContent.getPostDataBean()));
                            publicPostBean.setMsgType(bean.getShareType());
                            publicPostBean.setLikeCount(shareTypeContent.getLikeCount());
                            publicPostBean.setCommentCount(shareTypeContent.getCommentCount());
                            publicPostBean.setShareCount(shareTypeContent.getShareCount());
                            publicPostBean.setObjectId(shareTypeContent.getPid());
                            publicPostBean.setUpdatedAt(shareTypeContent.getCreateAt());
                            postBean=publicPostBean;
                            edit.setText(bean.getContent());
                        }
                        PostDataBean bean=gson.fromJson(postBean.getContent(),PostDataBean.class);
                        display.setVisibility(View.GONE);
                        mCardView.setVisibility(View.VISIBLE);
                        if (bean.getImageList()!=null&&bean.getImageList().size()>0) {
                            urlAvatar.setVisibility(View.VISIBLE);
                            Glide.with(this).load(bean.getImageList().get(0))
                                    .into(urlAvatar);
                        }else {
                            urlAvatar.setVisibility(View.GONE);
                        }
                        urlTitle.setText(bean.getContent());
                        mCardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CommentListActivity.start(EditShareMessageActivity.this,postBean);
                            }
                        });
                    }else {
                        display.setVisibility(View.GONE);
                    }
                }
            } else {
                LogUtil.e("这是文本说说");
                display.setVisibility(View.GONE);
            }
        }
        initBottomVisibilityData();
    }

    private void initVideo() {
        LogUtil.e("这是视频说说");
        isVideo = true;
        MediaRecorderConfig config = new MediaRecorderConfig.Buidler()
                .doH264Compress(true)
                .smallVideoWidth(480)
                .smallVideoHeight(360)
                .recordTimeMax(6 * 1000)
                .maxFrameRate(20)
                .minFrameRate(8)
                .captureThumbnailsTime(1)
                .recordTimeMin((int) (1.5 * 1000))
                .build();
        MediaRecorderActivity.goSmallVideoRecorder(this, EditShareMessageActivity.class.getName(), config);

    }

    VisibilityAdapter mAdapter;


    private void initBottomVisibilityData() {
        LogUtil.e("初始化底部数据");
        selectedFriend.setLayoutManager(new LinearLayoutManager(this));
        selectedFriend.setItemAnimator(new DefaultItemAnimator());
        selectedFriend.addItemDecoration(new ListViewDecoration(this));
        selectedFriend.setHasFixedSize(true);
        selectedFriend.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(EditShareMessageActivity.this);
                int width = getResources().getDimensionPixelSize(R.dimen.recent_top_height);
                deleteItem.setBackgroundDrawable(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25))).setText("删除").setTextColor(Color.WHITE).setHeight(ViewGroup.LayoutParams.MATCH_PARENT).setWidth(width);
                swipeRightMenu.addMenuItem(deleteItem);
            }
        });
        selectedFriend.setSwipeMenuItemClickListener(this);
        mAdapter = new VisibilityAdapter();
        selectedFriend.setAdapter(mAdapter);
    }


    public void updateBottomData() {
        if (selectedVisibilityPosition == 0 || selectedVisibilityPosition == 1) {
            title.setVisibility(View.GONE);
            selectedFriend.setVisibility(View.GONE);
        } else if (selectedVisibilityPosition == 2) {
            title.setVisibility(View.VISIBLE);
            title.setText("可见的如下:");
            selectedFriend.setVisibility(View.VISIBLE);
            mAdapter.clearAllData();
            mAdapter.addData(selectedVisibleUsers);
        } else if (selectedVisibilityPosition == 3) {
            title.setText("不可见的如下:");
            title.setVisibility(View.VISIBLE);
            selectedFriend.setVisibility(View.VISIBLE);
            mAdapter.clearAllData();
            mAdapter.addData(invisibleUsers);
        }
    }

    private void initImageAdapter() {
        display.setLayoutManager(new GridLayoutManager(this, 4));
        display.setHasFixedSize(true);
        display.setItemAnimator(new DefaultItemAnimator());
        adapter = new GridPictureAdapter();
        ImageItem imageItem = new ImageItem();
        imageItem.setItemType(ImageItem.ITEM_CAMERA);
        adapter.getData().add(imageItem);
//                adapter.setMaxSelectedCount(CommonImageLoader.getInstance().getMaxSelectedCount());
        adapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {
            @Override
            public void onItemChildClick(int position, View view, int id) {
                if (id == R.id.iv_grid_picture_item_display) {
                    LogUtil.e("点击item的position" + position);
                    if (position == adapter.getData().size() - 1) {
//                                        进入选择图片界面
                        Intent intent = new Intent(EditShareMessageActivity.this, SelectedPictureActivity.class);
                        startActivityForResult(intent, Constant.REQUEST_CODE_SELECT_PICTURE);
                    } else {
                        LogUtil.e("进入预览界面");
                        Intent intent = new Intent(EditShareMessageActivity.this, BasePreViewActivity.class);
                        intent.putExtra(CommonImageLoader.CURRENT_POSITION, position);
                        intent.putExtra(CommonImageLoader.PREVIEW_FROM, CommonImageLoader.PREVIEW_DELETE);
                        startActivity(intent);
                    }
                }
            }
        });
        display.setAdapter(adapter);
    }

    private void initHappyContentInfo() {
        urlAvatar.setVisibility(View.GONE);
        if (mHappyContentBean != null) {
            urlTitle.setText(mHappyContentBean.getContent());
        }
    }

    private void initPictureContentInfo() {
        Glide.with(this).load(mPictureBean.getUrl()).into(urlAvatar);
        urlTitle.setText("一张美女图片");
    }

    private void initHappyInfo() {
        if (mHappyBean != null) {
            urlTitle.setText(mHappyBean.getContent());
            Glide.with(this).load(mHappyBean.getUrl()).into(urlAvatar);
        }
    }

    private void initWeiXinInfo() {
        if (mWinXinBean != null) {
            urlTitle.setText(mWinXinBean.getTitle());
            Glide.with(this).load(mWinXinBean.getPicUrl()).into(urlAvatar);
        }
    }

    private void initActionBar() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setAvatar(null);
        toolBarOption.setTitle("心情");
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setRightText("发送");
        toolBarOption.setRightResId(R.drawable.ic_send_blue_grey_900_24dp);
        toolBarOption.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                                发送操作
                if (edit.getText().toString().trim().equals("")) {
                    ToastUtils.showShortToast("发送的内容不能为空");
                    return;
                }
                showLoadDialog("正在发表说说..........");

                if (from != null &&from.equals("public")) {
                    MsgManager.getInstance().sendPublicPostMessage(edit.getText().toString().trim(), location.getText().toString().trim(), selectedImageList,mPath, videoScreenshot,postBean, new OnCreatePublicPostListener() {
                        @Override
                        public void onSuccess(PublicPostBean publicPostBean) {
                            dismissLoadDialog();
                            RxBusManager.getInstance().post(publicPostBean);
                            ToastUtils.showShortToast("发送成功");
                            finish();
                        }

                        @Override
                        public void onFailed(String errorMsg, int errorCode) {
                            dismissLoadDialog();
                            ToastUtils.showShortToast("发送失败" + errorMsg + errorCode);
                        }
                    });
                } else {
                    int visibleType;
                    if (selectedVisibilityPosition == 0) {
                        visibleType = Constant.SHARE_MESSAGE_VISIBLE_TYPE_PRIVATE;
                    } else {
                        visibleType = Constant.SHARE_MESSAGE_VISIBLE_TYPE_PUBLIC;
                    }
                    MsgManager.getInstance().createSharedMessage(mHappyBean, mHappyContentBean, mWinXinBean, mPictureBean, location.getText().toString().trim(), mPath, videoScreenshot, edit.getText().toString().trim(), selectedImageList, invisibleUsers, visibleType, new OnCreateSharedMessageListener() {
                        @Override
                        public void onSuccess(final SharedMessage message) {
                            mShareMessagePresenter.addShareMessage(message);
                        }

                        @Override
                        public void onFailed(int errorId, String errorMsg) {
                            dismissBaseDialog();
                            LogUtil.e("总体上传图片失败");
                            LogUtil.e(errorMsg + errorId);
                        }
                    });
                }


            }
        });
        setToolBar(toolBarOption);
    }

    private void initCommonImageLoader() {
        CommonImageLoader imageLoader = CommonImageLoader.getInstance();
        imageLoader.initStanderConfig(8);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonImageLoader.getInstance().clearAllData();
        mShareMessagePresenter.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED && requestCode == Constant.REQUEST_CODE_SELECT_VISIBILITY) {
            LogUtil.e("取消选择好友，更新可见界面文本");
            selectedVisibilityPosition = 1;
            visibility.setText("对所有人都可见");
            updateBottomData();
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
//                        这里进行图片选择后URL消息的回调处理
            switch (requestCode) {
                case Constant.REQUEST_CODE_SELECT_VISIBILITY:
//                                        选择可见性
                    if (data != null) {
                        if (selectedVisibleUsers != null && selectedVisibleUsers.size() > 0) {
                            selectedVisibleUsers.clear();
                        }
                        if (invisibleUsers != null && invisibleUsers.size() > 0) {
                            invisibleUsers.clear();
                        }
                        if (selectedVisibilityPosition == 2) {
                            selectedVisibleUsers = data.getStringArrayListExtra(Constant.RESULT_CODE_SELECT_VISIBILITY);
                            List<String> users = new ArrayList<>(UserCacheManager.getInstance().getContacts().keySet());
                            if (invisibleUsers == null) {
                                invisibleUsers = new ArrayList<>();
                            }
                            for (String uid :
                                    selectedVisibleUsers) {
                                if (!users.contains(uid)) {
                                    invisibleUsers.add(uid);
                                }
                            }
                        } else if (selectedVisibilityPosition == 3) {
                            invisibleUsers = data.getStringArrayListExtra(Constant.RESULT_CODE_SELECT_VISIBILITY);
                        }
                    }
                    if (selectedVisibleUsers != null) {
                        for (String uid :
                                invisibleUsers) {
                            LogUtil.e("不可见用户ID" + uid + "\n");
                        }
                    }
                    updateBottomData();
                    break;
                case Constant.REQUEST_CODE_SELECT_PICTURE:
                    if (selectedImageList.size() > 0) {
                        selectedImageList.clear();
                    }
                    selectedImageList.addAll(CommonImageLoader.getInstance().getSelectedImages());
                    adapter.clearAllData();
                    adapter.getData().addAll(selectedImageList);
                    ImageItem imageItem = new ImageItem();
                    imageItem.setItemType(ImageItem.ITEM_CAMERA);
                    adapter.getData().add(imageItem);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.rl_edit_share_message_visibility_container) {
            mBaseDialog.setDialogContentView(R.layout.select_visibility_layout).setTitle("谁可见").setBottomLayoutVisible(true).setLeftButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelBaseDialog();
                }
            }).setRightButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedVisibleUsers = null;
                    invisibleUsers = null;
                    dismissBaseDialog();
                    RadioGroup radioGroup = (RadioGroup) mBaseDialog.getMiddleLayout().findViewById(R.id.rg_select_visibility_container);
                    int i1 = radioGroup.getCheckedRadioButtonId();
                    if (i1 == R.id.rb_visibility_private) {
                        selectedVisibilityPosition = 0;
                        visibility.setText("仅对自己可见");
                        updateBottomData();

                    } else if (i1 == R.id.rb_visibility_public) {
                        selectedVisibilityPosition = 1;
                        visibility.setText("对所有人都可见");
                        updateBottomData();

                    } else if (i1 == R.id.rb_visibility_part) {
                        selectedVisibilityPosition = 2;
                        visibility.setText("对部分人可见");
                        Intent partVisibility = new Intent(EditShareMessageActivity.this, SelectedFriendsActivity.class);
                        partVisibility.putExtra("from", "select_visibility");
                        partVisibility.putExtra("title", "对谁可见");
                        startActivityForResult(partVisibility, Constant.REQUEST_CODE_SELECT_VISIBILITY);

                    } else if (i1 == R.id.rb_visibility_exclude_part) {
                        selectedVisibilityPosition = 3;
                        visibility.setText("对部分人不可见");
                        Intent intent = new Intent(EditShareMessageActivity.this, SelectedFriendsActivity.class);
                        intent.putExtra("from", "select_visibility");
                        intent.putExtra("title", "对谁不可见");
                        startActivityForResult(intent, Constant.REQUEST_CODE_SELECT_VISIBILITY);

                    }
                }
            }).show();
            ((RadioGroup) mBaseDialog.getMiddleLayout().findViewById(R.id.rg_select_visibility_container)).check(getCheckIdFromPosition(selectedVisibilityPosition));

        } else if (i == R.id.iv_edit_share_message_video) {
            LogUtil.e("点击了播放录制视频");
            Intent intent = new Intent(this, VideoPlayActivity.class);
            intent.putExtra("path", mPath);
            startActivity(intent);

        } else if (i == R.id.rl_edit_share_message_location) {
            if (addressList != null) {
                addressList.clear();
            } else {
                addressList = new ArrayList<>();
            }
            if (LocationManager.getInstance().getLocationList() != null) {
                addressList.addAll(LocationManager.getInstance().getLocationList());
            }
            addressList.add("不显示");
            showChooseDialog("选择位置", addressList, this);

        } else if (i == R.id.cv_edit_share_message_url_container) {
            Intent happyContentIntent = new Intent(this, HappyContentDisplayActivity.class);
            if (getIntent().getStringExtra("type").equals("happy")) {
                happyContentIntent.putExtra("content", mHappyBean.getContent());
                happyContentIntent.putExtra("url", mHappyBean.getUrl());
                startActivity(happyContentIntent);
            } else if (getIntent().getStringExtra("type").equals("happy_content")) {
                happyContentIntent.putExtra("content", mHappyContentBean.getContent());
                startActivity(happyContentIntent);
            } else {
                WeiXinNewsActivity.start(this, mWinXinBean.getTitle(), mWinXinBean.getUrl());
            }

        }
    }

    private int getCheckIdFromPosition(int selectedVisibilityPosition) {
        switch (selectedVisibilityPosition) {
            case 0:
                return R.id.rb_visibility_private;
            case 1:
                return R.id.rb_visibility_public;
            case 2:
                return R.id.rb_visibility_part;
            case 3:
                return R.id.rb_visibility_exclude_part;
            default:
                return 0;
        }
    }

    @Override
    public void updateShareMessageAdded(SharedMessage shareMessage) {
        dismissBaseDialog();
        LogUtil.e("成功发送");
        ToastUtils.showShortToast("成功发送");
        LogUtil.e("这里的说说ID" + shareMessage.getObjectId());
//                这里把本地存储的视频或图片的URL存到内存中
        if (shareMessage.getMsgType().equals(Constant.MSG_TYPE_SHARE_MESSAGE_IMAGE)) {
            List<String> urlList = new ArrayList<>();
            for (ImageItem imageItem :
                    selectedImageList) {
                urlList.add(imageItem.getPath());
            }
            MessageCacheManager.getInstance().saveShareMessageCache(shareMessage.getObjectId(), urlList);
        } else if (shareMessage.getMsgType().equals(Constant.MSG_TYPE_SHARE_MESSAGE_VIDEO)) {
            List<String> urlList = new ArrayList<>();
            urlList.add(videoScreenshot);
            urlList.add(mPath);
            MessageCacheManager.getInstance().saveShareMessageCache(shareMessage.getObjectId(), urlList);
        } else if (shareMessage.getMsgType().equals(Constant.MSG_TYPE_SHARE_MESSAGE_LINK)) {
            LogUtil.e("这里是发送链接的消息");
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("url_share_message", shareMessage);
            startActivity(intent);
            finish();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(Constant.RESULT_CODE_SHARE_MESSAGE, shareMessage);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void updateShareMessageDeleted(String id) {

    }

    @Override
    public void updateLikerAdd(String id) {

    }

    @Override
    public void updateLikerDeleted(String id) {

    }

    @Override
    public void updateCommentAdded(String id, String content, int position) {

    }

    @Override
    public void updateCommentDeleted(String id, String content, int position) {

    }

    @Override
    public void updateAllShareMessages(List<SharedMessage> data, boolean isPullRefresh) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        location.setText(addressList.get(position));
        dismissBaseDialog();
    }

    @Override
    public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, @SwipeMenuRecyclerView.DirectionMode int direction) {
        LogUtil.e("adapterPosition" + adapterPosition);
        if (selectedVisibilityPosition == 2) {
            selectedVisibleUsers.remove(adapterPosition);
        } else if (selectedVisibilityPosition == 3) {
            String oldId = invisibleUsers.remove(adapterPosition);
            selectedVisibleUsers.add(oldId);
        }
        updateBottomData();
    }

    @Override
    public void updateData(List<SharedMessage> sharedMessages) {

    }
}
