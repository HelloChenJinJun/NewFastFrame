package com.example.music;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.example.chat.base.Constant;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.main.HomeFragment;
import com.example.chat.mvp.person.PersonFragment;
import com.example.chat.mvp.search.SearchActivity;
import com.example.chat.mvp.searchFriend.SearchFriendActivity;
import com.example.chat.mvp.settings.SettingsActivity;
import com.example.chat.mvp.shareinfo.ShareInfoFragment;
import com.example.chat.mvp.skin.SkinListActivity;
import com.example.chat.mvp.step.RecordStepActivity;
import com.example.chat.mvp.wallpaper.WallPaperActivity;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.bean.chat.SkinEntity;
import com.example.commonlibrary.net.download.DownloadListener;
import com.example.commonlibrary.net.download.FileInfo;
import com.example.commonlibrary.skin.SkinManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.StatusBarUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.news.CenterFragment;
import com.example.news.IndexFragment;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import io.reactivex.Observable;

public class MainActivity extends SlideBaseActivity {

    private List<Fragment> fragmentList;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getCurrentFragment() != null && getCurrentFragment() instanceof HomeFragment) {
            ((HomeFragment) getCurrentFragment()).notifyNewIntentCome(intent);
        }
    }


    @Override
    protected boolean needStatusPadding() {
        return false;
    }

    @Override
    public void updateData(Object o) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = (String) item.getTitle();
        switch (title) {
            case "搜索":
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case "添加好友":
                ToastUtils.showShortToast("点击了添加好友");
                SearchFriendActivity.start(this);
                break;
            case "建群":
                ToastUtils.showShortToast("创建群由于后台实时数据服务收费问题暂未开放");
                break;
            case "背景":
                ToastUtils.showShortToast("点击了背景");
                WallPaperActivity.start(this, Constant.WALLPAPER);
                break;
            case "设置":
                ToastUtils.showShortToast("点击了设置");
                SettingsActivity.start(this);
                break;
            case "重置皮肤":
               SkinEntity currentSkinEntity=UserDBManager.getInstance()
                       .getCurrentSkin();
               if (currentSkinEntity!=null){
                   currentSkinEntity.setHasSelected(false);
                   UserDBManager.getInstance().getDaoSession()
                           .getSkinEntityDao().update(currentSkinEntity);
                SkinManager.getInstance().update(null);
               }
                break;
            case "皮肤中心":
                SkinListActivity.start(this);
                break;
            case "计步器":
                RecordStepActivity.start(this);
            default:
                break;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu_layout, menu);
        return true;
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
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        RadioGroup bottomContainer = (RadioGroup) findViewById(R.id.rg_activity_main_bottom_container);
        bottomContainer.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_activity_main_bottom_index) {
                addOrReplaceFragment(fragmentList.get(0));
            } else if (checkedId == R.id.rb_activity_main_bottom_center) {
                addOrReplaceFragment(fragmentList.get(1));
            } else if (checkedId == R.id.rb_activity_main_bottom_person) {
                addOrReplaceFragment(fragmentList.get(2));
            } else if (checkedId == R.id.rb_activity_main_bottom_chat) {
                addOrReplaceFragment(fragmentList.get(3));
            } else if (checkedId == R.id.rb_activity_main_bottom_public) {
                addOrReplaceFragment(fragmentList.get(4));
            }
        });
    }

    @Override
    protected void initData() {
        fragmentList = new ArrayList<>();
        fragmentList.add(IndexFragment.newInstance());
        fragmentList.add(CenterFragment.newInstance());
        fragmentList.add(PersonFragment.newInstance());
        fragmentList.add(HomeFragment.newInstance());
        fragmentList.add(ShareInfoFragment.newInstance(UserManager
                .getInstance().getCurrentUserObjectId(), true));
        addOrReplaceFragment(fragmentList.get(3), R.id.fl_activity_main_container);
    }

    private long mExitTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            ToastUtils.showShortToast("再按一次退出程序");
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
