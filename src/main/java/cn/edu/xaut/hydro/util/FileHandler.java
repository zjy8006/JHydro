package cn.edu.xaut.hydro.util;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samantha on 2017/2/12.
 */
public class FileHandler {

    public static String[][] readFromTxt(String path) {
        String[][] result = new String[0][0];
        File file = new File(path);
        List<String> lineList = new ArrayList<String>();
        String regex = "\\s+";

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lineList.add(line.trim().replaceAll(regex, " "));
            }

            int maxColumnLength = lineList.get(0).split(" ").length;
            for (int i = 0; i < lineList.size(); i++) {

                if (maxColumnLength < lineList.get(i).split(" ").length) {
                    maxColumnLength = lineList.get(i).split(" ").length;
                }
            }
            result = new String[lineList.size()][maxColumnLength];

            if (result.length == 0) {
                System.err.println("The Content is NULL!");
            }

            for (int i = 0; i < result.length; i++) {
                String[] arr = lineList.get(i).split(" ");
                for (int j = 0; j < result[0].length; j++)
                    result[i][j] = (arr[j].trim());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }


    /**
     * export String TYpe ArrayLists to excel.
     * Note that
     *
     * @param filePath
     * @param source
     * @return
     */
    public static boolean exportToExcel(String filePath, ArrayList<String>... source) {
        boolean isSuccess = false;
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet0 = wb.createSheet();
        for (int j = 0; j < source[0].size(); j++) {//行数
            HSSFRow row = sheet0.createRow(j);
            for (int i = 0; i < source.length; i++) {//列数
                row.createCell(i).setCellValue(source[i].get(j).trim());
            }
        }
        try {
            FileOutputStream fileOut = new FileOutputStream(filePath);
            wb.write(fileOut);
            wb.close();
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * read strings from excel.
     * Note that each row's columns must be identical in the excel file.
     *
     * @param filePath
     * @return
     */
    public static ArrayList<ArrayList<String>> readFromExcel(String filePath) {
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        try {


            if (filePath.contains(".xlsx")) {
                InputStream is = new FileInputStream(filePath);
                XSSFWorkbook wb = new XSSFWorkbook(is);
                XSSFSheet sheet0 = wb.getSheetAt(0);
                String[][] strings = new String[sheet0.getLastRowNum() + 1][sheet0.getRow(0).getLastCellNum() + 1];
                for (int i = 0; i <= sheet0.getLastRowNum(); i++) {
                    for (int j = 0; j <= sheet0.getRow(i).getLastCellNum(); j++) {
                        strings[i][j] = String.valueOf(sheet0.getRow(i).getCell(j));
                    }
                }
                for (int i = 0; i < strings[0].length; i++) {//列数
                    ArrayList<String> temp = new ArrayList<String>();
                    for (int j = 0; j < strings.length; j++) {//行数
                        temp.add(strings[j][i]);
                    }
                    result.add(temp);
                }
                wb.close();
            } else {
                InputStream is = new FileInputStream(filePath);
                HSSFWorkbook wb = new HSSFWorkbook(is);
                HSSFSheet sheet0 = wb.getSheetAt(0);
                String[][] strings = new String[sheet0.getLastRowNum() + 1][sheet0.getRow(0).getLastCellNum() + 1];
                for (int i = 0; i <= sheet0.getLastRowNum(); i++) {
                    for (int j = 0; j <= sheet0.getRow(i).getLastCellNum(); j++) {
                        strings[i][j] = String.valueOf(sheet0.getRow(i).getCell(j));
                    }
                }
                for (int i = 0; i < strings[0].length; i++) {//列数
                    ArrayList<String> temp = new ArrayList<String>();
                    for (int j = 0; j < strings.length; j++) {//行数
                        temp.add(strings[j][i]);
                    }
                    result.add(temp);
                }
                wb.close();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println(result);
        return result;
    }

    public static ArrayList<ArrayList<Double>> readDoubleFromExcel(String filePath) {
        ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();
        try {


            if (filePath.contains(".xlsx")) {
                InputStream is = new FileInputStream(filePath);
                XSSFWorkbook wb = new XSSFWorkbook(is);
                XSSFSheet sheet0 = wb.getSheetAt(0);
                String[][] strings = new String[sheet0.getLastRowNum() + 1][sheet0.getRow(0).getLastCellNum() + 1];
                for (int i = 0; i <= sheet0.getLastRowNum(); i++) {
                    for (int j = 0; j <= sheet0.getRow(i).getLastCellNum(); j++) {
                        strings[i][j] = String.valueOf(sheet0.getRow(i).getCell(j));
                    }
                }
                for (int i = 0; i < strings[0].length; i++) {//列数
                    ArrayList<Double> temp = new ArrayList<Double>();
                    for (int j = 0; j < strings.length; j++) {//行数
                        if (strings[j][i]!=null){
                            temp.add(Double.valueOf(strings[j][i]));
                        }

                    }
                    result.add(temp);
                }
                wb.close();
            } else {
                InputStream is = new FileInputStream(filePath);
                HSSFWorkbook wb = new HSSFWorkbook(is);
                HSSFSheet sheet0 = wb.getSheetAt(0);
                String[][] strings = new String[sheet0.getLastRowNum() + 1][sheet0.getRow(0).getLastCellNum() + 1];
                for (int i = 0; i <= sheet0.getLastRowNum(); i++) {
                    for (int j = 0; j <= sheet0.getRow(i).getLastCellNum(); j++) {
                        strings[i][j] = String.valueOf(sheet0.getRow(i).getCell(j));
                    }
                }
                for (int i = 0; i < strings[0].length; i++) {//列数
                    ArrayList<Double> temp = new ArrayList<Double>();
                    for (int j = 0; j < strings.length; j++) {//行数
                        temp.add(Double.valueOf(strings[j][i]));
                    }
                    result.add(temp);
                }
                wb.close();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println(result);
        return result;
    }


    public static boolean exportToCSV(String filePath, ArrayList<Double>... dataList) {

        File file = new File(filePath);

        boolean isSucess = false;

        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);

            for (int j = 0; j < dataList[0].size(); j++) {
                for (int i = 0; i < dataList.length; i++) {
                    if (i == dataList.length - 1) {
                        bw.append(String.valueOf(dataList[i].get(j)));
                    } else
                        bw.append(String.valueOf(dataList[i].get(j))).append(",");
                }
                bw.append("\n");

            }
            isSucess = true;
        } catch (Exception e) {
            isSucess = false;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                    bw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                    osw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSucess;
    }


    public static boolean exportToTXT(String filePath, ArrayList<Double>... dataList) {
        File file = new File(filePath);
        boolean isSucess;
        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);

            for (int j = 0; j < dataList[0].size(); j++) {
                for (int i = 0; i < dataList.length; i++) {
                    if (i == dataList.length - 1) {
                        bw.append(String.valueOf(dataList[i].get(j)));
                    } else
                        bw.append(String.valueOf(dataList[i].get(j))).append(" ");
                }
                bw.append("\n");

            }
            isSucess = true;
        } catch (Exception e) {
            isSucess = false;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                    bw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                    osw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSucess;
    }


    /**
     * Export the labels and predictors to txt file.
     * Note that the size of labels and each predictor must be identical.
     *
     * @param filePath The txt file path
     * @param labels   The predicted labels
     * @param dataList The predictors
     * @return return true is success.
     */
    public static boolean exportToTXTForModeling(String filePath, ArrayList<Double> labels, ArrayList<Double>... dataList) {
        File file = new File(filePath);
        boolean isSucess;
        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);

            for (int j = 0; j < dataList[0].size(); j++) {
                for (int i = 0; i < dataList.length; i++) {
                    if (i == dataList.length - 1) {
                        bw.append(String.valueOf(dataList[i].get(j)));
                    } else
                        bw.append(String.valueOf(labels.get(j))).append(" ").append(String.valueOf(dataList[i].get(j))).append(" ");
                }
                bw.append("\n");

            }
            isSucess = true;
        } catch (Exception e) {
            isSucess = false;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                    bw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                    osw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSucess;
    }


    public static void main(String[] args) {
        String[][] test = readFromTxt("data\\housing.txt");
        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < test[0].length; j++) {
                System.out.print(test[i][j] + "   ");
            }
            System.out.println();
        }
    }
}
