package com.zhuoxin.treasurehunt.net;

import com.zhuoxin.treasurehunt.treasure.Area;
import com.zhuoxin.treasurehunt.treasure.Treasure;
import com.zhuoxin.treasurehunt.user.User;
import com.zhuoxin.treasurehunt.user.login.LoginResult;
import com.zhuoxin.treasurehunt.user.register.RegisterResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Retrofit的核心接口类，定义了各种网络请求的方法
 * Created by Administrator on 2017/8/29.
 */

public interface RetrofitAPI {

    //POST请求方式进行登录请求
    @POST("/Handler/UserHandler.ashx?action=login")
    Call<LoginResult> login(@Body User user);

    //POST请求方式进行注册请求
    @POST("/Handler/UserHandler.ashx?action=register")
    Call<RegisterResult> register(@Body User user);

    //根据区域获取宝藏
    @POST("/Handler/TreasureHandler.ashx?action=show")
    Call<List<Treasure>> getTreasure(@Body Area area);

}
