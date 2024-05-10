package task_9;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.apache.poi.ss.util.CellRangeAddress;

import java.sql.SQLException;

public class ArrayPI_9 {
    public static int[][] readMatrixFromInput(Scanner scanner, int rows, int columns) {
        int[][] matrix = new int[rows][columns];
        System.out.println("Введите элементы матрицы (через пробел каждое число в строке):");
        for (int i = 0; i < rows; i++) {
            System.out.println("Введите " + (i + 1) + " строку матрицы:");
            String[] rowValues = scanner.nextLine().trim().split("\\s+");
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = Integer.parseInt(rowValues[j]);
            }
        }
        return matrix;
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }


    public static void saveMatrixToDatabase(Connection connection, int[][] matrix, String tableName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS " + tableName);
            String createTableQuery = "CREATE TABLE " + tableName + " (id INT AUTO_INCREMENT PRIMARY KEY, value INT)";
            statement.executeUpdate(createTableQuery);

            for (int[] row : matrix) {
                for (int value : row) {
                    statement.executeUpdate("INSERT INTO " + tableName + " (value) VALUES (" + value + ")");
                }
            }

            System.out.println("Результаты успешно сохранены в базе данных.");
        } catch (SQLException e) {
            System.out.println("Ошибка при сохранении результатов в базу данных: " + e.getMessage());
        }
    }

    public static void saveDataToExcel(String tableName, int[][] firstMatrix, int[][] secondMatrix, int[][] productMatrix, int[][] sumMatrix, int[][] differenceMatrix, int[][] powerMatrix1, int[][] powerMatrix2) {
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream outputStream = new FileOutputStream("hard_9.xlsx")) {

            // Лист для первой матрицы
            // Лист для первой матрицы
            Sheet sheet1 = workbook.createSheet("Матрица 1");
            if (firstMatrix != null) {
                saveMatrixToSheet(sheet1, firstMatrix);
            } else {
                System.out.println("Первая матрица отсутствует");
            }

// Лист для второй матрицы
            Sheet sheet2 = workbook.createSheet("Матрица 2");
            if (secondMatrix != null) {
                saveMatrixToSheet(sheet2, secondMatrix);
            } else {
                System.out.println("Вторая матрица отсутствует");
            }

// Лист для умноженной матрицы
            Sheet sheet3 = workbook.createSheet("Перемноженная матрица");
            if (productMatrix != null) {
                saveMatrixToSheet(sheet3, productMatrix);
            } else {
                System.out.println("Умноженная матрица отсутствует");
            }

// Лист для сложенной матрицы
            Sheet sheet4 = workbook.createSheet("Сложенная матрица");
            if (sumMatrix != null) {
                saveMatrixToSheet(sheet4, sumMatrix);
            } else {
                System.out.println("Сложенная матрица отсутствует");
            }

// Лист для вычтенной матрицы
            Sheet sheet5 = workbook.createSheet("Вычтенная матрица");
            if (differenceMatrix != null) {
                saveMatrixToSheet(sheet5, differenceMatrix);
            } else {
                System.out.println("Вычтенная матрица отсутствует");
            }

// Лист для первой матрицы, возведенной в степень
            Sheet sheet6 = workbook.createSheet("Матрица 1 в степени");
            if (powerMatrix1 != null) {
                saveMatrixToSheet(sheet6, powerMatrix1);
            } else {
                System.out.println("Первая матрица в степени отсутствует");
            }

// Лист для второй матрицы, возведенной в степень
            Sheet sheet7 = workbook.createSheet("Матрица 2 в степени");
            if (powerMatrix2 != null) {
                saveMatrixToSheet(sheet7, powerMatrix2);
            } else {
                System.out.println("Вторая матрица в степени отсутствует");
            }


            workbook.write(outputStream);
            System.out.println("Данные успешно сохранены в файл hard_9.xlsx");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных в Excel: " + e.getMessage());
        }
    }

    private static void saveMatrixToSheet(Sheet sheet, int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            Row row = sheet.createRow(i);
            for (int j = 0; j < matrix[i].length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(matrix[i][j]);
            }
        }
    }

}
