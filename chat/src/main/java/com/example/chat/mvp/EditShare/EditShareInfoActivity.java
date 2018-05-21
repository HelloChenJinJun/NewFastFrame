package com.example.chat.mvp.EditShare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.R;
import com.example.chat.adapter.EditShareInfoAdapter;
import com.example.chat.base.Constant;
import com.example.chat.bean.ImageItem;
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
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.util.SystemUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.bean.chat.PublicPostEntity;
import com.example.commonlibrary.cusotomview.GridSpaceDecoration;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/25     20:45
 * QQ:         1981367757
 */

public class EditShareInfoActivity extends SlideBaseActivity<PublicPostBean, EditShareInfoPresenter> implements View.OnClickListener {

    private EditText input;
    private TextView location, visibility;
    private JZVideoPlayerStandard video;
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
        shareTitle = (TextView) findViewById(R.id.tv_activity_edit_share_info_title);
        shareCover = (ImageView) findViewById(R.id.iv_activity_edit_share_info_cover);
        input = (EditText) findViewById(R.id.et_activity_edit_share_info_edit);
        video = (JZVideoPlayerStandard) findViewById(R.id.js_activity_edit_share_info_video);
        record = (ImageView) findViewById(R.id.iv_activity_edit_share_info_video);
        RelativeLayout locationContainer = (RelativeLayout) findViewById(R.id.rl_activity_edit_share_info_location);
        RelativeLayout visibilityContainer = (RelativeLayout) findViewById(R.id.rl_activity_edit_share_info_visibility_container);
        location = (TextView) findViewById(R.id.tv_activity_edit_share_info_location);
        visibility = (TextView) findViewById(R.id.tv_activity_edit_share_info_visibility);
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_edit_share_info_display);
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
        isEdit = getIntent().getBooleanExtra(Constant.IS_EDIT, false);
