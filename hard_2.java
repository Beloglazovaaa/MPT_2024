/* Строковый тип данных. Реализовать программу с интерактивным консольным меню.

1. Вывести все таблицы из MySQL.
2. Создать таблицу в MySQL.
3. Ввести две строки с клавиатуры, результат сохранить в MySQL с последующим выводом в консоль.
4. Подсчитать размер ранее введенных строк, результат сохранить в MySQL с последующим выводом в консоль.
5. Объединить две строки в единое целое, результат сохранить в MySQL с последующим выводом в консоль.
6. Сравнить две ранее введенные строки, результат сохранить в MySQL с последующим выводом в консоль.
7. Сохранить все данные (вышеполученные результаты) из MySQL в Excel и вывести на экран. */

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

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
            System.out.println("4. Подсчитать размер ранее введенных строк, результат сохранить в MySQL с последующим выводом в консоль.");
            System.out.println("5. Объединить две строки в единое целое, результат сохранить в MySQL с последующим выводом в консоль.");
            System.out.println("6. Сравнить две ранее введенные строки, результат сохранить в MySQL с последующим выводом в консоль.");
            System.out.println("7. Сохранить все данные (вышеполученные результаты) из MySQL в Excel и вывести на экран.");
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
                            "len_1 INT, len_2 INT, combined_str VARCHAR(255), comparison_result VARCHAR(50))";

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

                            // Проверка и ввод первой строки
                            do {
                                System.out.println("Введите первую строку (минимум 50 символов): ");
                                firstStr = scanner.nextLine();
                            } while (firstStr.length() < 50);

                            // Проверка и ввод второй строки
                            do {
                                System.out.println("Введите вторую строку (минимум 50 символов): ");
                                secondStr = scanner.nextLine();
                            } while (secondStr.length() < 50);

                            // Сохранение введенных строк в MySQL
                            Statement statement = connection.createStatement();
                            String insertDataQuery = "INSERT INTO " + tableName + " (first_str, second_str) VALUES ('" + firstStr + "', '" + secondStr + "')";
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
                            // Проверяем, есть ли данные в таблице
                            Statement countStatement = connection.createStatement();
                            ResultSet countResultSet = countStatement.executeQuery("SELECT COUNT(*) AS count FROM " + tableName);
                            countResultSet.next();
                            int rowCount = countResultSet.getInt("count");
                            countResultSet.close();
                            countStatement.close();

                            if (rowCount == 0) {
                                System.out.println("В таблице отсутствуют данные. Сначала введите строки для выполнения операции.");
                            } else {
                                // Если данные есть, выполняем операцию
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
                            }
                        } catch (SQLException e) {
                            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                        }
                    }
                    break;


                case 5:
                    if (tableName.isEmpty()) {
                        System.out.println("Сначала необходимо выбрать таблицу.");
                    } else {
                        try {
                            Statement countStatement = connection.createStatement();
                            ResultSet countResultSet = countStatement.executeQuery("SELECT COUNT(*) AS count FROM " + tableName);
                            countResultSet.next();
                            int rowCount = countResultSet.getInt("count");
                            countResultSet.close();
                            countStatement.close();

                            if (rowCount == 0) {
                                System.out.println("В таблице отсутствуют данные. Сначала введите строки для выполнения операции.");
                            } else {
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
                                    updateStatement.close();
                                }

                                selectStatement.close();
                            }
                        } catch (SQLException e) {
                            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                        }
                    }
                    break;

                case 6:
                    if (tableName.isEmpty()) {
                        System.out.println("Сначала необходимо выбрать таблицу.");
                    } else {
                        try {
                            Statement countStatement = connection.createStatement();
                            ResultSet countResultSet = countStatement.executeQuery("SELECT COUNT(*) AS count FROM " + tableName);
                            countResultSet.next();
                            int rowCount = countResultSet.getInt("count");
                            countResultSet.close();
                            countStatement.close();

                            if (rowCount == 0) {
                                System.out.println("В таблице отсутствуют данные. Сначала введите строки для выполнения операции.");
                            } else {
                                Statement selectStatement = connection.createStatement();
                                ResultSet resultSet = selectStatement.executeQuery("SELECT * FROM " + tableName);

                                while (resultSet.next()) {
                                    String firstStr = resultSet.getString("first_str");
                                    String secondStr = resultSet.getString("second_str");
                                    String comparisonResult = firstStr.equals(secondStr) ? "Строки идентичны" : "Строки не идентичны";
                                    System.out.println("Результат сравнения строк: " + comparisonResult);

                                    Statement updateStatement = connection.createStatement();
                                    String updateQuery = "UPDATE " + tableName + " SET comparison_result = '" + comparisonResult + "' WHERE first_str = '" + firstStr + "'";
                                    updateStatement.executeUpdate(updateQuery);
                                    updateStatement.close();
                                }

                                selectStatement.close();
                            }
                        } catch (SQLException e) {
                            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                        }
                    }
                    break;


                case 7:
                    if (tableName.isEmpty()) {
                        System.out.println("Сначала необходимо выбрать таблицу.");
                    } else {
                        try {
                            // Создаем новую книгу Excel и записываем данные
                            Workbook workbook = new XSSFWorkbook();
                            Sheet sheet = workbook.createSheet("Data");

                            // Записываем заголовки столбцов
                            Row headerRow = sheet.createRow(0);
                            headerRow.createCell(0).setCellValue("Первая строка");
                            headerRow.createCell(1).setCellValue("Вторая строка");
                            headerRow.createCell(2).setCellValue("Длина первой строки");
                            headerRow.createCell(3).setCellValue("Длина второй строки");
                            headerRow.createCell(4).setCellValue("Объединенная строка");
                            headerRow.createCell(5).setCellValue("Результат сравнения");

                            // Получаем данные из таблицы и записываем их в книгу Excel
                            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM " + tableName);
                            int rowNum = 1;
                            while (resultSet.next()) {
                                Row row = sheet.createRow(rowNum++);
                                row.createCell(0).setCellValue(resultSet.getString("first_str"));
                                row.createCell(1).setCellValue(resultSet.getString("second_str"));
                                row.createCell(2).setCellValue(resultSet.getInt("len_1"));
                                row.createCell(3).setCellValue(resultSet.getInt("len_2"));
                                row.createCell(4).setCellValue(resultSet.getString("combined_str"));
                                row.createCell(5).setCellValue(resultSet.getString("comparison_result"));
                            }

                            // Сохраняем книгу Excel в файл
                            Thread excelThread = new Thread(() -> {
                                String excelFileName = "hard_2.xlsx";
                                try (FileOutputStream outputStream = new FileOutputStream(excelFileName)) {
                                    workbook.write(outputStream);
                                    System.out.println("Данные успешно сохранены в файл " + excelFileName);
                                } catch (IOException e) {
                                    System.out.println("Ошибка при сохранении данных в Excel: " + e.getMessage());
                                }
                            });
                            excelThread.start();


                        } catch (SQLException e) {
                            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                        }
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