package com.example.haojiangwang.vibrationsignalacquisionsystem;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * 隐藏状态栏
 * Created by haojiang wang on 2016/4/3 0003.
 */
public class MyMediaPlayer {
    private MediaPlayer mPlayer;

    public void stop(){
        if(mPlayer != null){
            mPlayer.release();
            mPlayer = null;
        }
    }
    public void paly(Context context, int rawRes){
        stop();
        mPlayer = MediaPlayer.create(context,rawRes);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stop();
            }
        });
        mPlayer.start();
    }
}
