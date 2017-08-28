package com.example.cootek.newfastframe;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.commonlibrary.mvp.BaseActivity;
import com.example.commonlibrary.net.DownloadListener;
import com.example.commonlibrary.net.FileInfo;
import com.example.commonlibrary.net.NetManager;
import com.example.commonlibrary.utils.CommonLogger;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by COOTEK on 2017/8/23.
 */

public class DownLoadActivity extends BaseActivity {


    @BindView(R.id.tv_one)
    TextView tvOne;
    @BindView(R.id.pb_one)
    ProgressBar pbOne;
    @BindView(R.id.tv_pause_one)
    TextView tvPauseOne;
    @BindView(R.id.tv_two)
    TextView tvTwo;
    @BindView(R.id.pb_two)
    ProgressBar pbTwo;
    @BindView(R.id.tv_pause_two)
    TextView tvPauseTwo;
    @BindView(R.id.tv_three)
    TextView tvThree;
    @BindView(R.id.pb_three)
    ProgressBar pbThree;
    @BindView(R.id.tv_pause_three)
    TextView tvPauseThree;
    @BindView(R.id.tv_all)
    TextView tvAll;
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

    }

    @Override
    protected void initData() {
    }

    private void startDownLoad() {
        long[] data = MusicManager.getInstance().getQueue();
        if (data != null && data.length > 0) {
            list = new ArrayList<>();
            for (int i = 0; i < data.length; i++) {
                MusicPlayBean bean = MainApplication.getMainComponent().getDaoSession().getMusicPlayBeanDao().queryBuilder()
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


    @OnClick({R.id.tv_pause_one, R.id.tv_pause_two, R.id.tv_pause_three, R.id.tv_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_pause_one:
                NetManager.getInstance().stop(list.get(0).getSongUrl());
                break;
            case R.id.tv_pause_two:
                NetManager.getInstance().stop(list.get(1).getSongUrl());
                break;
            case R.id.tv_pause_three:
                NetManager.getInstance().stop(list.get(2).getSongUrl());
                break;
            case R.id.tv_all:
                CommonLogger.e("点击啦");
                startDownLoad();
                break;
        }
    }
}
