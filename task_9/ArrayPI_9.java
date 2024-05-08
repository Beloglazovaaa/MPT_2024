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

    public static void saveDataToExcel(String tableName, int[][] firstMatrix, int[][] secondMatrix, int[][] sumMatrix,
                                       int[][] differenceMatrix, int[][] productMatrix, int[][] powerMatrix1, int[][] powerMatrix2) {
        try (Workbook workbook = new XSSFWorkbook()) {
            saveMatrixToSheet(workbook, "Матрица 1", firstMatrix);
            saveMatrixToSheet(workbook, "Матрица 2", secondMatrix);
            saveMatrixToSheet(workbook, "Сложение", sumMatrix);
            saveMatrixToSheet(workbook, "Вычитание", differenceMatrix);
            saveMatrixToSheet(workbook, "Умножение", productMatrix);
            saveMatrixToSheet(workbook, "Возведение в степень 1", powerMatrix1);
            saveMatrixToSheet(workbook, "Возведение в степень 2", powerMatrix2);

            try (FileOutputStream outputStream = new FileOutputStream(tableName + ".xlsx")) {
                workbook.write(outputStream);
                System.out.println("Данные успешно сохранены в файл " + tableName + ".xlsx");
            } catch (IOException e) {
                System.out.println("Ошибка при сохранении данных в Excel: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Ошибка при создании книги Excel: " + e.getMessage());
        }
    }

    private static void saveMatrixToSheet(Workbook workbook, String sheetName, int[][] matrix) {
        Sheet sheet = workbook.createSheet(sheetName);

        // Создаем строку для ID и описания
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Описание");

        // Создаем строку для записи матрицы
        Row matrixRow = sheet.createRow(1);
        // Преобразуем матрицу в строку и записываем в ячейку
        StringBuilder matrixAsString = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrixAsString.append(matrix[i][j]).append(" ");
            }
            matrixAsString.append("\n"); // Переходим на новую строку
        }
        // Записываем матрицу в ячейку, предварительно настроив форматирование
        Cell matrixCell = matrixRow.createCell(2);
        matrixCell.setCellValue(matrixAsString.toString());
        sheet.autoSizeColumn(2); // Растягиваем столбец для матрицы
    }
}
