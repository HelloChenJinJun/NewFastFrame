package com.example.chat.adapter.holder.publicShare;

import android.app.Activity;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.bean.User;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.UserDetail.UserDetailActivity;
import com.example.chat.util.FaceTextUtil;
import com.example.chat.view.CustomMoveMethod;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.bean.chat.PublicPostEntity;
import com.example.commonlibrary.cusotomview.GridSpaceDecoration;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/30     15:35
 * QQ:         1981367757
 */

public class ShareShareInfoHolder extends BaseShareInfoViewHolder {
    private final View container;
    private SuperRecyclerView display;
    private JZVideoPlayerStandard videoDisplay;
    private GridSpaceDecoration itemDecoration;

    public ShareShareInfoHolder(View itemView) {
        super(itemView);
        ViewStub viewStub = itemView.findViewById(R.id.vs_item_fragment_share_info_stub);
        viewStub.setLayoutResource(R.layout.item_fragment_share_info_share);
        container=viewStub.inflate();
        display=container.findViewById(R.id.srcv_item_fragment_share_info_display);
        videoDisplay=container.findViewById(R.id.js_item_fragment_share_info_video_display);
        TextView content=container.findViewById(R.id.tv_item_fragment_share_info_share_content);
        content.setMovementMethod(new CustomMoveMethod());
    }

    @Override
    protected void initData(PostDataBean data) {
        if (data != null&&data.getShareContent()!=null) {
            Gson gson= BaseApplication.getAppComponent().getGson();
            PublicPostEntity bean=gson.fromJson(data.getShareContent(),PublicPostEntity.class);
            PostDataBean shareBean=gson.fromJson(bean.getContent(),PostDataBean.class);
            setOnItemChildClickListener(R.id.ll_item_fragment_share_info_share_container)
            .setText(R.id.tv_item_fragment_share_info_share_content,
                    getSpannerContent(null,null
                            , FaceTextUtil.toSpannableString(getContext(),shareBean.getContent())));
            UserManager.getInstance().findUserById(bean.getUid(), new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    String nick=null;
                    if (e == null) {
                        if (list != null && list.size() > 0) {
                            setText(R.id.tv_item_fragment_share_info_share_content,
                                    getSpannerContent(list.get(0).getName(),list.get(0).getObjectId()
                                    , FaceTextUtil.toSpannableString(getContext(),shareBean.getContent())));
                        }



                    }else {
                        ToastUtils.showShortToast("加载用户信息失败"+e.toString());
                    }
                }
            });
            if (bean.getMsgType() ==Constant.EDIT_TYPE_IMAGE) {
                display.setVisibility(View.VISIBLE);
                videoDisplay.setVisibility(View.GONE);
                setOnItemChildClickListener(R.id.ll_item_fragment_share_info_share_image);
                int size = shareBean.getImageList().size();
                if (itemDecoration != null) {
                    display.removeItemDecoration(itemDecoration);
                }
                if (size <= 4) {
                    display.setLayoutManager(new WrappedGridLayoutManager(getContext(), 2));
                    display.addItemDecoration(itemDecoration=new GridSpaceDecoration(2, DensityUtil.toDp(5), false));
                } else {
                    display.setLayoutManager(new WrappedGridLayoutManager(getContext(), 3));
                    display.addItemDecoration(itemDecoration=new GridSpaceDecoration(3, DensityUtil.toDp(5), false));
                }
                final ImageShareInfoHolder.ImageShareAdapter adapter = new ImageShareInfoHolder.ImageShareAdapter();
                display.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnSimpleItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        view.setTag(adapter.getData(position));
                        getAdapter().getOnItemClickListener()
                                .onItemChildClick(getAdapterPosition()-getAdapter().getItemUpCount(), view, position);
                    }
                });
                adapter.addData(shareBean.getImageList());
            } else if (bean.getMsgType() ==Constant.EDIT_TYPE_TEXT) {
                display.setVisibility(View.GONE);
                videoDisplay.setVisibility(View.GONE);
            } else if (bean.getMsgType() == Constant.EDIT_TYPE_VIDEO) {
                display.setVisibility(View.GONE);
                videoDisplay.setVisibility(View.VISIBLE);
                if (shareBean.getImageList() != null && shareBean.getImageList().size() > 1) {
                    for (String item :
                            shareBean.getImageList()) {
                        if (item.endsWith(".mp4")) {
                            videoDisplay.setUp(item, JZVideoPlayer.SCREEN_WINDOW_LIST, "测试");
                        } else {
                            Glide.with(getContext()).load(item)
                                    .into(videoDisplay.thumbImageView);
                        }
                    }
                }
            }
        }
    }
    private SpannableStringBuilder getSpannerContent(String name,String uid,CharSequence content) {
        SpannableStringBuilder builder=new SpannableStringBuilder();
        String str="@"+name+":";
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#232121")), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (uid!=null) {
                    UserDetailActivity.start((Activity) widget.getContext(),uid);
                }else {
                    ToastUtils.showShortToast("用户数据加载失败");
                }
            }
        }, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableString).append(content);
        return builder;
    }
}
