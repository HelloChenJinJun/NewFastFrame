package com.example.commonlibrary.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.commonlibrary.R;
import com.example.commonlibrary.baseadapter.EmptyLayout;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;

/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/4/3      14:24
 * QQ:             1981367757
 */

public abstract class BaseFragment<T,P extends BasePresenter> extends RxFragment implements IView<T> {

    /**
     * 采用懒加载
     */
    protected View root;
    private EmptyLayout mEmptyLayout;
    private boolean hasInit = false;
    private RelativeLayout headerLayout;
    private ImageView icon;
    private TextView right;
    private TextView title;
    private ImageView rightImage;
    protected ImageView back;
    Unbinder unbinder;

    @Nullable
    @Inject
    protected P presenter;


    protected abstract boolean isNeedHeadLayout();

    protected abstract boolean isNeedEmptyLayout();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (root == null) {
            if (isNeedHeadLayout()) {
                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                headerLayout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.header_layout, null);
                linearLayout.addView(headerLayout);
                linearLayout.addView(LayoutInflater.from(getActivity()).inflate(getContentLayout(), null));
                if (isNeedEmptyLayout()) {
                    FrameLayout frameLayout = new FrameLayout(getActivity());
                    frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    mEmptyLayout = new EmptyLayout(getActivity());
                    mEmptyLayout.setVisibility(GONE);
                    frameLayout.addView(linearLayout);
                    frameLayout.addView(mEmptyLayout);
                    root = frameLayout;
                } else {
                    root = linearLayout;
                }
            } else {
                if (isNeedEmptyLayout()) {
                    FrameLayout frameLayout = new FrameLayout(getActivity());
                    frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    mEmptyLayout = new EmptyLayout(getActivity());
                    mEmptyLayout.setVisibility(GONE);
                    frameLayout.addView(LayoutInflater.from(getActivity()).inflate(getContentLayout(), null));
                    frameLayout.addView(mEmptyLayout);
                    root = frameLayout;
                } else {
                    root = inflater.inflate(getContentLayout(), container, false);
                }
            }
            if (root.getParent() != null) {
                ((ViewGroup) root.getParent()).removeView(root);
            }
            if (container != null) {
                CommonLogger.e("添加父类");
                container.addView(root);
            }
            unbinder = ButterKnife.bind(this, root);
            initBaseView();
            initData();
        }
        if (root.getParent() != null) {
            ((ViewGroup) root.getParent()).removeView(root);
        }
        return root;
    }

    private void initBaseView() {
        if (isNeedHeadLayout()) {
            icon = (ImageView) headerLayout.findViewById(R.id.riv_header_layout_icon);
            title = (TextView) headerLayout.findViewById(R.id.tv_header_layout_title);
            right = (TextView) headerLayout.findViewById(R.id.tv_header_layout_right);
            back = (ImageView) headerLayout.findViewById(R.id.iv_header_layout_back);
            rightImage = (ImageView) headerLayout.findViewById(R.id.iv_header_layout_right);
            rightImage.setVisibility(View.GONE);
            right.setVisibility(View.VISIBLE);
        }
        initView();
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
                    getActivity().finish();
                }
            });
        } else {
            back.setVisibility(GONE);
        }

    }


    @Override
    public void showLoading(String loadingMsg) {
        if (mEmptyLayout != null) {
            mEmptyLayout.setCurrentStatus(EmptyLayout.STATUS_LOADING);
        } else {
            if (!getActivity().isFinishing()) {
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).showLoadDialog(loadingMsg);
                }
            }
        }
    }

    @Override
    public void hideLoading() {
        if (mEmptyLayout != null) {
            mEmptyLayout.setCurrentStatus(EmptyLayout.STATUS_HIDE);
        } else {
            if (!getActivity().isFinishing()) {
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).dismissLoadDialog();
                }
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


    public ImageView getIcon() {
        return icon;
    }


}
