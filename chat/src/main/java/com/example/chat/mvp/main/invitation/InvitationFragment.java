package com.example.chat.mvp.main.invitation;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;

import com.example.chat.R;
import com.example.chat.adapter.NewFriendAdapter;
import com.example.chat.base.Constant;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.User;
import com.example.chat.events.AddEvent;
import com.example.chat.events.RecentEvent;
import com.example.chat.events.RefreshMenuEvent;
import com.example.chat.events.UserEvent;
import com.example.chat.listener.AddFriendCallBackListener;
import com.example.chat.listener.OnSendTagMessageListener;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.main.HomeFragment;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemChildClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.baseadapter.swipeview.SwipeMenuItem;
import com.example.commonlibrary.baseadapter.swipeview.SwipeMenuRecyclerView;
import com.example.commonlibrary.bean.chat.ChatMessageEntity;
import com.example.commonlibrary.bean.chat.ChatMessageEntityDao;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.rxbus.RxBusManager;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import io.reactivex.functions.Consumer;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/5      18:41
 * QQ:             1981367757
 */
public class InvitationFragment extends BaseFragment {
        private NewFriendAdapter adapter;
        private SwipeMenuRecyclerView display;


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
                return R.layout.fragment_invitation;
        }

        @Override
        public void initView() {
                display = (SwipeMenuRecyclerView) findViewById(R.id.swrc_new_friend_display);
                display.setLayoutManager(new WrappedLinearLayoutManager(getActivity()));
                display.setHasFixedSize(true);
                display.addItemDecoration(new ListViewDecoration(getActivity()));
                display.setSwipeMenuCreator((swipeLeftMenu, swipeRightMenu, viewType) -> {
                        int width = (int) getResources().getDimension(R.dimen.recent_top_height);
                        int height = ViewGroup.LayoutParams.MATCH_PARENT;
                        SwipeMenuItem item = new SwipeMenuItem(getActivity());
                        item.setBackgroundDrawable(new ColorDrawable(Color.rgb(0xF9,
                                0x3F, 0x25))).setText("删除").setTextColor(Color.WHITE).setHeight(height).setWidth(width);
                        swipeRightMenu.addMenuItem(item);
                });
        }


        @Override
        public void initData() {
               addDisposable(RxBusManager.getInstance().registerEvent(AddEvent.class, addEvent -> {
                       ChatMessageEntity chatMessageEntity=UserDBManager.getInstance()
                               .getChatMessageForType(addEvent.getId(),ChatMessage.MESSAGE_TYPE_ADD);
                       adapter.addData(0,chatMessageEntity);
               }));

                adapter = new NewFriendAdapter();
               adapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {
                       @Override
                       public void onItemChildClick(final int position, View view, int id) {
                               if (id == R.id.btn_new_friend_item_agree) {
                                       showLoading("正在添加........");
                                       LogUtil.e("正在同意添加为好友.........");
                                       ChatMessageEntity chatMessageEntity = adapter.getData(position);
                                       UserManager.getInstance().addNewFriend(chatMessageEntity.getBelongId(), chatMessageEntity.getToId(),
                                               new AddFriendCallBackListener() {
                                                       @Override
                                                       public void onSuccess(User user) {
                                                               LogUtil.e("正在发送回执同意消息.........");
                                                               MsgManager.getInstance().sendTagMessage(user.getObjectId(),ChatMessage.MESSAGE_TYPE_AGREE,
                                                                       new OnSendTagMessageListener() {
                                                                               @Override
                                                                               public void onSuccess(ChatMessage chatMessage) {
                                                                                       hideLoading();
                                                                                       LogUtil.e("发送同意消息成功");
                                                                                       LogUtil.e("最终添加好友成功添加好友成功");
                                                                                       chatMessageEntity.setReadStatus(Constant.READ_STATUS_READED);
                                                                                       UserDBManager
                                                                                               .getInstance()
                                                                                               .getDaoSession()
                                                                                               .getChatMessageEntityDao()
                                                                                               .insertOrReplace(chatMessageEntity);
                                                                                       adapter.addData(chatMessageEntity);
                                                                                       RxBusManager.getInstance().post(new RefreshMenuEvent(2));
                                                                                       RxBusManager.getInstance().post(new RefreshMenuEvent(0));
                                                                                       RxBusManager.getInstance().post(new RecentEvent(chatMessageEntity.getBelongId(),RecentEvent.ACTION_ADD));
                                                                                       RxBusManager.getInstance().post(new UserEvent(chatMessageEntity.getBelongId(),UserEvent.ACTION_ADD));
                                                                               }

                                                                               @Override
                                                                               public void onFailed(BmobException e) {
                                                                                       hideLoading();
                                                                                       LogUtil.e("发送同意消息失败"+e.getMessage()+e.getErrorCode());
                                                                               }
                                                                       });
                                                       }

                                                       @Override
                                                       public void onFailed(BmobException e) {
                                                               hideLoading();
                                                               LogUtil.e("添加好友失败" + e.getMessage() + e.getErrorCode());
                                                       }
                                               });
                               }
                       }
               });
                display.setSwipeMenuItemClickListener((closeable, adapterPosition, menuPosition, direction) -> {
                        ChatMessageEntity msg = adapter.getData(adapterPosition);
                        UserDBManager.getInstance().deleteChatMessage(msg);
                        adapter.removeData(adapterPosition);
                        ((HomeFragment) getParentFragment()).notifyMenuUpdate(2);
                });
                display.setAdapter(adapter);
        }


        @Override
        public void onResume() {
                super.onResume();
                onHiddenChanged(false);
        }

        @Override
        public void onHiddenChanged(boolean hidden) {
                super.onHiddenChanged(hidden);
                if (!hidden) {
                        ((HomeFragment) getParentFragment()).updateTitle("邀请");
                }

        }




        @Override
        protected void updateView() {
                List<ChatMessageEntity>  list=UserDBManager
                        .getInstance().getDaoSession()
                        .getChatMessageEntityDao()
                        .queryBuilder().where(ChatMessageEntityDao
                        .Properties.ToId.eq(UserManager
                                .getInstance()
                                .getCurrentUserObjectId())
                        ,ChatMessageEntityDao.Properties.MessageType
                        .eq(ChatMessage.MESSAGE_TYPE_ADD)).build().list();
                adapter.addData(list);
        }



        @Override
        public void updateData(Object o) {

        }
}
