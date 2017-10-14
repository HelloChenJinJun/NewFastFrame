package com.example.chat.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.User;
import com.example.chat.listener.AddBlackCallBackListener;
import com.example.chat.listener.CancelBlackCallBlackListener;
import com.example.chat.listener.OnSendTagMessageListener;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserCacheManager;
import com.example.chat.manager.UserManager;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.ToastUtils;

import cn.bmob.v3.exception.BmobException;

/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/25      13:17
 * QQ:             1981367757
 */
public class UserInfoActivity extends SlideBaseActivity implements View.OnClickListener {
        private User user;
        private Button black;
        private boolean isBlack;
        private Button chat;
        private Button add;
        private TextView userName;
        private TextView sex;
        private TextView nick;
        private ImageView avatar;
        private TextView address;



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
                return R.layout.activity_user_info;
        }

        @Override
        public void initView() {
                chat = (Button) findViewById(R.id.btn_user_info_chat);
                add = (Button) findViewById(R.id.btn_user_info_add_friend);
                avatar = (ImageView) findViewById(R.id.iv_user_info_avatar);
                nick = (TextView) findViewById(R.id.tv_user_info_nick);
                sex = (TextView) findViewById(R.id.tv_user_info_sex);
                address= (TextView) findViewById(R.id.tv_user_info_address);
                userName = (TextView) findViewById(R.id.tv_user_info_username);
                black = (Button) findViewById(R.id.btn_user_info_add_black);
                chat.setOnClickListener(this);
                add.setOnClickListener(this);
                black.setOnClickListener(this);
        }

        @Override
        public void initData() {
                String uid = getIntent().getStringExtra("uid");
                if (UserCacheManager.getInstance().getContacts().keySet().contains(uid)) {
                        user=UserCacheManager.getInstance().getContacts().get(uid);
                        add.setVisibility(View.GONE);
                        black.setVisibility(View.VISIBLE);
                        black.setText("添加为黑名单");
                } else if (UserCacheManager.getInstance().getBlackUser(uid) != null) {
                        user=UserCacheManager.getInstance().getBlackUser(uid);
                        black.setText("取消黑名单");
                        black.setVisibility(View.VISIBLE);
                        add.setVisibility(View.GONE);
                        isBlack=true;
                }else if (uid.equals(UserManager.getInstance().getCurrentUserObjectId())){
                       LogUtil.e("本地用户");
                        add.setVisibility(View.GONE);
                        black.setVisibility(View.GONE);
                        chat.setVisibility(View.GONE);
                }else {
                        user= (User) getIntent().getSerializableExtra("user");
                        chat.setVisibility(View.GONE);
                        black.setVisibility(View.GONE);
                }
                updateUserInfo();
                initActionBar();
        }

        private void updateUserInfo() {
                sex.setText(user.isSex() ? "男" : "女");
                userName.setText(user.getUsername());
                nick.setText(user.getNick());
                address.setText(user.getAddress());
                Glide.with(this).load(user.getAvatar()).into(avatar);
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
                        Intent intent = new Intent(this, ChatActivity.class);
                        intent.putExtra("user", user);
                        intent.putExtra("from", "person");
                        startActivity(intent);
                        finish();

                } else if (i == R.id.btn_user_info_add_friend) {
                        sendAddFriendMsg();

                } else if (i == R.id.btn_user_info_add_black) {//                                添加对方为黑名单
                        if (!isBlack) {
                                showAddBlackDialog();
                        } else {
                                showCancelBlackDialog();
                        }
                }
        }

        private void showCancelBlackDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setMessage("确定要取消对方的黑名单吗？，取消后将可以接受对方发来的消息");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                showLoadDialog("正在取消黑名单........请稍后");
                                UserManager.getInstance().cancelBlack(user, new CancelBlackCallBlackListener() {
                                        @Override
                                        public void onSuccess(User user) {
                                                UserCacheManager.getInstance().addContact(user);
                                                UserCacheManager.getInstance().deleteBlackUser(user.getObjectId());
                                                isBlack = false;
                                                black.setText("添加为黑名单");
                                                dismissLoadDialog();
                                        }

                                        @Override
                                        public void onFailed(BmobException e) {
                                                LogUtil.e("取消黑名单操作失败");
                                                cancelLoadDialog();
                                        }
                                });
                        }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                        }
                });
                builder.show();
        }

        private void showAddBlackDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setMessage("确定要添加对方为黑名单吗？，添加后将不能接受对方发来的消息");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                showLoadDialog("正在添加....请稍候");
                                UserManager.getInstance().addToBlack(user, new AddBlackCallBackListener() {
                                        @Override
                                        public void onSuccess(User user) {
                                                dismissLoadDialog();
                                                isBlack = true;
                                                ToastUtils.showShortToast("添加黑名单成功");
                                                LogUtil.e("添加黑名单成功");
                                                UserCacheManager.getInstance().deleteUser(user.getObjectId());
                                                UserCacheManager.getInstance().addBlackUser(user);
                                                black.setText("取消黑名单");
                                        }

                                        @Override
                                        public void onFailed(BmobException e) {
                                                ToastUtils.showShortToast("添加黑名单失败");
                                                LogUtil.e("添加黑名单失败" + e.getMessage() + e.getErrorCode());
                                                cancelLoadDialog();
                                        }
                                });
                        }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                        }
                });
                builder.show();
        }


//


        /**
         * 发送好友请求
         */
        private void sendAddFriendMsg() {
                showLoadDialog("正在发送请求，请稍候..........");
                LogUtil.e("正在发送好友请求，请稍后.............");
                MsgManager.getInstance().sendTagMessage(user.getObjectId(), Constant.TAG_ADD_FRIEND,
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
}
