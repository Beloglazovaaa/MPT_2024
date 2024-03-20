/* Объектно-ориентированное программирование. Необходимо создать класс ArrayPI
(модификатор доступа - public), в котором необходимо создать одномерный массив
(ввод с клавиатуры, 35 элементов). Далее необходимо создать класс-наследник Sort
(модификатор доступа - public final), в котором будет наследоваться и сортироваться
введенный ранее массив. Необходимо отсортировать введенный ранее массив
по возрастанию и убыванию методом пузырьковой сортировки.

Реализовать программу с интерактивным консольным меню:
1. Вывести все таблицы из базы данных MySQL.
2. Создать таблицу в базе данных MySQL.
3. Ввести одномерный массив и сохранить в MySQL с последующим выводом в консоль.
4. Отсортировать массив и сохранить в MySQL с последующим выводом в консоль.
5. Сохранить результаты из MySQL в Excel и вывести их в консоль. */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileOutputStream;

import java.util.*;
import java.util.stream.*;

public class hard_7 {
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
        String tableName = ""; // Объявляем переменную для хранения имени таблицы

        while (running) {
            System.out.println("Выберите действие:");
            System.out.println("1. Вывести все таблицы из базы данных");
            System.out.println("2. Создать таблицу в базе данных");
            System.out.println("3. Ввести одномерный массив и сохранить в MySQL");
            System.out.println("4. Отсортировать массив и сохранить в MySQL");
            System.out.println("5. Сохранить результаты из MySQL в Excel");
            System.out.println("0. Выход");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Считываем символ новой строки

            switch (choice) {
                // Кейсы

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
                    String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INT AUTO_INCREMENT PRIMARY KEY, array_column TEXT)";

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
                    int[] array = new int[35];
                    System.out.println("Введите 35 элементов массива:");

                    for (int i = 0; i < array.length; i++) {
                        array[i] = scanner.nextInt();
                    }

                    // Сохранение массива в базу данных
                    try {
                        String query = "INSERT INTO " + tableName + " (array_column) VALUES (?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(query);

                        // Преобразование массива в строку для сохранения в базу данных
                        StringBuilder arrayString = new StringBuilder();
                        for (int i = 0; i < array.length; i++) {
                            arrayString.append(array[i]);
                            if (i < array.length - 1) {
                                arrayString.append(",");
                            }
                        }

                        preparedStatement.setString(1, arrayString.toString());
                        preparedStatement.executeUpdate();
                        System.out.println("Массив сохранен в базу данных.");

                        // Вывод массива в консоль
                        System.out.println("Сохраненный массив:");
                        for (int i = 0; i < array.length; i++) {
                            System.out.print(array[i] + " ");
                        }
                        System.out.println();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    sortArrayAndOutput(connection, tableName, scanner);
                    break;

                case 5:
                    saveToExcel(connection, tableName);
                    break;

                case 0:
                    running = false;
                    System.out.println("Программа завершена.");
                    break;

                default:
                    System.out.println("Некорректный ввод. Пожалуйста, попробуйте еще раз.");
            }
        }
    }

    private static void sortArrayAndOutput(Connection connection, String tableName, Scanner scanner) {
        try {
            // Получение последнего сохраненного массива из базы данных
            Statement statement = connection.createStatement();
            System.out.println(tableName);
            ResultSet resultSet = statement.executeQuery("SELECT array_column FROM " + tableName + " ORDER BY id DESC LIMIT 1");

            if (resultSet.next()) {
                String arrayString = resultSet.getString("array_column");
                int[] array = Arrays.stream(arrayString.split(",")).mapToInt(Integer::parseInt).toArray();

                Sort sorter = new Sort(array);
                sorter.bubbleSortAscending();
                System.out.println("Отсортированный массив по возрастанию:");
                for (int value : sorter.array) {
                    System.out.print(value + " ");
                }
                System.out.println();

                // Сохранение отсортированного массива в базу данных
                String sortedArrayString = Arrays.stream(sorter.array).mapToObj(String::valueOf).collect(Collectors.joining(","));
                String query = "INSERT INTO " + tableName + " (array_column) VALUES (?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, sortedArrayString);
                preparedStatement.executeUpdate();
                System.out.println("Отсортированный массив сохранен в базу данных.");

            } else {
                System.out.println("В базе данных нет массивов для сортировки.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void saveToExcel(Connection connection, String tableName) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Results");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

            int rowNum = 0;
            while (resultSet.next()) {
                String arrayString = resultSet.getString("array_column");
                String[] arrayValues = arrayString.split(",");
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < arrayValues.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(arrayValues[i]);
                    sheet.autoSizeColumn(i);  // Адаптивная ширина колонки
                }
            }

            try (FileOutputStream outputStream = new FileOutputStream("hard_7.xlsx")) {
                workbook.write(outputStream);
            }

            System.out.println("Результаты сохранены в файл Excel: hard_7.xlsx");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ArrayPI {
    protected int[] array;

    public ArrayPI(int[] array) {
        this.array = array;
    }
}

final class Sort extends ArrayPI {
    public Sort(int[] array) {
        super(array);
    }

    public void bubbleSortAscending() {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }
}