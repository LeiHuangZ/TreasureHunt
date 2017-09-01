package com.zhuoxin.treasurehunt.user.register;

import com.zhuoxin.treasurehunt.net.RetrofitClient;
import com.zhuoxin.treasurehunt.user.User;
import com.zhuoxin.treasurehunt.user.UserPrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 注册中间人对象，实现注册的逻辑，以及通知view进行UI更新
 * Created by Administrator on 2017/8/29.
 */

class RegisterPresenter {
    private RegisterView mRegisterView;

    RegisterPresenter(RegisterView registerView) {
        mRegisterView = registerView;
    }

    public void register(User user) {
        RetrofitClient.getInstance().getAPI().register(user).enqueue(new Callback<RegisterResult>() {
            @Override
            public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                //隐藏进度条
                mRegisterView.hideProgress();
                if (response.isSuccessful()) {
                    RegisterResult mRegisterResult = response.body();
                    if (mRegisterResult == null){
                        mRegisterView.showResult("未知错误");
                        return;
                    }
                    if (mRegisterResult.getErrcode() == 1) {
                        //存储用户信息
                        UserPrefs.getInstance().setTokenid(mRegisterResult.getTokenid());
                        //调用成功方法
                        mRegisterView.success();
                    } else {
                        //调用失败的方法
                        mRegisterView.fail();
                    }
                }
                //展示请求结果，未知错误
                mRegisterView.showResult(response.body().getErrmsg());
                //失败
                mRegisterView.fail();
            }

            @Override
            public void onFailure(Call<RegisterResult> call, Throwable t) {
                //隐藏进度条
                mRegisterView.hideProgress();
                //调用失败方法
                mRegisterView.fail();
            }
        });
    }
}
