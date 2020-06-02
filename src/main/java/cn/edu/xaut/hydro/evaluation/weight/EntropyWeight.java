package cn.edu.xaut.hydro.evaluation.weight;

import cn.edu.xaut.hydro.math.basic.MathUtil;
import cn.edu.xaut.hydro.util.Container;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;


/**
 * @author Jianyi Zuo
 * Compute weights based on entropy.
 */
public class EntropyWeight {



    double[][] rawMatrix;   // Decision matrix Rm*n (with m indexes and n estimation scales)
    String[] propOfPosNeg;  // Properties of indexes (positive or negative)
    double[][] nonDem;  // The normalized decision matrix
    double[][] frArr ;  // Feature-rate matrix
    double[] entropy ;  // The entropy of m indexes
    double[] varCoef ;  // The diff-coefficient matrix
    double[] weight ;   //Weights matrix


    /**
     * Initialize the decision matrix and give the properties of indexes.
     *
     * @param rawMatrix    Raw decision matrix Rm*n
     * @param propOfPosNeg The symbol (positive or negative) of each index
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
            System.err.println("The number of indexes don't equal to the number of positive and negative indexes");
    }

    /**
     * Normalized the raw decision matrix
     *
     * @return The normalized decision matrix
     */
    private double[][] nondimensionalization() {
    	
    	double[] r_max = new double[rawMatrix[0].length];
    	double[] r_min = new double[rawMatrix[0].length];
    	for(int j=0;j<rawMatrix[0].length;j++) {//n indexes
    		double max = rawMatrix[0][j];
    		double min = rawMatrix[0][j];
    		for (int i = 1; i < rawMatrix.length; i++) {//m eva scales
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

        for (int i = 0; i < rawMatrix.length; i++) {//m scales
            for (int j = 0; j < rawMatrix[0].length; j++) {//n indexes
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
     * Get the feature-rate matrix
     *
     * @return The feature-rate matrix
     */
    private double[][] getFetureRate() {
        nondimensionalization();
        double sum = 0.0;
        for (int i = 0; i < nonDem.length; i++) {// m eva scales
        	for (int j = 0; j < nonDem[i].length; j++) {// n indexes
        		sum+=(nonDem[i][j]+1);
        	}
        }
        for (int i = 0; i < nonDem.length; i++) {//m eva scales
            for (int j = 0; j < nonDem[i].length; j++) {
                frArr[i][j] = (nonDem[i][j] + 1) / sum;
            }
        }
        return frArr;
    }

    /**
     * Compute the entropy of each index
     *
     * @return
     */
    private double[] getEntropy() {
        getFetureRate();
        for (int j = 0; j < frArr[0].length; j++) {//n indexes
        	double sum = 0.0;
        	for (int i = 0; i < frArr.length; i++) {//m eva scales
        		sum += frArr[i][j] * Math.log(frArr[i][j]);
        	}
        	entropy[j] = (-1 / Math.log(frArr.length)) * sum;
        }
        return entropy;
    }

    /**
     * Get the diff-coefficient 1-ei
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
//        	System.out.println("The "+j+"th diff-coeff ="+varCoef[j]);
//        	System.out.println("The sum of diff-coeff = "+MathUtil.sum(varCoef));
            weight[j] = varCoef[j]/MathUtil.sum(varCoef);
        }
        return weight;
    }
}
