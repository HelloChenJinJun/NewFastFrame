package com.example.chat.mvp.about.protocol;

import android.app.Activity;
import android.content.Intent;

import com.example.chat.R;
import com.example.chat.base.ChatBaseActivity;
import com.example.commonlibrary.customview.ToolBarOption;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2019/1/3     10:06
 */
public class ProtocolActivity extends ChatBaseActivity {
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ProtocolActivity.class);
        activity.startActivity(intent);
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
        return R.layout.activity_protocol;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("用户协议");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }

    @Override
    public void updateData(Object o) {

    }
}
