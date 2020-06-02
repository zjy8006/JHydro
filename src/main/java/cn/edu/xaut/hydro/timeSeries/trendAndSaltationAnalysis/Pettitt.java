package cn.edu.xaut.hydro.timeSeries.trendAndSaltationAnalysis;

import cn.edu.xaut.hydro.math.basic.MathUtil;
import cn.edu.xaut.hydro.util.Container;
import cn.edu.xaut.hydro.util.PlotUtil;
import cn.edu.xaut.hydro.util.TwoTuple;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.utils.Debug;

/**
 * Pettitt test for saltation (change point of time series)
 */
public class Pettitt {
    private double [] timeSeries;
    private int startStep=0;
    private double saltationPoint = 0.0;
    private double saltationValue = 0.0;
    double P = 0.0;
    private String saltationTest = "No Test";



    public Pettitt(double[] timeSeries, int startStep) {
        this.timeSeries = timeSeries;
        this.startStep = startStep;
    }

    public double[] calculateUt(double[] arr){
        int n = arr.length;
        double[] Ut = new double[n-1];
        for (int i = 0; i < n - 1; i++) {
            double s = 0.0;
            for (int j = i+1; j <n ; j++) {
                s+= MathUtil.sign(arr[i]-arr[j]);
            }
            Ut[i]=s;
        }
        return Ut;
    }

    public TwoTuple<Integer,Double> getSaltation() {
        double[] Ut = calculateUt(timeSeries);
        TwoTuple<Integer, Double> indexMax = MathUtil.absMaxIndex(Ut);
        return indexMax;
    }

    public double getSaltationPoint() {
        TwoTuple<Integer,Double> indexMax = getSaltation();
        saltationPoint=indexMax.first+startStep;
        System.out.println("saltationPoint:"+saltationPoint);
        return saltationPoint;
    }

    public double getSaltationValue() {
        TwoTuple<Integer,Double> indexMax = getSaltation();
        saltationValue= indexMax.second;
        System.out.println("saltationValue:"+saltationValue);
        return saltationValue;
    }

    public double getP() {
        TwoTuple<Integer,Double> indexMax = getSaltation();
        double max = indexMax.second;
        P=2*Math.exp(-6*Math.pow(max,2)/(Math.pow(timeSeries.length,3)+Math.pow(timeSeries.length,2)));
        return P;
    }

    public String getSaltationTest() {
        getP();
        if (P<=0.5)
            saltationTest="Pass Test";
        else
            saltationTest="Fail Test";
        System.out.println("P value:"+P+"; Test Result:"+saltationTest);
        return saltationTest;
    }

    public void plotSaltation(){
        double[] Ut = calculateUt(timeSeries);
        double[][] Utt = Container.indexArray(Ut,startStep);
        double[][] PP = Container.indexAndListCommon(getP(),timeSeries.length,startStep);
        JavaPlot p = new JavaPlot();
//        p.setTitle("The line of Time Series","Consolas",16);
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        p.getAxis("x").setLabel("time step(t)","Consolas",16);
        p.getAxis("y").setLabel("statistic of Ut","Consolas",16);
        DataSetPlot UtLine = new DataSetPlot(Utt);
        DataSetPlot PPLine = new DataSetPlot(PP);


        p.addPlot(UtLine);
        p.addPlot(PPLine);
        PlotStyle style_timeSeries = ((AbstractPlot) p.getPlots().get(0)).getPlotStyle();
        PlotStyle style_inverseTimeSeries = ((AbstractPlot) p.getPlots().get(1)).getPlotStyle();


        style_timeSeries.setLineWidth(2);
        style_timeSeries.setStyle(Style.LINESPOINTS);
        style_inverseTimeSeries.setLineWidth(2);
        style_inverseTimeSeries.setStyle(Style.LINES);
        ((AbstractPlot) p.getPlots().get(0)).setTitle("Ut");
        ((AbstractPlot) p.getPlots().get(1)).setTitle("P");
        p.plot();
    }


    public static void main(String[] args) {
        double[] yangxian={ 69.51 ,66.77 ,36.86 , 46.83 , 38.45 ,35.91 ,60.48 , 51.26 , 78.26,  55.2,
                30.7  , 45.   ,30.8  , 75.6 ,142.  , 61.5 , 127.  ,  75.3 , 55.2 , 36.1,
                57.   , 66.3  ,90.61 , 89.3 , 34.61, 51.08,  52.69,  32.64, 23.63, 39.61,
                18.31 , 67.75 ,27.72 , 33.79, 36.2 , 25.3 ,  64.61,  31.28, 49.16, 29.47,
                39.84 , 31.85 ,49.93 , 57.08, 84.57, 62.74,  60.75,  49.82};

        Pettitt pt = new Pettitt(yangxian,1967);
        pt.getSaltationPoint();
        pt.plotSaltation();
        pt.getSaltationTest();
    }
}
