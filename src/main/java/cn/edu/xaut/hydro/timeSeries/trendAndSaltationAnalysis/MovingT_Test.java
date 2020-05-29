package cn.edu.xaut.hydro.timeSeries.trendAndSaltationAnalysis;

import cn.edu.xaut.hydro.math.basic.MathUtil;
import org.apache.commons.math3.distribution.TDistribution;

public class MovingT_Test {
    double[] timeSeries;
    int saltationPoint=0;
    int startStep=0;
    double confidenceLevel = 0.0;

    public MovingT_Test(double[] timeSeries, int saltationPoint,int startStep,double confidenceLevel) {
        this.timeSeries = timeSeries;
        this.saltationPoint = saltationPoint;
        this.startStep=startStep;
        this.confidenceLevel=confidenceLevel;
    }

    public String test(){
        String test = "No test";
        int n1 = saltationPoint-startStep+1;
        int n2 = timeSeries.length-(saltationPoint-startStep+1);
        double[] X1 = new double[n1];
        double[] X2 = new double[n2];
        for (int i = 0; i < X1.length; i++) {
            X1[i]=timeSeries[i];
        }
        for (int i = 0; i < X2.length; i++) {
            X2[i]=timeSeries[i+n1];
        }
        double avr1= MathUtil.average(X1);
        double avr2= MathUtil.average(X2);
        double var1=MathUtil.variance(X1);
        double var2=MathUtil.variance(X2);
        double varP = ((n1-1)*var1+(n2-1)*var2)/(n1+n2-2);
        double t0 = (avr1-avr2)/(Math.sqrt(varP)*Math.pow(1/n1+1/n2,1/2));
        TDistribution tDistribution = new TDistribution(n1+n2-2);
        double t_alpha = tDistribution.inverseCumulativeProbability(confidenceLevel);
        System.out.println("freedom degree:"+(n1+n2-2));
        System.out.println("t0="+t0);
        System.out.println("tα="+t_alpha);
        if (Math.abs(t0)>=t_alpha)
            test="Pass Test";
        else
            test="Fail Test";
        System.out.println("Test Result:"+test);
        return test;
    }

    public static void main(String[] args) {
        double[] yangxian={ 69.51 ,66.77 ,36.86 , 46.83 , 38.45 ,35.91 ,60.48 , 51.26 , 78.26,  55.2,
                30.7  , 45.   ,30.8  , 75.6 ,142.  , 61.5 , 127.  ,  75.3 , 55.2 , 36.1,
                57.   , 66.3  ,90.61 , 89.3 , 34.61, 51.08,  52.69,  32.64, 23.63, 39.61,
                18.31 , 67.75 ,27.72 , 33.79, 36.2 , 25.3 ,  64.61,  31.28, 49.16, 29.47,
                39.84 , 31.85 ,49.93 , 57.08, 84.57, 62.74,  60.75,  49.82};
        MovingT_Test mt = new MovingT_Test(yangxian,1997,1967,0.95);
        mt.test();
    }
}
