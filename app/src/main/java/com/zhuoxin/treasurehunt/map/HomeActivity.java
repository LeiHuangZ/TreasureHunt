package com.zhuoxin.treasurehunt.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zhuoxin.treasurehunt.MainActivity;
import com.zhuoxin.treasurehunt.R;
import com.zhuoxin.treasurehunt.commons.ActivityUtils;
import com.zhuoxin.treasurehunt.treasure.list.TreasureListFragment;
import com.zhuoxin.treasurehunt.user.UserPrefs;
import com.zhuoxin.treasurehunt.user.account.AccountActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @BindView(R.id.navigation)
    NavigationView mNavigation;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    private ActivityUtils mActivityUtils;
    private ImageView userIcon;
    private FragmentManager mSupportFragmentManager;
    private TreasureListFragment mTreasureListFragment;
    private MapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(HomeActivity.this);
        userIcon = ((ImageView) mNavigation.getHeaderView(0).findViewById(R.id.iv_usericon));//找到头像控件
        //---------------------------------头像点击事件，跳转入个人信息界面--------------------------------//
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityUtils.startActivity(AccountActivity.class);
            }
        });

        mSupportFragmentManager = getSupportFragmentManager();
        mMapFragment = (MapFragment) mSupportFragmentManager.findFragmentById(R.id.mapFragment);
        mTreasureListFragment = new TreasureListFragment();

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mToggle.syncState();
        mDrawerLayout.addDrawerListener(mToggle);

        mNavigation.setNavigationItemSelectedListener(this);
    }

    @Override//加载头像
    protected void onStart() {
        super.onStart();
        String mPhoto = UserPrefs.getInstance().getPhoto();
        Log.e("mPhoto", "onStart: " + mPhoto);
        if (mPhoto != null) {
            Picasso.with(this).load(mPhoto).into(userIcon);
        }
    }

    //----------------------------------侧滑菜单的点击监听的实现----------------------------------------//
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_hide:
                mMapFragment.changeUIMode(MapFragment.TREASURE_MODE_BURY);
                mDrawerLayout.closeDrawer(Gravity.START);
                break;
            case R.id.menu_my_list:
                mActivityUtils.showToast("我的列表");
                break;
            case R.id.menu_help:
                mActivityUtils.showToast("帮助");
                break;
            case R.id.menu_logout://退出登录
                UserPrefs.getInstance().clearUser();//清除用户数据
                mActivityUtils.startActivity(MainActivity.class);
                finish();
                break;
        }
        return false;
    }

    //==============================================标题栏菜单按钮======================================//
    //在Activity创建的时候调用一次
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //在调用invalidateOptionsMenu()时调用
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem mItem = menu.getItem(0);
        if (mTreasureListFragment != null && mTreasureListFragment.isAdded()) {
            mItem.setIcon(R.drawable.ic_view_list);
        } else {
            mItem.setIcon(R.drawable.ic_map);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    //菜单按钮点击
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_toggle) {
            if (mTreasureListFragment != null && mTreasureListFragment.isAdded()) {
                mSupportFragmentManager.popBackStack();
            } else {
                mSupportFragmentManager.beginTransaction().replace(R.id.fragment_container, mTreasureListFragment)
                        .addToBackStack(null).commit();
            }
            invalidateOptionsMenu();//改变菜单的图片
        }
        return super.onOptionsItemSelected(item);
    }


    //====================================重写返回键，优化体验=======================================//
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)){
            mDrawerLayout.closeDrawer(Gravity.START);
        }else {
            if (mMapFragment.mCurrentMode == MapFragment.TREASURE_MODE_NORMAL){
                super.onBackPressed();
            }else {
                mMapFragment.changeUIMode(MapFragment.TREASURE_MODE_NORMAL);
            }
        }
    }
}
