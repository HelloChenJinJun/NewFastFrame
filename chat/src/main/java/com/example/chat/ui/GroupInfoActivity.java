package com.example.chat.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chat.R;
import com.example.chat.adapter.GroupGridViewAdapter;
import com.example.chat.base.CommonImageLoader;
import com.example.chat.base.Constant;
import com.example.chat.bean.GroupNumberInfo;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.bean.User;
import com.example.chat.events.GroupInfoEvent;
import com.example.chat.manager.MessageCacheManager;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserManager;
import com.example.chat.util.LogUtil;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemChildClickListener;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/31      18:01
 * QQ:             1981367757
 */

public class GroupInfoActivity extends SlideBaseActivity implements View.OnClickListener {
        private RelativeLayout headerLayout;
        private RoundAngleImageView groupAvatar;
        private TextView createTime;
        private LinearLayout numberName;
        private LinearLayout numberCount;
        private RecyclerView numberAvatar;
        private LinearLayout groupNameLayout;
        private LinearLayout groupDescription;
        private LinearLayout groupNotification;
        private LinearLayout groupRemind;
        private TextView numberNick;
        private TextView groupNumberCount;
        private TextView notification;
        private TextView description;
        private TextView remind;
        private TextView groupName;
        private TextView groupName1;
        private Button exit;
        private GroupTableMessage mGroupTableMessage;
        private GroupGridViewAdapter mAdapter;
        private String localImagePath;
        private boolean isCreator;
        private boolean isRemind;

        public static void start(Context context, String groupId, int requestCode) {
                Intent intent = new Intent(context, GroupInfoActivity.class);
                intent.putExtra("groupId", groupId);
                ((Activity) context).startActivityForResult(intent, requestCode);
        }




        @Override
        public void initView() {
                headerLayout = (RelativeLayout) findViewById(R.id.rl_group_info_header);
                groupAvatar = (RoundAngleImageView) findViewById(R.id.riv_group_info_avatar);
                groupName = (TextView) findViewById(R.id.tv_group_info_name);
                createTime = (TextView) findViewById(R.id.tv_group_info_create_time);
                numberName = (LinearLayout) findViewById(R.id.ll_group_info_number_name);
                numberCount = (LinearLayout) findViewById(R.id.ll_group_info_number_count);
                numberAvatar = (RecyclerView) findViewById(R.id.rcv_group_info_number_avatar);
                groupNameLayout = (LinearLayout) findViewById(R.id.ll_group_info_group_name);
                groupDescription = (LinearLayout) findViewById(R.id.ll_group_info_group_description);
                groupNotification = (LinearLayout) findViewById(R.id.ll_group_info_group_notification);
                groupRemind = (LinearLayout) findViewById(R.id.ll_group_info_remind);
                exit = (Button) findViewById(R.id.btn_group_info_exit_group);
                ((TextView) numberName.findViewById(R.id.tv_group_info_item_layout_title)).setText("群名片");
                numberNick = (TextView) numberName.findViewById(R.id.tv_group_info_item_layout_value);
                ((TextView) numberCount.findViewById(R.id.tv_group_info_item_layout_title)).setText("群总人数");
                ((TextView) groupNameLayout.findViewById(R.id.tv_group_info_item_layout_title)).setText("群名");
                ((TextView) groupDescription.findViewById(R.id.tv_group_info_item_layout_title)).setText("群介绍");
                ((TextView) groupNotification.findViewById(R.id.tv_group_info_item_layout_title)).setText("群通知");
                ((TextView) groupRemind.findViewById(R.id.tv_group_info_item_layout_title)).setText("群消息提醒");
                groupNumberCount = (TextView) numberCount.findViewById(R.id.tv_group_info_item_layout_value);
                notification = (TextView) groupNotification.findViewById(R.id.tv_group_info_item_layout_value);
                description = (TextView) groupDescription.findViewById(R.id.tv_group_info_item_layout_value);
                remind = (TextView) groupRemind.findViewById(R.id.tv_group_info_item_layout_value);
                groupName1 = (TextView) groupNameLayout.findViewById(R.id.tv_group_info_item_layout_value);
                headerLayout.setOnClickListener(this);
                numberName.setOnClickListener(this);
                numberCount.setOnClickListener(this);
                groupNameLayout.setOnClickListener(this);
                groupNotification.setOnClickListener(this);
                groupDescription.setOnClickListener(this);
                groupRemind.setOnClickListener(this);
                exit.setOnClickListener(this);
        }




