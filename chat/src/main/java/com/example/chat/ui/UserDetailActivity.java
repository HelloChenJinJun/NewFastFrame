package com.example.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.ChatApplication;
import com.example.chat.R;
import com.example.chat.adapter.OnShareMessageItemClickListener;
import com.example.chat.adapter.ShareMultipleLayoutAdapter;
import com.example.chat.base.CommonImageLoader;
import com.example.chat.base.Constant;
import com.example.chat.bean.ImageItem;
import com.example.chat.bean.SharedMessage;
import com.example.chat.bean.User;
import com.example.chat.listener.OnShareMessageReceivedListener;
import com.example.chat.manager.MessageCacheManager;
import com.example.chat.manager.UserCacheManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.ShareMessageTask.ShareMessageContacts;
import com.example.chat.mvp.ShareMessageTask.ShareMessageModel;
import com.example.chat.mvp.ShareMessageTask.ShareMessagePresenter;
import com.example.chat.service.GroupMessageService;
import com.example.chat.ui.fragment.HomeFragment;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.LogUtil;
import com.example.chat.view.CommentPopupWindow;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemChildClickListener;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/22      9:41
 * QQ:             1981367757
 */

public class UserDetailActivity extends SlideBaseActivity<List<SharedMessage>, ShareMessagePresenter> implements View.OnClickListener, OnShareMessageReceivedListener, ShareMessageContacts.View<List<SharedMessage>>, OnShareMessageItemClickListener, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    private SuperRecyclerView display;
    private ImageView bg;
    private FloatingActionButton floatingActionButton;
    String uid;
    private User mUser;
    private TextView nick;
    private TextView signature;
    private TextView sex;
    private RoundAngleImageView avatar;
    private RelativeLayout middle;
    private TextView address;
    private ShareMultipleLayoutAdapter mShareMultipleLayoutAdapter;
    private String currentId;
    private int currentPosition;
    private CommentPopupWindow mCommentPopupWindow;
    private ShareMessagePresenter presenter;
    private int currentCommentPosition;
    private String replyUid;
    private int commentItemOffset;
    private LinearLayout bottomInput;
    private EditText input;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isLoading = false;
    private int mKeyBoardHeight = 0;
    private int screenHeight;
    private RelativeLayout rootContainer;
    private ImageView send;
    private SwipeRefreshLayout refresh;


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
        return R.layout.activity_user_detail;
    }


    @Override
    public void initView() {
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_activity_user_detail_button);
        floatingActionButton.setImageResource(R.drawable.ic_mode_edit_blue_grey_900_24dp);
        display = (SuperRecyclerView) findViewById(R.id.rcv_activity_user_detail_display);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_activity_user_detail_refresh);
        bottomInput = (LinearLayout) findViewById(R.id.ll_user_detail_bottom);
        input = (EditText) findViewById(R.id.et_user_detail_input);
        send = (ImageView) findViewById(R.id.iv_user_detail_send);
        rootContainer = (RelativeLayout) findViewById(R.id.rl_activity_user_detail_container);
        rootContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                rootContainer.getWindowVisibleDisplayFrame(rect);
                screenHeight = rootContainer.getRootView().getHeight();
                int keyBoardHeight = screenHeight - rect.bottom;
//                                int status = getStatusHeight();
                if (keyBoardHeight != mKeyBoardHeight) {
                    if (keyBoardHeight > mKeyBoardHeight) {
                        bottomInput.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) bottomInput.getLayoutParams();
                        int realHeight = screenHeight - bottomInput.getHeight() - keyBoardHeight;
                        layoutParams.setMargins(0, realHeight, 0, 0);
                        bottomInput.setLayoutParams(layoutParams);
                        mKeyBoardHeight = keyBoardHeight;
                        floatingActionButton.setVisibility(View.GONE);
                        mLinearLayoutManager.scrollToPositionWithOffset(currentPosition, getListOffset());
                    } else {
                        floatingActionButton.setVisibility(View.VISIBLE);
                        mKeyBoardHeight = keyBoardHeight;
                        bottomInput.setVisibility(View.GONE);
                    }
                }
            }
        });
//                appBarLayout.addOnOffsetChangedListener(this);
        send.setOnClickListener(this);
        refresh.setOnRefreshListener(this);
        floatingActionButton.setOnClickListener(this);
        rootContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mCommentPopupWindow != null && mCommentPopupWindow.isShowing()) {
                    mCommentPopupWindow.dismiss();
                }

