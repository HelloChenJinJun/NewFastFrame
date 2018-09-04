package com.example.chat.mvp.notify;

import com.example.chat.base.AppBasePresenter;
import com.example.chat.bean.SystemNotifyBean;
import com.example.chat.manager.UserDBManager;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.bean.chat.SystemNotifyEntity;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.view.IView;

import java.util.ArrayList;
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

public class SystemNotifyPresenter extends AppBasePresenter<IView<List<SystemNotifyBean>>,DefaultModel> {
    public SystemNotifyPresenter(IView<List<SystemNotifyBean>> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void getAllSystemNotifyData(final boolean isRefresh, final String time) {
        if (isRefresh) {
            iView.showLoading(null);
        }
        BmobQuery<SystemNotifyBean> bmobQuery=new BmobQuery<>();
        BmobDate bmobDate=new BmobDate(new Date(TimeUtil.getTime(time,"yyyy-MM-dd HH:mm:ss")));
        bmobQuery.addWhereGreaterThan("createdAt",bmobDate);
        bmobQuery.order("-createdAt");
        addSubscription(bmobQuery.findObjects(new FindListener<SystemNotifyBean>() {
            @Override
            public void done(List<SystemNotifyBean> list, BmobException e) {
                if (e == null) {
                    iView.updateData(list);
                    if (list!=null&&list.size()>0) {
                        List<SystemNotifyEntity>  list1=new ArrayList<>(list.size());
                        for (SystemNotifyBean item :
                                list) {
                            SystemNotifyEntity entity = new SystemNotifyEntity();
                            entity.setId(item.getObjectId());
                            entity.setContentUrl(item.getContentUrl());
                            entity.setImageUrl(item.getImageUrl());
                            entity.setReadStatus(item.getReadStatus());
                            entity.setSubTitle(item.getSubTitle());
                            entity.setTitle(item.getTitle());
                            list1.add(entity);
                        }
                        UserDBManager.getInstance().addOrUpdateSystemNotify(list1);
                    }
                    iView.hideLoading();
                }else {
                    iView.showError(e.toString(), () -> getAllSystemNotifyData(isRefresh, time));
                }
            }
        }));

    }
}
