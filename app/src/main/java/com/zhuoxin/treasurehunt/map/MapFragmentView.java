package com.zhuoxin.treasurehunt.map;

import com.zhuoxin.treasurehunt.treasure.Treasure;

import java.util.List;

/**
 * 地图碎片视图的接口
 * Created by Administrator on 2017/8/31.
 */

interface MapFragmentView {
    void showTreasure(List<Treasure> treasureList);
    void showMessage(String result);
}
