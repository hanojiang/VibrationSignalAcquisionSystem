package com.example.haojiangwang.vibrationsignalacquisionsystem;

import android.content.Context;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

/**
 * 隐藏状态栏
 * Created by haojiang wang on 2016/5/13 0013.
 */
public class MyMarkerView2 extends MyMarkerView {

    public int mPointNumber;
    public MyMarkerView2(Context context, int layoutResource, float fs, int pointNumber) {
        super(context, layoutResource, fs);
        mPointNumber = pointNumber;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        float xTemp = (e.getXIndex() * mFs /mPointNumber);
        float  valueTempX   = Math.round(xTemp * 1000) /1000;
        float  valueTempY   =  (float)(Math.round(e.getVal() * 1000))/1000;
        mContentTv.setText("(" + valueTempX + ","+ valueTempY + ")");
    }
}
