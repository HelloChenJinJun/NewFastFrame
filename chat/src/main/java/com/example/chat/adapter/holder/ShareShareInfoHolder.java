package com.example.chat.adapter.holder;

import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.bean.PublicPostBean;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.util.PostUtil;
import com.example.chat.view.ListImageView;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/30     15:35
 * QQ:         1981367757
 */

public class ShareShareInfoHolder extends BaseShareInfoViewHolder {
    private ViewStub viewStub;

    public ShareShareInfoHolder(View itemView) {
        super(itemView);
        viewStub = itemView.findViewById(R.id.vs_item_fragment_share_info_stub);

    }

    @Override
    protected void initData(PostDataBean data) {
        if (data != null&&data.getShareContent()!=null) {
            if (data.getShareType() == PostUtil
                    .LAYOUT_TYPE_IMAGE) {
                viewStub.setLayoutResource(R.layout.item_fragment_share_info_image);
                ListImageView listImageView= (ListImageView) viewStub.inflate();
                listImageView.bindData(data.getShareContent().getImageList());
            } else if (data.getShareType() == PostUtil
                    .LAYOUT_TYPE_TEXT) {
                viewStub.setLayoutResource(R.layout.item_fragment_share_info_text);
                TextView content= (TextView) viewStub.inflate();
                content.setText(data.getShareContent().getContent());
            }
        }
    }
}
