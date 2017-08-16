package com.example.commonlibrary.mvp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.commonlibrary.R;
import com.example.commonlibrary.baseadapter.EmptyLayout;
import com.example.commonlibrary.cusotomview.BaseDialog;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


import javax.inject.Inject;

import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/4/3      14:21
 * QQ:             1981367757
 */

public abstract class BaseActivity<T,P extends BasePresenter> extends RxAppCompatActivity implements IView<T> {

    //  这里的布局view可能为空，取决于子类布局中是否含有该空布局



    private EmptyLayout mEmptyLayout;
    protected int fragmentContainerResId = 0;
    protected Fragment currentFragment;
    private RelativeLayout headerLayout;
    private ProgressDialog mProgressDialog;
    protected BaseDialog mBaseDialog;
    private RoundAngleImageView icon;
    private TextView right;
    private TextView title;
    private ImageView rightImage;
    protected ImageView back;


    @Nullable
    @Inject
    protected P presenter;



    public ImageView getBack() {
        return back;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNeedHeadLayout()) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            headerLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.header_layout, null);
            linearLayout.addView(headerLayout);
            if (isNeedEmptyLayout()) {
                FrameLayout frameLayout = new FrameLayout(this);
                frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mEmptyLayout = new EmptyLayout(this);
                mEmptyLayout.setVisibility(GONE);
                frameLayout.addView(LayoutInflater.from(this).inflate(getContentLayout(), null));
                frameLayout.addView(mEmptyLayout);
                linearLayout.addView(frameLayout);
                setContentView(linearLayout);
            } else {
                linearLayout.addView(LayoutInflater.from(this).inflate(getContentLayout(), null));
                setContentView(linearLayout);
            }
        } else {
            if (isNeedEmptyLayout()) {
                FrameLayout frameLayout = new FrameLayout(this);
                frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mEmptyLayout = new EmptyLayout(this);
                mEmptyLayout.setVisibility(GONE);
                frameLayout.addView(LayoutInflater.from(this).inflate(getContentLayout(), null));
                frameLayout.addView(mEmptyLayout);
                setContentView(frameLayout);
            } else {
                setContentView(getContentLayout());
            }
        }
        ButterKnife.bind(this);
        initBaseView();
        initData();
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
            icon = (RoundAngleImageView) headerLayout.findViewById(R.id.riv_header_layout_icon);
            title = (TextView) headerLayout.findViewById(R.id.tv_header_layout_title);
            right = (TextView) headerLayout.findViewById(R.id.tv_header_layout_right);
            back = (ImageView) headerLayout.findViewById(R.id.iv_header_layout_back);
            rightImage = (ImageView) headerLayout.findViewById(R.id.iv_header_layout_right);
            rightImage.setVisibility(View.GONE);
            right.setVisibility(View.VISIBLE);
        }
        mProgressDialog = new ProgressDialog(this);
        mBaseDialog = new BaseDialog(this);
        initView();
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
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            back.setVisibility(GONE);
        }

    }


    public void showBaseDialog(String title, String message, String leftName, String rightName, View.OnClickListener leftListener, View.OnClickListener rightListener) {
        mBaseDialog.setTitle(title).setMessage(message).setLeftButton(leftName, leftListener).setRightButton(rightName, rightListener).show();
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
        if (!isFinishing()) {
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
        }
        if (fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().hide(currentFragment).show(fragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().hide(currentFragment).add(fragmentContainerResId, fragment).show(fragment).commitAllowingStateLoss();
        }
        currentFragment = fragment;
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
    protected void onDestroy() {
        super.onDestroy();
        if (presenter!=null) {
            presenter.onDestroy();
        }
    }
}
