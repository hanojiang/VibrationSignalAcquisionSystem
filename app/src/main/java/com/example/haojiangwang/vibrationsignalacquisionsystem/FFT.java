package com.example.haojiangwang.vibrationsignalacquisionsystem;

/**
 * 隐藏状态栏
 * Created by haojiang wang on 2016/4/3 0003.
 */
public class FFT {
    //    private double[] inputData;
    //    private double[] outputData;

    public FFT() {
        //        this.inputData = inputData;
        //        outputData = new double[inputData.length];
    }

    public float[] i2Sort(float[] xConv2, int m) {
        int[] index = new int[xConv2.length]; // index数组用于，倒序索引
        int[] bits = new int[m];
        float[] temp = new float[xConv2.length];

        System.arraycopy(xConv2, 0, temp, 0, xConv2.length);

        for (int i = 0; i < index.length; i++) {
            index[i] = i; // 第i个位置，倒序前的值为i
            for (int j = 0; j < m; j++) {
                bits[j] = index[i] - index[i] / 2 * 2; // 提取index[i]的第j位二进制的值
                index[i] /= 2;
            }
            index[i] = 0; // 清零第i个位置的值
            for (int j = m, power = 1; j > 0; j--) {
                index[i] += bits[j - 1] * power; // 第i个位置，倒序后的位置
                power *= 2;
            }
        }
        //        System.out.println("二进制排序之后的x[n]数组：");
        for (int i = 0; i < xConv2.length; i++) {
            // 倒序实现
            xConv2[i] = temp[index[i]];
            //            System.out.print(String.format("%6.2f", xConv2[i]) + " ");
        }
        //        System.out.println("");
        return xConv2;
    }


    public float[] myFFT(float[] xConv2, int m) {
        float[] xConv3 = i2Sort(xConv2,m);

        int divBy; // divBy等分
        double[] Xr, Xi, Wr, Wi;
        float[] absY;// 分别表示：FFT结果的实部和虚部、旋转因子的实部和虚部
        double[] tempXr, tempXi; // 蝶形结果暂存器
        int n = xConv3.length;
        double pi = Math.PI;
        divBy = 1;
        Xr = new double[n];
        Xi = new double[n];
        tempXr = new double[n];
        tempXi = new double[n];
        Wr = new double[n / 2];
        Wi = new double[n / 2];

        for (int i = 0; i < n; i++) { // 初始化Xr、Xi，之所以这样初始化，是为了方便下面的蝶形结果暂存
            Xr[i] = xConv3[i];
            Xi[i] = 0;
        }

        for (int i = 0; i < m; i++) { // 共需要进行m次蝶形计算
            divBy *= 2;
            for (int k = 0; k < divBy / 2; k++) { // 旋转因子赋值
                Wr[k] = Math.cos(k * 2 * pi / divBy);
                Wi[k] = -Math.sin(k * 2 * pi / divBy);
            }

            for (int j = 0; j < n; j++) { // 蝶形结果暂存
                tempXr[j] = Xr[j];
                tempXi[j] = Xi[j];
            }

            for (int k = 0; k < n / divBy; k++) { // 蝶形运算：每一轮蝶形运算，都有n/2对的蝴蝶参与；n/2分为n/divBy组，每组divBy/2个。
                int wIndex = 0; // 旋转因子下标索引
                for (int j = k * divBy; j < k * divBy + divBy / 2; j++) {
                    double X1 = tempXr[j + divBy / 2] * Wr[wIndex]
                            - tempXi[j + divBy / 2] * Wi[wIndex];
                    double X2 = tempXi[j + divBy / 2] * Wr[wIndex]
                            + tempXr[j + divBy / 2] * Wi[wIndex];
                    Xr[j] = tempXr[j] + X1;
                    Xi[j] = tempXi[j] + X2;
                    Xr[j + divBy / 2] = tempXr[j] - X1; // 蝶形对两成员距离相差divBy/2
                    Xi[j + divBy / 2] = tempXi[j] - X2;
                    wIndex++;
                }
            }
        }
        absY = new float[n];
        //        System.out.println("FFT结果：");
        for (int i = 0; i < n; i++) {
            // FFT结果显示
            absY[i] = (float) Math.sqrt(Math.abs(Xr[i]) * Math.abs(Xr[i]) + Math.abs(Xi[i]) * Math.abs(Xi[i]));
            //            System.out.println(Math.sqrt(Math.abs(Xr[i])*Math.abs(Xr[i]) + Math.abs(Xi[i])*Math.abs(Xi[i])));
            //        System.out.println(String.format("%6.2f", Xr[i])
            //                    + (Xi[i] < 0 ? "   - " : "   + ")
            //                    + String.format("%6.2f", Math.abs(Xi[i])) + " * j");
        }
        return absY;

    }
}

