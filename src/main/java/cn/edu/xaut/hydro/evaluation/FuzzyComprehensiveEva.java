package cn.edu.xaut.hydro.evaluation;

import cn.edu.xaut.hydro.util.Container;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * @author Jianyi Zuo
 */
public class FuzzyComprehensiveEva {


    /**
     * 计算某一指标在不同等级范围内的隶属度函数值，形成该指标的隶属度数组
     * 指标范围的形式示例：
     * 特高风险   高风险      中风险         低风险
     * （负效应指标）生产工艺        ≥8     1~8   0.5~1  ≤0.5
     * （正效应指标）距黄河距离   ≤10   10~20  20~50   ≥50
     *
     * @param indexValue 指标实测值
     * @param indexStand 指标标准范围数组 String[] arr;如arr[0]="≥8"
     * @return 隶属度数组
     */
    public double[] getMembershipArray(double indexValue, String[] indexStand) {
        double[] arr = new double[indexStand.length];
        if (indexStand[0].contains(">=") || indexStand[0].contains("≥")) {// 成本型指标，指标值越大越不好
            for (int i = 0; i < indexStand.length; i++) {
                if (i == 0) {
                    String[] temp = indexStand[i + 1].split("~");
                    double v1 = Double.parseDouble(temp[1]);
                    double v2 = Double.parseDouble(temp[0]);
                    if (indexValue >= v1) {
                        arr[i] = 1;
                    } else if (indexValue > v2 && indexValue < v1) {
                        arr[i] = (indexValue - v2) / (v1 - v2);
                    } else if (indexValue <= v2) {
                        arr[i] = 0;
                    }
                } else if (i == indexStand.length - 1) {
                    String[] temp = indexStand[i - 1].split("~");
                    double vn_1 = Double.parseDouble(temp[1]);// 1
                    double vn = Double.parseDouble(temp[0]);// 0.5
                    if (indexValue >= vn_1) {
                        arr[i] = 0;
                    } else if (indexValue > vn && indexValue < vn_1) {
                        arr[i] = (vn_1 - indexValue) / (vn_1 - vn);
                    } else if (indexValue <= vn) {
                        arr[i] = 1;
                    }
                } else {


                    double vj_1 = 0.0;// 8
                    double vj = 0.0;// 1
                    double vj_plus_1 = 0.0;// 0.5

                    if (i == indexStand.length - 2) {
                        vj_1 = Double.parseDouble(indexStand[i - 1].split("~")[1]);
                        vj = Double.parseDouble(indexStand[i].split("~")[1]);
                        vj_plus_1 = Double.parseDouble(indexStand[i].split("~")[0]);
                    } else {
                        vj_1 = Double.parseDouble(indexStand[i].split("~")[1]);
                        vj = Double.parseDouble(indexStand[i].split("~")[0]);
                        vj_plus_1 = Double.parseDouble(indexStand[i + 1].split("~")[0]);
                    }
                    if (indexValue >= vj_1 || indexValue <= vj_plus_1) {
                        arr[i] = 0;
                    } else if (indexValue > vj && indexValue < vj_1) {
                        arr[i] = (vj_1 - indexValue) / (vj_1 - vj);
                    } else if (indexValue > vj_plus_1 && indexValue <= vj) {
                        arr[i] = (indexValue - vj_plus_1) / (vj - vj_plus_1);
                    }
                }

            }
        } else if (indexStand[0].contains("<=") || indexStand[0].contains("≤")) {// 效益型指标，指标越大越好
            for (int i = 0; i < indexStand.length; i++) {
                if (i == 0) {
                    String[] temp = indexStand[i + 1].split("~");
                    double v1 = Double.parseDouble(temp[0]);// 10
                    double v2 = Double.parseDouble(temp[1]);// 20
                    if (indexValue <= v1) {
                        arr[i] = 1;
                    } else if (indexValue > v1 && indexValue < v2) {
                        arr[i] = (v2 - indexValue) / (v2 - v1);
                    } else if (indexValue >= v2) {
                        arr[i] = 0;
                    }
                } else if (i == indexStand.length - 1) {
                    String[] temp = indexStand[i - 1].split("~");
                    double vn_1 = Double.parseDouble(temp[0]);// 20
                    double vn = Double.parseDouble(temp[1]);// 50
                    if (indexValue <= vn_1) {
                        arr[i] = 0;
                    } else if (indexValue > vn_1 && indexValue < vn) {
                        arr[i] = (indexValue - vn_1) / (vn - vn_1);
                    } else if (indexValue >= vn) {
                        arr[i] = 1;
                    }
                } else {
                    double vj_1 = 0.0;
                    double vj = 0.0;
                    double vj_plus_1 = 0.0;
                    if (i == indexStand.length - 2) {
                        vj_1 = Double.parseDouble(indexStand[i - 1].split("~")[0]);
                        vj = Double.parseDouble(indexStand[i].split("~")[0]);
                        vj_plus_1 = Double.parseDouble(indexStand[i].split("~")[1]);
                    } else {
                        vj_1 = Double.parseDouble(indexStand[i].split("~")[0]);
                        vj = Double.parseDouble(indexStand[i].split("~")[1]);
                        vj_plus_1 = Double.parseDouble(indexStand[i + 1].split("~")[1]);
                    }
                    if (indexValue <= vj_1 || indexValue >= vj_plus_1) {
                        arr[i] = 0;
                    } else if (indexValue > vj_1 && indexValue < vj) {
                        arr[i] = (indexValue - vj_1) / (vj - vj_1);
                    } else if (indexValue >= vj && indexValue < vj_plus_1) {
                        arr[i] = (vj_plus_1 - indexValue) / (vj_plus_1 - vj);
                    }
                }
            }
        }


        System.err.println("============单一评价指标的模糊矩阵如下==============");
        for (int i = 0; i < arr.length; i++) {
            System.err.print(arr[i] + " ");
        }
        System.err.println("\n");

        return arr;
    }


