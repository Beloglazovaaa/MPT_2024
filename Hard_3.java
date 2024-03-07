import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Hard_3 {
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
            System.out.println("3. Выполнение задачи базового варианта, результат сохранить в MySQL с последующим выводом в консоль.");
            System.out.println("4. Сохранить все данные (вышеполученные результаты) из MySQL в Excel и вывести на экран.");
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
                    String createTableQuery = "CREATE TABLE " + tableName +
                            "(id INT AUTO_INCREMENT PRIMARY KEY, number INT, is_integer BOOLEAN, is_even BOOLEAN)";

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
                    // Выполнение задачи базового варианта и сохранение результата в MySQL
                    executeTaskAndSaveToMySQL(scanner, connection);
                    break;

                case 4:
                    // Сохранение всех данных из MySQL в Excel и вывод на экран
                    saveDataToExcelAndDisplay(connection);
                    break;

                case 0:
                    running = false;
                    break;

                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }

        // Закрытие подключения к базе данных
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
        String tableName = scanner.next();

        // Создаем SQL-запрос для создания таблицы
        String createTableQuery = "CREATE TABLE " + tableName +
                "(id INT AUTO_INCREMENT PRIMARY KEY, number INT, is_integer BOOLEAN, is_even BOOLEAN)";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableQuery);
            System.out.println("Таблица успешно создана.");
        } catch (SQLException e) {
            System.out.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    private static void executeTaskAndSaveToMySQL(Scanner scanner, Connection connection) {
        System.out.println("Введите числа (через пробел): ");
        scanner.nextLine(); // Очистка буфера
        String[] numbers = scanner.nextLine().split(" ");

        // Подготовка SQL-запроса для вставки данных в конкретную таблицу
        String insertQuery = "INSERT INTO numbers_table (number, is_integer, is_even) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            for (String num : numbers) {
                try {
                    int number = Integer.parseInt(num);
                    preparedStatement.setInt(1, number);
                    preparedStatement.setBoolean(2, true); // Вы можете изменить логику определения, является ли число целым
                    preparedStatement.setBoolean(3, number % 2 == 0);
                    preparedStatement.executeUpdate();
                    System.out.printf("Число %d добавлено в таблицу.\n", number);
                } catch (NumberFormatException e) {
                    System.out.printf("'%s' не является целым числом.\n", num);
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при вставке данных в таблицу: " + e.getMessage());
        }
    }


    private static void saveDataToExcelAndDisplay(Connection connection) {
        // Используйте актуальное имя таблицы вместо "your_table_name"
        String query = "SELECT * FROM numbers_table";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
             Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");

            // Заполнение заголовков
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Number");
            headerRow.createCell(2).setCellValue("Is Integer");
            headerRow.createCell(3).setCellValue("Is Even");

            // Заполнение данных
            int rowNum = 1;
            while (resultSet.next()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(resultSet.getInt("id"));
                row.createCell(1).setCellValue(resultSet.getInt("number"));
                row.createCell(2).setCellValue(resultSet.getBoolean("is_integer"));
                row.createCell(3).setCellValue(resultSet.getBoolean("is_even"));
            }

            // Сохранение в файл
            try (FileOutputStream outputStream = new FileOutputStream("data.xlsx")) {
                workbook.write(outputStream);
                System.out.println("Данные успешно сохранены в файл 'data.xlsx'.");
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
                        case BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t");
                            break;
                        default:
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