//                                这里进行点击关闭编辑框
                if (bottomInput.getVisibility() == View.VISIBLE) {
                    LogUtil.e("触摸界面点击关闭输入法");
                    dealBottomView(false);
                    return true;
                }
                return false;
            }
        });
    }

    private int getListOffset() {
        int offset = 0;
        if (currentCommentPosition == -1) {
            ToastUtils.showShortToast("点击评论");
            LogUtil.e("22222点击评论");
            int firstVisiblePosition = mLinearLayoutManager.findFirstVisibleItemPosition();
            View view = mLinearLayoutManager.getChildAt(currentPosition - firstVisiblePosition);
            offset += view.getHeight();
        }
        offset += mKeyBoardHeight;
        offset += bottomInput.getHeight();
//                offset += getStatusHeight();
        if (replyUid != null) {
            offset += commentItemOffset;
        }
        return screenHeight - offset;
    }


    @Override
    public void initData() {
        uid = getIntent().getStringExtra("uid");
        if (uid.equals(UserManager.getInstance().getCurrentUserObjectId())) {
            LogUtil.e("当前用户111");
            floatingActionButton.setVisibility(View.VISIBLE);
            mUser = UserManager.getInstance().getCurrentUser();
        } else {
            floatingActionButton.setVisibility(View.GONE);
            mUser = UserCacheManager.getInstance().getUser(uid);
        }
        presenter = new ShareMessagePresenter(this, new ShareMessageModel(ChatApplication
                .getChatMainComponent().getMainRepositoryManager()));
        display.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this));
        display.setItemAnimator(new DefaultItemAnimator());
        display.addItemDecoration(new ListViewDecoration(this));
        mShareMultipleLayoutAdapter = new ShareMultipleLayoutAdapter(null);
        display.setLoadMoreFooterView(new LoadMoreFooterView(this));
        display.setOnLoadMoreListener(this);
        display.addHeaderView(getHeaderView());
//                设置item内部的点击事件
        mShareMultipleLayoutAdapter.setOnShareMessageItemClickListener(this);
        mShareMultipleLayoutAdapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {
            @Override
            public void onItemChildClick(int position, View view, int id) {
                if (id == R.id.riv_share_fragment_item_main_avatar) {
                    onImageAvatarClick(UserCacheManager.getInstance().getUser(mShareMultipleLayoutAdapter.getData(position).getBelongId()).getAvatar());

                } else if (id == R.id.tv_share_fragment_item_main_name) {
                    onNameClick(mShareMultipleLayoutAdapter.getData(position).getBelongId());

                } else if (id == R.id.iv_share_fragment_item_main_comment) {
                    boolean isLike = false;
                    if (mShareMultipleLayoutAdapter.getData(position).getLikerList() != null && mShareMultipleLayoutAdapter.getData(position).getLikerList().contains(UserManager.getInstance().getCurrentUserObjectId())) {
                        LogUtil.e("已有赞");
                        isLike = true;
                    }
                    onCommentBtnClick(view, mShareMultipleLayoutAdapter.getData(position).getObjectId(), position, isLike);
                }

            }
        });
//                设置加载更多点击事件

        display.setAdapter(mShareMultipleLayoutAdapter);
