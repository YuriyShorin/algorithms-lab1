package org.example;

public class Matrix {

    private final int[][] matrix;
    private final int rows;
    private final int columns;
    private final int key;

    Matrix(int rows, int columns, int key) {
        this.rows = rows;
        this.columns = columns;
        this.key = key;
        matrix = new int[rows][columns];
    }

    public void generateInTheFirstWay(){
        for(int i = 0; i < rows; ++i){
            for(int j = 0; j < columns; ++j){
                matrix[i][j] = (columns / rows* i + j) * 2;
            }
        }
    }

    public void generateInTheSecondWay(){
        for(int i = 0; i < rows; ++i){
            for(int j = 0; j < columns; ++j){
                matrix[i][j] = (columns / rows* (i+1) * (j+1)) * 2;
            }
        }
    }

    public boolean ladderSearch() {
        int row = 0, column = columns - 1;
        while (row < rows && column > -1){
            if(matrix[row][column] == key){
                return true;
            }
            else if(matrix[row][column] < key){
                row++;
            } else{
                column--;
            }
        }
        return false;
    }

    public boolean ladderExpSearch(){
        int line = 0, column = columns - 1;
        while (column > -1){
            if(matrix[line][column] == key){
                return true;
            }
            else if(matrix[line][column] > key){
                    column--;
            } else{
                int finish = 1;
                while ((line + finish < rows) && (matrix[line + finish][column] < key)){
                    finish = finish * 2;
                }
                int start = line + finish/2;
                if(line + finish >= rows){
                    finish = rows - 1;
                } else {
                    finish += line;
                }
                if(start == finish){
                    return false;
                }
                while (start < finish){
                    int middle = (start+finish)/2;
                    if(matrix[middle][column] == key){
                        return true;
                    }
                    if(matrix[middle][column] > key){
                        finish = middle - 1;
                    } else{
                        start = middle + 1;
                    }
                }
                line = start;
            }
        }
        return false;
    }





    public boolean binarySearch(){
        for(int i = 0; i < rows; ++i){
            int result = binSearch(i, columns-1);
            if(result != -1){
                return true;
            }
        }
        return false;
    }

    private int binSearch(int row, int right){
        int left = 0;
        while (left <= right){
            int middle = (left+right)/2;
            if(matrix[row][middle] == key){
                return middle;
            }
            if(matrix[row][middle] > key){
                right = middle - 1;
            } else{
                left = middle + 1;
            }
        }
        return -1;
    }

    public void print(){
        System.out.println("key= " + key);
        for(int i = 0; i < rows; ++i){
            for(int j = 0; j < columns; ++j) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
