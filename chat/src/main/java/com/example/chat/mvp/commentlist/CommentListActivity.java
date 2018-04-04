package com.example.chat.mvp.commentlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.ChatApplication;
import com.example.chat.R;
import com.example.chat.adapter.CommentListAdapter;
import com.example.chat.adapter.EmotionViewAdapter;
import com.example.chat.adapter.holder.publicShare.ImageShareInfoHolder;
import com.example.chat.base.Constant;
import com.example.chat.bean.FaceText;
import com.example.chat.bean.ImageItem;
import com.example.chat.bean.post.PublicPostBean;
import com.example.chat.bean.User;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.bean.post.PublicCommentBean;
import com.example.chat.bean.post.ShareTypeContent;
import com.example.chat.dagger.commentlist.CommentListModule;
import com.example.chat.dagger.commentlist.DaggerCommentListComponent;
import com.example.chat.events.CommentEvent;
import com.example.chat.events.LocationEvent;
import com.example.chat.events.UpdatePostEvent;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.EditShare.EditShareInfoActivity;
import com.example.chat.mvp.chat.ChatActivity;
import com.example.chat.mvp.commentdetail.CommentListDetailActivity;
import com.example.chat.mvp.preview.PhotoPreViewActivity;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.mvp.UserDetail.UserDetailActivity;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.FaceTextUtil;
import com.example.chat.util.TimeUtil;
import com.example.chat.view.CustomMoveMethod;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.adapter.CommonPagerAdapter;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.GridSpaceDecoration;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.cusotomview.WrappedViewPager;
import com.example.commonlibrary.imageloader.glide.GlideImageLoaderConfig;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/2     23:17
 * QQ:         1981367757
 */

public class CommentListActivity extends SlideBaseActivity<List<PublicCommentBean>, CommentListPresenter> implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, View.OnClickListener {


    private SwipeRefreshLayout refresh;
    private SuperRecyclerView display;
    @Inject
    CommentListAdapter commentListAdapter;
    private String postId;

    private int currentPosition = -1;


    //    bottom
    private EditText input;
    private Button face, keyboard, send;
    private WrappedViewPager emotionPager;
    private PublicPostBean data;
    private WrappedLinearLayoutManager manager;
    private ImageView retry;
    private ProgressBar loading;

    @Override
    public void updateData(List<PublicCommentBean> list) {
        if (refresh.isRefreshing()) {
            if (commentListAdapter.getData().size() > 10) {
                commentListAdapter.removeEndData(commentListAdapter.getData().size() - 10);
            }
            commentListAdapter.addData(0, list);
        } else {
            commentListAdapter.addData(list);
        }
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return true;
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
        face = (Button) findViewById(R.id.btn_comment_bottom_face);

        input = (EditText) findViewById(R.id.et_comment_bottom_input);
        keyboard = (Button) findViewById(R.id.btn_comment_bottom_keyboard);
        send = (Button) findViewById(R.id.btn_comment_bottom_send);
        face.setOnClickListener(this);
        keyboard.setOnClickListener(this);
        send.setOnClickListener(this);
        emotionPager = (WrappedViewPager) findViewById(R.id.vp_comment_bottom_emotion);
        initEmotionInfo();
    }


    private List<FaceText> emotionFaceList;
    private GridViewAdapter gridViewAdapter, mGridViewAdapter;

    private void initEmotionInfo() {
        List<View> list = new ArrayList<>();
        emotionFaceList = FaceTextUtil.getFaceTextList();
        for (int i = 0; i < 2; i++) {
            list.add(getGridView(i));
        }
        emotionPager.setAdapter(new CommonPagerAdapter(list));
    }


