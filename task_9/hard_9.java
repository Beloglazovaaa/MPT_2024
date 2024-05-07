package task_9;

import java.sql.*;
import java.util.Scanner;


public class hard_9 {
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

            int[][] firstMatrix = null;
            int[][] secondMatrix = null;

            while (running) {
                System.out.println("1. Вывести все таблицы из MySQL.");
                System.out.println("2. Создать таблицу в MySQL.");
                System.out.println("3. Ввести две матрицы с клавиатуры и каждую из них сохранить в MySQL с последующим выводом в консоль.");
                System.out.println("4. Перемножить, сложить, вычесть, возвести в степень матрицы, а также сохранить результаты в MySQL\n" +
                        "и вывести в консоль.");
                System.out.println("5. Сохранить результаты из MySQL в Excel и вывести их в консоль.");
                System.out.println("0. Выйти из программы.");
                System.out.println("Выберите действие: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        try (Statement statement = connection.createStatement()) {
                            statement.executeUpdate("USE " + dbName);
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
                        tableName = scanner.next();

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
                        System.out.println("Введите значения для первой матрицы (размер 7x7):");
                        firstMatrix = ArrayPI_9.readMatrixFromInput(scanner, 7, 7);
                        System.out.println("Первая матрица:");
                        MatrixMultiplier.printMatrix(firstMatrix);

                        System.out.println("Введите значения для второй матрицы (размер 7x7):");
                        secondMatrix = ArrayPI_9.readMatrixFromInput(scanner, 7, 7);
                        System.out.println("Вторая матрица:");
                        MatrixMultiplier.printMatrix(secondMatrix);

                        // Сохранение результатов в базу данных
                        ArrayPI_9.saveMatrixToDatabase(connection, firstMatrix, tableName + "_1");
                        ArrayPI_9.saveMatrixToDatabase(connection, secondMatrix, tableName + "_2");

                        break;

                    case 4:
                        if (firstMatrix != null && secondMatrix != null) {
                            // Сложение матриц
                            MatrixAdder adder = new MatrixAdder(firstMatrix, secondMatrix);
                            int[][] sumMatrix = adder.getResultMatrix();
                            adder.addAndPrintResult();
                            ArrayPI_9.saveMatrixToDatabase(connection, sumMatrix, tableName + "_addition");

                            // Вычитание матриц
                            MatrixSubtractor subtractor = new MatrixSubtractor(firstMatrix, secondMatrix);
                            int[][] differenceMatrix = subtractor.getResultMatrix();
                            subtractor.subtractAndPrintResult();
                            ArrayPI_9.saveMatrixToDatabase(connection, differenceMatrix, tableName + "_subtraction");

                            // Умножение матриц
                            MatrixMultiplier multiplier = new MatrixMultiplier(firstMatrix, secondMatrix);
                            int[][] productMatrix = multiplier.getResultMatrix();
                            multiplier.multiplyAndPrintResult();
                            ArrayPI_9.saveMatrixToDatabase(connection, productMatrix, tableName + "_multiplication");

                            // Возведение матриц в степень
                            System.out.println("Введите степень для возведения матриц в эту степень:");
                            int power = scanner.nextInt();
                            scanner.nextLine();

                            MatrixPower powerer = new MatrixPower(firstMatrix, secondMatrix, power);
                            powerer.powerAndPrintResult();

                            // Сохранение результатов возведения в степень в базу данных
                            ArrayPI_9.saveMatrixToDatabase(connection, powerer.getResultMatrix1(), tableName + "_power1");
                            ArrayPI_9.saveMatrixToDatabase(connection, powerer.getResultMatrix2(), tableName + "_power2");
                        } else {
                            System.out.println("Пожалуйста, сначала введите значения для обеих матриц.");
                        }
                        break;

                    case 5:
                        System.out.println("Введите название таблицы для экспорта в Excel:");
                        String exportTableName = scanner.nextLine();

                        try {
                            Connection connection1= DriverManager.getConnection(url, username, password);
                            ArrayPI_9.exportTableToExcel(connection, exportTableName);
                        } catch (SQLException e) {
                            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
                        }
                        break;


                    case 0:
                        running = false;
                        System.out.println("Программа завершена.");
                        break;

                    default:
                        System.out.println("Некорректный выбор. Попробуйте снова.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
