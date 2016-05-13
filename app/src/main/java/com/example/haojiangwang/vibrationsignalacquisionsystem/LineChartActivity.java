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

public class LineChartActivity extends AppCompatActivity {

//    public static final String DATA_DRAWCHART = "com.example.haojiangwang.vibrationsignalacquisionsystem.linechartactivity";
//    public static final String DATA_DRAWCHART1 = "com.example.haojiangwang.vibrationsignalacquisionsystem.linechartactivity";
    private float [] data1;
    private int fs;

    private TextView pointCor;
    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_line_chart);
        pointCor = (TextView) findViewById(R.id.pointCor);
        fs = this.getIntent().getIntExtra("theValueFS",0);
        data1 = this.getIntent().getFloatArrayExtra("dataValue");
//        fs = this.getIntent().getFloatExtra(DATA_DRAWCHART1,0);
        mChart = (LineChart) findViewById(R.id.chart);

        // 制作7个数据点（沿x坐标轴）
        LineData mLineData = makeLineData(data1.length);
        setChartStyle(mChart, mLineData, Color.WHITE);//背景颜色
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
//                Toast.makeText(LineChartActivity.this,"" + entry.getVal() + "  "+entry.getXIndex(),Toast.LENGTH_LONG).show();
                float xTemp = (entry.getXIndex() / (float)(fs));
                float  valueTempX   =  (float)(Math.round(xTemp*1000))/1000;
                float  valueTempY   =  (float)(Math.round(entry.getVal()*1000))/1000;

                String s = "选择点坐标： " + "(" + valueTempX + "," + valueTempY +")";
                pointCor.setText(s);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        MarkerView mv = new MyMarkerView(this,R.layout.content_marker_view,fs);
        mChart.setMarkerView(mv);

    }
    public void timeAnalys(View view){
        Intent intent = new Intent(LineChartActivity.this, TimeDetails.class);
        Bundle bundle = new Bundle();
        bundle.putFloatArray(TimeDetails.DATA_SOURSE, data1);
        intent.putExtras(bundle);
        //        intent.putExtra(DateList.DATA,data);
        startActivity(intent);
    }
    public void chartCut(View view){
        mChart.saveToPath("chart1","/data");
    }

    // 设置chart显示的样式
    private void setChartStyle(LineChart mLineChart, LineData lineData,
                               int color) {
        // 是否在折线图上添加边框
        mLineChart.setDrawBorders(false);

        mLineChart.setDescription("时间t/s");// 数据描述
        mLineChart.setDescriptionColor(Color.BLUE);
//        mLineChart.setDescriptionPosition(1200,1700);
        mLineChart.setDescriptionTextSize(15);

        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        mLineChart
                .setNoDataTextDescription("如果传给MPAndroidChart的数据为空，那么你将看到这段文字。");

        // 是否绘制背景颜色。绘图区域的背景颜色
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





        //        XAxis xAxis = mLineChart.getXAxis();
        //        xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());




        // 设置背景，图标的背景，大背景
        mLineChart.setBackgroundColor(color);

        // 设置x,y轴的数据
        mLineChart.setData(lineData);

        // 设置比例图标示，就是那个一组y的value的
        Legend mLegend = mLineChart.getLegend();

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
            float xTemp = (i / (float)(fs));
            float  valueTemp   =  (float)(Math.round(xTemp*1000))/1000;
            x.add("t:" + valueTemp);
        }

        // y轴的数据
        ArrayList<Entry> y = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float val = data1[i];
            Entry entry = new Entry(val, i);
            y.add(entry);
        }

        // y轴数据集  坐标 标题
        LineDataSet mLineDataSet = new LineDataSet(y, "时域波形图");

        // 用y轴的集合来设置参数
        // 线宽 原3.0f
        mLineDataSet.setLineWidth(1.0f);

        // 显示的圆形大小  修改过原5.0f
        mLineDataSet.setCircleSize(0.0f);

        // 折线的颜色
        mLineDataSet.setColor(Color.YELLOW);

        // 圆球的颜色
//        mLineDataSet.setCircleColor(Color.GREEN);

        // 设置mLineDataSet.setDrawHighlightIndicators(false)后，
        // Highlight的十字交叉的纵横线将不会显示，
        // 同时，mLineDataSet.setHighLightColor(Color.CYAN)失效。
        mLineDataSet.setDrawHighlightIndicators(true);

        // 按击后，十字交叉线的颜色
        mLineDataSet.setHighlightLineWidth(2.0f);
        mLineDataSet.setHighLightColor(Color.GREEN);
        // 设置这项上显示的数据点的字体大小。
        mLineDataSet.setValueTextSize(10.0f);

        //         mLineDataSet.setDrawCircleHole();

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
