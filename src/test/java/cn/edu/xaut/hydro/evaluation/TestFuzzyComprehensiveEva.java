package cn.edu.xaut.hydro.evaluation;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class TestFuzzyComprehensiveEva {
    @Test
    public void fuzzyComprehEva() {
        ArrayList<HashMap<String, Double>> indexValues = new ArrayList<HashMap<String, Double>>();
        HashMap<String, String[]> indexStands = new HashMap<String, String[]>();
        HashMap<String, Double> weightMap = new HashMap<String, Double>();

//        indexValue.put("有机质", 0.10846);
//        indexValue.put("速效钾", 47.3);
//        indexValue.put("有效磷", 1.337);
//        indexValue.put("全氮", 0.009);

        String[] YJZ = {"≥4", "3~4", "2~3", "1.5~2", "1.2~1.5", "1~1.2", "0.8~1", "0.6~0.8", "≤0.6"};
        String[] SXJ = {"≥200", "150~200", "120~150", "100~120", "70~100", "50~70", "30~50", "20~30", "≤20"};
        String[] YXL = {"≥40", "30~40", "20~30", "15~20", "10~15", "5~10", "3~5", "1~3", "≤1"};
        String[] QD = {"≥0.2", "0.16~0.2", "0.126~0.16", "0.11~0.126", "0.076~0.11", "0.06~0.076", "0.01~0.06", "0.005~0.01", "≤0.005"};

        indexStands.put("有机质", YJZ);
        indexStands.put("速效钾", SXJ);
        indexStands.put("有效磷", YXL);
        indexStands.put("全氮", QD);

        weightMap.put("有机质", 0.10);
        weightMap.put("速效钾", 0.12);
        weightMap.put("有效磷", 0.26);
        weightMap.put("全氮", 0.52);

        double[][] rawmatrix = new double[10][15];
        String[] index = {"生产工艺",};

        for (int i = 0; i < rawmatrix.length; i++) {//m个对象
            HashMap<String,Double> indexValue = new HashMap<String, Double>();
            for (int j = 0; j < rawmatrix[0].length; j++) {//n个指标
                indexValue.put(index[j],rawmatrix[i][j]);
            }
            indexValues.add(indexValue);
        }

        FuzzyComprehensiveEva fuzzyComprehensiveEva = new FuzzyComprehensiveEva();
        for (int i = 0; i < indexValues.size(); i++) {

            int grade = fuzzyComprehensiveEva.getGrade(indexValues.get(i), indexStands, weightMap);

            System.out.println("评价等级为" + grade);
        }

    }
}
