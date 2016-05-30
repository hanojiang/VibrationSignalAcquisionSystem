package com.example.haojiangwang.vibrationsignalacquisionsystem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

/**
 * 隐藏状态栏
 * Created by haojiang wang on 2016/4/3 0003.
 */
public class LineChartActivity2 extends AppCompatActivity {

//    public static final String DATA_DRAWCHART2 = "com.example.haojiangwang.vibrationsignalacquisionsystem.linechartactivity2";

    private float [] outputData;
    private float [] outputDataProcess;
    private float fs;
    private TextView tv;
    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_line_chart2);
        tv = (TextView) findViewById(R.id.tvPointCor) ;
        fs = this.getIntent().getFloatExtra("fsValue",0);
        outputData = this.getIntent().getFloatArrayExtra("outputDataValue");
        outputDataProcess = new float[outputData.length];
//        outputData = this.getIntent().getFloatArrayExtra(DATA_DRAWCHART2);
        //        outputData = new float[data.length];
        //        FFT fft = new FFT();
        //        outputData =  fft.i2Sort(data,10);
        //
        //        outputData = fft.myFFT(outputData,10);
        mChart = (LineChart) findViewById(R.id.chart);

        // 制作7个数据点（沿x坐标轴）
        LineData mLineData = makeLineData(outputData.length / 2);
        setChartStyle(mChart, mLineData, Color.WHITE);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                //                Toast.makeText(LineChartActivity.this,"" + entry.getVal() + "  "+entry.getXIndex(),Toast.LENGTH_LONG).show();
                float xTemp = (entry.getXIndex() * fs /outputData.length);
                float  valueTempX   = Math.round(xTemp * 1000) /1000;
                float  valueTempY   =  (float)(Math.round(entry.getVal() * 1000))/1000;
                String s = "选择点坐标： " + "(" + valueTempX + "," + valueTempY +")";
                tv.setText(s);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        MarkerView mv = new MyMarkerView2(this,R.layout.content_marker_view,fs,outputData.length);
        mChart.setMarkerView(mv);

        OutputData.outputData = outputDataProcess;

    }
    public void topTenClick(View view){
        Intent intent = new Intent(LineChartActivity2.this, TopTenActivity.class);
        intent.putExtra("outputDataTopTen",outputDataProcess);
        intent.putExtra("fs",fs);
        startActivity(intent);

    }
    public void chartCut2(View view){
        mChart.saveToPath("频谱截图"+ System.currentTimeMillis(),"/data");
    }

    // 设置chart显示的样式
    private void setChartStyle(LineChart mLineChart, LineData lineData,
                               int color) {
        // 是否在折线图上添加边框
        mLineChart.setDrawBorders(false);

        mLineChart.setDescription("频率/Hz");// 数据描述
        mLineChart.setDescriptionColor(Color.BLUE);
        mLineChart.setDescriptionTextSize(15);

        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        mLineChart
                .setNoDataTextDescription("如果传给MPAndroidChart的数据为空，那么你将看到这段文字。");

        // 是否绘制背景颜色。
        // 如果mLineChart.setDrawGridBackground(false)，
        // 那么mLineChart.setGridBackgroundColor(Color.CYAN)将失效;
        mLineChart.setDrawGridBackground(true);
        mLineChart.setGridBackgroundColor(Color.BLACK);

        // 触摸
        mLineChart.setTouchEnabled(true);

        // 拖拽
        mLineChart.setDragEnabled(true);

        // 缩放
        mLineChart.setScaleEnabled(true);

        mLineChart.setPinchZoom(false);
        // 隐藏右边 的坐标轴
        mLineChart.getAxisRight().setEnabled(false);
        // 让x轴在下面
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        // // 隐藏左边坐标轴横网格线
        // mLineChart.getAxisLeft().setDrawGridLines(false);
        // // 隐藏右边坐标轴横网格线
        // mLineChart.getAxisRight().setDrawGridLines(false);
        // // 隐藏X轴竖网格线
        // mLineChart.getXAxis().setDrawGridLines(false);

        // 设置背景
        mLineChart.setBackgroundColor(color);

        // 设置x,y轴的数据
        mLineChart.setData(lineData);

        // 设置比例图标示，就是那个一组y的value的
        Legend mLegend = mLineChart.getLegend();

        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
        mLegend.setForm(Legend.LegendForm.LINE);// 样式
        mLegend.setFormSize(15.0f);// 字体
        mLegend.setTextColor(Color.BLUE);// 颜色

        // 沿x轴动画，时间2000毫秒。
        mLineChart.animateX(2000);
    }

    /**
     * @param count 数据点的数量。
     * @return
     */
    private LineData makeLineData(int count) {
        ArrayList<String> x = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            // x轴显示的数据
            float xTemp = (i* fs /outputData.length);
            float  valueTemp   = Math.round(xTemp * 1000) /1000;
            x.add("f:" + valueTemp);
        }

        // y轴的数据
        ArrayList<Entry> y = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float val = outputData[i] *2/outputData.length;
            outputDataProcess[i] = val;
            Entry entry = new Entry(val, i);
            y.add(entry);
        }

        // y轴数据集
        LineDataSet mLineDataSet = new LineDataSet(y, "频域波形图");

        // 用y轴的集合来设置参数
        // 线宽 原3.0f
        mLineDataSet.setLineWidth(1.5f);

        // 显示的圆形大小  修改过原5。0f
        mLineDataSet.setCircleSize(0.0f);

        // 折线的颜色
        mLineDataSet.setColor(Color.GREEN);

        // 圆球的颜色
//        mLineDataSet.setCircleColor(Color.GREEN);

        // 设置mLineDataSet.setDrawHighlightIndicators(false)后，
        // Highlight的十字交叉的纵横线将不会显示，
        // 同时，mLineDataSet.setHighLightColor(Color.CYAN)失效。
        mLineDataSet.setDrawHighlightIndicators(true);

        // 按击后，十字交叉线的颜色
        mLineDataSet.setHighLightColor(Color.RED);
        mLineDataSet.setHighlightLineWidth(1.0f);

        // 设置这项上显示的数据点的字体大小。
        mLineDataSet.setValueTextSize(10.0f);

        // mLineDataSet.setDrawCircleHole(true);

        // 改变折线样式，用曲线。
        // mLineDataSet.setDrawCubic(true);
        // 默认是直线
        // 曲线的平滑度，值越大越平滑。
        // mLineDataSet.setCubicIntensity(0.2f);

        // 填充曲线下方的区域，红色，半透明。
        // mLineDataSet.setDrawFilled(true);
        // mLineDataSet.setFillAlpha(128);
        // mLineDataSet.setFillColor(Color.RED);

        // 填充折线上数据点、圆球里面包裹的中心空白处的颜色。
        //        mLineDataSet.setCircleColorHole(Color.YELLOW);

        // 设置折线上显示数据的格式。如果不设置，将默认显示float数据格式。
        mLineDataSet.setValueFormatter(new ValueFormatter() {

            //			@Override
            //			public String getFormattedValue(float value) {
            //				int n = (int) value;
            //				String s = "y:" + n;
            //				return s;
            //			}

            @Override
            public String getFormattedValue(float value, Entry entry,
                                            int dataSetIndex, ViewPortHandler viewPortHandler) {
                float  valueTemp   =  (float)(Math.round(value*1000))/1000;


                String s = "y:" + valueTemp;
                return s;
            }
        });





        ArrayList<LineDataSet> mLineDataSets = new ArrayList<LineDataSet>();
        mLineDataSets.add(mLineDataSet);

        LineData mLineData = new LineData(x, mLineDataSets);

        return mLineData;
    }
}
