package com.example.chat.mvp.UserDetail;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.R;
import com.example.chat.bean.User;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.editInfo.EditUserInfoActivity;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.mvp.shareinfo.ShareInfoFragment;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.cusotomview.WrappedViewPager;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import rx.Subscription;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/22      9:41
 * QQ:             1981367757
 */

public class UserDetailActivity extends SlideBaseActivity implements View.OnClickListener {
    private RoundAngleImageView avatar;
    private TextView name,signature,follow,fans,visit,sexContent,school,major;
    private ImageView sex;
    private WrappedViewPager display;
    private User user;
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
    protected int getContentLayout() {
        return R.layout.activity_user_detail;
    }

    @Override
    protected void initView() {
        initHeaderView();
        TabLayout tabLayout= (TabLayout) findViewById(R.id.tl_activity_user_detail_tab);
        display= (WrappedViewPager) findViewById(R.id.vp_activity_user_detail_display);
        tabLayout.setupWithViewPager(display);
    }

    private void initHeaderView() {
        avatar= (RoundAngleImageView) findViewById(R.id.riv_view_activity_user_detail_header_avatar);
        name= (TextView) findViewById(R.id.tv_view_activity_user_detail_header_name);
        signature= (TextView) findViewById(R.id.tv_view_activity_user_detail_header_signature);
        sex= (ImageView) findViewById(R.id.iv_view_activity_user_detail_header_sex);
        follow= (TextView) findViewById(R.id.tv_view_activity_user_detail_header_follow);
        fans= (TextView) findViewById(R.id.tv_view_activity_user_detail_header_fans);
        visit= (TextView) findViewById(R.id.tv_view_activity_user_detail_header_visit);
        sexContent= (TextView) findViewById(R.id.tv_view_activity_user_detail_header_sex_content);
        school= (TextView) findViewById(R.id.tv_view_activity_user_detail_header_school);
        major= (TextView) findViewById(R.id.tv_view_activity_user_detail_header_major);
        findViewById(R.id.tv_view_activity_user_detail_header_look)
                .setOnClickListener(this);

    }

    @Override
    protected void initData() {
        String uid=getIntent().getStringExtra("uid");
        if (uid.equals(UserManager.getInstance().getCurrentUserObjectId())) {
            user=UserManager.getInstance().getCurrentUser();
           Disposable disposable=RxBusManager.getInstance().registerEvent(User.class
                    , new Consumer<User>() {
                        @Override
                        public void accept(User user) throws Exception {
                            UserDetailActivity.this.user = user;
                            updateUserInfo();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
           addDisposable(disposable);
        }else {
            user=UserCacheManager.getInstance().getUser(uid);
        }
        if (user == null) {
           Subscription subscription= UserManager.getInstance().findUserById(uid, new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    if (e == null) {
                        if (list != null && list.size() > 0) {
                            user=list.get(0);
                            updateUserInfo();
                        }
                    }else {
                        ToastUtils.showShortToast("查询用户失败"+e.toString());
                    }
                }
            });
           addSubscription(subscription);
        }else {
            updateUserInfo();
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        List<String> titleList=new ArrayList<>();
        titleList.add("公共说说");
        titleList.add("私人说说");
        List<BaseFragment> fragments=new ArrayList<>();
        fragments.add(ShareInfoFragment.instance(user));
        fragments.add(PersonPostFragment.newInstance(user));
        adapter.setTitleAndFragments(titleList,fragments);
        display.setAdapter(adapter);
        display.setCurrentItem(0);
    }

    private void updateUserInfo() {
        if (user != null) {
            Glide.with(this)
                    .load(user.getAvatar())
                    .into(avatar);
            name.setText(user.getNick());
            signature.setText(user.getSignature());
            sex.setImageResource(user.isSex()?R.drawable.ic_sex_male:R.drawable.ic_sex_female);
            school.setText(user.getSchool());
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(user.getYear()).append("级")
                    .append(user.getMajor());
            major.setText(stringBuilder.toString());
            if (user.isSex()) {
                sexContent.setText("他");
            }else {
                sexContent.setText("她");
            }
        }
    }

    public static void start(Activity activity, String uid) {
        Intent intent=new Intent(activity,UserDetailActivity.class);
        intent.putExtra("uid",uid);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id==R.id.tv_view_activity_user_detail_header_look){
            Intent intent=new Intent(this,EditUserInfoActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }
    }
}
