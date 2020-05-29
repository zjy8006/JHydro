package cn.edu.xaut.hydro.meteoroRad;

import cn.edu.xaut.hydro.util.DateUtil;

public class ZenithRad {


    private double J = 0.0;//日序时数
    private double radianLati = 0.0;//弧度制纬度
    private double dr = 0.0;//日地间相对距离的倒数
    private double theta = 0.0;//太阳磁偏角
    private double omegas = 0.0;//日落时角
    private double ra = 0.0;//天顶辐射
    private double omega1 = 0.0;//时段初太阳时角（弧度制）
    private double omega2 = 0.0;//时段末太阳时角（弧度制）
    private double omega = 0.0;//时段中点太阳时角（弧度制）
    private double sc = 0.0;//日照时间季节修正系数

    public ZenithRad(String date,String timeZone_longitude,String longitude,String latitude) {
        getZenithOfRadiation(date,timeZone_longitude,longitude,latitude);
    }

    public ZenithRad() {
    }

    /**
     * 时段初太阳时角（弧度制）
     * @return
     */
    public double getOmega1() {
        return omega1;
    }

    /**
     * 时段末太阳时角（弧度制）
     * @return
     */
    public double getOmega2() {
        return omega2;
    }

    /**
     * 时段中点太阳时角（弧度制）
     * @return
     */
    public double getOmega() {
        return omega;
    }

    /**
     * 日照时间季节修正系数
     * @return
     */
    public double getSc() {
        return sc;
    }

    /**
     * 获取日序时数J
     * @return
     */
    public double getJ() {
        return J;
    }

    /**
     * 获取弧度制纬度Φ
     * @return
     */
    public double getRadianLati() {
        return radianLati;
    }

    /**
     * 获取日地间相对距离的倒数dr
     * @return
     */
    public double getDr() {
        return dr;
    }

    /**
     * 太阳磁偏角δ
     * @return
     */
    public double getTheta() {
        return theta;
    }

    /**
     * 日落时角ωs
     * @return
     */
    public double getOmegas() {
        return omegas;
    }

    /**
     * 天顶辐射
     * @return
     */
    public double getRa() {
        return ra;
    }



    /**
     * 计算天顶辐射
     * @param date 日期
     * @param timeZone_longitude 所处时区经度
     * @param longitude 测点经度
     * @param latitude 测点纬度
     */
    private void getZenithOfRadiation(String date, String timeZone_longitude,String longitude,String latitude) {

        J = DateUtil.getOrdinalNum(date);//计算日序数

        radianLati = 0.0;//计算弧度表示的纬度
        if (latitude.contains("N")) {
            String[] lttdArr = latitude.split("N|°|\'|\"");
            radianLati = (Math.PI / 180) * (Double.valueOf(lttdArr[1]) + Double.valueOf(lttdArr[2]) / 60);
        } else if (latitude.contains("S")) {
            String[] lttdArr = latitude.split("S|°|\'|\"");
            radianLati = (Math.PI / 180) * (-Double.valueOf(lttdArr[1]) - Double.valueOf(lttdArr[2]) / 60);
        }

        dr = 1 + 0.033 * Math.cos(2 * Math.PI / 365 * J);//日地间相对距离的倒数
        theta = 0.409 * Math.sin(2 * Math.PI / 365 * J - 1.39);//太阳磁偏角
        omegas = Math.acos(-Math.tan(radianLati) * Math.tan(theta));//日落时角

        if (date.length()>15){
            double b = 2*Math.PI*(J-81)/364;
            sc = 0.1645*Math.sin(2*b)-0.1255*Math.cos(b)-0.025*Math.sin(b);
            double t=Double.valueOf(date.substring(11,13))+0.5;//中点时刻

            double LZ = 0.0;//当地时区中心经度
            double Lm = 0.0;//测点的经度

            if (timeZone_longitude.contains("E")){

                String[] timeZone_long = timeZone_longitude.split("E|°|\'|\"");
                for (int i = 0; i < timeZone_long.length; i++) {
                    System.err.println("第"+i+"个值为："+timeZone_long[i]);
                }
                String[] longArr = longitude.split("E|°|\'|\"");
                for (int i = 0; i < longArr.length; i++) {
                    System.err.println("第"+i+"个值为："+longArr[i]);
                }
                if (timeZone_long.length>2 ){
                    LZ = ((Double.valueOf(timeZone_long[1])+Double.valueOf(timeZone_long[2])/60));
                }else{
                    LZ = ((Double.valueOf(timeZone_long[1])));
                }
                if (longArr.length > 2) {
                    Lm = ((Double.valueOf(longArr[1]) + Double.valueOf(longArr[2]) / 60));
                }else{
                    Lm = ((Double.valueOf(longArr[1])));
                }


            }else {
                String[] timeZone_long = timeZone_longitude.split("W|°|\'|\"");
                String[] longArr = longitude.split("W|°|\'|\"");
                LZ = Double.valueOf(timeZone_long[1]);
                Lm = Double.valueOf(longArr[1]);
            }

            System.err.println("t时刻中点="+t);
            System.err.println("格林威治以西时区中心经度 = "+LZ);
            System.err.println("格林威治以西测点经度 = "+Lm);

            omega = Math.PI/12*((t+0.06667*(LZ-Lm)+sc)-12);

            omega1 = omega-Math.PI*1/24;
            omega2 = omega+Math.PI*1/24;

            double raw_ra = 12 * 60 / Math.PI * 0.0820 * dr * ((omega2-omega1) * Math.sin(radianLati) * Math.sin(theta) +
                    Math.cos(radianLati) * Math.cos(theta) *(Math.sin(omega2)-Math.sin(omega1)) );

            if(raw_ra>=0){
                ra = raw_ra;
            }else{
                ra = 0.0;
            }
        }else{
            ra = 24 * 60 / Math.PI * 0.0820 * dr * (omegas * Math.sin(radianLati) * Math.sin(theta) +
                    Math.cos(radianLati) * Math.cos(theta) * Math.sin(omegas));
        }

    }
}
