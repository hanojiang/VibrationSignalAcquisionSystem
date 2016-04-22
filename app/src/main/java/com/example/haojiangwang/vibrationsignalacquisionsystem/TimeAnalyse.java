package com.example.haojiangwang.vibrationsignalacquisionsystem;

/**
 * 隐藏状态栏
 * Created by haojiang wang on 2016/4/2 0002.
 */
public class TimeAnalyse {
    private float[] dataSourse;
    private float ave;
    private float absAve;

    private float max;

    private float min;

    private float ffValue;

    private float ave2Root;
    private float fangCha;

    private float fangGengFu;

    private float shapeFactor;
    private float crestFactor;
    private float impluseFactor;
    private float clearanceFactor;
    private float kurtorisValue;

    private float qiaoDu;

    public float getQiaoDu() {
        float[] data2 = new float[dataSourse.length];
        for (int i = 0; i < data2.length; i++) {

            data2[i] = dataSourse[i] * dataSourse[i] * dataSourse[i] * dataSourse[i];

        }
        qiaoDu = getSum(data2) / data2.length;
        return qiaoDu;
    }

    public float getShapeFactor() {
        shapeFactor = getAve2Root() / getAbsAve();

        return shapeFactor;
    }

    public float getCerstFactor() {
        crestFactor = Math.abs(getMax()) / getAve2Root();
        return crestFactor;
    }

    public float getImpluseFactor() {
        impluseFactor = Math.abs(getMax()) / getAbsAve();
        return impluseFactor;
    }

    public float getClearanceFactor() {
        clearanceFactor = Math.abs(getMax()) / getFangGengFu();
        return clearanceFactor;
    }

    public float getKurtorisValue() {
        float temp = getAve2Root() * getAve2Root() * getAve2Root() * getAve2Root();
        kurtorisValue = getQiaoDu() / temp;
        return kurtorisValue;
    }

    //方差
    public float getFangCha() {
        float[] data2 = new float[dataSourse.length];
        for (int i = 0; i < data2.length; i++) {
            data2[i] = dataSourse[i] - getAve();
            data2[i] = data2[i] * data2[i];
        }
        fangCha = getSum(data2) / (data2.length - 1);
        return fangCha;
    }

    //均方根值
    public float getAve2Root() {
        float[] data2 = new float[dataSourse.length];
        for (int i = 0; i < data2.length; i++) {
            data2[i] = dataSourse[i] * dataSourse[i];
        }
        float sum2Ave = getOtherAve(data2);
        ave2Root = (float) Math.sqrt(sum2Ave);
        return ave2Root;
    }

    public TimeAnalyse(float[] dataSourse) {
        this.dataSourse = dataSourse;
    }


    //均值
    public float getAve() {
        ave = getOtherAve(dataSourse);
        return ave;
    }

    //绝对均值
    public float getAbsAve() {
        float[] data2 = new float[dataSourse.length];
        for (int i = 0; i < data2.length; i++) {
            data2[i] = Math.abs(dataSourse[i]);
        }
        absAve = getOtherAve(data2);
        return absAve;
    }

    private float getOtherAve(float[] otherData) { //求一般均值
        float sum = getSum(otherData);
        float average = sum / otherData.length;
        return average;
    }

    //方根幅值
    public float getFangGengFu() {
        float[] data2 = new float[dataSourse.length];
        for (int i = 0; i < data2.length; i++) {
            data2[i] = (float) Math.sqrt(Math.abs(dataSourse[i]));
        }
        float temp = (getSum(data2) / data2.length);
        fangGengFu = temp * temp;
        return fangGengFu;
    }

    //最大值
    public float getMax() {
        max = dataSourse[0];
        for (float aDataSourse : dataSourse) {
            if (aDataSourse >= max) {
                max = aDataSourse;
            }
        }
        return max;
    }

    //最小值
    public float getMin() {
        min = dataSourse[0];
        for (float aDataSourse : dataSourse) {
            if (aDataSourse <= min) {
                min = aDataSourse;
            }
        }
        return min;
    }

    //  峰峰值
    public float getFfValue() {
        ffValue = getMax() - getMin();
        return Math.abs(ffValue);
    }

    //一般求和
    private float getSum(float data[]) {
        float sum = 0;
        for (float aData :
                data) {
            sum += aData;
        }
        return sum;
    }
}
