package com.example.chat.mvp.developer;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.chat.R;
import com.example.chat.base.ConstantUtil;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.UserDetail.UserDetailActivity;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.bean.chat.User;
import com.example.commonlibrary.customview.RoundAngleImageView;
import com.example.commonlibrary.mvp.base.WebActivity;
import com.example.commonlibrary.utils.BlurBitmapUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/28     17:02
 */
public class DeveloperInfoActivity extends BaseActivity implements View.OnClickListener {


    private RelativeLayout headerBg;
    private TextView name;
    private ImageView sex;
    private TextView signature;
    private User user;
    private RoundAngleImageView avatar;
    private TextView github, qq, wx;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, DeveloperInfoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return true;
    }


    @Override
    protected boolean needStatusPadding() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_developer_info;
    }

    @Override
    protected void initView() {
        headerBg = findViewById(R.id.rl_activity_developer_info_header_bg);
        RelativeLayout headerContainer = findViewById(R.id.rl_activity_developer_info_container);
        findViewById(R.id.iv_activity_developer_info_back).setOnClickListener(this);
        name = findViewById(R.id.tv_activity_developer_info_name);
        signature = findViewById(R.id.tv_activity_developer_info_signature);
        sex = findViewById(R.id.iv_activity_developer_info_sex);
        avatar = findViewById(R.id.riv_activity_developer_info_avatar);
        github = findViewById(R.id.tv_activity_developer_info_github);
        github.setOnClickListener(this);
        qq = findViewById(R.id.tv_activity_developer_info_qq);
        wx = findViewById(R.id.tv_activity_developer_info_wx);
        findViewById(R.id.tv_activity_developer_info_qq).setOnClickListener(this);
        findViewById(R.id.tv_activity_developer_info_wx).setOnClickListener(this);
        headerContainer.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        showLoading("正在加载用户数据....");
        UserManager.getInstance().findUserById(ConstantUtil.SYSTEM_UID, new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null && list != null && list.size() > 0) {
                    hideLoading();
                    updateUserInfo(list.get(0));
                } else {
                    showError(e != null ? e.getMessage() : null, null);
                }
            }
        });
    }

    private void updateUserInfo(User user) {
        this.user = user;
        if (user != null) {
            Glide.with(this).load(user.getAvatar())
                    .into(avatar);
            name.setText(user.getName());
            signature.setText(user.getSignature());
            sex.setImageResource(user.isSex() ? R.drawable.ic_sex_male : R.drawable.ic_sex_female);
            Glide.with(this).asBitmap().load(user.getTitleWallPaper()).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    headerBg.setBackground(BlurBitmapUtil.createBlurredImageFromBitmap(resource, DeveloperInfoActivity.this, 20));
                }
            });
        }
    }

    @Override
    public void updateData(Object o) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_activity_developer_info_container) {
            if (user != null) {
                UserDetailActivity.start(this, user.getObjectId(),
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create(avatar, "avatar")
                                , Pair.create(name, "name"), Pair.create(sex, "sex")
                                , Pair.create(signature, "signature")));
            }
        } else if (id == R.id.tv_activity_developer_info_github) {
            WebActivity.start(this, github.getText().toString().trim(), user.getName());
        } else if (id == R.id.tv_activity_developer_info_wx) {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("text", wx.getText().toString().trim()));
            ToastUtils.showShortToast("复制微信号成功");
        } else if (id == R.id.tv_activity_developer_info_qq) {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("text", qq.getText().toString().trim()));
            ToastUtils.showShortToast("复制QQ号成功");
        } else if (id == R.id.iv_activity_developer_info_back) {
            finish();
        }
    }
}
