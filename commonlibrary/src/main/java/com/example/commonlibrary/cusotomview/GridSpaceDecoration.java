package com.example.commonlibrary.cusotomview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/29     16:27
 * QQ:         1981367757
 */

public class GridSpaceDecoration extends RecyclerView.ItemDecoration {


    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpaceDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column
        if (position>=spanCount) {
            outRect.top=spacing;
        }
        if (includeEdge) {
            if (column==spanCount-1) {
                outRect.right=spacing;
            }
            outRect.left=spacing;
        }else {
            if (column!=0) {
                outRect.left=spacing;
            }
        }
    }
}
