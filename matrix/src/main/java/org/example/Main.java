package org.example;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        Excel excel = new Excel();
        excel.create();
        int rows = 8192, columns = 8192, numberOfTests = 100, excelRowIndex = 14;
        while (rows > 1){
            checkMethods(rows, columns, numberOfTests, excel, "First", excelRowIndex);
            checkMethods(rows, columns, numberOfTests, excel, "Second", excelRowIndex+16);
            excelRowIndex--;
            rows = rows/2;
        }
        excel.createGraph("The first type of generation", 6, 2, 16, 25, 2, 14, 0, true);
        excel.createGraph("The second type of generation", 6, 27, 16, 50, 18, 30, 0, true);
        excel.createRatio();
        excel.createGraph("The ratio of two generation types", 6, 52, 16, 75, 34, 46, 0, false);
    }

private static void checkMethods(int rows, int columns, int numberOfTests, Excel excel, String generationWay, int excelRowIndex) throws IOException {
        ArrayList<Long> binaryTimes = new ArrayList<>();
        ArrayList<Long> ladderTimes = new ArrayList<>();
        ArrayList<Long> expTimes = new ArrayList<>();
        Matrix matrix;
        if(generationWay.equals("First")){
            matrix = new Matrix(rows, columns, 2*columns + 1);
            matrix.generateInTheFirstWay();
        } else{
            matrix = new Matrix(rows, columns, 16*columns + 1);
            matrix.generateInTheSecondWay();
        }
        for (int i = 0; i < numberOfTests; ++i) {
            long start = System.nanoTime();
            matrix.binarySearch();
            binaryTimes.add(System.nanoTime() - start);
            start = System.nanoTime();
            matrix.ladderSearch();
            ladderTimes.add(System.nanoTime() - start);
            matrix.ladderExpSearch();
            expTimes.add(System.nanoTime() - start);
        }
        long sumBinaryTimes = 0, sumLadderTimes = 0, sumExpTimes = 0;
        for (int i = 0; i < numberOfTests; ++i) {
            sumBinaryTimes += binaryTimes.get(i);
            sumLadderTimes += ladderTimes.get(i);
            sumExpTimes += expTimes.get(i);
        }
        excel.createRow(sumBinaryTimes / binaryTimes.size(), sumLadderTimes / ladderTimes.size(), sumExpTimes/expTimes.size(), excelRowIndex, rows);
        if(generationWay.equals("First")) {
            System.out.println("The first type of generation for matrix " + rows + "x" + columns + ":");
            printTimes(sumBinaryTimes, sumLadderTimes, sumExpTimes, binaryTimes, ladderTimes, expTimes);
        } else{
            System.out.println("The second type of generation for matrix " + rows + "x" + columns + ":");
            printTimes(sumBinaryTimes, sumLadderTimes, sumExpTimes, binaryTimes, ladderTimes, expTimes);
        }
    }

    private static void printTimes(long sumBinaryTimes, long sumLadderTimes, long sumExpTimes,
                                   ArrayList<Long> binaryTimes, ArrayList<Long> ladderTimes, ArrayList<Long> expTimes) {
        System.out.println("Average running time of the binary method: " + sumBinaryTimes / binaryTimes.size());
        System.out.println("Average running time of the ladder method: " + sumLadderTimes / ladderTimes.size());
        System.out.println("Average running time of the exponential method: " + sumExpTimes / expTimes.size());
    }
}