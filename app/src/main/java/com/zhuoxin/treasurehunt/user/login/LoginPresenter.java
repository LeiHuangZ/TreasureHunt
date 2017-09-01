package com.zhuoxin.treasurehunt.user.login;

import android.util.Log;

import com.zhuoxin.treasurehunt.net.RetrofitClient;
import com.zhuoxin.treasurehunt.user.User;
import com.zhuoxin.treasurehunt.user.UserPrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 登录逻辑的中间人代表，负责业务逻辑的处理，并通知VIEW进行UI更新
 * Created by Administrator on 2017/8/29.
 */

class LoginPresenter {
    private LoginView mLoginView;

    LoginPresenter(LoginView loginView) {
        mLoginView = loginView;
    }

    public void login() {
        String mName = mLoginView.getName();
        String mPassword = mLoginView.getPassword();

        User mUser = new User();
        mUser.setUserName(mName);
        mUser.setPassword(mPassword);
        RetrofitClient.getInstance().getAPI().login(mUser).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                mLoginView.hideProgress();//隐藏进度条
                Log.e("TAG", "onResponse: " + response.message());
                if (response.isSuccessful()) {//如果相应结果为成功的话
                    LoginResult mLoginResult = response.body();
                    if (mLoginResult == null) {
                        mLoginView.showResult("未知错误");
                        return;
                    }
                    int mErrcode = mLoginResult.getErrcode();
                    if (mErrcode == 1) {// 登录成功
                        //存储用户信息
                        UserPrefs.getInstance().setPhoto(RetrofitClient.BASE_URL+mLoginResult.getHeadpic());
                        UserPrefs.getInstance().setTokenid(mLoginResult.getTokenid());
                        mLoginView.loginSuccess();//通知View登录成功
                    } else {//登录失败
                        mLoginView.loginFail();//通知View登录失败
                    }
                    mLoginView.showResult(mLoginResult.getErrmsg());
                }
                mLoginView.loginFail();
                mLoginView.showResult(response.body().getErrmsg());
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                mLoginView.hideProgress();//隐藏进度条
                mLoginView.showResult("服务器连接失败，请检查网络");//展示结果
                mLoginView.loginFail();//通知View登录失败
            }
        });
    }
}
