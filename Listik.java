import java.sql.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Listik {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        String tableName = "";

        String url = "jdbc:mysql://localhost:3305/";
        String dbName = "DB";
        String username = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url + dbName, username, password)) {
            LinkedList<Integer> numbers = new LinkedList<>();

            System.out.println("Введите числа (введите 'stop' для завершения):");

            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("stop")) {
                    break;
                }
                try {
                    numbers.add(Integer.parseInt(input));
                } catch (NumberFormatException e) {
                    System.out.println("Пожалуйста, введите корректное число или 'stop' для завершения.");
                }
            }
            createTable(scanner, connection, tableName);
            saveNumbersToDatabase(numbers, connection, tableName);
            displayNumbersFromDatabase(tableName, connection);
        }
    }

    private static void saveNumbersToDatabase(LinkedList<Integer> numbers, Connection connection, String tableName) {
        try {
            String query = "INSERT INTO " + tableName + " (number) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                for (int number : numbers) {
                    preparedStatement.setInt(1, number);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayNumbersFromDatabase(String tableName, Connection connection) {
        try {
            String query = "SELECT * FROM " + tableName;
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int number = resultSet.getInt("number");
                    System.out.println("ID: " + id + ", Number: " + number);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Scanner scanner, Connection connection, String tableName) {
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
}
