/* Объектно-ориентированное программирование.
1. Разработать программу, в которой требуется создать класс, описывающий геометрическую фигуру - треугольник.
Методами класса должны быть - вычисление площади, периметра.
Создать класс-наследник, определяющий прямоугольный треугольник.

2. Разработать программу, в которой требуется создать класс с модификатором доступа Public.
В данном классе необходимо создать метод реализующий вычисление четных и нечетных факториалов.

Объектно-ориентированное программирование. Реализовать программу интерактивным консольным меню.
Каждый пункт меню должен быть отдельным классом-наследником (подклассом).
1. Вывести все таблицы из базы данных MySQL.
2. Создать таблицу в базе данных MySQL.
3. Решение задач, сохранение результатов в MySQL.
4. Вывод данных с условием: вывести данные по ID строки. Каждая строка - результаты, сохраненные в
MySQL в ходе решения подзадач Nº1 и Nº2 базового варианта.
5. Сохранить результаты из MySQL в Excel (заголовки столбцов должны присутствовать при экспорте данных из MySQL в Excel) и вывести их в консоль. */

import java.sql.*;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class hard_11 {
    private static String tableName = "";

    public static void main(String[] args) {
        String excelFilePath = "hard_11.xlsx";
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
            System.out.println("3. Решение базового варианта, сохранение результатов в MySQL.");
            System.out.println("4. Вывод данных с условием: вывести данные по ID строки. Каждая строка – результаты, сохраненные в MySQL в ходе решения подзадач №1 и №2 базового варианта.");
            System.out.println("5. Сохранить результаты из MySQL в Excel (заголовки столбцов должны присутствовать при экспорте данных из MySQL в Excel) и вывести их в консоль.");
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
                    thirdTask(scanner, connection);
                    break;
                case 4:
                    fourthTask(scanner, connection);
                    break;
                case 5:
                    fifthTask(scanner, connection);
                    break;
                case 0:
                    running = false;
                    System.out.println("Программа завершена.");
                    break;
            }
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
                "(id INT AUTO_INCREMENT PRIMARY KEY, number INT, even_factorial BIGINT, odd_factorial BIGINT, perimeter DOUBLE, area DOUBLE,rightPerimeter DOUBLE, rightArea DOUBLE)";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableQuery);
            System.out.println("Таблица успешно создана.");
        } catch (SQLException e) {
            System.out.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    private static void thirdTask(Scanner scanner, Connection connection) {
        System.out.println("Введите длину первой стороны треугольника:");
        double side1 = scanner.nextDouble();
        System.out.println("Введите длину второй стороны треугольника:");
        double side2 = scanner.nextDouble();
        System.out.println("Введите длину третьей стороны треугольника:");
        double side3 = scanner.nextDouble();

        // Создание объекта базового класса "Треугольник"
        Triangle triangle = new Triangle(side1, side2, side3);

        // Вывод периметра и площади треугольника
        double perimeter = triangle.calculatePerimeter();
        double area = triangle.calculateArea();
        System.out.println("Периметр треугольника: " + perimeter);
        System.out.println("Площадь треугольника: " + area);

        // Создание объекта класса-наследника "Прямоугольный треугольник"
        RightTriangle rightTriangle = new RightTriangle(side1, side2);

        // Вывод периметра и площади прямоугольного треугольника
        double rightPerimeter = rightTriangle.calculatePerimeter();
        double rightArea = rightTriangle.calculateArea();
        System.out.println("Периметр прямоугольного треугольника: " + rightPerimeter);
        System.out.println("Площадь прямоугольного треугольника: " + rightArea);
        System.out.println("Введите число для вычисления четных и нечетных факториалов:");
        int number = scanner.nextInt();
        FactorialCalculator calculator = new FactorialCalculator();
        Long[] result = calculator.calculateFactorials(number);
//        saveFactorialResults(connection, number, result[0], result[1]);
        try {
            String query = "INSERT INTO " + tableName + " (number, even_factorial, odd_factorial, perimeter, area, rightPerimeter, rightArea) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, number);
            statement.setLong(2, result[0]);
            statement.setLong(3, result[1]);
            statement.setDouble(4, perimeter);
            statement.setDouble(5, area);
            statement.setDouble(6, rightPerimeter);
            statement.setDouble(7, rightArea);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName +
//                "(id INT AUTO_INCREMENT PRIMARY KEY, number INT, even_factorial BIGINT, odd_factorial BIGINT, perimeter DOUBLE, area DOUBLE,rightPerimeter DOUBLE, rightArea DOUBLE)";

    }

    private static void fourthTask(Scanner scanner, Connection connection) {
        try {
            String query = "SELECT * FROM " + tableName + " WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, 1); // Set the ID value you want to retrieve
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int number = resultSet.getInt("number");
                long evenFactorial = resultSet.getLong("even_factorial");
                long oddFactorial = resultSet.getLong("odd_factorial");
                double perimeter = resultSet.getDouble("perimeter");
                double area = resultSet.getDouble("area");

                System.out.println("ID: " + id);
                System.out.println("Число: " + number);
                System.out.println("Чётный факториал: " + evenFactorial);
                System.out.println("Нечётный факториал: " + oddFactorial);
                System.out.println("Периметр: " + perimeter);
                System.out.println("Площадь: " + area);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void fifthTask(Scanner scanner, Connection connection) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");

            Row headerRow = sheet.createRow(0);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                Cell cell = headerRow.createCell(i - 1);
                cell.setCellValue(metaData.getColumnName(i));
            }

            int rowNum = 1;
            while (resultSet.next()) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 1; i <= columnCount; i++) {
                    Cell cell = row.createCell(i - 1);
                    cell.setCellValue(resultSet.getString(i));
                }
            }

            for (int i = 0; i < columnCount; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream("hard_11.xlsx")) {
                workbook.write(fileOut);
                System.out.println("Excel файл был успешно создан!");
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}

// Базовый класс "Треугольник"
class Triangle {
    protected double side1, side2, side3;

    // Конструктор для инициализации сторон треугольника
    public Triangle(double side1, double side2, double side3) {
        this.side1 = side1;
        this.side2 = side2;
        this.side3 = side3;
    }

    // Метод для вычисления периметра треугольника
    public double calculatePerimeter() {
        return side1 + side2 + side3;
    }

    // Метод для вычисления площади треугольника по формуле Герона
    public double calculateArea() {
        double s = (side1 + side2 + side3) / 2;
        return Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
    }
}

// Класс-наследник "Прямоугольный треугольник"
class RightTriangle extends Triangle {
    // Конструктор для инициализации сторон треугольника
    public RightTriangle(double side1, double side2) {
        super(side1, side2, Math.sqrt(side1 * side1 + side2 * side2));
    }
}

class FactorialCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите число для вычисления четных и нечетных факториалов:");
        int number = scanner.nextInt();

        FactorialCalculator calculator = new FactorialCalculator();
        calculator.calculateFactorials(number);

        scanner.close();
    }

    public Long[] calculateFactorials(int number) {
        long evenFactorial = 1;
        long oddFactorial = 1;

        // Вычисление четного факториала
        for (int i = 2; i <= number; i += 2) {
            evenFactorial *= i;
        }

        // Вычисление нечетного факториала
        for (int i = 1; i <= number; i += 2) {
            oddFactorial *= i;
        }

        System.out.println("Четный факториал числа " + number + ": " + evenFactorial);
        System.out.println("Нечетный факториал числа " + number + ": " + oddFactorial);
        Long[] facts = new Long[2];
        facts[0] = evenFactorial;
        facts[1] = oddFactorial;
        return facts;
    }
}