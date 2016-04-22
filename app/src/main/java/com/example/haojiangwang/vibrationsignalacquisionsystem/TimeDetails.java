package com.example.haojiangwang.vibrationsignalacquisionsystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 隐藏状态栏
 * Created by haojiang wang on 2016/4/3 0003.
 */
public class TimeDetails extends AppCompatActivity {

    public static final String DATA_SOURSE = "com.marswang.whj.acctest2.TimeDetails";
    private float [] dataSourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_time_details);
        dataSourse = this.getIntent().getFloatArrayExtra(DATA_SOURSE);

        TimeAnalyse ta = new TimeAnalyse(dataSourse);
        float ave = ta.getAve();
        float absAve = ta.getAbsAve();
        float ave2Root = ta.getAve2Root();
        float fangGengFu = ta.getFangGengFu();
        float fangCha = ta.getFangCha();
        float ffValue = ta.getFfValue();
        float shapeFactor = ta.getShapeFactor();
        float impluseFactor = ta.getImpluseFactor();
        float cerstFactor = ta.getCerstFactor();
        float clearanceFactor = ta.getClearanceFactor();
        float kurtorisValue = ta.getKurtorisValue();

        TextView tv = (TextView) findViewById(R.id.textView2);
        String string = "均值：" + ave +
                "\n平均幅值" + absAve +
                "\n均方根值" + ave2Root +
                "\n方根幅值" + fangGengFu +
                "\n方差" + fangCha +
                "\n峰峰值" + ffValue +
                "\n波形指标" + shapeFactor +
                "\n脉冲指标" + impluseFactor +
                "\n峰值指标" + cerstFactor +
                "\n裕度指标" + clearanceFactor +
                "\n峭度指标" + kurtorisValue ;
        tv.setText(string);

    }
}
