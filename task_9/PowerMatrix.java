package task_9;

import java.sql.Connection;

public final class PowerMatrix extends Array_PI9 {
    private final int[][] matrix;
    private final int power;

    public PowerMatrix(int[][] matrix, int power, String tableName, Connection connection) {
        super();
        this.matrix = matrix;
        this.power = power;

        // Получаем результат возведения матрицы в степень
        int[][] resultMatrix = getResultMatrix();

        // Выводим результат в консоль и сохраняем его в базе данных и Excel
        printMatrix(resultMatrix);
        saveMatrixToDatabase(resultMatrix, tableName, connection);
        saveDataToExcel(tableName, matrix, resultMatrix, resultMatrix);
    }

    @Override
    public int[][] getResultMatrix() {
        // Возводим матрицу в степень и возвращаем результат
        return powerMatrix(matrix, power);
    }

    private int[][] powerMatrix(int[][] matrix, int power) {
        int n = matrix.length;
        int[][] result = new int[n][n];
        // Инициализируем результат как единичную матрицу
        for (int i = 0; i < n; i++) {
            result[i][i] = 1;
        }
        // Возводим матрицу в указанную степень
        for (int p = 0; p < power; p++) {
            result = multiplyMatrices(result, matrix);
        }
        return result;
    }

    private int[][] multiplyMatrices(int[][] firstMatrix, int[][] secondMatrix) {
        int rows1 = firstMatrix.length;
        int columns1 = firstMatrix[0].length;
        int columns2 = secondMatrix[0].length;

        int[][] resultMatrix = new int[rows1][columns2];

        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < columns2; j++) {
                for (int k = 0; k < columns1; k++) {
                    resultMatrix[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }

        return resultMatrix;
    }
}
