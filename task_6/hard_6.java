/* Объектно-ориентированное программирование. Необходимо создать класс ArrayPI
(модификатор доступа - public), в котором необходимо создать два двухмерных массива
(ввод с клавиатуры, 7 столбцов и 7 строк). Далее необходимо создать класс-наследник Matrix
(модификатор доступа - public final), в котором будут наследоваться данные матрицы и перемножаться.
Необходимо перемножить две данных матрицы и итоговую матрицу (произведение двух матриц) вывести на экран.

Реализовать программу с интерактивным консольным меню:
1. Вывести все таблицы из базы данных MySQL.
2. Создать таблицу в базе данных MySQL.
3. Ввести две матрицы с клавиатуры и каждую из них сохранить в MySQL с последующим выводом в
консоль.
4. Перемножить матрицу, сохранить перемноженную матрицу в MySQL и вывести в консоль.
5. Сохранить результаты из MySQL в Excel и вывести их в консоль. */

package task_6;

import java.sql.*;
import java.util.Scanner;

public class hard_6 {
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
                System.out.println("4. Сохранить результаты из MySQL в Excel и вывести их в консоль.");
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
                        firstMatrix = ArrayPI_6.readMatrixFromInput(scanner, 7, 7);

                        System.out.println("Введите значения для второй матрицы (размер 7x7):");
                        secondMatrix = ArrayPI_6.readMatrixFromInput(scanner, 7, 7);

                        break;

                    case 4:
                        if (firstMatrix != null && secondMatrix != null && resultMatrix != null) {
                            ArrayPI_6.saveDataToExcel(tableName, firstMatrix, secondMatrix, resultMatrix);
                        } else {
                            System.out.println("Пожалуйста, сначала введите значения для обеих матриц и выполните умножение.");
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

