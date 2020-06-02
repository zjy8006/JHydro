package cn.edu.xaut.hydro.evaluation;

import cn.edu.xaut.hydro.evaluation.weight.EntropyWeight;
import cn.edu.xaut.hydro.util.Container;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import org.junit.Test;
public class TestEntropyWeight {
    @Test
    public void entropyWeightsTest() {
        HashMap<String, Double> indexValue = new HashMap<String, Double>();
        HashMap<String, String[]> indexStands = new HashMap<String, String[]>();
        HashMap<String, Double> weightMap = new HashMap<String, Double>();

        indexValue.put("生产工艺", 10.0);
        indexValue.put("行业类别", 50.0);
        indexValue.put("重金属排放", 500.0);
        indexValue.put("污染物排放达标情况", 50.0);
        indexValue.put("环境风险物质", 0.009);

        indexValue.put("废水排放去向", 50.0);
        indexValue.put("至黄河距离", 20.0);
        indexValue.put("企业规模", 50.0);
        indexValue.put("企业运营时间", 5.0);

        indexValue.put("受纳水体", 70.0);
        indexValue.put("企业周边居民密度", 300.0);
        indexValue.put("与下游自然保护区距离", 20.0);
        indexValue.put("下游饮用水水源地情况", 50.0);

        indexValue.put("企业应急演练频次", 100.0);
        indexValue.put("企业应急投入占比", 0.03);


        String[] SCGY = {">=100", "70~100", "50~70","10~50"};              //逆向
        String[] HYLB = {">=100", "70~100", "50~70","10~50"};              //逆向
        String[] ZJSPF = {">=1000", "500~1000", "10~500", "0~10"};         //逆向
        String[] WRWDB = {">=100", "70~100", "50~70","10~50"};             //逆向
        String[] HJFXWZ = {">=100", "10~100", "1~10", "0~1"};              //逆向

        String[] FSPFQX = {">=100", "70~100", "50~70","10~50"};            //逆向
        String[] ZHHJL = {"<=10", "10~20", "20~30", ">=30"};                //正向
        String[] QYGM = {">=100", "70~100", "50~70","10~50"};              //逆向
        String[] QYYYSJ = {">=20", "10~20", "5~10",  "<=5"};                //逆向

        String[] SNST = {">=100", "70~100", "50~70","10~50"};              //逆向
        String[] JMMD = {">=1000", "800~1000", "300~800","<=300"};          //逆向
        String[] BHQJL = {"<=10", "10~20", "20~30", ">=30"};                //正向
        String[] SYDQK = {">=100", "70~100", "50~70","10~50"};             //逆向

        String[] YJYL = {">=100", "70~100", "50~70","10~50"};              //逆向
        String[] YJTR = {"<=0.0.1", "0.02~0.03", "0.02~0.03",">=0.03"};     //正向


        indexStands.put("生产工艺", SCGY);
        indexStands.put("行业类别", HYLB);
        indexStands.put("重金属排放", ZJSPF);
        indexStands.put("环境风险物质", HJFXWZ);
        indexStands.put("污染物排放达标情况", WRWDB);

        indexStands.put("废水排放去向", FSPFQX);
        indexStands.put("至黄河距离", ZHHJL);
        indexStands.put("企业规模", QYGM);
        indexStands.put("企业运营时间", QYYYSJ);

        indexStands.put("受纳水体", SNST);
        indexStands.put("企业周边居民密度", JMMD);
        indexStands.put("与下游自然保护区距离", BHQJL);
        indexStands.put("下游饮用水水源地情况", SYDQK);

        indexStands.put("企业应急演练频次", YJYL);
        indexStands.put("企业应急投入占比", YJTR);

        Container<String> container = new Container<String>();
        TreeSet<String> indexSet = new TreeSet<String>(indexValue.keySet());
        ArrayList<String> indexList = container.toArrayList(indexSet);//排序后的指标集合
        String[] propOfPosNeg = new String[indexValue.keySet().size()];
        double[] wi = new double[indexValue.keySet().size()];//大小为指标数
        DecimalFormat df = new DecimalFormat("0.00");
        double[][] matrixDouble = {
                {10.0,50.0,500.0,50.0,0.009,50.0,20.0,50.0,5.0,70.0,300.0,20.0,50.0,100.0,0.03},
                {70.0,100.0,1000.0,10.0,0.011,70.0,50.0,100.0,10.0,50.0,500.0,50.0,10.0,70.0,0.08},
        };

        System.out.println(indexList);
        System.out.println(indexStands);


        for (int i = 0; i < indexList.size(); i++) {
            System.out.println(indexList.get(i));
            if (indexStands.get(indexList.get(i))[0].contains(">=")) {
                propOfPosNeg[i] = "-";
            } else if (indexStands.get(indexList.get(i))[0].contains("<=")) {
                propOfPosNeg[i] = "+";
            }
        }

        for (int i = 0; i < matrixDouble.length; i++) {
            for (int j = 0; j < matrixDouble[i].length; j++) {
                System.out.print(" (i="+i+";j="+j+")="+matrixDouble[i][j]);
            }
            System.out.println();
        }

        EntropyWeight entropyWeight = new EntropyWeight(matrixDouble, propOfPosNeg);
        wi = entropyWeight.getWeight();
        double sum=0;
        for (int i = 0; i <wi.length ; i++) {
            System.out.println(wi[i]);
            sum+=wi[i];
        }
        System.out.println("sum(wi)="+sum);
    }
}
