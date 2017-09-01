package com.zhuoxin.treasurehunt.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.zhuoxin.treasurehunt.R;
import com.zhuoxin.treasurehunt.map.MapFragment;
import com.zhuoxin.treasurehunt.treasure.Treasure;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 自定义展示地图宝藏信息的控件
 * Created by Administrator on 2017/9/1.
 */

public class TreasureView extends RelativeLayout {
    @BindView(R.id.tv_treasureTitle)
    TextView mTvTreasureTitle;
    @BindView(R.id.tv_distance)
    TextView mTvDistance;
    @BindView(R.id.tv_treasureLocation)
    TextView mTvTreasureLocation;
    private Unbinder mUnbinder;

    public TreasureView(Context context) {
        super(context);
        initView();
    }

    public TreasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TreasureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.view_treasure, this, true);
        mUnbinder = ButterKnife.bind(mView);
    }

    public void bindView(Treasure treasure){
        String mTitle = treasure.getTitle();
        String mLocation = treasure.getLocation();
        double mLatitude = treasure.getLatitude();
        double mLongitude = treasure.getLongitude();
        LatLng mLatLng = new LatLng(mLatitude, mLongitude);

        double mDistance = DistanceUtil.getDistance(mLatLng, MapFragment.getLocation());
        DecimalFormat mDecimalFormat = new DecimalFormat("#0.00");
        String distance = mDecimalFormat.format(mDistance / 1000) + "km";

        mTvTreasureTitle.setText(mTitle);
        mTvTreasureLocation.setText(mLocation);
        mTvDistance.setText(distance);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mUnbinder.unbind();
    }
}
