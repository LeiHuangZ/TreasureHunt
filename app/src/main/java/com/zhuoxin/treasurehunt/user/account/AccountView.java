package com.zhuoxin.treasurehunt.user.account;

/**
 * Created by Administrator on 2017/9/5.
 */

public interface AccountView {
    void showProgress();

    void hideProgress();

    void showResult(String result);

    void displayAvart();
}
