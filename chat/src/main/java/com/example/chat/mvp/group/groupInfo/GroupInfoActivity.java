package com.example.chat.mvp.group.groupInfo;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.events.GroupTableEvent;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.editInfo.EditUserInfoDetailActivity;
import com.example.chat.mvp.photoSelect.PhotoSelectActivity;
import com.example.chat.mvp.selectFriend.SelectedFriendsActivity;
import com.example.chat.util.SystemUtil;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.chat.GroupTableEntity;
import com.example.commonlibrary.bean.chat.UserEntity;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.imageloader.glide.GlideImageLoaderConfig;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/31      18:01
 * QQ:             1981367757
 */

public class GroupInfoActivity extends SlideBaseActivity<Object, GroupInfoPresenter> implements View.OnClickListener {

    private TextView notification;
    private TextView number;
    private RoundAngleImageView add, delete, groupAvatar,one, two,three;
    private Button exit;
    private TextView name;
    private TextView description;
    private TextView groupName, createTime;
    private GroupTableEntity groupTableEntity;
    private RelativeLayout avatarContainer, notificationContainer, groupNameContainer
            ,descriptionContainer;
    private CheckBox remind;


    @Override
    public void updateData(Object o) {
        finish();
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
    protected void initView() {
        name = (TextView) findViewById(R.id.tv_activity_group_info_name);
        groupName = (TextView) findViewById(R.id.tv_activity_group_info_group_name);
        createTime = (TextView) findViewById(R.id.tv_activity_group_info_create_time);
        notification = (TextView) findViewById(R.id.tv_activity_group_info_group_notification);
        number = (TextView) findViewById(R.id.tv_activity_group_info_group_number);
        one = (RoundAngleImageView) findViewById(R.id.riv_activity_group_info_number_avatar_one);
        two = (RoundAngleImageView) findViewById(R.id.riv_activity_group_info_number_avatar_two);
        three = (RoundAngleImageView) findViewById(R.id.riv_activity_group_info_number_avatar_three);
        groupAvatar = (RoundAngleImageView) findViewById(R.id.riv_activity_group_info_avatar);
        add = (RoundAngleImageView) findViewById(R.id.riv_activity_group_info_add);
        delete = (RoundAngleImageView) findViewById(R.id.riv_activity_group_info_delete);
        exit = (Button) findViewById(R.id.btn_activity_group_info_exit);
        remind= (CheckBox) findViewById(R.id.cb_activity_group_info_remind);
        description=(TextView)findViewById(R.id.tv_activity_group_info_group_description);
        descriptionContainer=(RelativeLayout)findViewById(R.id.rl_activity_group_info_description);
        groupNameContainer = (RelativeLayout) findViewById(R.id.rl_activity_group_info_group_name);
        notificationContainer = (RelativeLayout) findViewById(R.id.rl_activity_group_info_notification);
        avatarContainer = (RelativeLayout) findViewById(R.id.rl_activity_group_info_header);
        exit.setOnClickListener(this);
        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        groupNameContainer.setOnClickListener(this);
        avatarContainer.setOnClickListener(this);
        descriptionContainer.setOnClickListener(this);
        remind.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            MsgManager.getInstance().updateGroupMessage(groupTableEntity
//                    .getGroupId(), Constant.GROUP_REMIND, isChecked ? "true" : "false", new UpdateListener() {
//                @Override
//                public void done(BmobException e) {
//                    if (e != null) {
//                        ToastUtils.showShortToast("更新失败"+e.toString());
//                    }
//                }
//            });
        });


        findViewById(R.id.ll_activity_group_info_number_container).setOnClickListener(this);
        notificationContainer.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        groupTableEntity = UserDBManager.getInstance().getGroupTableEntity(getIntent().getStringExtra(Constant.GROUP_ID));
        if (!groupTableEntity.getCreatorId().equals(UserManager.getInstance().getCurrentUserObjectId())) {
            delete.setVisibility(View.GONE);
            groupNameContainer.setClickable(false);
            avatarContainer.setClickable(false);
            notificationContainer.setClickable(false);
            descriptionContainer.setClickable(false);
        }
        name.setText(groupTableEntity.getGroupName());
        groupName.setText(groupTableEntity.getGroupName());
        description.setText(groupTableEntity.getGroupDescription());
        notification.setText(groupTableEntity.getNotification());
        createTime.setText(TimeUtil.getTime(groupTableEntity.getCreatedTime()));
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(groupTableEntity.getGroupNumber().size())
                .append("位成员");
        number.setText(stringBuilder.toString());
        remind.setChecked(groupTableEntity.getIsRemind());
        BaseApplication
                .getAppComponent()
                .getImageLoader().loadImage(this,new GlideImageLoaderConfig
        .Builder().url(groupTableEntity.getGroupAvatar()).imageView(groupAvatar).build());
        String oneUrl=UserDBManager.getInstance().getUser(groupTableEntity.getGroupNumber().get(0))
                .getAvatar();
        BaseApplication
                .getAppComponent()
                .getImageLoader().loadImage(this,new GlideImageLoaderConfig
                .Builder().url(oneUrl).imageView(one).build());
        String twoUrl=UserDBManager.getInstance().getUser(groupTableEntity.getGroupNumber().get(1))
                .getAvatar();
        BaseApplication
                .getAppComponent()
                .getImageLoader().loadImage(this,new GlideImageLoaderConfig
                .Builder().url(twoUrl).imageView(two).build());
        String threeUrl=UserDBManager.getInstance().getUser(groupTableEntity.getGroupNumber().get(2))
                .getAvatar();
        BaseApplication
                .getAppComponent()
                .getImageLoader().loadImage(this,new GlideImageLoaderConfig
                .Builder().url(threeUrl).imageView(three).build());
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id==R.id.rl_activity_group_info_header){
            PhotoSelectActivity.start(this,null,true,true,null);
        } else if (id == R.id.rl_activity_group_info_group_name) {
            EditUserInfoDetailActivity.start(this,groupTableEntity.getGroupId(),Constant.GROUP_NAME,groupTableEntity.getGroupName()
            ,Constant.REQUEST_CODE_GROUP_NAME);
        }else if (id==R.id.rl_activity_group_info_description){
            EditUserInfoDetailActivity.start(this,groupTableEntity.getGroupId(),Constant.GROUP_DESCRIPTION,groupTableEntity.getGroupName()
                    ,Constant.REQUEST_CODE_GROUP_DESCRIPTION);
        } else if (id == R.id.rl_activity_group_info_notification) {
            EditUserInfoDetailActivity.start(this,groupTableEntity.getGroupId(),Constant.GROUP_NOTIFICATION,groupTableEntity.getGroupName()
                    ,Constant.REQUEST_CODE_GROUP_NOTIFICATION);
        } else if (id == R.id.btn_activity_group_info_exit) {
            presenter.exitGroup(groupTableEntity.getGroupId(),UserManager.getInstance()
            .getCurrentUserObjectId());
        } else if (id == R.id.riv_activity_group_info_add) {
            ArrayList<UserEntity>  list=new ArrayList<>();
            list.addAll(UserDBManager.getInstance()
                    .getAllFriend());
            for (String uid :
                    groupTableEntity.getGroupNumber()) {
                UserEntity item = new UserEntity();
                item.setUid(uid);
                if (list.contains(item)){
                    list.remove(item);
                }
            }
            SelectedFriendsActivity.start(this,Constant.FROM_GROUP_INFO,list,Constant.REQUEST_CODE_ADD_GROUP_NUMBER);
        } else if (id == R.id.riv_activity_group_info_delete) {
            ArrayList<UserEntity>  list=new ArrayList<>();
            for (int i = 0; i < groupTableEntity.getGroupNumber().size(); i++) {
                if (!groupTableEntity.getGroupNumber().get(i).equals(UserManager.getInstance().getCurrentUserObjectId())) {
                    list.add(UserDBManager.getInstance().getUser(groupTableEntity.getGroupNumber().get(i)));
                }
            }
            SelectedFriendsActivity.start(this,Constant.FROM_GROUP_INFO,list,Constant.REQUEST_CODE_DELETE_GROUP_NUMBER);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK){
            switch (requestCode){
                case Constant.REQUEST_CODE_ADD_GROUP_NUMBER:
                    ArrayList<String>  list= (ArrayList<String>) data.getSerializableExtra(Constant.DATA);
                    break;
                case Constant.REQUEST_CODE_DELETE_GROUP_NUMBER:
                    break;
                case Constant.REQUEST_CODE_GROUP_NOTIFICATION:
                    String content=data.getStringExtra(Constant.DATA);
                    notification.setText(content);
                    groupTableEntity.setNotification(content);
                    RxBusManager.getInstance().post(new GroupTableEvent(groupTableEntity.getGroupId()
                    ,GroupTableEvent.TYPE_GROUP_NOTIFICATION,content));
              break;
                case Constant.REQUEST_CODE_GROUP_DESCRIPTION:
                    String str=data.getStringExtra(Constant.DATA);
                    description.setText(str);
                    groupTableEntity.setGroupDescription(str);
                    RxBusManager.getInstance().post(new GroupTableEvent(groupTableEntity.getGroupId()
                            ,GroupTableEvent.TYPE_GROUP_DESCRIPTION,str));
                    break;
                case ConstantUtil.REQUEST_CODE_ONE_PHOTO:
                    try {
                        showLoadDialog("正在上传头像，请稍候........");
                        BmobFile bmobFile = new BmobFile(new File(new URI(data.getStringExtra(ConstantUtil.PATH))));
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    MsgManager.getInstance().updateGroupMessage(groupTableEntity.getGroupId(),Constant.GROUP_AVATAR,data
                                    .getStringExtra(ConstantUtil.PATH),new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            dismissLoadDialog();
                                            if (e == null) {
                                                CommonLogger.e("更新用户头像成功");
                                                groupTableEntity.setGroupAvatar(bmobFile.getFileUrl());
                                                BaseApplication
                                                        .getAppComponent()
                                                        .getImageLoader().loadImage(GroupInfoActivity.this,new GlideImageLoaderConfig
                                                        .Builder().url(bmobFile.getFileUrl()).imageView(groupAvatar).build());
                                                RxBusManager.getInstance().post(new GroupTableEvent(groupTableEntity.getGroupId()
                                                        ,GroupTableEvent.TYPE_GROUP_AVATAR,bmobFile.getFileUrl()));
                                            } else {
                                                CommonLogger.e("更新用户头像失败" + e.toString());
                                            }
                                        }


                                    });
                                } else {
                                    dismissLoadDialog();
                                    CommonLogger.e("加载失败");
                                }
                            }

                        });
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

            }
        }
    }

    public static void start(Activity activity, String groupId) {
        Intent intent=new Intent(activity,GroupInfoActivity.class);
        intent.putExtra(Constant.GROUP_ID,groupId);
        activity.startActivity(intent);
    }
}
