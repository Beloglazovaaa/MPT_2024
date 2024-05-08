/* Объектно-ориентированное программирование. Создать класс Student с
модификатором доступа public. В данном классе создать метод, в котором добавляем поля с
модификатором доступа private, с их последующим вводом с клавиатуры: Количество студентов, данные
о которых вводим с клавиатуры; ID студента; Направление подготовки студента; ФИО студента; Группа
студента. Далее создать два класса-наследника, в которых мы переопределяем данный метод супер-
класса. Необходимо реализовать полиморфный вывод данного метода. Минимальное количество
студентов 5. На выходе мы должны получить список студентов в табличном виде (с заголовками столбцов)

Реализовать программу с интерактивным консольным меню.
Каждый пункт меню должен быть отдельным классом-наследником (подклассом).
1. Вывести все таблицы из базы данных MySQL.
2. Создать таблицу в базе данных MySQL.
3. Ввести данные о всех студентах и сохранить их в MySQL с последующим табличным
(форматированным) выводом в консоль.
4. Вывести данные о студенте по ID из MySQL.
5. Удалить данные о студенте из MySQL по ID.
6. Сохранить итоговые результаты из MySQL в Excel и вывести их в консоль. */

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class hard_10 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

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
        String studentTableName = "";

        while (running) {
            System.out.println("Выберите действие:");
            System.out.println("1. Вывести все таблицы из базы данных");
            System.out.println("2. Создать таблицу в базе данных");
            System.out.println("3. Ввести данные о всех студентах и сохранить их в MySQL с последующим табличным (форматированным) выводом в консоль.");
            System.out.println("4. Вывести данные о студенте по ID из MySQL.");
            System.out.println("5. Удалить данные о студенте из MySQL по ID.");
            System.out.println("6. Сохранить итоговые результаты из MySQL в Excel и вывести их в консоль.");
            System.out.println("0. Выход");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        try {
                            Statement statement = connection.createStatement();
                            ResultSet resultSet = statement.executeQuery("SHOW TABLES");
                            System.out.println("Таблицы в базе данных:");
                            while (resultSet.next()) {
                                String tableName = resultSet.getString(1);
                                System.out.println(tableName);
                            }
                        } catch (SQLException e) {
                            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                        }
                        break;

                    case 2:
                        System.out.println("Введите название таблицы:");
                        String tableName = scanner.nextLine();
                        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INT AUTO_INCREMENT PRIMARY KEY, faculty VARCHAR(255), fullName VARCHAR(255), group_name VARCHAR(255))";
                        try {
                            Statement statement = connection.createStatement();
                            statement.executeUpdate(createTableQuery);
                            System.out.println("Таблица успешно создана.");
                        } catch (SQLException e) {
                            System.out.println("Ошибка при создании таблицы: " + e.getMessage());
                        }
                        break;

                    case 3:
                        System.out.println("Введите название таблицы для ввода данных о студентах:");
                        studentTableName = scanner.nextLine();
                        Student.inputStudentData(connection, studentTableName, scanner);
                        break;

                    case 4:
                        System.out.println("Введите ID студента для вывода информации:");
                        int studentId = 0;
                        try {
                            studentId = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Ошибка! Введите целое число.");
                            break;
                        }

                        try {
                            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + studentTableName + " WHERE id = ?");
                            statement.setInt(1, studentId);
                            ResultSet resultSet = statement.executeQuery();

                            if (resultSet.next()) {
                                System.out.println("Данные студента ID_" + studentId + ":");
                                System.out.println("id: " + resultSet.getInt("id"));
                                System.out.println("faculty: " + resultSet.getString("faculty"));
                                System.out.println("fullName: " + resultSet.getString("fullName"));
                                System.out.println("group_name: " + resultSet.getString("group_name"));
                            } else {
                                System.out.println("Студент с ID " + studentId + " не найден.");
                            }
                        } catch (SQLException e) {
                            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                        }
                        break;

                    case 5:
                        System.out.println("Выберите студента по ID для удаления из БД:");
                        int studentIdToDelete = 0;
                        try {
                            studentIdToDelete = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Ошибка! Введите целое число.");
                            break;
                        }

                        try {
                            PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM " + studentTableName + " WHERE id = ?");
                            deleteStatement.setInt(1, studentIdToDelete);
                            int rowsAffected = deleteStatement.executeUpdate();

                            if (rowsAffected > 0) {
                                System.out.println("Студент с ID " + studentIdToDelete + " удален из БД.");
                            } else {
                                System.out.println("Студент с ID " + studentIdToDelete + " не найден в БД.");
                            }
                        } catch (SQLException e) {
                            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
                        }
                        break;

                    case 6:
                        try {
                            // Выполнение SQL-запроса для выборки данных из таблицы
                            PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM " + studentTableName);
                            ResultSet resultSet = selectStatement.executeQuery();

                            // Создание нового Excel-файла и листа для записи данных
                            Workbook workbook = new XSSFWorkbook();
                            Sheet sheet = workbook.createSheet("Students");

                            // Запись заголовков столбцов в первую строку Excel-файла
                            ResultSetMetaData metaData = resultSet.getMetaData();
                            int columnCount = metaData.getColumnCount();
                            Row headerRow = sheet.createRow(0);
                            for (int i = 1; i <= columnCount; i++) {
                                headerRow.createCell(i - 1).setCellValue(metaData.getColumnName(i));
                            }

                            // Запись данных из результата SQL-запроса в Excel-файл
                            int rowNum = 1;
                            while (resultSet.next()) {
                                Row row = sheet.createRow(rowNum++);
                                for (int i = 1; i <= columnCount; i++) {
                                    row.createCell(i - 1).setCellValue(resultSet.getString(i));
                                }
                            }

                            // Автоматическое выравнивание ширины ячеек по содержимому
                            for (int i = 0; i < columnCount; i++) {
                                sheet.autoSizeColumn(i);
                            }

                            // Сохранение Excel-файла на диск
                            String excelFilePath = "students.xlsx";
                            try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
                                workbook.write(outputStream);
                            }

                            // Открытие сохраненного файла и вывод его содержимого в консоль
                            System.out.println("Итоговые результаты сохранены в файл " + excelFilePath + ":");
                            System.out.println("ID\tFaculty\tFullName\tGroup");
                            for (int i = 1; i < rowNum; i++) {
                                Row row = sheet.getRow(i);
                                for (int j = 0; j < columnCount; j++) {
                                    System.out.print(row.getCell(j) + "\t");
                                }
                                System.out.println();
                            }

                            // Закрытие workbook
                            workbook.close();
                        } catch (SQLException | IOException e) {
                            System.out.println("Ошибка при выполнении запроса или сохранении Excel-файла: " + e.getMessage());
                        }
                        break;

                    case 0:
                        running = false;
                        System.out.println("Программа завершена.");
                        break;

                    default:
                        System.out.println("Некорректный ввод. Пожалуйста, попробуйте еще раз.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка! Введите целое число.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }
}