        @Override
        public void initData() {
                mGroupTableMessage = MessageCacheManager.getInstance().getGroupTableMessage(getIntent().getStringExtra("groupId"));
                ToolBarOption toolBarOption = new ToolBarOption();
                toolBarOption.setAvatar(null);
                toolBarOption.setTitle(mGroupTableMessage.getGroupName());
                toolBarOption.setNeedNavigation(true);
                setToolBar(toolBarOption);
                mAdapter = new GroupGridViewAdapter();
                numberAvatar.setLayoutManager(new GridLayoutManager(this, 5));
                numberAvatar.setItemAnimator(new DefaultItemAnimator());
                mAdapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {
                        @Override
                        public void onItemChildClick(int position, View view, int id) {
                                if (id==R.id.riv_group_info_avatar_item_layout_avatar){
                                        Intent intent = new Intent(GroupInfoActivity.this, GroupNumberInfoDetailActivity.class);
                                        GroupNumberInfo groupNumberInfo = mAdapter.getData(position);
                                        intent.putExtra("isCreator", isCreator);
                                        intent.putExtra("groupId", mGroupTableMessage.getGroupId());
                                        intent.putExtra("groupNumberInfo", groupNumberInfo);
                                        startActivity(intent);
                                }
                        }
                });
                numberAvatar.setAdapter(mAdapter);
                if (UserManager.getInstance().getCurrentUser().getObjectId().equals(mGroupTableMessage.getCreatorId())) {
                        isCreator = true;
                        LogUtil.e("当前用户为群主，可以修改群头像");
                        headerLayout.setClickable(true);
                        groupNotification.setClickable(true);
                        groupDescription.setClickable(true);
                        groupNameLayout.setClickable(true);
                } else {
                        isCreator = false;
                        groupNameLayout.setClickable(false);
                        groupNotification.setClickable(false);
                        groupDescription.setClickable(false);
                        headerLayout.setClickable(false);
                }
                ToolBarOption mToolBarOption = new ToolBarOption();
                mToolBarOption.setAvatar(null);
                mToolBarOption.setTitle(mGroupTableMessage.getGroupName());
                mToolBarOption.setNeedNavigation(true);
                setToolBar(mToolBarOption);
                isRemind = BaseApplication.getAppComponent()
                        .getSharedPreferences().getBoolean(mGroupTableMessage.getGroupId(),true);
                updateData();
                numberAvatar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                                if (MessageCacheManager.getInstance().getAllGroupNumberInfo(mGroupTableMessage.getGroupId()) == null) {
                                        showLoadDialog("正在加载群成员消息，请稍候............");
                                        MsgManager.getInstance().queryAllGroupNumber(mGroupTableMessage.getGroupId(), new FindListener<User>() {
                                                @Override
                                                public void onSuccess(final List<User> userList) {
                                                        if (userList != null && userList.size() > 0) {
                                                                LogUtil.e("群成员信息");
                                                                for (User user :
                                                                        userList) {
                                                                        LogUtil.e(user);
                                                                }
                                                                MsgManager.getInstance().queryAllGroupTableMessage(mGroupTableMessage.getGroupId(), new FindListener<GroupTableMessage>() {
                                                                        @Override
                                                                        public void onSuccess(List<GroupTableMessage> list) {
                                                                                dismissLoadDialog();
                                                                                List<GroupNumberInfo> groupInfoList;
                                                                                if (list != null && list.size() > 0) {
                                                                                        groupInfoList = new ArrayList<>();
                                                                                        GroupNumberInfo groupNumberInfo;
                                                                                        LogUtil.e("查询得到的成员群结构消息列表");
                                                                                        for (GroupTableMessage message :
                                                                                                list) {
                                                                                                LogUtil.e(message);
                                                                                                groupNumberInfo = new GroupNumberInfo();
                                                                                                groupNumberInfo.setGroupNick(message.getGroupNick());
                                                                                                for (User user :
                                                                                                        userList
                                                                                                        ) {
                                                                                                        if (user.getObjectId().equals(message.getToId())) {
                                                                                                                groupNumberInfo.setUser(user);
                                                                                                        }
                                                                                                }
                                                                                                groupInfoList.add(groupNumberInfo);
                                                                                        }
                                                                                        MessageCacheManager.getInstance().setAllGroupNumberInfo(mGroupTableMessage.getGroupId(), groupInfoList);
                                                                                        mAdapter.clearAllData();
                                                                                        mAdapter.addData(MessageCacheManager.getInstance().getAllGroupNumberInfo(mGroupTableMessage.getGroupId()));
                                                                                        mAdapter.notifyDataSetChanged();
                                                                                } else {
                                                                                        LogUtil.e("在服务器上没有查询到群结构消息");
                                                                                }
                                                                        }

                                                                        @Override
                                                                        public void onError(int i, String s) {
                                                                                LogUtil.e("查询群成员的群资料失败" + s + i);
                                                                                dismissLoadDialog();
                                                                        }
                                                                });
                                                        }
                                                }

                                                @Override
                                                public void onError(int i, String s) {
                                                        dismissLoadDialog();
                                                        LogUtil.e("查询群成员信息失败" + s + i);
                                                }
                                        });
                                } else {
                                        mAdapter.clearAllData();
                                        mAdapter.addData(MessageCacheManager.getInstance().getAllGroupNumberInfo(mGroupTableMessage.getGroupId()));
                                        mAdapter.notifyDataSetChanged();
                                }
                        }
                },200);
        }

        private void updateData() {
                groupName.setText(mGroupTableMessage.getGroupName());
                groupName1.setText(mGroupTableMessage.getGroupName());
                if (mGroupTableMessage.getGroupNick() == null || mGroupTableMessage.getGroupNick().equals("")) {
                        numberNick.setText("未设置");
                        LogUtil.e("未设置");
                } else {
                        numberNick.setText(mGroupTableMessage.getGroupNick());
                }
                groupNumberCount.setText(mGroupTableMessage.getGroupNumber().size() + "人");
                description.setText(mGroupTableMessage.getGroupDescription());
                notification.setText(mGroupTableMessage.getNotification());
//                默认开启状态
                if (isRemind) {
                        remind.setText("开启");
                } else {
                        remind.setText("关闭");
                }
                Glide.with(this).load(mGroupTableMessage.getGroupAvatar()).into(groupAvatar);
                createTime.setText("于" + TimeUtil.getTime(Long.valueOf(mGroupTableMessage.getCreatedTime())) + "创建");
        }


        @Override
        protected void onResume() {
                super.onResume();
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
                return R.layout.activity_group_info;
        }

        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                        case R.id.rl_group_info_header:
                                List<String> list = new ArrayList<>();
                                list.add("拍摄");
                                list.add("从手机相册选择");
                                showChooseDialog("设置头像", list, new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                dismissBaseDialog();
                                                if (position == 0) {
//                                                        File imageFile = FileUtil.newFile(FileUtil.newDir(Constant.IMAGE_CACHE_DIR).getAbsolutePath() + System.currentTimeMillis());
                                                        localImagePath=    CommonImageLoader.getInstance().takePhoto(GroupInfoActivity.this, Constant.REQUEST_CODE_TAKE_PICTURE).getAbsolutePath();
                                                } else {
                                                        CommonImageLoader.getInstance().pickPhoto(GroupInfoActivity.this, Constant.REQUEST_CODE_SELECT_FROM_LOCAL);
                                                }
                                        }
                                });
                                break;
                        case R.id.ll_group_info_number_name:
