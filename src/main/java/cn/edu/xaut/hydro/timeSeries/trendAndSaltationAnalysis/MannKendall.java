package cn.edu.xaut.hydro.timeSeries.trendAndSaltationAnalysis;

import cn.edu.xaut.hydro.math.basic.MathUtil;
import cn.edu.xaut.hydro.util.Container;
import cn.edu.xaut.hydro.util.PlotUtil;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.utils.Debug;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Mann-kendall method for trend and saltation analysis.
 * 参考文献： “近57年来和丰县气温和降水量的趋势性及突变特征”
 */
public class MannKendall {

    private double [] timeSeries;//The original time series
    private double [] inverseTimeSeries;
    private int startTimeStep;
    private double confidenceLevel;//The confidence level for MK-Trend test
    private double S = 0.0;//The Mann-kendall statistic
    private double Z = 0.0;//The normal(gaussian) statistic computed by Mann-Kendall statistic
    private double Z_ALPHA = 0.0;//The standard normal statistic with confidence level ALPHA
    private String trend="未检验";//The trend of original time series recognized by Mann-Kendall
    private double[] UFk;
    private double[] UBk;
    HashMap<Double,Double> saltation = new HashMap<Double, Double>();
    HashMap<Double,String> saltationTest = new HashMap<Double, String>();


    /**
     * Instantiation of Mann-Kendall
     * @param timeSeries The time series.
     * @param confidenceLevel The confidence level for MK-Trend test
     */
    public MannKendall(double[] timeSeries,double confidenceLevel,int startTimeStep) {
        this.timeSeries = timeSeries;
        this.confidenceLevel=confidenceLevel;
        this.startTimeStep=startTimeStep;
        inverseTimeSeries=new double[timeSeries.length];
        UFk=new double[timeSeries.length];
        UBk=new double[timeSeries.length];
        for (int i = 0; i < inverseTimeSeries.length; i++) {
            inverseTimeSeries[i]=timeSeries[timeSeries.length-1-i];
        }

    }

    /**
     * Calculate the Mann-kendall statistic S.
     * @return Mann-Kendall variable
     */
    public double getS() {
        S=calculateSk(timeSeries)[timeSeries.length-1];
        return S;
    }

    /**
     * Calculate the Mann-kendall statistic of each time step k.
     * @return An array of Mann-Kendall statistic.
     */
    public double[] calculateSk(double[] arr) {
        double[] result=new double[arr.length];
        for (int n = 1; n <= arr.length; n++) {
            double[] sum = new double[n - 1];
            for (int i = 0; i < n - 1; i++) {
                int sums = 0;
                for (int j = i + 1; j < n; j++) {
                    double val = arr[j] - arr[i];
                    if (val > 0) {
                        sums = sums + 1;
                    } else if (val < 0) {
                        sums = sums - 1;
                    } else {
                        continue;
                    }
                }
                sum[i]=sums;
            }
            double ss = 0.0;
            for (int i = 0;i<sum.length;i++) {
                ss = ss + sum[i];
            }
            result[n-1]=ss;
        }

        for (int i = 0; i <result.length ; i++) {
            System.out.println("result["+i+"]="+result[i]);
        }
        return result;
    }

    /**
     * Calculate the normal(Gaussian) statistic of Mann-Kendall statistics.
     * @return The normal statistic of Mann-Kendall statistics.
     */
    public double getZ() {
        int n = timeSeries.length;
        double vars = n*(n-1)*(2*n+5)/18.0;
        getS();//get Mann-kendall variable
        if (S>0) {
            Z=(S-1)/Math.sqrt(vars);
        } else if (S < 0) {
            Z = (S + 1) / Math.sqrt(vars);
        }
        return Z;
    }

    /**
     * Calculate the standard normal statistic given the confidence level ALPHA.
     * @return The standard normal statistic.
     */
    public double getZ_ALPHA() {
        double alpha = 1-(1-confidenceLevel)/2;
        NormalDistribution normalDistribution = new NormalDistribution(0,1);
        Z_ALPHA = normalDistribution.inverseCumulativeProbability(alpha);
        System.out.println("Z_α with confidence level "+confidenceLevel+" : "+Z_ALPHA);
        return Z_ALPHA;
    }

