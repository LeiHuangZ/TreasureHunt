package com.zhuoxin.treasurehunt.map;

import com.zhuoxin.treasurehunt.net.RetrofitClient;
import com.zhuoxin.treasurehunt.treasure.Area;
import com.zhuoxin.treasurehunt.treasure.Treasure;
import com.zhuoxin.treasurehunt.treasure.TreasureRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * map的中间人对象
 * Created by Administrator on 2017/8/31.
 */

class MapFragmentPresenter {
    private MapFragmentView mView;

    MapFragmentPresenter(MapFragmentView view) {
        mView = view;
    }

    void getTreasureByArea(Area area) {
        if (TreasureRepo.getInstance().isCached(area)) {
            mView.showTreasure(TreasureRepo.getInstance().getTreasure());
            return;
        }
        TreasureRepo.getInstance().clear();

        RetrofitClient.getInstance().getAPI().getTreasure(area).enqueue(new Callback<List<Treasure>>() {
            @Override
            public void onResponse(Call<List<Treasure>> call, Response<List<Treasure>> response) {
                if (response.isSuccessful()) {
                    List<Treasure> mTreasureList = response.body();
                    if (mTreasureList == null) {
                        //展示吐司
                        mView.showMessage("未知错误");
                    }
                    //存储宝藏信息
                    TreasureRepo.getInstance().addTreasure(mTreasureList);
                    //展示宝藏信息
                    mView.showTreasure(mTreasureList);
                } else {
                    mView.showMessage("服务器响应异常");
                }
            }

            @Override
            public void onFailure(Call<List<Treasure>> call, Throwable t) {
                mView.showMessage("服务器连接失败");
            }
        });
    }
}
