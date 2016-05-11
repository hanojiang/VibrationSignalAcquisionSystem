package com.example.haojiangwang.vibrationsignalacquisionsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private RelativeLayout mTlRoot;
    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        HideState.hideState(this);
        //        mTlRoot = (RelativeLayout) findViewById(R.id.tl_root);
        setAnimationMul();

    }

    /**
     * 闪屏页动画设置
     * 包括旋转、缩放、和渐变
     */
    private void setAnimationMul() {
        mIv = (ImageView) findViewById(R.id.horse);
        //设置旋转动画
//        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
//                Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);
//        rotateAnimation.setDuration(2000);
//        rotateAnimation.setFillAfter(true);
        //设置缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setFillAfter(true);
        //设置渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);
        //设置动画集合
        AnimationSet animationSet = new AnimationSet(true);
//        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        mIv.startAnimation(animationSet);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                boolean isFirstShow = PrefUtils.getBoolean( "isFirstShow", false,getApplicationContext());
//                if(isFirstShow){
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                }else {
//                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
//                }
                final Intent intent = new Intent(SplashActivity.this, DataAcquisionProcess.class);
                Timer timer = new Timer();
                TimerTask tast = new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        finish();
                    }
                };
                timer.schedule(tast, 800);
            }

                //结束当前页面activity



            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
