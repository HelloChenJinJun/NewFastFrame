package com.example.commonlibrary.baseadapter.manager;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by COOTEK on 2017/9/1.
 */

public class WrappedGridLayoutManager extends GridLayoutManager {
    public WrappedGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public WrappedGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public WrappedGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException |IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
