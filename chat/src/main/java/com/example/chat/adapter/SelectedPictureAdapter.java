package com.example.chat.adapter;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.chat.R;
import com.example.chat.base.CommonImageLoader;
import com.example.chat.bean.ImageItem;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.baseadapter.adapter.BaseMultipleRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

import java.util.List;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/30      14:01
 * QQ:             1981367757
 */

public class SelectedPictureAdapter extends BaseMultipleRecyclerAdapter<ImageItem, BaseWrappedViewHolder> {


        public SelectedPictureAdapter(List<ImageItem> data, int layoutId) {
                super(data, layoutId);
        }


        public SelectedPictureAdapter() {
                this(null, 0);
        }


        public interface OnItemCheckClickListener {
                public void onItemCheck(CheckBox checkBox, int position);

                public void onItemClick(View view, int position);
        }


        private OnItemCheckClickListener mOnItemCheckClickListener;


        public void setOnItemCheckClickListener(OnItemCheckClickListener onItemCheckClickListener) {
                mOnItemCheckClickListener = onItemCheckClickListener;
        }

        public SelectedPictureAdapter(List<ImageItem> list) {
                this(list, 0);
        }

        @Override
        protected SparseArray<Integer> getLayoutIdMap() {
                SparseArray<Integer> integerSparseArray = new SparseArray<>();
                integerSparseArray.put(ImageItem.ITEM_CAMERA, R.layout.select_picture_item);
                integerSparseArray.put(ImageItem.ITEM_NORMAL, R.layout.select_picute_list_item);
                return integerSparseArray;
        }


        @Override
        public BaseWrappedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                BaseWrappedViewHolder baseWrappedViewHolder = super.onCreateViewHolder(parent, viewType);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtils.getLayoutItemSize(parent.getContext(), 3, 0));
                baseWrappedViewHolder.itemView.setLayoutParams(params);
                return baseWrappedViewHolder;
        }

        @Override
        protected void convert(final BaseWrappedViewHolder holder, ImageItem data) {
                if (holder.getItemViewType() == ImageItem.ITEM_NORMAL) {
                        holder.setOnClickListener(R.id.iv_select_picture_list_item_display, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        if (mOnItemCheckClickListener != null) {
                                                mOnItemCheckClickListener.onItemClick(v, holder.getAdapterPosition());
                                        }
                                }
                        });
                        (holder.getView(R.id.cb_select_picture_list_item_select)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        LogUtil.e("这里1122呢");
                                        int size = CommonImageLoader.getInstance().getMaxSelectedCount();
                                        int selectedCount = CommonImageLoader.getInstance().getSelectedImages().size();
                                        CheckBox checkBox = (CheckBox) v;
                                        if (checkBox.isChecked()) {
                                                LogUtil.e("已选择");
                                                if (selectedCount >= size) {
                                                        Toast.makeText(checkBox.getContext(), "最多只能选择" + size + "张图片", Toast.LENGTH_SHORT).show();
                                                        checkBox.setChecked(false);
                                                        return;
                                                } else {
                                                        holder.setVisible(R.id.view_select_picture_list_item_mask, true);
                                                        CommonImageLoader.getInstance().getSelectedImages().add(CommonImageLoader.getInstance().getCurrentImageFolder().getAllImages().get((holder.getAdapterPosition() - 1)));
                                                }
                                        } else {
                                                LogUtil.e("未选择");
                                                holder.setVisible(R.id.view_select_picture_list_item_mask, false);
                                                CommonImageLoader.getInstance().getSelectedImages().remove(CommonImageLoader.getInstance().getCurrentImageFolder().getAllImages().get((holder.getAdapterPosition() - 1)));
                                        }
                                        if (mOnItemCheckClickListener != null) {
                                                mOnItemCheckClickListener.onItemCheck(checkBox, holder.getAdapterPosition());
                                        }
                                }
                        });
                        holder.setImageUrl(R.id.iv_select_picture_list_item_display, data.getPath());
                        if (CommonImageLoader.getInstance().getSelectedImages().contains(data)) {
                                holder.setVisible(R.id.view_select_picture_list_item_mask, true);
                                ((CheckBox) holder.getView(R.id.cb_select_picture_list_item_select)).setChecked(true);
                        } else {
                                holder.setVisible(R.id.view_select_picture_list_item_mask, false);
                                ((CheckBox) holder.getView(R.id.cb_select_picture_list_item_select)).setChecked(false);
                        }
                } else if (holder.getItemViewType() == ImageItem.ITEM_CAMERA) {
                        holder.setOnClickListener(R.id.rl_select_picture_item_container, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        if (mOnItemCheckClickListener != null) {
                                                mOnItemCheckClickListener.onItemClick(v, holder.getAdapterPosition());
                                        }
                                }
                        });
                }
        }


