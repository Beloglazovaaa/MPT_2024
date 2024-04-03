package task_6;

public class MatrixMultiplier {
    private final int[][] firstMatrix;
    private final int[][] secondMatrix;

    public MatrixMultiplier(int[][] firstMatrix, int[][] secondMatrix) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
    }

    public void multiplyAndPrintResult() {
        int[][] resultMatrix = multiplyMatrices(firstMatrix, secondMatrix);
        System.out.println("Результат умножения матриц:");
        printMatrix(resultMatrix);
    }

    public static int[][] multiplyMatrices(int[][] firstMatrix, int[][] secondMatrix) {
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

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
}
