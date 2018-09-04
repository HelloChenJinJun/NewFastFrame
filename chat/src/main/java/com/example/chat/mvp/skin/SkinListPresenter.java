package com.example.chat.mvp.skin;

import com.example.chat.base.AppBasePresenter;
import com.example.chat.bean.SkinBean;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserDBManager;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.bean.chat.SkinEntity;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/22     23:48
 */

public class SkinListPresenter extends AppBasePresenter<IView<List<SkinEntity>>,DefaultModel>{

    private int page;

    public SkinListPresenter(IView<List<SkinEntity>> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void getSkinData(boolean isRefresh) {
        if (isRefresh) {
            iView.showLoading(null);
            page=0;
        }
        page++;
        BmobQuery<SkinBean> skinBeanBmobQuery=new BmobQuery<>();
        skinBeanBmobQuery.setLimit(10);
        skinBeanBmobQuery.setSkip((page-1)*10);
        skinBeanBmobQuery.order("-createdAt");
        addSubscription(skinBeanBmobQuery.findObjects(new FindListener<SkinBean>() {
            @Override
            public void done(List<SkinBean> list, BmobException e) {
                List<SkinEntity>  skinEntityList=null;
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        skinEntityList=new ArrayList<>();
                        for (SkinBean bean :
                                list) {
                            skinEntityList.add(MsgManager.getInstance().cover(bean));
                        }
                    }
                }else {
                    page--;
                    ToastUtils.showShortToast("缓存数据获取");
                    CommonLogger.e("查询服务器上皮肤数据出错"+e.toString());
                    if (isRefresh) {
                        skinEntityList= UserDBManager
                              .getInstance().getSkinList();
                    }
                }
                iView.updateData(skinEntityList);
                iView.hideLoading();
            }
        }));
    }
}
