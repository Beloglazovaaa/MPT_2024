package task_9;

import java.sql.Connection;

public final class SubtractMatrices extends Array_PI9 {
    private final int[][] firstMatrix;
    private final int[][] secondMatrix;

    public SubtractMatrices(int[][] firstMatrix, int[][] secondMatrix, String tableName, Connection connection) {
        super();
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;

        // Получаем результат вычитания матриц
        int[][] resultMatrix = getResultMatrix();

        // Выводим результат в консоль и сохраняем его в базе данных и Excel
        printMatrix(resultMatrix);
        saveMatrixToDatabase(resultMatrix, tableName, connection);
        saveDataToExcel(tableName, firstMatrix, secondMatrix, resultMatrix);
    }

    @Override
    public int[][] getResultMatrix() {
        // Вычитаем матрицы и возвращаем результат
        return subtractMatrices(firstMatrix, secondMatrix);
    }

    private int[][] subtractMatrices(int[][] matrix1, int[][] matrix2) {
        int[][] result = new int[matrix1.length][matrix1[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                result[i][j] = matrix1[i][j] - matrix2[i][j];
            }
        }
        return result;
    }
}