class Student {
    public static void inputStudentData(Connection connection, String tableName, Scanner scanner) {
        boolean validInput = false;
        while (!validInput) {
            System.out.println("Введите количество студентов (минимум 5):");
            int numOfStudents;
            try {
                numOfStudents = Integer.parseInt(scanner.nextLine());
                if (numOfStudents < 5) {
                    System.out.println("Не менее 5 студентов!");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Попробуйте еще раз.");
                continue;
            }

            validInput = true;

            String insertQuery = "INSERT INTO " + tableName + " (id, faculty, fullName, group_name) VALUES (?, ?, ?, ?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

                for (int i = 0; i < numOfStudents; i++) {
                    System.out.println("Введите ID студента " + (i + 1) + ":");
                    int id;
                    try {
                        id = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка! Введите целое число. Попробуйте еще раз.");
                        i--;
                        continue;
                    }

                    System.out.println("Введите факультет студента " + (i + 1) + ":");
                    String faculty = scanner.nextLine();

                    System.out.println("Введите ФИО студента " + (i + 1) + ":");
                    String fullName = scanner.nextLine();

                    System.out.println("Введите группу студента " + (i + 1) + ":");
                    String group = scanner.nextLine();

                    preparedStatement.setInt(1, id);
                    preparedStatement.setString(2, faculty);
                    preparedStatement.setString(3, fullName);
                    preparedStatement.setString(4, group);

                    preparedStatement.executeUpdate();
                }

                System.out.println("Данные о студентах успешно внесены в базу данных.");
            } catch (SQLException e) {
                System.out.println("Ошибка при вводе данных о студентах: " + e.getMessage());
            }
        }
    }
}
