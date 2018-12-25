package com.example.chat.mvp.person;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.base.AppBaseFragment;
import com.example.chat.base.ConstantUtil;
import com.example.chat.dagger.person.DaggerPersonComponent;
import com.example.chat.dagger.person.PersonModule;
import com.example.chat.events.UnReadSystemNotifyEvent;
import com.example.chat.events.UserInfoUpdateEvent;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.UserDetail.UserDetailActivity;
import com.example.chat.mvp.commentnotify.CommentNotifyActivity;
import com.example.chat.mvp.editInfo.EditUserInfoActivity;
import com.example.chat.mvp.feedback.ChatListActivity;
import com.example.chat.mvp.notify.SystemNotifyActivity;
import com.example.chat.mvp.photoSelect.PhotoSelectActivity;
import com.example.chat.mvp.settings.SettingsActivity;
import com.example.commonlibrary.bean.chat.User;
import com.example.commonlibrary.customview.RoundAngleImageView;
import com.example.commonlibrary.imageloader.glide.GlideImageLoaderConfig;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.SystemUtil;

import java.io.File;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/21     14:28
 * QQ:         1981367757
 */

public class PersonFragment extends AppBaseFragment<Object, PersonPresenter> implements View.OnClickListener {
    private TextView signature;
    private RoundAngleImageView avatar;
    private ImageView titleBg;
    private User user;
    private TextView systemCount;

    @Override
    public void updateData(Object o) {

    }

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
        return R.layout.fragment_person;
    }


    @Override
    protected boolean needStatusPadding() {
        return false;
    }

    @Override
    protected void initView() {
        signature = findViewById(R.id.tv_fragment_person_signature);
        avatar = findViewById(R.id.riv_fragment_person_avatar);
        titleBg = findViewById(R.id.iv_fragment_person_title_bg);
        RelativeLayout settings = findViewById(R.id.rl_fragment_person_settings);
        RelativeLayout edit = findViewById(R.id.rl_fragment_person_edit);
        RelativeLayout index = findViewById(R.id.rl_fragment_person_index);
        RelativeLayout notify = findViewById(R.id.rl_fragment_person_notify);
        systemCount = findViewById(R.id.tv_fragment_person_system_notify_count);
        findViewById(R.id.rl_fragment_person_comment).setOnClickListener(this);
        settings.setOnClickListener(this);
        edit.setOnClickListener(this);
        index.setOnClickListener(this);
        notify.setOnClickListener(this);
        findViewById(R.id.rl_fragment_person_feedback).setOnClickListener(this);
        titleBg.setOnLongClickListener(v -> {
            PhotoSelectActivity.start(this, ConstantUtil.TITLE_WALLPAPER, true, false, null);
            return true;
        });
        avatar.setOnLongClickListener(v -> {
            PhotoSelectActivity.start(this, ConstantUtil.AVATAR, true, false, null);
            return true;
        });
    }

    @Override
    protected void initData() {
        DaggerPersonComponent.builder().chatMainComponent(getMainComponent())
                .personModule(new PersonModule(this))
                .build().inject(this);
        presenter.registerEvent(UnReadSystemNotifyEvent.class, unReadSystemNotifyEvent -> updateSystemNotifyCount());
        presenter.registerEvent(User.class, user -> {
            PersonFragment.this.user = user;
            updateUserInfo();
        });
        user = UserManager.getInstance().getCurrentUser();
    }

    private void updateUserInfo() {
        if (getContext() != null) {
            getAppComponent()
                    .getImageLoader()
                    .loadImage(getContext(), GlideImageLoaderConfig
                            .newBuild().imageView(avatar).url(user.getAvatar()).placeHolderResId(R.mipmap.ic_launcher).build());
            signature.setText(user.getSignature());
            getAppComponent()
                    .getImageLoader()
                    .loadImage(getContext(), GlideImageLoaderConfig
                            .newBuild().imageView(titleBg).url(user.getTitleWallPaper()).build());
        }
    }

    @Override
    protected void updateView() {
        updateUserInfo();
        updateSystemNotifyCount();
    }

    private void updateSystemNotifyCount() {
        long count = UserDBManager.getInstance().getUnReadSystemNotifyCount();
        if (count > 0) {
            systemCount.setVisibility(View.VISIBLE);
            systemCount.setText(count + "");
        } else {
            systemCount.setVisibility(View.GONE);
        }
    }

    public static PersonFragment newInstance() {
        return new PersonFragment();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rl_fragment_person_settings) {
            SettingsActivity.start(getActivity());
        } else if (id == R.id.rl_fragment_person_edit) {
            EditUserInfoActivity.start(getActivity(), UserManager.getInstance().getCurrentUserObjectId());
        } else if (id == R.id.rl_fragment_person_index) {
            UserDetailActivity.start(getActivity(), UserManager.getInstance().getCurrentUserObjectId(),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), Pair.create(avatar, "avatar")
                            ,Pair.create(signature,"signature")
            ));
        } else if (id == R.id.rl_fragment_person_notify) {
            SystemNotifyActivity.start(getActivity());
        } else if (id == R.id.rl_fragment_person_comment) {
            CommentNotifyActivity.start(getActivity(), null);
        } else if (id == R.id.rl_fragment_person_feedback) {
            ChatListActivity.start(getActivity());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SystemUtil.REQUEST_CODE_ONE_PHOTO:
                    String path = data.getStringExtra(ConstantUtil.PATH);
                    String from = data.getStringExtra(ConstantUtil.FROM);
                    showLoadDialog("正在上传图片中，请稍候........");
                    // todo path is not absolute
                    BmobFile bmobFile = new BmobFile(new File(path));
                    bmobFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                UserManager.getInstance().updateUserInfo(from, bmobFile.getFileUrl(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        dismissLoadDialog();
                                        if (e == null) {
                                            CommonLogger.e("更新用户信息成功");
                                            if (from.equals(ConstantUtil.TITLE_WALLPAPER)) {
                                                getAppComponent()
                                                        .getImageLoader()
                                                        .loadImage(getContext(), GlideImageLoaderConfig
                                                                .newBuild().imageView(titleBg).url(bmobFile.getFileUrl()).build());
                                            } else {
                                                getAppComponent()
                                                        .getImageLoader()
                                                        .loadImage(getContext(), GlideImageLoaderConfig
                                                                .newBuild().imageView(avatar).url(bmobFile.getFileUrl()).build());
                                            }
                                            RxBusManager.getInstance().post(new UserInfoUpdateEvent());
                                        } else {
                                            CommonLogger.e("更新用户信息失败" + e.toString());
                                        }
                                    }


                                });
                            } else {
                                dismissLoadDialog();
                                CommonLogger.e("加载失败");
                            }
                        }

                    });


            }

        }

    }
}
