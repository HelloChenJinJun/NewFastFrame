package com.example.chat.adapter.holder;

import android.view.View;
import android.view.ViewStub;

import com.example.chat.R;
import com.example.chat.bean.PublicPostBean;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.util.PixelUtil;
import com.example.chat.view.ListImageView;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/30     15:26
 * QQ:         1981367757
 */

public class ImageShareInfoHolder extends BaseShareInfoViewHolder {
    private ListImageView display;
    public ImageShareInfoHolder(View itemView) {
        super(itemView);
       ViewStub viewStub= itemView.findViewById(R.id.vs_item_fragment_share_info_stub);
       viewStub.setLayoutResource(R.layout.item_fragment_share_info_image);
        display= (ListImageView) viewStub.inflate();
    }



    @Override
    protected void initData(PostDataBean data) {
        if (data!=null) {
            display.setImagePadding(PixelUtil.todp(3));
            display.bindData(data.getImageList());
            display.setOnImageViewItemClickListener(new ListImageView.OnImageViewItemClickListener() {
                @Override
                public void onImageClick(View view, int position, String url) {
                    view.setTag(url);
                    getAdapter().getOnItemClickListener()
                            .onItemChildClick(getAdapterPosition(),view,position);
                }
            });
        }
    }
}
