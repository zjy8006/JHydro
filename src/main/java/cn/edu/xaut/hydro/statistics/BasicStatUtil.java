package cn.edu.xaut.hydro.statistics;

/**
 * Created by Samantha on 2017/4/9.
 */
public class BasicStatUtil {

    public static double getRMSE(double[] orig,double[] pred){
        if (orig.length != pred.length) {
            System.err.println("The Length of original sequence and predicted sequence must be identical");
        }
        double sum = 0.0;
        for (int i = 0; i < orig.length; i++) {
            sum+=Math.pow(orig[i]-pred[i],2);
        }
        return Math.sqrt(sum/orig.length);
    }
}
