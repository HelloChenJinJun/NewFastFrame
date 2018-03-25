package com.example.chat.adapter.holder;

import android.app.Activity;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.bean.post.ShareTypeContent;
import com.example.chat.ui.UserDetailActivity;
import com.example.chat.util.PixelUtil;
import com.example.chat.util.PostUtil;
import com.example.chat.view.CustomMoveMethod;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.cusotomview.GridSpaceDecoration;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/30     15:35
 * QQ:         1981367757
 */

public class ShareShareInfoHolder extends BaseShareInfoViewHolder {
    private final View container;
    private SuperRecyclerView display;
    private ImageView videoDisplay;

    public ShareShareInfoHolder(View itemView) {
        super(itemView);
        ViewStub viewStub = itemView.findViewById(R.id.vs_item_fragment_share_info_stub);
        viewStub.setLayoutResource(R.layout.item_fragment_share_info_share);
        container=viewStub.inflate();
        display=container.findViewById(R.id.srcv_item_fragment_share_info_display);
        videoDisplay=container.findViewById(R.id.iv_item_fragment_share_info_video_display);
        TextView content=container.findViewById(R.id.tv_item_fragment_share_info_share_content);
        content.setMovementMethod(new CustomMoveMethod(getContext().getResources().getColor(R.color.blue_500),getContext().getResources().getColor(R.color.blue_500)));
    }

    @Override
    protected void initData(PostDataBean data) {
        if (data != null&&data.getShareContent()!=null) {
            ShareTypeContent bean=data.getShareContent();
            setOnItemChildClickListener(R.id.ll_item_fragment_share_info_share_container)
                    .setText(R.id.tv_item_fragment_share_info_share_content,getSpannerContent(bean));
            if (data.getShareType() == PostUtil
                    .LAYOUT_TYPE_IMAGE) {
                display.setVisibility(View.VISIBLE);
                videoDisplay.setVisibility(View.GONE);
                setOnItemChildClickListener(R.id.ll_item_fragment_share_info_share_image);
                int size = bean.getPostDataBean().getImageList().size();
                if (size <= 4) {
                    display.setLayoutManager(new WrappedGridLayoutManager(getContext(), 2));
                    display.addItemDecoration(new GridSpaceDecoration(2, PixelUtil.todp(5), false));
                } else {
                    display.setLayoutManager(new WrappedGridLayoutManager(getContext(), 3));
                    display.addItemDecoration(new GridSpaceDecoration(3, PixelUtil.todp(5), false));
                }
                final ImageShareInfoHolder.ImageShareAdapter adapter = new ImageShareInfoHolder.ImageShareAdapter();
                display.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnSimpleItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        view.setTag(adapter.getData(position));
                        getAdapter().getOnItemClickListener()
                                .onItemChildClick(getAdapterPosition(), view, position);
                    }
                });
                adapter.addData(bean.getPostDataBean().getImageList());
            } else if (data.getShareType() == PostUtil
                    .LAYOUT_TYPE_TEXT) {
                display.setVisibility(View.GONE);
                videoDisplay.setVisibility(View.GONE);
            } else if (data.getShareType() == PostUtil.LAYOUT_TYPE_VIDEO) {
                display.setVisibility(View.GONE);
                videoDisplay.setVisibility(View.VISIBLE);
                setOnItemChildClickListener(R.id.iv_item_fragment_share_info_video_display)
                .setImageUrl(R.id.iv_item_fragment_share_info_video_display,bean.getPostDataBean().getImageList()
                .get(0));
            }
        }
    }
    private SpannableStringBuilder getSpannerContent(final ShareTypeContent bean) {
        SpannableStringBuilder builder=new SpannableStringBuilder();
        String name="@"+bean.getNick()+":";
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#232121")), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                UserDetailActivity.start((Activity) widget.getContext(),bean.getUid());
            }
        }, 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableString).append(bean.getPostDataBean().getContent());
        return builder;
    }
}
