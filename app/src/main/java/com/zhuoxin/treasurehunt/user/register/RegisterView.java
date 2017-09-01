package com.zhuoxin.treasurehunt.user.register;

/**
 * 注册视图接口，展示进度条，隐藏进度条，展示注册结果
 * Created by Administrator on 2017/8/29.
 */

public interface RegisterView {
    void showProgress();
    void hideProgress();

    void showResult(String result);

    void fail();
    void success();
}
