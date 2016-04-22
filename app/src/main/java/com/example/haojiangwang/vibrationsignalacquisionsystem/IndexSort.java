package com.example.haojiangwang.vibrationsignalacquisionsystem;

/**
 * 隐藏状态栏
 * Created by haojiang wang on 2016/4/7 0007.
 */
public class IndexSort {

    public static int[] dataSort(float[] data) {
        float[] tempData = new float[data.length];
        int[] indexData = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            indexData[i] = i;
        }


        System.arraycopy(data, 0, tempData, 0, data.length);
        for (int i = 0; i < tempData.length - 1; i++) {
            float currentMax = tempData[i];
            int index = i;
            for (int j = i + 1; j < tempData.length; j++) {
                if (currentMax < tempData[j]) {
                    currentMax = tempData[j];
                    index = j;
                }
            }
            if (index != i) {
                tempData[index] = tempData[i];
                tempData[i] = currentMax;
                int temp = indexData[index];
                indexData[index] = indexData[i];
                indexData[i] = temp;
            }
        }


        return indexData;
    }

}