//        private OnSelectedPictureItemClickListener mOnSelectedPictureItemClickListener;
//
//        private int itemSize;
//
//        public void setOnSelectedPictureItemClickListener(OnSelectedPictureItemClickListener onSelectedPictureItemClickListener) {
//                mOnSelectedPictureItemClickListener = onSelectedPictureItemClickListener;
//        }
//
//        public void bindData(ArrayList<ImageItem> allImages) {
//                LogUtil.e("绑定数据bindData");
//                if (allImages == null) {
//                        return;
//                }
//                data.clear();
//                data = allImages;
//                notifyDataSetChanged();
//        }
//
//        public ImageItem getItemFromPosition(int position) {
//                if (position != 0) {
//                        return data.get(position);
//                } else {
//                        return null;
//                }
//        }
//
//
//        private Context mContext;
//
//        public interface OnSelectedPictureItemClickListener {
//                void onImageClick(View view, int position);
//
//                void onCheckClick(CheckBox checkBox, int position);
//        }
//
//        public SelectedPictureAdapter(Context context, IMultipleItem<ImageItem> iMultipleItem, List<ImageItem> data, int layoutItemSize) {
//                super(context, iMultipleItem, data);
//                this.itemSize = layoutItemSize;
//                mContext = context;
//                LogUtil.e("获取item大小为" + itemSize);
//        }
//
//
//        @Override
//        public BaseRecyclerHolder<ImageItem> onCreateViewHolder(ViewGroup parent, int viewType) {
//                LogUtil.e("onCreateViewHolder");
//                BaseRecyclerHolder<ImageItem> baseRecyclerHolder = super.onCreateViewHolder(parent, viewType);
//                ViewGroup.LayoutParams params = baseRecyclerHolder.itemView.getLayoutParams();
//                params.height = itemSize;
//                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                baseRecyclerHolder.itemView.setLayoutParams(params);
//                return baseRecyclerHolder;
//        }
//
//        @Override
//        boolean hasMultiLayout() {
//                return true;
//        }
//
//        @Override
//        protected void bindViewData(ImageItem data, final BaseRecyclerHolder<ImageItem> baseRecyclerHolder, final int position) {
//                if (getItemViewType(position) == Constant.CAMERA_LAYOUT) {
//                        View view = baseRecyclerHolder.getView(R.id.rl_select_picture_item_container);
//                        view.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                        if (mOnSelectedPictureItemClickListener != null) {
//                                                mOnSelectedPictureItemClickListener.onImageClick(v, position);
//                                        }
//                                }
//                        });
//                } else {
//                        CommonImageLoader.getInstance().getImageLoader().displayImage(mContext, data.getPath(), (ImageView) baseRecyclerHolder.getView(R.id.iv_select_picture_list_item_display), itemSize, itemSize);
//                        if (CommonImageLoader.getInstance().getSelectedImages().contains(data)) {
//                                baseRecyclerHolder.setVisible(R.id.view_select_picture_list_item_mask, true);
//                                ((CheckBox) baseRecyclerHolder.getView(R.id.cb_select_picture_list_item_select)).setChecked(true);
//                        } else {
//                                baseRecyclerHolder.setVisible(R.id.view_select_picture_list_item_mask, false);
//                                ((CheckBox) baseRecyclerHolder.getView(R.id.cb_select_picture_list_item_select)).setChecked(false);
//                        }
//                        baseRecyclerHolder.setOnClickListener(R.id.iv_select_picture_list_item_display, new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                        if (mOnSelectedPictureItemClickListener != null) {
//                                                mOnSelectedPictureItemClickListener.onImageClick(v, position);
//                                        }
//                                }
//                        });
//                        baseRecyclerHolder.setOnClickListener(R.id.cb_select_picture_list_item_select, new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                        if (mOnSelectedPictureItemClickListener != null) {
//                                                int size = CommonImageLoader.getInstance().getMaxSelectedCount();
//                                                int selectedCount = CommonImageLoader.getInstance().getSelectedImages().size();
//                                                CheckBox checkBox = (CheckBox) v;
//                                                if (checkBox.isChecked()) {
//                                                        if (selectedCount >= size) {
//                                                                Toast.makeText(mContext, "最多只能选择" + size + "张图片", Toast.LENGTH_SHORT).show();
//                                                                checkBox.setChecked(false);
//                                                                return;
//                                                        } else {
//                                                                baseRecyclerHolder.setVisible(R.id.view_select_picture_list_item_mask, true);
//                                                                CommonImageLoader.getInstance().getSelectedImages().add(CommonImageLoader.getInstance().getCurrentImageFolder().getAllImages().get((position - 1)));
//                                                        }
//                                                } else {
//                                                        baseRecyclerHolder.setVisible(R.id.view_select_picture_list_item_mask, false);
//                                                        CommonImageLoader.getInstance().getSelectedImages().remove(CommonImageLoader.getInstance().getCurrentImageFolder().getAllImages().get((position - 1)));
//                                                }
//                                                mOnSelectedPictureItemClickListener.onCheckClick(((CheckBox) v), position);
//                                        }
//                                }
//                        });
//                }
//        }
}
