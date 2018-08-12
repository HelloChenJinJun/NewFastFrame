package com.example.chat.mvp.person;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.chat.R;
import com.example.chat.base.AppBaseFragment;
import com.example.chat.base.Constant;
import com.example.chat.bean.User;
import com.example.chat.dagger.person.DaggerPersonComponent;
import com.example.chat.dagger.person.PersonModule;
import com.example.chat.events.UnReadSystemNotifyEvent;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.UserDetail.UserDetailActivity;
import com.example.chat.mvp.commentnotify.CommentNotifyActivity;
import com.example.chat.mvp.editInfo.EditUserInfoActivity;
import com.example.chat.mvp.notify.SystemNotifyActivity;
import com.example.chat.mvp.photoSelect.PhotoSelectActivity;
import com.example.chat.mvp.settings.SettingsActivity;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import io.reactivex.functions.Consumer;


/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/21     14:28
 * QQ:         1981367757
 */

public class PersonFragment extends AppBaseFragment<Object, PersonPresenter> implements View.OnClickListener {
    private TextView signature;
    private RoundAngleImageView avatar;
    private RelativeLayout titleBg;
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
        signature = (TextView) findViewById(R.id.tv_fragment_person_signature);
        avatar = (RoundAngleImageView) findViewById(R.id.riv_fragment_person_avatar);
        titleBg = (RelativeLayout) findViewById(R.id.rl_fragment_person_title_bg);
        RelativeLayout settings = (RelativeLayout) findViewById(R.id.rl_fragment_person_settings);
        RelativeLayout edit = (RelativeLayout) findViewById(R.id.rl_fragment_person_edit);
        RelativeLayout index = (RelativeLayout) findViewById(R.id.rl_fragment_person_index);
        RelativeLayout notify = (RelativeLayout) findViewById(R.id.rl_fragment_person_notify);
        systemCount= (TextView) findViewById(R.id.tv_fragment_person_system_notify_count);
        findViewById(R.id.rl_fragment_person_comment).setOnClickListener(this);
        settings.setOnClickListener(this);
        edit.setOnClickListener(this);
        index.setOnClickListener(this);
        notify.setOnClickListener(this);
        titleBg.setOnLongClickListener(v -> {
            PhotoSelectActivity.start(this, Constant.TITLE_WALLPAPER,true,false,null);
            return true;
        });
        avatar.setOnLongClickListener(v -> {
            PhotoSelectActivity.start(this,Constant.AVATAR,true,false,null);
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
            PersonFragment.this.user=user;
            updateUserInfo();
        });
        user= UserManager.getInstance().getCurrentUser();
    }

    private void updateUserInfo() {
        if (getContext() != null) {
            Glide.with(getContext()).load(user.getAvatar()).placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher).into(this.avatar);
            signature.setText(user.getSignature());
            Glide.with(getContext()).load(user.getTitleWallPaper()).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    titleBg.setBackground(resource);
                }
            });
        }
    }

    @Override
    protected void updateView() {
        updateUserInfo();
       updateSystemNotifyCount();
    }

    private void updateSystemNotifyCount() {
        long count=UserDBManager.getInstance().getUnReadSystemNotifyCount();
        if (count > 0) {
            systemCount.setVisibility(View.VISIBLE);
            systemCount.setText(count+"");
        }else {
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
            EditUserInfoActivity.start(getActivity(),UserManager.getInstance().getCurrentUserObjectId());
        } else if (id == R.id.rl_fragment_person_index) {
            UserDetailActivity.start(getActivity(),UserManager.getInstance().getCurrentUserObjectId());
        } else if (id == R.id.rl_fragment_person_notify) {
            SystemNotifyActivity.start(getActivity());
        }else if (id==R.id.rl_fragment_person_comment){
            CommentNotifyActivity.start(getActivity(),null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case ConstantUtil.REQUEST_CODE_ONE_PHOTO:
                    String path=data.getStringExtra(ConstantUtil.PATH);
                    String from=data.getStringExtra(Constant.FROM);
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
                                            if (from.equals(Constant.TITLE_WALLPAPER)) {
                                                Glide.with(PersonFragment.this).load(bmobFile.getFileUrl()).into(new SimpleTarget<GlideDrawable>() {
                                                    @Override
                                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                                        titleBg.setBackground(resource);
                                                    }
                                                });
                                            }else {
                                                Glide.with(PersonFragment.this).load(bmobFile.getFileUrl())
                                                        .into(avatar);
                                            }
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
