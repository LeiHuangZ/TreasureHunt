package com.zhuoxin.treasurehunt.treasure.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.treasurehunt.R;
import com.zhuoxin.treasurehunt.custom.TreasureView;
import com.zhuoxin.treasurehunt.treasure.Treasure;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TreasureDetailActivity extends AppCompatActivity {

    private static final String TREASURE = "treasure";
    @BindView(R.id.iv_navigation)
    ImageView mIvNavigation;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;
    @BindView(R.id.detail_treasure)
    TreasureView mDetailTreasure;
    @BindView(R.id.tv_detail_description)
    TextView mTvDetailDescription;
    private Treasure mTreasure;

    //暴露方法给外面，用以跳转，目的是为了方便知道需要传入什么数据给Activity
    public static void navigateToDetail(Context context, Treasure treasure) {
        context.startActivity(new Intent(context, TreasureDetailActivity.class).putExtra(TREASURE, treasure));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_detail);
        ButterKnife.bind(this);
        //获取传入的数据
        mTreasure = (Treasure) getIntent().getSerializableExtra(TREASURE);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mTreasure.getTitle());
        }
    }
}
