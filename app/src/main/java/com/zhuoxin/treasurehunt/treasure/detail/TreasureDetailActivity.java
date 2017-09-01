package com.zhuoxin.treasurehunt.treasure.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.zhuoxin.treasurehunt.R;
import com.zhuoxin.treasurehunt.commons.ActivityUtils;
import com.zhuoxin.treasurehunt.custom.TreasureView;
import com.zhuoxin.treasurehunt.treasure.Treasure;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TreasureDetailActivity extends AppCompatActivity implements TreasureDetailView {

    private static final String TREASURE = "treasure";
    @BindView(R.id.iv_navigation)
    ImageView mIvNavigation;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;
    @BindView(R.id.detail_treasure)
    TreasureView mDetailTreasure;
    @BindView(R.id.tv_detail_description)
    TextView mTvDetailDescription;
    private Treasure mTreasure;
    private ActivityUtils mActivityUtils;

    //暴露方法给外面，用以跳转，目的是为了方便知道需要传入什么数据给Activity
    public static void navigateToDetail(Context context, Treasure treasure) {
        context.startActivity(new Intent(context, TreasureDetailActivity.class).putExtra(TREASURE, treasure));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_detail);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
        //获取传入的数据
        mTreasure = (Treasure) getIntent().getSerializableExtra(TREASURE);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mTreasure.getTitle());
        }
        //添加地图
        initMapView();
    }

    private void initMapView() {
        //展示宝物地图
        LatLng mLatLng = new LatLng(mTreasure.getLatitude(), mTreasure.getLongitude());
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(mLatLng)
                .overlook(0f)
                .rotate(0f)
                .zoom(20f)
                .build();
        BaiduMapOptions baiduMapOptions = new BaiduMapOptions()
                .mapStatus(mMapStatus)
                .rotateGesturesEnabled(true)
                .zoomControlsEnabled(false)
                .scaleControlEnabled(false);
        MapView mMapView = new MapView(this, baiduMapOptions);
        mFrameLayout.addView(mMapView);

        //添加位置标识
        MarkerOptions mMarkerOptions = new MarkerOptions();
        mMarkerOptions.anchor(0.5f, 0.5f);
        mMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.treasure_expanded));
        mMarkerOptions.position(mLatLng);
        mMapView.getMap().addOverlay(mMarkerOptions);

        //展示宝藏的详情
        mDetailTreasure.bindView(mTreasure);

        //获取宝物描述等详细信息
        TreasureDetail mTreasureDetail = new TreasureDetail(mTreasure.getId());
        new TreasureDetailPresenter(this).getTreasureDetailResult(mTreasureDetail);
    }

    //------------------------------------实现自View层接口的方法--------------------------------//
    @Override
    public void showDetail(TreasureDetailResult detailResult) {
        if (detailResult.description != null && !detailResult.description.equals("")) {
            mTvDetailDescription.setText(detailResult.description);
        } else {
            mTvDetailDescription.setText("没有相关描述");
        }
    }

    @Override
    public void showResult(String result) {
        mActivityUtils.showToast(result);
    }
}
