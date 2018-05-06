package com.example.chat.adapter.holder.publicShare;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.bean.post.PublicPostBean;
import com.example.chat.bean.User;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.events.LocationEvent;
import com.example.chat.manager.UserManager;
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

public class BaseShareInfoViewHolder extends BaseWrappedViewHolder {
    private Gson gson;
    public BaseShareInfoViewHolder(View itemView) {
        super(itemView);
        gson = BaseApplication
                .getAppComponent().getGson();
    }


    protected void initData(PostDataBean data) {
    }

    public void initCommonData(PublicPostBean data) {
        User user = data.getAuthor();
        setImageUrl(R.id.riv_item_fragment_share_info_avatar, user
                .getAvatar())
                .setText(R.id.tv_item_fragment_share_info_main_text
                        , user.getName())
                .setImageResource(R.id.iv_item_fragment_share_info_sex, user
                        .isSex() ? R.drawable.ic_sex_male : R.drawable.ic_sex_female)
                .setText(R.id.tv_item_fragment_share_info_sub_text, getText(data))
                .setText(R.id.tv_item_fragment_share_info_share, data
                        .getShareCount()==0? "转发" :data
                        .getShareCount()+ "")
                .setText(R.id.tv_item_fragment_share_info_comment
                        , data.getCommentCount()==0? "评论" : data.getCommentCount() + "")
                .setText(R.id.tv_item_fragment_share_info_like, data
                        .getLikeCount()==0? "点赞" : data
                        .getLikeCount() + "")
                .setOnItemChildClickListener(R.id.riv_item_fragment_share_info_avatar)
                .setOnItemChildClickListener(R.id.iv_item_fragment_share_info_more)
                .setOnItemChildClickListener(R.id.tv_item_fragment_share_info_share)
                .setOnItemChildClickListener(R.id.tv_item_fragment_share_info_comment)
                .setOnItemChildClickListener(R.id.tv_item_fragment_share_info_like)
                .setOnItemChildClickListener(R.id.iv_item_fragment_share_info_retry)
        .setOnItemClickListener();
        if (data.getLikeList().contains(UserManager.getInstance().getCurrentUserObjectId())) {
            ((TextView)getView(R.id.tv_item_fragment_share_info_like)).setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.ic_favorite_deep_orange_a700_24dp), null, null, null);
        }else {
            ((TextView)getView(R.id.tv_item_fragment_share_info_like)).setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.ic_favorite_border_deep_orange_a700_24dp), null, null, null);
        }
        PostDataBean postDataBean = gson.fromJson(data.getContent(), PostDataBean.class);
        if (postDataBean != null) {
            setText(R.id.tv_item_fragment_share_info_normal_text
                    , postDataBean.getContent());
        }

        if (data.getSendStatus().equals(Constant.SEND_STATUS_SUCCESS)) {
            setVisible(R.id.iv_item_fragment_share_info_retry, false)
                    .setVisible(R.id.pb_item_fragment_share_info_retry_loading, false);
        } else if (data.getSendStatus().equals(Constant.SEND_STATUS_SENDING)) {
            setVisible(R.id.pb_item_fragment_share_info_retry_loading, true)
                    .setVisible(R.id.iv_item_fragment_share_info_retry, false);
        } else {
            setVisible(R.id.pb_item_fragment_share_info_retry_loading, false)
                    .setVisible(R.id.iv_item_fragment_share_info_retry, true)
                    .setOnItemChildClickListener(R.id.iv_item_fragment_share_info_retry);
        }
        initData(postDataBean);
    }


    private SpannableStringBuilder getText(PublicPostBean bean) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        SpannableString spannableString = new SpannableString(".");
        spannableString.setSpan(new ImageSpan(getContext(), R.drawable.ic_location, DynamicDrawableSpan.ALIGN_BASELINE), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        String location = "";
        if (bean.getLocation() != null) {
            LocationEvent mLocationEvent = gson.fromJson(bean.getLocation(), LocationEvent.class);
            if (mLocationEvent.getCity().equals(mLocationEvent.getTitle())) {
                location = mLocationEvent.getTitle();
            } else {
                location = mLocationEvent.getCity() + "." + mLocationEvent.getTitle();
            }
        }
        spannableStringBuilder.append(TimeUtil.getRealTime(bean.getCreatedAt() == null ? bean.getUpdatedAt() :
                bean.getCreatedAt())).append("   ").append(spannableString).append(location);
        return spannableStringBuilder;
    }
}
