package hard_15;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class ExportToExcel {
    public static void exportDataToExcel(String tableName, Connection connection) {
        String query = "SELECT * FROM " + tableName;
        String excelFilePath = "hard_15.xlsx";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query);
             Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet(tableName);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Создание заголовков столбцов
            Row headerRow = sheet.createRow(0);
            for (int i = 1; i <= columnCount; i++) {
                Cell cell = headerRow.createCell(i - 1);
                cell.setCellValue(metaData.getColumnName(i));
            }

            // Заполнение данных
            int rowCount = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowCount++);
                for (int i = 1; i <= columnCount; i++) {
                    Cell cell = row.createCell(i - 1);
                    cell.setCellValue(rs.getString(i));
                }
            }

            // Автоизменение размера столбцов
            for (int i = 0; i < columnCount; i++) {
                sheet.autoSizeColumn(i);
            }

            // Запись данных в файл Excel
            try (FileOutputStream fos = new FileOutputStream(excelFilePath)) {
                workbook.write(fos);
            }

            System.out.println("Данные успешно экспортированы в файл: " + excelFilePath);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка при экспорте данных в файл Excel.");
        }
    }

    public static void displayDataInConsole(String tableName, Connection connection) {
        String query = "SELECT * FROM " + tableName;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Вывод заголовков столбцов
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println();

            // Вывод данных
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка при выводе данных из базы данных.");
        }
    }
}
