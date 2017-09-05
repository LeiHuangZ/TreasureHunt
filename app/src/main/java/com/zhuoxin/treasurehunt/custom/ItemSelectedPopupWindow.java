package com.zhuoxin.treasurehunt.custom;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.zhuoxin.treasurehunt.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/5.
 */

public class ItemSelectedPopupWindow extends PopupWindow {
    private OnItemClickListener mListener;

    public ItemSelectedPopupWindow(Activity activity, OnItemClickListener listener) {
        super(activity.getLayoutInflater().inflate(R.layout.window_select_icon, null), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        setBackgroundDrawable(ContextCompat.getDrawable(activity, R.color.text_color_white));
        mListener = listener;

        ButterKnife.bind(this, getContentView());
    }

    @OnClick({R.id.btn_gallery, R.id.btn_camera, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_gallery:
                mListener.toGallery();
                break;
            case R.id.btn_camera:
                mListener.toCamera();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }


    public interface OnItemClickListener {
        void toGallery();

        void toCamera();
    }
}
