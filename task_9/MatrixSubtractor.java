package task_9;

public final class MatrixSubtractor {
    private final int[][] firstMatrix;
    private final int[][] secondMatrix;

    public MatrixSubtractor(int[][] firstMatrix, int[][] secondMatrix) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
    }

    public void subtractAndPrintResult() {
        int[][] resultMatrix = subtractMatrices(firstMatrix, secondMatrix);
        System.out.println("Результат вычитания матриц:");
        MatrixMultiplier.printMatrix(resultMatrix);
    }

    public int[][] getResultMatrix() {
        return subtractMatrices(firstMatrix, secondMatrix);
    }
    
    public static int[][] subtractMatrices(int[][] firstMatrix, int[][] secondMatrix) {
        int rows = firstMatrix.length;
        int columns = firstMatrix[0].length;
        int[][] resultMatrix = new int[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                resultMatrix[i][j] = firstMatrix[i][j] - secondMatrix[i][j];
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

