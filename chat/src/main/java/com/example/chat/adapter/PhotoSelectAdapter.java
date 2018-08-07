package com.example.chat.adapter;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.chat.R;
import com.example.chat.bean.ImageItem;
import com.example.chat.util.SystemUtil;
import com.example.commonlibrary.baseadapter.adapter.BaseMultipleRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.utils.ToastUtils;


/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/26     20:27
 * QQ:         1981367757
 */

public class PhotoSelectAdapter extends BaseMultipleRecyclerAdapter<ImageItem, BaseWrappedViewHolder> {
    @Override
    protected SparseIntArray getLayoutIdMap() {

        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(ImageItem.ITEM_CAMERA, R.layout.item_activity_photo_select_camera);
        sparseArray.put(ImageItem.ITEM_NORMAL, R.layout.item_activity_photo_select_normal);
        return sparseArray;
    }


    @Override
    public BaseWrappedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseWrappedViewHolder baseWrappedViewHolder = super.onCreateViewHolder(parent, viewType);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SystemUtil.getLayoutItemSize(parent.getContext(), 3, 0));
        baseWrappedViewHolder.itemView.setLayoutParams(params);
        return baseWrappedViewHolder;
    }
    
    
    
    private int selectedSize=0;


    public int getSelectedSize() {
        return selectedSize;
    }



    private boolean isOne;
    public void setOne(boolean isOne){
        this.isOne=isOne;
    }

    public boolean isOne() {
        return isOne;
    }

    public void setSelectedSize(int selectedSize) {
        this.selectedSize = selectedSize;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, ImageItem data) {
        if (data.getItemViewType() == ImageItem.ITEM_NORMAL) {
            holder.setImageUrl(R.id.iv_item_activity_photo_select_normal_image, data.getPath())
                    .setOnItemClickListener();

            CheckBox checkBox = ((CheckBox) holder.getView(R.id.cb_item_activity_photo_select_normal_check));
            if (isOne) {
                checkBox.setVisibility(View.GONE);
                return;
            }
            if (data.isCheck()) {
                holder.setVisible(R.id.view_item_activity_photo_select_normal_mask, true);
            } else {
                holder.setVisible(R.id.view_item_activity_photo_select_normal_mask, false);
            }
            checkBox.setChecked(data.isCheck());
            checkBox.setOnClickListener(view -> {
              
                CheckBox checkBox1 = (CheckBox) view;
                if (checkBox1.isChecked()) {
                    if (getSelectedSize() == 8) {
                        ToastUtils.showShortToast("最多只能选8张图片");
                        checkBox1.setChecked(false);
                        holder.setVisible(R.id.view_item_activity_photo_select_normal_mask, false);
                        return;
                    } else {
                        holder.setVisible(R.id.view_item_activity_photo_select_normal_mask, true);
                    }
                } else {
                    holder.setVisible(R.id.view_item_activity_photo_select_normal_mask, false);
                }
                getOnItemClickListener().onItemChildClick(holder.getAdapterPosition() - getItemUpCount()
                        , view, R.id.cb_item_activity_photo_select_normal_check);
            });
        } else {
            holder.setOnItemClickListener();
        }
    }
}
