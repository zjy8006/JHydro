package cn.edu.xaut.hydro.util;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.utils.Debug;

/**
 * @author Jianyi Zuo
 */
public class PlotUtil {


    public static String GnuplotPath = "D:\\WorkSpace\\Idea\\JHydro\\src\\main\\resources\\gp530\\bin\\gnuplot.exe";

    public void plot2Dlines(double[][] ... timeSeries) {
        JavaPlot p = new JavaPlot(GnuplotPath);
        p.setTitle("-----Time Series-----","Consolas",16);
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        p.getAxis("x").setLabel("Time Interval","Consolas",16);
        p.getAxis("y").setLabel("Target Value","Consolas",16);

        DataSetPlot[] dataSetPlots = new DataSetPlot[timeSeries.length];
        for (int i = 0; i < dataSetPlots.length; i++) {
            dataSetPlots[i] = new DataSetPlot(timeSeries[i]);
            p.addPlot(dataSetPlots[i]);
        }
        for (int i = 0; i < timeSeries.length; i++) {
            ((AbstractPlot)p.getPlots().get(i)).getPlotStyle().setStyle(Style.LINESPOINTS);
            ((AbstractPlot)p.getPlots().get(i)).setTitle("time series "+i);
            ((AbstractPlot)p.getPlots().get(i)).getPlotStyle().setLineWidth(2);

        }
        p.plot();
    }

    public static void plotMultiLines(
            String graphTitle,
            String xLabel,
            String yLabel,
            String l1Title,
            double[] l1data,
            String l2Title,
            double[] l2data,
            String l3Title,
            double[] l3data,
            String l4Title,
            double[] l4data
    ) {

        double[][] line1 = new double[l1data.length][2];
        double[][] line2 = new double[l2data.length][2];
        double[][] line3 = new double[l3data.length][2];
        double[][] line4 = new double[l4data.length][2];
        for (int i = 0; i < l1data.length; i++) {
            line1[i][0] = i + 1;
            line1[i][1] = l1data[i];
        }
        for (int i = 0; i < l2data.length; i++) {
            line2[i][0] = i + 1;
            line2[i][1] = l2data[i];
        }

        for (int i = 0; i < l3data.length; i++) {
            line3[i][0] = i + 1;
            line3[i][1] = l3data[i];
        }

        for (int i = 0; i < l4data.length; i++) {
            line4[i][0] = i + 1;
            line4[i][1] = l4data[i];
        }

        JavaPlot p = new JavaPlot(GnuplotPath);
        p.setTitle(graphTitle,"Consolas",16);
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        p.getAxis("x").setLabel(xLabel,"Consolas",16);
        p.getAxis("y").setLabel(yLabel,"Consolas",16);
        DataSetPlot pline1 = new DataSetPlot(line1);
        DataSetPlot pline2 = new DataSetPlot(line2);
        DataSetPlot pline3 = new DataSetPlot(line3);
        DataSetPlot pline4 = new DataSetPlot(line4);
        p.addPlot(line1);
        p.addPlot(line2);
        p.addPlot(line3);
        p.addPlot(line4);
        PlotStyle style1 = ((AbstractPlot) p.getPlots().get(0)).getPlotStyle();
        PlotStyle style2 = ((AbstractPlot) p.getPlots().get(1)).getPlotStyle();
        PlotStyle style3 = ((AbstractPlot) p.getPlots().get(2)).getPlotStyle();
        PlotStyle style4 = ((AbstractPlot) p.getPlots().get(3)).getPlotStyle();
        style1.setLineWidth(2);
        style2.setLineWidth(2);
        style3.setLineWidth(2);
        style4.setLineWidth(2);


        style1.setStyle(Style.LINES);
        style2.setStyle(Style.LINESPOINTS);
        style3.setStyle(Style.ERRORLINES);
        style4.setStyle(Style.XYERRORLINES);
        ((AbstractPlot) p.getPlots().get(0)).setTitle(l1Title);
        ((AbstractPlot) p.getPlots().get(1)).setTitle(l2Title);
        ((AbstractPlot) p.getPlots().get(2)).setTitle(l3Title);
        ((AbstractPlot) p.getPlots().get(3)).setTitle(l4Title);
        p.plot();
    }


    public static void plot(double[] orig, double[] pred, String graphTitle,String origTitle, String predTitle,String xLabel,String yLabel) {

        double[][] pred_data = new double[pred.length][2];
        double[][] orig_data = new double[orig.length][2];
        for (int i = 0; i < pred.length; i++) {
            pred_data[i][0] = i + 1;
            pred_data[i][1] = pred[i];
            orig_data[i][0] = i + 1;
            orig_data[i][1] = orig[i];
        }

        JavaPlot p = new JavaPlot(GnuplotPath);
        p.setTitle(graphTitle,"Consolas",16);
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        p.getAxis("x").setLabel(xLabel,"Consolas",16);
        p.getAxis("y").setLabel(yLabel,"Consolas",16);
        DataSetPlot orig_line = new DataSetPlot(orig_data);
        DataSetPlot pred_line = new DataSetPlot(pred_data);
        p.addPlot(orig_line);
        p.addPlot(pred_line);
        PlotStyle style_orig = ((AbstractPlot) p.getPlots().get(0)).getPlotStyle();
        PlotStyle style_pred = ((AbstractPlot) p.getPlots().get(1)).getPlotStyle();
        style_orig.setLineWidth(2);
        style_pred.setLineWidth(2);
        style_orig.setStyle(Style.LINES);
        style_pred.setStyle(Style.LINESPOINTS);
        ((AbstractPlot) p.getPlots().get(0)).setTitle(origTitle);
        ((AbstractPlot) p.getPlots().get(1)).setTitle(predTitle);
//        ((AbstractPlot) p.getPlots().get(0)).setSmooth(Smooth.SBEZIER);
//        ((AbstractPlot) p.getPlots().get(1)).setSmooth(Smooth.ACSPLINES);
        p.plot();
    }

    public static void plot(float[] orig, float[] pred,String graphTitle, String origTitle, String predTitle,String xLabel,String yLabel) {

        float[][] pred_data = new float[pred.length][2];
        float[][] orig_data = new float[orig.length][2];
        for (int i = 0; i < pred.length; i++) {
            pred_data[i][0] = i + 1;
            pred_data[i][1] = pred[i];
            orig_data[i][0] = i + 1;
            orig_data[i][1] = orig[i];
        }

        JavaPlot p = new JavaPlot();
        p.setTitle(graphTitle,"Consolas",16);
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        p.getAxis("x").setLabel(xLabel,"Consolas",16);
        p.getAxis("y").setLabel(yLabel,"Consolas",16);
        DataSetPlot orig_line = new DataSetPlot(orig_data);
        DataSetPlot pred_line = new DataSetPlot(pred_data);
        p.addPlot(orig_line);
        p.addPlot(pred_line);
        PlotStyle style_orig = ((AbstractPlot) p.getPlots().get(0)).getPlotStyle();
        PlotStyle style_pred = ((AbstractPlot) p.getPlots().get(1)).getPlotStyle();
        style_orig.setLineWidth(2);
        style_pred.setLineWidth(2);
        style_orig.setStyle(Style.LINES);
        style_pred.setStyle(Style.LINESPOINTS);
        ((AbstractPlot) p.getPlots().get(0)).setTitle(origTitle);
        ((AbstractPlot) p.getPlots().get(1)).setTitle(predTitle);
        p.plot();
    }
}
