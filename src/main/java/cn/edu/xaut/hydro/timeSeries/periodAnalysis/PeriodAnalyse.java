package cn.edu.xaut.hydro.timeSeries.periodAnalysis;

import cn.edu.xaut.hydro.math.basic.MathUtil;
import org.apache.commons.math3.distribution.FDistribution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Samantha on 2017/7/8.
 */
public class PeriodAnalyse {


    public static Map<Integer,String> VarianceAnalysis(ArrayList<Double> longSeries,double alpha){

        Map<Integer,String> result = new HashMap<Integer, String>();

        int n = longSeries.size();//长系列水文资料长度
        double avg = MathUtil.average(longSeries);//长系列水文资料均值
        Map<Integer,Double> period_F = new HashMap<Integer, Double>();
        int groupSize = 0;
        if (n%2==0)
            groupSize=n/2;
        else
            groupSize=(n-1)/2;
        for (int i = 2; i <=groupSize ; i++) {

            List<List<Double>> currentGroup = MathUtil.splitList(longSeries,i);//对应当前分组
            double s1 = 0.0;//组间离差平方和
            for (int j = 0; j <currentGroup.size() ; j++) {
                s1+=currentGroup.get(j).size()*Math.pow(MathUtil.average(currentGroup.get(j))-avg,2);
            }
            double s2 = 0.0;//组内离差平方和
            for (int j = 0; j < currentGroup.size(); j++) {
                double sum_diff_squ = 0.0;
                for (int k = 0; k < currentGroup.get(j).size(); k++) {

                    sum_diff_squ+=Math.pow(currentGroup.get(j).get(k)-MathUtil.average(currentGroup.get(j)),2);
                }
                s2+=sum_diff_squ;
            }

            double f1 = i-1;
            double f2 = longSeries.size()-i;
            double F = (s1/f1)/(s2/f2);

            FDistribution fDistribution = new FDistribution(f1,f2);
            double F_alpha = fDistribution.inverseCumulativeProbability(alpha);
            if (F>=F_alpha)
                result.put(i,"是");
            else
                result.put(i,"否");
            period_F.put(i,F);
        }

        return result;
    }
}
