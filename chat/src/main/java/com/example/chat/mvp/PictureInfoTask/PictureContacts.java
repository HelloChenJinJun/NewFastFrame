package com.example.chat.mvp.PictureInfoTask;


import com.example.chat.MainRepositoryManager;
import com.example.chat.bean.PictureBean;
import com.example.commonlibrary.mvp.model.BaseModel;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;

import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/9      0:08
 * QQ:             1981367757
 */

public interface PictureContacts {
        public interface View<T> extends IView<T> {
                void onUpdatePictureInfo(List<PictureBean> data);
        }


        public abstract class Model extends BaseModel<MainRepositoryManager> {
                public Model(MainRepositoryManager repositoryManager) {
                        super(repositoryManager);
                }

                abstract boolean savePictureInfo(String key, String json);

                abstract List<PictureBean> getPictureInfo(String key);

                abstract boolean clearAllCacheData();
        }

        abstract class Presenter extends BasePresenter<View, Model> {
                public Presenter(View iView, Model baseModel) {
                        super(iView, baseModel);
                }

                abstract public void getPictureInfo(int page,boolean showLoading);
        }
}
