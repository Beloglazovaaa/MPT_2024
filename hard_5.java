import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class hard_5 {
    private static String tableName = "";

    public static void main(String[] args) {
        String excelFilePath = "hard_5.xlsx";
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
            System.out.println("1. Вывести все таблицы из MySQL.");
            System.out.println("2. Создать таблицу в MySQL.");
            System.out.println("3. Изменить порядок символов строки на обратный, результат сохранить в MySQL с последующим выводом в консоль.");
            System.out.println("4. Добавить одну строку в другую, результат сохранить в MySQL с последующим выводом в консоль.");
            System.out.println("5. Сохранить все данные (вышеполученные результаты) из MySQL в Excel и вывести на экран.");
            System.out.println("0. Выйти из программы.");
            System.out.println("Выберите действие: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // очистка буфера

            switch (choice) {
                case 1:
                    showTables(connection);
                    break;

                case 2:
                    createTable(scanner, connection);
                    break;

                case 3:
                    reverseStringsAndSave(scanner, connection);
                    break;

                case 4:
                    concatenateStringsAndSave(connection);
                    break;

                case 5:
                    saveDataToExcelAndDisplay(connection);
                    break;

                case 0:
                    running = false;
                    break;

                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }

        try {
            if (connection != null) {
                connection.close();
                System.out.println("Подключение к базе данных закрыто.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при закрытии подключения к базе данных: " + e.getMessage());
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
                "(id INT AUTO_INCREMENT PRIMARY KEY, string VARCHAR(255))";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableQuery);
            System.out.println("Таблица успешно создана.");
        } catch (SQLException e) {
            System.out.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    private static void reverseStringsAndSave(Scanner scanner, Connection connection) {
        System.out.println("Введите первую строку: ");
        String firstString = scanner.nextLine();
        StringBuffer reversedFirstString = new StringBuffer(firstString).reverse();

        System.out.println("Введите вторую строку: ");
        String secondString = scanner.nextLine();
        StringBuffer reversedSecondString = new StringBuffer(secondString).reverse();

        System.out.println("Перевернутая первая строка: " + reversedFirstString);
        System.out.println("Перевернутая вторая строка: " + reversedSecondString);

        // Сохраняем перевернутые строки в базу данных
        saveToDatabase(reversedFirstString.toString(), reversedSecondString.toString(), connection);
    }

    private static void concatenateStringsAndSave(Connection connection) {
        // Получаем строки из базы данных
        String firstString = getFirstStringFromDatabase(connection);
        String secondString = getSecondStringFromDatabase(connection);

        // Объединяем строки
        String concatenatedString = firstString + secondString;

        // Сохраняем объединенную строку в базу данных
        saveConcatenatedStringToDatabase(concatenatedString, connection);
    }

    private static String getFirstStringFromDatabase(Connection connection) {
        // Получаем первую строку из базы данных
        String query = "SELECT string FROM " + tableName + " WHERE id = 1";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getString("string");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении данных из таблицы: " + e.getMessage());
        }
        return "";
    }

    private static String getSecondStringFromDatabase(Connection connection) {
        // Получаем вторую строку из базы данных
        String query = "SELECT string FROM " + tableName + " WHERE id = 2";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getString("string");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении данных из таблицы: " + e.getMessage());
        }
        return "";
    }

    private static void saveToDatabase(String reversedFirstString, String reversedSecondString, Connection connection) {
        String insertQuery = "INSERT INTO " + tableName + " (string) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, reversedFirstString);
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, reversedSecondString);
            preparedStatement.executeUpdate();

            System.out.println("Перевернутые строки успешно сохранены в MySQL.");
        } catch (SQLException e) {
            System.out.println("Ошибка при вставке данных в таблицу: " + e.getMessage());
        }
    }

    private static void saveConcatenatedStringToDatabase(String concatenatedString, Connection connection) {
        String insertQuery = "INSERT INTO " + tableName + " (string) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, concatenatedString);
            preparedStatement.executeUpdate();

            System.out.println("Результат успешно сохранен в MySQL.");
            System.out.println("Объединенная строка: " + concatenatedString);
        } catch (SQLException e) {
            System.out.println("Ошибка при вставке данных в таблицу: " + e.getMessage());
        }
    }

    private static void saveDataToExcelAndDisplay(Connection connection) {
        String query = "SELECT * FROM " + tableName;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
             Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");

            // Заполнение заголовков
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("String");
            headerRow.createCell(2).setCellValue("Reversed String");

            // Заполнение данных
            int rowNum = 1;
            while (resultSet.next()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(resultSet.getInt("id"));
                row.createCell(1).setCellValue(resultSet.getString("string"));

                // Получение перевернутой строки из исходной
                String originalString = resultSet.getString("string");
                String reversedString = new StringBuilder(originalString).reverse().toString();
                row.createCell(2).setCellValue(reversedString);
            }

            // Автоматическое выравнивание по ширине колонки
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            // Сохранение в файл
            try (FileOutputStream outputStream = new FileOutputStream("hard_5.xlsx")) {
                workbook.write(outputStream);
                System.out.println("Данные успешно сохранены в файл 'hard_5.xlsx'.");
            } catch (IOException e) {
                System.out.println("Ошибка при сохранении данных в файл: " + e.getMessage());
            }

            // Вывод данных на экран
            for (Row row : sheet) {
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "\t");
                            break;
                        case NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t");
                            break;
                        default:
                            System.out.print("Unknown\t");
                            break;
                    }
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении данных из таблицы: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Ошибка при создании Excel файла: " + e.getMessage());
        }
    }
}

