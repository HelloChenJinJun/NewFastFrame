package com.example.cootek.newfastframe.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonlibrary.baseadapter.OnLoadMoreListener;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.skin.SkinManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.mvp.MainBaseActivity;
import com.example.cootek.newfastframe.slidingpanel.SlidingPanelLayout;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


/**
 * Created by COOTEK on 2017/8/7.
 */
@Route(path = "/video/main")
public class MainActivity extends MainBaseActivity implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.video_activity_main;
    }

    @Override
    protected void initView() {
        CommonLogger.e("1这里开始初始化fragment");
        addOrReplaceFragment(HolderFragment.newInstance(), R.id.fl_activity_main_container);
    }

    @Override
    protected void initData() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("music");

        RxBusManager.getInstance().registerEvent(ThemeEvent.class, new Consumer<ThemeEvent>() {
            @Override
            public void accept(@NonNull ThemeEvent themeEvent) throws Exception {
                CommonLogger.e("1接受到主题啦");
                setTheme(themeEvent.isNight() ? R.style.CustomTheme_Night : R.style.CustomTheme_Day);
                getSharedPreferences("theme", Context.MODE_PRIVATE).edit().putBoolean("isNight", themeEvent.isNight()).apply();
                CommonLogger.e("isTheme" + getSharedPreferences("theme", Context.MODE_PRIVATE).getBoolean("isTheme", false));
                SkinManager.getInstance().refreshSkin();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                CommonLogger.e("传递主题出错");
            }
        });

        setToolBar(toolBarOption);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu_layout, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.main_sub_item_equalizer) {
            try {
                final Intent effects = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                effects.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, MusicManager.getInstance().getAudioSessionId());
                startActivityForResult(effects, 666);
            } catch (final ActivityNotFoundException notFound) {
                Toast.makeText(this, "Equalizer not found", Toast.LENGTH_SHORT).show();
            }

        } else if (i == R.id.main_sub_item_setting) {
            SettingActivity.start(this);

        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void loadMore() {
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void updateData(Object o) {

    }


}
