package com.zhuoxin.treasurehunt.treasure.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zhuoxin.treasurehunt.custom.TreasureView;
import com.zhuoxin.treasurehunt.treasure.Treasure;
import com.zhuoxin.treasurehunt.treasure.TreasureRepo;
import com.zhuoxin.treasurehunt.treasure.detail.TreasureDetailActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/9/5.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Treasure> mList;
    private Context mContext;

    public MyAdapter() {
        mList = TreasureRepo.getInstance().getTreasure();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyViewHolder(new TreasureView(mContext));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Treasure mTreasure = mList.get(position);
        holder.mTreasureView.bindView(mTreasure);
        holder.mTreasureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TreasureDetailActivity.navigateToDetail(mContext, mTreasure);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TreasureView mTreasureView;

        MyViewHolder(TreasureView view) {
            super(view);
            mTreasureView = view;
        }
    }
}
