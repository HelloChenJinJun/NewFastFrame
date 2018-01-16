package com.example.chat.mvp.notify;

import com.example.chat.bean.SystemNotifyBean;
import com.example.chat.mvp.ChatBasePresenter;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.view.IView;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/16     14:35
 * QQ:         1981367757
 */

public class SystemNotifyPresenter extends ChatBasePresenter<IView<List<SystemNotifyBean>>,SystemNotifyModel> {
    public SystemNotifyPresenter(IView<List<SystemNotifyBean>> iView, SystemNotifyModel baseModel) {
        super(iView, baseModel);
    }

    public void getAllSystemNotifyData(final boolean isRefresh, final String time) {
        if (isRefresh) {
            iView.showLoading(null);
        }
        BmobQuery<SystemNotifyBean> bmobQuery=new BmobQuery<>();
        BmobDate bmobDate=new BmobDate(new Date(TimeUtil.getTime(time,"yyyy-MM-dd HH:mm:ss")));
        bmobQuery.addWhereGreaterThan("createAt",bmobDate);
        bmobQuery.order("-createdAt");
        bmobQuery.setLimit(10);
        addSubscription(bmobQuery.findObjects(new FindListener<SystemNotifyBean>() {
            @Override
            public void done(List<SystemNotifyBean> list, BmobException e) {
                if (e == null) {
                    iView.updateData(list);
                    iView.hideLoading();
                }else {
                    iView.showError(e.toString(), new EmptyLayout.OnRetryListener() {
                        @Override
                        public void onRetry() {
                            getAllSystemNotifyData(isRefresh, time);
                        }
                    });
                }
            }
        }));

    }
}
