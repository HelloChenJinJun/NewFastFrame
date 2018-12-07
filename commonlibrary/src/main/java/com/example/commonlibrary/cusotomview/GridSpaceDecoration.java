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
        boolean hasFoot=false;
        if (parent instanceof SuperRecyclerView) {
            SuperRecyclerView superRecyclerView = (SuperRecyclerView) parent;
            if (superRecyclerView.getHeaderContainer().getChildCount() > position) {
                return;
            }
            position -= superRecyclerView.getHeaderContainer().getChildCount();
            hasFoot=superRecyclerView.getLoadMoreFooterView()!=null;
        }
        int column = position % spanCount; // item column
        outRect.top = spacing;
//        if (!hasFoot) {
        //            if (position==)
        //
        //        }
        if (column == spanCount - 1) {
            if (includeEdge) {
                outRect.right = spacing;
            }
            outRect.left = spacing / 2;
        } else if (column == 0) {
            if (includeEdge) {
                outRect.left = spacing;
            }
            outRect.right = spacing / 2;
        } else {
            outRect.left = spacing / 2;
            outRect.right = spacing / 2;
        }
    }
}
