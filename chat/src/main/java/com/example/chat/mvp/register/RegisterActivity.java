package com.example.chat.mvp.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.example.chat.R;
import com.example.chat.base.ConstantUtil;
import com.example.chat.base.RandomData;
import com.example.chat.bean.CustomInstallation;
import com.example.chat.manager.UserManager;
import com.example.chat.util.LogUtil;
import com.example.chat.view.AutoEditText;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.bean.chat.User;
import com.example.commonlibrary.customview.ToolBarOption;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.List;

import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/12      22:43
 * QQ:             1981367757
 */
public class RegisterActivity extends BaseActivity {
    private AutoEditText name;
    private AutoEditText passWord;
    private AutoEditText passWordComfirm;
    private Button register;
    private ImageView bg;


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
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        name = findViewById(R.id.aet_register_name);
        passWord = findViewById(R.id.aet_register_password);
        passWordComfirm = findViewById(R.id.aet_register_password_confirm);
        register = findViewById(R.id.btn_register_confirm);
        bg = findViewById(R.id.iv_register_bg);
        register.setOnClickListener(view -> {
            if (TextUtils.isEmpty(name.getText().toString().trim())) {
                ToastUtils.showShortToast(getString(R.string.account_null));
                name.startShakeAnimation();
                return;
            }
            if (TextUtils.isEmpty(passWord.getText())) {
                ToastUtils.showShortToast(getString(R.string.password_null));
                return;
            }
            if (TextUtils.isEmpty(passWordComfirm.getText())) {
                ToastUtils.showShortToast(getString(R.string.password_null));
                passWordComfirm.startShakeAnimation();
                return;
            }
            if (!passWord.getText().toString().trim().equals(passWordComfirm.getText().toString().trim())) {
                ToastUtils.showShortToast(getString(R.string.register_password_error));
                return;
            }
            if (!AppUtil.isNetworkAvailable()) {
                ToastUtils.showShortToast(getString(R.string.network_tip));
                return;
            }
            showLoadDialog("正在注册，请稍候.......");
            User user = new User();
            //                                默认注册为男性
            user.setSex(true);
            //                                 设备类型
            user.setDeviceType("android");
            //                                与设备ID绑定
            CustomInstallation customInstallation = new CustomInstallation();
            user.setInstallId(customInstallation.getInstallationId());

            User currentUser = new User();
            currentUser.setObjectId(ConstantUtil.SYSTEM_UID);
            BmobRelation relation = new BmobRelation();
            relation.add(user);
            user.setContacts(relation);
            user.setNick(RandomData.getRandomNick());
            user.setSignature(RandomData.getRandomSignature());
            user.setAvatar(RandomData.getRandomAvatar());
            LogUtil.e("用户的头像信息:" + user.getAvatar());
            user.setUsername(name.getText().toString().trim());
            user.setPassword(passWord.getText().toString().trim());
            user.setTitleWallPaper(RandomData.getRandomTitleWallPaper());
            user.setSchool("中国地质大学(武汉)");
            user.setName(RandomData.getRandomName());
            user.setCollege(RandomData.getRandomCollege());
            user.setYear(RandomData.getRandomYear());
            user.setEducation(RandomData.getRandomEducation());
            user.setClassNumber(RandomData.getRandomClassNumber());
            user.setMajor(RandomData.getRandomMajor());
            user.setWallPaper(RandomData.getRandomWallPaper());
            user.signUp(new SaveListener<User>() {
                @Override
                public void done(User s, BmobException e) {
                    dismissLoadDialog();
                    if (e == null) {
                        ToastUtils.showShortToast("注册成功");
                        //   进行用户Id和设备的绑定
                        if (UserManager.getInstance().getCurrentUser() != null) {
                            LogUtil.e("uid：" + UserManager.getInstance().getCurrentUser().getObjectId());
                        }
                        UserManager.getInstance().findUserById(ConstantUtil.SYSTEM_UID, new FindListener<User>() {
                            @Override
                            public void done(List<User> list, BmobException e) {
                                if (e == null && list != null && list.size() > 0) {
                                    BmobRelation bmobRelation = new BmobRelation();
                                    bmobRelation.add(s);
                                    list.get(0).setContacts(bmobRelation);
                                    list.get(0).update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            dealFinish();
                                        }
                                    });
                                } else {
                                    dealFinish();
                                }
                            }
                        });
                    } else {
                        ToastUtils.showShortToast("注册失败" + e.toString());
                    }
                }
            });
        });
    }

    private void dealFinish() {
        Intent intent = new Intent();
        intent.putExtra("username", name.getText().toString().trim());
        intent.putExtra("password", passWord.getText().toString().trim());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    @Override
    public void initData() {
        new Handler().postDelayed(() -> {
            Animation animation = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.translate_anim);
            bg.startAnimation(animation);
        }, 200);
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("注册");
        setToolBar(toolBarOption);
    }


    @Override
    public void updateData(Object o) {

    }
}
