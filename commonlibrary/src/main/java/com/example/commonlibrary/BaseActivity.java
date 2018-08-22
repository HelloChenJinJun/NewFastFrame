package com.example.commonlibrary;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.commonlibrary.adaptScreen.IAdaptScreen;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.cusotomview.BaseDialog;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.skin.SkinLayoutInflaterFactory;
import com.example.commonlibrary.skin.SkinManager;
import com.example.commonlibrary.utils.StatusBarUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static android.view.View.GONE;



/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/4/3      14:21
 * QQ:             1981367757
 */

public  abstract class BaseActivity<T, P extends BasePresenter> extends RxAppCompatActivity implements IView<T>, IAdaptScreen {

    //  这里的布局view可能为空，取决于子类布局中是否含有该空布局


    private EmptyLayout mEmptyLayout;
    protected int fragmentContainerResId = 0;
    protected Fragment currentFragment;
    private View headerLayout;
    private ProgressDialog mProgressDialog;
    protected BaseDialog mBaseDialog;
    private RoundAngleImageView icon;
    protected TextView right;
    private TextView title;
    private ImageView rightImage;
    protected ImageView back;
    private CompositeDisposable compositeDisposable=new CompositeDisposable();



    protected void addDisposable(Disposable disposable){
        compositeDisposable.add(disposable);
    }


    @Override
    public boolean isBaseOnWidth() {
        return true;
    }


    @Override
    public int getScreenSize() {
        return 360;
    }


    @Override
    public boolean cancelAdapt() {
        return false;
    }

    @Nullable
    @Inject
    protected P presenter;


