package hard_15;

import java.sql.*;
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
                    ShowTables.showTables(connection);
                    break;

                case 2:
                    tableName = CreateTable.createTable(scanner, connection);
                    break;
                case 3:
                    listik1.inputElements(tableName, connection);
                    break;
                case 4:
                    DeleteElement.deleteById(scanner, tableName, connection);
                    break;
                case 5:
                    ExportToExcel.exportDataToExcel(tableName, connection);
                    ExportToExcel.displayDataInConsole(tableName, connection);
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
}