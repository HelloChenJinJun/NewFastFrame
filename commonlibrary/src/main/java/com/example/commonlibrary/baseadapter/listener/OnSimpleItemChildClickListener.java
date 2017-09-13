package com.example.commonlibrary.baseadapter.listener;

import android.view.View;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;

/**
 * Created by COOTEK on 2017/8/1.
 */

public abstract class OnSimpleItemChildClickListener implements BaseRecyclerAdapter.OnItemClickListener {
    @Override
    public void onItemClick(int position, View view) {

    }

    @Override
    public boolean onItemLongClick(int position, View view) {
        return false;
    }

    @Override
    public boolean onItemChildLongClick(int position, View view, int id) {
        return false;
    }


}
