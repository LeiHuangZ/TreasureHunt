package com.zhuoxin.treasurehunt.treasure.detail;

import com.zhuoxin.treasurehunt.net.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 宝物详细信息获取的中间人对象，获取宝物详细信息的具体逻辑实现
 * Created by Administrator on 2017/9/1.
 */

class TreasureDetailPresenter {
    private TreasureDetailView mTreasureDetailView;

    TreasureDetailPresenter(TreasureDetailView treasureDetailView) {
        mTreasureDetailView = treasureDetailView;
    }

    void getTreasureDetailResult(TreasureDetail detail) {
        RetrofitClient.getInstance().getAPI().getTreasureDetailResult(detail).enqueue(new Callback<List<TreasureDetailResult>>() {
            @Override
            public void onResponse(Call<List<TreasureDetailResult>> call, Response<List<TreasureDetailResult>> response) {
                if (response.isSuccessful()) {
                    List<TreasureDetailResult> mDetailResultList = response.body();
                    if (mDetailResultList == null) {
                        mTreasureDetailView.showResult("未知错误");
                    }
                    assert mDetailResultList != null;
                    mTreasureDetailView.showDetail(mDetailResultList.get(0));
                } else {
                    mTreasureDetailView.showResult("服务器响应异常");
                }
            }

            @Override
            public void onFailure(Call<List<TreasureDetailResult>> call, Throwable t) {
                mTreasureDetailView.showResult("服务器连接失败");
            }
        });
    }
}
