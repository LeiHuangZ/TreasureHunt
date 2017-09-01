package com.zhuoxin.treasurehunt.treasure.detail;

/**
 * 宝物详细信息的View层接口，定义了presenter和Activity之间交互的方法
 * Created by Administrator on 2017/9/1.
 */

interface TreasureDetailView {
    void showDetail(TreasureDetailResult detailResult);

    void showResult(String result);
}
