package com.example.chat.mvp.HappyInfoTask;


import com.example.chat.MainRepositoryManager;
import com.example.chat.bean.HappyBean;
import com.example.commonlibrary.mvp.model.BaseModel;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;

import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      19:12
 * QQ:             1981367757
 */

public interface HappyContacts {


        public interface View<T> extends IView<T> {
                void onUpdateHappyInfo(List<HappyBean> data);
        }


        public abstract class  Model extends BaseModel<MainRepositoryManager>{
                public Model(MainRepositoryManager repositoryManager) {
                        super(repositoryManager);
                }

                abstract boolean saveHappyInfo(String key, String json);
                abstract List<HappyBean> getHappyInfo(String key);
                abstract boolean clearAllCacheHappyData();
        }

        abstract class Presenter extends BasePresenter<View, Model> {
                public Presenter(View iView, Model baseModel) {
                        super(iView, baseModel);
                }

                abstract public void getHappyInfo(int page,boolean showLoading);
        }
}
