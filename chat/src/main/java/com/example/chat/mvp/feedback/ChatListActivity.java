package com.example.chat.mvp.feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.chat.R;
import com.example.chat.adapter.ChatListAdapter;
import com.example.chat.base.ChatBaseActivity;
import com.example.chat.bean.ChatBean;
import com.example.chat.dagger.feedback.ChatListModule;
import com.example.chat.dagger.feedback.DaggerChatListComponent;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemChildLongClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.customview.ToolBarOption;
import com.example.commonlibrary.customview.swipe.CustomSwipeRefreshLayout;
import com.example.commonlibrary.utils.SoftHideBoardUtil;
import com.example.commonlibrary.utils.SystemUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


/**
 * 项目名称:    SecondhandMarket
 * 创建人:      李晨
 * 创建时间:    2018/4/26     17:08
 * QQ:         1981367757
 */

public class ChatListActivity extends ChatBaseActivity<List<ChatBean>, ChatListPresenter> implements CustomSwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @Inject
    ChatListAdapter adapter;


    private SuperRecyclerView display;
    private CustomSwipeRefreshLayout refresh;
    private EditText input;
    private WrappedLinearLayoutManager manager;

    @Override
    public void updateData(List<ChatBean> chatBeans) {
        adapter.refreshData(chatBeans);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SoftHideBoardUtil.assistActivity(findViewById(R.id.ll_activity_chat_list_container), isHide -> {
            if (!isHide) {
                scrollToBottom();
            }
        });
    }

    private void scrollToBottom() {
        manager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
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
        return R.layout.activity_chat_list;
    }


    @Override
    protected void initView() {
        refresh = findViewById(R.id.refresh_activity_chat_list_refresh);
        display = findViewById(R.id.srcv_activity_chat_list_display);
        input = findViewById(R.id.et_activity_chat_list_input);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                scrollToBottom();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        findViewById(R.id.iv_activity_chat_list_send).setOnClickListener(this);
        refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        DaggerChatListComponent
                .builder().chatMainComponent(getChatMainComponent())
                .chatListModule(new ChatListModule(this))
                .build().inject(this);
        display.setLayoutManager(manager = new WrappedLinearLayoutManager(this));
        display.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnSimpleItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(int position, View view, int id) {
                showBaseDialog("删除操作", "是否删除该留言", "取消", "确定", null, v -> {
                    showLoadDialog("正在删除中。。。。。。。");
                    presenter.addSubscription(adapter.getData(position).delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            dismissLoadDialog();
                            if (e == null) {
                                ToastUtils.showShortToast("删除成功");
                                adapter.removeData(position);
                            } else {
                                ToastUtils.showShortToast("删除失败" + e.toString());
                            }
                        }
                    }));
                });
                return true;
            }
        });
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setTitle("留言界面");
        setToolBar(toolBarOption);
        presenter.registerEvent(ChatBean.class, chatBean -> {
            input.setText("");
            ToastUtils.showShortToast("留言成功");
            adapter.addData(chatBean);
            SystemUtil.hideSoftInput(ChatListActivity.this, input);
        });
        presenter.getData();
    }

    @Override
    public void onRefresh() {
        presenter.getData();
    }


    @Override
    public void showLoading(String loadMessage) {
        refresh.setRefreshing(true);
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        refresh.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_activity_chat_list_send) {
            String content = input.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                ToastUtils.showShortToast("内容不能为空");
                return;
            }
            showLoadDialog("正在留言中..........");
            presenter.sendChatBean(content);

        }
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ChatListActivity.class);
        activity.startActivity(intent);
    }
}