    /**
     * Do Mann-Kendall trend analysis.
     * @return The trend of the given time series.
     */
    public String getTrend() {
        getS();
        getZ();
        getZ_ALPHA();
        System.out.println("Mann-Kendall Variable:S="+S);
        System.out.println("Normal statistic:Z="+Z);
        System.out.println("Standard normal statistic(confidence level="+confidenceLevel+"):Z_α="+Z_ALPHA);
        if (Math.abs(Z)>=Z_ALPHA && Z>0) {
            trend="Up trend";
        }
        if (Math.abs(Z)>=Z_ALPHA && Z<0) {
            trend="Down trend";
        }
        if (Math.abs(Z)<Z_ALPHA) {
            trend="No trend";
        }
        return trend;
    }



    public double[] getUFk() {
        double[] Sk0 = calculateSk(timeSeries);

        double E_Sk0 = MathUtil.average(Sk0);
        double var_Sk0 = MathUtil.variance(Sk0);
        for (int i = 0; i < UFk.length; i++) {
            UFk[i]=(Sk0[i]-E_Sk0)/Math.sqrt(var_Sk0);
        }
        return UFk;
    }

    public double[] getUBk() {
        double[] Sk1 = calculateSk(inverseTimeSeries);
        double E_Sk1 = MathUtil.average(Sk1);
        double var_Sk1 = MathUtil.variance(Sk1);
        for (int i = 0; i < UBk.length; i++) {
            UBk[i]=(Sk1[i]-E_Sk1)/Math.sqrt(var_Sk1);
        }
        return UBk;
    }

    public HashMap<Double,Double> getSaltation(){
        double[] UFk=getUFk();
        double[] UBk=getUBk();
        double saltationPoint=0;
        double saltationValue=0.0;
        double gap=0.0;
        for (int i = 0; i < UFk.length-1; i++) {

            gap=UFk[i]-UBk[i];
            System.out.println(gap);
            if (Math.abs(gap)<=0.02){
                saltationPoint=startTimeStep+i*1.0;
                saltationValue=(UFk[i]+UBk[i])/2;
                System.out.println("saltation point:"+saltationPoint);
                System.out.println("saltation value:"+saltationValue);
                saltation.put(saltationPoint,saltationValue);
            }
        }
        return saltation;
    }

    public HashMap<Double,String> getSaltationTest(){
        getSaltation();
        double Z_ALPHA = getZ_ALPHA();
        ArrayList<Double> saltationPoints = new Container<Double>().toArrayList(saltation.keySet());
        for (int i = 0; i < saltationPoints.size(); i++) {
            if (saltation.get(saltationPoints.get(i))<Z_ALPHA)
                saltationTest.put(saltationPoints.get(i),"saltation(突变点)");
                System.out.println(saltationPoints.get(i)+"突变检验通过");

        }
        return saltationTest;
    }

    public void plotTimeSeries(){
        double[][] indexTimeSeties = Container.indexArray(timeSeries,startTimeStep);
        double[][] indexInverseTimeSeriesLine=Container.indexArray(inverseTimeSeries,startTimeStep);
        JavaPlot p = new JavaPlot(PlotUtil.GnuplotPath);
//        p.setTitle("The line of Time Series","Consolas",16);
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        p.getAxis("x").setLabel("time step(t)","Consolas",16);
        p.getAxis("y").setLabel("statistic of UF and UB","Consolas",16);
        DataSetPlot timeSetiesLine = new DataSetPlot(indexTimeSeties);
        DataSetPlot inverseTimeSetiesLine = new DataSetPlot(indexInverseTimeSeriesLine);


        p.addPlot(timeSetiesLine);
        p.addPlot(inverseTimeSetiesLine);
        PlotStyle style_timeSeries = ((AbstractPlot) p.getPlots().get(0)).getPlotStyle();
        PlotStyle style_inverseTimeSeries = ((AbstractPlot) p.getPlots().get(1)).getPlotStyle();


        style_timeSeries.setLineWidth(2);
        style_timeSeries.setStyle(Style.LINESPOINTS);
        style_inverseTimeSeries.setLineWidth(2);
        style_inverseTimeSeries.setStyle(Style.LINESPOINTS);
        ((AbstractPlot) p.getPlots().get(0)).setTitle("The time series");
        ((AbstractPlot) p.getPlots().get(1)).setTitle("The inverse time series");
        p.plot();
    }

