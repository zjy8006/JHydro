package cn.edu.xaut.hydro.function;

/**
 * 一元一次函数y=k*x+b
 */
public class Linear {
    private double slope = 0.0;//斜率
    private double bias = 0.0;//截距

    public Linear(double x1,double y1,double x2,double y2) {
        slope = (y2-y1)/(x2-x1);
        bias = ((y1+y2)-slope*(x1+x2))/2;
    }

    public double getSlope() {
        return slope;
    }

    public double getBias() {
        return bias;
    }

    @Override
    public String toString() {
        return "y=" +slope +"*x+" + bias;
    }


    public static void main(String[] args) {

        Linear linear = new Linear(2881.0,1.15,3601.0,1.05);
        System.out.println(linear);
        System.out.println(2882.0*linear.getSlope()+linear.getBias());
    }
}

