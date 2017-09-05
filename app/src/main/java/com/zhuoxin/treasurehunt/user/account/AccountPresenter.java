package com.zhuoxin.treasurehunt.user.account;

import com.zhuoxin.treasurehunt.net.RetrofitClient;
import com.zhuoxin.treasurehunt.user.UserPrefs;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/9/5.
 */

public class AccountPresenter {
    private AccountView mAccountView;

    public AccountPresenter(AccountView accountView) {
        mAccountView = accountView;
    }

    public void upload(File file) {
        RequestBody mRequestBody = RequestBody.create(null, file);
        MultipartBody.Part mPart = MultipartBody.Part.createFormData("上传的头像", "photo.jpg", mRequestBody);
        mAccountView.showProgress();
        RetrofitClient.getInstance().getAPI().upLoad(mPart).enqueue(new Callback<UploadResult>() {
            @Override
            public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {
                mAccountView.hideProgress();
                if (response.isSuccessful()) {
                    UploadResult mUploadResult = response.body();
                    if (mUploadResult == null) {
                        mAccountView.showResult("未知错误");
                        return;
                    }
                    if (mUploadResult.getCount() != 1) {
                        mAccountView.showResult(mUploadResult.getMsg());
                        return;
                    }
                    UserPrefs.getInstance().setPhoto(mUploadResult.getUrl());
                    update(mUploadResult.getUrl().substring(mUploadResult.getUrl().lastIndexOf("/")));
                }
            }

            @Override
            public void onFailure(Call<UploadResult> call, Throwable t) {
                mAccountView.hideProgress();
                mAccountView.showResult("头像上传失败");
            }
        });
    }

    private void update(String url) {
        Update mUpdate = new Update(UserPrefs.getInstance().getTokenid(), url);
        RetrofitClient.getInstance().getAPI().updateIcon(mUpdate).enqueue(new Callback<UpdateResult>() {
            @Override
            public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                if (response.isSuccessful()) {
                    UpdateResult mUpdateResult = response.body();
                    if (mUpdateResult == null) {
                        mAccountView.showResult("未知错误");
                        return;
                    }
                    if (mUpdateResult.getCode() != 1) {
                        mAccountView.showResult(mUpdateResult.getMsg());
                        return;
                    }
                    mAccountView.displayAvart();
                }
            }

            @Override
            public void onFailure(Call<UpdateResult> call, Throwable t) {
                mAccountView.showResult("头像更新失败");
            }
        });
    }
}
