package com.example.chat.mvp.wallpaper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.chat.R;
import com.example.chat.adapter.WallPaperAdapter;
import com.example.chat.base.Constant;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserManager;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.mvp.main.HomeActivity;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.rxbus.RxBusManager;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/2/26      20:59
 * QQ:             1981367757
 */

public class WallPaperActivity extends SlideBaseActivity {
    private RecyclerView display;
    private WallPaperAdapter adapter;
    private String selectedImage;
    private String from;

    private int prePosition = -1;


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
        return R.layout.activity_wallpaper;
    }


    @Override
    public void initView() {
        display = (RecyclerView) findViewById(R.id.rcv_wall_paper_display);
    }


    @Override
    public void initData() {
        from = getIntent().getStringExtra(Constant.FROM);
        showLoadDialog("1正在加载背景图片.........");
        if (from.equals(Constant.TITLE_WALLPAPER)) {
            MsgManager.getInstance().getAllDefaultTitleWallPaperFromServer(new FindListener<String>() {
                @Override
                public void done(List<String> list, BmobException e) {
                    dismissLoadDialog();
                    if (e == null) {
                        initAdapter(list);
                    } else {
                        LogUtil.e("加载背景图片信息失败" + e.toString());
                    }
                }
            });
        } else {
            MsgManager.getInstance().getAllDefaultWallPaperFromServer(new FindListener<String>() {
                @Override
                public void done(List<String> list, BmobException e) {
                    dismissLoadDialog();
                    if (e == null) {
                        initAdapter(list);
                    } else {
                        LogUtil.e("加载背景图片信息失败" + e.toString());
                    }
                }
            });
        }
        initActionBar();
    }

    private void initAdapter(List<String> list) {
        display.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new WallPaperAdapter();
        int i = 0;
        for (String url :
                list) {
            if (url.equals(UserManager.getInstance().getCurrentUser().getTitleWallPaper())) {
                adapter.setSelectedPosition(i);
                prePosition = i;
            }
            i++;
        }
        adapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (position != prePosition) {
                    selectedImage = adapter.getData(position);
                    ((BaseWrappedViewHolder) display.findViewHolderForAdapterPosition(position)).setImageResource(R.id.iv_wallpaper_item_display, R.drawable.change_background_picture_btn);
                    if (prePosition != -1) {
                        ((BaseWrappedViewHolder) display.findViewHolderForAdapterPosition(prePosition)).setImageResource(R.id.iv_wallpaper_item_display, 0);
                    }
                }
                prePosition = position;
            }
        });
        display.setAdapter(adapter);
        adapter.addData(list);
    }

    private void initActionBar() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setRightText("完成");
        toolBarOption.setTitle("选择背景图片");
        toolBarOption.setAvatar(UserManager.getInstance().getCurrentUser().getAvatar());
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setRightListener(v -> {
            if (selectedImage != null && !selectedImage.equals(UserManager.getInstance().getCurrentUser()
                    .getTitleWallPaper())) {
                UserManager.getInstance().updateUserInfo(from, selectedImage, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            RxBusManager.getInstance().post(UserManager.getInstance().cover(UserManager.getInstance().getCurrentUser()));
                        } else {
                            LogUtil.e("上传背景图片到服务器上失败" + e.toString());
                        }
                        finish();
                    }
                });
            }

        });
        setToolBar(toolBarOption);
    }


    @Override
    public void updateData(Object o) {

    }

    public static void start(Activity activity, String from) {
        Intent intent = new Intent(activity, WallPaperActivity.class);
        intent.putExtra(Constant.FROM, from);
        activity.startActivity(intent);
    }
}
