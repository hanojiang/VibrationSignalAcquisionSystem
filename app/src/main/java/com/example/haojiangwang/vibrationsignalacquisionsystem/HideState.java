package com.example.haojiangwang.vibrationsignalacquisionsystem;

import android.app.Activity;
import android.view.WindowManager;

/**隐藏状态栏
 * Created by haojiang wang on 2016/4/2 0002.
 */
public class HideState {
    public static void hideState(Activity activity){
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
    }
}
