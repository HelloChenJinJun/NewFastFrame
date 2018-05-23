package com.example.news.mvp.systemcenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.cusotomview.GridSpaceDecoration;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.news.CenterAdapter;
import com.example.news.LibraryInfoActivity;
import com.example.news.NewsApplication;
import com.example.news.R;
import com.example.news.ScoreQueryActivity;
import com.example.news.bean.CenterBean;
import com.example.news.bean.SystemUserBean;
import com.example.news.dagger.systemcenter.DaggerSystemCenterComponent;
import com.example.news.dagger.systemcenter.SystemCenterModule;
import com.example.news.event.ReLoginEvent;
import com.example.news.mvp.cardinfo.CardInfoActivity;
import com.example.news.mvp.consume.ConsumeQueryActivity;
import com.example.news.mvp.course.CourseQueryActivity;
import com.example.news.util.NewsUtil;
import com.example.news.util.ReLoginUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/17     18:07
 * QQ:         1981367757
 */

public class SystemCenterActivity extends BaseActivity<Object, SystemCenterPresenter> {
    private SuperRecyclerView display;
    private CenterAdapter centerAdapter;
    private ImageView verifyImage;
    private EditText input;
    private ReLoginUtil reLoginUtil;

    @Override
    public void updateData(Object o) {
        if (o != null) {
//            提前隐藏，否则有内存泄漏的危险
            hideLoading();
            if (o instanceof Boolean) {
                mBaseDialog.dismiss();
                if (((Boolean) o)) {
                    LibraryInfoActivity.start(this);
                } else {
                    CardInfoActivity.start(this);
                }
            } else if (o instanceof Bitmap) {
                verifyImage.setImageBitmap((Bitmap) o);
            } else if (o instanceof String) {
                String cookie = ((String) o);
                if (cookie.equals(BaseApplication.getAppComponent().getSharedPreferences()
                        .getString(NewsUtil.LIBRARY_COOKIE, null))) {
                    presenter.getLibraryVerifyImage();
                } else if (cookie.equals(BaseApplication.getAppComponent().getSharedPreferences()
                        .getString(NewsUtil.CARD_LOGIN_COOKIE, null))) {
                    presenter.getCardVerifyImage();
                } else {
                    ToastUtils.showShortToast(((String) o));
                }
            }
        }
    }

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
        return R.layout.activity_system_center;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_system_center_display);

    }

    @Override
    protected void initData() {
        DaggerSystemCenterComponent.builder().newsComponent(NewsApplication.getNewsComponent())
                .systemCenterModule(new SystemCenterModule(this)).build().inject(this);
        centerAdapter = new CenterAdapter();
        display.setLayoutManager(new WrappedGridLayoutManager(this, 3));
        display.addItemDecoration(new GridSpaceDecoration(3, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));
        display.setAdapter(centerAdapter);
        centerAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (position == 0) {
                    ScoreQueryActivity.start(SystemCenterActivity.this);
                } else if (position == 1) {
                    ConsumeQueryActivity.start(SystemCenterActivity.this);
                } else if (position == 2) {
                    mBaseDialog.setDialogContentView(getEditView("library")).setTitle("输入验证码").setLeftButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mBaseDialog.cancel();
                        }
                    }).setRightButton("确定", view12 -> {
                        if (TextUtils.isEmpty(input.getText().toString().trim())) {
                            ToastUtils.showShortToast("验证码不能为空");
                        } else {
                            presenter.libraryLogin(input.getText().toString().trim());
                        }
                    }).show();
                    if (BaseApplication.getAppComponent()
                            .getSharedPreferences().getString(NewsUtil.LIBRARY_COOKIE, null) == null) {
                        presenter.getLibraryCookie();
                    } else {
                        presenter.getLibraryVerifyImage();
                    }
                } else if (position == 3) {
                    String pw=BaseApplication.getAppComponent().getSharedPreferences()
                            .getString(ConstantUtil.ACCOUNT,null);
                    if (pw ==null|| pw.length() == 6) {
                        return;
                    }
                    mBaseDialog.setDialogContentView(getEditView("card")).setTitle("输入验证码").setLeftButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mBaseDialog.cancel();
                        }
                    }).setRightButton("确定", view1 -> {
                        if (TextUtils.isEmpty(input.getText().toString().trim())) {
                            ToastUtils.showShortToast("验证码不能为空");
                        } else {
                            presenter.cardLogin(input.getText().toString().trim());
                        }
                    }).show();
                    if (BaseApplication.getAppComponent()
                            .getSharedPreferences().getString(NewsUtil.CARD_LOGIN_COOKIE, null) == null) {
                        presenter.getCardCookie();
                    } else {
                        presenter.getCardVerifyImage();
                    }
                } else if (position == 4) {
                    CourseQueryActivity.start(SystemCenterActivity.this);
                }
            }
        });
        initToolBar();
        display.post(() -> centerAdapter.addData(getDefaultData()));
    }

    private void initToolBar() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("功能列表");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }


    @Override
    public void showError(String errorMsg, final EmptyLayout.OnRetryListener listener) {
        if (AppUtil.isNetworkAvailable(this)) {
            ToastUtils.showShortToast("Cookie失效");
            String account = BaseApplication.getAppComponent().getSharedPreferences()
                    .getString(ConstantUtil.ACCOUNT, null);
            String password = BaseApplication.getAppComponent().getSharedPreferences()
                    .getString(ConstantUtil.PASSWORD, null);
            reLoginUtil=new ReLoginUtil();
            reLoginUtil.login(account, password, new ReLoginUtil.CallBack() {
                @Override
                public void onSuccess(SystemUserBean systemUserBean) {
                    if (listener != null) {
                        listener.onRetry();
                    }
                }

                @Override
                public void onFailed(String errorMessage) {
                    ToastUtils.showShortToast("重试失败" + errorMessage);
                    hideLoading();
                    input.setText("");
                }
            });
            return;
        }
        super.showError(errorMsg, listener);
    }


    private View getEditView(String tag) {
        View view = getLayoutInflater().inflate(R.layout.view_activity_system_center_edit_view, null, false);
        verifyImage = view.findViewById(R.id.iv_view_activity_system_center_edit_view_verify);
        input = view.findViewById(R.id.et_view_activity_system_center_edit_view_input);
        verifyImage.setTag(tag);
        verifyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = (String) view.getTag();
                if (str != null && str.equals("card")) {
                    presenter.getCardVerifyImage();
                } else {
                    presenter.getLibraryVerifyImage();
                }

            }
        });
        return view;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reLoginUtil != null) {
            reLoginUtil.clear();
        }
    }

    private List<CenterBean> getDefaultData() {
        List<CenterBean> data = new ArrayList<>();
        CenterBean item = new CenterBean();
        item.setResId(R.drawable.ic_demo_one);
        item.setTitle("成绩查询");
        data.add(item);
        CenterBean consumerQuery = new CenterBean();
        consumerQuery.setTitle("消费查询");
        consumerQuery.setResId(R.drawable.ic_demo_two);
        data.add(consumerQuery);
        CenterBean libraryBean = new CenterBean();
        libraryBean.setTitle("图书馆");
        libraryBean.setResId(R.drawable.ic_demo_three);
        CenterBean cardBean = new CenterBean();
        cardBean.setTitle("一卡通");
        cardBean.setResId(R.drawable.ic_demo_four);
        data.add(libraryBean);
        data.add(cardBean);
        CenterBean courseBean = new CenterBean();
        courseBean.setTitle("课表查询");
        courseBean.setResId(R.drawable.ic_demo_five);
        data.add(courseBean);
        return data;
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SystemCenterActivity.class);
        activity.startActivity(intent);
    }
}
