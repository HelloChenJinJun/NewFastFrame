package com.example.chat.mvp.HappyContentInfoTask;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/8      20:29
 * QQ:             1981367757
 */

import com.example.chat.MainRepositoryManager;
import com.example.chat.bean.HappyContentBean;
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

public interface HappyContentContacts {


        public interface View extends IView<List<HappyContentBean>> {
//                void onUpdateHappyContentInfo(List<HappyContentBean> data);
        }


        public abstract class Model extends BaseModel<MainRepositoryManager> {
                public Model(MainRepositoryManager repositoryManager) {
                        super(repositoryManager);
                }

                public abstract boolean saveHappyContentInfo(String key, String json);

                public abstract List<HappyContentBean> getHappyContentInfo(String key);
                public abstract boolean clearAllCacheHappyContentData();
        }

        abstract class Presenter extends BasePresenter<View, Model> {
                public Presenter(View iView, Model baseModel) {
                        super(iView, baseModel);
                }

                abstract public void getHappyContentInfo(int page,boolean showLoading);
        }
}

