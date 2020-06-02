package cn.edu.xaut.hydro.evaluation;

import cn.edu.xaut.hydro.evaluation.weight.AnalyticalHierarchyProcess;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;

public class TestAnalyticalHierarchyProcess {
    @Test
    public void ahpWeightTest(){
        /**
         * Using AHP to evaluate a travel scheme.
         * Target: select a travel scheme.
         * Index: View, Cost, Living condition, Food, Traffic
         * Scheme: Suhang(苏杭), Beidaihe(北戴河)， Guilin(桂林)
         */
//        The judgement matrix from index layer to target layer
        float[][] judge2DArray = new float[][]{
                {1.0f,1/2.0f,4.0f,3.0f,3.0f},
                {2.0f,1.0f,7.0f,5.0f,5.0f},
                {1/4.0f,1/7.0f,1.0f,1/2.0f,1/3.0f},
                {1/3.0f,1/5.0f,2.0f,1.0f,1.0f},
                {1/3.0f,1/5.0f,3.0f,1.0f,1.0f},
        };

//        The judgement matrix from scheme to index layer
        float[][] judge2DArray1 = new float[][]{//View
                {1.0f,2.0f,5.0f},
                {1/2.0f,1.0f,2.0f},
                {1/5.0f,1/2.0f,1.0f},
        };

        float[][] judge2DArray2 = new float[][]{//Cost
                {1.0f,1/3.0f,1/8.0f},
                {3.0f,1.0f,1/3.0f},
                {8.0f,3.0f,1.0f},
        };

        float[][] judge2DArray3 = new float[][]{//Living condition
                {1.0f,1.0f,3.0f},
                {1.0f,1.0f,3.0f},
                {1/3.0f,1/3.0f,1.0f},
        };

        float[][] judge2DArray4 = new float[][]{//Food
                {1.0f,3.0f,4.0f},
                {1/3.0f,1.0f,1.0f},
                {1/4.0f,1.0f,1.0f},
        };

        float[][] judge2DArray5 = new float[][]{//Traffic
                {1.0f,1.0f,1/4.0f},
                {1.0f,1.0f,1/4.0f},
                {4.0f,4.0f,1.0f},
        };


//        Compute the weight from the index to target
        INDArray w = new AnalyticalHierarchyProcess(judge2DArray).weights();

//        Compute the weight from the scheme to index
        INDArray w1 = new AnalyticalHierarchyProcess(judge2DArray1).weights();
        INDArray w2 = new AnalyticalHierarchyProcess(judge2DArray2).weights();
        INDArray w3 = new AnalyticalHierarchyProcess(judge2DArray3).weights();
        INDArray w4 = new AnalyticalHierarchyProcess(judge2DArray4).weights();
        INDArray w5 = new AnalyticalHierarchyProcess(judge2DArray5).weights();

        System.out.println("w = \n"+w);
        System.out.println("w1 = \n"+w1);
        System.out.println("w2 = \n"+w2);
        System.out.println("w3 = \n"+w3);
        System.out.println("w4 = \n"+w4);
        System.out.println("w5 = \n"+w5);

        INDArray schemeScore = AnalyticalHierarchyProcess.schemeScore(w,w1,w2,w3,w4,w5);



    }
}
