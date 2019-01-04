package com.example.chat.mvp.UserInfoTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.chat.R;
import com.example.chat.base.ChatBaseActivity;
import com.example.chat.base.ConstantUtil;
import com.example.chat.bean.ChatMessage;
import com.example.chat.listener.CancelBlackCallBlackListener;
import com.example.chat.listener.OnSendTagMessageListener;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.UserDetail.UserDetailActivity;
import com.example.chat.mvp.chat.ChatActivity;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.bean.chat.User;
import com.example.commonlibrary.bean.chat.UserEntity;
import com.example.commonlibrary.customview.RoundAngleImageView;
import com.example.commonlibrary.customview.ToolBarOption;
import com.example.commonlibrary.utils.BlurBitmapUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/25      13:17
 * QQ:             1981367757
 */
public class UserInfoActivity extends ChatBaseActivity implements View.OnClickListener {
    private UserEntity userEntity;
    private Button black;
    private boolean isBlack;
    private Button chat;
    private Button add;
    private TextView userName;
    private ImageView sex;
    private TextView name;
    private RoundAngleImageView avatar;
    private String uid;
    private TextView signature;


    private FrameLayout headerContainer;


    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initView() {
        chat = findViewById(R.id.btn_user_info_chat);
        add = findViewById(R.id.btn_user_info_add_friend);
        avatar = findViewById(R.id.riv_user_info_avatar);
        name = findViewById(R.id.tv_user_info_name);
        sex = findViewById(R.id.iv_user_info_sex);
        userName = findViewById(R.id.tv_user_info_username);
        black = findViewById(R.id.btn_user_info_add_black);
        headerContainer = findViewById(R.id.fl_activity_user_info_bg);
        signature = findViewById(R.id.tv_activity_user_info_signature);
        chat.setOnClickListener(this);
        add.setOnClickListener(this);
        black.setOnClickListener(this);
        avatar.setOnClickListener(this);
    }

