package com.example.manager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.chat.SystemNotifyEntity;
import com.example.commonlibrary.utils.ToastUtils;

import java.io.File;

import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    private ImageView imageView;
    private String path;
    private EditText title, subTitle, link;
    private Button send;


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
        return R.layout.activity_manager;
    }

    @Override
    protected void initView() {
        imageView = findViewById(R.id.iv_activity_manager_image);
        findViewById(R.id.btn_activity_manager_image).setOnClickListener(this);
        imageView.setOnClickListener(this);
        send = findViewById(R.id.btn_activity_manager_send);
        send.setOnClickListener(this);
        subTitle = findViewById(R.id.et_activity_manager_subtitle);
        title = findViewById(R.id.et_activity_manager_title);
        link = findViewById(R.id.et_activity_manager_link);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_activity_manager_image) {
            pickPhoto(this, 1);
        } else if (id == R.id.btn_activity_manager_send) {
            if (path == null) {
                ToastUtils.showShortToast("封面图片不能为空");
                return;
            }
            if (TextUtils.isEmpty(link.getText().toString())
                    || TextUtils.isEmpty(subTitle.getText().toString())
                    || TextUtils.isEmpty(title.getText().toString())) {
                ToastUtils.showShortToast("输入不能为空");
                return;
            }


            if (!Patterns.WEB_URL.matcher(link.getText().toString()).matches() && !URLUtil.isValidUrl(link.getText().toString())) {
                ToastUtils.showShortToast("输入的链接格式有误");
                return;
            }

            send.setEnabled(false);
            showLoading("正在发送中.........");
            BmobFile bmobFile = new BmobFile(new File(path));
            bmobFile.upload(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        final SystemNotifyBean systemNotifyBean = new SystemNotifyBean();
                        systemNotifyBean.setContentUrl(link.getText().toString().trim());
                        systemNotifyBean.setTitle(title.getText().toString().trim());
                        systemNotifyBean.setSubTitle(subTitle.getText().toString().trim());
                        systemNotifyBean.setImageUrl(bmobFile.getFileUrl());
                        systemNotifyBean.setReadStatus(ManagerConfig.READ_STATUS_UNREAD);
                        systemNotifyBean.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                dismissLoadDialog();
                                if (e == null) {
                                    ToastUtils.showShortToast("上传消息成功");
                                    BmobPushManager<CustomInstallation> bmobPushManager = new BmobPushManager<>();
                                    BmobQuery<CustomInstallation> query = CustomInstallation.getQuery();
                                    //TODO 属性值为android
                                    query.addWhereEqualTo("deviceType", "android");
                                    bmobPushManager.setQuery(query);
                                    SystemNotifyEntity systemNotifyEntity = new SystemNotifyEntity();
                                    systemNotifyEntity.setId(systemNotifyBean.getObjectId());
                                    systemNotifyEntity.setContentUrl(link.getText().toString().trim());
                                    systemNotifyEntity.setTitle(title.getText().toString().trim());
                                    systemNotifyEntity.setSubTitle(subTitle.getText().toString().trim());
                                    systemNotifyEntity.setImageUrl(bmobFile.getFileUrl());
                                    systemNotifyEntity.setReadStatus(ManagerConfig.READ_STATUS_UNREAD);
                                    bmobPushManager.pushMessage(BaseApplication.getAppComponent()
                                            .getGson().toJson(systemNotifyEntity), new PushListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                ToastUtils.showShortToast("推送消息成功");
                                            } else {
                                                ToastUtils.showShortToast("推送消息失败" + e.toString());
                                            }
                                        }
                                    });
                                } else {
                                    ToastUtils.showShortToast("上传信息失败" + e.toString());
                                }
                            }
                        });
                    } else {
                        ToastUtils.showShortToast("图片上传失败" + e.toString());
                    }
                    send.setEnabled(true);
                }
            });


        } else {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        Glide.with(this).load(path).into(imageView);
                    }
                    cursor.close();
                }
            }
        }
    }

    public void pickPhoto(Activity activity, int requestCode) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void updateData(Object o) {

    }
}
