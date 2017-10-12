package com.example.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.AlphabetIndexer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.R;
import com.example.chat.adapter.ContactsAdapter;
import com.example.chat.base.Constant;
import com.example.chat.bean.User;
import com.example.chat.db.ChatDB;
import com.example.chat.listener.OnEditDataCompletedListener;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserCacheManager;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.LogUtil;
import com.example.chat.view.MyLetterView;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.SaveListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/27      20:36
 * QQ:             1981367757
 */
public class SelectedFriendsActivity extends SlideBaseActivity implements MyLetterView.MyLetterChangeListener, ContactsAdapter.OnItemCheckListener {
        RecyclerView display;
        MyLetterView mMyLetterView;
        SectionIndexer mIndexer;
        TextView middle;
        String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private LinearLayout bottomDisplay;
        private List<String> selectedContacts = new ArrayList<>();
        private ContactsAdapter adapter;
        private String from;
        private LinearLayoutManager mLinearLayoutManager;


        @Override
        public void initView() {
                display = (RecyclerView) findViewById(R.id.rcv_selected_friends_display);
                mMyLetterView = (MyLetterView) findViewById(R.id.ml_selected_friends_letter);
                middle = (TextView) findViewById(R.id.tv_selected_friends_middle);
                bottomDisplay = (LinearLayout) findViewById(R.id.ll_selected_friends_bottom);
                mMyLetterView.setListener(this);
        }

        @Override
        public void initData() {
                mLinearLayoutManager = new LinearLayoutManager(this);
                display.setLayoutManager(mLinearLayoutManager);
                display.addItemDecoration(new ListViewDecoration(this));
                display.setItemAnimator(new DefaultItemAnimator());
                mMyLetterView.setTextView(middle);
                adapter = new ContactsAdapter();
                adapter.setOnCheckedChangeListener(this);
                mIndexer = new AlphabetIndexer(ChatDB.create().getSortedKeyCursor(), 0, alphabet);
                adapter.setSectionIndexer(mIndexer);
                display.setAdapter(adapter);
                adapter.addData(UserCacheManager.getInstance().getAllContacts());
                initActionBar();
        }

        private void initActionBar() {
                from = getIntent().getStringExtra("from");
                ToolBarOption toolBarOption = new ToolBarOption();
                toolBarOption.setNeedNavigation(true);
                toolBarOption.setTitle("选择好友");
                toolBarOption.setRightText("完成");
                toolBarOption.setRightListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (from.equals("createGroup")) {
                                        List<String> list = new ArrayList<>();
                                        list.add("群组名");
                                        list.add("群描述");
                                        if (selectedContacts != null && selectedContacts.size() > 0) {
                                                showEditDialog("建群", list, new OnEditDataCompletedListener() {
                                                        @Override
                                                        public void onDataInputCompleted(List<String> data) {
                                                                if (!CommonUtils.isNetWorkAvailable(BaseApplication.getInstance())) {
                                                                        LogUtil.e("网络连接失败");
                                                                        ToastUtils.showShortToast("网络连接失败");
                                                                        return;
                                                                }
                                                                if (selectedContacts.size() > 0) {
                                                                        showLoadDialog("正在建群中.....");
                                                                        MsgManager.getInstance().sendCreateGroupMessage(data.get(0), data.get(1), selectedContacts, new SaveListener() {
                                                                                @Override
                                                                                public void onSuccess() {
                                                                                        LogUtil.e("1总体建群成功");
                                                                                        ToastUtils.showShortToast("建群成功");
                                                                                        dismissLoadDialog();
                                                                                        finish();
                                                                                }
                                                                                @Override
                                                                                public void onFailure(int i, String s) {
                                                                                        dismissLoadDialog();
                                                                                        LogUtil.e("总体建群失败" + s + i);
                                                                                }
                                                                        });
                                                                } else {
                                                                        ToastUtils.showShortToast("你没有选择好友");
                                                                }
                                                        }
                                                });
                                        }
                                } else if (from.equals("select_visibility")) {
                                        Intent intent = new Intent();
                                        intent.putStringArrayListExtra(Constant.RESULT_CODE_SELECT_VISIBILITY, (ArrayList<String>) selectedContacts);
                                        setResult(Activity.RESULT_OK, intent);
                                        finish();
                                }
                        }
                });
                toolBarOption.setAvatar(null);
                setToolBar(toolBarOption);
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
                return R.layout.selected_friends;
        }

        @Override
        public void onLetterChanged(String s) {
                int index = alphabet.indexOf(s);
                mLinearLayoutManager.scrollToPosition(mIndexer.getPositionForSection(index));
        }

        private ImageView getImageView(String url, String tag) {
                LogUtil.e("添加View11111 ");
                ImageView avatar = new ImageView(SelectedFriendsActivity.this);
                int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
                avatar.setLayoutParams(params);
                avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                avatar.setPadding(5, 5, 5, 5);
                avatar.setTag(tag);
                Glide.with(this).load(url).into(avatar);
                return avatar;
        }



        @Override
        public void onItemChecked(boolean isCheck, User user, BaseWrappedViewHolder holder) {
                if (isCheck) {
                        selectedContacts.add(user.getObjectId());
                        bottomDisplay.addView(getImageView(user.getAvatar(), user.getObjectId()));

                } else {
                        selectedContacts.remove(user.getObjectId());
                        if (bottomDisplay.findViewWithTag(user.getObjectId()) != null) {
                                LogUtil.e("移除View");
                                bottomDisplay.removeView(bottomDisplay.findViewWithTag(user.getObjectId()));
                        }
                }
                bottomDisplay.invalidate();
        }

        @Override
        public void updateData(Object o) {

        }
}
