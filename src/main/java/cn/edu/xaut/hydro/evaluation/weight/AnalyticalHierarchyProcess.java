package cn.edu.xaut.hydro.evaluation.weight;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * @author Jianyi Zuo
 * Compute weights using Analytical Hierarchy Process (AHP).
 * Step 1. Construct hierarchy structure: target layer; index layer ; scheme layer;
 * Step 2. Construct judgement matrix using the following comparement rules:
 *  1 : equally important
 *  3 : slightly important
 *  5 : Obviously important
 *  7 : highly important
 *  9 : extremely important
 * Step 3: COmpute the weight matrix and check consistence.
 * Step 4: Compute the score of each scheme.
 */
public class AnalyticalHierarchyProcess {

    private INDArray judgeMatrix;

    /**
     * New AnalyticalHierarchyProcess object
     * @param judge2DArray The 2-D float judgement array
     */
    public AnalyticalHierarchyProcess(float[][] judge2DArray) {
        this.judgeMatrix = Nd4j.create(judge2DArray);
    }

    /**
     * Normalize the judgement matrix to compute weights
     * @param judgeMatrix The judgement matrix evaluated by experts.
     * @return The normalized judgement matrix, which is the weights.
     */
    private INDArray normJudgeMatrix(INDArray judgeMatrix){
        INDArray colSum = (Nd4j.sum(judgeMatrix,0)).broadcast(new int[]{judgeMatrix.rows(),judgeMatrix.columns()});
        INDArray colNorm = judgeMatrix.div(colSum);
        INDArray normJudgeMatrix = Nd4j.sum(colNorm,1).div(judgeMatrix.columns());
        return normJudgeMatrix;
    }

    /**
     * Compute lambda from A*omega=lambda*omega (where A is the judgement matrix, omega is the weight vector).
     * @param judgeMatrix The judgement matrix evaluated by experts.
     * @return The lambda.
     */
    private float computeLambda(INDArray judgeMatrix){
        INDArray A_Omega = judgeMatrix.mmul(normJudgeMatrix(judgeMatrix));
        INDArray lambda = Nd4j.sum(A_Omega.div(normJudgeMatrix(judgeMatrix))).div(judgeMatrix.columns());
        return lambda.getFloat(0,0);
    }

    /**
     * Compute the consistency index from ci = (lambda-n)/(n-1) (where n is the number of index).
     * @param judgeMatrix The judgement matrix evaluated by experts.
     * @return The consistency index.
     */
    private float computeConsistencyIndex(INDArray judgeMatrix){
        float lambda = computeLambda(judgeMatrix);
        float CI = (lambda-judgeMatrix.columns())/(judgeMatrix.columns()-1);
        return CI;
    }

    /**
     * Compute the random consistency index by evaluate random sampled judgement matrix.
     * The values of random sampled judgement matrix must be belong to the following float array:
     *      [1.0f,2.0f,3.0f,4.0f,5.0f,6.0f,8.0f,9.0f,1/2.0f,1/3.0f,1/4.0f,1/5.0f,1/6.0f,1/7.0f,1/8.0f,1/9.0f]
     * The default number of evaluation is 2000. The random consistency index is computed from
     *      $RCI=\sum_{n=0}^{n=1999}/2000$
     * @param judgeMatrix
     * @return
     */
    private float computeRandomConsistencyIndex(INDArray judgeMatrix){
        float[] P = new float[]{1.0f,2.0f,3.0f,4.0f,5.0f,6.0f,8.0f,9.0f,1/2.0f,1/3.0f,1/4.0f,1/5.0f,1/6.0f,1/7.0f,1/8.0f,1/9.0f};
        int L = P.length;
        float[][] rawJudgeMetrix = new float[judgeMatrix.rows()][judgeMatrix.columns()];
        int num = 2000;
        float R = 0.0f;
        for (int kp = 0; kp <num ; kp++) {
            for (int i = 0; i < judgeMatrix.columns()-1; i++) {
                for (int j = i+1; j < judgeMatrix.columns(); j++) {
                    int k = (int)Math.floor(L*Math.random());
                    rawJudgeMetrix[i][j] = P[k];
                    rawJudgeMetrix[j][i] = 1/P[k];
                }
            }
            R+=computeConsistencyIndex(Nd4j.create(rawJudgeMetrix));
        }
        float RCI = R/num;
        return RCI;
    }

    /**
     * Compute the consistency rate from CR = CI/RCI.
     * @return The consistency rate.
     */
    private float computeConsistencyRate(){
        return computeConsistencyIndex(judgeMatrix)/computeRandomConsistencyIndex(judgeMatrix);
    }

    /**
     * Compute the weight by @normJudgeMatrix and perform consistency check before return weights.
     * @return The weights.
     */
    public INDArray weights(){
        INDArray weights = normJudgeMatrix(judgeMatrix);
        float CR = computeConsistencyRate();
        if (CR < 0.1){
            System.out.println("Pass the consistency check");
        }else{
            System.err.println("Failed the consistency check!");
            System.exit(0);
        }
        return weights;
    }

    /**
     * Compute the scheme score from the index weights and scheme weights.
     * @param indexWeight The weights from index to target.
     * @param schemeWeights The weights from scheme to index.
     * @return The scheme score.
     */
    public static INDArray schemeScore(INDArray indexWeight, INDArray ... schemeWeights){
        if (indexWeight.rows() != schemeWeights.length){
            System.err.println("Expect "+indexWeight.rows()+" schemeWeights, given "+schemeWeights.length);
            System.exit(0);
        }
        INDArray indWeights = indexWeight.reshape(1,indexWeight.rows());
        INDArray sWeights = schemeWeights[0].reshape(1,schemeWeights[0].rows());
        for (int i = 1; i < schemeWeights.length; i++) {
            sWeights = Nd4j.concat(0,sWeights,schemeWeights[i].reshape(1,schemeWeights[i].rows()));
        }
        INDArray schemeScore = indWeights.mmul(sWeights);
        return schemeScore;
    }

}
