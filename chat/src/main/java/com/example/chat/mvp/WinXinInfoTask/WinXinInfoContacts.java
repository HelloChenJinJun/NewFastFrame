package com.example.chat.mvp.WinXinInfoTask;


import com.example.chat.MainRepositoryManager;
import com.example.chat.bean.WinXinBean;
import com.example.commonlibrary.mvp.model.BaseModel;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;

import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      0:16
 * QQ:             1981367757
 */

public interface WinXinInfoContacts {
        public interface View<T> extends IView<T> {
                void updateData(List<WinXinBean> data);
        }


        public abstract class Model extends BaseModel<MainRepositoryManager> {
                public Model(MainRepositoryManager repositoryManager) {
                        super(repositoryManager);
                }

                abstract boolean saveCacheWeiXinInfo(String key, String json);

                abstract List<WinXinBean> getCacheWeiXinInfo(String key);


                abstract boolean clearAllData();
        }

        abstract class Presenter extends BasePresenter<View, Model> {
                public Presenter(View iView, Model baseModel) {
                        super(iView, baseModel);
                }

                abstract public void getWinXinInfo(int page);
        }
}
