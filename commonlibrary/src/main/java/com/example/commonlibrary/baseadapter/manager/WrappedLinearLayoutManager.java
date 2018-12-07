package com.example.commonlibrary.baseadapter.manager;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by COOTEK on 2017/8/23.
 */

public class WrappedLinearLayoutManager extends LinearLayoutManager {
    public WrappedLinearLayoutManager(Context context) {
        super(context);
    }


    public WrappedLinearLayoutManager(Context context,int orientation){
        super(context,orientation,false);
    }

    public WrappedLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public WrappedLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
