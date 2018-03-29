package com.example.chat.mvp.shareinfo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.ChatApplication;
import com.example.chat.R;
import com.example.chat.adapter.ShareInfoAdapter;
import com.example.chat.base.Constant;
import com.example.chat.bean.ImageItem;
import com.example.chat.bean.NotifyPostResult;
import com.example.chat.bean.post.PublicPostBean;
import com.example.chat.bean.User;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.bean.post.ShareTypeContent;
import com.example.chat.dagger.shareinfo.DaggerShareInfoComponent;
import com.example.chat.dagger.shareinfo.ShareInfoModule;
import com.example.chat.events.CommentEvent;
import com.example.chat.events.NetStatusEvent;
import com.example.chat.events.NotifyEvent;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.EditShare.EditShareInfoActivity;
import com.example.chat.mvp.commentlist.CommentListActivity;
import com.example.chat.mvp.preview.PhotoPreViewActivity;
import com.example.chat.mvp.UserDetail.UserDetailActivity;
import com.example.chat.view.CustomMoveMethod;
import com.example.chat.view.fab.FloatingActionButton;
import com.example.chat.view.fab.FloatingActionsMenu;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/29     22:33
 * QQ:         1981367757
 */

public class ShareInfoFragment extends BaseFragment<List<PublicPostBean>, ShareInfoPresenter> implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, View.OnClickListener {

    private SuperRecyclerView display;
    @Inject
    ShareInfoAdapter shareInfoAdapter;
    private SwipeRefreshLayout refresh;
    private FloatingActionsMenu mMenu;
    private LinearLayout topContainer;
    private RoundAngleImageView topAvatar;
    private TextView topContent;
    private WrappedLinearLayoutManager manager;
    private String uid;
    private User mUser;
    private Disposable disposable;

    @Override
    protected boolean isNeedHeadLayout() {
        if (getArguments().getString("uid") == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_share_info;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_share_info_display);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_fragment_share_info_refresh);
        mMenu = (FloatingActionsMenu) findViewById(R.id.fam_share_info_menu);
        FloatingActionButton normal = (FloatingActionButton) findViewById(R.id.fab_share_info_normal);
        FloatingActionButton video = (FloatingActionButton) findViewById(R.id.fab_share_info_video);
        FloatingActionButton image = (FloatingActionButton) findViewById(R.id.fab_share_info_image);
        topContainer = (LinearLayout) findViewById(R.id.ll_fragment_share_info_top_container);
        topAvatar = topContainer.findViewById(R.id.riv_fragment_share_info_avatar);
        topContent = topContainer.findViewById(R.id.tv_fragment_share_info_nick);
        topContainer.setOnClickListener(this);
        normal.setOnClickListener(this);
        video.setOnClickListener(this);
        image.setOnClickListener(this);
        refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        DaggerShareInfoComponent
                .builder()
                .chatMainComponent(ChatApplication.getChatMainComponent())
                .shareInfoModule(new ShareInfoModule(this))
                .build().inject(this);
        mUser= (User) getArguments().getSerializable(Constant.USER);
        if (mUser!=null) {
            uid = mUser.getObjectId();
        }
        if (uid != null) {
            mMenu.setVisibility(View.GONE);
        } else {
            initTopBar();
        }



        display.setLayoutManager(manager = new WrappedLinearLayoutManager(getContext()));
        display.setLoadMoreFooterView(new LoadMoreFooterView(getContext()));
        display.setOnLoadMoreListener(this);
        mMenu.attachToRecyclerView(display);
        display.setAdapter(shareInfoAdapter);

