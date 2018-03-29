package com.example.chat.manager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.chat.listener.LocationChangedListener;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseApplication;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/3      16:04
 * QQ:             1981367757
 */

public class LocationManager implements AMapLocationListener {
        private static LocationManager insatance;
        private AMapLocationClient mLocationClient = null;
        private double latitude;
        private double longitude;
        private String address;
        private List<String> locationList;

        public static LocationManager getInstance() {
                if (insatance == null) {
                        synchronized (LocationManager.class) {
                                if (insatance == null) {
                                        insatance = new LocationManager();
                                }
                        }
                }
                return insatance;
        }


        public void startLocation() {
                if (mLocationClient == null) {
                        LogUtil.e("这里新建定位");
                        mLocationClient = new AMapLocationClient(BaseApplication.getInstance());
                        mLocationClient.setLocationOption(getDefaultOption());
                        mLocationClient.setLocationListener(this);
                        mLocationClient.startLocation();
                }
        }


        private LocationManager() {

        }

        private AMapLocationClientOption getDefaultOption() {
                LogUtil.e("获取定位选项");
                AMapLocationClientOption option = new AMapLocationClientOption();
//          定位模式：1 高精度、2仅设备、3仅网络
// 设置高精度模式
                option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//                设置是否优先使用GPS
                option.setGpsFirst(false);
//                连接超时3秒
                option.setHttpTimeOut(3000);
//                设置定位间隔60秒
                option.setInterval(60000);
//                设置是否返回地址，默认返回
                option.setNeedAddress(true);
//                设置是否单次定位
                option.setOnceLocation(false);
                //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
                option.setOnceLocationLatest(false);
//                设置网络请求协议
                AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);
//                设置是否使用传感器,不使用
                option.setSensorEnable(false);
                return option;
        }



        public List<String> getLocationList() {
                return locationList;
        }


        public double getLongitude() {
                return longitude;
        }

        public double getLatitude() {
                return latitude;
        }

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                        if (aMapLocation.getErrorCode() == 0) {
                                LogUtil.e("1获取到位置信息拉");
                                //                获取纬度
                                if (latitude != aMapLocation.getLatitude() || longitude != aMapLocation.getLongitude()) {
                                        latitude = aMapLocation.getLatitude();
                                        //                获取经度
                                        longitude = aMapLocation.getLongitude();
                                        address = aMapLocation.getAddress();
                                        LogUtil.e("address:" + aMapLocation.getAddress());
                                        LogUtil.e("country" + aMapLocation.getCountry());
                                        LogUtil.e("province" + aMapLocation.getProvince());
                                        LogUtil.e("city" + aMapLocation.getCity());
                                        LogUtil.e("district" + aMapLocation.getDistrict());
                                        LogUtil.e("street" + aMapLocation.getStreet());
                                        LogUtil.e("aoiName", aMapLocation.getAoiName());
                                        LogUtil.e("streetNumber", aMapLocation.getStreetNum());
//                                        aMapLocation.getLocationType();//获取当前定位结果来源
//                                        aMapLocation.getLatitude();//获取纬度
//                                        aMapLocation.getLongitude();//获取经度
//                                        aMapLocation.getAccuracy();//获取精度信息
//                                        aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                                        aMapLocation.getCountry();//国家信息
//                                        aMapLocation.getProvince();//省信息
//                                        aMapLocation.getCity();//城市信息
//                                        aMapLocation.getDistrict();//城区信息
//                                        aMapLocation.getStreet();//街道信息
//                                        aMapLocation.getStreetNum();//街道门牌号信息
//                                        aMapLocation.getCityCode();//城市编码
//                                        aMapLocation.getAdCode();//地区编码
//                                        aMapLocation.getAoiName();//获取当前定位点的AOI信息
                                        BaseApplication.getAppComponent().getSharedPreferences().edit().putString(ChatUtil.LOCATION,latitude + "&" + longitude)
                                        .apply();
                                        BaseApplication.getAppComponent().getSharedPreferences().edit().putString(ChatUtil.ADDRESS,address)
                                        .apply();
                                } else {
                                        LogUtil.e("定位相同,不定位");
                                }
                                if (locationList == null) {
                                        locationList=new ArrayList<>();
                                        locationList.add(address);
                                        locationList.add(aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet() + aMapLocation.getAoiName());
                                        locationList.add(aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet());
                                        locationList.add(aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict());
                                        locationList.add(aMapLocation.getProvince() + aMapLocation.getCity());
                                        locationList.add(aMapLocation.getProvince());
                                }
                                if (UserManager.getInstance().getCurrentUser() != null) {
                                        UserManager.getInstance().updateUserInfo("location", longitude + "&" + latitude, new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                        if (e == null) {
                                                                if (UserManager.getInstance().getCurrentUser()!=null) {
                                                                        UserManager.getInstance().getCurrentUser().setLocation(new BmobGeoPoint(longitude,latitude));
                                                                }
                                                        }else {
                                                                LogUtil.e("定位获取的到的经纬度信息更新到服务器上失败");
                                                        }
                                                }
                                        });
                                }
                                //                获取地址
                                List<String> addressList = null;
                                for (LocationChangedListener mListener : mLocationChangedListeners) {
                                        addressList = new ArrayList<>();
                                        addressList.add(address);
                                        addressList.add(aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet() + aMapLocation.getAoiName());
                                        addressList.add(aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet());
                                        addressList.add(aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict());
                                        addressList.add(aMapLocation.getProvince() + aMapLocation.getCity());
                                        addressList.add(aMapLocation.getProvince());
                                        mListener.onLocationChanged(addressList, latitude, longitude);
                                }
                        } else {
                                LogUtil.e("出错消息：" + aMapLocation.getErrorInfo() + "\n" + "错误码:" + aMapLocation.getErrorCode() + "\n" + "错误的细节" +
                                        aMapLocation.getLocationDetail());
                                for (LocationChangedListener mListener : mLocationChangedListeners) {
                                        mListener.onLocationFailed(aMapLocation.getErrorCode(), aMapLocation.getLocationDetail());
                                }
                        }
                }
        }


        private List<LocationChangedListener> mLocationChangedListeners = new ArrayList<>();


        public void registerLocationListener(LocationChangedListener listener) {
                if (!mLocationChangedListeners.contains(listener)) {
                        mLocationChangedListeners.add(listener);
                }
        }


        public void unregisterLocationListener(LocationChangedListener listener) {
                if (mLocationChangedListeners.contains(listener)) {
                        mLocationChangedListeners.remove(listener);
                }
        }


        public void clear() {
                LogUtil.e("这里清除定位");
                if (mLocationClient != null) {
                        mLocationClient.onDestroy();
                        mLocationClient = null;
                }
                if (mLocationChangedListeners.size() > 0) {
                        mLocationChangedListeners.clear();
                }
                longitude = 0;
                latitude = 0;
        }
}
