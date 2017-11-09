package com.example.cootek.newfastframe.ui;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.bean.music.MusicPlayBeanDao;
import com.example.commonlibrary.net.NetManager;
import com.example.commonlibrary.net.download.DownloadListener;
import com.example.commonlibrary.net.download.FileInfo;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.VideoApplication;

import java.util.ArrayList;

/**
 * Created by COOTEK on 2017/8/23.
 */
@Route(path = "/video/download")
public class DownLoadActivity extends BaseActivity implements View.OnClickListener {


    private TextView tvOne;
    private ProgressBar pbOne;
    private TextView tvPauseOne;
    private TextView tvTwo;
    private ProgressBar pbTwo;
    private TextView tvPauseTwo;
    private TextView tvThree;
    private ProgressBar pbThree;
    private TextView tvPauseThree;
    private TextView tvAll;
    private ArrayList<MusicPlayBean> list;

    @Override
    public void updateData(Object o) {

    }

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_download;
    }

    @Override
    protected void initView() {
        tvOne = (TextView) findViewById(R.id.tv_one);
        pbOne = (ProgressBar) findViewById(R.id.pb_one);
        tvPauseOne = (TextView) findViewById(R.id.tv_pause_one);
        tvTwo = (TextView) findViewById(R.id.tv_two);
        pbTwo = (ProgressBar) findViewById(R.id.pb_two);
        tvPauseTwo = (TextView) findViewById(R.id.tv_pause_two);
        tvThree = (TextView) findViewById(R.id.tv_three);
        pbThree = (ProgressBar) findViewById(R.id.pb_three);
        tvPauseThree = (TextView) findViewById(R.id.tv_pause_three);
        tvAll = (TextView) findViewById(R.id.tv_all);
        tvOne.setOnClickListener(this);
        tvTwo.setOnClickListener(this);
        tvThree.setOnClickListener(this);
    }

    @Override
    protected void initData() {
    }

    private void startDownLoad() {
        long[] data = MusicManager.getInstance().getQueue();
        if (data != null && data.length > 0) {
            list = new ArrayList<>();
            for (int i = 0; i < data.length; i++) {
                MusicPlayBean bean = VideoApplication.getMainComponent().getDaoSession().getMusicPlayBeanDao().queryBuilder()
                        .where(MusicPlayBeanDao.Properties.SongId.eq(data[i])).build().list().get(0);

                list.add(bean);
            }
        }
        for (int i = 0; i < 3; i++) {
            downLoad(list.get(i), i);
        }
    }

    private void downLoad(MusicPlayBean bean, final int i) {
        CommonLogger.e("开始下载啦啦啦");
        NetManager.getInstance().downLoad(bean.getSongUrl(), new DownloadListener() {
            @Override
            public void onStart(FileInfo fileInfo) {
                CommonLogger.e("第" + i + "个" + "开始下载");
                if (i == 0) {
                    pbOne.setMax(fileInfo.getTotalBytes());
                }
            }

            @Override
            public void onUpdate(FileInfo fileInfo) {
                if (i == 0) {
                    if (pbOne.getMax() == 0) {
                        pbOne.setMax(fileInfo.getTotalBytes());
                    }
                    pbOne.setProgress(fileInfo.getLoadBytes());
                } else if (i == 1) {
                    if (pbTwo.getMax() == 0) {
                        pbTwo.setMax(fileInfo.getTotalBytes());
                    }
                    pbTwo.setProgress(fileInfo.getLoadBytes());
                } else {
                    if (pbThree.getMax() == 0) {
                        pbThree.setMax(fileInfo.getTotalBytes());
                    }
                    pbThree.setProgress(fileInfo.getLoadBytes());
                }
            }

            @Override
            public void onStop(FileInfo fileInfo) {
                CommonLogger.e("停止");
            }

            @Override
            public void onComplete(FileInfo fileInfo) {
                CommonLogger.e("完成" + fileInfo.getPath() + fileInfo.getName());
            }

            @Override
            public void onCancel(FileInfo FileInfo) {
                CommonLogger.e("取消");
            }

            @Override
            public void onError(FileInfo fileInfo, String errorMsg) {
                CommonLogger.e("出错" + errorMsg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_pause_one) {
            NetManager.getInstance().stop(list.get(0).getSongUrl());
        } else if (i == R.id.tv_pause_two) {
            NetManager.getInstance().stop(list.get(1).getSongUrl());
        } else if (i == R.id.tv_pause_three) {
            NetManager.getInstance().stop(list.get(2).getSongUrl());
        } else if (i == R.id.tv_all) {
            CommonLogger.e("点击啦");
            startDownLoad();

        }
    }
}
