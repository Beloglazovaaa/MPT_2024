package task_14;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;

public class hard_14 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        String tableName = "";
        List<Integer> inputList = new ArrayList<>();
        listik listik = new listik();

        String url = "jdbc:mysql://localhost:3305/";
        String dbName = "DB";
        String username = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url + dbName, username, password)) {
            System.out.println("Подключение к базе данных успешно!");

            List<Integer> randomList = listik.random();

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
                                "(id INT AUTO_INCREMENT PRIMARY KEY, value INT)";

                        try (Statement statement = connection.createStatement()) {
                            statement.executeUpdate(createTableQuery);
                            System.out.println("Таблица успешно создана.");
                        } catch (SQLException e) {
                            System.out.println("Ошибка при создании таблицы: " + e.getMessage());
                        }
                        break;

                    case 3:
                        inputList = listik.input();

                        System.out.println("Введите число для проверки: ");
                        int number = scanner.nextInt();
                        boolean isInList = listik.containsNumber(inputList, number);
                        System.out.println("Число " + number + (isInList ? " содержится" : " не содержится") + " в списке.");

                        try (Statement statement = connection.createStatement()) {
                            if (tableName.isEmpty()) {
                                System.out.println("Сначала создайте таблицу (кейс 2)!");
                                break;
                            }
                            connection.setAutoCommit(false); // Выключаем автокоммит
                            statement.executeUpdate("USE " + dbName);
                            String insertQuery = "INSERT INTO " + tableName + " (value) VALUES (?)";
                            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                                for (Integer value : inputList) {
                                    preparedStatement.setInt(1, value);
                                    preparedStatement.executeUpdate();
                                }
                                for (Integer value : randomList) {
                                    preparedStatement.setInt(1, value);
                                    preparedStatement.executeUpdate();
                                }
                                connection.commit(); // Сохраняем изменения в базе данных
                                System.out.println("Списки успешно сохранены в MySQL.");
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
                                    connection.commit(); // Сохраняем изменения в базе данных

                                    // Удаление из рандомного списка
                                    if (id > 0 && id <= randomList.size()) {
                                        randomList.remove(id - 1);
                                        System.out.println("Элемент успешно удален из рандомного списка.");
                                    } else {
                                        System.out.println("Элемент с указанным ID не найден в рандомном списке.");
                                    }
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
                        break;

                    case 5:
                        try (Statement statement = connection.createStatement()) {
                            statement.executeUpdate("USE " + dbName);
                            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

                            List<String> tableData = new ArrayList<>();
                            while (resultSet.next()) {
                                int idFromDB = resultSet.getInt("id");
                                int valueFromDB = resultSet.getInt("value");
                                tableData.add("ID: " + idFromDB + ", Value: " + valueFromDB);
                            }

                            Workbook workbook = new XSSFWorkbook();

                            // Create sheet for random list
                            Sheet randomSheet = workbook.createSheet("Random List");
                            Row randomHeaderRow = randomSheet.createRow(0);
                            randomHeaderRow.createCell(0).setCellValue("ID");
                            randomHeaderRow.createCell(1).setCellValue("Value");
                            for (int i = 0; i < randomList.size(); i++) {
                                Row row = randomSheet.createRow(i + 1);
                                row.createCell(0).setCellValue(i + 1);
                                row.createCell(1).setCellValue(randomList.get(i));
                            }
                            randomSheet.autoSizeColumn(0);
                            randomSheet.autoSizeColumn(1);

                            // Create sheet for input list
                            Sheet inputSheet = workbook.createSheet("Input List");
                            Row inputHeaderRow = inputSheet.createRow(0);
                            inputHeaderRow.createCell(0).setCellValue("ID");
                            inputHeaderRow.createCell(1).setCellValue("Value");
                            for (int i = 0; i < inputList.size(); i++) {
                                Row row = inputSheet.createRow(i + 1);
                                row.createCell(0).setCellValue(i + 1);
                                row.createCell(1).setCellValue(inputList.get(i));
                            }
                            inputSheet.autoSizeColumn(0);
                            inputSheet.autoSizeColumn(1);

                            String excelFilePath = "hard_14.xlsx";
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