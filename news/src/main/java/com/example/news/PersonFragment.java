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
import com.example.commonlibrary.rxbus.event.LoginEvent;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.commonlibrary.rxbus.event.UserInfoEvent;
import com.example.news.dagger.person.DaggerPersonComponent;
import com.example.news.dagger.person.PersonModule;
import com.example.news.mvp.person.PersonPresenter;
import com.example.news.util.NewsUtil;

import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/21     14:28
 * QQ:         1981367757
 */

public class PersonFragment extends BaseFragment<Object, PersonPresenter> implements View.OnClickListener {
    private TextView signature;
    private RoundAngleImageView avatar;
    private RelativeLayout titleBg;
    private RelativeLayout settings, index, edit,notify;

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
        settings = (RelativeLayout) findViewById(R.id.rl_fragment_person_settings);
        edit = (RelativeLayout) findViewById(R.id.rl_fragment_person_edit);
        index = (RelativeLayout) findViewById(R.id.rl_fragment_person_index);
        notify= (RelativeLayout) findViewById(R.id.rl_fragment_person_notify);
        avatar.setOnClickListener(this);
        settings.setOnClickListener(this);
        edit.setOnClickListener(this);
        index.setOnClickListener(this);
        notify.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        DaggerPersonComponent.builder().newsComponent(NewsApplication.getNewsComponent())
                .personModule(new PersonModule(this))
                .build().inject(this);
        presenter.registerEvent(UserInfoEvent.class, new Consumer<UserInfoEvent>() {
            @Override
            public void accept(UserInfoEvent userInfoEvent) throws Exception {
                ToastUtils.showShortToast(userInfoEvent.toString());
                CommonLogger.e(userInfoEvent.toString());
                boolean isLogin = BaseApplication.getAppComponent()
                        .getSharedPreferences().getBoolean(ConstantUtil.LOGIN_STATUS, false);
                if (!isLogin) {
//                    注销账号通
                    ((MainActivity) getActivity()).notifyLoginStatus(isLogin);
                }
                updateUserInfo(userInfoEvent.getAvatar(), userInfoEvent.getNick(), userInfoEvent.getHalfBg());
            }
        });
        presenter.registerEvent(LoginEvent.class, new Consumer<LoginEvent>() {
            @Override
            public void accept(LoginEvent loginEvent) throws Exception {
                if (!loginEvent.isSuccess()) {
//                    ToastUtils.showShortToast(loginEvent.getErrorMessage());
                    NewsUtil.clearAllUserCache();
                    updateUserInfo(null, null, null);
//                    getActivity().finish();
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
        int id = view.getId();
        if (id == R.id.rl_fragment_person_settings) {
            if (BaseApplication.getAppComponent()
                    .getSharedPreferences()
                    .getBoolean(ConstantUtil.LOGIN_STATUS, false)) {
                Router.getInstance().deal(new RouterRequest.Builder().context(getActivity())
                        .provideName("chat").actionName("setting")
                        .build());
            } else {
                ToastUtils.showShortToast("请先登录~亲~~~");
            }
        } else if (id == R.id.rl_fragment_person_edit) {
            if (BaseApplication.getAppComponent()
                    .getSharedPreferences()
                    .getBoolean(ConstantUtil.LOGIN_STATUS, false)) {
                Router.getInstance().deal(new RouterRequest.Builder()
                        .provideName("chat").actionName("edit_user_info")
                        .context(getActivity()).build());
            } else {
                ToastUtils.showShortToast("请先登录~亲~~~");
            }
        } else if (id == R.id.rl_fragment_person_index) {
            if (BaseApplication.getAppComponent()
                    .getSharedPreferences()
                    .getBoolean(ConstantUtil.LOGIN_STATUS, false)) {
                Router.getInstance().deal(new RouterRequest.Builder()
                        .provideName("chat").actionName("user_index")
                        .context(getActivity()).build());
            } else {
                ToastUtils.showShortToast("请先登录~亲~~~");
            }
        } else if (id==R.id.rl_fragment_person_notify){
            Router.getInstance().deal(new RouterRequest.Builder().provideName("chat")
            .actionName("notify").context(getContext()).build());
        }else {
            if (BaseApplication.getAppComponent().getSharedPreferences().getBoolean(NewsUtil
                    .IS_LOGIN, false)) {

            } else {
                Map<String, Object> map = new HashMap<>();
                map.put(ConstantUtil.FROM, ConstantUtil.FROM_MAIN);
                Router.getInstance().deal(new RouterRequest.Builder().context(getActivity())
                        .provideName("chat").actionName("login").paramMap(map).build());
            }
        }
    }
}
