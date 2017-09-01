package com.zhuoxin.treasurehunt.custom;

import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.zhuoxin.treasurehunt.commons.ActivityUtils;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * 自定义播放视频控件
 * Created by Administrator on 2017/8/25.
 */

public class MainMP4Fragment extends Fragment implements TextureView.SurfaceTextureListener {

    private TextureView mTextureView;//展示视频的控件
    private MediaPlayer mMediaPlayer;//视频播放工具

    /**
     * 播放视频之前准备
     * 1.TextureView准备好，即创建完毕
     * 2.获取视频资源
     * 3.准备MediaPlayer
     * 4.播放视频
     * 5.在界面销毁时，释放准备MediaPlayer资源
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //1.1 获取TextureView对象
        mTextureView = new TextureView(getContext());
        return mTextureView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //1.2 TextureView设置监听
        mTextureView.setSurfaceTextureListener(this);
    }

    //--------------------------------------------TextureView监听的实现----------------------------------------------------------------//
    //1.3 准备完毕（创建完毕）
    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {
        try {
            //2.1 获取视频资源
            AssetFileDescriptor mAssetFileDescriptor = getContext().getAssets().openFd("welcome.mp4");
            //2.2 将视频资源转换为需要的文件类型
            FileDescriptor mFileDescriptor = mAssetFileDescriptor.getFileDescriptor();
            //3.1 获取MediaPlayer
            mMediaPlayer = new MediaPlayer();
            //3.2 为MediaPlayer设置视频资源
            mMediaPlayer.setDataSource(mFileDescriptor, mAssetFileDescriptor.getStartOffset(), mAssetFileDescriptor.getLength());
            //3.3 MediaPlayer异步准备(准备事件较长，需要一步，防止主线程停止5S，出现ANR)
            mMediaPlayer.prepareAsync();
            //3.4 MediaPlayer播放监听
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //3.5 MediaPlayer设置展示控件
                    Surface mSurface = new Surface(surface);
                    mMediaPlayer.setSurface(mSurface);
                    //3.6 MediaPlayer设置循环播放
                    mMediaPlayer.setLooping(true);
                    //4 开始播放
                    mMediaPlayer.start();
                }
            });

        } catch (IOException e) {
            new ActivityUtils(this).showToast("视频播放失败，code："+e.getMessage());
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    //5 释放资源
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
