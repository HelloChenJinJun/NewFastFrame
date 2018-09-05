package com.example.commonlibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.StatusBarUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static android.view.View.GONE;

/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/4/3      14:24
 * QQ:             1981367757
 */

public abstract class BaseFragment<T, P extends BasePresenter> extends RxFragment implements IView<T> {

    /**
     * 采用懒加载
     */
    protected View root;
    private EmptyLayout mEmptyLayout;
    private boolean hasInit = false;
    protected View headerLayout;
    private ImageView icon;
    private TextView right;
    private TextView title;
    private ImageView rightImage;
    protected ImageView back;

    @Nullable
    @Inject
    protected P presenter;
    private CompositeDisposable compositeDisposable;




    protected void addDisposable(Disposable disposable){
        if (compositeDisposable == null) {
            compositeDisposable=new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }


    protected abstract boolean isNeedHeadLayout();

    protected abstract boolean isNeedEmptyLayout();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (root == null) {
            if (isNeedHeadLayout()) {
                LinearLayout linearLayout =new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                headerLayout =  LayoutInflater.from(getActivity()).inflate(R.layout.header_layout, null);
                linearLayout.addView(headerLayout);
                if (isNeedEmptyLayout()) {
                    FrameLayout frameLayout = new FrameLayout(getActivity());
                    frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    frameLayout.addView(LayoutInflater.from(getActivity()).inflate(getContentLayout(),null));
                    mEmptyLayout = new EmptyLayout(getActivity());
                    mEmptyLayout.setVisibility(GONE);
                    mEmptyLayout.setContentView(frameLayout.getChildAt(0));
                    frameLayout.addView(mEmptyLayout);
                    linearLayout.addView(frameLayout);
                } else {
                    linearLayout.addView(LayoutInflater.from(getActivity()).inflate(getContentLayout(), null));
                }
                root=linearLayout;
            } else {
                if (isNeedEmptyLayout()) {
                    FrameLayout frameLayout =new FrameLayout(getContext());
                    frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    frameLayout.addView(LayoutInflater.from(getActivity()).inflate(getContentLayout(), null));
                    mEmptyLayout = new EmptyLayout(getActivity());
                    mEmptyLayout.setVisibility(GONE);
                    mEmptyLayout.setContentView(frameLayout.getChildAt(0));
                    frameLayout.addView(mEmptyLayout);
                    root = frameLayout;
                }else {
                    root=LayoutInflater.from(getActivity()).inflate(getContentLayout(), null);
                }
            }
            if (root.getParent() != null) {
                ((ViewGroup) root.getParent()).removeView(root);
            }
            if (container != null) {
                container.addView(root);
            }
            initBaseView();
            initData();
        }
        if (root.getParent() != null) {
            ((ViewGroup) root.getParent()).removeView(root);
        }
        if (needStatusPadding()) {
                StatusBarUtil.setStatusPadding(getPaddingView());
        }
        return root;
    }



    private View getPaddingView() {
        if (needStatusPadding()) {
            return headerLayout!=null?headerLayout:root;
        }
        return null;
    }

    protected boolean needStatusPadding() {
        return true;
    }

    private void initBaseView() {
        if (isNeedHeadLayout()) {
            icon = headerLayout.findViewById(R.id.riv_header_layout_icon);
            title = headerLayout.findViewById(R.id.tv_header_layout_title);
            right = headerLayout.findViewById(R.id.tv_header_layout_right);
            back = headerLayout.findViewById(R.id.iv_header_layout_back);
            rightImage = headerLayout.findViewById(R.id.iv_header_layout_right);
            rightImage.setVisibility(View.GONE);
            right.setVisibility(View.VISIBLE);
            ((AppCompatActivity) getActivity()).setSupportActionBar(headerLayout.findViewById(R.id.toolbar));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        }
        initView();
    }


    public void showCustomDialog(String title, View view, String leftName, String rightName, View.OnClickListener leftListener, View.OnClickListener rightListener) {
        ((BaseActivity) getActivity()).showCustomDialog(title, view, leftName, rightName, leftListener, rightListener);
    }



    public void showBaseDialog(String title,String message,String leftName,String rightName,View.OnClickListener leftListener, View.OnClickListener rightListener){
        ((BaseActivity) getActivity()).showBaseDialog(title, message, leftName, rightName, leftListener, rightListener);
    }


    protected void updateTitle(String title) {
        ((BaseActivity) getActivity()).updateTitle(title);
    }


    protected View findViewById(int id) {
        if (root != null) {
            return root.findViewById(id);
        }
        return null;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (root != null && getUserVisibleHint() && !hasInit) {
            hasInit = true;
            updateView();
        }
    }


    /**
     * 视图真正可见的时候才调用
     */

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (root != null && isVisibleToUser && !hasInit) {
            hasInit = true;
            updateView();
        }
    }

    protected abstract int getContentLayout();


    protected abstract void initView();

    protected abstract void initData();

    protected abstract void updateView();


    public void setToolBar(ToolBarOption option) {
        if (!isNeedHeadLayout()) {
            return;
        }

        if (option.getBgColor() != 0) {
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
            back.setOnClickListener(v -> getActivity().finish());
        } else {
            back.setVisibility(GONE);
        }

    }


    @Override
    public void showLoading(String loadingMsg) {
        if (mEmptyLayout != null) {
            mEmptyLayout.setCurrentStatus(EmptyLayout.STATUS_LOADING);
        } else {
            showLoadDialog(loadingMsg);
        }
    }


    protected void showLoadDialog(String message) {
        if (!getActivity().isFinishing()) {
            if (getActivity() instanceof BaseActivity) {
                ((BaseActivity) getActivity()).showLoadDialog(message);
            }
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



    protected void dismissLoadDialog(){
        if (!getActivity().isFinishing()) {
            if (getActivity() instanceof BaseActivity) {
                ((BaseActivity) getActivity()).dismissLoadDialog();
            }
        }
    }


    protected void hideBaseDialog() {
        if (getActivity() instanceof BaseActivity && !getActivity().isFinishing()) {
            ((BaseActivity) getActivity()).dismissBaseDialog();
        }
    }


    protected void showChooseDialog(String title, List<String> list, AdapterView.OnItemClickListener listener) {
        if (getActivity() instanceof BaseActivity && !getActivity().isFinishing()) {
            ((BaseActivity) getActivity()).showChooseDialog(title, list, listener);
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


    public void showEmptyLayout(int status) {
        if (mEmptyLayout != null) {
            mEmptyLayout.setCurrentStatus(status);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (compositeDisposable != null) {
            if (!compositeDisposable.isDisposed()) {
                compositeDisposable.dispose();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
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


    public ImageView getIcon() {
        return icon;
    }


    public void addOrReplaceFragment(Fragment fragment) {
        addOrReplaceFragment(fragment, 0);
    }


    protected int fragmentContainerResId = 0;
    protected Fragment currentFragment;

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
            getChildFragmentManager().beginTransaction().add(resId, fragment).show(fragment).commitAllowingStateLoss();
            currentFragment = fragment;
            return;
        }
        if (fragment.isAdded()) {
            getChildFragmentManager().beginTransaction().hide(currentFragment).show(fragment).commit();
        } else {
            getChildFragmentManager().beginTransaction().hide(currentFragment).add(fragmentContainerResId, fragment).show(fragment).commitAllowingStateLoss();
        }
        currentFragment = fragment;
    }






}
