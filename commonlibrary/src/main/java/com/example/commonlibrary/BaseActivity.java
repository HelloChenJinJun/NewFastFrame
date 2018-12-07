package com.example.commonlibrary;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.commonlibrary.adaptScreen.IAdaptScreen;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.adapter.ListItemAdapter;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.CommonDialog;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.dagger.component.AppComponent;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.skin.SkinManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.Constant;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.commonlibrary.utils.StatusBarUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/4/3      14:21
 * QQ:             1981367757
 */

public abstract class BaseActivity<T, P extends BasePresenter> extends RxAppCompatActivity implements IView<T>, IAdaptScreen {

    //  这里的布局view可能为空，取决于子类布局中是否含有该空布局


    private EmptyLayout mEmptyLayout;
    protected int fragmentContainerResId = 0;
    protected Fragment currentFragment;
    private View headerLayout;
    private RoundAngleImageView icon;
    protected TextView right;
    protected TextView title;
    protected ImageView rightImage;
    protected ImageView back;
    private CompositeDisposable compositeDisposable;
    protected ViewGroup bg;


    //    字体初始化
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    protected void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }


    public AppComponent getAppComponent() {
        return BaseApplication.getAppComponent();
    }


    @Override
    public boolean isBaseOnWidth() {
        return true;
    }


    @Override
    public int getScreenSize() {
        return 250;
    }


    @Override
    public boolean cancelAdapt() {
        return true;
    }


    @Override
    public boolean needResetAdapt() {
        return getScreenSize() != (isBaseOnWidth() ? BaseApplication.getAppComponent().getSharedPreferences().getInt(Constant.DESIGNED_WIDTH, 0) :
                BaseApplication.getAppComponent().getSharedPreferences().getInt(Constant.DESIGNED_HEIGHT, 0));
    }

    @Nullable
    @Inject
    protected P presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SkinManager.getInstance().apply(this);
        super.onCreate(savedInstanceState);
        UMGameAgent.init(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        if (isNeedHeadLayout()) {
            bg = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.content_layout_ll, null);
            headerLayout = LayoutInflater.from(this).inflate(R.layout.header_layout, null);
            ((TextView) headerLayout.findViewById(R.id.tv_header_layout_title)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            bg.addView(headerLayout);
            if (isNeedEmptyLayout()) {
                FrameLayout frameLayout = new FrameLayout(this);
                frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mEmptyLayout = new EmptyLayout(this);
                mEmptyLayout.setVisibility(View.GONE);
                if (getContentLayout() != 0) {
                    LayoutInflater.from(this).inflate(getContentLayout(), frameLayout);
                }
                mEmptyLayout.setContentView(mEmptyLayout.getChildAt(0));
                frameLayout.addView(mEmptyLayout);
                bg.addView(frameLayout);
            } else {
                if (getContentLayout() != 0) {
                    LayoutInflater.from(this).inflate(getContentLayout(), bg);
                }
            }
        } else {
            bg = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.content_layout_fl, null);
            bg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            if (getContentLayout() != 0) {
                LayoutInflater.from(this).inflate(getContentLayout(), bg);
            }
            if (isNeedEmptyLayout()) {
                mEmptyLayout = new EmptyLayout(this);
                mEmptyLayout.setVisibility(View.GONE);
                mEmptyLayout.setContentView(bg.getChildAt(0));
                bg.addView(mEmptyLayout);
            }
        }
        if (getContentLayout() != 0) {
            setContentView(bg);
        }
        initBaseView();
        initData();
        updateStatusBar();
    }


    protected RoundAngleImageView getIcon() {
        return icon;
    }

    protected abstract boolean isNeedHeadLayout();

    protected abstract boolean isNeedEmptyLayout();

    protected abstract int getContentLayout();

    protected abstract void initView();

    protected abstract void initData();

    protected void initBaseView() {
        if (isNeedHeadLayout()) {
            icon = headerLayout.findViewById(R.id.riv_header_layout_icon);
            title = headerLayout.findViewById(R.id.tv_header_layout_title);
            right = headerLayout.findViewById(R.id.tv_header_layout_right);
            back = headerLayout.findViewById(R.id.iv_header_layout_back);
            rightImage = headerLayout.findViewById(R.id.iv_header_layout_right);
            rightImage.setVisibility(View.GONE);
            right.setVisibility(View.VISIBLE);
            setSupportActionBar(headerLayout.findViewById(R.id.toolbar));
            getSupportActionBar().setTitle("");
        }
        initView();
    }

    protected void updateStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, getPaddingView());
    }

    private View getPaddingView() {
        if (needStatusPadding()) {
            return headerLayout != null ? headerLayout : ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        }
        return null;
    }

    protected boolean needStatusPadding() {
        return true;
    }


    protected void showEmptyLayout(int status) {
        if (mEmptyLayout != null) {
            mEmptyLayout.setCurrentStatus(status);
        }
    }


    public void setToolBar(ToolBarOption option) {
        if (!isNeedHeadLayout()) {
            return;
        }

        if (option.getBgColor() != -1) {
            headerLayout.setBackgroundColor(option.getBgColor());
        }
        if (option.getCustomView() != null) {
            ViewGroup container = headerLayout.findViewById(R.id.toolbar);
            container.removeAllViews();
            container.addView(option.getCustomView());
            return;
        }
        if (option.getAvatar() != null) {
            icon.setVisibility(View.VISIBLE);
            Glide.with(this).load(option.getAvatar()).into(icon);
        } else {
            icon.setVisibility(View.GONE);
        }
        if (option.getRightResId() != 0) {
            right.setVisibility(View.GONE);
            rightImage.setVisibility(View.VISIBLE);
            rightImage.setImageResource(option.getRightResId());
            rightImage.setOnClickListener(option.getRightListener());
        } else if (option.getRightText() != null) {
            right.setVisibility(View.VISIBLE);
            rightImage.setVisibility(View.GONE);
            right.setText(option.getRightText());
            right.setOnClickListener(option.getRightListener());
        } else {
            right.setVisibility(View.GONE);
            rightImage.setVisibility(View.GONE);
        }
        if (option.getTitle() != null) {
            title.setVisibility(View.VISIBLE);
            title.setText(option.getTitle());
            if (option.getTitleColor() != -10) {
                title.setTextColor(option.getTitleColor());
            }

        } else {
            title.setVisibility(View.GONE);
        }
        if (option.isNeedNavigation()) {
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(v -> finish());
        } else {
            back.setVisibility(View.GONE);
        }

        if (option.getBackResId() != 0) {
            back.setImageResource(option.getBackResId());
        }


    }


    public void updateTitle(String title) {
        if (isNeedHeadLayout()) {
            this.title.setText(title);
        }
    }


    public void showBaseDialog(String title, String message, String leftName, String rightName, View.OnClickListener leftListener, View.OnClickListener rightListener) {
        //        if (mBaseDialog == null) {
        //        mBaseDialog = new BaseDialog(this);
        //        mBaseDialog.setTitle(title).setMessage(message).setLeftButton(leftName, leftListener).setRightButton(rightName, rightListener).show();
        CommonDialog.newBuild(this).setTitle(title)
                .setInfo(message).setLeftButton(leftName, leftListener).setRightButton(rightName, rightListener).build().show();
    }


    public void showChooseDialog(String title, List<String> titleList, OnSimpleItemClickListener onSimpleItemClickListener) {
        ListItemAdapter listItemAdapter = new ListItemAdapter();
        SuperRecyclerView superRecyclerView = new SuperRecyclerView(this);
        superRecyclerView.setLayoutManager(new WrappedLinearLayoutManager(this));
        superRecyclerView.setAdapter(listItemAdapter);
        superRecyclerView.addItemDecoration(new ListViewDecoration(DensityUtil.toDp(10)));
        listItemAdapter.refreshData(titleList);
        CommonDialog commonDialog = showCustomDialog(title, superRecyclerView, null, "确定", null, null);
        listItemAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                commonDialog.dismiss();
                if (onSimpleItemClickListener != null) {
                    onSimpleItemClickListener.onItemClick(position, view);
                }
            }
        });
    }


    public CommonDialog showCustomDialog(String title, View view, String leftName, String rightName, View.OnClickListener leftListener, View.OnClickListener rightListener) {
        CommonDialog commonDialog = CommonDialog.newBuild(this).setTitle(title)
                .setContentView(view).setLeftButton(leftName, leftListener).setRightButton(rightName, rightListener).build();
        commonDialog.show();
        return commonDialog;
    }


    public void showLoadDialog(final String message) {
        ToastUtils.showShortToast(message);
    }

    public void dismissLoadDialog() {
        showEmptyLayout(EmptyLayout.STATUS_HIDE);
    }


    public void addOrReplaceFragment(Fragment fragment) {
        addOrReplaceFragment(fragment, 0);
    }

    /**
     * 第一次加载的时候调用该方法设置resId
     */
    public void addOrReplaceFragment(Fragment fragment, int resId) {
        if (resId != 0) {
            fragmentContainerResId = resId;
        }
        if (fragment == null) {
            return;
        }
        if (currentFragment == null) {
            getSupportFragmentManager().beginTransaction().add(resId, fragment).show(fragment).commitAllowingStateLoss();
            currentFragment = fragment;
        } else if (currentFragment != fragment) {
            if (fragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().hide(currentFragment).show(fragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().hide(currentFragment).add(fragmentContainerResId, fragment).show(fragment).commitAllowingStateLoss();
            }
            currentFragment = fragment;
        }
    }


    private int backStackLayoutId = 0;

    protected void addBackStackFragment(Fragment fragment, int resId) {
        backStackLayoutId = resId;
        addBackStackFragment(fragment, true);
    }


    protected void addBackStackFragment(Fragment fragment, int resId, boolean needAddBackStack) {
        backStackLayoutId = resId;
        addBackStackFragment(fragment, needAddBackStack);
    }


    protected void addBackStackFragment(Fragment fragment, boolean needAddBackStack) {
        if (backStackLayoutId == 0) {
            return;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .add(backStackLayoutId, fragment)
                .setCustomAnimations(R.anim.slide_bottom_in, R.anim.slide_bottom_out);
        if (needAddBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }


    @Override
    public void showLoading(String loadMessage) {
        if (mEmptyLayout != null) {
            mEmptyLayout.setCurrentStatus(EmptyLayout.STATUS_LOADING);
        } else {
            showLoadDialog(loadMessage);
        }
    }

    @Override
    public void hideLoading() {
        if (mEmptyLayout != null) {
            if (mEmptyLayout.getCurrentStatus() != EmptyLayout.STATUS_HIDE) {
                mEmptyLayout.setCurrentStatus(EmptyLayout.STATUS_HIDE);
            }
        } else {
            dismissLoadDialog();
        }

    }


    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        if (mEmptyLayout != null) {
            mEmptyLayout.setCurrentStatus(EmptyLayout.STATUS_NO_NET);
            if (listener != null) {
                mEmptyLayout.setOnRetryListener(listener);
            }
        } else {
            ToastUtils.showShortToast(errorMsg);
        }
        CommonLogger.e(errorMsg);

    }


    @Override
    public void showEmptyView() {
        showEmptyLayout(EmptyLayout.STATUS_NO_DATA);
    }

    @Override
    public <Y> LifecycleTransformer<Y> bindLife() {
        return bindToLifecycle();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().clear(this);
        if (presenter != null) {
            presenter.onDestroy();
        }
        if (compositeDisposable != null) {
            if (!compositeDisposable.isDisposed()) {
                compositeDisposable.dispose();
            }
            compositeDisposable.clear();
            compositeDisposable = null;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (needRecord()) {
            MobclickAgent.onPageStart(this.getClass().getName());
        }
        UMGameAgent.onResume(this);
    }

    protected boolean needRecord() {
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (needRecord()) {
            MobclickAgent.onPageEnd(this.getClass().getName());
        }
        UMGameAgent.onPause(this);
    }


}