        display.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                JZVideoPlayer jzvd = view.findViewById(R.id.js_item_fragment_share_info_video_display);
                if (jzvd != null && JZUtils.dataSourceObjectsContainsUri(jzvd.dataSourceObjects, JZMediaManager.getCurrentDataSource())) {
                    JZVideoPlayer.releaseAllVideos();
                }
            }
        });

        presenter.registerEvent(PublicPostBean.class, publicPostBean -> {
            if (!publicPostBean.getObjectId().contains("-") && shareInfoAdapter.getData().contains(publicPostBean)) {
                ToastUtils.showLongToast("更新帖子中...........");
                publicPostBean.setSendStatus(Constant.SEND_STATUS_SENDING);
                shareInfoAdapter.addData(0, publicPostBean);
                presenter.updatePublicPostBean(publicPostBean);
            } else {
                shareInfoAdapter.addData(0, publicPostBean);
                if (AppUtil.isNetworkAvailable()) {
                    refreshOfflineMessage();
                }
            }
        });
        
        shareInfoAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                CommentListActivity.start(getActivity(), shareInfoAdapter.getData(position));
            }


            @Override
            public void onItemChildClick(int position, View view, int id) {
                if (id == R.id.tv_item_fragment_share_info_share) {
                    EditShareInfoActivity.start(getActivity(), Constant.EDIT_TYPE_SHARE
                    ,shareInfoAdapter.getData(position),false);
                } else if (id == R.id.tv_item_fragment_share_info_comment) {
                    CommentListActivity.start(getActivity(), shareInfoAdapter.getData(position));
                } else if (id == R.id.tv_item_fragment_share_info_like) {
                    dealLike(shareInfoAdapter.getData(position));
                } else if (id == R.id.riv_item_fragment_share_info_avatar) {
                    UserDetailActivity.start(getActivity(), shareInfoAdapter.getData(position)
                            .getAuthor().getObjectId());
                } else if (id == R.id.iv_item_fragment_share_info_more) {
                    List<String>  list=new ArrayList<>();
                    list.add("修改");
                    list.add("删除");
                    showChooseDialog("操作方式", list, (parent, view12, position1, id1) -> {
                        if (shareInfoAdapter.getData(position1).getAuthor().getObjectId().equals(UserManager.getInstance().getCurrentUserObjectId())) {
                            List<String> list1 = new ArrayList<>();
                            list1.add("删除");
                            list1.add("修改");
                            showChooseDialog("帖子操作", list1, (adapterView, view1, i, l) -> {
                                hideBaseDialog();
                                if (i == 0) {
                                    showLoadDialog("删除中....");
                                    presenter.deleteShareInfo(shareInfoAdapter.getData(position1), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            dismissLoadDialog();
                                            if (e == null) {
                                                ToastUtils.showShortToast("删除成功");
                                                CommonLogger.e("删除成功");
                                                shareInfoAdapter.removeData(position1);
                                            } else {
                                                ToastUtils.showShortToast("删除失败" + e.toString());
                                                CommonLogger.e("删除失败" + e.toString());
                                            }
                                        }
                                    });
                                } else {
                                    PublicPostBean publicPostBean = shareInfoAdapter.getData(position1);
                                    EditShareInfoActivity.start(getActivity(), publicPostBean.getMsgType(), publicPostBean, true);
                                }
                            });
                        } else {
                            ToastUtils.showShortToast("非帖子作者，不可编辑");
                        }
                    });


                } else if (id == R.id.ll_item_fragment_share_info_share_image) {
                    dealSharePostData(position);
                } else if (id == R.id.ll_item_fragment_share_info_share_container) {
                    dealSharePostData(position);
                } else if (id==R.id.iv_item_fragment_share_info_retry){
                    shareInfoAdapter.getData(position).setSendStatus(Constant.SEND_STATUS_SENDING);
                    shareInfoAdapter.notifyItemChanged(position + shareInfoAdapter.getItemUpCount());
                    presenter.reSendPublicPostBean(shareInfoAdapter.getData(position), shareInfoAdapter.getData(position).getObjectId());
                }else {
                    List<String> imageList = BaseApplication
                            .getAppComponent()
                            .getGson().fromJson(shareInfoAdapter.getData(position - shareInfoAdapter.getItemUpCount())
                                    .getContent(), PostDataBean.class).getImageList();
                    if (imageList != null && imageList.size() > 0) {
                        ArrayList<ImageItem> result = new ArrayList<>();
                        for (String str :
                                imageList) {
                            ImageItem imageItem = new ImageItem();
                            imageItem.setPath(str);
                            result.add(imageItem);
                        }
                        PhotoPreViewActivity.start(getActivity(), id, result, false);
                    } else {
                        dealSharePostData(position);
                    }
                }

            }


        });
        presenter.registerEvent(PublicPostBean.class, publicPostBean -> {
            if (!publicPostBean.getObjectId().contains("-") && shareInfoAdapter.getData().contains(publicPostBean)) {
                ToastUtils.showLongToast("更新帖子中...........");
                publicPostBean.setSendStatus(Constant.SEND_STATUS_SENDING);
                shareInfoAdapter.addData(0, publicPostBean);
                presenter.updatePublicPostBean(publicPostBean);
            } else {
                shareInfoAdapter.addData(0, publicPostBean);
                if (AppUtil.isNetworkAvailable()) {
                    refreshOfflineMessage();
                }
            }
        });
        presenter.registerEvent(CommentEvent.class, likeEvent -> {
            if (likeEvent.getType() == CommentEvent.TYPE_LIKE) {
                PublicPostBean bean = shareInfoAdapter.getPublicPostDataById(likeEvent.getId());
                if (likeEvent.getAction()==CommentEvent.ACTION_ADD) {
                    bean.setLikeCount(bean.getLikeCount() + 1);
                }else {
                    bean.setLikeCount(bean.getLikeCount()-1);
                }
                shareInfoAdapter.addData(bean);
            } else if (likeEvent.getType()==CommentEvent.TYPE_COMMENT){
                notifyCommentAdd(likeEvent.getId());
            }else if (likeEvent.getType()==CommentEvent.TYPE_POST){
                if (likeEvent.getAction()==CommentEvent.ACTION_DELETE){
                    PublicPostBean publicPostBean = new PublicPostBean();
                    publicPostBean.setObjectId(likeEvent.getId());
                    shareInfoAdapter.removeData(publicPostBean);
                }
            }
        });

        presenter.registerEvent(NotifyPostResult.class, new Consumer<NotifyPostResult>() {
            @Override
            public void accept(NotifyPostResult notifyPostResult) throws Exception {
                if (uid != null && !notifyPostResult.getData().getAuthor().equals(uid)) {
                    return;
                }
                topContainer.setVisibility(View.VISIBLE);
                User user=UserCacheManager.getInstance().getUser(notifyPostResult.getData()
                        .getAuthor());
                if (user == null) {
                    topAvatar.setVisibility(View.GONE);
                    topContent.setText("你有一个非好友动态");
                }else {
                    topAvatar.setVisibility(View.VISIBLE);
                    if (getActivity()!=null) {
                        Glide.with(getActivity())
                                .load(user.getAvatar())
                                .into(topAvatar);
                    }
                    topContent.setMovementMethod(new CustomMoveMethod(getContext().getResources().getColor(R.color.blue_500), getContext().getResources().getColor(R.color.blue_500)));
                    topContent.setText(getSpannerContent(user));
                }
            }
        });

    }

    private void refreshOfflineMessage() {
        int size = shareInfoAdapter.getData().size();
        for (int i = 0; i < size; i++) {
            PublicPostBean publicPostBean = shareInfoAdapter.getData(i);
            if (publicPostBean.getSendStatus() == Constant.SEND_STATUS_FAILED) {
                publicPostBean.setSendStatus(Constant.SEND_STATUS_SENDING);
                shareInfoAdapter.notifyItemChanged(i + shareInfoAdapter.getItemUpCount());
                if (publicPostBean.getObjectId().contains("-")) {
                    presenter.reSendPublicPostBean(shareInfoAdapter.getData(i), shareInfoAdapter.getData(i).getObjectId());
                } else {
                    presenter.updatePublicPostBean(shareInfoAdapter.getData(i));
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

    private void initTopBar() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("公共说说");
        toolBarOption.setNeedNavigation(false);
        setToolBar(toolBarOption);
    }


    private SpannableStringBuilder getSpannerContent(final User user) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("好友[");
        String name = user.getNick() + "]:";
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#232121")), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                UserDetailActivity.start((Activity) widget.getContext(), user.getObjectId());
            }
        }, 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableString).append("发布新动态");
        return builder;
    }

    private void dealSharePostData(int position) {
        Gson gson = BaseApplication.getAppComponent()
                .getGson();
        PostDataBean bean = gson.fromJson(shareInfoAdapter.getData(position).getContent(), PostDataBean.class);
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
        publicPostBean.setUpdatedAt(shareTypeContent.getCreateAt());
        CommentListActivity.start(getActivity(), publicPostBean);
    }

    private void notifyCommentAdd(String id) {
        PublicPostBean bean = shareInfoAdapter.getPublicPostDataById(id);
        bean.setCommentCount(bean.getCommentCount() + 1);
        shareInfoAdapter.addData(bean);
    }

    


    @Override
    protected void updateView() {
        RxBusManager.getInstance().post(new NotifyEvent(NotifyEvent.TYPE_NOTIFY_POST));
        presenter.getAllPostData(true, uid, getRefreshTime(true));
    }


    private String getRefreshTime(boolean isRefresh) {
        if (isRefresh) {
            if (shareInfoAdapter.getData().size() == 0) {
                return "0000-00-00 01:00:00";
            }
            if (shareInfoAdapter.getData().size() > 10) {
                return shareInfoAdapter.getData(9).getCreatedAt();
            } else {
                return shareInfoAdapter.getData(shareInfoAdapter.getData().size() - 1)
                        .getCreatedAt();
            }
        } else {
            if (shareInfoAdapter.getData(shareInfoAdapter.getData().size() - 1) != null) {
                return shareInfoAdapter.getData(shareInfoAdapter.getData().size() - 1).getCreatedAt();
            } else {
                return "0000-00-00 01:00:00";
            }
        }
    }


    @Override
    public void updateData(List<PublicPostBean> publicPostBeans) {
        if (refresh.isRefreshing()) {
            if (shareInfoAdapter.getData().size() > 10) {
                shareInfoAdapter.removeEndData(shareInfoAdapter.getData().size() - 10);
            }
            shareInfoAdapter.addData(0, publicPostBeans);
        } else {
            shareInfoAdapter.addData(publicPostBeans);
        }
    }


    @Override
    public void showLoading(String loadingMsg) {
        if (!refresh.isRefreshing()) {
            refresh.setRefreshing(true);
        }
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
    }

    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
            super.showError(errorMsg, listener);
        } else {
            display.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
        }
    }

    @Override
    public void onRefresh() {
        if (topContainer.getVisibility() == View.VISIBLE) {
            topContainer.setVisibility(View.GONE);
        }
        presenter.getAllPostData(true, uid, getRefreshTime(true));
    }


    @Override
    public void onResume() {
        super.onResume();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = RxBusManager.getInstance().registerEvent(NetStatusEvent.class, netStatusEvent -> {
            if (netStatusEvent.isConnected()) {
                ToastUtils.showLongToast("断线重连中..............");
                refreshOfflineMessage();
            }
        }, throwable -> ToastUtils.showLongToast("传递出错" + throwable.getMessage()));
    }


    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void loadMore() {

        if (shareInfoAdapter.getData().size() > 0) {
            presenter.getAllPostData(false, uid, getRefreshTime(false));
        } else {
            presenter.getAllPostData(false, uid, getRefreshTime(false));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (mMenu.isExpanded()) {
            mMenu.collapse();
        }
        if (id == R.id.fab_share_info_video) {
            EditShareInfoActivity.start(getActivity(),Constant.EDIT_TYPE_VIDEO,null,false);
        } else if (id == R.id.fab_share_info_normal) {
            EditShareInfoActivity.start(getActivity(),Constant.EDIT_TYPE_TEXT,null,false);
        } else if (id == R.id.ll_fragment_share_info_top_container) {
            manager.scrollToPositionWithOffset(0, 0);
            refresh.setRefreshing(true);
            onRefresh();
        } else  {
            EditShareInfoActivity.start(getActivity(),Constant.EDIT_TYPE_IMAGE,null,false);
        }
    }

    public static ShareInfoFragment instance(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.USER, user);
        ShareInfoFragment fragment = new ShareInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
