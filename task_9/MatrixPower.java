package task_9;

public final class MatrixPower {
    private final int[][] matrix;
    private final int power;

    public MatrixPower(int[][] matrix, int power) {
        this.matrix = matrix;
        this.power = power;
    }

    public void powerAndPrintResult() {
        int[][] resultMatrix = powerMatrix(matrix, power);
        System.out.println("Результат возведения матрицы в степень " + power + ":");
        MatrixMultiplier.printMatrix(resultMatrix);
    }

    public static int[][] powerMatrix(int[][] matrix, int power) {
        if (power == 0) {
            int size = matrix.length;
            int[][] resultMatrix = new int[size][size];
            for (int i = 0; i < size; i++) {
                resultMatrix[i][i] = 1; // Identity matrix
            }
            return resultMatrix;
        } else if (power == 1) {
            return matrix;
        } else {
            int[][] tempMatrix = powerMatrix(matrix, power / 2);
            if (power % 2 == 0) {
                return MatrixMultiplier.multiplyMatrices(tempMatrix, tempMatrix);
            } else {
                return MatrixMultiplier.multiplyMatrices(MatrixMultiplier.multiplyMatrices(tempMatrix, tempMatrix), matrix);
            }
        }
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
}
