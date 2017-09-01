package com.zhuoxin.treasurehunt.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.zhuoxin.treasurehunt.R;
import com.zhuoxin.treasurehunt.commons.ActivityUtils;
import com.zhuoxin.treasurehunt.custom.TreasureView;
import com.zhuoxin.treasurehunt.treasure.Area;
import com.zhuoxin.treasurehunt.treasure.Treasure;
import com.zhuoxin.treasurehunt.treasure.TreasureRepo;
import com.zhuoxin.treasurehunt.treasure.detail.TreasureDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 自定义Fragment，显示百度地图
 * Created by Administrator on 2017/8/30.
 */

public class MapFragment extends Fragment implements MapFragmentView {
    @BindView(R.id.center)
    Space mCenter;
    @BindView(R.id.iv_located)
    ImageView mIvLocated;
    @BindView(R.id.btn_HideHere)
    Button mBtnHideHere;
    @BindView(R.id.centerLayout)
    RelativeLayout mCenterLayout;
    @BindView(R.id.iv_scaleUp)
    ImageView mIvScaleUp;
    @BindView(R.id.iv_scaleDown)
    ImageView mIvScaleDown;
    @BindView(R.id.tv_located)
    TextView mTvLocated;
    @BindView(R.id.tv_satellite)
    TextView mTvSatellite;
    @BindView(R.id.tv_compass)
    TextView mTvCompass;
    @BindView(R.id.ll_locationBar)
    LinearLayout mLlLocationBar;
    @BindView(R.id.layout_bottom)
    FrameLayout mLayoutBottom;
    @BindView(R.id.map_frame)
    FrameLayout mMapFrame;
    Unbinder unbinder;
    @BindView(R.id.treasureView)
    TreasureView mTreasureView;
    @BindView(R.id.hide_treasure)
    RelativeLayout mHideTreasure;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private ActivityUtils mActivityUtils;
    private static final int REQUEST_CODE = 100;//权限请求码
    private static LatLng currentLatLng;
    private BitmapDescriptor mBitmapDescriptor;
    private BitmapDescriptor infoBitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this, view);
        mActivityUtils = new ActivityUtils(this);

        //获取应用权限
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
        }

        initLocation();//定位

        initMapView();//地图初始化

        return view;
    }

    public static final int TREASURE_MODE_NORMAL = 0;//正常模式
    public static final int TREASURE_MODE_SELECTED = 1;//marker被选中的状态
    public static final int TREASURE_MODE_BURY = 2;//埋藏宝藏状态

    public void changeUIMode(int mode) {
        switch (mode) {
            case TREASURE_MODE_NORMAL:
                if (mCurrentMarker != null) {//如果前一次被选择的覆盖物不为空
                    mCurrentMarker.setVisible(true);//展示前一次的覆盖物
                }
                mBaiduMap.hideInfoWindow();
                mLayoutBottom.setVisibility(View.GONE);
                mCenterLayout.setVisibility(View.GONE);
                break;
            case TREASURE_MODE_SELECTED:
                if (mCurrentMarker != null) {//如果前一次被选择的覆盖物不为空
                    mCurrentMarker.setVisible(true);//展示前一次的覆盖物
                }
                mLayoutBottom.setVisibility(View.VISIBLE);
                mTreasureView.setVisibility(View.VISIBLE);
                mHideTreasure.setVisibility(View.GONE);
                mCenterLayout.setVisibility(View.GONE);
                break;
            case TREASURE_MODE_BURY:
                if (mCurrentMarker != null) {
                    mCurrentMarker.setVisible(true);
                }
                mLayoutBottom.setVisibility(View.GONE);
                mBaiduMap.hideInfoWindow();
                mCenterLayout.setVisibility(View.VISIBLE);
                mBtnHideHere.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLayoutBottom.setVisibility(View.VISIBLE);
                        mTreasureView.setVisibility(View.INVISIBLE);
                        mHideTreasure.setVisibility(View.VISIBLE);
                    }
                });
                break;
        }
    }

    private void initLocation() {

        //第一步，初始化LocationClient类
        mLocationClient = new LocationClient(getContext().getApplicationContext());

        //第二步，配置定位SDK参数
        LocationClientOption mOption = new LocationClientOption();
        mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mOption.setOpenGps(true);//可选，默认false,设置是否使用gps
//        mOption.setLocationNotify(true);
        mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mOption.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mLocationClient.setLocOption(mOption);

        //第三步，实现BDAbstractLocationListener接口，设置监听
        mLocationClient.registerLocationListener(bdLocationListener);

        //第四步，启动定位
        mLocationClient.start();
    }

    private LatLng mLatLng;
    //-------------------------------------------定位监听的实现-------------------------------------------//
    private BDLocationListener bdLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            double mLongitude = bdLocation.getLongitude();//经度
            double mAltitude = bdLocation.getLatitude();//纬度

            mLatLng = new LatLng(mAltitude, mLongitude);
            String mAddrStr = bdLocation.getAddrStr();//详细地理位置

            Log.e("===================", "经度：" + mLongitude + " 纬度：" + mAltitude + " 地理位置：" + mAddrStr);

            //激活地图图层，是地图可以显示圆点
            mBaiduMap.setMyLocationEnabled(true);
            //设置地图显示的圆点位置
            MyLocationData myLocationData = new MyLocationData.Builder()
                    .accuracy(50f)
                    .longitude(mLongitude)
                    .latitude(mAltitude)
                    .build();
            mBaiduMap.setMyLocationData(myLocationData);
            moveToLocation();
        }
    };

    private void initMapView() {
        //获取宝藏展示的图标
        mBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_dot);
        //获取infoWindow展示的图标
        infoBitmap = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_expanded);
        MapStatus mMapStatus = new MapStatus.Builder()