    /**
     * 获取单一评价对象的评价等级
     * <p>
     * * 指标范围的形式示例：
     * 特高风险   高风险      中风险         低风险
     * （负效应指标）生产工艺        ≥8     1~8   0.5~1  ≤0.5
     * （正效应指标）距黄河距离   ≤10   10~20  20~50   ≥50
     *
     * @param indexValue 指标实测值
     * @param indexValue {key=指标名称， value=指标值}
     * @param indexStand {key=指标名称，value=指标范围数组}
     * @return 返回评价等级，数值越小，危险等级越高
     */
    public int getGrade(HashMap<String, Double> indexValue, HashMap<String, String[]> indexStand, HashMap<String, Double> weightMap) {

        Container<String> container = new Container<String>();
        TreeSet<String> indexSet = new TreeSet<String>(indexValue.keySet());
        ArrayList<String> indexList = container.toArrayList(indexSet);//排序后的指标集合
        String[] propOfPosNeg = new String[indexValue.keySet().size()];
        double[] wi = new double[indexValue.keySet().size()];//大小为指标数
        DecimalFormat df = new DecimalFormat("0.00");
        double[][] matrixDouble = new double[indexValue.keySet().size()][];
        for (int i = 0; i < indexList.size(); i++) {
            matrixDouble[i] = getMembershipArray(indexValue.get(indexList.get(i)), indexStand.get(indexList.get(i)));
            if (indexStand.get(indexList.get(i))[0].contains("≥")) {
                propOfPosNeg[i] = "-";
            } else if (indexStand.get(indexList.get(i))[0].contains("≤")) {
                propOfPosNeg[i] = "+";
            }
        }

        System.err.println("模糊关系矩阵");
        for (int i = 0; i < matrixDouble.length; i++) {
            for (int j = 0; j < matrixDouble[i].length; j++) {
                System.err.print("R[" + i + "][" + j + "]=" + matrixDouble[i][j] + "	");
            }
            System.err.println();
        }
		
		/*EntropyWeight entropyWeight = new EntropyWeight(matrixDouble, propOfPosNeg);
		wi = entropyWeight.getWeight();*/

        double[] b = new double[indexStand.get(indexList.get(0)).length];


        /*for (int i = 0; i < b.length; i++) {//n个指标
            double sum = 0.0;
            for (int j = 0; j < indexList.size(); j++) {//4
                sum += weightMap.get(indexList.get(j)) * matrixDouble[j][i];
            }
            b[i] = sum;
        }*/

        //B=W*R: 矩阵的运算规则定义为，两数相乘取小者为积，多数相加取大着为和
        for (int i = 0; i < b.length; i++) {//p个评价等级
            double[] m = new double[b.length];
            for (int j = 0; j < indexList.size(); j++) {
                //sum += weightMap.get(indexList.get(j)) * matrixDouble[j][i];
                if(weightMap.get(indexList.get(j))<=matrixDouble[j][i]){
                    m[i] = weightMap.get(indexList.get(j));
                }else{
                    m[i]= matrixDouble[j][i];
                }
            }
            double sum = m[0];
            for(int x = 0;x < m.length;x++){
                if(sum < m[x]){
                    sum = m[x];
                }
            }
            b[i] = sum;
        }

        System.err.println(b.length);


        int loc = 0;
        double max = b[0];
        for (int i = 0; i < b.length; i++) {
            System.out.print("    " + b[i]);
            if (max < b[i]) {
                max = b[i];
                loc = i;
            }

        }
        System.out.println();

        return loc + 1;
    }



	public double[] getGradeArr(HashMap<String,HashMap<String,Double>> indexValuesMap,HashMap<String, String[]> indexStand,HashMap<String, Double> weightMap){
		ArrayList<String> cdList = new Container<String>().toArrayList(indexValuesMap.keySet());
		double[] arr = new double[cdList.size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = getGrade(indexValuesMap.get(cdList.get(i)),indexStand,weightMap);
		}

		return arr;
	}

}
