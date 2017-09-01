package com.zhuoxin.treasurehunt.user.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhuoxin.treasurehunt.MainActivity;
import com.zhuoxin.treasurehunt.R;
import com.zhuoxin.treasurehunt.commons.ActivityUtils;
import com.zhuoxin.treasurehunt.commons.RegexUtils;
import com.zhuoxin.treasurehunt.custom.AlertDialogFragment;
import com.zhuoxin.treasurehunt.map.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_Username)
    EditText mEtUsername;
    @BindView(R.id.et_Password)
    EditText mEtPassword;
    @BindView(R.id.btn_Login)
    Button mBtnLogin;
    private ActivityUtils mActivityUtils;
    private String mUserName;
    private String mPassword;
    private ProgressDialog mLoginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //获取Activity工具类对象
        mActivityUtils = new ActivityUtils(this);

        //设置标题栏
        setSupportActionBar(mToolbar);
        //展示返回箭头
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //为编辑框设置文本改变监听
        mEtUsername.addTextChangedListener(mTextWatcher);
        mEtPassword.addTextChangedListener(mTextWatcher);
    }

    //-------------------------------内部类的方式实现编辑框的文本监听----------------------------------//
    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mUserName = mEtUsername.getText().toString();
            mPassword = mEtPassword.getText().toString();
            //用户名和密码中的文本内容不为空，让button变为可点击状态
            boolean enabled = !TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mPassword);
            mBtnLogin.setEnabled(enabled);
        }
    };

    @OnClick({R.id.btn_Login})
    public void onViewClicked(View view) {
        if (RegexUtils.verifyUsername(mUserName) != RegexUtils.VERIFY_SUCCESS) {
            AlertDialogFragment.getAlertDialogFragment(getString(R.string.username_error), getString(R.string.username_rules)).show(getSupportFragmentManager(), "useName");
            return;
        }
        if (RegexUtils.verifyUsername(mPassword) != RegexUtils.VERIFY_SUCCESS) {
            AlertDialogFragment.getAlertDialogFragment(getString(R.string.password_error), getString(R.string.password_rules)).show(getSupportFragmentManager(), "useName");
            return;
        }
        showProgress();
        //调用Presenter的登录方法
        new LoginPresenter(this).login();
    }

    //重写标题栏返回按键
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //--------------------------------------------------实现View接口的方法----------------------------------------------//
    @Override
    public String getName() {
        return mUserName;
    }

    @Override
    public String getPassword() {
        return mPassword;
    }

    @Override
    public void showProgress() {
        mLoginProgressBar = ProgressDialog.show(this, "登录", "玩命儿登录中......");
    }

    @Override
    public void hideProgress() {
        if (mLoginProgressBar != null) mLoginProgressBar.dismiss();
    }

    @Override
    public void showResult(String result) {
        mActivityUtils.showToast(result);
    }

    @Override
    public void loginFail() {
        mEtPassword.setText("");
    }

    @Override
    public void loginSuccess() {
        mActivityUtils.startActivity(HomeActivity.class);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MainActivity.ACTION_MAIN));//发送一个本地广播
        finish();
    }
}
