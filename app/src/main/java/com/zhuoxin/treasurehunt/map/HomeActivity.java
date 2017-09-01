package com.zhuoxin.treasurehunt.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zhuoxin.treasurehunt.MainActivity;
import com.zhuoxin.treasurehunt.R;
import com.zhuoxin.treasurehunt.commons.ActivityUtils;
import com.zhuoxin.treasurehunt.user.UserPrefs;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
        userIcon = ((ImageView) mNavigation.getHeaderView(0).findViewById(R.id.iv_usericon));//找到头像控件

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
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
        Log.e("mPhoto", "onStart: "+mPhoto );
        if (mPhoto != null) {
            Picasso.with(this).load(mPhoto).into(userIcon);
        }
    }

    //----------------------------------侧滑菜单的点击监听的实现----------------------------------------//
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_hide:
                ((MapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment)).changeUIMode(MapFragment.TREASURE_MODE_BURY);
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
                break;
        }
        return false;
    }
}
