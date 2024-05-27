package hard_15;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class CreateTable {
    public static String createTable(Scanner scanner, Connection connection) {
        System.out.println("Введите название таблицы: ");
        String tableName = scanner.nextLine();

        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName +
                "(id INT AUTO_INCREMENT PRIMARY KEY, data_type VARCHAR(50) NOT NULL, data_value TEXT NOT NULL)";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableQuery);
            System.out.println("Таблица успешно создана.");

        } catch (SQLException e) {
            System.out.println("Ошибка при создании таблицы: " + e.getMessage());
        }
        return tableName;
    }
}
