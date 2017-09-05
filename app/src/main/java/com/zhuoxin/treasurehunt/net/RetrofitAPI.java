package com.zhuoxin.treasurehunt.net;

import com.zhuoxin.treasurehunt.treasure.Area;
import com.zhuoxin.treasurehunt.treasure.Treasure;
import com.zhuoxin.treasurehunt.treasure.detail.TreasureDetail;
import com.zhuoxin.treasurehunt.treasure.detail.TreasureDetailResult;
import com.zhuoxin.treasurehunt.treasure.hide.HideTreasure;
import com.zhuoxin.treasurehunt.treasure.hide.HideTreasureResult;
import com.zhuoxin.treasurehunt.user.User;
import com.zhuoxin.treasurehunt.user.account.Update;
import com.zhuoxin.treasurehunt.user.account.UpdateResult;
import com.zhuoxin.treasurehunt.user.account.UploadResult;
import com.zhuoxin.treasurehunt.user.login.LoginResult;
import com.zhuoxin.treasurehunt.user.register.RegisterResult;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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

    //获取宝藏详情
    @POST("/Handler/TreasureHandler.ashx?action=tdetails")
    Call<List<TreasureDetailResult>> getTreasureDetailResult(@Body TreasureDetail treasureDetail);

    //宝藏上传
    @POST("/Handler/TreasureHandler.ashx?action=hide")
    Call<HideTreasureResult> hideTreasure(@Body HideTreasure hideTreasure);

    //头像上传
    @Multipart
    @POST("/Handler/UserLoadPicHandler1.ashx")
    Call<UploadResult> upLoad(@Part MultipartBody.Part part);

    //头像跟新
    @POST("/Handler/UserHandler.ashx?action=update")
    Call<UpdateResult> updateIcon(@Body Update update);

}