    public void plotSaltation(){
        getUBk();
        getUFk();
        double[][] UFk_line= Container.indexArray(UFk,startTimeStep);
        double[][] UBk_line = Container.indexArray(UBk,startTimeStep);
        double[][] Z_ALPHA = Container.indexAndListCommon(getZ_ALPHA(),timeSeries.length,startTimeStep);
        double[][] OZ_ALPHA = Container.indexAndListOppositeCommon(getZ_ALPHA(),timeSeries.length,startTimeStep);
        JavaPlot p = new JavaPlot(PlotUtil.GnuplotPath);
        p.setTitle("The line of UB and UF for MK saltation test","Consolas",16);
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        p.getAxis("x").setLabel("time step(t)","Consolas",16);
        p.getAxis("y").setLabel("value","Consolas",16);
        DataSetPlot UF_line = new DataSetPlot(UFk_line);
        DataSetPlot UB_line = new DataSetPlot(UBk_line);
        DataSetPlot ZALPHA_line = new DataSetPlot(Z_ALPHA);
        DataSetPlot OZALPHA_line = new DataSetPlot(OZ_ALPHA);


        p.addPlot(UF_line);
        p.addPlot(UB_line);
        p.addPlot(ZALPHA_line);
        p.addPlot(OZALPHA_line);
        PlotStyle style_UF = ((AbstractPlot) p.getPlots().get(0)).getPlotStyle();
        PlotStyle style_UB = ((AbstractPlot) p.getPlots().get(1)).getPlotStyle();
        PlotStyle style_ZALPHA = ((AbstractPlot) p.getPlots().get(2)).getPlotStyle();
        PlotStyle style_OZALPHA = ((AbstractPlot) p.getPlots().get(3)).getPlotStyle();

        style_UF.setLineWidth(2);
        style_UF.setStyle(Style.LINESPOINTS);
        style_UB.setLineWidth(2);
        style_UB.setStyle(Style.LINESPOINTS);
        style_ZALPHA.setLineWidth(2);
        style_ZALPHA.setStyle(Style.LINES);
        style_OZALPHA.setLineWidth(2);
        style_OZALPHA.setStyle(Style.LINES);
        ((AbstractPlot) p.getPlots().get(0)).setTitle("The line of UF");
        ((AbstractPlot) p.getPlots().get(1)).setTitle("The line of UB");
        ((AbstractPlot) p.getPlots().get(2)).setTitle("The line of Z_α(alpha="+confidenceLevel+")");
        ((AbstractPlot) p.getPlots().get(3)).setTitle("The line of -Z_α(alpha="+confidenceLevel+")");
        p.plot();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static void main(String[] args) {

        //y=x for k=1:10; y=2*x-6 for k=11:20
        double[] arr = new double[20];
        for (int i = 0; i < arr.length; i++) {
            if (i<=9)
                arr[i]= (i+1);
            else
                arr[i]=2*(i+1)-10;
        }

        double[] yangxian={ 18.95337792,11.12744736,15.87222432 ,10.13979168 ,13.98593088 ,10.2030624,
                11.11577472, 8.48604384,14.31749952, 6.05695968 , 7.38791712 , 6.293808,
                4.51282752 ,6.85151136 ,6.93940608 ,17.42230944 , 5.96280096 , 6.923016,
                4.53312288 ,4.76973792 ,2.85702336 , 3.37218336 ,10.35088416 , 8.80935264,
                6.3275472 , 10.2990528  ,5.4280368 ,  3.38962752 , 2.32632864,  5.69087424};

        double[] zhangjiashan = {7.922016, 19.287936, 61.829568, 70.384032, 99.08784, 61.5816, 380.94624, 808.56576, 182.25216, 136.08, 59.701536, 7.699104, 24.228288, 41.83488, 93.07872, 62.474976, 98.244576, 48.603456, 191.71296, 222.836832, 163.43424, 73.315584, 82.859328, 10.120896, 7.090848, 34.118496, 91.14336, 54.598752, 134.66304, 54.19872, 242.808192, 276.253632, 358.57728, 216.22464, 100.717344, 16.828128, 17.358624, 53.4816, 78.159168, 81.72576, 130.319712, 313.041888,
                118.361952, 86.173632, 83.87712, 32.999616, 5.45616, 13.023936, 18.29088, 10.558944, 51.662016, 28.638144, 25.557984, 101.088864, 94.410144, 710.282304, 171.79776, 122.88672, 58.63536, 4.783968, 14.17392, 44.646336, 84.99168, 58.823712, 54.667008, 23.151744, 215.904096, 233.78112, 108.53568, 115.56864, 58.5792, 7.483104, 8.875008, 30.292704, 72.682272, 97.57584, 28.534464, 88.260192, 351.52704, 218.219616, 84.400704, 49.808736, 53.11872, 28.282176,
                11.952576, 5.234976, 5.241888, 16.538688, 13.1976, 11.4696, 138.584736, 452.36448, 98.480448, 49.788, 40.506048, 5.245344, 3.66336, 11.993184, 5.975424, 14.693184, 23.433408, 87.46704, 623.935872, 346.660992, 125.064, 80.33472, 94.22784, 14.300928, 4.158432, 19.888416, 62.581248, 50.649408, 33.54912,
                15.431904, 152.966016, 181.766592, 33.848928, 23.464512, 10.81728, 16.574112, 9.297504, 11.496384, 44.24976, 20.077632, 199.0656, 69.990048, 157.781088, 131.36256, 41.433984, 38.199168, 12.007872, 3.830112, 4.831488, 14.513472, 7.729344, 13.939776, 42.456096, 37.755072, 342.909504, 40.230432, 46.528128, 53.40384, 21.165408, 3.91824, 4.12992, 9.421056, 15.423264, 5.723136, 7.159968, 113.061312, 92.403936, 61.047648, 18.799776, 82.219968, 33.360768, 8.532, 7.277472, 13.207968, 8.187264, 18.265824, 40.196736, 11.68128, 58.51008, 152.6256, 199.03968, 130.70592, 38.33568, 7.117632, 3.868992, 7.195392,
                16.2, 19.741536, 74.67552, 165.646944, 124.08768, 176.814144, 66.053664, 22.500288, 11.792736, 5.363712, 4.343328, 10.595232, 7.557408, 20.585664, 38.185344, 4.39776, 97.206048, 496.159776, 345.95424, 539.03232, 134.80128, 43.412544, 9.854784, 20.990016, 33.591456, 9.340704, 14.093568, 7.738848, 91.698912, 235.065024, 64.145088, 71.59968, 30.505248, 7.656768, 5.294592, 4.843584, 11.778048, 8.120736, 42.787872, 38.758176, 200.974176, 81.033696, 79.309152, 184.84416, 26.977536, 7.579872, 5.817312, 16.323552, 21.026304, 7.081344, 55.337472, 29.01312, 92.548224, 66.073536, 95.514336, 44.56944, 12.805344, 7.202304, 5.139072, 7.252416, 23.044608, 4.48416, 9.803808, 8.431776, 117.329472, 69.679008, 63.79776, 142.87968, 18.525024, 6.607008, 10.541664, 8.854272, 21.74256, 4.6224, 19.278432, 10.276416, 71.199648, 65.082528, 29.559168, 33.175872, 7.695648, 3.673728, 3.407616, 5.616864, 10.957248, 6.421248, 27.553824, 7.91424, 35.811936, 176.750208, 29.215296, 11.43936, 10.090656, 12.03984, 5.955552, 5.145984, 6.709824, 10.118304, 18.410112, 14.196384,
                185.29776, 453.675168, 225.0288, 96.64704, 11.771136, 2.132352, 2.14272, 2.709504, 9.654336, 3.566592, 21.85488, 8.67888, 17.43552, 95.36832, 356.93568, 168.31584, 131.66496, 62.608032, 18.902592, 7.33968, 60.454944, 35.738496, 79.26336, 25.044768, 87.746976, 112.231872, 137.592, 53.690688, 8.802432,
                5.946912, 3.267648, 18.387648, 4.404672, 25.82928, 40.645152, 30.144096, 492.165504, 180.57168, 152.52192, 43.62336, 28.181088, 10.163232, 6.803136, 7.372512, 23.596704, 59.502816, 55.403136, 20.506176, 27.620352, 50.796288, 159.184224, 88.87104, 26.5032, 16.644096, 5.41728, 14.339808, 20.736, 91.83456, 55.735776, 21.394368, 26.599104, 26.00208, 37.271232, 21.939552, 11.368512, 6.32448, 3.48192, 3.25728, 8.195904, 22.246272, 21.302784, 5.894208, 57.74544, 83.688768, 9.661248, 6.15168, 5.597856, 5.409504, 16.033248, 6.939648, 11.085984, 12.19968, 16.52832, 35.84736, 35.189856, 111.204576, 96.12864, 157.1616, 51.050304, 19.718208};

        MannKendall mk = new MannKendall(yangxian,0.95,1988);
        System.out.println(mk.getS());
        String trend = mk.getTrend();
        System.out.println(trend);
        System.out.println(mk.getZ_ALPHA());
//        mk.getSaltation();
//        mk.getSaltationTest();
        mk.plotSaltation();
//        mk.plotTimeSeries();
    }
}
