/* Базовый вариант. Объектно-ориентированное программирование и коллекции. Создать класс Listik с
модификатором доступа public. В данном классе создать два метода: random и input с модификатором
доступа protected. Внутри метода random создать список длиной из 1000 случайных значений int
(генератор). В методе input необходимо создать список из 10000 значений Integer. При этом количество
элементов и числовой диапазон элементов вводятся пользователем. Выполнить ввод числа с клавиатуры с
последующей проверкой на принадлежность данного числа данному списку.
Объектно-ориентированное программирование и коллекции. Реализовать программу с интерактивным
консольным меню, (т.е. вывод списка действий по цифрам. При этом при нажатии на цифру у нас
должно выполняться определенное действие). Задачи полностью идентичны заданию №1. Каждый пункт
меню должен быть отдельным классом-наследником (подклассом).
1. Вывести все таблицы из базы данных MySQL.
2. Создать таблицу в базе данных MySQL.
3. Сохранить вводимое число и списки в MySQL.
4. Удалить элемент из списка в MySQL по ID.
5. Сохранить итоговые результаты из MySQL в Excel и вывести их в консоль. */

package task_14;

import java.sql.*;
import java.util.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;

public class hard_14 {
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

            listik listik = new listik();
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
                scanner.nextLine();

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
                        List<Integer> inputList = listik.input();
                        System.out.println("Введите число для проверки: ");
                        int number = scanner.nextInt();
                        boolean isInList = listik.containsNumber(inputList, number);
                        System.out.println("Число " + number + (isInList ? " содержится" : " не содержится") + " в списке.");

                        try {
                            if (tableName.isEmpty()) {
                                System.out.println("Сначала создайте таблицу (кейс 2)!");
                                break;
                            }
                            connection.setAutoCommit(false); // Выключаем автокоммит
                            try (Statement statement = connection.createStatement()) {
                                statement.executeUpdate("USE " + dbName);
                                String insertQuery = "INSERT INTO " + tableName + " (value) VALUES (?)";
                                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                                    for (Integer value : inputList) {
                                        preparedStatement.setString(1, String.valueOf(value));
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
                        } catch (SQLException e) {
                            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                        }
                        break;

                    case 4:
                        System.out.println("Введите ID для удаления элемента из введенного списка: ");
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

                        System.out.println("Введите ID для удаления элемента из рандомного списка: ");
                        int randomId = scanner.nextInt();
                        listik.deleteRandom(randomList, randomId);
                        break;

                    case 5:
                        try (Statement statement = connection.createStatement()) {
                            statement.executeUpdate("USE " + dbName);
                            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

                            List<String> tableData = new ArrayList<>();
                            System.out.println("Результаты из введенного списка пользователем:");
                            while (resultSet.next()) {
                                int idFromDB = resultSet.getInt("id");
                                String valueFromDB = resultSet.getString("value");
                                tableData.add("ID: " + idFromDB + ", Value: " + valueFromDB);
                                System.out.println("ID: " + idFromDB + ", Value: " + valueFromDB);
                            }

                            System.out.println("Результаты из рандомного списка:");
                            for (Map.Entry<Integer, Integer> entry : randomList) {
                                System.out.println("ID: " + entry.getKey() + ", Value: " + entry.getValue());
                            }

                            Workbook workbook = new XSSFWorkbook();
                            Sheet sheet1 = workbook.createSheet("Введенный список");
                            Sheet sheet2 = workbook.createSheet("Рандомный список");


                            Row headerRow1 = sheet1.createRow(0);
                            headerRow1.createCell(0).setCellValue("ID");
                            headerRow1.createCell(1).setCellValue("Значение");
                            int rowNum1 = 1;
                            for (String data : tableData) {
                                Row row = sheet1.createRow(rowNum1++);
                                String[] parts = data.split(", Value: ");
                                row.createCell(0).setCellValue(parts[0].replace("ID: ", ""));
                                row.createCell(1).setCellValue(parts[1]);
                            }


                            Row headerRow2 = sheet2.createRow(0);
                            headerRow2.createCell(0).setCellValue("ID");
                            headerRow2.createCell(1).setCellValue("Значение");
                            int rowNum2 = 1;
                            for (Map.Entry<Integer, Integer> entry : randomList) {
                                Row row = sheet2.createRow(rowNum2++);
                                row.createCell(0).setCellValue(entry.getKey());
                                row.createCell(1).setCellValue(entry.getValue());
                            }


                            sheet1.autoSizeColumn(0);
                            sheet1.autoSizeColumn(1);
                            sheet2.autoSizeColumn(0);
                            sheet2.autoSizeColumn(1);

                            String excelFilePath = "hard_14.xlsx";
                            try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
                                workbook.write(outputStream);
                                System.out.println("Результаты успешно сохранены в Excel файл.");
                            } catch (IOException e) {
                                System.out.println("Ошибка при сохранении в Excel: " + e.getMessage());
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
