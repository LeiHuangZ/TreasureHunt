package com.zhuoxin.treasurehunt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhuoxin.treasurehunt.commons.ActivityUtils;
import com.zhuoxin.treasurehunt.map.HomeActivity;
import com.zhuoxin.treasurehunt.user.UserPrefs;
import com.zhuoxin.treasurehunt.user.login.LoginActivity;
import com.zhuoxin.treasurehunt.user.register.RegisterActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_MAIN = "main_action";
    private ActivityUtils mActivityUtils;

    //--------------------------------------------------广播接收者，接受关闭当前界面的广播--------------------------------//
    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
            MainActivity.this.startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);//获取Activity工具类对象

        //判断是否已经登录
        if (UserPrefs.getInstance().getTokenid() != -1) {
            mActivityUtils.startActivity(HomeActivity.class);
            finish();
        }

        //注册广播接受者，接受到发送的广播后，销毁当前界面
        IntentFilter mFilter = new IntentFilter(ACTION_MAIN);
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(mReceiver, mFilter);

    }

    @OnClick({R.id.btn_Register, R.id.btn_Login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_Register:
                mActivityUtils.startActivity(RegisterActivity.class);
                break;
            case R.id.btn_Login:
                mActivityUtils.startActivity(LoginActivity.class);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(mReceiver);
    }
}
