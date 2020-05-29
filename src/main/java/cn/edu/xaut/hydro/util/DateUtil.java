package cn.edu.xaut.hydro.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DateUtil {

    public static HashMap<String,Integer> getOrdinalNum(ArrayList<String> dateList){
        HashMap<String,Integer> dateOrdinalNum = new HashMap<String, Integer>();
        for (int i = 0; i < dateList.size(); i++) {

            String[] dateArr = dateList.get(i).substring(0,10).split("-");
            int year = Integer.parseInt(dateArr[0]);
            int month = Integer.parseInt(dateArr[1]);
            int day = Integer.parseInt(dateArr[2]);

            int J = (int) (Math.floor(275.0*month/9-30+day)-2);

            if (((year%4==0 && year%100!=0) || year%400==0) && month>2) {
                J=J+1;
            }else if (month<3) {
                J=J+2;
            }

            dateOrdinalNum.put(dateList.get(i), J);
        }
        return dateOrdinalNum;
    }

    public static int getOrdinalNum(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date0 = new Date();
        try {
            date0 = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String date1 = sdf.format(date0);
        String[] dateArr = date1.split("-");
        int year = Integer.parseInt(dateArr[0]);
        int month = Integer.parseInt(dateArr[1]);
        int day = Integer.parseInt(dateArr[2]);

        int J = (int) (Math.floor(275.0*month/9-30+day)-2);

        if (((year%4==0 && year%100!=0) || year%400==0) && month>2) {
            J=J+1;
        }else if (month<3) {
            J=J+2;
        }
        return J;

    }

    

    public static void main(String[] args) {
        getOrdinalNum("2017-07-01");
    }
}
