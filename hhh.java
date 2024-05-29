import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class hhh {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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
            return;
        }

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE " + dbName);
        } catch (SQLException e) {
            System.out.println("Ошибка при выборе базы данных: " + e.getMessage());
            return;
        }

        String tableName = "numbers"; // Имя новой таблицы

        // Создание новой таблицы
        try {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INT AUTO_INCREMENT PRIMARY KEY, number INT)";
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableQuery);
            System.out.println("Таблица успешно создана или уже существует.");
        } catch (SQLException e) {
            System.out.println("Ошибка при создании таблицы: " + e.getMessage());
            return;
        }

        // Теперь у вас есть база данных и таблица, в которую вы будете записывать данные.

        List<Integer> list = new LinkedList<>();

        System.out.println("Введите целые числа для добавления в связанный список (для завершения ввода введите 'exit'):");

        String input;
        while (true) {
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                int number = Integer.parseInt(input);
                list.add(number);
                System.out.println("Добавлено число: " + number);

                // Сохранение числа в базу данных
                String insertQuery = "INSERT INTO " + tableName + " (number) VALUES (?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    preparedStatement.setInt(1, number);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("Ошибка при записи числа в базу данных: " + e.getMessage());
                }
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите допустимое целое число.");
            }
        }

        // Создание Excel файла и запись в него данных
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Numbers");
            int rowNum = 0;
            for (int number : list) {
                Row row = sheet.createRow(rowNum++);
                Cell cell = row.createCell(0);
                cell.setCellValue(number);
            }
            // Запись данных в файл
            try (FileOutputStream outputStream = new FileOutputStream("numbers.xlsx")) {
                workbook.write(outputStream);
                System.out.println("Данные успешно записаны в Excel файл.");
            } catch (IOException e) {
                System.out.println("Ошибка при записи данных в Excel файл: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Ошибка при создании Excel файла: " + e.getMessage());
        }

        scanner.close();
    }
}

