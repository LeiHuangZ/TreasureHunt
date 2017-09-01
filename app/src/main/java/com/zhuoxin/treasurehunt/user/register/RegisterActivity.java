package com.zhuoxin.treasurehunt.user.register;

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
import android.widget.Button;
import android.widget.EditText;

import com.zhuoxin.treasurehunt.MainActivity;
import com.zhuoxin.treasurehunt.R;
import com.zhuoxin.treasurehunt.commons.ActivityUtils;
import com.zhuoxin.treasurehunt.commons.RegexUtils;
import com.zhuoxin.treasurehunt.custom.AlertDialogFragment;
import com.zhuoxin.treasurehunt.map.HomeActivity;
import com.zhuoxin.treasurehunt.user.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements RegisterView{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_Username)
    EditText mEtUsername;
    @BindView(R.id.et_Password)
    EditText mEtPassword;
    @BindView(R.id.et_Confirm)
    EditText mEtConfirm;
    @BindView(R.id.btn_Register)
    Button mBtnRegister;
    private ActivityUtils mActivityUtils;
    private String mUserName;
    private String mPassword;
    private String mConfirm;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);//获取Activity工具类对象

        //设置标题栏
        setSupportActionBar(mToolbar);
        //标题栏展示返回箭头
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //设置文本改变监听
        mEtUsername.addTextChangedListener(textWatcher);
        mEtPassword.addTextChangedListener(textWatcher);
        mEtConfirm.addTextChangedListener(textWatcher);
    }

    //------------------------------------编辑框文本改变监听的实现--------------------------------------//
    TextWatcher textWatcher = new TextWatcher() {
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
            mConfirm = mEtConfirm.getText().toString();
            boolean clickable = !TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mPassword) && !TextUtils.isEmpty(mConfirm);
            mBtnRegister.setEnabled(clickable);
        }
    };

    @OnClick(R.id.btn_Register)
    public void onViewClicked() {
        //用户名和密码以及确认密码的正则检查
        if (RegexUtils.verifyUsername(mUserName) != RegexUtils.VERIFY_SUCCESS) {
            AlertDialogFragment.getAlertDialogFragment(getString(R.string.username_error), getString(R.string.username_rules))
                    .show(getSupportFragmentManager(), "");
            return;
        }
        if (RegexUtils.verifyUsername(mPassword) != RegexUtils.VERIFY_SUCCESS) {
            AlertDialogFragment.getAlertDialogFragment(getString(R.string.password_error), getString(R.string.password_rules))
                    .show(getSupportFragmentManager(), "");
            return;
        }
        if (!TextUtils.equals(mPassword, mConfirm)) {
            AlertDialogFragment.getAlertDialogFragment(getString(R.string.please_confirm_password), "两次输入的密码不一致")
                    .show(getSupportFragmentManager(), "");
            return;
        }

        User mUser = new User();
        mUser.setUserName(mUserName);
        mUser.setPassword(mPassword);
        new RegisterPresenter(this).register(mUser);
    }

    //重写标题栏返回键
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //-----------------------------------------------实现的接口的方法---------------------------------------//
    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(this, "注册", "拼命注册中...");
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showResult(String result) {
        mActivityUtils.showToast(result);
    }

    @Override
    public void fail() {
        mEtPassword.setText("");
        mEtConfirm.setText("");
    }

    @Override
    public void success() {
        mActivityUtils.startActivity(HomeActivity.class);
        finish();
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MainActivity.ACTION_MAIN));//发送本地广播，用于结束开始界面
    }
}
