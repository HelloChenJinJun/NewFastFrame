package com.example.chat.mvp.skin.skinDetail;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.manager.UserDBManager;
import com.example.commonlibrary.baseadapter.adapter.CommonPagerAdapter;
import com.example.commonlibrary.bean.chat.SkinEntity;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.cusotomview.WrappedViewPager;
import com.example.commonlibrary.net.NetManager;
import com.example.commonlibrary.net.download.DownloadListener;
import com.example.commonlibrary.net.download.FileInfo;
import com.example.commonlibrary.skin.SkinManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;



/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/23     11:03
 */

public class SkinDetailActivity extends SlideBaseActivity implements View.OnClickListener {
    private WrappedViewPager display;
    private Button downLoad;
    private SkinEntity skinEntity;
    private TextView title;

    @Override
    public void updateData(Object o) {

    }

    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_skin_detail;
    }

    @Override
    protected void initView() {
        display = (WrappedViewPager) findViewById(R.id.vp_activity_skin_detail_display);
        downLoad= (Button) findViewById(R.id.btn_activity_skin_detail_download);
        title= (TextView) findViewById(R.id.tv_activity_skin_detail_title);
        downLoad.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        skinEntity =getIntent().getParcelableExtra(Constant.DATA);
        title.setText(skinEntity.getTitle());
        if (skinEntity.isHasSelected()) {
            downLoad.setText("使用中");
            downLoad.setEnabled(false);
        }else {
            if (skinEntity.getPath() != null) {
                downLoad.setText("使用");
            }else {
                downLoad.setText("下载并使用");
            }
        }
        CommonPagerAdapter commonPagerAdapter = new CommonPagerAdapter(getImageViewList());
        display.setAdapter(commonPagerAdapter);
        ToolBarOption toolBarOption=new ToolBarOption();
        toolBarOption.setTitle("皮肤详情");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }

    private List<View> getImageViewList() {
        List<View> list = new ArrayList<>();
        for (String url :
                skinEntity.getImageList()) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Glide.with(this).load(url).into(imageView);
            list.add(imageView);
        }
        return list;
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if (id == R.id.btn_activity_skin_detail_download) {
            showLoading("正在使用中........");
            if (skinEntity.getPath()== null) {
                NetManager.getInstance().downLoad(skinEntity.getUrl(), new DownloadListener() {
                    @Override
                    public void onStart(FileInfo fileInfo) {

                    }

                    @Override
                    public void onUpdate(FileInfo fileInfo) {

                    }

                    @Override
                    public void onStop(FileInfo fileInfo) {

                    }

                    @Override
                    public void onComplete(FileInfo fileInfo) {
                        skinEntity.setPath(fileInfo.getPath()+fileInfo.getName());
                        UserDBManager.getInstance().getDaoSession()
                                .getSkinEntityDao().update(skinEntity);
                        updateSkin();
                    }

                    @Override
                    public void onCancel(FileInfo fileInfo) {
                          hideLoading();
                    }

                    @Override
                    public void onError(FileInfo fileInfo, String errorMsg) {
                        hideLoading();
                        ToastUtils.showShortToast(errorMsg);
                    }
                });
            }else {
                updateSkin();
            }
        }
    }

    private void updateSkin() {
        SkinManager.getInstance().updateSkin(skinEntity.getPath(), exception -> {
            hideLoading();
            if (exception == null) {
                ToastUtils.showShortToast("更新皮肤成功");
                downLoad.setEnabled(false);
                downLoad.setText("使用中");
                skinEntity.setHasSelected(true);
               SkinEntity currentSkin= UserDBManager.getInstance().getCurrentSkin();
                if (currentSkin != null) {
                    currentSkin.setHasSelected(false);
                    UserDBManager.getInstance().getDaoSession().getSkinEntityDao()
                            .update(currentSkin);
                }
                UserDBManager.getInstance().getDaoSession().getSkinEntityDao()
                        .update(skinEntity);
                setResult(Activity.RESULT_OK);

            }else {
                ToastUtils.showShortToast("更新皮肤失败"+exception.toString());
                CommonLogger.e("更新皮肤失败"+exception.toString());
            }
        });
    }


    public static void start(Activity activity,SkinEntity skinEntity){
        Intent intent=new Intent(activity,SkinDetailActivity.class);
        intent.putExtra(Constant.DATA,skinEntity);
        activity.startActivityForResult(intent,10);
    }
}
