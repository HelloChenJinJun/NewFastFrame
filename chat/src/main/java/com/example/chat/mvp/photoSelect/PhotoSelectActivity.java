package com.example.chat.mvp.photoSelect;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.adapter.PhotoSelectAdapter;
import com.example.chat.base.ChatBaseActivity;
import com.example.chat.base.ConstantUtil;
import com.example.chat.events.ImageFolderEvent;
import com.example.chat.events.PhotoPreViewEvent;
import com.example.chat.mvp.bottomFolder.CustomBottomFragment;
import com.example.chat.mvp.preview.PhotoPreViewActivity;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemChildClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.cusotomview.GridSpaceDecoration;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.PermissionUtil;
import com.example.commonlibrary.utils.SystemUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;


/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/26     20:21
 * QQ:         1981367757
 */

public class PhotoSelectActivity extends ChatBaseActivity implements SystemUtil.OnImageLoadListener, View.OnClickListener {

    private SuperRecyclerView display;


    private PhotoSelectAdapter photoSelectAdapter;


    private List<SystemUtil.ImageFolder> imageFolderList;

    private TextView preView;
    private String takePhotoPath;
    private ArrayList<SystemUtil.ImageItem> selectedImageItemList;
    private Button all;
    private boolean isOne;
    private Uri cropUri;
    private boolean isCrop;

