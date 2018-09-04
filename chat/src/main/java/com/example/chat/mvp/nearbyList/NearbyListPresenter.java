package com.example.chat.mvp.nearbyList;

import android.app.Activity;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.chat.base.AppBasePresenter;
import com.example.chat.bean.NearbyListBean;
import com.example.chat.events.LocationEvent;
import com.example.chat.manager.NewLocationManager;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;


import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/28     9:28
 * QQ:         1981367757
 */

public class NearbyListPresenter extends AppBasePresenter<IView<List<NearbyListBean>>, DefaultModel> {
    private int page = 0;
    private PoiSearch.Query query;


    public NearbyListPresenter(IView<List<NearbyListBean>> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void getNearbyListData(LocationEvent locationEvent, boolean isRefresh) {
        String mType = "汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施" +
                "|学校";
        query = new PoiSearch.Query("", mType, locationEvent.getCity());
        query.setCityLimit(true);
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        if (isRefresh) {
            iView.showLoading("正在搜索周边位置....");
            page = 0;
        } else {
            page++;
        }
        query.setPageNum(page);
        PoiSearch poiSearch = new PoiSearch(((Activity) iView), query);
        //以当前定位的经纬度为准搜索周围5000米范围
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(locationEvent.getLatitude(), locationEvent.getLongitude()), 1000, true));//
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult result, int code) {

                if (code == 1000) {
                    List<NearbyListBean> listBeans = new ArrayList<>();
                    if (result != null && result.getQuery() != null) {// 搜索poi的结果
                        if (result.getQuery().equals(query)) {// 是否是同一条
                            List<PoiItem> poiItems = result.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                            if (poiItems != null && poiItems.size() > 0) {
                                for (int i = 0; i < poiItems.size(); i++) {
                                    PoiItem poiItem = poiItems.get(i);
                                    NearbyListBean nearbyListBean = new NearbyListBean();
                                    nearbyListBean.setCheck(false);
                                    LocationEvent event = new LocationEvent();
                                    event.setLatitude(poiItem.getLatLonPoint().getLatitude());
                                    event.setLongitude(poiItem.getLatLonPoint().getLongitude());
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append(poiItem.getProvinceName()).append(poiItem.getCityName()).append(poiItem.getAdName());
                                    if (!poiItem.getAdName().equals(poiItem.getSnippet())) {
                                        stringBuilder.append(poiItem.getSnippet());
                                    }
                                    stringBuilder.append(poiItem.getTitle());
                                    event.setLocation(stringBuilder.toString());
                                    event.setTitle(poiItem.getTitle());
                                    event.setCity(poiItem.getCityName());
                                    event.setProvince(poiItem.getProvinceName());
                                    nearbyListBean.setLocationEvent(event);
                                    CommonLogger.e(poiItem.getTitle() + "," + poiItem.getProvinceName() + ","
                                            + poiItem.getCityName() + ","
                                            + poiItem.getAdName() + ","//区
                                            + poiItem.getSnippet() + ","
                                            + poiItem.getBusinessArea() + ","
                                            + poiItem.getLatLonPoint() + "\n");
                                    listBeans.add(nearbyListBean);
                                }
                            }
                        }
                    }
                    if (listBeans.size() == 0) {
                        page--;
                    }
                    iView.updateData(listBeans);
                    iView.hideLoading();
                } else {
                    iView.showError(code + "", () -> {
                        page--;
                        getNearbyListData(locationEvent, isRefresh);
                    });
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(locationEvent.getLatitude(), locationEvent.getLongitude()), 1000, true));//
        poiSearch.searchPOIAsyn();// 异步搜索

    }


    public void getLocation() {
        NewLocationManager.getInstance().startLocation();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
