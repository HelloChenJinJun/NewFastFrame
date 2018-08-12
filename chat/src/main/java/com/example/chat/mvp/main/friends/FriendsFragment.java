package com.example.chat.mvp.main.friends;

import android.Manifest;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;


import com.example.chat.R;
import com.example.chat.adapter.FriendsAdapter;
import com.example.chat.base.AppBaseFragment;
import com.example.chat.base.Constant;
import com.example.chat.dagger.friends.DaggerFriendsComponent;
import com.example.chat.dagger.friends.FriendsModule;
import com.example.chat.events.UserEvent;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.NearByPeople.NearbyPeopleActivity;
import com.example.chat.mvp.blackList.BlackListActivity;
import com.example.chat.mvp.chat.ChatActivity;
import com.example.chat.mvp.group.groupList.GroupListActivity;
import com.example.chat.mvp.main.HomeFragment;
import com.example.chat.view.IndexView;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.bean.chat.UserEntity;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.PermissionUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;



/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/16     23:44
 * QQ:         1981367757
 */

public class FriendsFragment extends AppBaseFragment<List<UserEntity>, FriendsPresenter> implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, IndexView.MyLetterChangeListener {

    private SwipeRefreshLayout refresh;
    private SuperRecyclerView display;
    private IndexView indexView;
    private TextView index;
    @Inject
    FriendsAdapter adapter;
    private SearchView search;
    private WrappedLinearLayoutManager manager;

    private List<UserEntity> tempList = new ArrayList<>();

    @Override
    public void updateData(List<UserEntity> users) {
        tempList.clear();
        if (users != null && users.size() > 0) {
            tempList.addAll(users);
        }
        if (users != null && users.size() > 1) {
            Collections.sort(users, (user, t1) -> AppUtil.getSortedKey(user.getName()).compareTo(AppUtil.getSortedKey(t1.getNick())));
        }
        adapter.refreshData(users);
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
    protected boolean needStatusPadding() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_friends;
    }


    @Override
    protected void initView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_fragment_friends_refresh);
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_friends_display);
        indexView = (IndexView) findViewById(R.id.index_fragment_friends_index);
        index = (TextView) findViewById(R.id.tv_fragment_friends_index);
        search = (SearchView) findViewById(R.id.sv_fragment_friends_search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!TextUtils.isEmpty(s)) {
                    List<UserEntity> result = new ArrayList<>();
                    for (UserEntity user :
                            tempList) {
                        if (user.getName().contains(s)) {
                            result.add(user);
                        }
                    }
                    adapter.refreshData(result);
                } else {
                    adapter.refreshData(tempList);
                }
                return true;
            }
        });
        refresh.setOnRefreshListener(this);
        indexView.setListener(this);

    }

    @Override
    protected void initData() {
        DaggerFriendsComponent.builder().chatMainComponent(getMainComponent())
                .friendsModule(new FriendsModule(this))
                .build().inject(this);
        display.setLayoutManager(manager = new WrappedLinearLayoutManager(getContext()));
        display.addItemDecoration(new ListViewDecoration(getContext()));
        display.addHeaderView(getHeaderView());
        display.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                ChatActivity.start(getActivity(), Constant.TYPE_PERSON,adapter
                .getData(position).getUid());
            }
        });
        presenter.registerEvent(UserEntity.class, user -> {
            if (user.getUid().equals(UserManager.getInstance().getCurrentUserObjectId())) {
                return;
            }
            if (adapter != null) {
                List<UserEntity> list = adapter.getData();
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    if (AppUtil.getSortedKey(user.getName()).compareTo(AppUtil.getSortedKey(list.get(i).getNick())) == 0) {
                        adapter.addData(i, user);
                        return;
                    }
                }
                adapter.addData(user);
            }
        });
        presenter.registerEvent(UserEvent.class, userEvent -> {
            if (userEvent.getAction() == UserEvent.ACTION_ADD) {
                UserEntity userEntity= UserDBManager.getInstance()
                        .getUser(userEvent.getUid());
                adapter.addData(userEntity);
            }else {
               adapter.deleteFriendById(userEvent.getUid());
            }
        });
    }




    private View getHeaderView() {
        View headerView = LayoutInflater.from(getContext())
                .inflate(R.layout.view_fragment_friends_header, null);
        headerView.findViewById(R.id.tv_view_fragment_friends_header_black).setOnClickListener(this);
        headerView.findViewById(R.id.tv_view_fragment_friends_header_nearby).setOnClickListener(this);
        headerView.findViewById(R.id.tv_view_fragment_friends_header_group).setOnClickListener(this);
        return headerView;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!hidden) {
                ((HomeFragment) getParentFragment()).updateTitle("好友管理");
            }
        }
    }

    @Override
    protected void updateView() {
        presenter.getAllFriends(true);
    }

    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }


    @Override
    public void showLoading(String loadingMsg) {
        refresh.setRefreshing(true);
    }

    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        refresh.setRefreshing(false);
        ToastUtils.showShortToast(errorMsg);
    }


    @Override
    public void hideLoading() {
        refresh.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_view_fragment_friends_header_black) {
            BlackListActivity.start(getActivity());
        }else if (id==R.id.tv_view_fragment_friends_header_nearby){
            NearbyPeopleActivity.start(getActivity());
        }else if (id==R.id.tv_view_fragment_friends_header_group){
            GroupListActivity.start(getActivity());
        }
    }

    @Override
    public void onRefresh() {
        presenter.getAllFriends(true);
    }

    @Override
    public void onLetterChanged(String s) {
        index.setVisibility(View.VISIBLE);
        index.setText(s);
        int size = adapter.getData().size();
        for (int i = 0; i < size; i++) {
            UserEntity bean = adapter.getData(i);
            if (AppUtil.getSortedKey(bean.getName()).equals(s)) {
                manager.scrollToPositionWithOffset(i, 0);
                break;
            }
        }
    }

    @Override
    public void onFinished() {
        index.setVisibility(View.GONE);
    }
}
