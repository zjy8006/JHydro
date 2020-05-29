package cn.edu.xaut.hydro.util;

/**
 * Created by Samantha on 2017/2/13.
 */
public class PrintUtil {

    public static void print2DArray(double[][] arr) {
        System.out.println("\n\n");
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j]+"    ");
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }

    public static void print2DArray(float[][] arr) {
        System.out.println("\n\n");
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j]+"    ");
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }

    public static void print2DArray(String[][] arr){
        System.out.println("\n\n");
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j]+"    ");
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }

    public static void print2DArray(int[][] arr){
        System.out.println("\n\n");
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j]+"    ");
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }

    public static void print2DArray(long[][] arr){
        System.out.println("\n\n");
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j]+"    ");
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }

    public static void printArray(double[] arr) {
        System.out.println("==================");
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
        System.out.println("==================");
    }
}