    @Override
    public void updateData(Object o) {

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
        return R.layout.activity_photo_select;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_photo_select_display);
        all = (Button) findViewById(R.id.btn_activity_photo_select_all);
        preView = (TextView) findViewById(R.id.tv_activity_photo_select_preview);
        all.setOnClickListener(this);
        preView.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        photoSelectAdapter = new PhotoSelectAdapter();
        isOne = getIntent().getBooleanExtra(ConstantUtil.IS_ONE, false);
        isCrop = getIntent().getBooleanExtra(ConstantUtil.IS_CROP, false);
        photoSelectAdapter.setOne(isOne);
        if (isOne) {
            preView.setVisibility(View.GONE);
        }
        display.setLayoutManager(new WrappedGridLayoutManager(this, 3));
        display.addItemDecoration(new GridSpaceDecoration(3, 5, false));
        display.setAdapter(photoSelectAdapter);
        photoSelectAdapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {
            @Override
            public void onItemChildClick(int position, View view, int id) {
                if (id == R.id.cb_item_activity_photo_select_normal_check) {
                    CheckBox checkBox = (CheckBox) view;
                    photoSelectAdapter.getData(position).setCheck(checkBox.isChecked());
                    if (checkBox.isChecked()) {
                        if (!selectedImageItemList.contains(photoSelectAdapter.getData(position))) {
                            selectedImageItemList.add(photoSelectAdapter.getData(position));
                        }
                    } else {
                        if (selectedImageItemList.contains(photoSelectAdapter.getData(position))) {
                            selectedImageItemList.remove(photoSelectAdapter.getData(position));
                        }
                    }
                    photoSelectAdapter.setSelectedSize(selectedImageItemList.size());
                    updateText();
                }
            }


            @Override
            public void onItemClick(int position, View view) {
                if (position != 0) {
                    if (isOne) {
                        if (isCrop) {
                            cropUri = SystemUtil.cropPhoto(PhotoSelectActivity.this, photoSelectAdapter.getData(position).getPath());
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra(ConstantUtil.PATH, photoSelectAdapter.getData(position).getPath());
                            intent.putExtra(ConstantUtil.FROM, getIntent().getStringExtra(ConstantUtil.FROM));
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                        return;
                    }
                    ArrayList<SystemUtil.ImageItem> list = new ArrayList<>();
                    list.addAll(photoSelectAdapter.getData());
                    list.remove(0);
                    PhotoPreViewActivity.start(PhotoSelectActivity.this, position - 1, list, true);
                } else {
                    PermissionUtil.requestTakePhoto(PhotoSelectActivity.this, new PermissionUtil.RequestPermissionCallBack() {
                        @Override
                        public void onRequestPermissionSuccess() {
                            takePhotoPath = SystemUtil.takePhoto(PhotoSelectActivity.this, SystemUtil.REQUEST_CODE_TAKE_PHOTO);
                        }

                        @Override
                        public void onRequestPermissionFailure() {

                        }
                    });
                }
            }
        });
        SystemUtil.getAllImageFolder(this, this);
        addDisposable(RxBusManager.getInstance().registerEvent(ImageFolderEvent.class, imageFolderEvent -> {
            if (imageFolderEvent.getFrom().equals(ImageFolderEvent.FROM_FOLDER)) {
                SystemUtil.ImageItem imageItem = new SystemUtil.ImageItem();
                imageItem.setLayoutType(SystemUtil.ImageItem.ITEM_CAMERA);
                int size = imageFolderEvent.getImageItems().size();
                for (int i = 0; i < size; i++) {
                    for (SystemUtil.ImageItem select :
                            selectedImageItemList) {
                        if (imageFolderEvent.getImageItems().get(i).equals(select)) {
                            imageFolderEvent.getImageItems().set(i, select);
                        }
                    }
                }
                imageFolderEvent.getImageItems().add(0, imageItem);
                photoSelectAdapter.refreshData(imageFolderEvent.getImageItems());
                currentImageFolderPosition = imageFolderEvent.getImageFolderPosition();
                all.setText(imageFolderEvent.getImageFolderName());
            }

        }, throwable -> ToastUtils.showShortToast("传递出错" + throwable.getMessage())));

        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setTitle("选择图片");
        toolBarOption.setRightText("完成(0/8)");
        toolBarOption.setRightListener(view -> {
            RxBusManager.getInstance().post(new ImageFolderEvent(ImageFolderEvent.FROM_SELECT, selectedImageItemList, currentImageFolderPosition));
            finish();
        });
        setToolBar(toolBarOption);
        RxBusManager.getInstance().registerEvent(PhotoPreViewEvent.class, photoPreViewEvent -> {
            if (photoPreViewEvent.getType() == PhotoPreViewEvent.TYPE_ADD) {
                if (selectedImageItemList.size() == 8) {
                    return;
                }
                photoPreViewEvent.getImageItem().setCheck(true);
                if (!selectedImageItemList.contains(photoPreViewEvent.getImageItem())) {
                    selectedImageItemList.add(photoPreViewEvent.getImageItem());
                }
            } else {
                photoPreViewEvent.getImageItem().setCheck(false);
                if (selectedImageItemList.contains(photoPreViewEvent.getImageItem())) {
                    selectedImageItemList.remove(photoPreViewEvent.getImageItem());
                }
            }
            photoSelectAdapter.addData(photoPreViewEvent.getImageItem());
            photoSelectAdapter.setSelectedSize(selectedImageItemList.size());
            updateText();
        }, throwable -> ToastUtils.showShortToast("传递过程出错" + throwable.getMessage()));

    }

    private void updateText() {
        if (selectedImageItemList != null) {
            right.setText(String.format("完成(%d/8)", selectedImageItemList.size()));
            preView.setText("预览(" + selectedImageItemList.size() + ")");
        }
    }

    @Override
    public void onImageLoaded(List<SystemUtil.ImageFolder> imageFolderList) {
        this.imageFolderList = imageFolderList;
        if (imageFolderList.size() > 0) {
            List<SystemUtil.ImageItem> list = new ArrayList<>(imageFolderList.get(0).getAllImages());
            for (SystemUtil.ImageItem item :
                    list) {
                item.setLayoutType(SystemUtil.ImageItem.ITEM_NORMAL);
            }
            SystemUtil.ImageItem imageItem = new SystemUtil.ImageItem();
            imageItem.setLayoutType(SystemUtil.ImageItem.ITEM_CAMERA);
            list.add(0, imageItem);
            photoSelectAdapter.refreshData(list);
        }
        selectedImageItemList = (ArrayList<SystemUtil.ImageItem>) getIntent().getSerializableExtra(ConstantUtil.DATA);
        if (selectedImageItemList == null) {
            selectedImageItemList = new ArrayList<>();
        }
        for (SystemUtil.ImageItem item :
                selectedImageItemList) {
            if (!photoSelectAdapter.getData().contains(item)) {
                item.setCheck(true);
            }
        }
        photoSelectAdapter.addData(new ArrayList<>(selectedImageItemList));
        photoSelectAdapter.getLayoutManager().scrollToPosition(0);
        updateText();
    }


    private int currentImageFolderPosition = 0;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_activity_photo_select_all) {
            CustomBottomFragment.newInstance((ArrayList<SystemUtil.ImageFolder>) imageFolderList, currentImageFolderPosition).show(getSupportFragmentManager(), "dialog");
        } else if (id == R.id.tv_activity_photo_select_preview) {
            if (selectedImageItemList != null && selectedImageItemList.size() > 0) {
                PhotoPreViewActivity.start(this, 0, selectedImageItemList, true);
            } else {
                ToastUtils.showShortToast("请选择图片");
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SystemUtil.REQUEST_CODE_TAKE_PHOTO) {
                if (!isOne) {
                    SystemUtil.ImageItem imageItem = new SystemUtil.ImageItem();
                    imageItem.setPath(takePhotoPath);
                    imageItem.setLayoutType(SystemUtil.ImageItem.ITEM_NORMAL);
                    if (!selectedImageItemList.contains(imageItem)) {
                        selectedImageItemList.add(imageItem);
                    }
                    RxBusManager.getInstance().post(new ImageFolderEvent(ImageFolderEvent.FROM_SELECT, selectedImageItemList, currentImageFolderPosition));
                    finish();
                } else {
                    if (isCrop) {
                        cropUri = SystemUtil.cropPhoto(this, takePhotoPath);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(ConstantUtil.PATH, takePhotoPath);
                        intent.putExtra(ConstantUtil.FROM, getIntent().getStringExtra(ConstantUtil.FROM));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            } else if (requestCode == com.example.commonlibrary.utils.SystemUtil.REQUEST_CODE_CROP) {
                Intent intent = new Intent();
                intent.putExtra(ConstantUtil.PATH, cropUri.toString());
                intent.putExtra(ConstantUtil.FROM, getIntent().getStringExtra(ConstantUtil.FROM));
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    public static void start(Activity activity, String from, boolean isOne, boolean isCrop, ArrayList<SystemUtil.ImageItem> selectedImageItemList) {
        Intent intent = new Intent(activity, PhotoSelectActivity.class);
        intent.putExtra(ConstantUtil.DATA, selectedImageItemList);
        intent.putExtra(ConstantUtil.IS_ONE, isOne);
        intent.putExtra(ConstantUtil.IS_CROP, isCrop);
        intent.putExtra(ConstantUtil.FROM, from);
        if (isOne) {
            activity.startActivityForResult(intent, com.example.commonlibrary.utils.SystemUtil.REQUEST_CODE_ONE_PHOTO);
        } else {
            activity.startActivity(intent);
        }
    }

    public static void start(Fragment fragment, String from, boolean isOne, boolean isCrop, ArrayList<SystemUtil.ImageItem> selectedImageItemList) {
        Intent intent = new Intent(fragment.getContext(), PhotoSelectActivity.class);
        intent.putExtra(ConstantUtil.DATA, selectedImageItemList);
        intent.putExtra(ConstantUtil.IS_ONE, isOne);
        intent.putExtra(ConstantUtil.IS_CROP, isCrop);
        intent.putExtra(ConstantUtil.FROM, from);
        if (isOne) {
            fragment.startActivityForResult(intent, com.example.commonlibrary.utils.SystemUtil.REQUEST_CODE_ONE_PHOTO);
        } else {
            fragment.startActivity(intent);
        }
    }


}
