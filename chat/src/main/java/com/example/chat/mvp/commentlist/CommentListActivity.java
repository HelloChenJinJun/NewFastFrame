package com.example.chat.mvp.commentlist;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.adapter.CommentListAdapter;
import com.example.chat.bean.ImageItem;
import com.example.chat.bean.PublicPostBean;
import com.example.chat.bean.User;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.ui.BasePreViewActivity;
import com.example.chat.util.PixelUtil;
import com.example.chat.util.PostUtil;
import com.example.chat.util.TimeUtil;
import com.example.chat.view.ListImageView;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.imageloader.glide.GlideImageLoaderConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/2     23:17
 * QQ:         1981367757
 */

public class CommentListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, View.OnClickListener {


    private SwipeRefreshLayout refresh;
    private SuperRecyclerView display;
    CommentListAdapter commentListAdapter;

    @Override
    public void updateData(Object o) {

    }

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_comment_list;
    }

    @Override
    protected void initView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_activity_comment_list_refresh);
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_comment_list_display);
        refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        PublicPostBean data = (PublicPostBean) getIntent().getSerializableExtra("data");
        display.setLayoutManager(new WrappedLinearLayoutManager(this));
        display.addItemDecoration(new ListViewDecoration(this));
        display.setOnLoadMoreListener(this);
        display.setLoadMoreFooterView(new LoadMoreFooterView(this));
        display.addHeaderView(getHeaderView(data));

    }

    private TextView share, comment, like;


    private View getHeaderView(PublicPostBean data) {
        final PostDataBean postDataBean = BaseApplication.getAppComponent().getGson()
                .fromJson(data.getContent(), PostDataBean.class);
        View headerView = getLayoutInflater().inflate(R.layout.item_fragment_share_info, null);
        headerView.findViewById(R.id.iv_item_fragment_share_info_more).setVisibility(View.GONE);
        share = headerView.findViewById(R.id.tv_item_fragment_share_info_share);
        share.setText(data.getShareCount() == 0 ? "转发" : data.getShareCount() + "");
        share.setOnClickListener(this);
        comment = headerView.findViewById(R.id.tv_item_fragment_share_info_comment);
        comment.setText(data.getCommentCount() == 0 ? "评论" : data.getCommentCount() + "");
        comment.setOnClickListener(this);
        like = headerView.findViewById(R.id.tv_item_fragment_share_info_like);
        like.setText(data.getLikeCount() == 0 ? "点赞" : data.getLikeCount() + "");
        like.setOnClickListener(this);
        RoundAngleImageView avatar = headerView.findViewById(R.id.riv_item_fragment_share_info_avatar);
        BaseApplication.getAppComponent().getImageLoader()
                .loadImage(this, new GlideImageLoaderConfig.Builder().url(data.getAuthor()
                        .getAvatar()).imageView(avatar).build());
        avatar.setOnClickListener(this);
        ((TextView) headerView.findViewById(R.id.tv_item_fragment_share_info_main_text))
                .setText(data.getAuthor().getNick());
        ((ImageView) headerView.findViewById(R.id.iv_item_fragment_share_info_sex))
                .setImageResource(data.getAuthor().isSex() ? R.drawable.ic_sex_male : R.drawable
                        .ic_sex_female);
        ((TextView) headerView.findViewById(R.id.tv_item_fragment_share_info_sub_text))
                .setText(getText(data.getAuthor()));
        ((TextView) headerView.findViewById(R.id.tv_item_fragment_share_info_normal_text))
                .setText(postDataBean.getContent());

        if (data.getMsgType() == PostUtil.LAYOUT_TYPE_IMAGE) {
            ListImageView display = null;
            ViewStub viewStub = headerView.findViewById(R.id.vs_item_fragment_share_info_stub);
            viewStub.setLayoutResource(R.layout.item_fragment_share_info_image);
            display = (ListImageView) viewStub.inflate();
            display.setImagePadding(PixelUtil.todp(3));
            display.bindData(postDataBean.getImageList());
            display.setOnImageViewItemClickListener(new ListImageView.OnImageViewItemClickListener() {
                @Override
                public void onImageClick(View view, int position, String url) {
                    List<String> imageList=postDataBean.getImageList();
                    if (imageList != null && imageList.size() > 0) {
                        List<ImageItem>  result=new ArrayList<>();
                        for (String str :
                                imageList) {
                            ImageItem imageItem=new ImageItem();
                            imageItem.setPath(str);
                            result.add(imageItem);
                        }
                        BasePreViewActivity.startBasePreview(CommentListActivity.this,result,position);
                    }
                }
            });

        } else if (data.getMsgType() == PostUtil.LAYOUT_TYPE_TEXT) {

        } else if (data.getMsgType() == PostUtil.LAYOUT_TYPE_VOICE) {

        } else if (data.getMsgType() == PostUtil.LAYOUT_TYPE_SHARE) {
            if (postDataBean != null&&postDataBean.getShareContent()!=null) {
                ViewStub viewStub = headerView.findViewById(R.id.vs_item_fragment_share_info_stub);
                if (postDataBean.getShareType() == PostUtil
                        .LAYOUT_TYPE_IMAGE) {
                    viewStub.setLayoutResource(R.layout.item_fragment_share_info_image);
                    ListImageView listImageView= (ListImageView) viewStub.inflate();
                    listImageView.bindData(postDataBean.getShareContent().getImageList());
                } else if (postDataBean.getShareType() == PostUtil
                        .LAYOUT_TYPE_TEXT) {
                    viewStub.setLayoutResource(R.layout.item_fragment_share_info_text);
                    TextView content= (TextView) viewStub.inflate();
                    content.setText(postDataBean.getShareContent().getContent());
                }
            }

        }


        return null;
    }


    private String getText(User user) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TimeUtil.getRealTime(user.getCreatedAt()))
                .append("  来自[")
                .append(user.getAddress())
                .append("]");
        return stringBuilder.toString();
    }

    public static void start(Activity activity, PublicPostBean data) {
        Intent intent = new Intent(activity, CommentListActivity.class);
        intent.putExtra("data", data);
        activity.startActivity(intent);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void onClick(View v) {

    }
}