    private View getGridView(int i) {
        View emotionView = LayoutInflater.from(this).inflate(R.layout.view_activity_chat_emotion, null);
        SuperRecyclerView superRecyclerView = emotionView.findViewById(R.id.srcv_view_activity_chat_emotion_display);
        superRecyclerView.setLayoutManager(new WrappedGridLayoutManager(this,7));
        EmotionViewAdapter emotionViewAdapter = new EmotionViewAdapter();
        superRecyclerView.setAdapter(emotionViewAdapter);
        emotionViewAdapter.addData(i == 0 ? emotionFaceList.subList(0, 21) : emotionFaceList.subList(21, emotionFaceList.size()));
        emotionViewAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                FaceText faceText= emotionViewAdapter.getData(position);
                String content = faceText.getText();
                if (input != null) {
                    int startIndex = input.getSelectionStart();
                    CharSequence content1 = input.getText().insert(startIndex, content);
                    input.setText(FaceTextUtil.toSpannableString(CommentListActivity.this.getApplicationContext(), content1.toString()));
//                                        重新定位光标位置
                    CharSequence info = input.getText();
                    if (info != null) {
                        Spannable spannable = (Spannable) info;
                        Selection.setSelection(spannable, startIndex + content.length());
                    }
                }
            }
        });
        return emotionView;
    }


    private class GridViewAdapter extends BaseAdapter {
        private List<FaceText> mFaceTextList = new ArrayList<>();
        private Context mContext;

        GridViewAdapter(Context context, List<FaceText> faceTextList) {
            this.mContext = context;
            if (faceTextList != null && faceTextList.size() > 0) {
                mFaceTextList.clear();
                mFaceTextList.addAll(faceTextList);
            }
        }

        @Override
        public int getCount() {
            return mFaceTextList.size();
        }

        @Override
        public Object getItem(int position) {
            return mFaceTextList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.emtion_item, parent, false);
                viewHolder.display = convertView.findViewById(R.id.iv_emotion_item_display);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            FaceText item = mFaceTextList.get(position);
            viewHolder.display.setImageDrawable(mContext.getResources().getDrawable(mContext.getResources().getIdentifier(item.getText().substring(1), "mipmap", mContext.getPackageName())));
            return convertView;
        }
    }


    private class ViewHolder {
        ImageView display;
    }

    @Override
    protected void initData() {
        DaggerCommentListComponent.builder()
                .chatMainComponent(ChatApplication.getChatMainComponent())
                .commentListModule(new CommentListModule(this))
                .build().inject(this);
        data = (PublicPostBean) getIntent().getSerializableExtra("data");
        postId = data.getObjectId();
        display.setLayoutManager(manager = new WrappedLinearLayoutManager(this));
        display.setOnLoadMoreListener(this);
        display.setLoadMoreFooterView(new LoadMoreFooterView(this));
        display.addHeaderView(getHeaderView(data));
        display.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    currentPosition = -1;
                    input.setHint("");
                }
            }
        });
        commentListAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                ToastUtils.showShortToast("展示对话框");
                currentPosition = position;
            }

            @Override
            public void onItemChildClick(int position, View view, int id) {
                if (id == R.id.iv_item_activity_comment_list_comment) {
                    currentPosition = position;
                    input.setHint("回复@" + commentListAdapter.getData(position).getUser().getNick() + ":");
                    CommonUtils.showSoftInput(CommentListActivity.this, input);
                    manager.scrollToPositionWithOffset(currentPosition + commentListAdapter.getItemUpCount(), 0);
                } else if (id == R.id.riv_item_activity_comment_list_avatar) {
                    UserDetailActivity.start(CommentListActivity.this, commentListAdapter.getData(position).getUser().getObjectId());
                } else if (id == R.id.tv_item_activity_comment_list_look) {
                    CommentListDetailActivity.start(CommentListActivity.this, commentListAdapter
                            .getData(position));
                }
            }
        });
        display.setAdapter(commentListAdapter);
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setTitle("动态详情");
        setToolBar(toolBarOption);
        presenter.registerEvent(CommentEvent.class, commentEvent -> {
            if (commentEvent.getType() == CommentEvent.TYPE_LIKE) {
                if (commentEvent.getAction() == CommentEvent.ACTION_ADD) {
                    data.setLikeCount(data.getLikeCount() + 1);
                    data.getLikeList().add(UserManager.getInstance()
                            .getCurrentUserObjectId());
                }else {
                    data.setLikeCount(data.getLikeCount()-1);
                    data.getLikeList().remove(UserManager.getInstance()
                            .getCurrentUserObjectId());
                }
                updateLikeStatus();
            } else {
                data.setCommentCount(data.getCommentCount() + 1);
                updateCommentStatus();
            }
        });
        presenter.registerEvent(PublicCommentBean.class, publicCommentBean -> commentListAdapter.addData(0, publicCommentBean));
        presenter.registerEvent(UpdatePostEvent.class, updatePostEvent -> {
            PublicPostBean publicPostBean=updatePostEvent.getPublicPostBean();
            if (data.getObjectId().contains("-")) {
                if (publicPostBean.getUpdatedAt().equals(data.getUpdatedAt())
                        && data.getAuthor().equals(data.getAuthor())) {
                    data=publicPostBean;
                    updateStatus();
                }
            } else if (data.equals(publicPostBean)) {
                data=publicPostBean;
                updateStatus();
            }
        });
        presenter.getCommentListData(postId, true, getRefreshTime(true));
    }





    private String getRefreshTime(boolean isRefresh) {
        if (isRefresh) {
            if (commentListAdapter.getData().size() == 0) {
                return "0000-00-00 01:00:00";
            }
                if (commentListAdapter.getData().size() >=10) {
                    return commentListAdapter.getData(9).getCreatedAt();
                } else {
                    return commentListAdapter.getData(commentListAdapter.getData().size() - 1)
                            .getCreatedAt();
                }
        } else {
            if (commentListAdapter.getData(commentListAdapter.getData().size() - 1) != null) {
                return commentListAdapter.getData(commentListAdapter.getData().size() - 1).getCreatedAt();
            } else {
                return "0000-00-00 01:00:00";
            }
        }
    }

    private TextView share, comment, like;


    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        dismissLoadDialog();
        if (input.hasFocus()) {
            CommonUtils.hideSoftInput(
                    this, input);
            input.setText("");
        }
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
            super.showError(errorMsg, listener);
        } else {
            display.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
        }
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        dismissLoadDialog();
        if (input.hasFocus()) {
            CommonUtils.hideSoftInput(
                    this, input);
            input.setText("");
        }
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
    }

    @Override
    public void showLoading(String loadMessage) {
        refresh.setRefreshing(true);
    }

    private View getHeaderView(final PublicPostBean data) {
        final PostDataBean postDataBean = BaseApplication.getAppComponent().getGson()
                .fromJson(data.getContent(), PostDataBean.class);
        View headerView = getLayoutInflater().inflate(R.layout.item_fragment_share_info, null);
        headerView.findViewById(R.id.iv_item_fragment_share_info_more).setVisibility(View.GONE);
        share = headerView.findViewById(R.id.tv_item_fragment_share_info_share);
        share.setText(data.getShareCount() == 0 ? "转发" : data.getShareCount() + "");
        share.setOnClickListener(this);
        comment = headerView.findViewById(R.id.tv_item_fragment_share_info_comment);
        updateCommentStatus();
        comment.setOnClickListener(this);
        like = headerView.findViewById(R.id.tv_item_fragment_share_info_like);
        updateLikeStatus();
        like.setOnClickListener(this);

         retry=headerView.findViewById(R.id.iv_item_fragment_share_info_retry);
         loading=headerView.findViewById(R.id.pb_item_fragment_share_info_retry_loading);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setSendStatus(Constant.SEND_STATUS_SENDING);
                presenter.reSendPublicPostBean(data,data.getObjectId());
            }
        });
        updateStatus();



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
                .setText(getText(data));
        ((TextView) headerView.findViewById(R.id.tv_item_fragment_share_info_normal_text))
                .setText(postDataBean.getContent());
        headerView.findViewById(R.id.iv_item_fragment_share_info_more).setOnClickListener(view -> {
            if (UserManager.getInstance().getCurrentUserObjectId().equals(data.getAuthor().getObjectId())) {
                List<String> list = new ArrayList<>();
                list.add("删除");
                list.add("修改");
                showChooseDialog("帖子操作", list, (adapterView, view1, i, l) -> {
                    dismissBaseDialog();
                    if (i == 0) {
                        showLoadDialog("删除中....");
                        presenter.deleteShareInfo(data, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                dismissLoadDialog();
                                if (e == null) {
                                    ToastUtils.showShortToast("删除成功");
                                    CommonLogger.e("删除成功");
                                    RxBusManager.getInstance().post(new CommentEvent(data.getObjectId(),CommentEvent.TYPE_POST,CommentEvent.ACTION_DELETE));
                                    finish();
                                } else {
                                    ToastUtils.showShortToast("删除失败" + e.toString());
                                    CommonLogger.e("删除失败" + e.toString());
                                }
                            }
                        });
                    } else {
                        EditShareInfoActivity.start(CommentListActivity.this,data.getMsgType(),data,true);
                        finish();
                    }
                });
            } else {
                ToastUtils.showShortToast("非帖子作者，不可编辑");
            }
        });
        
        ViewStub viewStub = headerView.findViewById(R.id.vs_item_fragment_share_info_stub);
        if (data.getMsgType() == Constant.EDIT_TYPE_IMAGE) {
            SuperRecyclerView display = null;
            viewStub.setLayoutResource(R.layout.item_fragment_share_info_image);
            display = (SuperRecyclerView) viewStub.inflate();
            int size = postDataBean.getImageList().size();
            if (size <= 4) {
                display.setLayoutManager(new WrappedGridLayoutManager(this, 2));
                display.addItemDecoration(new GridSpaceDecoration(2, DensityUtil.toDp(5), false));
            } else {
                display.setLayoutManager(new WrappedGridLayoutManager(this, 3));
                display.addItemDecoration(new GridSpaceDecoration(3, DensityUtil.toDp(5), false));
            }

            final ImageShareInfoHolder.ImageShareAdapter adapter = new ImageShareInfoHolder.ImageShareAdapter();
            display.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnSimpleItemClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    List<String> imageList = postDataBean.getImageList();
                    if (imageList != null && imageList.size() > 0) {
                        ArrayList<ImageItem> result = new ArrayList<>();
                        for (String str :
                                imageList) {
                            ImageItem imageItem = new ImageItem();
                            imageItem.setPath(str);
                            result.add(imageItem);
                        }
                        PhotoPreViewActivity.start(CommentListActivity.this, position, result, false);
                    }
                }
            });
            adapter.addData(postDataBean.getImageList());

        } else if (data.getMsgType() == Constant.EDIT_TYPE_TEXT) {
        } else if (data.getMsgType() == Constant.EDIT_TYPE_SHARE) {

            if (postDataBean.getShareContent() != null) {
                final ShareTypeContent bean = postDataBean.getShareContent();
               viewStub.setLayoutResource(R.layout.item_fragment_share_info_share);
                View rootView=viewStub.inflate();
                rootView.setOnClickListener(v -> CommentListActivity.start(CommentListActivity.this, getSharePublicPostBean()));
                SuperRecyclerView display=rootView.findViewById(R.id.srcv_item_fragment_share_info_display);
                JZVideoPlayerStandard videoDisplay=rootView.findViewById(R.id.js_item_fragment_share_info_video_display);
               TextView content=rootView.findViewById(R.id.tv_item_fragment_share_info_share_content);
                content.setMovementMethod(new CustomMoveMethod(getResources().getColor(R.color.blue_500),getResources().getColor(R.color.blue_500)));
                content.setText(getSpannerContent(bean));
                if (postDataBean.getShareType() ==Constant.EDIT_TYPE_IMAGE) {
                    display.setVisibility(View.VISIBLE);
                    videoDisplay.setVisibility(View.GONE);
                    int size = bean.getPostDataBean().getImageList().size();
                    if (size <= 4) {
                        display.setLayoutManager(new WrappedGridLayoutManager(this, 2));
                        display.addItemDecoration(new GridSpaceDecoration(2, DensityUtil.toDp(5), false));
                    } else {
                        display.setLayoutManager(new WrappedGridLayoutManager(this, 3));
                        display.addItemDecoration(new GridSpaceDecoration(3, DensityUtil.toDp(5), false));
                    }
                    final ImageShareInfoHolder.ImageShareAdapter adapter = new ImageShareInfoHolder.ImageShareAdapter();
                    display.setAdapter(adapter);
                    adapter.setOnItemClickListener(new OnSimpleItemClickListener() {
                        @Override
                        public void onItemClick(int position, View view) {
                            CommentListActivity.start(CommentListActivity.this, getSharePublicPostBean());
                        }
                    });
                    adapter.addData(bean.getPostDataBean().getImageList());
                    rootView.setOnClickListener(v -> CommentListActivity.start(CommentListActivity.this, getSharePublicPostBean()));
                } else if (postDataBean.getShareType() == Constant.EDIT_TYPE_TEXT) {
                    display.setVisibility(View.GONE);
                    videoDisplay.setVisibility(View.GONE);
                } else if (postDataBean.getShareType() == Constant.EDIT_TYPE_VIDEO) {
                    display.setVisibility(View.GONE);
                    videoDisplay.setVisibility(View.VISIBLE);
                    if (bean.getPostDataBean().getImageList() != null && bean.getPostDataBean().getImageList().size() > 1) {
                        for (String item :
                                bean.getPostDataBean().getImageList()) {
                            if (item.endsWith(".mp4")) {
                                videoDisplay.setUp(item, JZVideoPlayer.SCREEN_WINDOW_LIST, "测试");
                            } else {
                                Glide.with(this).load(item)
                                        .into(videoDisplay.thumbImageView);
                            }
                        }
                    }
                }
            }
        } else if (data.getMsgType() == Constant.EDIT_TYPE_VIDEO) {
            viewStub.setLayoutResource(R.layout.item_fragment_share_info_video);
            JZVideoPlayerStandard videoPlayerStandard=viewStub.inflate().findViewById(R.id.js_item_fragment_share_info_video_display);
            if (postDataBean.getImageList() != null && postDataBean.getImageList().size() > 1) {
                for (String item :
                        postDataBean.getImageList()) {
                    if (item.endsWith(".mp4")) {
                        videoPlayerStandard.setUp(item, JZVideoPlayer.SCREEN_WINDOW_LIST, "测试");
                    } else {
                        Glide.with(this).load(item)
                                .into(videoPlayerStandard.thumbImageView);
                    }
                }
            }
        }
        return headerView;
    }

    private void updateCommentStatus() {
        comment.setText(data.getCommentCount() == 0 ? "评论" : data.getCommentCount() + "");
    }

    private void updateLikeStatus() {
        like.setText(data.getLikeCount() == 0 ? "点赞" : data.getLikeCount() + "");
        if (data.getLikeList().contains(UserManager.getInstance().getCurrentUserObjectId())) {
            like.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_favorite_deep_orange_a700_24dp), null, null, null);
        }else {
            like.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_favorite_border_deep_orange_a700_24dp), null, null, null);
        }
    }

    private void updateStatus() {
        if (data.getSendStatus() == Constant.SEND_STATUS_SUCCESS) {
            retry.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
        } else if (data.getSendStatus() == Constant.SEND_STATUS_SENDING) {
            loading.setVisibility(View.VISIBLE);
            retry.setVisibility(View.GONE);
        } else {
            loading.setVisibility(View.GONE);
            retry.setVisibility(View.VISIBLE);
        }
    }

    private SpannableStringBuilder getSpannerContent(final ShareTypeContent bean) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String name = "@" + bean.getNick() + ":";
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#232121")), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                UserDetailActivity.start(CommentListActivity.this, bean.getUid());
            }
        }, 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableString).append(bean.getPostDataBean().getContent());
        return builder;
    }

    private PublicPostBean getSharePublicPostBean() {
        Gson gson = BaseApplication.getAppComponent()
                .getGson();
        PostDataBean bean = gson.fromJson(data.getContent(), PostDataBean.class);
//                            分享文章的ID
        PublicPostBean publicPostBean = new PublicPostBean();
        ShareTypeContent shareTypeContent = bean.getShareContent();
        User author = new User();
        author.setAvatar(shareTypeContent.getAvatar());
        author.setNick(shareTypeContent.getNick());
        author.setObjectId(shareTypeContent.getUid());
        author.setSex(shareTypeContent.isSex());
        author.setAddress(shareTypeContent.getAddress());
        publicPostBean.setAuthor(author);
        publicPostBean.setContent(gson.toJson(shareTypeContent.getPostDataBean()));
        publicPostBean.setMsgType(bean.getShareType());
        publicPostBean.setLikeCount(shareTypeContent.getLikeCount());
        publicPostBean.setCommentCount(shareTypeContent.getCommentCount());
        publicPostBean.setShareCount(shareTypeContent.getShareCount());
        publicPostBean.setObjectId(shareTypeContent.getPid());
//        用于不能设置创建时间，所以把创建时间放在更新时间那里
        publicPostBean.setUpdatedAt(shareTypeContent.getCreateAt());
        return publicPostBean;
    }


    private SpannableStringBuilder getText(PublicPostBean bean) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        SpannableString spannableString = new SpannableString(".");
        spannableString.setSpan(new ImageSpan(this, R.drawable.ic_location, DynamicDrawableSpan.ALIGN_BASELINE), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        String location = "";
        if (bean.getLocation() != null) {
            LocationEvent mLocationEvent = BaseApplication.getAppComponent().getGson().fromJson(bean.getLocation(), LocationEvent.class);
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

    public static void start(Activity activity, PublicPostBean data) {
        Intent intent = new Intent(activity, CommentListActivity.class);
        intent.putExtra("data", data);
        activity.startActivity(intent);
    }

    @Override
    public void onRefresh() {
        presenter.getCommentListData(postId, true, getRefreshTime(true));
    }

    @Override
    public void loadMore() {
        presenter.getCommentListData(postId, false, getRefreshTime(false));

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_item_fragment_share_info_share) {
            if (data.getAuthor().getObjectId().equals(UserManager.getInstance().getCurrentUserObjectId())) {
                ToastUtils.showShortToast("不能转发自己的说说");
            }else {
                EditShareInfoActivity.start(this,Constant.EDIT_TYPE_SHARE,data
                ,false);
                finish();
            }
        } else if (id == R.id.tv_item_fragment_share_info_comment) {
            currentPosition = -1;
            input.setHint("");
            CommonUtils.showSoftInput(this, input);
        } else if (id == R.id.tv_item_fragment_share_info_like) {
            dealLike(data);

        } else if (id == R.id.riv_item_fragment_share_info_avatar) {
            UserDetailActivity.start(this, data.getAuthor().getObjectId());

        } else {
            if (id == R.id.btn_comment_bottom_face) {
                emotionPager.setVisibility(View.VISIBLE);
                face.setVisibility(View.GONE);
                keyboard.setVisibility(View.VISIBLE);
            } else if (id == R.id.btn_comment_bottom_keyboard) {
                face.setVisibility(View.VISIBLE);
                keyboard.setVisibility(View.GONE);
                emotionPager.setVisibility(View.GONE);
            } else if (id == R.id.btn_comment_bottom_send) {
                if (TextUtils.isEmpty(input.getText().toString().trim())) {
                    ToastUtils.showShortToast("输入内容不能为空");
                } else {
                    PublicCommentBean bean = null;
                    if (currentPosition != -1) {
                        bean = commentListAdapter.getData(currentPosition);
                    }
                    showLoadDialog("正在发送.....");
                    presenter.sendCommentData(bean, postId, input.getText().toString().trim());
                }
            }
        }
    }

    private void dealLike(PublicPostBean bean) {
        if (bean.getLikeList() != null && bean.getLikeList().contains(UserManager.getInstance().getCurrentUserObjectId())) {
            ToastUtils.showShortToast("已点赞，取消点赞");
            showLoadDialog("取消赞中...");
            presenter.dealLike(bean.getObjectId(), false);
        } else {
            ToastUtils.showShortToast("未点赞，点赞");
            showLoadDialog("点赞中...");
            presenter.dealLike(bean.getObjectId(), true);
        }
    }
}
