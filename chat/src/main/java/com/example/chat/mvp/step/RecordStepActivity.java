package com.example.chat.mvp.step;

import android.app.Activity;
import android.content.Intent;

import com.example.chat.R;
import com.example.chat.base.MainBaseActivity;
import com.example.chat.events.StepEvent;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserDBManager;
import com.example.chat.util.TimeUtil;
import com.example.chat.view.StepArcView;
import com.example.commonlibrary.bean.chat.StepData;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.rxbus.RxBusManager;


/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/6/18     10:56
 * QQ:1981367757
 */

public class RecordStepActivity extends MainBaseActivity {


    private StepArcView stepArcView;

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
        return R.layout.activity_record_step;
    }

    @Override
    protected void initView() {
        stepArcView = (StepArcView) findViewById(R.id.sav_activity_record_view_display);
    }

    @Override
    protected void initData() {
            addDisposable(RxBusManager.getInstance().registerEvent(StepEvent.class
                    , stepEvent -> stepArcView.setCurrentCount(10000,stepEvent.getStepCount())));
        StepData stepData= UserDBManager.getInstance().getStepData(TimeUtil
        .getTime(System.currentTimeMillis(),"yyyy-MM-dd"));
        if (stepData != null) {
            stepArcView.setCurrentCount(10000,stepData.getStepCount());
        }else {
            stepArcView.setCurrentCount(10000,0);
        }
        ToolBarOption toolBarOption=new ToolBarOption();
        toolBarOption.setTitle("计步器");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }


    @Override
    protected void onStop() {
        super.onStop();
        MsgManager.getInstance().saveCurrentStep(stepArcView.getStepNumber());
    }

    public static void start(Activity activity){
        Intent intent=new Intent(activity,RecordStepActivity.class);
        activity.startActivity(intent);
    }
}
