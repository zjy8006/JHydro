package cn.edu.xaut.hydro.util;

import java.util.ArrayList;

/**
 * Created by Samantha on 2017/2/13.
 */
public class TimeSeries {
    public static double[][] timeSerilization(double[] arr){
        double[][] result = new double[arr.length][2];
        for (int i = 0; i < result.length; i++) {
            result[i][0] = i+1;//time interval
            result[i][1] = arr[i];
        }
        return result;
    }

    public static double[][] timeSerilization(ArrayList<Double> arrayList){
        double[][] result = new double[arrayList.size()][2];
        for (int i = 0; i < result.length; i++) {
            result[i][0] = i+1;//time interval
            result[i][1] = arrayList.get(i);
        }
        return result;
    }


}
