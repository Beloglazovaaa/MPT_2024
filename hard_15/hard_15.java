package hard_15;

import java.sql.*;
import java.util.Scanner;
import java.util.*;

public class hard_15 {
    private static String tableName = "";

    public static void main(String[] args) {
        String excelFilePath = "hard_11.xlsx";
        Connection connection = null;

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        String url = "jdbc:mysql://localhost:3305/";
        String dbName = "DB";
        String username = "root";
        String password = "root";

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Подключение к базе данных успешно!");
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
            return;
        }

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE " + dbName);
            System.out.println("База данных успешно выбрана.");
        } catch (SQLException e) {
            System.out.println("Ошибка при выборе базы данных: " + e.getMessage());
            return;
        }

        try {
            Statement statement = connection.createStatement();
            String createDBQuery = "CREATE DATABASE IF NOT EXISTS " + dbName;
            statement.executeUpdate(createDBQuery);
            System.out.println("База данных успешно создана или уже существует.");
        } catch (SQLException e) {
            System.out.println("Ошибка при создании базы данных: " + e.getMessage());
            return;
        }


        while (running) {
            System.out.println("1. Вывести все таблицы из базы данных MySQL.");
            System.out.println("2. Создать таблицу в базе данных MySQL.");
            System.out.println("3. Сохранить вводимый с клавиатуры список, а также строку и множество в MySQL.");
            System.out.println("4. Удалить элемент из списка, строки и множества в MySQL по ID");
            System.out.println("5. Сохранить итоговые результаты из MySQL в Excel и вывести их в консоль.");
            System.out.println("0. Выйти из программы.");
            System.out.println("Выберите действие: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // очистка буфера
            Listik1 listik1 = new Listik1();

            switch (choice) {
                case 1:
                    showTables(connection);
                    break;

                case 2:
                    createTable(scanner, connection);
                    break;
                case 3:
                    listik1.inputElements(tableName, connection);
                    break;
            }
        }
    }

    private static void showTables(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SHOW TABLES");
            System.out.println("Таблицы в базе данных:");
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выводе таблиц: " + e.getMessage());
        }
    }


    private static void createTable(Scanner scanner, Connection connection) {
        System.out.println("Введите название таблицы: ");
        tableName = scanner.nextLine();

        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName +
                "(id INT AUTO_INCREMENT PRIMARY KEY, data_type VARCHAR(50) NOT NULL, data_value TEXT NOT NULL)";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableQuery);
            System.out.println("Таблица успешно создана.");
        } catch (SQLException e) {
            System.out.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

}