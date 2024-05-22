package task_13;

import java.sql.*;
import java.util.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;

public class hard_13 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        String tableName = "";

        String url = "jdbc:mysql://localhost:3305/";
        String dbName = "DB";
        String username = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url + dbName, username, password)) {
            System.out.println("Подключение к базе данных успешно!");

            Listik listik = new Listik();
            List<Map.Entry<Integer, Integer>> randomList = listik.random();

            while (running) {
                System.out.println("1. Вывести все таблицы из MySQL.");
                System.out.println("2. Создать таблицу в MySQL.");
                System.out.println("3. Ввести список и сохранить в MySQL.");
                System.out.println("4. Удалить элемент из списка в MySQL по ID и из рандомного списка по ID.");
                System.out.println("5. Сохранить итоговые результаты из MySQL в Excel и вывести их в консоль.");
                System.out.println("0. Выйти из программы.");
                System.out.println("Выберите действие: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline character

                switch (choice) {
                    case 1:
                        try (Statement statement = connection.createStatement()) {
                            statement.executeUpdate("USE " + dbName);
                            ResultSet resultSet = statement.executeQuery("SHOW TABLES");
                            List<String> tableNames = new ArrayList<>();
                            while (resultSet.next()) {
                                tableNames.add(resultSet.getString(1));
                            }
                            System.out.println("Таблицы в базе данных:");
                            for (String name : tableNames) {
                                System.out.println(name);
                            }
                        } catch (SQLException e) {
                            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                        }
                        break;

                    case 2:
                        System.out.println("Введите название таблицы: ");
                        tableName = scanner.nextLine();

                        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName +
                                "(id INT AUTO_INCREMENT PRIMARY KEY, value VARCHAR(255))";

                        try (Statement statement = connection.createStatement()) {
                            statement.executeUpdate(createTableQuery);
                            System.out.println("Таблица успешно создана.");
                        } catch (SQLException e) {
                            System.out.println("Ошибка при создании таблицы: " + e.getMessage());
                        }
                        break;

                    case 3:
                        try (Statement statement = connection.createStatement()) {
                            if (tableName.isEmpty()) {
                                System.out.println("Сначала создайте таблицу (кейс 2)!");
                                break;
                            }
                            connection.setAutoCommit(false); // Выключаем автокоммит
                            statement.executeUpdate("USE " + dbName);
                            List<String> inputList = listik.input();
                            String insertQuery = "INSERT INTO " + tableName + " (value) VALUES (?)";
                            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                                for (String value : inputList) {
                                    preparedStatement.setString(1, value);
                                    preparedStatement.executeUpdate();
                                }
                                connection.commit(); // Сохраняем изменения в базе данных
                                System.out.println("Список успешно сохранен в MySQL.");
                            }
                        } catch (SQLException e) {
                            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                            try {
                                connection.rollback(); // Откатываем изменения в случае ошибки
                            } catch (SQLException rollbackException) {
                                System.out.println("Ошибка при откате изменений: " + rollbackException.getMessage());
                            }
                        }
                        break;

                    case 4:
                        System.out.println("Введите ID элемента для удаления из MySQL: ");
                        int mysqlId = scanner.nextInt();
                        scanner.nextLine(); // consume newline character
                        try (Statement statement = connection.createStatement()) {
                            statement.executeUpdate("USE " + dbName);
                            String deleteQuery = "DELETE FROM " + tableName + " WHERE id = ?";
                            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                                preparedStatement.setInt(1, mysqlId);
                                int rowsAffected = preparedStatement.executeUpdate();
                                if (rowsAffected > 0) {
                                    System.out.println("Элемент успешно удален из MySQL.");
                                    connection.commit(); // Сохраняем изменения в базе данных
                                } else {
                                    System.out.println("Элемент с указанным ID не найден.");
                                }
                            }
                        } catch (SQLException e) {
                            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                            try {
                                connection.rollback(); // Откатываем изменения в случае ошибки
                            } catch (SQLException rollbackException) {
                                System.out.println("Ошибка при откате изменений: " + rollbackException.getMessage());
                            }
                        }

                        System.out.println("Введите ID элемента для удаления из рандомного списка: ");
                        int randomId = scanner.nextInt();
                        listik.deleteRandom(randomList, randomId);
                        break;

                    case 5:
                        try (Statement statement = connection.createStatement()) {
                            statement.executeUpdate("USE " + dbName);
                            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

                            List<String> tableData = new ArrayList<>();
                            while (resultSet.next()) {
                                int idFromDB = resultSet.getInt("id");
                                String valueFromDB = resultSet.getString("value");
                                tableData.add("ID: " + idFromDB + ", Value: " + valueFromDB);
                            }

                            Workbook workbook = new XSSFWorkbook();
                            Sheet sheet1 = workbook.createSheet("Input Data");
                            Sheet sheet2 = workbook.createSheet("Random Data");

                            // Fill Sheet 1 with input data
                            int rowNum1 = 0;
                            for (String data : tableData) {
                                Row row = sheet1.createRow(rowNum1++);
                                row.createCell(0).setCellValue(data);
                            }

                            // Fill Sheet 2 with random data
                            int rowNum2 = 0;
                            for (Map.Entry<Integer, Integer> entry : randomList) {
                                Row row = sheet2.createRow(rowNum2++);
                                row.createCell(0).setCellValue("ID: " + entry.getKey());
                                row.createCell(1).setCellValue("Value: " + entry.getValue());
                            }

                            String excelFilePath = "hard_13.xlsx";
                            try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
                                workbook.write(outputStream);
                                System.out.println("Результаты успешно сохранены в Excel файл.");
                            } catch (IOException e) {
                                System.out.println("Ошибка при сохранении в Excel: " + e.getMessage());
                            }

                            // Вывод в консоль
                            System.out.println("Результаты из MySQL:");
                            for (String data : tableData) {
                                System.out.println(data);
                            }

                        } catch (SQLException e) {
                            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                        }
                        break;

                    case 0:
                        running = false;
                        break;

                    default:
                        System.out.println("Некорректный ввод.");
                        break;
                }
            }

        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}