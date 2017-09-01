package com.zhuoxin.treasurehunt.user.login;

/**
 * 登录功能的View接口
 * Created by Administrator on 2017/8/29.
 */

interface LoginView {
    String getName();//获取用户名
    String getPassword();//获取密码

    void showProgress();//展示进度条
    void hideProgress();//隐藏进度条

    void showResult(String result);//展示登录结果

    void loginFail();//登录失败调用
    void loginSuccess();//登录成功调用
}