//        编辑信息或分享的信息
        publicPostBean = (PublicPostBean) getIntent().getSerializableExtra(Constant.DATA);
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
        type = getIntent().getIntExtra(Constant.EDIT_TYPE, Constant.EDIT_TYPE_IMAGE);
        if (type == Constant.EDIT_TYPE_IMAGE) {
//            用于接受预览图片界面的对图片的操作事件监听
            disposable = registerPreViewEvent();
//            用于接受图片选择界面传递的图片信息
            presenter.registerEvent(ImageFolderEvent.class, imageFolderEvent -> {
                if (imageFolderEvent.getFrom().equals(ImageFolderEvent.FROM_SELECT)) {
                    if (imageFolderEvent.getImageItems().size() < 8) {
                        ImageItem imageItem = new ImageItem();
                        imageItem.setLayoutType(ImageItem.ITEM_CAMERA);
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
                            .getLayoutType() == ImageItem.ITEM_CAMERA) {
                        return makeMovementFlags(0, 0);
                    }
                    int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    if (editShareAdapter.getData(target.getAdapterPosition() - editShareAdapter.getItemUpCount())
                            .getLayoutType() == ImageItem.ITEM_CAMERA) {
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
                    if (editShareAdapter.getData(position).getItemViewType() == ImageItem.ITEM_CAMERA) {
//                        取消，防止在选择图片预览时添加事件
                        if (!disposable.isDisposed()) {
                            disposable.dispose();
                        }
                        ArrayList<ImageItem> imageItemList = null;
                        if (editShareAdapter != null) {
                            imageItemList = new ArrayList<>(editShareAdapter.getData());
                            if (editShareAdapter.getData(editShareAdapter.getData().size() - 1).getItemViewType() == ImageItem.ITEM_CAMERA) {
                                imageItemList.remove(imageItemList.size() - 1);
                            }
                        }
                        PhotoSelectActivity.start(EditShareInfoActivity.this, null, false, false, imageItemList);
                    } else {
                        ArrayList<ImageItem> imageItemList = new ArrayList<>(editShareAdapter.getData());
                        if (editShareAdapter.getData(editShareAdapter.getData().size() - 1).getItemViewType() == ImageItem.ITEM_CAMERA) {
                            imageItemList.remove(imageItemList.size() - 1);
                        }

                        for (ImageItem item :
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
                            ImageItem imageItem = new ImageItem();
                            imageItem.setLayoutType(ImageItem.ITEM_CAMERA);
                            editShareAdapter.addData(imageItem);
                        } else {
                            editShareAdapter.removeData(position);
                        }
                    }
                }
            });


//            初始化第一个控件
            ImageItem imageItem = new ImageItem();
            imageItem.setLayoutType(ImageItem.ITEM_CAMERA);
            editShareAdapter.addData(imageItem);

            List<ImageItem> result = new ArrayList<>();
            if (postDataBean != null) {
                for (String str :
                        postDataBean.getImageList()) {
                    ImageItem item = new ImageItem();
                    item.setPath(str);
                    item.setLayoutType(ImageItem.ITEM_NORMAL);
                    result.add(item);
                }
                if (result.size() < 8) {
                    ImageItem camera = new ImageItem();
                    camera.setLayoutType(ImageItem.ITEM_CAMERA);
                    result.add(camera);
                    editShareAdapter.refreshData(result);
                } else {
                    editShareAdapter.refreshData(result);
                }
            }
        } else if (type == Constant.EDIT_TYPE_VIDEO) {
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
                        video.setUp(str, JZVideoPlayer.SCREEN_WINDOW_NORMAL, "测试");
                        videoPath = str;
                    } else {
                        Glide.with(this).load(str).into(video.thumbImageView);
                        thumbImage = str;
                    }
                }
            }
        } else if (type == Constant.EDIT_TYPE_SHARE) {
//            处理分享的信息，主要把分享的内容转化为要本地,再分享
            if (isEdit) {
                publicPostBean =
                        MsgManager.getInstance()
                                .cover(gson.fromJson(gson.fromJson(publicPostBean.getContent(), PostDataBean.class).getShareContent()
                                        , PublicPostEntity.class));
                postDataBean = BaseApplication.getAppComponent().getGson().fromJson(publicPostBean.getContent(), PostDataBean.class);
            }
            if (publicPostBean.getMsgType() == Constant.EDIT_TYPE_IMAGE) {
                Glide.with(this).load(postDataBean.getImageList().get(0))
                        .into(shareCover);
            } else if (publicPostBean.getMsgType() == Constant.EDIT_TYPE_VIDEO) {
                for (String url :
                        postDataBean.getImageList()) {
                    if (!url.endsWith(".mp4")) {
                        Glide.with(this).load(url)
                                .into(shareCover);
                    }
                }
            } else if (publicPostBean.getMsgType() == Constant.EDIT_TYPE_TEXT) {
                shareCover.setVisibility(View.GONE);
            }
            shareTitle.setText(postDataBean.getContent());
            display.setVisibility(View.GONE);
            video.setVisibility(View.GONE);
            record.setVisibility(View.GONE);
            shareContainer.setVisibility(View.VISIBLE);
        } else if (type == Constant.EDIT_TYPE_TEXT) {
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
        ArrayList<ImageItem> imageItemList = null;
        if (type == Constant.EDIT_TYPE_IMAGE) {
            imageItemList = new ArrayList<>(editShareAdapter.getData());
            if (editShareAdapter.getData(editShareAdapter.getData().size() - 1).getItemViewType() == ImageItem.ITEM_CAMERA) {
                imageItemList.remove(imageItemList.size() - 1);
            }
        }
        if (TextUtils.isEmpty(input.getText().toString().trim())) {
            ToastUtils.showShortToast("内容不能为空!");
            return;
        }
        if (type == Constant.EDIT_TYPE_IMAGE) {
            if (imageItemList.size() == 0) {
                ToastUtils.showShortToast("图片不能为空");
                return;
            }
        } else if (type == Constant.EDIT_TYPE_VIDEO) {
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
            if (type == Constant.EDIT_TYPE_IMAGE) {
                List<String> photoUrls = new ArrayList<>();
                if (imageItemList != null) {
                    for (ImageItem imageItem :
                            imageItemList) {
                        photoUrls.add(imageItem.getPath());
                    }
                }
                bean.setImageList(photoUrls);
            } else if (type == Constant.EDIT_TYPE_VIDEO) {
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
                if (editShareAdapter.getData(editShareAdapter.getData().size() - 1).getItemViewType() != ImageItem.ITEM_CAMERA) {
                    editShareAdapter.removeData(photoPreViewEvent.getImageItem());
                    ImageItem imageItem = new ImageItem();
                    imageItem.setLayoutType(ImageItem.ITEM_CAMERA);
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
            NearbyListActivity.start(this, currentLocationEvent, Constant.REQUEST_CODE_LOCATION);
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
                video.setVisibility(View.VISIBLE);
                record.setVisibility(View.GONE);
                video.setUp(videoPath, JZVideoPlayer.SCREEN_WINDOW_NORMAL, "测试");
                Bitmap bitmap = SystemUtil.getVideoThumbnail(videoPath, DensityUtil.getScreenWidth(this), DensityUtil.getScreenHeight(this)
                        , MediaStore.Images.Thumbnails.MINI_KIND);
                thumbImage = SystemUtil.bitmapToFile(bitmap);
                video.thumbImageView.setImageBitmap(bitmap);
            } else if (requestCode == Constant.REQUEST_CODE_LOCATION) {
                LocationEvent locationEvent = (LocationEvent) data.getSerializableExtra(Constant.LOCATION);
                updateLocation(locationEvent);
            }
        }
    }


    public static void start(Activity activity, int type, PublicPostBean data, boolean isEdit) {
        Intent intent = new Intent(activity, EditShareInfoActivity.class);
        intent.putExtra(Constant.EDIT_TYPE, type);
        intent.putExtra(Constant.DATA, data);
        intent.putExtra(Constant.IS_EDIT, isEdit);
        activity.startActivity(intent);
    }
}
