package com.zhuoxin.treasurehunt.custom;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * 自定义弹窗，显示错误信息
 * Created by Administrator on 2017/8/25.
 */

public class AlertDialogFragment extends DialogFragment {
    private static String KEY_TITLE = "key_title";//标题
    private static String KEY_MESSAGE = "key_message";//内容

    //通过静态方法方式获取对象，并以setArguments方式传递需要展示的标题和内容信息
    public static AlertDialogFragment getAlertDialogFragment(String title, String message) {
        AlertDialogFragment mAlertDialogFragment = new AlertDialogFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(KEY_TITLE, title);
        mBundle.putString(KEY_MESSAGE, message);
        mAlertDialogFragment.setArguments(mBundle);
        return mAlertDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //获取需要展示的传递的标题和内容信息
        Bundle mArguments = getArguments();
        String title = mArguments.getString(KEY_TITLE);
        String message = mArguments.getString(KEY_MESSAGE);
        return new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }
}
