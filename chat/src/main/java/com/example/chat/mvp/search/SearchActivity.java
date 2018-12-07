package com.example.chat.mvp.search;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.chat.R;
import com.example.chat.base.ChatBaseActivity;
import com.example.chat.util.CommonUtils;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.ToastUtils;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import static android.view.View.GONE;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/27      22:14
 * QQ:             1981367757
 */

public class SearchActivity extends ChatBaseActivity {
    private SearchView searchView;
    private SuperRecyclerView display;


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
        return R.layout.activity_search;
    }


    @Override
    public void initView() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setNeedNavigation(false);
        setToolBar(toolBarOption);
        display = (SuperRecyclerView) findViewById(R.id.rcv_search_display);
        display.setVisibility(GONE);
        findViewById(R.id.ll_search_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return true;
            }
        });
    }

    @Override
    protected void initData() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu_layout, menu);
        final MenuItem menuItem = menu.findItem(R.id.search_action_item);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MenuItemCompat.expandActionView(menuItem);
            }
        });
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                ToastUtils.showShortToast("蔓延");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                ToastUtils.showShortToast("收缩");
                finish();
                return true;
            }
        });
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                CommonUtils.hideSoftInput(SearchActivity.this, searchView);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ToastUtils.showShortToast(newText);
                return true;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateData(Object o) {

    }
}
