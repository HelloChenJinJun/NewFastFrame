package com.example.chat.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.chat.R;
import com.example.chat.adapter.holder.BaseShareInfoViewHolder;
import com.example.chat.adapter.holder.ImageShareInfoHolder;
import com.example.chat.adapter.holder.ShareShareInfoHolder;
import com.example.chat.adapter.holder.TextShareInfoHolder;
import com.example.chat.adapter.holder.VideoShareInfoHolder;
import com.example.chat.adapter.holder.VoiceShareInfoHolder;
import com.example.chat.bean.PublicPostBean;
import com.example.chat.util.PostUtil;
import com.example.commonlibrary.baseadapter.adapter.BaseMultipleRecyclerAdapter;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/30     14:25
 * QQ:         1981367757
 */

public class ShareInfoAdapter extends BaseMultipleRecyclerAdapter<PublicPostBean, BaseShareInfoViewHolder> {
    @Override
    protected SparseArray<Integer> getLayoutIdMap() {
        SparseArray<Integer> sparseArray = new SparseArray<>();
        sparseArray.put(PostUtil.LAYOUT_TYPE_IMAGE, R.layout.item_fragment_share_info);
        sparseArray.put(PostUtil.LAYOUT_TYPE_SHARE, R.layout.item_fragment_share_info);
        sparseArray.put(PostUtil.LAYOUT_TYPE_TEXT, R.layout.item_fragment_share_info);
        sparseArray.put(PostUtil.LAYOUT_TYPE_VOICE,R.layout.item_fragment_share_info);
        sparseArray.put(PostUtil.LAYOUT_TYPE_VIDEO,R.layout.item_fragment_share_info);
        return sparseArray;
    }


    @Override
    public BaseShareInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PostUtil.LAYOUT_TYPE_IMAGE) {
            return new ImageShareInfoHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutIds().get(viewType), parent, false));
        }else if (viewType==PostUtil.LAYOUT_TYPE_SHARE){
            return new ShareShareInfoHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutIds().get(viewType), parent, false));
        } else if (viewType == PostUtil.LAYOUT_TYPE_TEXT) {
            return new TextShareInfoHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutIds().get(viewType), parent, false));
        } else if (viewType == PostUtil.LAYOUT_TYPE_VOICE) {
            return new VoiceShareInfoHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutIds().get(viewType), parent, false));
        } else if (viewType == PostUtil.LAYOUT_TYPE_VIDEO) {
            return new VideoShareInfoHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutIds().get(viewType), parent, false));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected void convert(BaseShareInfoViewHolder holder, PublicPostBean data) {
        holder.bindAdapter(this);
        holder.initCommonData(data);
    }

    public PublicPostBean getPublicPostDataById(String id) {
        for (PublicPostBean bean :
                data) {
            if (bean.getObjectId().equals(id)) {
                return bean;
            }
        }
        return null;
    }
}
