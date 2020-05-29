package cn.edu.xaut.hydro.math.basic;

import cn.edu.xaut.hydro.util.Container;
import cn.edu.xaut.hydro.util.TwoTuple;
import org.joda.time.Days;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 左岗岗 on 5/20/2016.
 */
public class MathUtil {

    public static TwoTuple<Integer, Double> absMaxIndex(double[] arr) {
        double[] arr1 = new double[arr.length];
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = Math.abs(arr[i]);
        }
        double max = arr1[0];
        int index = 0;
        for (int i = 1; i < arr1.length; i++) {
            if (arr1[i] > max) {
                max = arr1[i];
                index = i;
            }
        }
        return new TwoTuple<Integer, Double>(index, max);
    }

    public static double max(double v1, double v2) {
        double max = v1;
        if (v2 > v1) {
            max = v2;
        }
        return max;
    }

    public static double min(double v1, double v2) {
        double min = v1;
        if (v2 < v1) {
            min = v2;
        }
        return min;
    }

    public static double max(ArrayList<Double> arrayList) {
        double max = arrayList.get(0);
        for (int i = 1; i < arrayList.size(); i++) {
            if (max < arrayList.get(i)) {
                max = arrayList.get(i);
            }
        }
        return max;
    }

    public static double max(double[] arr) {
        double max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    public static double min(double[] arr) {
        double min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (min > arr[i])
                min = arr[i];
        }
        return min;
    }


    public static double min(ArrayList<Double> arrayList) {
        double min = arrayList.get(0);
        for (int i = 1; i < arrayList.size(); i++) {
            if (min > arrayList.get(i)) {
                min = arrayList.get(i);
            }
        }
        return min;
    }


    public static double average(List<Double> arrayList) {
        double sum = 0.0;
        for (int i = 0; i < arrayList.size(); i++) {
            sum += arrayList.get(i);
        }

        double average = sum / arrayList.size();
        return average;
    }

    public static double average(double[] arr) {
        double sum = 0.0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum / arr.length;
    }

    public static double sum(ArrayList<Double> arrayList) {
        double sum = 0.0;
        for (int i = 0; i < arrayList.size(); i++) {
            sum += arrayList.get(i);
        }
        return sum;
    }

    public static double sum(double[] arr) {
        double sum = 0.0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }

    public static <T> List<List<T>> splitList(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();

        int reminder = source.size() % n;//均分n等分后剩余
        int number = source.size() / n;//每一份的长度

        int offset = 0;//设置偏移量为零
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (reminder > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                reminder--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    public static double variance(double[] arr) {
        double ave = average(arr);
        double sum = 0.0;
        for (int i = 0; i < arr.length; i++) {
            sum += Math.pow(arr[i] - ave, 2);
        }
        return sum / arr.length;

    }

    public static double sign(double var) {
        double sign = 0.0;
        if (var > 0.0)
            sign = 1.0;
        if (var < 0.0)
            sign = -1.0;
        return sign;
    }


    public static BigDecimal multiply(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2);
    }


    /**
     *  Create by JianYi Zuo
     * @param CdDays_Vals A HashMap that contain daily information (keys=CD&TM, values=double values).
     * @param regex ["\\&"] A connector.
     * @param sumTo ["Month" or "Year"] Sum the daily information to Monthly or annual.
     * @return A HashMap that contain the summed information (keys=CD&TM, values=double values)
     */
    public static HashMap<String, Double> sumDayTo(HashMap<String, Double> CdDays_Vals, String regex, String sumTo) {
        HashMap<String, Double> CdMonths_Vals = new HashMap<String, Double>();
        Container<String> container = new Container<String>();
        ArrayList<String> keys = container.toArrayList(CdDays_Vals.keySet());
        ArrayList<String> CDs = new ArrayList<String>();//重复的编码
        ArrayList<String> scaleDateS = new ArrayList<String>();//重复的月份或者年份
        ArrayList<String> DAYs = new ArrayList<String>();//重复的天
        ArrayList<Double> VALs = new ArrayList<Double>();
        for (int i = 0; i < keys.size(); i++) {
            String[] arr = keys.get(i).split(regex);
            CDs.add(arr[0]);
            if (sumTo.equals("Month")) {
                scaleDateS.add(arr[1].substring(0, 7));
            } else if (sumTo.equals("Year")) {
                scaleDateS.add(arr[1].substring(0, 4));
            }

            DAYs.add(arr[1]);
            VALs.add(CdDays_Vals.get(keys.get(i)));
        }

        ArrayList<String> scaleDate = container.withoutRepeat(scaleDateS);
        ArrayList<String> CD = container.withoutRepeat(CDs);

        for (int i = 0; i < CD.size(); i++) {
            for (int j = 0; j < scaleDate.size(); j++) {
                double sum = 0.0;
                for (int k = 0; k < DAYs.size(); k++) {
                    if (sumTo.equals("Month")) {
                        if (CDs.get(k).equals(CD.get(i)) && DAYs.get(k).substring(0, 7).equals(scaleDate.get(j))) {
                            sum += VALs.get(k);
                        }
                    } else if (sumTo.equals("Year")) {
                        if (CDs.get(k).equals(CD.get(i)) && DAYs.get(k).substring(0, 4).equals(scaleDate.get(j))) {
                            sum += VALs.get(k);
                        }
                    }
                }
                CdMonths_Vals.put(CD.get(i) + "&" + scaleDate.get(j), sum);
            }
        }

        return CdMonths_Vals;
    }






}
