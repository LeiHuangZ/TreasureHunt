package com.zhuoxin.treasurehunt.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 封装Retrofit，调用核心接口类中的各种方法进行登录、注册等网络请求，返回Call对象
 * Created by Administrator on 2017/8/29.
 */

public class RetrofitClient {
    public static final String BASE_URL = "http://admin.syfeicuiedu.com";
    private static RetrofitClient mRetrofitClient;
    private Retrofit mRetrofit;

    private RetrofitClient() {
        //防止接口的不规范性，添加Gson对象
        Gson mGson = new GsonBuilder().setLenient().create();
        //添加日志拦截器
        HttpLoggingInterceptor mHttpLoggingInterceptor = new HttpLoggingInterceptor();
        mHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder().addInterceptor(mHttpLoggingInterceptor).build();
        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .client(mOkHttpClient)
                .baseUrl(BASE_URL)
                .build();
    }

    public static RetrofitClient getInstance() {
        if (mRetrofitClient == null) {
            mRetrofitClient = new RetrofitClient();
        }
        return mRetrofitClient;
    }

    //返回API接口对象
    public RetrofitAPI getAPI() {
        return mRetrofit.create(RetrofitAPI.class);
    }
}