//                .target(mLatLng)
                .overlook(0f)
                .rotate(0f)
                .zoom(20f)
                .build();
        BaiduMapOptions baiduMapOptions = new BaiduMapOptions()
                .mapStatus(mMapStatus)
                .rotateGesturesEnabled(true)
                .zoomControlsEnabled(false)
                .scaleControlEnabled(false);
        MapView mMapView = new MapView(getContext(), baiduMapOptions);
        mBaiduMap = mMapView.getMap();
        mMapFrame.addView(mMapView, 0);


        //添加地图状态改变监听
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                if (mapStatus.target != currentLatLng) {
                    updateMapView(mapStatus);
                }
                currentLatLng = mapStatus.target;
            }
        });

        //添加地图覆盖物的点击事件
        mBaiduMap.setOnMarkerClickListener(mOnMarkerClickListener);

        //添加地图的点击监听
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                changeUIMode(TREASURE_MODE_NORMAL);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        //添加宝物详情的点击事件
        mTreasureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TreasureDetailActivity.navigateToDetail(getActivity(), TreasureRepo.getInstance().getTreasure(mCurrentMarker.getExtraInfo().getInt("treasure_id")));
            }
        });

    }

    //---------------------------------地图覆盖物点击监听的实现------------------------//
    private Marker mCurrentMarker;
    private BaiduMap.OnMarkerClickListener mOnMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            changeUIMode(TREASURE_MODE_SELECTED);

            mCurrentMarker = marker;
            mCurrentMarker.setVisible(false);//隐藏当前的覆盖物

            InfoWindow mInfoWindow = new InfoWindow(infoBitmap, marker.getPosition(), 0, new InfoWindow.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick() {
                    changeUIMode(TREASURE_MODE_NORMAL);
                }
            });
            mBaiduMap.showInfoWindow(mInfoWindow);
            //展示宝藏信息
            mTreasureView.bindView(TreasureRepo.getInstance().getTreasure(marker.getExtraInfo().getInt("treasure_id")));
            return false;
        }
    };

    private void updateMapView(MapStatus mapStatus) {
        //获取宝藏信息
        LatLng mLatLng = mapStatus.target;
        double mLatitude = mLatLng.latitude;//纬度
        double mLongitude = mLatLng.longitude;//经度
        Area mArea = new Area();
        mArea.setMaxLat(Math.ceil(mLatitude));
        mArea.setMinLat(Math.floor(mLatitude));
        mArea.setMaxLng(Math.ceil(mLongitude));
        mArea.setMinLng(Math.floor(mLongitude));
        new MapFragmentPresenter(this).getTreasureByArea(mArea);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_scaleUp, R.id.iv_scaleDown, R.id.tv_located, R.id.tv_satellite, R.id.tv_compass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_scaleUp:
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                break;
            case R.id.iv_scaleDown:
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                break;
            case R.id.tv_located:
                mLocationClient.requestLocation();
                moveToLocation();
                break;
            case R.id.tv_satellite:
                int mMapType = mBaiduMap.getMapType();
                mMapType = mMapType == BaiduMap.MAP_TYPE_NORMAL ? BaiduMap.MAP_TYPE_SATELLITE : BaiduMap.MAP_TYPE_NORMAL;
                String typeName = mMapType == BaiduMap.MAP_TYPE_NORMAL ? "卫星" : "普通";
                mBaiduMap.setMapType(mMapType);
                mTvSatellite.setText(typeName);
                break;
            case R.id.tv_compass:
                boolean mCompassEnabled = mBaiduMap.getUiSettings().isCompassEnabled();
                mBaiduMap.getUiSettings().setCompassEnabled(mCompassEnabled);
                break;
        }
    }

    //移动到当前位置
    private void moveToLocation() {
        MapStatus mapStatus = new MapStatus.Builder()
                .target(mLatLng)
                .zoom(20)
                .overlook(0)
                .rotate(0)
                .build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationClient.requestLocation();
                } else {
                    mActivityUtils.showToast("权限不足，请先授权！");
                }
        }
    }

    //------------------------------------------------实现视图接口的方法----------------------------------------//
    @Override
    public void showTreasure(List<Treasure> treasureList) {

        Log.e("==================", "" + treasureList.size());
        mBaiduMap.clear();//清除前面的覆盖物
        mLayoutBottom.setVisibility(View.GONE);
        for (Treasure treasure : treasureList
                ) {
            Bundle mBundle = new Bundle();
            mBundle.putInt("treasure_id", treasure.getId());
            MarkerOptions mMarkerOptions = new MarkerOptions();
            mMarkerOptions.anchor(0.5f, 0.5f);
            mMarkerOptions.extraInfo(mBundle);
            mMarkerOptions.icon(mBitmapDescriptor);
            mMarkerOptions.position(new LatLng(treasure.getLatitude(), treasure.getLongitude()));
            mBaiduMap.addOverlay(mMarkerOptions);

        }

    }

    public static LatLng getLocation() {
        return currentLatLng;
    }

    @Override
    public void showMessage(String result) {
        mActivityUtils.showToast(result);
    }
}