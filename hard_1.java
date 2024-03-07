//Сложный вариант. Целочисленные, байтовые и вещественные типы данных. Реализовать программу с интерактивным консольным меню,
// (т.е. вывод списка действий по цифрам. При этом при нажатии на цифру у нас должно выполняться определенное действие).
// Задания полностью идентичны базовому варианту. При этом в программе данные пункты должны называться следующим образом:
//1. Вывести все таблицы из MySQL.
//2. Создать таблицу в MySQL.
//3. Сложение чисел, результат сохранить в MySQL с последующим выводом в консоль.
//4.
//Вычитание чисел, результат сохранить в MySQL с последующим выводом в консоль.
//5.
//Умножение чисел, результат сохранить в MySQL с последующим выводом в консоль.
//6.
//Деление чисел, результат сохранить в MySQL с последующим выводом в консоль.
//7.
//Деление чисел по модулю (остаток), результат сохранить в MySQL с последующим выводом в консоль.
//8.
//Возведение числа в модуль, результат сохранить в MySQL с последующим выводом в консоль.
//9. Возведение числа в степень, результат сохранить в MySQL с последующим выводом в консоль.
//10. Сохранить все данные (вышеполученные результаты) из MySQL в Excel и вывести на экран.

import java.sql.*;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class hard_1 {
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
            System.out.println("База данных успешно выбрана.");
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
            System.out.println("3. Сложение чисел.");
            System.out.println("4. Вычитание чисел.");
            System.out.println("5. Умножение чисел.");
            System.out.println("6. Деление чисел.");
            System.out.println("7. Деление чисел по модулю (остаток).");
            System.out.println("8. Возведение числа в модуль.");
            System.out.println("9. Возведение числа в степень.");
            System.out.println("10. Сохранить все данные в Excel и вывести на экран.");
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
                            tableName = resultSet.getString(1); // Обновляем tableName здесь
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
                     String createTableQuery = "CREATE TABLE " + tableName +
                            "(first_number VARCHAR(255), second_number VARCHAR(255), " +
                            "sum_result INT, subtraction_result INT, multiplication_result INT, " +
                            "division_result DOUBLE, remainder_result DOUBLE, number_module DOUBLE, " +
                            "number_power DOUBLE)";

                    try {
                        Statement statement = connection.createStatement();
                        statement.executeUpdate(createTableQuery);
                        System.out.println("Таблица успешно создана.");
                    } catch (SQLException e) {
                        System.out.println("Ошибка при создании таблицы: " + e.getMessage());
                        break; // Выходим из case 2
                    }

                    // Добавляем данные в таблицу
                    try {
                        Statement statement = connection.createStatement();
                        String insertDataQuery = "INSERT INTO " + tableName + " " + "(first_number, second_number) VALUES (?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(insertDataQuery);

                        String firstNumberStr = "", secondNumberStr = "";
                        while (true) {
                            System.out.println("Введите первое число или 'стоп' для завершения ввода: ");
                            firstNumberStr = scanner.next();
                            if (firstNumberStr.equalsIgnoreCase("стоп")) {
                                break;
                            }

                            System.out.println("Введите второе число или 'стоп' для завершения ввода: ");
                            secondNumberStr = scanner.next();
                            if (secondNumberStr.equalsIgnoreCase("стоп")) {
                                break;
                            }

                            // Устанавливаем значения параметров запроса
                            preparedStatement.setString(1, firstNumberStr);
                            preparedStatement.setString(2, secondNumberStr);

                            preparedStatement.executeUpdate();
                            System.out.println("Данные успешно добавлены.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Ошибка при добавлении данных: " + e.getMessage());
                    }
                    break;
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    try {
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
                        List<int[]> numbers = new ArrayList<>();
                        while (resultSet.next()) {
                            int firstNumber = Integer.parseInt(resultSet.getString("first_number"));
                            int secondNumber = Integer.parseInt(resultSet.getString("second_number"));
                            numbers.add(new int[]{firstNumber, secondNumber});
                        }

                        for (int[] pair : numbers) {
                            int firstNumber = pair[0];
                            int secondNumber = pair[1];

                            int result = 0; // Инициализация результата
                            String sign = "";
                            switch (choice) {
                                case 3: // Сложение
                                    result = firstNumber + secondNumber;
                                    sign = " + ";
                                    break;
                                case 4: // Вычитание
                                    result = firstNumber - secondNumber;
                                    sign = " - ";
                                    break;
                                case 5: // Умножение
                                    result = firstNumber * secondNumber;
                                    sign = " * ";
                                    break;
                                case 6: // Деление
                                    if (secondNumber != 0) {
                                        result = firstNumber / secondNumber;
                                    } else {
                                        System.out.println("На ноль делить нельзя.");
                                        continue; // Пропускаем текущую итерацию цикла
                                    }
                                    sign = " / ";
                                    break;
                                case 7: // Деление по модулю
                                    if (secondNumber != 0) {
                                        result = firstNumber % secondNumber;
                                    } else {
                                        System.out.println("На ноль делить нельзя.");
                                        continue; // Пропускаем текущую итерацию цикла
                                    }
                                    sign = " % ";
                                    break;
                                case 8: // Возведение в модуль
                                    result = Math.abs(firstNumber);
                                    sign = " abs ";
                                    break;
                                case 9: // Возведение в степень
                                    result = (int) Math.pow(firstNumber, secondNumber);
                                    sign = " ^ ";
                                    break;
                            }

                            // Вывод результата
                            System.out.println("Результат вычисления чисел: " + firstNumber  + sign + secondNumber + " = " + result);
                            // Обновление данных в таблице
                            String updateQuery = "UPDATE " + tableName + " SET " + getResultColumnName(choice) + " = " + result + " WHERE first_number = " + firstNumber + " AND second_number = " + secondNumber;
                            statement.executeUpdate(updateQuery);
                        }
                    } catch (SQLException e) {
                        System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                    }
                    break;

                case 10:
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
                        String excelFileName = "java_1/hard_1_excel.xlsx";
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

    private static String getResultColumnName(int choice) {
        switch (choice) {
            case 3:
                return "sum_result";
            case 4:
                return "subtraction_result";
            case 5:
                return "multiplication_result";
            case 6:
                return "division_result";
            case 7:
                return "remainder_result";
            case 8:
                return "number_module";
            case 9:
                return "number_power";
            default:
                return "";
        }
    }
}