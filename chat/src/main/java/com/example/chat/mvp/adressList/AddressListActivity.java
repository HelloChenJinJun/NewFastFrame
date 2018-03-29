package com.example.chat.mvp.adressList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.chat.R;
import com.example.chat.adapter.AddressListAdapter;
import com.example.chat.base.Constant;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.bean.CityInfoBean;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.FileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/29     10:51
 * QQ:         1981367757
 */

public class AddressListActivity extends SlideBaseActivity {
    private SuperRecyclerView display;
    private AddressListAdapter addressListAdapter;
    private CityInfoBean cityInfoBean;
    private String name;

    @Override
    public void updateData(Object o) {

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
        return R.layout.activity_address_list;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_address_list_display);
    }

    @Override
    protected void initData() {
        addressListAdapter = new AddressListAdapter();
        display.setLayoutManager(new WrappedLinearLayoutManager(this));
        display.addItemDecoration(new ListViewDecoration(this));
        display.setAdapter(addressListAdapter);
        addressListAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                CityInfoBean cityInfoBean = addressListAdapter.getData(position);
                if (AddressListActivity.this.cityInfoBean == null) {
                    name = cityInfoBean.getName().trim();
//                    这是省份，赋值
                    if (cityInfoBean.getName().trim().endsWith("市")) {
//                        直辖市
                        if (cityInfoBean.getName().trim().equals("重庆市")) {
                            cityInfoBean.getCityList().get(0).getCityList().addAll(cityInfoBean.getCityList().get(1).getCityList());
                        }
                        AddressListActivity.start(AddressListActivity.this, cityInfoBean.getCityList().get(0), Constant.REQUEST_CODE_NORMAL);
                        return;
                    }
                }
                if (cityInfoBean.getCityList() != null && cityInfoBean.getCityList().size() > 0) {

                    AddressListActivity.start(AddressListActivity.this, cityInfoBean, Constant.REQUEST_CODE_NORMAL);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Constant.CONTENT, AddressListActivity.this.cityInfoBean.getName() + cityInfoBean.getName().trim());
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });

        cityInfoBean = getIntent().getParcelableExtra(Constant.CONTENT);


        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setNeedNavigation(true);
        if (cityInfoBean != null) {
            addressListAdapter.refreshData(cityInfoBean.getCityList());
            if (cityInfoBean.getName().trim().endsWith("省")
                    || cityInfoBean
                    .getName().trim().equals("北京市")
                    || cityInfoBean
                    .getName().trim().equals("上海市")
                    || cityInfoBean
                    .getName().trim().equals("天津市")
                    || cityInfoBean
                    .getName().trim().equals("重庆市")
                    || cityInfoBean
                    .getName().trim().equals("北京市")
                    || cityInfoBean
                    .getName().trim().equals("内蒙古自治区")
                    || cityInfoBean
                    .getName().trim().equals("广西壮族自治区")
                    || cityInfoBean
                    .getName().trim().equals("西藏自治区")
                    || cityInfoBean
                    .getName().trim().equals("宁夏回族自治区")
                    || cityInfoBean
                    .getName().trim().equals("新疆维吾尔自治区")
                    || cityInfoBean
                    .getName().trim().equals("香港")
                    || cityInfoBean
                    .getName().trim().equals("澳门")
                    ) {
                toolBarOption.setTitle("请选择城市");
            } else {
                toolBarOption.setTitle("请选择区县");
            }
        } else {
//            省份数据
            toolBarOption.setTitle("请选择省份");
            Type type = new TypeToken<ArrayList<CityInfoBean>>() {
            }.getType();
            //解析省份
            ArrayList<CityInfoBean> mProvinceBeanArrayList = new Gson().fromJson(FileUtil.getJson(this, "china_city_data.json"), type);
            addressListAdapter.refreshData(mProvinceBeanArrayList);
        }
        setToolBar(toolBarOption);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.REQUEST_CODE_NORMAL) {
                String content = data.getStringExtra(Constant.CONTENT);
                Intent intent = new Intent();
                StringBuilder stringBuilder = new StringBuilder();

                if (cityInfoBean != null && !content.startsWith(cityInfoBean.getName())) {
                    if (name != null&&!cityInfoBean.getName().trim().equals(name.trim())) {
                        stringBuilder.append(name);
                    }
                    stringBuilder.append(cityInfoBean.getName());
                }
                if (name != null&&!content.trim().startsWith(name.trim())) {
                    stringBuilder.append(name);
                }
                stringBuilder.append(content);
                intent.putExtra(Constant.CONTENT, stringBuilder.toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        } else {
            name = null;
        }
    }

    public static void start(Activity activity, CityInfoBean cityInfoBean, int requestCode) {
        Intent intent = new Intent(activity, AddressListActivity.class);
        intent.putExtra(Constant.CONTENT, cityInfoBean);
        activity.startActivityForResult(intent, requestCode);
    }
}
