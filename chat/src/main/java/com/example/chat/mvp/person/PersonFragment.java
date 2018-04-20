package com.example.chat.mvp.person;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.chat.R;
import com.example.chat.base.AppBaseFragment;
import com.example.chat.base.Constant;
import com.example.chat.bean.User;
import com.example.chat.dagger.person.DaggerPersonComponent;
import com.example.chat.dagger.person.PersonModule;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.UserDetail.UserDetailActivity;
import com.example.chat.mvp.editInfo.EditUserInfoActivity;
import com.example.chat.mvp.notify.SystemNotifyActivity;
import com.example.chat.mvp.photoSelect.PhotoSelectActivity;
import com.example.chat.mvp.settings.SettingsActivity;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.utils.ConstantUtil;

import java.util.HashMap;
import java.util.Map;

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
    protected void initView() {
        signature = (TextView) findViewById(R.id.tv_fragment_person_signature);
        avatar = (RoundAngleImageView) findViewById(R.id.riv_fragment_person_avatar);
        titleBg = (RelativeLayout) findViewById(R.id.rl_fragment_person_title_bg);
        RelativeLayout settings = (RelativeLayout) findViewById(R.id.rl_fragment_person_settings);
        RelativeLayout edit = (RelativeLayout) findViewById(R.id.rl_fragment_person_edit);
        RelativeLayout index = (RelativeLayout) findViewById(R.id.rl_fragment_person_index);
        RelativeLayout notify = (RelativeLayout) findViewById(R.id.rl_fragment_person_notify);
        settings.setOnClickListener(this);
        edit.setOnClickListener(this);
        index.setOnClickListener(this);
        notify.setOnClickListener(this);
        titleBg.setOnLongClickListener(v -> {
            PhotoSelectActivity.start(getActivity(), Constant.TITLE_WALLPAPER,true,false,null);
            return true;
        });
        avatar.setOnLongClickListener(v -> {
            PhotoSelectActivity.start(getActivity(),Constant.AVATAR,true,false,null);
            return true;
        });
    }

    @Override
    protected void initData() {
        DaggerPersonComponent.builder().chatMainComponent(getMainComponent())
                .personModule(new PersonModule(this))
                .build().inject(this);
        presenter.registerEvent(User.class, user -> {
            PersonFragment.this.user=user;
            updateUserInfo();
        });
        user= UserManager.getInstance().getCurrentUser();
    }

    private void updateUserInfo() {
        if (getContext() != null) {
            Glide.with(getContext()).load(avatar).placeholder(R.mipmap.ic_launcher)
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
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case ConstantUtil.REQUEST_CODE_ONE_PHOTO:
                    String path=data.getStringExtra(ConstantUtil.PATH);
                    String from=data.getStringExtra(Constant.FROM);
//                    todo  4.21

            }

        }

    }
}
