package com.example.chat.adapter;

import android.util.SparseArray;

import com.example.chat.R;
import com.example.chat.bean.ImageItem;
import com.example.commonlibrary.baseadapter.adapter.BaseMultipleRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

import java.util.List;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/29      13:37
 * QQ:             1981367757
 */
public class GridPictureAdapter extends BaseMultipleRecyclerAdapter<ImageItem, BaseWrappedViewHolder> {



        public GridPictureAdapter(List<ImageItem> data, int layoutId) {
                super(data, layoutId);
        }

        public GridPictureAdapter() {
                this(null, 0);
        }

        @Override
        protected SparseArray<Integer> getLayoutIdMap() {
                SparseArray<Integer> sparseArray = new SparseArray<>();
                sparseArray.put(ImageItem.ITEM_CAMERA, R.layout.grid_picture_item);
                sparseArray.put(ImageItem.ITEM_NORMAL, R.layout.grid_picture_item);
                return sparseArray;
        }






        @Override
        protected void convert(BaseWrappedViewHolder holder, ImageItem data) {
                if (holder.getItemViewType() == ImageItem.ITEM_CAMERA) {
                        holder.setImageResource(R.id.iv_grid_picture_item_display, R.drawable.selector_image_add);
                } else {
                        holder.setImageUrl(R.id.iv_grid_picture_item_display, data.getPath());
                }
                holder.setOnItemChildClickListener(R.id.iv_grid_picture_item_display);
        }


//        private List<ImageItem> data;
//        private int maxSelectedCount;
//
//        /**
//         * 是否添加了最后一张选择图片
//         */
//        private boolean isAdded;
//        private LayoutInflater mLayoutInflater;
//        private OnItemClickListener mOnItemClickListener;
////        private int itemSize;
//
//        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//                mOnItemClickListener = onItemClickListener;
//        }
//
//        public interface OnItemClickListener {
//                void onItemClick(View view, int position);
//        }
//
//        public GridPictureAdapter(Context context, List<ImageItem> selectedImageList, int maxSelectedCount) {
//                this.maxSelectedCount = maxSelectedCount;
//                mLayoutInflater = LayoutInflater.from(context);
//
////                itemSize = CommonUtils.getLayoutItemSize(context, 4, 0);
////                LogUtil.e("GridPictureAdapter,item" + itemSize);
//                LogUtil.e("没有设置item的大小");
//                bindData(selectedImageList);
//        }
//
//        public void bindData(List<ImageItem> list) {
//                if (list == null) {
//                        data = new ArrayList<>();
//                        data.add(new ImageItem());
//                        isAdded = true;
//                } else {
//                        data = list;
//                        if (data.size() < maxSelectedCount) {
////                                还有空间
//                                data.add(new ImageItem());
//                                isAdded = true;
//                        } else {
//                                isAdded = false;
//                        }
//                }
//                notifyDataSetChanged();
//        }
//
//        @Override
//        public BaseRecyclerHolder<ImageItem> onCreateViewHolder(ViewGroup parent, int viewType) {
//                BaseRecyclerHolder<ImageItem> baseRecyclerHolder = new BaseRecyclerHolder<ImageItem>(mLayoutInflater.inflate(R.layout.grid_picture_item, parent, false), R.layout.grid_picture_item) {
//                        @Override
//                        public void bindView(ImageItem data) {
//                                final int clickPosition;
//                                if (isAdded && getAdapterPosition() == getItemCount() - 1) {
//                                        setImageResource(R.id.iV_grid_picture_item_display, R.drawable.selector_image_add);
//                                        clickPosition = CommonImageLoader.CLICK_POSITION;
//                                } else {
//                                        CommonImageLoader.getInstance().getImageLoader().displayImage((mContext), data.getPath(), ((ImageView) getView(R.id.iV_grid_picture_item_display)), 0, 0);
//                                        clickPosition = getAdapterPosition();
//                                }
//                                if (mOnItemClickListener != null) {
//                                        setOnClickListener(R.id.iV_grid_picture_item_display, new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                        mOnItemClickListener.onItemClick(v, clickPosition);
//                                                }
//                                        });
//                                }
//                        }
//                };
////                ViewGroup.LayoutParams params = baseRecyclerHolder.itemView.getLayoutParams();
////                params.height = itemSize;
////                baseRecyclerHolder.itemView.setLayoutParams(params);
//                return baseRecyclerHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(BaseRecyclerHolder<ImageItem> holder, int position) {
//                holder.bindView(data.get(position));
//        }
//
//        @Override
//        public int getItemCount() {
//                return data.size();
//        }

}
