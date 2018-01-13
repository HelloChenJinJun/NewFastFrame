package com.example.chat.mvp.account.password;

import com.example.chat.base.Constant;
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

import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/13     23:46
 * QQ:         1981367757
 */

public class PasswordChangePresenter extends RxBasePresenter<IView<Object>,PasswordChangeModel> {
    public PasswordChangePresenter(IView<Object> iView, PasswordChangeModel baseModel) {
        super(iView, baseModel);
        registerEvent(PwChangeEvent.class, new Consumer<PwChangeEvent>() {
            @Override
            public void accept(PwChangeEvent pwChangeEvent) throws Exception {
                ToastUtils.showShortToast("修改密码一成功"+pwChangeEvent.toString());
                ChangePw(pwChangeEvent);

            }
        });
    }

    private void ChangePw(PwChangeEvent pwChangeEvent) {

//      todo  bmob上修改密码
    }

    public void resetPassword(String old, String news) {
        Map<String,Object>  map=new HashMap<>();
        map.put(ConstantUtil.PASSWORD_OLD,old);
        map.put(ConstantUtil.PASSWORD_NEW,news);
        Router.getInstance().deal(new RouterRequest.Builder().provideName("chat")
        .actionName("pw_change")
        .paramMap(map).build());
        PwChangeEvent pwChangeEvent=new PwChangeEvent();
        pwChangeEvent.setOld(old);
        pwChangeEvent.setNews(news);
        pwChangeEvent.setSuccess(false);
        RxBusManager.getInstance().post(pwChangeEvent);
    }
}
