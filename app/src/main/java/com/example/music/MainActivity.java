package com.example.music;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ToastUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void jump(View view) {
        Toast.makeText(this, "升级", Toast.LENGTH_SHORT).show();
        ARouter.getInstance().build("/video/main").navigation();

    }


    public void crash(View view) {
        ToastUtils.showShortToast("这是基线版本的补丁+陈锦军");
    }
}
