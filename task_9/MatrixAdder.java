package task_9;

public final class MatrixAdder {
    private final int[][] firstMatrix;
    private final int[][] secondMatrix;

    public MatrixAdder(int[][] firstMatrix, int[][] secondMatrix) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
    }

    public void addAndPrintResult() {
        int[][] resultMatrix = addMatrices(firstMatrix, secondMatrix);
        System.out.println("Результат сложения матриц:");
        MatrixMultiplier.printMatrix(resultMatrix);
    }

    public int[][] getResultMatrix() {
        return addMatrices(firstMatrix, secondMatrix);
    }

    public static int[][] addMatrices(int[][] firstMatrix, int[][] secondMatrix) {
        int rows = firstMatrix.length;
        int columns = firstMatrix[0].length;
        int[][] resultMatrix = new int[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                resultMatrix[i][j] = firstMatrix[i][j] + secondMatrix[i][j];
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
