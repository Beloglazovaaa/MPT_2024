package task_9;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Array_PI9 {
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

    public static void saveDataToExcel(String tableName, int[][] firstMatrix, int[][] secondMatrix, int[][] resultMatrix, int[][] sumMatrix, int[][] diffMatrix, int[][] powerMatrix) {
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream outputStream = new FileOutputStream("hard_6.xlsx")) {
            Sheet sheet = workbook.createSheet("Data");

            // Header for first matrix
            Row row1 = sheet.createRow(0);
            Cell cell1 = row1.createCell(0);
            cell1.setCellValue("Матрица 1");

            // Empty row after header of first matrix
            sheet.createRow(1);

            // Add first matrix
            for (int i = 0; i < firstMatrix.length; i++) {
                Row dataRow = sheet.createRow(i + 2);
                for (int j = 0; j < firstMatrix[i].length; j++) {
                    dataRow.createCell(j).setCellValue(firstMatrix[i][j]);
                }
            }

            // Empty row after first matrix
            sheet.createRow(firstMatrix.length + 2);

            // Header for second matrix
            Row row2 = sheet.createRow(firstMatrix.length + 3);
            Cell cell2 = row2.createCell(0);
            cell2.setCellValue("Матрица 2");

            // Empty row after header of second matrix
            sheet.createRow(firstMatrix.length + 4);

            // Add second matrix
            for (int i = 0; i < secondMatrix.length; i++) {
                Row dataRow = sheet.createRow(firstMatrix.length + 5 + i);
                for (int j = 0; j < secondMatrix[i].length; j++) {
                    dataRow.createCell(j).setCellValue(secondMatrix[i][j]);
                }
            }

            // Empty row after second matrix
            sheet.createRow(firstMatrix.length + secondMatrix.length + 5);

            // Header for multiplied matrix
            Row row3 = sheet.createRow(firstMatrix.length + secondMatrix.length + 6);
            Cell cell3 = row3.createCell(0);
            cell3.setCellValue("Перемноженная матрица");

            // Empty row after header of multiplied matrix
            sheet.createRow(firstMatrix.length + secondMatrix.length + 7);

            // Add multiplied matrix
            for (int i = 0; i < resultMatrix.length; i++) {
                Row dataRow = sheet.createRow(firstMatrix.length + secondMatrix.length + 8 + i);
                for (int j = 0; j < resultMatrix[i].length; j++) {
                    dataRow.createCell(j).setCellValue(resultMatrix[i][j]);
                }
            }

            // Header for added matrix
            Row row4 = sheet.createRow(firstMatrix.length + secondMatrix.length + resultMatrix.length + 9);
            Cell cell4 = row4.createCell(0);
            cell4.setCellValue("Сложенная матрица");

            // Empty row after header of added matrix
            sheet.createRow(firstMatrix.length + secondMatrix.length + resultMatrix.length + 10);

            // Add added matrix
            for (int i = 0; i < sumMatrix.length; i++) {
                Row dataRow = sheet.createRow(firstMatrix.length + secondMatrix.length + resultMatrix.length + 11 + i);
                for (int j = 0; j < sumMatrix[i].length; j++) {
                    dataRow.createCell(j).setCellValue(sumMatrix[i][j]);
                }
            }

            // Header for subtracted matrix
            Row row5 = sheet.createRow(firstMatrix.length + secondMatrix.length + resultMatrix.length + sumMatrix.length + 12);
            Cell cell5 = row5.createCell(0);
            cell5.setCellValue("Вычитанная матрица");

            // Empty row after header of subtracted matrix
            sheet.createRow(firstMatrix.length + secondMatrix.length + resultMatrix.length + sumMatrix.length + 13);

            // Add subtracted matrix
            for (int i = 0; i < diffMatrix.length; i++) {
                Row dataRow = sheet.createRow(firstMatrix.length + secondMatrix.length + resultMatrix.length + sumMatrix.length + 14 + i);
                for (int j = 0; j < diffMatrix[i].length; j++) {
                    dataRow.createCell(j).setCellValue(diffMatrix[i][j]);
                }
            }

            // Header for powered matrix
            Row row6 = sheet.createRow(firstMatrix.length + secondMatrix.length + resultMatrix.length + sumMatrix.length + diffMatrix.length + 15);
            Cell cell6 = row6.createCell(0);
            cell6.setCellValue("Возведенная в степень матрица");

            // Empty row after header of powered matrix
            sheet.createRow(firstMatrix.length + secondMatrix.length + resultMatrix.length + sumMatrix.length + diffMatrix.length + 16);

            // Add powered matrix
            for (int i = 0; i < powerMatrix.length; i++) {
                Row dataRow = sheet.createRow(firstMatrix.length + secondMatrix.length + resultMatrix.length + sumMatrix.length + diffMatrix.length + 17 + i);
                for (int j = 0; j < powerMatrix[i].length; j++) {
                    dataRow.createCell(j).setCellValue(powerMatrix[i][j]);
                }
            }

            workbook.write(outputStream);
            System.out.println("Данные успешно сохранены в файл hard_6.xlsx");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных в Excel: " + e.getMessage());
        }
    }
}

