package com.example.chat.adapter.holder;

import android.view.View;

import com.example.chat.R;
import com.example.chat.bean.PublicPostBean;
import com.example.chat.bean.User;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.google.gson.Gson;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/30     14:36
 * QQ:         1981367757
 */

public abstract class BaseShareInfoViewHolder extends BaseWrappedViewHolder {
    public BaseShareInfoViewHolder(View itemView) {
        super(itemView);
    }


    public abstract void initData(PostDataBean data);

    public void initCommonData(PublicPostBean data) {
        User user=data.getAuthor();
        setImageUrl(R.id.riv_item_fragment_share_info_avatar,user
        .getAvatar())
                .setText(R.id.tv_item_fragment_share_info_main_text
                ,user.getNick())
                .setImageResource(R.id.iv_item_fragment_share_info_sex,user
                .isSex()?R.drawable.ic_sex_female:R.drawable.ic_sex_male)
                .setText(R.id.tv_item_fragment_share_info_sub_text,getText(user))
        .setText(R.id.tv_item_fragment_share_info_share,data.getShare()
                .getObjects().size()==0?"转发":data.getShare()
                .getObjects().size()+"")
        .setText(R.id.tv_item_fragment_share_info_comment
        ,data.getComments().getObjects().size()==0?"评论":data
        .getComments().getObjects().size()+"")
        .setText(R.id.tv_item_fragment_share_info_like,data
        .getLikes().getObjects().size()==0?"点赞":data
        .getLikes().getObjects().size()+"")
        .setOnItemChildClickListener(R.id.riv_item_fragment_share_info_avatar)
        .setOnItemChildClickListener(R.id.iv_item_fragment_share_info_more)
        .setOnItemChildClickListener(R.id.tv_item_fragment_share_info_share)
        .setOnItemChildClickListener(R.id.tv_item_fragment_share_info_comment)
        .setOnItemChildClickListener(R.id.tv_item_fragment_share_info_like);
        Gson gson= BaseApplication
                .getAppComponent().getGson();
        PostDataBean postDataBean=gson.fromJson(data.getContent(),PostDataBean.class);
        if (postDataBean != null) {
            setText(R.id.tv_item_fragment_share_info_main_text
            ,postDataBean.getContent());
        }
        initData(postDataBean);
    }



    private String getText(User user) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(TimeUtil.getRealTime(user.getCreatedAt()))
                .append("  来自[")
                .append(user.getAddress())
        .append("]");
        return stringBuilder.toString();
    }
}
