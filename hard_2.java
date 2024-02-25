///Сложный вариант. Строковый тип данных. Реализовать программу с интерактивным
//консольным меню, (т.е. вывод списка действий по цифрам. При этом при нажатии на цифру у нас
//должно выполняться определенное действие). Задания полностью идентичны базовому варианту.
//При этом в программе данные пункты должны называться следующим образом:
//1. Вывести все таблицы из MySQL.
//2. Создать таблицу в MySQL.
//3. Ввести две строки с клавиатуры, результат сохранить в MySQL с последующим выводом в
//консоль.
//4. Подсчитать размер ранее введенных строк, результат сохранить в MySQL с последующим
//выводом в консоль.
//5. Объединить две строки в единое целое, результат сохранить в MySQL с последующим выводом
//в консоль.
//6. Сравнить две ранее введенные строки, результат сохранить в MySQL с последующим выводом
//в консоль.
//7. Сохранить все данные (вышеполученные результаты) из MySQL в Excel и вывести на экран.

import java.sql.*;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import java.io.FileOutputStream;
import java.io.IOException;

public class hard_2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Подключение к базе данных
        String url = "jdbc:mysql://localhost:3305/";
        String dbName = "DB";
        String username = "root";
        String password = "root";

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Подключение к базе данных успешно!");
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
            return; // Прерываем выполнение программы, если не удалось подключиться к базе данных
        }

        // Выбор базы данных
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE " + dbName);
        } catch (SQLException e) {
            System.out.println("Ошибка при выборе базы данных: " + e.getMessage());
            return;
        }

        // Создание базы данных, если она не существует
        try {
            Statement statement = connection.createStatement();
            String createDBQuery = "CREATE DATABASE IF NOT EXISTS " + dbName;
            statement.executeUpdate(createDBQuery);
            System.out.println("База данных успешно создана или уже существует.");
        } catch (SQLException e) {
            System.out.println("Ошибка при создании базы данных: " + e.getMessage());
            return;
        }

        url += dbName; // Обновляем URL для подключения к созданной базе данных
        String tableName = "";

        while (running) {
            System.out.println("1. Вывести все таблицы из MySQL.");
            System.out.println("2. Создать таблицу в MySQL.");
            System.out.println("3. Ввести две строки с клавиатуры, результат сохранить в MySQL с последующим выводом в консоль.");
            System.out.println("4. Подсчитать размер ранее введенных строк, результат сохранить в MySQL с последующим выводом в консоль.");
            System.out.println("5. Объединить две строки в единое целое, результат сохранить в MySQL с последующим выводом в консоль.");
            System.out.println("6. Сравнить две ранее введенные строки, результат сохранить в MySQL с последующим выводом в консоль.");
            System.out.println("7. Сохранить все данные (вышеполученные результаты) из MySQL в Excel и вывести на экран.");
            System.out.println("0. Выйти из программы.");
            System.out.println("Выберите действие: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    try {
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SHOW TABLES");
                        System.out.println("Таблицы в базе данных:");
                        while (resultSet.next()) {
                            tableName = resultSet.getString(1);
                            System.out.println(tableName);
                        }
                    } catch (SQLException e) {
                        System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("Введите название таблицы: ");
                    tableName = scanner.next(); // Обновляем tableName здесь

                    // Создаем SQL-запрос для создания таблицы
                    String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName +
                            "(first_str VARCHAR(255), second_str VARCHAR(255), " +
                            "len_1 INT, len_2 INT, combined_str VARCHAR(255), comparison_result VARCHAR(50))";

                    try {
                        Statement statement = connection.createStatement();
                        statement.executeUpdate(createTableQuery);
                        System.out.println("Таблица успешно создана.");
                    } catch (SQLException e) {
                        System.out.println("Ошибка при создании таблицы: " + e.getMessage());
                        break; // Выходим из case 2
                    }
                    break;

                case 3:
                    // Добавляем данные в таблицу
                    try {
                        System.out.println("Введите первую строку: ");
                        String firstStr = scanner.next();
                        System.out.println("Введите вторую строку: ");
                        String secondStr = scanner.next();

                        Statement statement = connection.createStatement();
                        String insertDataQuery = "INSERT INTO " + tableName + " (first_str, second_str) VALUES ('" + firstStr + "', '" + secondStr + "')";
                        statement.executeUpdate(insertDataQuery);
                        System.out.println("Данные успешно добавлены.");
                    } catch (SQLException e) {
                        System.out.println("Ошибка при добавлении данных: " + e.getMessage());
                    }
                    break;

                case 4:
                    try {
                        Statement selectStatement = connection.createStatement();
                        ResultSet resultSet = selectStatement.executeQuery("SELECT * FROM " + tableName);
                        while (resultSet.next()) {
                            String firstStr = resultSet.getString("first_str");
                            String secondStr = resultSet.getString("second_str");
                            int lenFirstStr = firstStr.length();
                            int lenSecondStr = secondStr.length();

                            System.out.println("Длина первой строки: " + lenFirstStr);
                            System.out.println("Длина второй строки: " + lenSecondStr);

                            // Выполняем обновление каждой строки
                            Statement updateStatement = connection.createStatement();
                            String updateQuery = "UPDATE " + tableName + " SET len_1 = " + lenFirstStr + " WHERE first_str = '" + firstStr + "'";
                            updateStatement.executeUpdate(updateQuery);

                            updateQuery = "UPDATE " + tableName + " SET len_2 = " + lenSecondStr + " WHERE second_str = '" + secondStr + "'";
                            updateStatement.executeUpdate(updateQuery);
                        }
                        System.out.println("Результаты успешно сохранены.");
                    } catch (SQLException e) {
                        System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                    }
                    break;


                case 5:
                    try {
                        Statement selectStatement = connection.createStatement();
                        ResultSet resultSet = selectStatement.executeQuery("SELECT * FROM " + tableName);

                        while (resultSet.next()) {
                            String firstStr = resultSet.getString("first_str");
                            String secondStr = resultSet.getString("second_str");
                            String combinedStr = firstStr + secondStr;
                            System.out.println("Объединенная строка: " + combinedStr);

                            Statement updateStatement = connection.createStatement();
                            String updateQuery = "UPDATE " + tableName + " SET combined_str = '" + combinedStr + "' WHERE first_str = '" + firstStr + "'";
                            updateStatement.executeUpdate(updateQuery);
                            updateStatement.close(); // Закрываем statement после использования
                        }
                        selectStatement.close(); // Закрываем statement после использования
                    } catch (SQLException e) {
                        System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                    }
                    break;

                case 6:
                    try {
                        Statement selectStatement = connection.createStatement();
                        ResultSet resultSet = selectStatement.executeQuery("SELECT * FROM " + tableName);

                        while (resultSet.next()) {
                            String firstStr = resultSet.getString("first_str");
                            String secondStr = resultSet.getString("second_str");
                            String comparisonResult = firstStr.equals(secondStr) ? "Строки идентичны" : "Строки не идентичны";
                            System.out.println("Объединенная строка: " + comparisonResult);

                            Statement updateStatement = connection.createStatement();
                            String updateQuery = "UPDATE " + tableName + " SET comparison_result = '" + comparisonResult + "' WHERE first_str = '" + firstStr + "'";
                            updateStatement.executeUpdate(updateQuery);
                            updateStatement.close(); // Закрываем statement после использования
                        }
                        selectStatement.close(); // Закрываем statement после использования
                    } catch (SQLException e) {
                        System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                    }
                    break;

                case 7:
                    try {
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

                        // Создаем новую книгу Excel
                        Workbook workbook = new XSSFWorkbook();
                        Sheet sheet = workbook.createSheet("Data");

                        // Записываем заголовки столбцов
                        Row headerRow = sheet.createRow(0);
                        ResultSetMetaData metaData = resultSet.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnName(i);
                            Cell cell = headerRow.createCell(i - 1);
                            cell.setCellValue(columnName);
                        }

                        // Записываем данные из ResultSet в книгу Excel
                        int rowNum = 1;
                        while (resultSet.next()) {
                            Row row = sheet.createRow(rowNum++);
                            for (int i = 1; i <= columnCount; i++) {
                                Object value = resultSet.getObject(i);
                                Cell cell = row.createCell(i - 1);
                                if (value != null) {
                                    if (value instanceof Number) {
                                        cell.setCellValue(((Number) value).doubleValue());
                                    } else {
                                        cell.setCellValue(value.toString());
                                    }
                                }
                            }
                        }

                        // Сохраняем книгу Excel в файл
                        String excelFileName = "hard_2_excel.xlsx";
                        try (FileOutputStream outputStream = new FileOutputStream(excelFileName)) {
                            workbook.write(outputStream);
                            System.out.println("Данные успешно сохранены в файл " + excelFileName);
                        } catch (IOException e) {
                            System.out.println("Ошибка при сохранении данных в Excel: " + e.getMessage());
                        }
                    } catch (SQLException e) {
                        System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                    }
                    break;

                case 0:
                    running = false;
                    System.out.println("Программа завершена.");
                    break;

                default:
                    System.out.println("Некорректный выбор. Попробуйте снова.");
            }
        }
        scanner.close();
    }
}