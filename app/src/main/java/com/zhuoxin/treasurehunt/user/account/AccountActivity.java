package com.zhuoxin.treasurehunt.user.account;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;
import com.zhuoxin.treasurehunt.R;
import com.zhuoxin.treasurehunt.commons.ActivityUtils;
import com.zhuoxin.treasurehunt.custom.ItemSelectedPopupWindow;
import com.zhuoxin.treasurehunt.net.RetrofitClient;
import com.zhuoxin.treasurehunt.user.UserPrefs;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.baidu.mapapi.BMapManager.getContext;

public class AccountActivity extends AppCompatActivity implements AccountView{

    @BindView(R.id.account_toolbar)
    Toolbar mAccountToolbar;
    @BindView(R.id.iv_usericon)
    CircularImageView mIvUsericon;
    private ActivityUtils mActivityUtils;
    private ItemSelectedPopupWindow mItemSelectedPopupWindow;
    private ProgressDialog mProgressDialog;
    private static final int REQUEST_CODE = 100;//权限请求码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        setSupportActionBar(mAccountToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("个人信息");
        }

        //获取应用权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //1、First you need a CropHandler to handle the activity results of cropping photos.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropHelper.handleResult(cropHandler, requestCode, resultCode, data);
    }

    //2、Make sure you implemented these methods:
    private CropHandler cropHandler = new CropHandler() {

        @Override
        public void onPhotoCropped(Uri uri) {
            new AccountPresenter(AccountActivity.this).upload(new File(uri.getPath()));
        }

        @Override
        public void onCropCancel() {
            mActivityUtils.showToast("剪切取消");
        }

        @Override
        public void onCropFailed(String message) {
            mActivityUtils.showToast("剪切失败");
        }

        @Override
        public CropParams getCropParams() {
            return new CropParams();
        }

        @Override
        public Activity getContext() {
            return AccountActivity.this;
        }
    };


    @OnClick(R.id.iv_usericon)
    public void onViewClicked() {
        if (mItemSelectedPopupWindow == null) {
            mItemSelectedPopupWindow = new ItemSelectedPopupWindow(this, new ItemSelectedPopupWindow.OnItemClickListener() {
                @Override
                public void toGallery() {
                    //Step 3 Launch a request to crop photos.
                    // MUST!! Clear Last Cached Image
                    CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
                    Intent intent = CropHelper.buildCropFromGalleryIntent(cropHandler.getCropParams());
                    startActivityForResult(intent, CropHelper.REQUEST_CROP);
                }

                @Override
                public void toCamera() {
                    // MUST!! Clear Last Cached Image
                    CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
                    Intent intent = CropHelper.buildCaptureIntent(cropHandler.getCropParams().uri);
                    startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
                }
            });
        }
        mItemSelectedPopupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }


    @Override
    protected void onDestroy() {
        mItemSelectedPopupWindow = null;
        //Step 4 Clear the cached image if possible.
        if (cropHandler.getCropParams() != null)
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
        mProgressDialog = null;
        super.onDestroy();
    }


    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(this, "上传头像", "努力上传中...");
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
    public void displayAvart() {
        Picasso.with(this).load(RetrofitClient.BASE_URL+UserPrefs.getInstance().getPhoto()).into(mIvUsericon);
    }

    //==========================动态添加权限=============================//
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    mActivityUtils.showToast("权限不足，请先授权！");
                }
        }
    }
}
