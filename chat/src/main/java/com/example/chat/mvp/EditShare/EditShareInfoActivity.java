package com.example.chat.mvp.EditShare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.chat.R;
import com.example.chat.adapter.EditShareInfoAdapter;
import com.example.chat.base.ChatBaseActivity;
import com.example.chat.base.ConstantUtil;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.bean.post.PublicPostBean;
import com.example.chat.dagger.EditShare.DaggerEditShareInfoComponent;
import com.example.chat.dagger.EditShare.EditShareInfoModule;
import com.example.chat.events.ImageFolderEvent;
import com.example.chat.events.LocationEvent;
import com.example.chat.events.PhotoPreViewEvent;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.NewLocationManager;
import com.example.chat.mvp.commentlist.CommentListActivity;
import com.example.chat.mvp.nearbyList.NearbyListActivity;
import com.example.chat.mvp.photoSelect.PhotoSelectActivity;
import com.example.chat.mvp.preview.PhotoPreViewActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.decoration.GridSpaceDecoration;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.bean.chat.PublicPostEntity;
import com.example.commonlibrary.customview.ToolBarOption;
import com.example.commonlibrary.manager.video.DefaultVideoController;
import com.example.commonlibrary.manager.video.DefaultVideoPlayer;
import com.example.commonlibrary.manager.video.ListVideoManager;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.SystemUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/25     20:45
 * QQ:         1981367757
 */

public class EditShareInfoActivity extends ChatBaseActivity<PublicPostBean, EditShareInfoPresenter> implements View.OnClickListener {

    private EditText input;
    private TextView location, visibility;
    private DefaultVideoPlayer video;
    private SuperRecyclerView display;

    private TextView shareTitle;
    private ImageView shareCover;
    private View shareContainer;

    //    是否修改说说界面
    private boolean isEdit;

    private LocationEvent currentLocationEvent;

    @Inject
    EditShareInfoAdapter editShareAdapter;

    //    编辑的说说类型
    private int type;
    private Disposable disposable;
    private PublicPostBean publicPostBean;
    //    视频路径
    private String videoPath;
    //    视频封面路径
    private String thumbImage;
    private ImageView record;
    private Gson gson = BaseApplication.getAppComponent().getGson();


    @Override
    public void updateData(PublicPostBean publicPostBean) {
        RxBusManager.getInstance().post(publicPostBean);
        finish();
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_edit_share_info;
    }

    @Override
    protected void initView() {
        shareContainer = findViewById(R.id.cv_activity_edit_share_info_share_container);
        shareTitle = findViewById(R.id.tv_activity_edit_share_info_title);
        shareCover = findViewById(R.id.iv_activity_edit_share_info_cover);
        input = findViewById(R.id.et_activity_edit_share_info_edit);
        video = findViewById(R.id.dvp_activity_edit_share_info_video);
        record = findViewById(R.id.iv_activity_edit_share_info_video);
        RelativeLayout locationContainer = findViewById(R.id.rl_activity_edit_share_info_location);
        RelativeLayout visibilityContainer = findViewById(R.id.rl_activity_edit_share_info_visibility_container);
        location = findViewById(R.id.tv_activity_edit_share_info_location);
        visibility = findViewById(R.id.tv_activity_edit_share_info_visibility);
        display = findViewById(R.id.srcv_activity_edit_share_info_display);
        locationContainer.setOnClickListener(this);
        visibilityContainer.setOnClickListener(this);
        shareContainer.setOnClickListener(this);
        record.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        DaggerEditShareInfoComponent.builder().chatMainComponent(getChatMainComponent())
                .editShareInfoModule(new EditShareInfoModule(this)).build()
                .inject(this);
        NewLocationManager.getInstance().startLocation();
        isEdit = getIntent().getBooleanExtra(ConstantUtil.IS_EDIT, false);
        //        编辑信息或分享的信息
        publicPostBean = (PublicPostBean) getIntent().getSerializableExtra(ConstantUtil.DATA);
        PostDataBean postDataBean = null;
        if (publicPostBean != null) {
            postDataBean = BaseApplication.getAppComponent().getGson().fromJson(publicPostBean.getContent(), PostDataBean.class);
            if (isEdit) {
                //            编辑说说信息，赋值到界面
                if (postDataBean != null) {
                    input.setText(postDataBean.getContent());
                    updateLocation(BaseApplication.getAppComponent()
                            .getGson().fromJson(publicPostBean.getLocation(), LocationEvent.class));
                }
            }
        }
        //        监控位置信息
        presenter.registerEvent(LocationEvent.class, locationEvent -> updateLocation(locationEvent));
        type = getIntent().getIntExtra(ConstantUtil.EDIT_TYPE, ConstantUtil.EDIT_TYPE_IMAGE);
        if (type == ConstantUtil.EDIT_TYPE_IMAGE) {
            //            用于接受预览图片界面的对图片的操作事件监听
            disposable = registerPreViewEvent();
            //            用于接受图片选择界面传递的图片信息
            presenter.registerEvent(ImageFolderEvent.class, imageFolderEvent -> {
                if (imageFolderEvent.getFrom().equals(ImageFolderEvent.FROM_SELECT)) {
                    if (imageFolderEvent.getImageItems().size() < 8) {
                        SystemUtil.ImageItem imageItem = new SystemUtil.ImageItem();
                        imageItem.setLayoutType(SystemUtil.ImageItem.ITEM_CAMERA);
                        imageFolderEvent.getImageItems().add(imageItem);
                    }
                    editShareAdapter.refreshData(imageFolderEvent.getImageItems());
                }
            });
            display.setVisibility(View.VISIBLE);
            video.setVisibility(View.GONE);
            record.setVisibility(View.GONE);
            display.setLayoutManager(new WrappedGridLayoutManager(this, 4));
            display.addItemDecoration(new GridSpaceDecoration(2, 10, true));
            //            设置图片之间拖拽
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
                @Override
                public boolean isLongPressDragEnabled() {
                    return true;
                }

                @Override
                public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    // 如果你不想上下拖动，可以将 dragFlags = 0
                    if (editShareAdapter.getData(viewHolder.getAdapterPosition() - editShareAdapter.getItemUpCount())
                            .getLayoutType() == SystemUtil.ImageItem.ITEM_CAMERA) {
                        return makeMovementFlags(0, 0);
                    }
                    int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    if (editShareAdapter.getData(target.getAdapterPosition() - editShareAdapter.getItemUpCount())
                            .getLayoutType() == SystemUtil.ImageItem.ITEM_CAMERA) {
                        return false;
                    } else {
                        Collections.swap(editShareAdapter.getData(), viewHolder.getAdapterPosition() - editShareAdapter.getItemUpCount()
                                , target.getAdapterPosition() - editShareAdapter.getItemUpCount());
                        editShareAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                }
            });
            itemTouchHelper.attachToRecyclerView(display);
            display.setAdapter(editShareAdapter);

            editShareAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    if (editShareAdapter.getData(position).getItemViewType() == SystemUtil.ImageItem.ITEM_CAMERA) {
                        //                        取消，防止在选择图片预览时添加事件
                        if (!disposable.isDisposed()) {
                            disposable.dispose();
                        }
                        ArrayList<SystemUtil.ImageItem> imageItemList = null;
                        if (editShareAdapter != null) {
                            imageItemList = new ArrayList<>(editShareAdapter.getData());
                            if (editShareAdapter.getData(editShareAdapter.getData().size() - 1).getItemViewType() == SystemUtil.ImageItem.ITEM_CAMERA) {
                                imageItemList.remove(imageItemList.size() - 1);
                            }
                        }
                        PhotoSelectActivity.start(EditShareInfoActivity.this, null, false, false, imageItemList);
                    } else {
                        ArrayList<SystemUtil.ImageItem> imageItemList = new ArrayList<>(editShareAdapter.getData());
                        if (editShareAdapter.getData(editShareAdapter.getData().size() - 1).getItemViewType() == SystemUtil.ImageItem.ITEM_CAMERA) {
                            imageItemList.remove(imageItemList.size() - 1);
                        }

                        for (SystemUtil.ImageItem item :
                                imageItemList) {
                            item.setCheck(true);
                        }

                        PhotoPreViewActivity.start(EditShareInfoActivity.this, position, imageItemList, true);
                    }
                }


                @Override
                public void onItemChildClick(int position, View view, int id) {
                    if (id == R.id.iv_item_activity_edit_share_info_normal_delete) {
                        if (editShareAdapter.getData().size() == 8) {
                            editShareAdapter.removeData(position);
                            SystemUtil.ImageItem imageItem = new SystemUtil.ImageItem();
                            imageItem.setLayoutType(SystemUtil.ImageItem.ITEM_CAMERA);
                            editShareAdapter.addData(imageItem);
                        } else {
                            editShareAdapter.removeData(position);
                        }
                    }
                }
            });


            //            初始化第一个控件
            SystemUtil.ImageItem imageItem = new SystemUtil.ImageItem();
            imageItem.setLayoutType(SystemUtil.ImageItem.ITEM_CAMERA);
            editShareAdapter.addData(imageItem);

            List<SystemUtil.ImageItem> result = new ArrayList<>();
            if (postDataBean != null) {
                for (String str :
                        postDataBean.getImageList()) {
                    SystemUtil.ImageItem item = new SystemUtil.ImageItem();
                    item.setPath(str);
                    item.setLayoutType(SystemUtil.ImageItem.ITEM_NORMAL);
                    result.add(item);
                }
                if (result.size() < 8) {
                    SystemUtil.ImageItem camera = new SystemUtil.ImageItem();
                    camera.setLayoutType(SystemUtil.ImageItem.ITEM_CAMERA);
                    result.add(camera);
                    editShareAdapter.refreshData(result);
                } else {
                    editShareAdapter.refreshData(result);
                }
            }
        } else if (type == ConstantUtil.EDIT_TYPE_VIDEO) {
            display.setVisibility(View.GONE);
            video.setVisibility(View.GONE);
            record.setVisibility(View.VISIBLE);

            //            赋值
            if (postDataBean != null) {
                record.setVisibility(View.GONE);
                video.setVisibility(View.VISIBLE);
                for (String str :
                        postDataBean.getImageList()) {
                    if (str.endsWith("mp4")) {
                        videoPath = str;
                        video.setUp(str, null);
                    } else {
                        thumbImage = str;
                        video.setImageCover(thumbImage);
                    }
                }
            }
        } else if (type == ConstantUtil.EDIT_TYPE_SHARE) {
            //            处理分享的信息，主要把分享的内容转化为要本地,再分享
            if (isEdit) {
                publicPostBean =
                        MsgManager.getInstance()
                                .cover(gson.fromJson(gson.fromJson(publicPostBean.getContent(), PostDataBean.class).getShareContent()
                                        , PublicPostEntity.class));
                postDataBean = BaseApplication.getAppComponent().getGson().fromJson(publicPostBean.getContent(), PostDataBean.class);
            }
            if (publicPostBean.getMsgType() == ConstantUtil.EDIT_TYPE_IMAGE) {
                Glide.with(this).load(postDataBean.getImageList().get(0))
                        .into(shareCover);
            } else if (publicPostBean.getMsgType() == ConstantUtil.EDIT_TYPE_VIDEO) {
                for (String url :
                        postDataBean.getImageList()) {
                    if (!url.endsWith(".mp4")) {
                        Glide.with(this).load(url)
                                .into(shareCover);
                    }
                }
            } else if (publicPostBean.getMsgType() == ConstantUtil.EDIT_TYPE_TEXT) {
                shareCover.setVisibility(View.GONE);
            }
            shareTitle.setText(postDataBean.getContent());
            display.setVisibility(View.GONE);
            video.setVisibility(View.GONE);
            record.setVisibility(View.GONE);
            shareContainer.setVisibility(View.VISIBLE);
        } else if (type == ConstantUtil.EDIT_TYPE_TEXT) {
            //            正常的文本内容
            display.setVisibility(View.GONE);
            video.setVisibility(View.GONE);
            record.setVisibility(View.GONE);
        }
        initToolBar();
    }

    private void initToolBar() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("编辑");
        toolBarOption.setNeedNavigation(true);
        if (isEdit) {
            toolBarOption.setRightText("更新");
        } else {
            toolBarOption.setRightText("发送");
        }
        toolBarOption.setRightListener(view -> createOrUpdatePostInfo());
        setToolBar(toolBarOption);
    }

    private void createOrUpdatePostInfo() {
        ArrayList<SystemUtil.ImageItem> imageItemList = null;
        if (type == ConstantUtil.EDIT_TYPE_IMAGE) {
            imageItemList = new ArrayList<>(editShareAdapter.getData());
            if (editShareAdapter.getData(editShareAdapter.getData().size() - 1).getItemViewType() == SystemUtil.ImageItem.ITEM_CAMERA) {
                imageItemList.remove(imageItemList.size() - 1);
            }
        }
        if (TextUtils.isEmpty(input.getText().toString().trim())) {
            ToastUtils.showShortToast("内容不能为空!");
            return;
        }
        if (type == ConstantUtil.EDIT_TYPE_IMAGE) {
            if (imageItemList.size() == 0) {
                ToastUtils.showShortToast("图片不能为空");
                return;
            }
        } else if (type == ConstantUtil.EDIT_TYPE_VIDEO) {
            if (videoPath == null) {
                ToastUtils.showShortToast("视频录制不能为空");
                return;
            }
        }
        if (isEdit) {
            //            PostDataBean bean=App.getAppComponent().getGson().fromJson(shareMessage.getContent(),PostDataBean.class);
            PostDataBean bean = new PostDataBean();
            bean.setContent(input.getText().toString().trim());
            publicPostBean.setLocation(getRealLocation());
            if (type == ConstantUtil.EDIT_TYPE_IMAGE) {
                List<String> photoUrls = new ArrayList<>();
                if (imageItemList != null) {
                    for (SystemUtil.ImageItem imageItem :
                            imageItemList) {
                        photoUrls.add(imageItem.getPath());
                    }
                }
                bean.setImageList(photoUrls);
            } else if (type == ConstantUtil.EDIT_TYPE_VIDEO) {
                List<String> list = new ArrayList<>();
                list.add(thumbImage);
                list.add(videoPath);
                bean.setImageList(list);
                bean.setImageList(list);
            }
            publicPostBean.setMsgType(type);
            publicPostBean.setContent(BaseApplication.getAppComponent()
                    .getGson().toJson(bean));
            presenter.updatePublicPostBean(publicPostBean);
            return;
        }
        presenter.sendPublicPostBean(type, input.getText().toString().trim(), imageItemList, videoPath, thumbImage, publicPostBean, getRealLocation());
    }

    private String getRealLocation() {
        if (currentLocationEvent != null) {
            return BaseApplication.getAppComponent()
                    .getGson().toJson(currentLocationEvent);
        } else {
            return null;
        }
    }

    private Disposable registerPreViewEvent() {
        return RxBusManager.getInstance().registerEvent(PhotoPreViewEvent.class, photoPreViewEvent -> {
            if (photoPreViewEvent.getType() == PhotoPreViewEvent.TYPE_ADD) {
                if (editShareAdapter.getData().size() == 8) {
                    editShareAdapter.removeData(7);
                    editShareAdapter.addData(photoPreViewEvent.getImageItem());
                } else {
                    editShareAdapter.addData(editShareAdapter.getData().size() - 1, photoPreViewEvent.getImageItem());
                }
            } else {
                if (editShareAdapter.getData(editShareAdapter.getData().size() - 1).getItemViewType() != SystemUtil.ImageItem.ITEM_CAMERA) {
                    editShareAdapter.removeData(photoPreViewEvent.getImageItem());
                    SystemUtil.ImageItem imageItem = new SystemUtil.ImageItem();
                    imageItem.setLayoutType(SystemUtil.ImageItem.ITEM_CAMERA);
                    editShareAdapter.addData(imageItem);
                } else {
                    editShareAdapter.removeData(photoPreViewEvent.getImageItem());
                }

            }
        }, throwable -> ToastUtils.showShortToast("传递过程出错" + throwable.getMessage()));
    }

    private void updateLocation(LocationEvent locationEvent) {
        if (locationEvent == null) {
            currentLocationEvent = null;
            location.setText("不显示位置");
        } else {
            if (locationEvent.getTitle().equals("不显示位置")) {
                currentLocationEvent = null;
            } else {
                currentLocationEvent = locationEvent;
            }
            location.setText(locationEvent.getTitle());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_activity_edit_share_info_location) {
            NearbyListActivity.start(this, currentLocationEvent, ConstantUtil.REQUEST_CODE_LOCATION);
        } else if (id == R.id.rl_activity_edit_share_info_visibility_container) {

        } else if (id == R.id.iv_activity_edit_share_info_video) {
            videoPath = SystemUtil.recorderVideo(this, SystemUtil.REQUEST_CODE_VIDEO_RECORDER);
        } else if (id == R.id.cv_activity_edit_share_info_share_container) {
            CommentListActivity.start(this, publicPostBean);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (disposable != null && disposable.isDisposed()) {
            disposable = registerPreViewEvent();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        NewLocationManager.getInstance().clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SystemUtil.REQUEST_CODE_VIDEO_RECORDER) {
                record.setVisibility(View.GONE);
                ToastUtils.showShortToast("正在解析视频，请稍后.....");
                addDisposable(Observable.timer(6, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Glide.with(EditShareInfoActivity.this).asBitmap().load(videoPath)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        ToastUtils.showShortToast("解析视频成功");
                                        video.setVisibility(View.VISIBLE);
                                        thumbImage = SystemUtil.bitmapToFile(resource);
                                        ((DefaultVideoController) video.setUp(videoPath, null).getController()).getImageCover()
                                                .setImageBitmap(resource);
                                    }
                                });
                    }
                }));
            } else if (requestCode == ConstantUtil.REQUEST_CODE_LOCATION) {
                LocationEvent locationEvent = (LocationEvent) data.getSerializableExtra(ConstantUtil.LOCATION);
                updateLocation(locationEvent);
            }
        }
    }


    public static void start(Activity activity, int type, PublicPostBean data, boolean isEdit) {
        Intent intent = new Intent(activity, EditShareInfoActivity.class);
        intent.putExtra(ConstantUtil.EDIT_TYPE, type);
        intent.putExtra(ConstantUtil.DATA, data);
        intent.putExtra(ConstantUtil.IS_EDIT, isEdit);
        activity.startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        if (!ListVideoManager.getInstance().onBackPressed()) {
            super.onBackPressed();
        }
    }
}
