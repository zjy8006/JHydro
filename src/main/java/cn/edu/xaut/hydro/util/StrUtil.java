package cn.edu.xaut.hydro.util;

import java.util.ArrayList;

public class StrUtil {

    public static String commaSplice(ArrayList<String> arrayList){
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < arrayList.size(); i++) {
            s.append("'").append(arrayList.get(i)).append("',");
        }
        String STCD = s.toString();
        STCD = STCD.substring(0, STCD.length() - 1);
        return STCD;
    }

    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("20180525");
        arrayList.add("20180526");
        arrayList.add("20180527");
        arrayList.add("20180528");
        arrayList.add("20180529");
        System.out.println(commaSplice(arrayList));
    }
}
