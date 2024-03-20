/* Ввести две строки (не менее 50 символов каждая) с клавиатуры.
Необходимо вывести на экран две введенных ранее строки, вернуть подстроку по индексам (substring(),
перевести все строки в верхний и нижний регистры, найти подстроку (тоже вводить с клавиатуры)
и определить: заканчивается ли строка данной подстрокой (endsWith)).

Реализовать программу с интерактивным консольным меню:
1. Вывести все таблицы из MySQL.
2. Создать таблицу в MySQL.
3. Возвращение подстроки по индексам, результат сохранить в MySQL с последующим выводом в
консоль.
4. Перевод строк в верхний и нижний регистры, результат сохранить в MySQL с последующим выводом
в консоль.
5. Поиск подстроки и определение окончания подстроки, результат сохранить в MySQL с последующим
выводом в консоль.
6. Сохранить все данные (вышеполученные результаты) из MySQL в Excel и вывести на экран. */

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class hard_4 {
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

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE " + dbName);
        } catch (SQLException e) {
            System.out.println("Ошибка при выборе базы данных: " + e.getMessage());
            return;
        }

        // Создание базы данных, если ее не существует
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
        String tableName = ""; // Объявляем переменную для хранения имени таблицы

        while (running) {
            System.out.println("1. Вывести все таблицы из MySQL.");
            System.out.println("2. Создать таблицу в MySQL.");
            System.out.println("3. Ввести две строки с клавиатуры, результат сохранить в MySQL с последующим выводом в консоль.");
            System.out.println("4. Перевести все строки в верхний и нижний регистры, результат сохранить в MySQL с последующим выводом в консоль.");
            System.out.println("5. Найти подстроку в каждой строке и определить, заканчивается ли строка данной подстрокой, результат сохранить в MySQL с последующим выводом в консоль.");
            System.out.println("6. Сохранить все данные (вышеполученные результаты) из MySQL в Excel и вывести на экран.");
            System.out.println("0. Выйти из программы.");
            System.out.println("Выберите действие: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

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
                    tableName = scanner.next();

                    // Создаем SQL-запрос для создания таблицы
                    String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName +
                            "(first_str VARCHAR(255), second_str VARCHAR(255), " +
                            "upper_first_str VARCHAR(255), lower_first_str VARCHAR(255), " +
                            "upper_second_str VARCHAR(255), lower_second_str VARCHAR(255), " +
                            "ends_with_first VARCHAR(3), ends_with_second VARCHAR(3))";

                    try {
                        Statement statement = connection.createStatement();
                        statement.executeUpdate(createTableQuery);
                        System.out.println("Таблица успешно создана.");
                    } catch (SQLException e) {
                        System.out.println("Ошибка при создании таблицы: " + e.getMessage());
                        break; //
                    }
                    break;

                case 3:
                    if (tableName.isEmpty()) {
                        System.out.println("Сначала необходимо создать таблицу.");
                    } else {
                        try {
                            String firstStr = "";
                            String secondStr = "";

                            // Ввод первой строки
                            do {
                                System.out.println("Введите первую строку (минимум 50 символов): ");
                                firstStr = scanner.nextLine();
                            } while (firstStr.length() < 50);

                            // Ввод второй строки
                            do {
                                System.out.println("Введите вторую строку (минимум 50 символов): ");
                                secondStr = scanner.nextLine();
                            } while (secondStr.length() < 50);

                            // Перевод всех строк в верхний и нижний регистры
                            String upperFirstStr = firstStr.toUpperCase();
                            String lowerFirstStr = firstStr.toLowerCase();
                            String upperSecondStr = secondStr.toUpperCase();
                            String lowerSecondStr = secondStr.toLowerCase();

                            // Проверка, заканчивается ли первая строка данной подстрокой
                            System.out.println("Введите подстроку для проверки в первой строке: ");
                            String searchSubstringFirst = scanner.nextLine();
                            boolean endsWithStringFirst = firstStr.endsWith(searchSubstringFirst);

                            // Проверка, заканчивается ли вторая строка данной подстрокой
                            System.out.println("Введите подстроку для проверки во второй строке: ");
                            String searchSubstringSecond = scanner.nextLine();
                            boolean endsWithStringSecond = secondStr.endsWith(searchSubstringSecond);

                            // Сохранение введенных данных в MySQL
                            Statement statement = connection.createStatement();
                            String insertDataQuery = "INSERT INTO " + tableName + " (first_str, second_str, " +
                                    "upper_first_str, lower_first_str, upper_second_str, lower_second_str, " +
                                    "ends_with_first, ends_with_second) " +
                                    "VALUES ('" + firstStr + "', '" + secondStr + "', '" +
                                    upperFirstStr + "', '" + lowerFirstStr + "', '" +
                                    upperSecondStr + "', '" + lowerSecondStr + "', '" +
                                    (endsWithStringFirst ? "да" : "нет") + "', '" +
                                    (endsWithStringSecond ? "да" : "нет") + "')";
                            statement.executeUpdate(insertDataQuery);
                            System.out.println("Данные успешно добавлены.");

                        } catch (SQLException e) {
                            System.out.println("Ошибка при добавлении данных: " + e.getMessage());
                        }
                    }
                    break;

                case 4:
                    // Проверяем, выбрана ли таблица
                    if (tableName.isEmpty()) {
                        System.out.println("Сначала необходимо выбрать таблицу.");
                    } else {
                        try {
                            // Создаем новый Statement для выполнения обновления данных
                            Statement updateStatement = connection.createStatement();

                            // Получаем данные из таблицы
                            Statement selectStatement = connection.createStatement();
                            ResultSet resultSet = selectStatement.executeQuery("SELECT * FROM " + tableName);

                            // Обновляем каждую запись, переводя строки в верхний и нижний регистры
                            while (resultSet.next()) {
                                String firstStr = resultSet.getString("first_str");
                                String secondStr = resultSet.getString("second_str");

                                // Перевод всех строк в верхний и нижний регистры
                                String upperFirstStr = firstStr.toUpperCase();
                                String lowerFirstStr = firstStr.toLowerCase();
                                String upperSecondStr = secondStr.toUpperCase();
                                String lowerSecondStr = secondStr.toLowerCase();

                                // Обновление записи в таблице с новыми данными
                                String updateQuery = "UPDATE " + tableName + " SET " +
                                        "upper_first_str='" + upperFirstStr + "', " +
                                        "lower_first_str='" + lowerFirstStr + "', " +
                                        "upper_second_str='" + upperSecondStr + "', " +
                                        "lower_second_str='" + lowerSecondStr + "' WHERE first_str='" + firstStr + "' AND second_str='" + secondStr + "'";
                                updateStatement.executeUpdate(updateQuery);

                                // Выводим строки в верхнем и нижнем регистре
                                System.out.println("Первая строка в верхнем регистре: " + upperFirstStr);
                                System.out.println("Первая строка в нижнем регистре: " + lowerFirstStr);
                                System.out.println("Вторая строка в верхнем регистре: " + upperSecondStr);
                                System.out.println("Вторая строка в нижнем регистре: " + lowerSecondStr);
                            }

                            // Закрываем Statement после выполнения обновления данных
                            updateStatement.close();

                            // Закрываем ResultSet после обработки данных
                            resultSet.close();

                            System.out.println("Все строки успешно обновлены.");
                        } catch (SQLException e) {
                            System.out.println("Ошибка при обновлении данных: " + e.getMessage());
                        }
                    }
                    break;

                case 5:
                    // Проверяем, выбрана ли таблица
                    if (tableName.isEmpty()) {
                        System.out.println("Сначала необходимо выбрать таблицу.");
                    } else {
                        try {
                            // Получаем данные из таблицы
                            Statement statement = connection.createStatement();
                            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

                            // Обрабатываем каждую запись, проверяя, заканчивается ли строка подстрокой
                            while (resultSet.next()) {
                                String firstStr = resultSet.getString("first_str");
                                String secondStr = resultSet.getString("second_str");

                                // Проверка, заканчивается ли первая строка данной подстрокой
                                String endsWithStringFirst = resultSet.getString("ends_with_first");

                                // Проверка, заканчивается ли вторая строка данной подстрокой
                                String endsWithStringSecond = resultSet.getString("ends_with_second");

                                System.out.println("Первая строка заканчивается на подстроку: " + endsWithStringFirst);
                                System.out.println("Вторая строка заканчивается на подстроку: " + endsWithStringSecond);
                            }
                        } catch (SQLException e) {
                            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                        }
                    }
                    break;

                case 6:
                    // Сохранение данных из MySQL в Excel
                    try {
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

                        Workbook workbook = new XSSFWorkbook();
                        Sheet sheet = workbook.createSheet("Data");

                        // Названия столбцов
                        Row headerRow = sheet.createRow(0);
                        headerRow.createCell(0).setCellValue("Первая строка");
                        headerRow.createCell(1).setCellValue("Вторая строка");
                        headerRow.createCell(2).setCellValue("Верхний регистр первой строки");
                        headerRow.createCell(3).setCellValue("Нижний регистр первой строки");
                        headerRow.createCell(4).setCellValue("Верхний регистр второй строки");
                        headerRow.createCell(5).setCellValue("Нижний регистр второй строки");
                        headerRow.createCell(6).setCellValue("Заканчивается ли первая строка подстрокой");
                        headerRow.createCell(7).setCellValue("Заканчивается ли вторая строка подстрокой");

                        int rowNum = 1;
                        while (resultSet.next()) {
                            Row row = sheet.createRow(rowNum++);
                            row.createCell(0).setCellValue(resultSet.getString("first_str"));
                            row.createCell(1).setCellValue(resultSet.getString("second_str"));
                            row.createCell(2).setCellValue(resultSet.getString("upper_first_str"));
                            row.createCell(3).setCellValue(resultSet.getString("lower_first_str"));
                            row.createCell(4).setCellValue(resultSet.getString("upper_second_str"));
                            row.createCell(5).setCellValue(resultSet.getString("lower_second_str"));
                            row.createCell(6).setCellValue(resultSet.getString("ends_with_first"));
                            row.createCell(7).setCellValue(resultSet.getString("ends_with_second"));
                        }

                        FileOutputStream outputStream = new FileOutputStream("hard_4.xlsx");
                        workbook.write(outputStream);
                        workbook.close();
                        outputStream.close();

                        System.out.println("Данные успешно сохранены в Excel файл.");
                    } catch (SQLException | IOException e) {
                        System.out.println("Ошибка при сохранении данных в Excel: " + e.getMessage());
                    }
                    break;

                case 0:
                    running = false;
                    System.out.println("Программа завершена.");
                    break;

                default:
                    System.out.println("Некорректный ввод. Пожалуйста, попробуйте еще раз.");
            }
        }

        scanner.close();
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при закрытии подключения: " + e.getMessage());
        }
    }
}

