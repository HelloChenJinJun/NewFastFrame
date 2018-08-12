package com.example.chat.mvp.shareinfo;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.ChatApplication;
import com.example.chat.R;
import com.example.chat.adapter.ShareInfoAdapter;
import com.example.chat.base.Constant;
import com.example.chat.bean.ImageItem;
import com.example.chat.bean.post.PublicPostBean;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.dagger.shareinfo.DaggerShareInfoComponent;
import com.example.chat.dagger.shareinfo.ShareInfoModule;
import com.example.chat.events.CommentEvent;
import com.example.chat.events.NetStatusEvent;
import com.example.chat.events.UnReadPostNotifyEvent;
import com.example.chat.events.UpdatePostEvent;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.EditShare.EditShareInfoActivity;
import com.example.chat.mvp.commentlist.CommentListActivity;
import com.example.chat.mvp.commentnotify.CommentNotifyActivity;
import com.example.chat.mvp.preview.PhotoPreViewActivity;
import com.example.chat.mvp.UserDetail.UserDetailActivity;
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
import com.example.commonlibrary.bean.chat.PostNotifyInfo;
import com.example.commonlibrary.bean.chat.PublicPostEntity;
import com.example.commonlibrary.bean.chat.UserEntity;
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

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/29     22:33
 * QQ:         1981367757
 */

