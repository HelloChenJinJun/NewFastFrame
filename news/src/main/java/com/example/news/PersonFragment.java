package com.example.news;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.commonlibrary.rxbus.event.UserInfoEvent;
import com.example.news.util.NewsUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/21     14:28
 * QQ:         1981367757
 */

public class PersonFragment extends BaseFragment implements View.OnClickListener {
    private TextView signature;
    private RoundAngleImageView avatar;
    private RelativeLayout titleBg;

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
        avatar.setOnClickListener(this);
        avatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                NewsUtil.clearAllUserCache();
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        RxBusManager.getInstance().registerEvent(UserInfoEvent.class, new Consumer<UserInfoEvent>() {
            @Override
            public void accept(UserInfoEvent userInfoEvent) throws Exception {
                ToastUtils.showShortToast(userInfoEvent.toString());
                CommonLogger.e(userInfoEvent.toString());
                updateUserInfo(userInfoEvent.getAvatar(), userInfoEvent.getNick(), userInfoEvent.getHalfBg());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (throwable != null) {
                    ToastUtils.showShortToast("请求异常" + throwable.getMessage());
                    CommonLogger.e(throwable);
                }
            }
        });
    }

    private void updateUserInfo(String avatar, String nick, String bg) {
        if (getContext() != null) {
            Glide.with(getContext()).load(avatar).placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round).into(this.avatar);
            signature.setText(nick);
            Glide.with(getContext()).load(bg).placeholder(R.drawable.cug_index)
                    .error(R.drawable.cug_index).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    titleBg.setBackground(resource);
                }
            });
        }
    }

    @Override
    protected void updateView() {
        boolean loginStatus = BaseApplication.getAppComponent().getSharedPreferences()
                .getBoolean(ConstantUtil.LOGIN_STATUS, false);
        if (loginStatus) {
            SharedPreferences sharedPreferences = BaseApplication.getAppComponent()
                    .getSharedPreferences();
            updateUserInfo(sharedPreferences.getString(ConstantUtil.AVATAR, null)
                    , sharedPreferences.getString(ConstantUtil.NICK, null)
                    , sharedPreferences.getString(ConstantUtil.BG_HALF, null));
        } else {
            ToastUtils.showShortToast("未登录状态，请及时登录");
        }
    }

    public static PersonFragment newInstance() {
        return new PersonFragment();
    }

    @Override
    public void onClick(View view) {
        if (BaseApplication.getAppComponent().getSharedPreferences().getBoolean(NewsUtil
                .IS_LOGIN, false)) {
            ToastUtils.showShortToast("已经登录");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put(ConstantUtil.FROM, ConstantUtil.FROM_MAIN);
            Router.getInstance().deal(new RouterRequest.Builder().context(getActivity())
                    .provideName("chat").actionName("login").paramMap(map).build());
        }
    }
}
