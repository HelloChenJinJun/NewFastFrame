package com.example.chat.mvp.account.password;

import com.example.chat.base.Constant;
import com.example.chat.bean.User;
import com.example.chat.manager.UserManager;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.rxbus.event.PwChangeEvent;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/13     23:46
 * QQ:         1981367757
 */

public class PasswordChangePresenter extends RxBasePresenter<IView<Object>, DefaultModel> {
    public PasswordChangePresenter(final IView<Object> iView, DefaultModel baseModel) {
        super(iView, baseModel);
        registerEvent(PwChangeEvent.class, pwChangeEvent -> {
            if (pwChangeEvent.isSuccess()) {
                ToastUtils.showShortToast("修改密码一成功" + pwChangeEvent.toString());
                ChangePw(pwChangeEvent);
            } else {
                iView.showError(pwChangeEvent.getErrorMsg(), null);
            }
        });
    }

    private void ChangePw(final PwChangeEvent pwChangeEvent) {
        User user = new User();
        user.setPassword(pwChangeEvent.getNews());
        user.setPw(pwChangeEvent.getNews());
        user.update(UserManager.getInstance().getCurrentUserObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    BaseApplication
                            .getAppComponent().getSharedPreferences().edit().putString(ConstantUtil.PASSWORD, pwChangeEvent.getNews())
                            .putString(UserManager.getInstance().getCurrentUser().getUsername(),null)
                            .apply();
                    iView.updateData(pwChangeEvent);
                    iView.hideLoading();
                } else {
                    BaseApplication
                            .getAppComponent().getSharedPreferences()
                            .edit().putString(UserManager
                            .getInstance().getCurrentUser().getUsername(), pwChangeEvent.getNews()).apply();
                    ToastUtils.showShortToast("bmob修改密码失败" + e.toString());
                    iView.showError(e.toString(), null);
                }
            }
        });


//      todo  bmob上修改密码
    }

    public void resetPassword(String old, String news) {
        iView.showLoading("修改中..........");
        Map<String, Object> map = new HashMap<>();
        map.put(ConstantUtil.PASSWORD_OLD, old);
        map.put(ConstantUtil.PASSWORD_NEW, news);
        Router.getInstance().deal(new RouterRequest.Builder().provideName("news")
                .actionName("pw_change")
                .paramMap(map).build());
    }
}
