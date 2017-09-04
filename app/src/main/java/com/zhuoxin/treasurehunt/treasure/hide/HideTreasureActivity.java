package com.zhuoxin.treasurehunt.treasure.hide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.baidu.mapapi.model.LatLng;
import com.zhuoxin.treasurehunt.R;
import com.zhuoxin.treasurehunt.commons.ActivityUtils;
import com.zhuoxin.treasurehunt.user.UserPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HideTreasureActivity extends AppCompatActivity implements HideTreasureView {

    @BindView(R.id.hide_send)
    ImageView mHideSend;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_description)
    EditText mEtDescription;
    private ProgressDialog mProgressDialog;
    private ActivityUtils mActivityUtils;

    public static void goHideTreasureActivity(Context context, String title, String location, LatLng target) {
        Intent mIntent = new Intent(context, HideTreasureActivity.class);
        mIntent.putExtra("title", title);
        mIntent.putExtra("location", location);
        mIntent.putExtra("latlng", target);
        context.startActivity(mIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide_treasure);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.hide_send)
    public void onViewClicked() {
        LatLng mLatlng = getIntent().getParcelableExtra("latlng");
        HideTreasure mHideTreasure = new HideTreasure();
        mHideTreasure.setAltitude(0.0);
        mHideTreasure.setLatitude(mLatlng.latitude);
        mHideTreasure.setLongitude(mLatlng.longitude);
        mHideTreasure.setDescription(mEtDescription.getText().toString().trim());
        mHideTreasure.setLocation(getIntent().getStringExtra("location"));
        mHideTreasure.setTokenId(UserPrefs.getInstance().getTokenid());
        mHideTreasure.setTitle(getIntent().getStringExtra("title"));
        new HideTreasurePresenter(this).hideTreasure(mHideTreasure);
    }

    //===================================================================================//
    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(this, "埋宝中", "正在填土...");
    }

    @Override
    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showResult(String result) {
        mActivityUtils.showToast(result);
    }

    @Override
    public void success() {
        finish();
    }
}
