package cn.edu.xaut.hydro.evaluation;

import cn.edu.xaut.hydro.math.basic.MathUtil;
import cn.edu.xaut.hydro.util.Container;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;


/**
 * @author
 * 假设有m个对象，n个指标
 */
public class EntropyWeight {



    double[][] rawMatrix;//判断矩阵：m个评价指标，n个评价等级 Rm*n
    String[] propOfPosNeg;//指标正负属性
    double[][] nonDem;//判断矩阵归一化后的矩阵
    double[][] frArr ;//特征比重矩阵
    double[] entropy ;//m个指标的信息熵
    double[] varCoef ;//m个指标差异系数矩阵
    double[] weight ;//权重矩阵


    /**
     * 初始化熵权法所需原始矩阵，也就是判断矩阵
     *
     * @param rawMatrix    熵权法输入的判断举证：二维数组Rm*n,其中m个评价对象，n个评价指标
     * @param propOfPosNeg 存储指标效益的正负：其中正效益指标为指标越大越好，用“+”来表示；负效益指标为指标越小越好；
     */
    public EntropyWeight(double[][] rawMatrix, String[] propOfPosNeg) {
        this.rawMatrix = rawMatrix;
        nonDem = new double[rawMatrix.length][rawMatrix[0].length];
        frArr = new double[rawMatrix.length][rawMatrix[0].length];
        entropy = new double[rawMatrix[0].length];
        varCoef = new double[rawMatrix[0].length];
        weight = new double[rawMatrix[0].length];
        this.propOfPosNeg = propOfPosNeg;
        if (rawMatrix[0].length != propOfPosNeg.length)
            System.err.println("指标数目与正负效益指示不相符");
    }

    /**
     * 将正负效益指标对应的等级无量纲化，也就是归一化的意思
     *
     * @return 归一化后的判断矩阵
     */
    private double[][] nondimensionalization() {
    	
    	double[] r_max = new double[rawMatrix[0].length];
    	double[] r_min = new double[rawMatrix[0].length];
    	for(int j=0;j<rawMatrix[0].length;j++) {//n个评价指标
    		double max = rawMatrix[0][j];
    		double min = rawMatrix[0][j];
    		for (int i = 1; i < rawMatrix.length; i++) {//m个对象
    			if (rawMatrix[i][j]>max) {
					max=rawMatrix[i][j];
				}
    			if (rawMatrix[i][j]<min) {
					min=rawMatrix[i][j];
				}
    		}
    		r_max[j]=max;
    		r_min[j]=min;
    	}

    	//test for the font

        for (int i = 0; i < rawMatrix.length; i++) {//m个对象
            for (int j = 0; j < rawMatrix[0].length; j++) {//n个评价指标
                if (propOfPosNeg[i].equals("+")) {
                    nonDem[i][j] = (rawMatrix[i][j] - r_min[j]) / (r_max[j] - r_min[j]);
                } else if (propOfPosNeg[i].equals("-")) {
                    nonDem[i][j] = (r_max[j] - rawMatrix[i][j]) / (r_max[j] - r_min[j]);
                }
                System.out.println("[i="+i+";j="+j+"]="+nonDem[i][j]);
            }
        }
        return nonDem;
    }

    /**
     * 获取第i个指标下，第j个评价等级的特征比重
     *
     * @return
     */
    private double[][] getFetureRate() {
        nondimensionalization();
        double sum = 0.0;
        for (int i = 0; i < nonDem.length; i++) {//m个对象
        	for (int j = 0; j < nonDem[i].length; j++) {//n个指标
        		sum+=(nonDem[i][j]+1);
        	}
        }
        for (int i = 0; i < nonDem.length; i++) {//m个对象
            for (int j = 0; j < nonDem[i].length; j++) {
                frArr[i][j] = (nonDem[i][j] + 1) / sum;
            }
        }
        return frArr;
    }

    /**
     * 计算信息熵，一个指标对应一个信息熵
     *
     * @return
     */
    private double[] getEntropy() {
        getFetureRate();
        for (int j = 0; j < frArr[0].length; j++) {//n个指标
        	double sum = 0.0;
        	for (int i = 0; i < frArr.length; i++) {//m个对象
        		sum += frArr[i][j] * Math.log(frArr[i][j]);
        	}
        	entropy[j] = (-1 / Math.log(frArr.length)) * sum;
        }
        return entropy;
    }

    /**
     * 获取差异系数1-ei
     * @return
     */
    private double[] getVarCoef() {
        getEntropy();
        for (int j = 0; j < entropy.length; j++) {
            varCoef[j] = 1 - entropy[j];
        }
        return varCoef;
    }

    public double[] getWeight(){

        getVarCoef();
        for (int j = 0; j < varCoef.length; j++) {
//        	System.out.println("第"+j+"变异系数="+varCoef[j]);
//        	System.out.println("变异系数总和="+MathUtil.sum(varCoef));
            weight[j] = varCoef[j]/MathUtil.sum(varCoef);
        }
        return weight;
    }


    public static void main(String[] args) {
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
        
        
        String[] SCGY = {"≥100", "70~100", "50~70","10~50"};              //逆向
        String[] HYLB = {"≥100", "70~100", "50~70","10~50"};              //逆向
        String[] ZJSPF = {"≥1000", "500~1000", "10~500", "0~10"};         //逆向
        String[] WRWDB = {"≥100", "70~100", "50~70","10~50"};             //逆向
        String[] HJFXWZ = {"≥100", "10~100", "1~10", "0~1"};              //逆向
        
        String[] FSPFQX = {"≥100", "70~100", "50~70","10~50"};            //逆向
        String[] ZHHJL = {"≤10", "10~20", "20~30", "≥30"};                //正向
        String[] QYGM = {"≥100", "70~100", "50~70","10~50"};              //逆向
        String[] QYYYSJ = {"≥20", "10~20", "5~10",  "≤5"};                //逆向
        
        String[] SNST = {"≥100", "70~100", "50~70","10~50"};              //逆向
        String[] JMMD = {"≥1000", "800~1000", "300~800","≤300"};          //逆向
        String[] BHQJL = {"≤10", "10~20", "20~30", "≥30"};                //正向
        String[] SYDQK = {"≥100", "70~100", "50~70","10~50"};             //逆向
       
        String[] YJYL = {"≥100", "70~100", "50~70","10~50"};              //逆向
        String[] YJTR = {"≤0.0.1", "0.02~0.03", "0.02~0.03","≥0.03"};     //正向
        
        
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
        
        
        
        
        
        FuzzyComprehensiveEva fuzzy=new FuzzyComprehensiveEva();
        
        for (int i = 0; i < indexList.size(); i++) {
            if (indexStands.get(indexList.get(i))[0].contains("≥")) {
                propOfPosNeg[i] = "-";
            } else if (indexStands.get(indexList.get(i))[0].contains("≤")) {
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
