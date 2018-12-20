package com.snew.video.manager;

import com.example.commonlibrary.utils.CommonLogger;
import com.snew.video.bean.VideoUpLoadDataBean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/20     16:21
 */
public class VideoUpLoadManager {
    private static VideoUpLoadManager sInstance;

    public static VideoUpLoadManager getInstance() {
        if (sInstance == null) {
            sInstance = new VideoUpLoadManager();
        }
        return sInstance;
    }

    public void uploadVideoBean(String url, String title, String playUrl) {
        BmobQuery<VideoUpLoadDataBean> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("title", title);
        bmobQuery.findObjects(new FindListener<VideoUpLoadDataBean>() {
            @Override
            public void done(List<VideoUpLoadDataBean> list, BmobException e) {
                if (e == null || e.getErrorCode() == 101) {
                    if (list != null && list.size() > 0) {
                        VideoUpLoadDataBean item = list.get(0);
                        if (!item.getPlayList().contains(playUrl)) {
                            item.getPlayList().add(playUrl);
                            item.update();
                        }
                    } else {
                        VideoUpLoadDataBean videoUpLoadDataBean = new VideoUpLoadDataBean();
                        videoUpLoadDataBean.setUrl(url);
                        List<String> stringList = new ArrayList<>();
                        stringList.add(playUrl);
                        videoUpLoadDataBean.setPlayList(stringList);
                        videoUpLoadDataBean.setTitle(title);
                        videoUpLoadDataBean.save();
                    }
                } else {
                    CommonLogger.e("bmob上传出错:" + e.getMessage());
                }
            }
        });
    }
}
