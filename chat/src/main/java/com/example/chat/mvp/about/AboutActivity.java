package com.example.chat.mvp.about;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.view.View;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.base.ChatBaseActivity;
import com.example.chat.mvp.developer.DeveloperInfoActivity;
import com.example.commonlibrary.customview.ToolBarOption;
import com.example.commonlibrary.utils.AppUtil;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/28     11:32
 */
public class AboutActivity extends ChatBaseActivity implements View.OnClickListener {


    private TextView version;
    private TextView name;
    private TextView desc;
    private TextView versionDesc;


    public static void start(Activity activity) {
        Intent intent = new Intent(activity, AboutActivity.class);
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
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        name = findViewById(R.id.tv_activity_about_name);
        version = findViewById(R.id.tv_activity_about_version);
        desc = findViewById(R.id.tv_activity_about_des);
        versionDesc = findViewById(R.id.tv_activity_about_version_desc);
        findViewById(R.id.rl_activity_about_protocol).setOnClickListener(this);
        findViewById(R.id.rl_activity_about_policy).setOnClickListener(this);
        findViewById(R.id.rl_activity_about_encourage).setOnClickListener(this);
        findViewById(R.id.rl_activity_about_check_up_grade).setOnClickListener(this);
        findViewById(R.id.rl_activity_about_developer_info).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        PackageInfo packageInfo = AppUtil.getPackageInfo();
        version.setText(packageInfo.versionName);
        desc.setText("倾、听、看");
        name.setText("简倾");
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setTitle("关于简倾");
        setToolBar(toolBarOption);
        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
        if (upgradeInfo != null) {
            versionDesc.setText("立即更新");
        } else {
            versionDesc.setText("已是最新版本");
        }
    }

    @Override
    public void updateData(Object o) {

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_activity_about_protocol) {
            //                ProtocolActivity.start(this);
        } else if (id == R.id.rl_activity_about_policy) {
        } else if (id == R.id.rl_activity_about_encourage) {
        } else if (id == R.id.rl_activity_about_check_up_grade) {
            Beta.checkUpgrade();
        }else if (id==R.id.rl_activity_about_developer_info){
            DeveloperInfoActivity.start(this);
        }
    }
}
