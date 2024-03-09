import java.io.FileOutputStream;
import java.io.File;
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

public class Hard_3 {
    private static String tableName = "";

    public static void main(String[] args) {
        String excelFilePath = "hard_3.xlsx";
        Connection connection = null;

        // Удаление данных из таблицы Excel при каждом запуске программы
        clearExcelData(excelFilePath);

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
        clearSQLData(connection);

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
                    // Вывод всех таблиц из MySQL
                    showTables(connection);
                    break;

                case 2:
                    // Создание таблицы в MySQL
                    createTable(scanner, connection);
                    break;

                case 3:
                    try {
                        if (!tableName.isEmpty()) {
                            // Выполнение задачи базового варианта и сохранение результата в MySQL
                            executeTaskAndSaveToMySQL(scanner, connection);
                        } else {
                            throw new SQLException("Таблица не выбрана или не создана.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                    break;

                case 4:
                    try {
                        if (!tableName.isEmpty()) {
                            // Сохранение всех данных из MySQL в Excel и вывод на экран
                            saveDataToExcelAndDisplay(connection);
                        } else {
                            throw new SQLException("Таблица не выбрана или не создана.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
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
    private static void clearExcelData(String excelFilePath) {
        File excelFile = new File(excelFilePath);
        if (excelFile.exists()) {
            excelFile.delete();
            System.out.println(" ");
        }
    }

    private static void clearSQLData(Connection connection) {
        String clearTableQuery = "TRUNCATE TABLE numbers_table";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(clearTableQuery);
            System.out.println(" ");
        } catch (SQLException e) {
            System.out.println("Error clearing SQL data: " + e.getMessage());
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
        tableName = scanner.next(); // Обновляем имя текущей таблицы

        // Создаем SQL-запрос для создания таблицы
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName +
                "(id INT AUTO_INCREMENT PRIMARY KEY, number DOUBLE, is_integer BOOLEAN, is_even BOOLEAN, note VARCHAR(255))";

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

        String insertQuery = "INSERT INTO numbers_table (number, is_integer, is_even, note) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            for (String num : numbers) {
                try {
                    double number = Double.parseDouble(num);
                    boolean isInteger;
                    boolean isEven;
                    String note;

                    if (number == 0) {
                        System.out.println("Число 0 не является натуральным числом.");
                        continue; // Пропускаем обработку числа 0
                    } else if (number % 1 == 0) {
                        isInteger = true;
                        isEven = number % 2 == 0;
                        note = isEven ? "Целое, Четное" : "Целое, Нечетное";
                    } else {
                        isInteger = false;
                        isEven = false;
                        note = "Нецелое число";
                    }

                    preparedStatement.setDouble(1, number);
                    preparedStatement.setBoolean(2, isInteger);
                    preparedStatement.setBoolean(3, isEven);
                    preparedStatement.setString(4, note);
                    preparedStatement.executeUpdate();

                    System.out.printf("Число %s добавлено в таблицу. %s\n", num, note);
                } catch (NumberFormatException e) {
                    System.out.printf("'%s' не является числом.\n", num);
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при вставке данных в таблицу: " + e.getMessage());
        }
    }

    private static void saveDataToExcelAndDisplay(Connection connection) {
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
            headerRow.createCell(4).setCellValue("Note"); // Добавляем заголовок для колонки Note

            // Заполнение данных
            int rowNum = 1;
            while (resultSet.next()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(resultSet.getInt("id"));
                row.createCell(1).setCellValue(resultSet.getDouble("number")); //
                row.createCell(2).setCellValue(resultSet.getBoolean("is_integer") ? "Yes" : "No");
                row.createCell(3).setCellValue(resultSet.getBoolean("is_even") ? "Yes" : "No");
                row.createCell(4).setCellValue(resultSet.getString("note")); // Добавляем данные для колонки Note
            }

            // Сохранение в файл
            try (FileOutputStream outputStream = new FileOutputStream("hard3.xlsx")) {
                workbook.write(outputStream);
                System.out.println("Данные успешно сохранены в файл 'hard3.xlsx'.");
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