public class ShareInfoFragment extends BaseFragment<List<PublicPostBean>, ShareInfoPresenter> implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

    private SuperRecyclerView display;
    @Inject
    ShareInfoAdapter shareInfoAdapter;
    private SwipeRefreshLayout refresh;
    private FloatingActionsMenu mMenu;
    private WrappedLinearLayoutManager manager;
    private UserEntity userEntity;
    private Disposable disposable;
    private boolean isPublic;
    private ImageView titleBg;
    private LinearLayout unReadContainer;
    private ImageView unReadAvatar;
    private TextView unReadCount;
    private ArrayList<PostNotifyInfo> unReadPostNotifyList;

    @Override
    protected boolean isNeedHeadLayout() {
        if (getArguments().getBoolean(Constant.IS_PUBLIC,false)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_share_info;
    }


    @Override
    protected boolean needStatusPadding() {
        return true;
    }



    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_share_info_display);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_fragment_share_info_refresh);
        mMenu = (FloatingActionsMenu) findViewById(R.id.fam_share_info_menu);
        FloatingActionButton normal = (FloatingActionButton) findViewById(R.id.fab_share_info_normal);
        FloatingActionButton video = (FloatingActionButton) findViewById(R.id.fab_share_info_video);
        FloatingActionButton image = (FloatingActionButton) findViewById(R.id.fab_share_info_image);
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
        String uid=getArguments().getString(Constant.ID);
        isPublic=getArguments().getBoolean(Constant.IS_PUBLIC,false);
        userEntity= UserDBManager.getInstance().getUser(uid);
        if (uid.equals(UserManager.getInstance().getCurrentUserObjectId())){
            mMenu.setVisibility(View.VISIBLE);
        }else {
            mMenu.setVisibility(View.GONE);
        }
        initTopBar();
        presenter.registerEvent(UnReadPostNotifyEvent.class, unReadCommentEvent -> {
            updateInfo(unReadCommentEvent!=null?unReadCommentEvent.getPostNotifyBean().getRelatedUser().getAvatar():null);

        });
        display.setLayoutManager(manager = new WrappedLinearLayoutManager(getContext()));
        display.setLoadMoreFooterView(new LoadMoreFooterView(getContext()));
        display.addHeaderView(getHeaderView());
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
                if (jzvd != null &&jzvd.getVisibility()!=View.GONE&&JZUtils.dataSourceObjectsContainsUri(jzvd.dataSourceObjects, JZMediaManager.getCurrentDataSource())) {
                    JZVideoPlayer.releaseAllVideos();
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
                    PublicPostBean data=shareInfoAdapter.getData(position);
                    if (data.getAuthor().getObjectId().equals(UserManager.getInstance().getCurrentUserObjectId())) {
                        ToastUtils.showShortToast("不能转发自己的说说");
                    }else {
                        if (data.getMsgType() == Constant.EDIT_TYPE_SHARE) {
                            Gson gson=BaseApplication
                                    .getAppComponent().getGson();
                            PublicPostBean bean=
                                    MsgManager.getInstance()
                                    .cover(gson.fromJson(gson.fromJson(data.getContent(),PostDataBean.class).getShareContent()
                                            ,PublicPostEntity.class));
                            EditShareInfoActivity.start(getActivity(),Constant.EDIT_TYPE_SHARE,bean
                                    ,false);
                        }else {
                            EditShareInfoActivity.start(getActivity(),Constant.EDIT_TYPE_SHARE,data
                                    ,false);
                        }
                    }
                } else if (id == R.id.tv_item_fragment_share_info_comment) {
                    CommentListActivity.start(getActivity(), shareInfoAdapter.getData(position));
                } else if (id == R.id.tv_item_fragment_share_info_like) {
                    dealLike(shareInfoAdapter.getData(position));
                } else if (id == R.id.riv_item_fragment_share_info_avatar) {
                    UserDetailActivity.start(getActivity(), shareInfoAdapter.getData(position)
                            .getAuthor().getObjectId());
                } else if (id == R.id.iv_item_fragment_share_info_more) {
                    if (shareInfoAdapter.getData(position).getAuthor().getObjectId().equals(UserManager.getInstance().getCurrentUserObjectId())) {
                        List<String> list1 = new ArrayList<>();
                        list1.add("删除");
                        list1.add("修改");
                        showChooseDialog("帖子操作", list1, (adapterView, view1, i, l) -> {
                            hideBaseDialog();
                            if (i == 0) {
                                showLoadDialog("删除中....");
                                presenter.deleteShareInfo(shareInfoAdapter.getData(position), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        dismissLoadDialog();
                                        if (e == null) {
                                            ToastUtils.showShortToast("删除成功");
                                            CommonLogger.e("删除成功");
                                            shareInfoAdapter.removeData(position);
                                        } else {
                                            ToastUtils.showShortToast("删除失败" + e.toString());
                                            CommonLogger.e("删除失败" + e.toString());
                                        }
                                    }
                                });
                            } else {
                                PublicPostBean publicPostBean = shareInfoAdapter.getData(position);
                                EditShareInfoActivity.start(getActivity(), publicPostBean.getMsgType(), publicPostBean, true);
                            }
                        });
                    } else {
                        ToastUtils.showShortToast("非帖子作者，不可编辑");
                    }
                } else if (id == R.id.ll_item_fragment_share_info_share_image) {
                    dealSharePostData(position);
                } else if (id == R.id.ll_item_fragment_share_info_share_container) {
                    dealSharePostData(position);
                } else if (id==R.id.iv_item_fragment_share_info_retry){
                    shareInfoAdapter.getData(position).setSendStatus(Constant.SEND_STATUS_SENDING);
                    shareInfoAdapter.notifyItemChanged(position);
                    presenter.reSendPublicPostBean(shareInfoAdapter.getData(position), shareInfoAdapter.getData(position).getObjectId());
                }else {
                    List<String> imageList = BaseApplication
                            .getAppComponent()
                            .getGson().fromJson(shareInfoAdapter.getData(position)
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
                manager.scrollToPositionWithOffset(0,0);
                if (AppUtil.isNetworkAvailable()) {
                    refreshOfflineMessage();
                }
            }
        });

