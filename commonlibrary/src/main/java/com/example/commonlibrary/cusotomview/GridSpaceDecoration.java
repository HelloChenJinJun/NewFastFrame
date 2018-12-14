package com.example.commonlibrary.cusotomview;

import android.graphics.Rect;
import android.view.View;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/29     16:27
 * QQ:         1981367757
 */

public class GridSpaceDecoration extends RecyclerView.ItemDecoration {


    private int spanCount;
    private boolean includeEdge;
    private int horizontalSize;
    private int verticalSize;

    public GridSpaceDecoration(int spanCount, int spacing, boolean includeEdge) {
        this(spanCount, spacing, spacing, includeEdge);
    }


    public GridSpaceDecoration(int spanCount, int horizontalSize, int verticalSize, boolean includeEdge) {
        this.spanCount = spanCount;
        this.includeEdge = includeEdge;
        this.horizontalSize = horizontalSize;
        this.verticalSize = verticalSize;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        if (parent instanceof SuperRecyclerView) {
            SuperRecyclerView superRecyclerView = (SuperRecyclerView) parent;
            if (superRecyclerView.getHeaderContainer().getChildCount() > position) {
                return;
            }
            position -= superRecyclerView.getHeaderContainer().getChildCount();
        }
        int column = position % spanCount; // item column
        outRect.top = verticalSize;

        if (column == spanCount - 1) {
            if (includeEdge) {
                outRect.right = horizontalSize;
            }
            outRect.left = horizontalSize / 2;
        } else if (column == 0) {
            if (includeEdge) {
                outRect.left = horizontalSize;
            }
            outRect.right = horizontalSize / 2;
        } else {
            outRect.left = horizontalSize / 2;
            outRect.right = horizontalSize / 2;
        }
    }
}
