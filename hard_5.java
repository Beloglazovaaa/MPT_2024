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

        url += dbName;
        clearSQLData(connection);

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
                    concatenateStringsAndSave(scanner, connection);
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

    private static void clearSQLData(Connection connection) {
        String clearTableQuery = "TRUNCATE TABLE " + tableName;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(clearTableQuery);
            System.out.println("Таблица очищена.");
        } catch (SQLException e) {
            System.out.println("Ошибка при очистке данных таблицы: " + e.getMessage());
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

        // Сохранение перевернутых строк в базу данных
        saveToDatabase(reversedFirstString.toString(), reversedSecondString.toString(), connection);
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

    private static void concatenateStringsAndSave(Scanner scanner, Connection connection) {
        // Объединяем строки, которые были введены в 3 пункте
        String concatenatedString = reverseStrings(scanner, connection);

        // Сохранение объединенной строки в базу данных
        saveConcatenatedStringToDatabase(concatenatedString, connection);
    }

    private static String reverseStrings(Scanner scanner, Connection connection) {
        System.out.println("Введите первую строку: ");
        String firstString = scanner.nextLine();
        StringBuffer reversedFirstString = new StringBuffer(firstString).reverse();

        System.out.println("Введите вторую строку: ");
        String secondString = scanner.nextLine();
        StringBuffer reversedSecondString = new StringBuffer(secondString).reverse();

        System.out.println("Перевернутая первая строка: " + reversedFirstString);
        System.out.println("Перевернутая вторая строка: " + reversedSecondString);

        return reversedFirstString.toString() + reversedSecondString.toString();
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

            // Заполнение данных
            int rowNum = 1;
            while (resultSet.next()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(resultSet.getInt("id"));
                row.createCell(1).setCellValue(resultSet.getString("string"));
            }

            // Сохранение в файл
            try (FileOutputStream outputStream = new FileOutputStream("InteractiveMenu.xlsx")) {
                workbook.write(outputStream);
                System.out.println("Данные успешно сохранены в файл 'InteractiveMenu.xlsx'.");
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