//        用于接收更新过后的post
        presenter.registerEvent(UpdatePostEvent.class, updatePostEvent -> {
                shareInfoAdapter.addData(updatePostEvent.getPublicPostBean());
        });



        presenter.registerEvent(CommentEvent.class, likeEvent -> {
            if (likeEvent.getType() == CommentEvent.TYPE_LIKE) {
                PublicPostBean bean = shareInfoAdapter.getPublicPostDataById(likeEvent.getId());
                if (likeEvent.getAction()==CommentEvent.ACTION_ADD) {
                    bean.getLikeList().add(UserManager.getInstance().getCurrentUserObjectId());
                    bean.setLikeCount(bean.getLikeCount() + 1);
                }else {
                    bean.setLikeCount(bean.getLikeCount()-1);
                    bean.getLikeList().remove(UserManager.getInstance().getCurrentUserObjectId());
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
    }

    private View getHeaderView() {
        View headerView=getLayoutInflater().inflate(R.layout.view_fragment_share_info_header,null);
        titleBg=headerView.findViewById(R.id.iv_view_fragment_share_info_header_bg);
        unReadContainer=headerView.findViewById(R.id.ll_view_fragment_share_info_header_unread);

        unReadAvatar=headerView.findViewById(R.id.iv_view_fragment_share_info_header_avatar);
        unReadCount=headerView.findViewById(R.id.tv_view_fragment_share_info_header_unread);
        unReadContainer.setOnClickListener(this);
        return headerView;
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




    private void dealSharePostData(int position) {
        Gson gson = BaseApplication.getAppComponent()
                .getGson();
        PostDataBean bean = gson.fromJson(shareInfoAdapter.getData(position).getContent(), PostDataBean.class);
//                            分享文章的ID
        PublicPostBean publicPostBean = MsgManager.getInstance().cover(gson.fromJson(bean.getShareContent(), PublicPostEntity.class));
        CommentListActivity.start(getActivity(), publicPostBean);
    }

    private void notifyCommentAdd(String id) {
        PublicPostBean bean = shareInfoAdapter.getPublicPostDataById(id);
        bean.setCommentCount(bean.getCommentCount() + 1);
        shareInfoAdapter.addData(bean);
    }

    


    @Override
    protected void updateView() {
        Glide.with(this).load(UserManager.getInstance().getCurrentUser()
                .getTitleWallPaper()).into(titleBg);
        updateInfo(null);
        presenter.getAllPostData(isPublic,true, userEntity.getUid(), getRefreshTime(true));
    }

    private void updateInfo(String avatar) {
        unReadPostNotifyList=UserDBManager.getInstance().getUnReadPostNotify();
        int count=0;
        if (unReadPostNotifyList!=null) {
            count = unReadPostNotifyList.size();
        }
        if (count > 0) {
            unReadContainer.setVisibility(View.VISIBLE);
            unReadCount.setText("你有"+count+"条未读消息");
            if (avatar != null) {
                Glide.with(getContext()).load(avatar).into(unReadAvatar);
            }else {
                presenter.getFirstPostNotifyBean();
            }
        }else {
            unReadContainer.setVisibility(View.GONE);
        }
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
        presenter.getAllPostData(isPublic, true, userEntity.getUid(), getRefreshTime(true));
    }


    @Override
    public void onResume() {
        super.onResume();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable=registerNet();
    }

    private Disposable registerNet() {
        return RxBusManager.getInstance().registerEvent(NetStatusEvent.class, netStatusEvent -> {
            if (netStatusEvent.isConnected()) {
                refreshOfflineMessage();
            }
        });
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
            presenter.getAllPostData(isPublic, false, userEntity.getUid(), getRefreshTime(false));
        } else {
            presenter.getAllPostData(isPublic, false, userEntity.getUid(), getRefreshTime(false));
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
        }else if (id==R.id.ll_view_fragment_share_info_header_unread){
            CommentNotifyActivity.start(getActivity(),unReadPostNotifyList);
        }else {
            EditShareInfoActivity.start(getActivity(),Constant.EDIT_TYPE_IMAGE,null,false);
        }
    }

    public static ShareInfoFragment newInstance(String uid,boolean isPublic) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.ID,uid);
        bundle.putBoolean(Constant.IS_PUBLIC,isPublic);
        ShareInfoFragment fragment = new ShareInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                CommonLogger.e(verticalOffset+"");
    }
}