//                                编辑昵称
                                Intent intent = new Intent(this, EditUserInfoDetailActivity.class);
                                intent.putExtra("groupId", mGroupTableMessage.getGroupId());
                                intent.putExtra("from", "groupNick");
                                startActivityForResult(intent, Constant.REQUEST_CODE_EDIT_GROUP_INFO_NICK);
                                break;
                        case R.id.ll_group_info_number_count:
                                Intent numberIntent = new Intent(this, GroupNumberDisplayActivity.class);
                                numberIntent.putExtra("groupId", mGroupTableMessage.getGroupId());
                                numberIntent.putExtra("isCreator", isCreator);
                                startActivity(numberIntent);
                                break;
                        case R.id.ll_group_info_group_name:
                                Intent groupName = new Intent(this, EditUserInfoDetailActivity.class);
                                groupName.putExtra("groupId", mGroupTableMessage.getGroupId());
                                groupName.putExtra("from", "groupName");
                                groupName.putExtra("message", mGroupTableMessage.getGroupName());
                                startActivityForResult(groupName, Constant.REQUEST_CODE_EDIT_GROUP_INFO_GROUP_NAME);
                                break;
                        case R.id.ll_group_info_group_description:
                                Intent description = new Intent(this, EditUserInfoDetailActivity.class);
                                description.putExtra("groupId", mGroupTableMessage.getGroupId());
                                description.putExtra("from", "groupDescription");
                                description.putExtra("message", mGroupTableMessage.getGroupDescription());
                                startActivityForResult(description, Constant.REQUEST_CODE_EDIT_GROUP_INFO_DESCRIPTION);
                                break;
                        case R.id.ll_group_info_group_notification:
                                Intent notification = new Intent(this, EditUserInfoDetailActivity.class);
                                notification.putExtra("groupId", mGroupTableMessage.getGroupId());
                                notification.putExtra("from", "groupNotification");
                                notification.putExtra("message", mGroupTableMessage.getNotification());
                                startActivityForResult(notification, Constant.REQUEST_CODE_EDIT_GROUP_INFO_NOTIFICATION);
                                break;
                        case R.id.ll_group_info_remind:
                                isRemind = !isRemind;
                                BaseApplication.getAppComponent().getSharedPreferences()
                                        .edit().putBoolean(mGroupTableMessage.getGroupId(),isRemind);
                                if (isRemind) {
                                        remind.setText("开启");
                                } else {
                                        remind.setText("关闭");
                                }
                                break;
                        case R.id.btn_group_info_exit_group:
                                if (mGroupTableMessage.getCreatorId().equals(UserManager.getInstance().getCurrentUserObjectId())) {
//                                        群主退出该群,
                                        Toast.makeText(this, "群组不能退出该群", Toast.LENGTH_SHORT).show();
                                }else {
                                        GroupTableMessage groupTableMessage=new GroupTableMessage();
                                        groupTableMessage.setObjectId(mGroupTableMessage.getGroupId());
                                        List<String> numberList=new ArrayList<>(mGroupTableMessage.getGroupNumber());
                                        if (numberList.contains(UserManager.getInstance().getCurrentUserObjectId())) {
                                                numberList.remove(UserManager.getInstance().getCurrentUserObjectId());
                                        }
                                        groupTableMessage.setGroupNumber(numberList);
                                        groupTableMessage.update(this, new UpdateListener() {
                                                @Override
                                                public void onSuccess() {
                                                        LogUtil.e("更新群主的群结构消息成功");
                                                        finish();
                                                }

                                                @Override
                                                public void onFailure(int i, String s) {
                                                        LogUtil.e("更新群主的群结构消息失败"+s+i);
                                                }
                                        });
                                }

                                break;
                        default:
                                break;
                }
        }




        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == Activity.RESULT_OK) {
                        String message = data.getStringExtra("message");
                        switch (requestCode) {
                                case Constant.REQUEST_CODE_TAKE_PICTURE:
                                        showLoadDialog("正在上传群头像，请稍后.........");
                                        LogUtil.e("拍照的图片path为" + localImagePath);
                                        final BmobFile bmobFile = new BmobFile(new File(localImagePath));
                                        bmobFile.uploadblock(BaseApplication.getInstance(), new UploadFileListener() {
                                                @Override
                                                public void onSuccess() {
                                                        LogUtil.e("上传群头像成功");
                                                        MsgManager.getInstance().updateGroupMessage(mGroupTableMessage.getGroupId(), "groupAvatar", bmobFile.getFileUrl(BaseApplication.getInstance()), new UpdateListener() {
                                                                @Override
                                                                public void onSuccess() {
                                                                        dismissLoadDialog();
                                                                        ToastUtils.showShortToast("更新群头像成功");
                                                                        LogUtil.e("更新群消息的头像成功");
                                                                        Glide.with(GroupInfoActivity.this).load(new File(localImagePath)).into(groupAvatar);
                                                                        LogUtil.e("通知聊天界面群头像的改变");
                                                                        RxBusManager.getInstance().post(new GroupInfoEvent(localImagePath,GroupInfoEvent.TYPE_GROUP_AVATRA));


                                                                }

                                                                @Override
                                                                public void onFailure(int i, String s) {
                                                                        dismissLoadDialog();
                                                                        LogUtil.e("更新群消息的头像失败" + s + i);
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
                                                final String path = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                                                if (path != null) {
                                                        LogUtil.e("挑选的图片path为" + path);
                                                        showLoadDialog("正在上传群头像，请稍后.........");
                                                        final BmobFile bmobFile1 = new BmobFile(new File(path));
                                                        bmobFile1.uploadblock(BaseApplication.getInstance(), new UploadFileListener() {
                                                                @Override
                                                                public void onSuccess() {
                                                                        LogUtil.e("上传群头像成功");
                                                                        MsgManager.getInstance().updateGroupMessage(mGroupTableMessage.getGroupId(), "groupAvatar", bmobFile1.getFileUrl(BaseApplication.getInstance()), new UpdateListener() {
                                                                                @Override
                                                                                public void onSuccess() {
                                                                                        dismissLoadDialog();
                                                                                        ToastUtils.showShortToast("更新群头像成功");
                                                                                        LogUtil.e("更新群消息的头像成功");
//                                                                                        LogUtil.e("现在的群头像" + mGroupTableMessage.getGroupAvatar());
//                                                                                        MessageCacheManager.getInstance().getGroupTableMessage(mGroupTableMessage.getGroupId()).setGroupAvatar(bmobFile1.getFileUrl(BaseApplication.getInstance()));
//                                                                                        LogUtil.e("如今的群头像" + mGroupTableMessage.getGroupAvatar());
//                                                                                        ChatDB.create().saveGroupTableMessage(mGroupTableMessage);
                                                                                        Glide.with(GroupInfoActivity.this).load(new File(path)).into(groupAvatar);
                                                                                        RxBusManager.getInstance().post(new GroupInfoEvent(path,GroupInfoEvent.TYPE_GROUP_AVATRA));
                                                                                }

                                                                                @Override
                                                                                public void onFailure(int i, String s) {
                                                                                        dismissLoadDialog();
                                                                                        LogUtil.e("更新群消息的头像失败" + s + i);
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
                                                } else {
                                                        LogUtil.e("挑选的图片的路径为空");
                                                }
                                        }
                                        if (cursor != null && !cursor.isClosed()) {
                                                cursor.close();
                                        }
                                        break;
                                case Constant.REQUEST_CODE_EDIT_GROUP_INFO_NICK:
                                        LogUtil.e("这里群昵称的改变");
                                        mGroupTableMessage.setGroupNick(message);
                                        numberNick.setText(message);
                                        List<GroupNumberInfo> list = MessageCacheManager.getInstance().getAllGroupNumberInfo(mGroupTableMessage.getGroupId());
                                        if (list != null) {
                                                for (GroupNumberInfo info :
                                                        list) {
                                                        if (info.getUser().getObjectId().equals(UserManager.getInstance().getCurrentUser().getObjectId())) {
                                                                info.setGroupNick(message);
                                                                break;
                                                        }
                                                }
                                                mAdapter.clearAllData();
                                                mAdapter.addData(list);
                                        }
                                        RxBusManager.getInstance().post(new GroupInfoEvent(message,GroupInfoEvent.TYPE_GROUP_NICK));
                                        break;
                                case Constant.REQUEST_CODE_EDIT_GROUP_INFO_DESCRIPTION:
                                        mGroupTableMessage.setGroupDescription(message);
                                        description.setText(message);
                                        RxBusManager.getInstance().post(new GroupInfoEvent(message,GroupInfoEvent.TYPE_GROUP_DESCRIPTION));

                                        break;
                                case Constant.REQUEST_CODE_EDIT_GROUP_INFO_NOTIFICATION:
                                        mGroupTableMessage.setNotification(message);
                                        notification.setText(message);
                                        RxBusManager.getInstance().post(new GroupInfoEvent(message,GroupInfoEvent.TYPE_GROUP_NOTIFICATION));
                                        break;
                                case Constant.REQUEST_CODE_EDIT_GROUP_INFO_GROUP_NAME:
                                        mGroupTableMessage.setGroupName(message);
                                        groupName.setText(message);
                                        groupName1.setText(message);




                                        RxBusManager.getInstance().post(new GroupInfoEvent(message,GroupInfoEvent.TYPE_GROUP_NAME));
                                        break;
                        }
                }
        }

        @Override
        public void updateData(Object o) {
                
        }
}
