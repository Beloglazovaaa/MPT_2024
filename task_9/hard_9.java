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

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Подключение к базе данных успешно!");

            int[][] firstMatrix = null;
            int[][] secondMatrix = null;
            int[][] resultMatrix = null;

            while (running) {
                System.out.println("1. Вывести все таблицы из MySQL.");
                System.out.println("2. Создать таблицу в MySQL.");
                System.out.println("3. Ввести две матрицы с клавиатуры и каждую из них сохранить в MySQL с последующим выводом в консоль.");
                System.out.println("4. Перемножить, сложить, вычесть, возвести в степень матрицы, а также сохранить результаты в MySQL\n" +
                        "и вывести в консоль.");
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

                        System.out.println("Введите значения для второй матрицы (размер 7x7):");
                        secondMatrix = ArrayPI_9.readMatrixFromInput(scanner, 7, 7);

                        System.out.println("Результат умножения матриц:");
                        resultMatrix = MatrixMultiplier.multiplyMatrices(firstMatrix, secondMatrix);
                        MatrixMultiplier.printMatrix(resultMatrix);

                        System.out.println("Результат сложения матриц:");
                        MatrixAdder adder = new MatrixAdder(firstMatrix, secondMatrix);
                        adder.addAndPrintResult();

                        System.out.println("Результат вычитания матриц:");
                        MatrixSubtractor subtractor = new MatrixSubtractor(firstMatrix, secondMatrix);
                        subtractor.subtractAndPrintResult();

                        System.out.println("Введите степень для возведения матрицы:");
                        int power = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Результат возведения матрицы в степень:");
                        MatrixPower powerer = new MatrixPower(firstMatrix, power);
                        powerer.powerAndPrintResult();

                        ArrayPI_9.saveMatrixToDatabase(connection, resultMatrix, tableName);
                        break;

                    case 4:
                        if (firstMatrix != null && secondMatrix != null) {
                            System.out.println("Выберите операцию:");
                            System.out.println("1. Сложение матриц");
                            System.out.println("2. Вычитание матриц");
                            System.out.println("3. Умножение матриц");
                            System.out.println("4. Возведение матрицы в степень");
                            int operationChoice = scanner.nextInt();
                            scanner.nextLine();

                            switch (operationChoice) {
                                case 1:
                                    System.out.println("Результат сложения матриц:");
                                    MatrixAdder adderCase4 = new MatrixAdder(firstMatrix, secondMatrix);
                                    adderCase4.addAndPrintResult();
                                    break;
                                case 2:
                                    System.out.println("Результат вычитания матриц:");
                                    MatrixSubtractor subtractorCase4 = new MatrixSubtractor(firstMatrix, secondMatrix);
                                    subtractorCase4.subtractAndPrintResult();
                                    break;
                                case 3:
                                    System.out.println("Результат умножения матриц:");
                                    MatrixMultiplier MultiplyCase4 = new MatrixMultiplier (firstMatrix, secondMatrix);
                                    MultiplyCase4.multiplyAndPrintResult();
                                case 4:
                                    System.out.println("Введите степень для возведения матрицы:");
                                    int powerCase4 = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.println("Результат возведения матрицы в степень:");
                                    MatrixPower powererCase4 = new MatrixPower(firstMatrix, powerCase4);
                                    powererCase4.powerAndPrintResult();
                                    break;
                                default:
                                    System.out.println("Некорректный выбор операции.");
                            }
                        } else {
                            System.out.println("Пожалуйста, сначала введите значения для обеих матриц.");
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