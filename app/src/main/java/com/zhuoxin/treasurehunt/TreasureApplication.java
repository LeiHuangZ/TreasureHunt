package com.zhuoxin.treasurehunt;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.zhuoxin.treasurehunt.user.UserPrefs;

/**
 * 初试化用户存储的仓库以及初始化百度地图的相关工具类
 * Created by Administrator on 2017/8/29.
 */

public class TreasureApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UserPrefs.init(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
    }
}
