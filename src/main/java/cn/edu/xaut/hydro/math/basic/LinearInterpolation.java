package cn.edu.xaut.hydro.math.basic;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class LinearInterpolation {

    public static String interpolation(double x,ArrayList<Double> xList,ArrayList<Double> yList){
        String y ="";
        if (xList.size()==0 || yList.size()==0){
            return y;
        }else{
            DecimalFormat df = new DecimalFormat("0.00");
            for (int i = 0; i < xList.size()-1; i++) {
                int j=i+1;
                if (x>=xList.get(i) && x<=xList.get(j)){
                    y = df.format((xList.get(j)-x)/(xList.get(j)-xList.get(i))*yList.get(i)+(x-xList.get(i))/(xList.get(j)-xList.get(i))*yList.get(j));
                }

            }
        }



        return y;
    }

    public static void main(String[] args) {
        ArrayList<Double> xList = new ArrayList<Double>();
        ArrayList<Double> yList = new ArrayList<Double>();
        xList.add(0.0);
        xList.add(1.0);
        xList.add(2.0);
        xList.add(3.0);
        xList.add(4.0);
        xList.add(5.0);
        yList.add(10.042569825);
        yList.add(14.012456155);
        yList.add(18.084562622);
        yList.add(22.015181518);
        yList.add(26.154823005);
        yList.add(30.21515080008);

        System.out.println(interpolation(2.5,xList,yList));
    }
}
