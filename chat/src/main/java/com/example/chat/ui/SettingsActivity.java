package com.example.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.R;
import com.example.chat.base.CommonImageLoader;
import com.example.chat.base.Constant;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserManager;
import com.example.chat.util.ChatUtil;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/5      16:43
 * QQ:             1981367757
 */

public class SettingsActivity extends SlideBaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {


        private RoundAngleImageView avatar;
        private RelativeLayout headerLayout;
        private TextView account;
        private TextView nick;
        private SwitchCompat notification;
        private LinearLayout clear;
        private TextView chatFlow;
        private Button logout;
        private String localImagePath;


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
                return R.layout.activity_settings;
        }


        @Override
        public void initView() {
                headerLayout = (RelativeLayout) findViewById(R.id.rl_setting_header);
                account = (TextView) findViewById(R.id.tv_setting_account);
                nick = (TextView) findViewById(R.id.tv_tv_setting_nick);
                avatar = (RoundAngleImageView) findViewById(R.id.riv_setting_avatar);
                RelativeLayout notificationLayout = (RelativeLayout) findViewById(R.id.rl_setting_notification);
                ((TextView) notificationLayout.findViewById(R.id.tv_setting_item_title)).setText("通知提醒");
                notification = (SwitchCompat) findViewById(R.id.switch_setting_item_check);
                clear = (LinearLayout) findViewById(R.id.ll_setting_clear);
                ((TextView) clear.findViewById(R.id.tv_group_info_item_layout_title)).setText("清空所有的聊天记录");
                chatFlow = (TextView) clear.findViewById(R.id.tv_group_info_item_layout_value);
                logout = (Button) findViewById(R.id.btn_setting_logout);
                headerLayout.setOnClickListener(this);
                clear.setOnClickListener(this);
                logout.setOnClickListener(this);
                notification.setOnCheckedChangeListener(this);
        }


        @Override
        public void initData() {
                nick.setText(UserManager.getInstance().getCurrentUser().getNick());
                account.setText("帐号：" +UserManager.getInstance().getCurrentUser().getUsername());
                Glide.with(this).load(UserManager.getInstance().getCurrentUser().getAvatar()).into(avatar);
                initActionBar();
        }

        private void initActionBar() {
                ToolBarOption toolBarOption = new ToolBarOption();
                toolBarOption.setAvatar(UserManager.getInstance().getCurrentUser().getAvatar());
                toolBarOption.setTitle("设置");
                toolBarOption.setNeedNavigation(true);
                setToolBar(toolBarOption);
        }

        private List<String> mStringList;

        @Override
        public void onClick(View v) {
                int i = v.getId();
                if (i == R.id.rl_setting_header) {
                        if (mStringList == null) {
                                mStringList = new ArrayList<>();
                                mStringList.add("拍照");
                                mStringList.add("从手机上取");
                        }
                        showChooseDialog("拍照", mStringList, this);

                } else if (i == R.id.ll_setting_clear) {
                        showLoadDialog("正在删除所有的聊天记录..........");
                        new Thread(new Runnable() {
                                @Override
                                public void run() {
                                        dismissLoadDialog();
                                        MsgManager.getInstance().clearAllChatMessage();
                                }
                        }).start();

                } else if (i == R.id.btn_setting_logout) {
                        Intent intent = new Intent(Constant.NEW_MESSAGE_ACTION);
                        intent.putExtra("isLogout", true);
                        sendBroadcast(intent);
                        Intent loginIntent = new Intent(this, LoginActivity.class);
                        startActivity(loginIntent);
                        UserManager.getInstance().logout();
                        finish();

                }

        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismissBaseDialog();
                if (position == 0) {
                        localImagePath = CommonImageLoader.getInstance().takePhoto(this, Constant.REQUEST_CODE_TAKE_PICTURE).getAbsolutePath();
                } else {
                        CommonImageLoader.getInstance().pickPhoto(this, Constant.REQUEST_CODE_SELECT_FROM_LOCAL);
                }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == Activity.RESULT_OK) {
                        switch (requestCode) {
                                case Constant.REQUEST_CODE_TAKE_PICTURE:
                                        showLoadDialog("正在上传群头像，请稍后.........");
                                        LogUtil.e("拍照的图片path为" + localImagePath);
                                        final BmobFile bmobFile = new BmobFile(new File(localImagePath));
                                        bmobFile.uploadblock(BaseApplication.getInstance(), new UploadFileListener() {
                                                @Override
                                                public void onSuccess() {
                                                        LogUtil.e("上传群头像成功");
                                                        UserManager.getInstance().updateUserInfo("avatar", bmobFile.getFileUrl(BaseApplication.getInstance()), new UpdateListener() {
                                                                @Override
                                                                public void onSuccess() {
                                                                        LogUtil.e("更新用户头像成功");
                                                                        dismissLoadDialog();
                                                                        UserManager.getInstance().getCurrentUser().setAvatar(bmobFile.getFileUrl(BaseApplication.getInstance()));
                                                                        Glide.with(SettingsActivity.this).load(bmobFile.getFileUrl(BaseApplication.getInstance())).into(avatar);
                                                                        Intent intent = new Intent();
                                                                        intent.putExtra("user", UserManager.getInstance().getCurrentUser());
                                                                        setResult(Activity.RESULT_OK, intent);
                                                                }

                                                                @Override
                                                                public void onFailure(int i, String s) {
                                                                        dismissLoadDialog();
                                                                        LogUtil.e("更新用户头像失败");
                                                                }
                                                        });

                                                }

                                                @Override
                                                public void onFailure(int i, String s) {
                                                        dismissLoadDialog();
                                                        ToastUtils.showShortToast("上传群头像失败" + s + i);
                                                        LogUtil.e("上传群头像失败" + s + i);
                                                }
                                        });
                                        break;
                                case Constant.REQUEST_CODE_SELECT_FROM_LOCAL:
                                        Uri uri = data.getData();
                                        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                                        if (cursor != null && cursor.moveToFirst()) {
                                                String path = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                                                if (path != null) {
                                                        LogUtil.e("挑选的图片path为" + path);
                                                        showLoadDialog("正在上传群头像，请稍后.........");
                                                        final BmobFile bmobFile1 = new BmobFile(new File(path));
                                                        bmobFile1.uploadblock(BaseApplication.getInstance(), new UploadFileListener() {
                                                                @Override
                                                                public void onSuccess() {
                                                                        dismissLoadDialog();
                                                                        LogUtil.e("上传群头像成功");
                                                                        UserManager.getInstance().getCurrentUser().setAvatar(bmobFile1.getFileUrl(BaseApplication.getInstance()));
                                                                        Glide.with(SettingsActivity.this).load(bmobFile1.getFileUrl(BaseApplication.getInstance())).into(avatar);
                                                                        Intent intent = new Intent();
                                                                        intent.putExtra("user", UserManager.getInstance().getCurrentUser());
                                                                        setResult(Activity.RESULT_OK, intent);
                                                                }

                                                                @Override
                                                                public void onFailure(int i, String s) {
                                                                        dismissLoadDialog();
                                                                        ToastUtils.showShortToast("上传群头像失败" + s + i);
                                                                        LogUtil.e("上传群头像失败" + s + i);
                                                                }
                                                        });
                                                } else {
                                                        LogUtil.e("挑选的图片的路径为空");
                                                }
                                        }
                                        if (cursor != null && !cursor.isClosed()) {
                                                cursor.close();
                                        }
                                        break;
                        }
                }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BaseApplication.getAppComponent().getSharedPreferences().edit().putBoolean(ChatUtil.PUSH_STATUS,isChecked);
        }

        public static void start(Activity activity, int requestCode) {
                Intent intent = new Intent(activity, SettingsActivity.class);
                activity.startActivityForResult(intent, requestCode);
        }

        @Override
        public void updateData(Object o) {
                
        }
}
