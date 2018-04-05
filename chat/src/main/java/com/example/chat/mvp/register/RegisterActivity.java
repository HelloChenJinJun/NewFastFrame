package com.example.chat.mvp.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.example.chat.R;
import com.example.chat.base.RandomData;
import com.example.chat.bean.CustomInstallation;
import com.example.chat.bean.User;
import com.example.chat.manager.UserManager;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.LogUtil;
import com.example.chat.view.AutoEditText;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.ToastUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

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
                name = (AutoEditText) findViewById(R.id.aet_register_name);
                passWord = (AutoEditText) findViewById(R.id.aet_register_password);
                passWordComfirm = (AutoEditText) findViewById(R.id.aet_register_password_confirm);
                register = (Button) findViewById(R.id.btn_register_confirm);
                bg = (ImageView) findViewById(R.id.iv_register_bg);
                register.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
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
                                CustomInstallation customInstallation=new CustomInstallation();
                                user.setInstallId(customInstallation.getInstallationId());
                                user.setNick(RandomData.getRandomNick());
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
//                                                        进行用户Id和设备的绑定
                                                        if (UserManager.getInstance().getCurrentUser() != null) {
                                                                LogUtil.e("uid：" + UserManager.getInstance().getCurrentUser().getObjectId());
                                                        }
                                                        Intent intent = new Intent();
                                                        intent.putExtra("username", name.getText().toString().trim());
                                                        intent.putExtra("password", passWord.getText().toString().trim());
                                                        setResult(Activity.RESULT_OK, intent);
                                                        finish();
                                                }else {
                                                        ToastUtils.showShortToast("注册失败" +e.toString());
                                                }
                                        }
                                });
                        }
                });
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
//                RandomData.initAllRanDomData();
        }


        @Override
        public void updateData(Object o) {

        }
}