    @Override
    public void initData() {
        initActionBar();
        uid = getIntent().getStringExtra(ConstantUtil.ID);
        if (uid.equals(UserManager.getInstance().getCurrentUserObjectId())) {
            userEntity = UserManager.getInstance().cover(UserManager
                    .getInstance().getCurrentUser());
            LogUtil.e("本地用户");
            add.setVisibility(View.GONE);
            black.setVisibility(View.GONE);
            chat.setVisibility(View.GONE);
        } else {
            userEntity = UserDBManager.getInstance()
                    .getUser(uid);
            if (UserDBManager.getInstance().isFriend(uid)) {
                add.setVisibility(View.GONE);
                black.setVisibility(View.VISIBLE);
                black.setText("添加为黑名单");
            } else if (UserDBManager.getInstance().isAddBlack(uid)) {
                black.setText("取消黑名单");
                black.setVisibility(View.VISIBLE);
                add.setVisibility(View.GONE);
                isBlack = true;
            } else{
                black.setVisibility(View.GONE);
                add.setVisibility(View.VISIBLE);
                chat.setVisibility(View.GONE);
            }
        }
        updateUserInfo();
        ViewCompat.setTransitionName(avatar, "avatar");
        ViewCompat.setTransitionName(sex, "sex");
        ViewCompat.setTransitionName(name, "name");
        ViewCompat.setTransitionName(signature, "signature");
        ViewCompat.setTransitionName(headerContainer, "headerContainer");
        supportPostponeEnterTransition();
        name.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                name.getViewTreeObserver().removeOnPreDrawListener(this);
                supportStartPostponedEnterTransition();
                return false;
            }
        });
    }


    @Override
    protected boolean needSlide() {
        return false;
    }

    private void loadUserInfo() {
        UserManager.getInstance().findUserById(uid, new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (list != null && list.size() > 0) {
                    UserDBManager
                            .getInstance()
                            .addOrUpdateUser(UserManager
                                    .getInstance()
                                    .cover(list.get(0), true));
                    userEntity = UserManager.getInstance()
                            .cover(list.get(0));
                    if (UserDBManager.getInstance().isFriend(uid)) {
                        add.setVisibility(View.GONE);
                        black.setVisibility(View.VISIBLE);
                        black.setText("添加为黑名单");
                    } else if (UserDBManager.getInstance().isAddBlack(uid)) {
                        black.setText("取消黑名单");
                        black.setVisibility(View.VISIBLE);
                        add.setVisibility(View.GONE);
                        isBlack = true;
                    } else {
                        black.setVisibility(View.GONE);
                        add.setVisibility(View.VISIBLE);
                        chat.setVisibility(View.GONE);
                    }
                    updateUserInfo();
                    hideLoading();
                } else {
                    if (e != null) {
                        showError(e.toString(), () -> loadUserInfo());
                    } else {
                        ToastUtils.showShortToast("暂时没有该用户数据");
                        showEmptyView();
                    }
                }
            }
        });
    }

    private void updateUserInfo() {
        sex.setImageResource(userEntity.isSex() ? R.drawable.ic_sex_male : R.drawable.ic_sex_female);
        userName.setText(userEntity.getUserName());
        name.setText(userEntity.getName());
        signature.setText(userEntity.getSignature());
        Glide.with(this).load(userEntity.getAvatar()).into(avatar);
        Glide.with(this).asBitmap().load(userEntity.getTitlePaper()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                headerContainer.setBackground(BlurBitmapUtil.createBlurredImageFromBitmap(resource, UserInfoActivity.this, 20));
            }
        });
    }

    private void initActionBar() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setAvatar(null);
        toolBarOption.setTitle("详细资料");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_user_info_chat) {
            ChatActivity.start(this, ConstantUtil.TYPE_PERSON, uid);

        } else if (i == R.id.btn_user_info_add_friend) {
            sendAddFriendMsg();

        } else if (i == R.id.btn_user_info_add_black) {//                                添加对方为黑名单
            if (!isBlack) {
                showAddBlackDialog();
            } else {
                showCancelBlackDialog();
            }
        } else if (i == R.id.riv_user_info_avatar) {
            UserDetailActivity.start(this,userEntity.getUid(), ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create(avatar, "avatar")
                    , Pair.create(name, "name")
                    , Pair.create(sex, "sex")
            ,Pair.create(signature,"signature")));
        }
    }

    private void showCancelBlackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("确定要取消对方的黑名单吗？，取消后将可以接受对方发来的消息");
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            showLoadDialog("正在取消黑名单........请稍后");
            UserManager.getInstance().cancelBlack(uid, new CancelBlackCallBlackListener() {
                @Override
                public void onSuccess() {
                    isBlack = false;
                    black.setText("添加为黑名单");
                    dismissLoadDialog();
                }

                @Override
                public void onFailed(BmobException e) {
                    LogUtil.e("取消黑名单操作失败");
                }
            });
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showAddBlackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("确定要添加对方为黑名单吗？，添加后将不能接受对方发来的消息");
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            ToastUtils.showLongToast("黑名单该版本不支持");
//            showLoadDialog("正在添加....请稍候");
//            UserManager.getInstance().addToBlack(uid, new AddBlackCallBackListener() {
//                @Override
//                public void onSuccess() {
//                    dismissLoadDialog();
//                    isBlack = true;
//                    ToastUtils.showShortToast("添加黑名单成功");
//                    LogUtil.e("添加黑名单成功");
//                    black.setText("取消黑名单");
//                }
//
//                @Override
//                public void onFailed(BmobException e) {
//                    ToastUtils.showShortToast("添加黑名单失败");
//                    LogUtil.e("添加黑名单失败" + e.getMessage() + e.getErrorCode());
//                }
//            });
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());
        builder.show();
    }


    //


    /**
     * 发送好友请求
     */
    private void sendAddFriendMsg() {
        showLoadDialog("正在发送请求，请稍候..........");
        LogUtil.e("正在发送好友请求，请稍后.............");
        MsgManager.getInstance().sendTagMessage(uid, ChatMessage.MESSAGE_TYPE_ADD,
                new OnSendTagMessageListener() {
                    @Override
                    public void onSuccess(ChatMessage chatMessage) {
                        dismissLoadDialog();
                        ToastUtils.showShortToast("发送好友请求成功");
                        LogUtil.e("发送好友请求成功");
                        add.setClickable(false);
                    }

                    @Override
                    public void onFailed(BmobException e) {
                        dismissLoadDialog();
                        ToastUtils.showShortToast("发送好友请求失败");
                        LogUtil.e("发送好友请求失败");
                    }
                });
    }

    @Override
    public void updateData(Object o) {

    }


    public static void start(Activity activity, String uid) {
        start(activity, uid, null);
    }


    public static void start(Activity activity, String uid, ActivityOptionsCompat activityOptionsCompat) {
        Intent intent = new Intent(activity, UserInfoActivity.class);
        intent.putExtra(ConstantUtil.ID, uid);
        if (activityOptionsCompat != null) {
            activity.startActivity(intent, activityOptionsCompat.toBundle());
        } else {
            activity.startActivity(intent);
        }
    }
}
