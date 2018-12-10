package com.example.commonlibrary.keeplive.onepix;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.commonlibrary.utils.CommonLogger;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/10     14:24
 */
public class OnePxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonLogger.e("一像素activity启动");
        Window mWindow = getWindow();
        mWindow.setGravity(Gravity.END | Gravity.TOP);
        WindowManager.LayoutParams attrParams = mWindow.getAttributes();
        attrParams.x = 0;
        attrParams.y = 0;
        attrParams.height = 1;
        attrParams.width = 1;
        mWindow.setAttributes(attrParams);
    }
}
