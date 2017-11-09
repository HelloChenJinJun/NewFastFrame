package com.example.cootek.newfastframe.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.skin.SkinManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.ui.fragment.HolderFragment;
import com.example.cootek.newfastframe.view.OnDragDeltaChangeListener;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


/**
 * Created by COOTEK on 2017/8/7.
 */
@Route(path = "/music/main")
public class MainActivity extends MusicBaseActivity implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, OnDragDeltaChangeListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
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
        addOrReplaceFragment(HolderFragment.newInstance(), R.id.fl_activity_main_container);
        dragLayout.setListener(this);
    }

    @Override
    protected void initData() {
        RxBusManager.getInstance().registerEvent(ThemeEvent.class, new Consumer<ThemeEvent>() {
            @Override
            public void accept(@NonNull ThemeEvent themeEvent) throws Exception {
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
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.main_menu_layout, menu);
//        return true;
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int i = item.getItemId();
//        if (i == R.id.main_sub_item_equalizer) {
//            try {
//                final Intent effects = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
//                effects.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, MusicManager.getInstance().getAudioSessionId());
//                startActivityForResult(effects, 666);
//            } catch (final ActivityNotFoundException notFound) {
//                Toast.makeText(this, "Equalizer not found", Toast.LENGTH_SHORT).show();
//            }
//
//        } else if (i == R.id.main_sub_item_setting) {
//            SettingActivity.start(this);
//
//        }
//        return true;
//    }

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


    public void switchMenu() {
        dragLayout.switchMenu();
    }


    public void notifyIntercept(boolean isIntercept) {
        if (dragLayout != null) {
            dragLayout.setIntercept(isIntercept);
        }
    }

    @Override
    public void onDrag(View view, float delta) {
        ((HolderFragment) currentFragment).onDrag(delta);
    }

    @Override
    public void onCloseMenu() {

    }

    @Override
    public void onOpenMenu() {

    }
}
