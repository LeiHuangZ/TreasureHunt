package com.zhuoxin.treasurehunt.treasure.hide;

/**
 * Created by Administrator on 2017/9/4.
 */

public interface HideTreasureView {
    void showProgress();

    void hideProgress();

    void showResult(String result);

    void success();
}
