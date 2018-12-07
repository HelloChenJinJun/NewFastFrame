package com.example.chat.mvp.editInfo;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.R;
import com.example.chat.base.ConstantUtil;
import com.example.chat.base.ChatBaseActivity;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.photoSelect.PhotoSelectActivity;
import com.example.commonlibrary.bean.chat.UserEntity;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.imageloader.glide.GlideImageLoaderConfig;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.SystemUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/26      16:49
 * QQ:             1981367757
 * 编辑用户信息界面
 */

public class EditUserInfoActivity extends ChatBaseActivity implements View.OnClickListener {
    private RelativeLayout avatarLayout, nickLayout, sexLayout, birthLayout, phoneLayout,
            emailLayout, signatureLayout, addressLayout, schoolContainer, collegeContainer, majorContainer, educationContainer, yearContainer, classContainer, nameContainer;
    private RoundAngleImageView avatar;
    private TextView nick, sex, birth, phone, email, signature, address, school, college, major, education, year, className, name;
    private UserEntity mUser;


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
        return R.layout.activity_edit_info;
    }


    @Override
    public void initView() {
        avatarLayout = findViewById(R.id.rl_edit_user_info_avatar);
        nickLayout = findViewById(R.id.rl_edit_user_info_nick);
        sexLayout = findViewById(R.id.rl_edit_user_info_sex);
        birthLayout = findViewById(R.id.rl_edit_user_info_birth);
        phoneLayout = findViewById(R.id.rl_edit_user_info_phone);
        emailLayout = findViewById(R.id.rl_edit_user_info_email);
        signatureLayout = findViewById(R.id.rl_edit_user_info_signature);
        addressLayout = findViewById(R.id.rl_edit_user_info_address);
        avatar = findViewById(R.id.riv_edit_user_info_avatar);
        schoolContainer = findViewById(R.id.rl_edit_user_info_school);
        collegeContainer = findViewById(R.id.rl_edit_user_info_college);
        majorContainer = findViewById(R.id.rl_edit_user_info_major);
        educationContainer = findViewById(R.id.rl_edit_user_info_education);
        yearContainer = findViewById(R.id.rl_edit_user_info_year);
        classContainer = findViewById(R.id.rl_edit_user_info_class);
        nameContainer = findViewById(R.id.rl_edit_user_info_name);
        nick = findViewById(R.id.tv_edit_user_info_nick);
        sex = findViewById(R.id.tv_edit_user_info_sex);
        birth = findViewById(R.id.tv_edit_user_info_birth);
        phone = findViewById(R.id.tv_edit_user_info_phone);
        email = findViewById(R.id.tv_edit_user_info_email);
        signature = findViewById(R.id.tv_edit_user_info_signature);
        address = findViewById(R.id.tv_edit_user_info_address);
        school = findViewById(R.id.tv_edit_user_info_school);
        college = findViewById(R.id.tv_edit_user_info_college);
        major = findViewById(R.id.tv_edit_user_info_major);
        education = findViewById(R.id.tv_edit_user_info_education);
        year = findViewById(R.id.tv_edit_user_info_year);
        className = findViewById(R.id.tv_edit_user_info_class);
        name = findViewById(R.id.tv_edit_user_info_name);
        avatarLayout.setOnClickListener(this);
        nickLayout.setOnClickListener(this);
        sexLayout.setOnClickListener(this);
        birthLayout.setOnClickListener(this);
        phoneLayout.setOnClickListener(this);
        emailLayout.setOnClickListener(this);
        signatureLayout.setOnClickListener(this);
        addressLayout.setOnClickListener(this);
        schoolContainer.setOnClickListener(this);
        collegeContainer.setOnClickListener(this);
        majorContainer.setOnClickListener(this);
        educationContainer.setOnClickListener(this);
        yearContainer.setOnClickListener(this);
        classContainer.setOnClickListener(this);
        nameContainer.setOnClickListener(this);

    }


    public static void start(Activity activity, String uid) {
        Intent intent = new Intent(activity, EditUserInfoActivity.class);
        intent.putExtra(ConstantUtil.ID, uid);
        activity.startActivity(intent);
    }


    @Override
    public void initData() {
        mUser = UserDBManager.getInstance().getUser(getIntent().getStringExtra(ConstantUtil.ID));
        if (!mUser.getUid().equals(UserManager.getInstance().getCurrentUserObjectId())) {
            sexLayout.setEnabled(false);
            avatarLayout.setEnabled(false);
            nickLayout.setEnabled(false);
            birthLayout.setEnabled(false);
            phoneLayout.setEnabled(false);
            emailLayout.setEnabled(false);
            signatureLayout.setEnabled(false);
            schoolContainer.setEnabled(false);
            addressLayout.setEnabled(false);
            collegeContainer.setEnabled(false);
            majorContainer.setEnabled(false);
            educationContainer.setEnabled(false);
            yearContainer.setEnabled(false);
            classContainer.setEnabled(false);
            nameContainer.setEnabled(false);
        }
        nick.setText(mUser.getNick());
        birth.setText(mUser.getBirthDay());
        phone.setText(mUser.getPhone());
        sex.setText(mUser.isSex() ? "男" : "女");
        email.setText(mUser.getEmail());
        signature.setText(mUser.getSignature());
        address.setText(mUser.getAddress());
        school.setText(mUser.getSchool());
        college.setText(mUser.getCollege());
        major.setText(mUser.getMajor());
        className.setText(mUser.getClassNumber());
        education.setText(mUser.getEducation());
        name.setText(mUser.getName());
        year.setText(mUser.getYear());
        Glide.with(this).load(mUser.getAvatar()).into(avatar);
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setAvatar(null);
        toolBarOption.setRightResId(R.drawable.ic_file_upload_blue_grey_900_24dp);
        toolBarOption.setTitle("编辑个人资料");
        toolBarOption.setRightListener(v -> {
            if (mUser.getAvatar() == null) {
                ToastUtils.showShortToast("请设置个人头像拉^_^");
                return;
            }
            finish();
        });
        back.setOnClickListener(v -> {
            if (mUser.getAvatar() == null) {
                ToastUtils.showShortToast("请设置个人头像拉^_^");
                return;
            }
            finish();
        });
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }

    @Override

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.rl_edit_user_info_avatar) {
            PhotoSelectActivity.start(this, null, true, true, null);
        } else if (i == R.id.rl_edit_user_info_nick) {
            EditUserInfoDetailActivity.start(this, ConstantUtil.NICK
                    , mUser.getNick(), ConstantUtil.REQUEST_CODE_NICK);
        } else if (i == R.id.rl_edit_user_info_sex) {
            EditUserInfoDetailActivity.start(this, ConstantUtil.GENDER
                    , mUser.isSex() ? "男" : "女", ConstantUtil.REQUEST_CODE_SEX);
        } else if (i == R.id.rl_edit_user_info_birth) {

            EditUserInfoDetailActivity.start(this, ConstantUtil.BIRTHDAY
                    , mUser.getBirthDay(), ConstantUtil.REQUEST_CODE_BIRTH);
        } else if (i == R.id.rl_edit_user_info_phone) {
            EditUserInfoDetailActivity.start(this, ConstantUtil.PHONE
                    , mUser.getPhone(), ConstantUtil.REQUEST_CODE_PHONE);
        } else if (i == R.id.rl_edit_user_info_email) {
            EditUserInfoDetailActivity.start(this, ConstantUtil.EMAIL
                    , mUser.getEmail(), ConstantUtil.REQUEST_CODE_EMAIL);
        } else if (i == R.id.rl_edit_user_info_signature) {
            EditUserInfoDetailActivity.start(this, ConstantUtil.SIGNATURE
                    , mUser.getSignature(), ConstantUtil.REQUEST_CODE_SIGNATURE);
        } else if (i == R.id.rl_edit_user_info_address) {
            EditUserInfoDetailActivity.start(this, ConstantUtil.ADDRESS, mUser.getAddress()
                    , ConstantUtil.REQUEST_CODE_ADDRESS);
        } else if (i == R.id.rl_edit_user_info_school) {

        } else if (i == R.id.rl_edit_user_info_college) {

        } else if (i == R.id.rl_edit_user_info_education) {

        } else if (i == R.id.rl_edit_user_info_major) {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String message = null;
            if (data != null) {
                message = data.getStringExtra(ConstantUtil.DATA);
            }
            switch (requestCode) {
                case SystemUtil.REQUEST_CODE_ONE_PHOTO:
                    CommonLogger.e("裁剪完成");
                    try {
                        showLoadDialog("正在上传头像，请稍候........");
                        BmobFile bmobFile = new BmobFile(new File(new URI(data.getStringExtra(ConstantUtil.PATH))));
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    UserManager.getInstance().updateUserInfo(ConstantUtil.AVATAR, bmobFile.getFileUrl(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            dismissLoadDialog();
                                            if (e == null) {
                                                CommonLogger.e("更新用户头像成功");
                                                getAppComponent()
                                                        .getImageLoader().loadImage(EditUserInfoActivity.this, GlideImageLoaderConfig
                                                        .newBuild().cacheStrategy(GlideImageLoaderConfig.CACHE_ALL).url(bmobFile.getFileUrl())
                                                        .imageView(avatar).build());
                                                RxBusManager.getInstance().post(UserManager.getInstance().getCurrentUser());
                                            } else {
                                                CommonLogger.e("更新用户头像失败" + e.toString());
                                            }
                                        }


                                    });
                                } else {
                                    dismissLoadDialog();
                                    CommonLogger.e("加载失败");
                                }
                            }

                        });
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    break;
                case ConstantUtil.REQUEST_CODE_SEX:
                    sex.setText(message);
                    if (message != null) {
                        mUser.setSex(message.equals("男"));
                    }
                    RxBusManager.getInstance().post(mUser);
                    break;
                case ConstantUtil.REQUEST_CODE_BIRTH:
                    birth.setText(message);
                    mUser.setBirthDay(message);
                    RxBusManager.getInstance().post(mUser);
                    break;
                case ConstantUtil.REQUEST_CODE_SIGNATURE:
                    signature.setText(message);
                    mUser.setSignature(message);
                    RxBusManager.getInstance().post(mUser);
                    break;
                case ConstantUtil.REQUEST_CODE_EMAIL:
                    email.setText(message);
                    mUser.setEmail(message);
                    RxBusManager.getInstance().post(mUser);
                    break;
                case ConstantUtil.REQUEST_CODE_NICK:
                    nick.setText(message);
                    mUser.setNick(message);
                    RxBusManager.getInstance().post(mUser);
                    break;
                case ConstantUtil.REQUEST_CODE_ADDRESS:
                    address.setText(message);
                    mUser.setAddress(message);
                    RxBusManager.getInstance().post(mUser);
                case ConstantUtil.REQUEST_CODE_PHONE:
                    phone.setText(message);
                    mUser.setPhone(message);
                    RxBusManager.getInstance().post(mUser);
                default:
                    break;
            }
        }
    }


    @Override
    public void updateData(Object o) {

    }
}
