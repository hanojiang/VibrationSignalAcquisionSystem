package com.example.haojiangwang.vibrationsignalacquisionsystem;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

/**
 * 隐藏状态栏
 * Created by haojiang wang on 2016/5/13 0013.
 */
public class MyMarkerView extends MarkerView {
    public TextView mContentTv;
    public float mFs ;
    public MyMarkerView(Context context, int layoutResource,float fs) {
        super(context, layoutResource);
        mContentTv = (TextView) findViewById(R.id.tv_content_marker_view);
        mFs = fs;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {


        float xTemp = (e.getXIndex() / (float)(mFs));
        float  valueTempX   =  (float)(Math.round(xTemp*1000))/1000;
        float  valueTempY   =  (float)(Math.round(e.getVal()*1000))/1000;
        mContentTv.setText("(" + valueTempX + ","+ valueTempY + ")");
    }

    @Override
    public int getXOffset(float xpos) {
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        return -getHeight();
    }
}
