package com.example.haojiangwang.vibrationsignalacquisionsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TopTenActivity extends AppCompatActivity {
    private float[] outputData;
    private float[] dataCopy;
    private int [] index;
    private float fs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_details);
        outputData = this.getIntent().getFloatArrayExtra("outputDataTopTen");
        fs = this.getIntent().getFloatExtra("fs",0);
        dataCopy = new float[outputData.length/2];
        System.arraycopy(outputData,0,dataCopy,0,dataCopy.length/2);
        index = IndexSort.dataSort(dataCopy);
        TextView tv = (TextView) findViewById(R.id.textView2);
        tv.setText("Top 10  " + "频率/Hz  " + "幅值\n");
        for(int i=0;i<9;i++){
            String s = (i+1) + "    "+index[i] * fs /outputData.length +  "    "+outputData[index[i]] + "\n";
            tv.append(s);
        }
    }
}
