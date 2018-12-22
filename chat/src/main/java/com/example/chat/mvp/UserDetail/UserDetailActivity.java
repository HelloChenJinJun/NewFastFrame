package com.example.chat.mvp.UserDetail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.chat.R;
import com.example.chat.base.ChatBaseActivity;
import com.example.chat.base.ConstantUtil;
import com.example.chat.manager.UserDBManager;
import com.example.chat.mvp.editInfo.EditUserInfoActivity;
import com.example.chat.mvp.shareinfo.ShareInfoFragment;
import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.bean.chat.UserEntity;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.cusotomview.WrappedViewPager;
import com.example.commonlibrary.cusotomview.swipe.CustomSwipeRefreshLayout;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.BlurBitmapUtil;
import com.example.commonlibrary.utils.StatusBarUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/22      9:41
 * QQ:             1981367757
 */

public class UserDetailActivity extends ChatBaseActivity implements View.OnClickListener {
    private RoundAngleImageView avatar;
    private TextView name, signature, follow, fans, visit, sexContent, school, major;
    private ImageView sex;
    private WrappedViewPager display;
    private UserEntity user;
    private Toolbar mToolbar;
    private RelativeLayout headerBg;
    private AppBarLayout mAppBarLayout;
    private CustomSwipeRefreshLayout refresh;

    @Override

    public void updateData(Object object) {

    }

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }


    @Override
    protected boolean needStatusPadding() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_user_detail;
    }

    @Override
    protected void initView() {
        initHeaderView();
        TabLayout tabLayout = findViewById(R.id.tl_activity_user_detail_tab);
        display = findViewById(R.id.vp_activity_user_detail_display);
        findViewById(R.id.iv_activity_user_detail_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar = findViewById(R.id.tb_activity_user_detail_title);
        headerBg = findViewById(R.id.rl_activity_user_detail_header_bg);
        refresh = findViewById(R.id.refresh_activity_user_detail_refresh);
        setSupportActionBar(mToolbar);
        mToolbar.getRootView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mToolbar.getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mToolbar.getLayoutParams().height += StatusBarUtil.getStatusBarHeight(UserDetailActivity.this);
                mToolbar.requestLayout();
            }
        });

        mAppBarLayout = findViewById(R.id.al_activity_user_detail_bar);
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset >= 0) {
                refresh.setEnabled(true);
            } else {
                refresh.setEnabled(false);
            }
        });
        tabLayout.setupWithViewPager(display);
    }

    private void initHeaderView() {
        avatar = findViewById(R.id.riv_view_activity_user_detail_header_avatar);
        name = findViewById(R.id.tv_view_activity_user_detail_header_name);
        signature = findViewById(R.id.tv_view_activity_user_detail_header_signature);
        sex = findViewById(R.id.iv_view_activity_user_detail_header_sex);
        follow = findViewById(R.id.tv_view_activity_user_detail_header_follow);
        fans = findViewById(R.id.tv_view_activity_user_detail_header_fans);
        visit = findViewById(R.id.tv_view_activity_user_detail_header_visit);
        sexContent = findViewById(R.id.tv_view_activity_user_detail_header_sex_content);
        school = findViewById(R.id.tv_view_activity_user_detail_header_school);
        major = findViewById(R.id.tv_view_activity_user_detail_header_major);
        findViewById(R.id.tv_view_activity_user_detail_header_look)
                .setOnClickListener(this);

    }

    @Override
    protected void initData() {
        String uid = getIntent().getStringExtra(ConstantUtil.ID);
        user = UserDBManager.getInstance().getUser(uid);
        updateUserInfo();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        List<String> titleList = new ArrayList<>();
        titleList.add("公共说说");
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ShareInfoFragment.newInstance(user.getUid(), false));
        adapter.setTitleAndFragments(titleList, fragments);
        display.setAdapter(adapter);
        display.setCurrentItem(0);
        addDisposable(RxBusManager.getInstance().registerEvent(UserEntity.class, user -> {
            if (user.equals(UserDetailActivity.this.user)) {
                UserDetailActivity.this.user = user;
                updateUserInfo();
            }
        }));
    }

    private void updateUserInfo() {
        if (user != null) {
            Glide.with(this)
                    .load(user.getAvatar())
                    .into(avatar);
            name.setText(user.getNick());
            signature.setText(user.getSignature());
            sex.setImageResource(user.isSex() ? R.drawable.ic_sex_male : R.drawable.ic_sex_female);
            school.setText(user.getSchool());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(user.getYear()).append("级")
                    .append(user.getMajor());
            major.setText(stringBuilder.toString());
            Glide.with(this).asBitmap().load(user.getTitlePaper()).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    headerBg.setBackground(BlurBitmapUtil.createBlurredImageFromBitmap(resource, UserDetailActivity.this, 20));
                }
            });

            if (user.isSex()) {
                sexContent.setText("他");
            } else {
                sexContent.setText("她");
            }
        }
    }

    public static void start(Activity activity, String uid) {
        Intent intent = new Intent(activity, UserDetailActivity.class);
        intent.putExtra(ConstantUtil.ID, uid);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_view_activity_user_detail_header_look) {
            EditUserInfoActivity.start(this, user.getUid());
        }
    }
}
