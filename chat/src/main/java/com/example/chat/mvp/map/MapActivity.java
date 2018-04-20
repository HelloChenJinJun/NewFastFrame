package com.example.chat.mvp.map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.events.LocationEvent;
import com.example.chat.manager.NewLocationManager;
import com.example.chat.util.LogUtil;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.ConstantUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/30      16:38
 * QQ:             1981367757
 */
public class MapActivity extends SlideBaseActivity implements View.OnClickListener {
        private Button send;
        private MapView display;
        private AMapLocationClient mLocationClient;
        private double latitude = 0;
        private double longitude = 0;
        private String address;
        private AMap mMap;
        private Marker mMarker;
        private String localPath;
        private Bundle savedInstanceState;
        private Button type;
        private List<String> list;


        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                this.savedInstanceState = savedInstanceState;
        }

        @Override
        public void initView() {
                boolean isBrowse=getIntent().getBooleanExtra(Constant.IS_BROWSE,false);
                if (latitude == 0 || longitude == 0) {
                        if (isBrowse) {
                                longitude = Double.valueOf(getIntent().getStringExtra(Constant.LONGITUDE));
                                latitude = Double.valueOf(getIntent().getStringExtra(Constant.LATITUDE));
                                address = getIntent().getStringExtra(Constant.ADDRESS);
                        } else {
                                SharedPreferences sharedPreferences
                                        = BaseApplication.getAppComponent().getSharedPreferences();
                                longitude =Double.parseDouble(sharedPreferences.getString(Constant.LONGITUDE,null));
                                latitude =Double.parseDouble(sharedPreferences.getString(Constant.LATITUDE,null));
                                        address =sharedPreferences.getString(Constant.ADDRESS,null);
                        }
                }




                send = (Button) findViewById(R.id.btn_map_send);
                Button back = (Button) findViewById(R.id.btn_map_back);
                display = (MapView) findViewById(R.id.mv_display);
                type = (Button) findViewById(R.id.btn_map_type);
                if (display != null) {
                        display.onCreate(savedInstanceState);
                }
                send.setOnClickListener(this);
                back.setOnClickListener(this);
                type.setOnClickListener(this);
                if (mMap == null) {
                        mMap = display.getMap();
                }

                mMap.setOnMapLoadedListener(() -> {
                        mMap.setOnMapLoadedListener(null);
                        send.setClickable(true);
                        type.setClickable(true);
                        updateMarkerLocation();
                });

                if (isBrowse){
                    send.setVisibility(View.GONE);
                }
        }




        @Override
        public void initData() {
                NewLocationManager.getInstance()
                        .startLocation();
                RxBusManager.getInstance().registerEvent(LocationEvent.class, locationEvent -> {
                        latitude=locationEvent.getLatitude();
                        longitude=locationEvent.getLongitude();
                        address=locationEvent.getLocation();
                        updateMarkerLocation();
                });
                send.setClickable(false);
                type.setClickable(false);
        }

        private void addMarkerOnMap(String message) {
                if (mMarker == null) {
                        LogUtil.e("这里设置图标选项");
                        MarkerOptions mOptions = new MarkerOptions();
                        mOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        mOptions.snippet(message);
                        mOptions.position(new LatLng(latitude, longitude));
                        mMarker = mMap.addMarker(mOptions);
                }
                mMap.reloadMap();
                startMarkerAnimation();
        }

        /**
         * 开始定位点的动画
         */
        private void startMarkerAnimation() {
                if (mMarker != null) {
                        ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1);
                        animation.setInterpolator(new LinearInterpolator());
                        animation.setDuration(1000);
                        mMarker.setAnimation(animation);
                        mMarker.startAnimation();
                }
        }


        @Override
        protected void onSaveInstanceState(Bundle outState) {
                super.onSaveInstanceState(outState);
                if (display != null) {
                        display.onSaveInstanceState(outState);
                }
        }

        @Override
        protected void onResume() {
                super.onResume();
                display.onResume();

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
        protected int getContentLayout() {
                return R.layout.activity_map;
        }

        @Override
        protected void onPause() {
                super.onPause();

                if (display != null) {
                        display.onPause();
                }
        }

        @Override
        protected void onDestroy() {
                super.onDestroy();
                NewLocationManager.getInstance()
                        .clear();
                if (display != null) {
                        display.onDestroy();
                }

        }


        @Override
        public void onClick(View v) {
                int i = v.getId();
                if (i == R.id.btn_map_send) {
                        mMap.getMapScreenShot(new AMap.OnMapScreenShotListener() {
                                @Override
                                public void onMapScreenShot(Bitmap bitmap) {
                                }

                                @Override
                                public void onMapScreenShot(Bitmap bitmap, int i) {
                                        if (bitmap == null) {
                                                return;
                                        }
                                        try {
                                                localPath = getScreenShotLocalPath();
                                                FileOutputStream outputStream = new FileOutputStream(localPath);
                                                boolean result = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                                                if (result) {
                                                        LogUtil.e("截屏成功");
                                                        sendResult(true);
                                                } else {
                                                        LogUtil.e("截屏失败");
                                                        sendResult(false);
                                                }
                                                outputStream.flush();
                                                outputStream.close();
                                        } catch (IOException e) {
                                                e.printStackTrace();
                                        }
                                }
                        });

                } else if (i == R.id.btn_map_back) {
                        if (mLocationClient != null) {
                                mLocationClient.onDestroy();
                                mLocationClient = null;
                        }
                        if (display != null) {
                                display.onDestroy();
                                display = null;
                        }
                        LogUtil.e("finish");
                        finish();

                } else if (i == R.id.btn_map_type) {
                        if (list == null) {
                                list = new ArrayList<>();
                                list.add("标准模式");
                                list.add("卫星模式");
                                list.add("夜间模式");
                        }
                        showChooseDialog("选择视图类型", list, (parent, view, position, id) -> {
                                dismissBaseDialog();
                                if (position == 0) {
                                        mMap.setMapType(AMap.MAP_TYPE_NORMAL);
                                } else if (position == 1) {
                                        mMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                                } else {
                                        mMap.setMapType(AMap.MAP_TYPE_NIGHT);
                                }
                        });


                }
        }

        private void sendResult(boolean isSuccess) {
                if (isSuccess) {
                        Intent intent = new Intent();
                        LogUtil.e("result ");
                        intent.putExtra(Constant.LATITUDE, latitude + "");
                        intent.putExtra(Constant.LONGITUDE, longitude + "");
                        intent.putExtra(Constant.ADDRESS, address);
                        intent.putExtra(ConstantUtil.PATH, localPath);
                        setResult(RESULT_OK, intent);
                } else {
                        setResult(RESULT_CANCELED);
                }
                if (mLocationClient != null) {
                        mLocationClient.onDestroy();
                }
                display.onDestroy();
                mLocationClient = null;
                display = null;
                finish();
        }

        private String getScreenShotLocalPath() {
                return Environment.getExternalStorageDirectory() + File.separator + "TestChat" + File.separator + TimeUtil.getTime(System.currentTimeMillis()) + ".png";
        }


        private void updateMarkerLocation() {
                LogUtil.e("更新定位拉");

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(latitude, longitude), 18, 0, 0)), 1500, new AMap.CancelableCallback() {

                        @Override
                        public void onFinish() {

                                mMap.clear();
                                addMarkerOnMap(address);
                                mMap.moveCamera(CameraUpdateFactory.changeTilt(60));
                                try {
                                        Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                        e.printStackTrace();
                                }
                                mMap.animateCamera(CameraUpdateFactory.changeBearing(90), 2000, null);
                        }

                        @Override
                        public void onCancel() {

                        }
                });
        }

        @Override
        public void onBackPressed() {
                super.onBackPressed();
                if (mLocationClient != null) {
                        mLocationClient.onDestroy();
                        mLocationClient = null;
                }
                if (display != null) {
                        display.onDestroy();
                        display = null;
                }
        }

        @Override
        public void updateData(Object o) {

        }

        public static void start(Activity activity,boolean isBrowse,String longitude,String latitude,
                                 String address,int requestCode) {
                Intent intent=new Intent(activity,MapActivity.class);
                intent.putExtra(Constant.IS_BROWSE,isBrowse);
                if (isBrowse){
                        intent.putExtra(Constant.LONGITUDE,longitude);
                        intent.putExtra(Constant.LATITUDE,latitude);
                        intent.putExtra(Constant.ADDRESS,address);
                        activity.startActivity(intent);
                }else {
                        activity.startActivityForResult(intent,requestCode);
                }
        }
}