//                        mShareMultipleLayoutAdapter.setOnShareMessageItemClickListener(this);
        GroupMessageService.registerListener(this);
        display.postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(true);
                onRefresh();
            }
        }, 200);
        updateUserData();
    }


    private void loadData(boolean isPullRefresh, String time) {
        presenter.loadShareMessages(uid, isPullRefresh, time);
    }

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.user_detail_header_layout, display, false);
        bg = (ImageView) view.findViewById(R.id.iv_background);
        nick = (TextView) view.findViewById(R.id.tv_user_detail_name);
        sex = (TextView) view.findViewById(R.id.tv_user_detail_sex);
        signature = (TextView) view.findViewById(R.id.tv_user_detail_signature);
        avatar = (RoundAngleImageView) view.findViewById(R.id.riv_user_detail_avatar);
        middle = (RelativeLayout) view.findViewById(R.id.rl_user_detail_middle);
        address = (TextView) view.findViewById(R.id.tv_user_detail_address);
        return view;
    }

    private void updateUserData() {

        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle(mUser.getNick());
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
        signature.setText(mUser.getSignature());
        address.setText(mUser.getAddress());
        nick.setText(mUser.getNick());
        sex.setText(mUser.isSex() ? "男" : "女");
        Glide.with(this).load(mUser.getAvatar())
                .bitmapTransform(new BlurTransformation(this, 5))
                .into(bg);
        Glide.with(this).load(mUser.getAvatar()).into(avatar);
//                Glide.with(this).load(mUser.getAvatar()).into(bgCover);
//                ViewHelper.setAlpha(bgCover, 1);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.fab_activity_user_detail_button) {
            Intent intent = new Intent(this, EditUserInfoActivity.class);
            startActivityForResult(intent, Constant.REQUEST_CODE_EDIT_USER_INFO);

        } else if (i == R.id.iv_user_detail_send) {
            String content = input.getText().toString().trim();
            if (content.equals("") || content.contains("$") || content.contains("&")) {
                LogUtil.e("评论内容不能为空或包含特殊符号，比如$或者&");
            } else {
                String wrappedContent;
                if (replyUid != null) {
                    wrappedContent = UserManager.getInstance().getCurrentUserObjectId() + "$" + replyUid + "$" + content;
                } else {
                    wrappedContent = UserManager.getInstance().getCurrentUserObjectId() + "$" + content;
                }
                presenter.addComment(mShareMultipleLayoutAdapter.getData(currentPosition).getObjectId(), wrappedContent);
                presenter.addComment(mShareMultipleLayoutAdapter.getData(currentPosition).getObjectId(), wrappedContent);
            }
            input.setText("");

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_CODE_EDIT_USER_INFO:
                    mUser = (User) data.getSerializableExtra("user");
                    updateUserData();
                    Intent intent = new Intent();
                    intent.putExtra("user", mUser);
                    setResult(Activity.RESULT_OK, intent);
                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        GroupMessageService.unRegisterListener(this);
//                appBarLayout.removeOnOffsetChangedListener(this);
    }

    @Override
    public void onAddLiker(String id, String uid) {

    }

    @Override
    public void onDeleteLiker(String id, String uid) {

    }

    @Override
    public void onDeleteCommentMessage(String id, String content) {

    }

    @Override
    public void onAddCommentMessage(String id, String content) {

    }

    @Override
    public void onAddShareMessage(SharedMessage sharedMessage) {
        LogUtil.e("实时检测到说说消息到拉");
        LogUtil.e(sharedMessage);
        if (mShareMultipleLayoutAdapter.getData().contains(sharedMessage)) {
            SharedMessage oldSharedMessage = mShareMultipleLayoutAdapter.getSharedMessageById(sharedMessage.getObjectId());
            if (oldSharedMessage != null) {
                int index = mShareMultipleLayoutAdapter.getData().indexOf(oldSharedMessage);
                mShareMultipleLayoutAdapter.getData().set(index, sharedMessage);
            }
        } else {
            mShareMultipleLayoutAdapter.getData().add(0, sharedMessage);
        }
        mShareMultipleLayoutAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteShareMessage(String id) {

    }

    @Override
    public void onImageAvatarClick(String uid) {

    }

    @Override
    public void onNameClick(String uid) {

    }

    @Override
    public void onCommentBtnClick(View view, String id, int shareMessagePosition, boolean isLike) {
        LogUtil.e("点击评论按钮");
        LogUtil.e("是否已点赞" + isLike);
        LogUtil.e("位置" + shareMessagePosition);
        currentId = id;
        currentPosition = shareMessagePosition;
        if (mCommentPopupWindow == null) {
            mCommentPopupWindow = new CommentPopupWindow(this);
            mCommentPopupWindow.setOnCommentPopupItemClickListener(new CommentPopupWindow.OnCommentPopupItemClickListener() {
                @Override
                public void onItemClick(View view, int position, boolean isLiker) {
                    if (position == 0) {
                        LogUtil.e("处理赞的操作");
                        if (isLiker) {
                            LogUtil.e("这里取消点赞操作");
                            LogUtil.e("删除点赞是的ID" + currentId);
                            presenter.deleteLiker(currentId);
                        } else {
                            LogUtil.e("这里添加点赞操作");
                            LogUtil.e("position为多少" + position);
                            LogUtil.e("添加点赞时的ID" + currentId);
                            presenter.addLiker(currentId);
                        }
                    } else if (position == 1) {
                        LogUtil.e("处理评论的操作");
//                                                进行位移
                        currentCommentPosition = -1;
                        replyUid = null;
                        commentItemOffset = 0;
                        dealBottomView(true);
                    }
                }
            });
        }
        if (isLike) {
            mCommentPopupWindow.setLikerName("取消");
        } else {
            mCommentPopupWindow.setLikerName("赞");
        }
        mCommentPopupWindow.showPopupWindow(view);

    }

    private void dealBottomView(boolean isShow) {
        if (isShow) {
            bottomInput.setVisibility(View.VISIBLE);
            CommonUtils.showSoftInput(this, input);
            input.requestFocus();
        } else {
            bottomInput.setVisibility(View.GONE);
            CommonUtils.hideSoftInput(this, input);
        }
    }

    @Override
    public void onCommentItemClick(View view, String id, int shareMessagePosition, int commentPosition, String replyUid) {
        LogUtil.e("位置" + shareMessagePosition);
        currentPosition = shareMessagePosition;
        currentCommentPosition = commentPosition;
        ViewParent viewParent = view.getParent();
        if (viewParent != null) {
            ViewGroup parent = (ViewGroup) viewParent;
            commentItemOffset += parent.getHeight() - view.getBottom();
            if (parent.getParent() != null) {
                ViewGroup rootParent = (ViewGroup) parent.getParent();
                commentItemOffset += rootParent.getHeight() + parent.getBottom();
            }
        }
        this.replyUid = replyUid;
        dealBottomView(true);
    }

    @Override
    public void onCommentItemLongClick(String id, int shareMessagePosition, int commentPosition) {
        currentPosition = shareMessagePosition;
        currentCommentPosition = commentPosition;
        showCommentDialog(id, commentPosition);
    }

    private void showCommentDialog(final String id, final int commentPosition) {
        List<String> list = new ArrayList<>();
        list.add("复制");
        if (CommonUtils.content2List(mShareMultipleLayoutAdapter.getSharedMessageById(id).getCommentMsgList().get(commentPosition)).get(0).equals(UserManager.getInstance().getCurrentUser().getObjectId())) {
            list.add("删除");
        }
        showChooseDialog("操作", list, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position1, long id1) {
                dismissBaseDialog();
                if (position1 == 0) {
                    LogUtil.e("复制操作");
                } else {
                    LogUtil.e("删除操作");
                    presenter.deleteComment(id, commentPosition);
                }
            }
        });
    }

    @Override
    public void onCommentItemNameClick(String uid) {
        enterUserDetailActivity(uid);
    }

    private void enterUserDetailActivity(String uid) {
        if (uid.equals(UserManager.getInstance().getCurrentUser().getObjectId())) {
            return;
        }
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("from", "other");
        startActivity(intent);
    }

    @Override
    public void onLikerTextViewClick(String uid) {
        enterUserDetailActivity(uid);
    }

    @Override
    public void onLinkViewClick(SharedMessage shareMessage) {

    }

    @Override
    public void onDeleteShareMessageClick(final String id) {
        LogUtil.e("删除");
        showBaseDialog("提示", "确定要删除该说说消息吗?", "取消", "确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBaseDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissBaseDialog();
                presenter.deleteShareMessage(id);
            }
        });
    }

    @Override
    public void onPhotoItemClick(final View view, final String id, final int photoPosition, final String url) {
        LogUtil.e("点击图片");
        LogUtil.e("点击的说说ID");
        List<String> cache = MessageCacheManager.getInstance().getShareMessageCache(id);
        final String newUrl;
        if (!url.contains("$")) {
            LogUtil.e("这是图片URL");
            if (cache == null || cache.size() == 0) {
                LogUtil.e("没有缓存证明不是自己发的说说");
                newUrl = url;
            } else {
                LogUtil.e("有缓存证明是自己的图片说说，这里就是用缓存");
                newUrl = cache.get(photoPosition);
            }
            LogUtil.e("url:" + newUrl);
            File file = new File(newUrl);
            if (file.exists()) {
                LogUtil.e("提前加载图片文件成功");
                List<ImageItem> list = new ArrayList<>();
                ImageItem imageItem;
                for (String str :
                        mShareMultipleLayoutAdapter.getSharedMessageById(id).getImageList()) {
                    imageItem = new ImageItem();
                    imageItem.setPath(str);
                    LogUtil.e("url" + str);
                }
                CommonImageLoader.getInstance().setSelectedImages(list);
                Intent intent = new Intent(UserDetailActivity.this, ImageDisplayActivity.class);
                intent.putStringArrayListExtra("urlList", (ArrayList<String>) mShareMultipleLayoutAdapter.getSharedMessageById(id).getImageList());
                intent.putExtra("position", photoPosition);
                intent.putExtra("name", "photo");
                intent.putExtra("url", url);
                LogUtil.e("启动图片浏览界面11");
                startActivity(intent);
                ActivityCompat.startActivity(UserDetailActivity.this, intent, ActivityOptionsCompat.makeSceneTransitionAnimation(UserDetailActivity.this, view, "photo").toBundle());
            } else {
                List<ImageItem> list = new ArrayList<>();
                ImageItem imageItem;
                for (String str :
                        mShareMultipleLayoutAdapter.getSharedMessageById(id).getImageList()) {
                    imageItem = new ImageItem();
                    imageItem.setPath(str);
                    LogUtil.e("url" + str);
                }
                CommonImageLoader.getInstance().setSelectedImages(list);
                Intent intent = new Intent(UserDetailActivity.this, ImageDisplayActivity.class);
                intent.putStringArrayListExtra("urlList", (ArrayList<String>) mShareMultipleLayoutAdapter.getSharedMessageById(id).getImageList());
                intent.putExtra("position", photoPosition);
                intent.putExtra("name", "photo");
                intent.putExtra("url", url);
                LogUtil.e("启动图片浏览界面11");
                startActivity(intent);
            }
        } else {
            if (cache == null || cache.size() == 0) {
                LogUtil.e("没有缓存证明不是自己发的说说");
                LogUtil.e("这是视频和封面混合的URL列表");
                List<String> urlList = CommonUtils.content2List(url);
                if (urlList != null && urlList.size() > 0) {
                    String videoUrl = null;
                    String imageUrl = null;
                    LogUtil.e("视频URL列表");
                    for (String str : urlList
                            ) {
                        if (str.contains(".jpg") || str.contains(".png") || str.contains(".jpeg")) {
                            LogUtil.e("封面图片URL" + str);
                            imageUrl = str;
                        } else {
                            LogUtil.e("视频URL" + str);
                            videoUrl = str;
                        }
                    }
                    if (videoUrl != null) {
                        Intent videoIntent = new Intent(UserDetailActivity.this, ImageDisplayActivity.class);
                        videoIntent.putExtra("name", "photo");
                        videoIntent.putExtra("url", imageUrl);
                        videoIntent.putExtra("videoUrl", videoUrl);
                        videoIntent.putExtra("id", id);
                        startActivity(videoIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(UserDetailActivity.this, view, "photo").toBundle());
                    }
                }
            } else {
                LogUtil.e("有缓存证明是自己的视频说说，这里就是用缓存");
                String videoUrl = cache.get(1);
                Intent intent = new Intent(UserDetailActivity.this, VideoPlayActivity.class);
                intent.putExtra("path", videoUrl);
                startActivity(intent);
            }
        }

    }


    @Override
    public void updateShareMessageAdded(SharedMessage shareMessage) {

    }

    @Override
    public void updateShareMessageDeleted(String id) {
        SharedMessage sharedMessage = mShareMultipleLayoutAdapter.getSharedMessageById(id);
        LogUtil.e("将要删除的说说消息数据格式");
        if (sharedMessage != null) {
            LogUtil.e(sharedMessage);
            mShareMultipleLayoutAdapter.getData().remove(sharedMessage);
            mShareMultipleLayoutAdapter.notifyDataSetChanged();
//                这里通知删除
            notifySharedMessageChanged(id, false);
//                        ((MainActivity) getActivity()).notifySharedMessageChanged(sharedMessage.getObjectId(), false);
        } else {
            LogUtil.e("该说说已删除");
        }
    }

    @Override
    public void updateLikerAdd(String id) {
        if (!mShareMultipleLayoutAdapter.getSharedMessageById(id).getLikerList().contains(UserManager.getInstance().getCurrentUserObjectId())) {
            LogUtil.e("还未点赞，这里添加点赞");
            mShareMultipleLayoutAdapter.getSharedMessageById(id).getLikerList().add(UserManager.getInstance().getCurrentUserObjectId());
            mShareMultipleLayoutAdapter.notifyDataSetChanged();
        } else {
            LogUtil.e("已经点赞，这里添加点赞失败,可能的原因是因为实时已经检测到拉");
        }
    }

    @Override
    public void updateLikerDeleted(String id) {
        if (mShareMultipleLayoutAdapter.getSharedMessageById(id).getLikerList().contains(UserManager.getInstance().getCurrentUserObjectId())) {
            LogUtil.e("已有点赞，这里删除点赞");
            mShareMultipleLayoutAdapter.getSharedMessageById(id).getLikerList().remove(UserManager.getInstance().getCurrentUserObjectId());
            mShareMultipleLayoutAdapter.notifyDataSetChanged();
        } else {
            LogUtil.e("没有点赞，这里删除点赞失败，可能的原因是因为实时已经检测到啦");
        }
    }

    @Override
    public void updateCommentAdded(String id, String content, int position) {
        LogUtil.e("更新添加评论操作，这里就不更新了，因为在实时检测的时候已经更新拉");
        dealBottomView(false);
        mShareMultipleLayoutAdapter.notifyDataSetChanged();

    }

    @Override
    public void updateCommentDeleted(String id, String content, int position) {
        LogUtil.e("更新删除评论操作，这里就不更新了，因为在实时检测的时候已经更新啦啦啦");
        mShareMultipleLayoutAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateAllShareMessages(List<SharedMessage> data, boolean isPullRefresh) {
        if (isPullRefresh) {
            mShareMultipleLayoutAdapter.addData(0, data);
        } else {
            mShareMultipleLayoutAdapter.addData(data);
        }
        if (data != null&&data.size()>0) {
            ArrayList<String>  list=new ArrayList<>();
            for (SharedMessage shareMessage :
                    data) {
                list.add(shareMessage.getObjectId());
            }
            notifySharedMessageChanged(list, true);
        }
    }


    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
            super.showError(errorMsg, listener);
        }else {
            display.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
        }
    }

    private void notifySharedMessageChanged(String id, boolean isAdd) {
        Intent intent = new Intent(Constant.NOTIFY_CHANGE_ACTION);
        ArrayList<String> idList = new ArrayList<>();
        idList.add(id);
        intent.putStringArrayListExtra("id", idList);
        intent.putExtra("isAdd", isAdd);
        sendBroadcast(intent);
    }

    private void notifySharedMessageChanged(ArrayList<String> list, boolean isAdd) {
        Intent intent = new Intent(Constant.NOTIFY_CHANGE_ACTION);
        intent.putStringArrayListExtra("id", list);
        intent.putExtra("isAdd", isAdd);
        sendBroadcast(intent);
    }

    @Override
    public void onRefresh() {
        SharedMessage shareMessage = mShareMultipleLayoutAdapter.getData(0);
        LogUtil.e("下拉加载的前一个说说消息为1");
        if (shareMessage == null) {
            LogUtil.e("首次加载数据");
            loadData(true, "0000-00-00 01:00:00");
        } else {
            LogUtil.e(shareMessage);
            loadData(true, shareMessage.getCreatedAt());
        }
    }


    @Override
    public void hideLoading() {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
        super.hideLoading();
    }

    @Override
    public void updateData(List<SharedMessage> sharedMessages) {

    }

    @Override
    public void loadMore() {
        int size = mShareMultipleLayoutAdapter.getData().size();
        if (size > 0) {
            loadData(false, mShareMultipleLayoutAdapter.getData(size - 1).getCreatedAt());
        } else {
            LogUtil.e("获取更多的时候data为空");
        }
    }

    public static void start(Activity activity, String uid) {
        Intent intent = new Intent(activity, UserDetailActivity.class);
        if (uid.equals(UserManager.getInstance().getCurrentUserObjectId())) {
            intent.putExtra("from", "me");
        } else {
            intent.putExtra("from", "other");
        }
        intent.putExtra("uid", uid);
        activity.startActivity(intent);
    }
}
