package hard_8;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

class CreateTableMenuItem {
    private final Connection connection;

    public CreateTableMenuItem(Connection connection) {
        this.connection = connection;
    }

    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название таблицы: ");
        String tableName = scanner.nextLine();

        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName +
                "(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), age INT)";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableQuery);
            System.out.println("Таблица успешно создана.");
        } catch (SQLException e) {
            System.out.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }
}
