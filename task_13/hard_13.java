package task_13;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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

            while (running) {
                System.out.println("1. Вывести все таблицы из MySQL.");
                System.out.println("2. Создать таблицу в MySQL.");
                System.out.println("3. Ввести список и сохранить в MySQL.");
                System.out.println("4. Удалить элемент из списка в MySQL по ID.");
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
                            statement.executeUpdate("USE " + dbName);
                            Listik listik = new Listik();
                            List<String> inputList = listik.input();
                            String insertQuery = "INSERT INTO " + tableName + " (value) VALUES (?)";
                            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                                for (String value : inputList) {
                                    preparedStatement.setString(1, value);
                                    preparedStatement.executeUpdate();
                                }
                                System.out.println("Список успешно сохранен в MySQL.");
                            }
                        } catch (SQLException e) {
                            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                        }
                        break;

                    case 4:
                        System.out.println("Введите ID элемента для удаления: ");
                        int id = scanner.nextInt();
                        try (Statement statement = connection.createStatement()) {
                            statement.executeUpdate("USE " + dbName);
                            String deleteQuery = "DELETE FROM " + tableName + " WHERE id = ?";
                            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                                preparedStatement.setInt(1, id);
                                int rowsAffected = preparedStatement.executeUpdate();
                                if (rowsAffected > 0) {
                                    System.out.println("Элемент успешно удален из списка.");
                                } else {
                                    System.out.println("Элемент с указанным ID не найден.");
                                }
                            }
                        } catch (SQLException e) {
                            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                        }
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
                            Sheet sheet = workbook.createSheet("Data");

                            int rowNum = 0;
                            for (String data : tableData) {
                                Row row = sheet.createRow(rowNum++);
                                row.createCell(0).setCellValue(data);
                            }

                            String excelFilePath = "data.xlsx";
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