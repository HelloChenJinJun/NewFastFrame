package com.example.commonlibrary.baseadapter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.commonlibrary.R;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.baseadapter.swipeview.OnSwipeMenuItemClickListener;
import com.example.commonlibrary.baseadapter.swipeview.SwipeMenu;
import com.example.commonlibrary.baseadapter.swipeview.SwipeMenuCreator;
import com.example.commonlibrary.baseadapter.swipeview.SwipeMenuLayout;
import com.example.commonlibrary.baseadapter.swipeview.SwipeMenuRecyclerView;
import com.example.commonlibrary.baseadapter.swipeview.SwipeMenuView;
import com.example.commonlibrary.utils.CommonLogger;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/23      8:39
 * QQ:             1981367757
 */

public abstract class BaseSwipeRecyclerAdapter<T, K extends BaseWrappedViewHolder> extends BaseRecyclerAdapter<T, K> {

    /**
     * Swipe menu creator。
     */
    private SwipeMenuCreator mSwipeMenuCreator;

    /**
     * Swipe menu click listener。
     */
    private OnSwipeMenuItemClickListener mSwipeMenuItemClickListener;

    /**
     * Set to create menu listener.
     *
     * @param swipeMenuCreator listener.
     */
    public void setSwipeMenuCreator(SwipeMenuCreator swipeMenuCreator) {
        this.mSwipeMenuCreator = swipeMenuCreator;
    }

    /**
     * Set to click menu listener.
     *
     * @param swipeMenuItemClickListener listener.
     */
    public void setSwipeMenuItemClickListener(OnSwipeMenuItemClickListener swipeMenuItemClickListener) {
        this.mSwipeMenuItemClickListener = swipeMenuItemClickListener;
    }




    @Override
    protected void convert(K holder, T data) {
        if (holder.itemView instanceof SwipeMenuLayout) {
            SwipeMenuLayout swipeMenuLayout = (SwipeMenuLayout) holder.itemView;
            int childCount = swipeMenuLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = swipeMenuLayout.getChildAt(i);
                if (childView instanceof SwipeMenuView) {
                    ((SwipeMenuView) childView).bindAdapterViewHolder(holder);
                }
            }
            convert(holder, data, true);
        } else {
            convert(holder, data, false);
        }
    }


    protected abstract void convert(K holder, T data, boolean isSwipeItem);


    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        K viewHolder = super.onCreateViewHolder(parent, viewType);
        View contentView = viewHolder.itemView;

        if (mSwipeMenuCreator != null && (viewType != HEADER && viewType != FOOTER && viewType != EMPTY)) {
            SwipeMenuLayout swipeMenuLayout = (SwipeMenuLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_item_layout, parent, false);
            SwipeMenu swipeLeftMenu = new SwipeMenu(swipeMenuLayout, viewType);
            SwipeMenu swipeRightMenu = new SwipeMenu(swipeMenuLayout, viewType);

            mSwipeMenuCreator.onCreateMenu(swipeLeftMenu, swipeRightMenu, viewType);

            int leftMenuCount = swipeLeftMenu.getMenuItems().size();
            if (leftMenuCount > 0) {
                SwipeMenuView swipeLeftMenuView = swipeMenuLayout.findViewById(R.id.swipe_left);
                swipeLeftMenuView.setOrientation(swipeLeftMenu.getOrientation());
                swipeLeftMenuView.bindMenu(swipeLeftMenu, SwipeMenuRecyclerView.LEFT_DIRECTION);
                swipeLeftMenuView.bindMenuItemClickListener(mSwipeMenuItemClickListener, swipeMenuLayout);
            }

            int rightMenuCount = swipeRightMenu.getMenuItems().size();
            if (rightMenuCount > 0) {
                SwipeMenuView swipeRightMenuView = (SwipeMenuView) swipeMenuLayout.findViewById(R.id.swipe_right);
                swipeRightMenuView.setOrientation(swipeRightMenu.getOrientation());
                swipeRightMenuView.bindMenu(swipeRightMenu, SwipeMenuRecyclerView.RIGHT_DIRECTION);
                swipeRightMenuView.bindMenuItemClickListener(mSwipeMenuItemClickListener, swipeMenuLayout);
            }

            if (leftMenuCount > 0 || rightMenuCount > 0) {
                ViewGroup viewGroup = swipeMenuLayout.findViewById(R.id.swipe_content);
                viewGroup.addView(contentView);
                return createBaseViewHolder(swipeMenuLayout);
            }
        }
        return viewHolder;
    }
}
