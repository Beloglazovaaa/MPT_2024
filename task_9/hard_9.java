/* Объектно-ориентированное программирование. Необходимо создать класс ArrayPI
(модификатор доступа - public), в котором необходимо создать два двухмерных массива (ввод с
клавиатуры, 7 столбцов и 7 строк). Далее необходимо создать классы-наследники (у всех - модификатор
доступа - public final, каждая операция с матрицами - отдельный класс-наследник), в которых будут
наследоваться данные матрицы и: перемножаться, складываться, вычитаться, возводиться в степень. После
выполнения каждого класса необходимо выводить итоговый результат.

Реализовать программу с интерактивным консольным меню.
Каждый пункт меню должен быть отдельным классом-наследником (подклассом).
1. Вывести все таблицы из базы данных MySQL.
2. Создать таблицу в базе данных MySQL.
3. Ввести две матрицы с клавиатуры и каждую из них сохранить в MySQL с последующим
форматированным выводом в консоль.
4. Перемножить, сложить, вычесть, возвести в степень матрицы, а также сохранить результаты в MySQL c выводом в консоль.
5. Сохранить результаты из MySQL в Excel и вывести их в консоль. */

package task_9;

import java.sql.*;
import java.util.Scanner;
import java.sql.SQLException;



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
            int[][] productMatrix = null;
            int[][] sumMatrix = null;
            int[][] differenceMatrix = null;
            int[][] powerMatrix1 = null;
            int[][] powerMatrix2 = null;

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
                        // Ввод и сохранение первой матрицы в базу данных
                        System.out.println("Введите значения для первой матрицы (размер 7x7):");
                        firstMatrix = ArrayPI_9.readMatrixFromInput(scanner, 7, 7);
                        ArrayPI_9.saveMatrixToDatabase(connection, firstMatrix, tableName);
                        System.out.println("Первая матрица успешно сохранена в базе данных.");

                        // Ввод и сохранение второй матрицы в базу данных
                        System.out.println("Введите значения для второй матрицы (размер 7x7):");
                        secondMatrix = ArrayPI_9.readMatrixFromInput(scanner, 7, 7);
                        ArrayPI_9.saveMatrixToDatabase(connection, secondMatrix, tableName);
                        System.out.println("Вторая матрица успешно сохранена в базе данных.");

                        // Вывод сохраненных матриц в консоль
                        System.out.println("Первая матрица:");
                        ArrayPI_9.printMatrix(firstMatrix);
                        System.out.println("Вторая матрица:");
                        ArrayPI_9.printMatrix(secondMatrix);
                        break;


                    case 4:
                        if (firstMatrix != null && secondMatrix != null) {
                            // Выполнение операций над матрицами
                            MatrixAdder adder = new MatrixAdder(firstMatrix, secondMatrix);
                            sumMatrix = adder.getResultMatrix();
                            MatrixSubtractor subtractor = new MatrixSubtractor(firstMatrix, secondMatrix);
                            differenceMatrix = subtractor.getResultMatrix();
                            MatrixMultiplier multiplier = new MatrixMultiplier(firstMatrix, secondMatrix);
                            productMatrix = multiplier.getResultMatrix();
                            System.out.println("Введите степень для возведения матриц в эту степень:");
                            int power = scanner.nextInt();
                            scanner.nextLine();
                            MatrixPower powerer = new MatrixPower(firstMatrix, secondMatrix, power);

// Сохранение результатов операций в базу данных
                            ArrayPI_9.saveMatrixToDatabase(connection, sumMatrix, tableName);
                            ArrayPI_9.saveMatrixToDatabase(connection, differenceMatrix, tableName);
                            ArrayPI_9.saveMatrixToDatabase(connection, productMatrix, tableName);
                            ArrayPI_9.saveMatrixToDatabase(connection, powerer.getResultMatrix1(), tableName);
                            ArrayPI_9.saveMatrixToDatabase(connection, powerer.getResultMatrix2(), tableName);

                            // Вывод результатов в консоль
                            System.out.println("Сумма матриц:");
                            ArrayPI_9.printMatrix(sumMatrix);
                            System.out.println("Разность матриц:");
                            ArrayPI_9.printMatrix(differenceMatrix);
                            System.out.println("Произведение матриц:");
                            ArrayPI_9.printMatrix(productMatrix);
                            System.out.println("Первая матрица в степени " + power + ":");
                            ArrayPI_9.printMatrix(powerer.getResultMatrix1());
                            System.out.println("Вторая матрица в степени " + power + ":");
                            ArrayPI_9.printMatrix(powerer.getResultMatrix2());
                        } else {
                            System.out.println("Пожалуйста, сначала введите значения для обеих матриц.");
                        }
                        break;


                    case 5:
                        ArrayPI_9.saveDataToExcel(tableName, firstMatrix, secondMatrix, productMatrix, sumMatrix, differenceMatrix, powerMatrix1, powerMatrix2);
                        System.out.println("Данные успешно сохранены в файл hard_9.xlsx");
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
