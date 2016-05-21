package com.example.haojiangwang.vibrationsignalacquisionsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DataAcquisionProcess extends AppCompatActivity implements SensorEventListener {

    @InjectView(R.id.button)
    Button mButton;
    @InjectView(R.id.button3)
    Button mButton3;
    @InjectView(R.id.button4)
    Button mButton4;
    @InjectView(R.id.button5)
    Button mButton5;
    @InjectView(R.id.button6)
    Button mButton6;
    @InjectView(R.id.button7)
    Button mButton7;
    @InjectView(R.id.editText)
    EditText mEditText;
    @InjectView(R.id.editText2)
    EditText mEditText2;
    @InjectView(R.id.textView4)
    TextView mTextView4;
    @InjectView(R.id.button10)
    Button mButton10;

    public int acquisionFrequence;
    private int pointNumber;
    private float[] data;
    private float[] outputDate;
    private float gValue;
    private SensorManager mSensorManager;
    private int i;
    private static final String WAIT_SHOW = "点击开始采样按钮采集数据";
    private MyMediaPlayer mMyMediaPlayer = new MyMediaPlayer();
    private byte[] c = {0x0d, 0x0a};


    String[] strings;
    String path;
    private int j = 0;
    private File file;

    //测试用变量
    private long[] time = new long[1024];


    public int getAcquisionFrequence() {
        return acquisionFrequence;
    }

    public void setAcquisionFrequence() {
        this.acquisionFrequence = Integer.parseInt(mEditText.getText().toString());
    }

    public int getPointNumber() {
        return pointNumber;
    }

    public void setPointNumber() {
        this.pointNumber = Integer.parseInt(mEditText2.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_acquision_process);
        ButterKnife.inject(this);
        HideState.hideState(this);
        setTvNoContent("请设置采样频率和采样点数");

    }

    @OnClick({R.id.button, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button10})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                setTvNoContent("参数设置完成，可以进行数据采集或本地数据处理");
                setAcquisionFrequence();
                setPointNumber();
                //                toastTest(getAcquisionFrequence());
                //                toastTest(getPointNumber());
                data = new float[getPointNumber()];
                outputDate = new float[getPointNumber()];
                break;
            case R.id.button3:
                setTvNoContent("已经开始采集重力加速度，请稍后。。。");
                dataAcquision();
                break;
            case R.id.button4:
                setTvNoContent("已经开始采样，请稍后。。。");
                dataAcquision();
                break;
            case R.id.button5:

                drawChart1();
                break;
            case R.id.button6:
                fftAction();
                setTvNoContent("快速傅里叶变换完成");
                break;
            case R.id.button7:
                drawChart2();
                break;
            case R.id.button10:
                setgValue();
                setTvNoContent("校定成功，可以开始正式采样");
                mTextView4.setText(gValue + "");
                break;
        }
    }

    private void toastTest(int data) {
        Toast.makeText(this, data + "", Toast.LENGTH_SHORT).show();
    }

    private void dataAcquision() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        int fs = 1 / acquisionFrequence *1000000;
        mSensorManager.registerListener(this, accSensor, fs);
        i = 0;
    }

    private void setgValue() {

        TimeAnalyse ta = new TimeAnalyse(data);
        this.gValue = ta.getAbsAve();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (i < data.length) {
            time[i] = System.currentTimeMillis();
            float x = event.values[2];
            data[i] = x;

            i++;
        } else {
            long timeTotle = time[time.length - 1] - time[0];
            System.out.println(timeTotle + "共用时间");
            setTvNoContent("采集完毕");
            for (int i = 0; i < data.length; i++) {
                data[i] = data[i] - gValue;
            }
            mMyMediaPlayer.paly(getApplication(), R.raw.over);
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void setTvNoContent(String string) {
        TextView tv = (TextView) findViewById(R.id.textView6);

        tv.setText(string);
        tv.setTextSize(40);

    }

    private void drawChart1() {
        Intent intent = new Intent(DataAcquisionProcess.this, LineChartActivity.class);
        intent.putExtra("theValueFS",acquisionFrequence);
        intent.putExtra("dataValue",data);
//        Bundle bundle = new Bundle();
//        bundle.putFloatArray(LineChartActivity.DATA_DRAWCHART, data);
////        bundle.put(LineChartActivity.DATA_DRAWCHART, acquisionFrequence);
//        intent.putExtras(bundle);
        //        intent.putExtra(DateList.DATA,data);
        startActivity(intent);
        setTvNoContent(WAIT_SHOW);
    }

    private void fftAction() {
//        FFT fft = new FFT();
        float[] dataTemp = new float[outputDate.length];
        //此处添加dataTemp复制data数组中值，防止在排序后data数组值混乱，导致fft操作后重绘时域波形，发生错误。
        System.arraycopy(data, 0, dataTemp, 0, dataTemp.length);
//        outputDate = fft.i2Sort(dataTemp, (int) (Math.log(dataTemp.length) / Math.log(2)));
        outputDate = FFT.myFFT(dataTemp, (int) (Math.log(dataTemp.length) / Math.log(2)));
    }

    private void drawChart2() {
        Intent intent = new Intent(DataAcquisionProcess.this, LineChartActivity2.class);
        intent.putExtra("fsValue",(float) acquisionFrequence);
        intent.putExtra("outputDataValue",outputDate);
//        Bundle bundle = new Bundle();
//        bundle.putFloatArray(LineChartActivity2.DATA_DRAWCHART2, outputDate);
//        intent.putExtras(bundle);
        //        intent.putExtra(DateList.DATA,data);
        startActivity(intent);
        setTvNoContent(WAIT_SHOW);
    }

    public void btnWriteSD(View view) throws IOException {
        File file1 = new File(Environment.getExternalStorageDirectory().getPath()
                + File.separator + "data" + File.separator);
        if (!file1.exists()) {

            boolean mkdirs = file1.mkdirs();
            //            TextView tv = (TextView) findViewById(R.id.textView);
            //            tv.setText("" + mkdirs);
        }
        File file2 = new File(file1, "data1.txt");

        FileOutputStream fos1 = new FileOutputStream(file2, false);
        //        OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
        for (int i = 0; i < data.length; i++) {
            String temp = data[i] + " ";
            fos1.write(temp.getBytes());
            fos1.write(c);
        }

        fos1.close();

        File file3 = new File(file1, "data2.txt");
        FileOutputStream fos2 = new FileOutputStream(file3, false);
        //        OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
        float[] od = OutputData.outputData;
        for (int i = 0; i < od.length / 2; i++) {
            String temp = od[i] + " ";
            fos2.write(temp.getBytes());
            fos2.write(c);
        }

        fos2.close();
        setTvNoContent("存储数据成功");


    }

    public void browse(View view) {
        file = new File(Environment.getExternalStorageDirectory().getPath()
                + File.separator + "data" + File.separator);
        strings = file.list();

        AlertDialog.Builder builder = new AlertDialog.Builder(DataAcquisionProcess.this);
        builder.setTitle("请选择一个样本文件")
                .setSingleChoiceItems(strings, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        path = strings[which];

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DataAcquisionProcess.this,
                                "你选择了样本文件" + file.toString() + File.separator + path,
                                Toast.LENGTH_SHORT).show();
                        //                        tv.setText(path);
                        readFile(path);

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }

    private void readFile(String filePath) {

        //        File file1 = new File(file.getPath() + File.separator + filePath);
        //第一个参数是目录，通过file.getPath()或file.toString()方法，得到的都是不带分隔符的字符串，
        //在创建文件对象时，可以用File(字符串，目录的路径后要带分隔符)，或者用File(file,文件名)
        File file1 = new File(file, filePath);
        //        tv.append(file1.toString());
        if (file1.exists()) {
            String s = null;
            try {
                BufferedReader br = new BufferedReader(new FileReader(file1));
                while ((s = br.readLine()) != null && j < data.length) {

                    data[j] = Float.parseFloat(s);
                    j++;
                }
                //            setText(data);
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