    public ImageView getBack() {
        return back;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SkinManager.getInstance().apply(this);
        super.onCreate(savedInstanceState);
        if (isNeedHeadLayout()) {
            LinearLayout linearLayout= (LinearLayout) LayoutInflater.from(this).inflate(R.layout.content_layout_ll,null);
            headerLayout =LayoutInflater.from(this).inflate(R.layout.header_layout, null);
            linearLayout.addView(headerLayout);
            if (isNeedEmptyLayout()) {
                FrameLayout frameLayout = new FrameLayout(this);
                frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mEmptyLayout = new EmptyLayout(this);
                mEmptyLayout.setVisibility(GONE);
                frameLayout.addView(LayoutInflater.from(this).inflate(getContentLayout(), null));
                mEmptyLayout.setContentView(mEmptyLayout.getChildAt(0));
                frameLayout.addView(mEmptyLayout);
                linearLayout.addView(frameLayout);
            } else {
                linearLayout.addView(LayoutInflater.from(this).inflate(getContentLayout(), null));
            }
            setContentView(linearLayout);
        } else {
            FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.content_layout_fl,null);
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            frameLayout.addView(LayoutInflater.from(this).inflate(getContentLayout(), null));
            if (isNeedEmptyLayout()) {
                mEmptyLayout = new EmptyLayout(this);
                mEmptyLayout.setVisibility(GONE);
                mEmptyLayout.setContentView(frameLayout.getChildAt(0));
                frameLayout.addView(mEmptyLayout);

            }
            setContentView(frameLayout);
        }
        initBaseView();
        initData();
        updateStatusBar();
    }

    protected RoundAngleImageView getIcon() {
        return icon;
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    private void checkLogin() {
    }

    protected TextView getCustomTitle() {
        return title;
    }

    protected abstract boolean isNeedHeadLayout();

    protected abstract boolean isNeedEmptyLayout();

    protected abstract int getContentLayout();

    protected abstract void initView();

    protected abstract void initData();


    protected void initBaseView() {
        if (isNeedHeadLayout()) {
            icon =  headerLayout.findViewById(R.id.riv_header_layout_icon);
            title =  headerLayout.findViewById(R.id.tv_header_layout_title);
            right =  headerLayout.findViewById(R.id.tv_header_layout_right);
            back =  headerLayout.findViewById(R.id.iv_header_layout_back);
            rightImage =  headerLayout.findViewById(R.id.iv_header_layout_right);
            rightImage.setVisibility(View.GONE);
            right.setVisibility(View.VISIBLE);
            setSupportActionBar( headerLayout.findViewById(R.id.toolbar));
            getSupportActionBar().setTitle("");
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mBaseDialog = new BaseDialog(this);
        initView();
    }

    protected void updateStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this,0, getPaddingView());
    }

    private View getPaddingView() {
        if (needStatusPadding()) {
            return headerLayout!=null?headerLayout: ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        }
        return null;
    }



    protected boolean needStatusPadding(){
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

        if (option.getBgColor() !=0) {
            headerLayout.setBackgroundColor(option.getBgColor());
        }
        if (option.getCustomView() != null) {
            ViewGroup container= headerLayout.findViewById(R.id.toolbar);
            container.removeAllViews();
            container.addView(option.getCustomView());
            return;
        }

        if (option.getAvatar() != null) {
            icon.setVisibility(View.VISIBLE);
            Glide.with(this).load(option.getAvatar()).into(icon);
        } else {
            icon.setVisibility(GONE);
        }
        if (option.getRightResId() != 0) {
            right.setVisibility(GONE);
            rightImage.setVisibility(View.VISIBLE);
            rightImage.setImageResource(option.getRightResId());
            rightImage.setOnClickListener(option.getRightListener());
        } else if (option.getRightText() != null) {
            right.setVisibility(View.VISIBLE);
            rightImage.setVisibility(GONE);
            right.setText(option.getRightText());
            right.setOnClickListener(option.getRightListener());
        } else {
            right.setVisibility(GONE);
            rightImage.setVisibility(GONE);
        }
        if (option.getTitle() != null) {
            title.setVisibility(View.VISIBLE);
            title.setText(option.getTitle());
        } else {
            title.setVisibility(GONE);
        }
        if (option.isNeedNavigation()) {
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(v -> finish());
        } else {
            back.setVisibility(GONE);
        }

    }



    public void updateTitle(String title) {
        if (isNeedHeadLayout()) {
            this.title.setText(title);
        }
    }


    public void showBaseDialog(String title, String message, String leftName, String rightName, View.OnClickListener leftListener, View.OnClickListener rightListener) {
        mBaseDialog.setTitle(title).setMessage(message).setLeftButton(leftName, leftListener).setRightButton(rightName, rightListener).show();
    }






    public void showCustomDialog(String title,View view,String leftName, String rightName, View.OnClickListener leftListener, View.OnClickListener rightListener){
        mBaseDialog.setDialogContentView(view).setTitle(title)
                .setLeftButton(leftName,leftListener)
                .setRightButton(rightName,rightListener).show();
    }



    public void dismissBaseDialog() {
        if (mBaseDialog != null && mBaseDialog.isShowing()) {
            mBaseDialog.dismiss();
        }
    }

    public void cancelBaseDialog() {
        if (mBaseDialog != null && mBaseDialog.isShowing()) {
            mBaseDialog.cancel();
        }
    }


    public void showLoadDialog(final String message) {
        if (!isFinishing()&&!mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    public void dismissLoadDialog() {
        showEmptyLayout(EmptyLayout.STATUS_HIDE);
        if (!isFinishing()) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    public void cancelLoadDialog() {
        if (!isFinishing()) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.cancel();
            }
        }
    }

    public void showChooseDialog(String title, List<String> list, AdapterView.OnItemClickListener listener) {
        ListView view = (ListView) getLayoutInflater().inflate(R.layout.base_dialog_list, null);
        view.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));
        view.setOnItemClickListener(listener);
        mBaseDialog.setDialogContentView(view).setTitle(title).setBottomLayoutVisible(false).show();
    }

    public void addOrReplaceFragment(Fragment fragment) {
        addOrReplaceFragment(fragment, 0);
    }

    /**
     * 第一次加载的时候调用该方法设置resId
     *
     * @param fragment
     * @param resId
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
            return;
        } else if (currentFragment != fragment) {
            if (fragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().hide(currentFragment).show(fragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().hide(currentFragment).add(fragmentContainerResId, fragment).show(fragment).commitAllowingStateLoss();
            }
            currentFragment = fragment;
        }
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
            compositeDisposable=null;
        }
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}
