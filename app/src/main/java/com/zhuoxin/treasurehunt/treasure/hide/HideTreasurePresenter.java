package com.zhuoxin.treasurehunt.treasure.hide;

import android.util.Log;

import com.zhuoxin.treasurehunt.net.RetrofitClient;
import com.zhuoxin.treasurehunt.treasure.TreasureRepo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/9/4.
 */

public class HideTreasurePresenter {
    private HideTreasureView mHideTreasureView;

    public HideTreasurePresenter(HideTreasureView hideTreasureView) {
        mHideTreasureView = hideTreasureView;
    }

    void hideTreasure(HideTreasure hideTreasure) {
        mHideTreasureView.showProgress();
        RetrofitClient.getInstance().getAPI().hideTreasure(hideTreasure).enqueue(new Callback<HideTreasureResult>() {
            @Override
            public void onResponse(Call<HideTreasureResult> call, Response<HideTreasureResult> response) {
                mHideTreasureView.hideProgress();
                if (response.isSuccessful()) {
                    HideTreasureResult mHideTreasureResult = response.body();
                    if (mHideTreasureResult == null) {
                        mHideTreasureView.showResult("未知错误");
                    } else {
                        if (mHideTreasureResult.getCode() == 1) {
                            mHideTreasureView.success();
                            TreasureRepo.getInstance().clear();
                        }
                        mHideTreasureView.showResult(mHideTreasureResult.getMsg());
                    }
                } else {
                    mHideTreasureView.showResult("服务器响应错误");
                }
            }

            @Override
            public void onFailure(Call<HideTreasureResult> call, Throwable t) {
                mHideTreasureView.hideProgress();
                mHideTreasureView.showResult("服务器连接失败");
            }
        });
    }
}
