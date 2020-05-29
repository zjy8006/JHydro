package cn.edu.xaut.hydro.timeSeries.trendAndSaltationAnalysis;

import cn.edu.xaut.hydro.math.basic.MathUtil;
import cn.edu.xaut.hydro.util.PlotUtil;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.utils.Debug;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * The recognition and test of trend of time series.
 */
public class TrendAnalyse {

	/**
	 * Time series analysis>>Sliding average method
	 * @param arr The time series
	 * @param k The slide range
	 * @return The slided time series of original series.
	 */
    public Map<String, Double> slideAverage(double[][] arr, int k) {

        Map<String, Double> result = new HashMap<String, Double>();
        int length = arr[0].length;
        
        for (int i = (k - 1) / 2; i < (length - (k - 1) / 2); i++) {
        	
            double sum = 0.0;
            
            for (int j = i - (k - 1) / 2; j < (i - (k - 1) / 2) + k; j++) {
                sum = sum + arr[1][j];
            }
            
            DecimalFormat f = new DecimalFormat("0");
            
            result.put(String.valueOf(f.format(arr[0][i])), sum / k);
        }


        return result;
    }


    /**
     * Time series trend analysis-->cumulative target-average distance method (CTADM)
     * <p>Cumulative distance between the value at time step t and the average of the time series.</p>
     * @param timeSeries The original time series.
     * @return The time series of cumulative distance.
     */
    public double[] cumulativeDistance(double[] timeSeries){
        double[] results = new double[timeSeries.length];
        double average = MathUtil.average(timeSeries);
        double sum = 0.0;
        for (int i = 0; i < timeSeries.length; i++) {
            sum+=timeSeries[i]-average;
            results[i]=sum;
        }

        //plot the line of cumulative distance
        double[][] cumulativeDistance = new double[timeSeries.length][2];
        for (int i = 0; i < cumulativeDistance.length; i++) {
            cumulativeDistance[i][0]=i+1;
            cumulativeDistance[i][1]=results[i];
        }
        JavaPlot p = new JavaPlot(PlotUtil.GnuplotPath);
        p.setTitle("The line of cumulative distance","Consolas",16);
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        p.getAxis("x").setLabel("time step(t)","Consolas",16);
        p.getAxis("y").setLabel("cumulative distance","Consolas",16);
        DataSetPlot line = new DataSetPlot(cumulativeDistance);
        p.addPlot(line);
        PlotStyle style_orig = ((AbstractPlot) p.getPlots().get(0)).getPlotStyle();

        style_orig.setLineWidth(2);
        style_orig.setStyle(Style.LINES);
        ((AbstractPlot) p.getPlots().get(0)).setTitle("The line of cumulative distance");
        p.plot();

        //return the cumulative distance between value at time step t and the average of time series.
        return results;
    }




    public static void main(String[] args) {
        TrendAnalyse trendAnalyse = new TrendAnalyse();
        //y=x for k=1:10; y=2*x-6 for k=11:20
        double[] arr = new double[20];
        for (int i = 0; i < arr.length; i++) {
            if (i<10)
                arr[i]= (i+1);
            else
                arr[i]=2*(i+1)-3;
        }

        //Test cumulative distance
        double[] arr1 = trendAnalyse.cumulativeDistance(arr);

    }

}

