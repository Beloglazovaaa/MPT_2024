package hard_15;

import java.sql.*;
import java.util.*;

public class DeleteElement {
    public static void deleteById(Scanner scanner, String tableName, Connection connection) {
        System.out.print("Введите ID элемента для удаления: ");
        int id = scanner.nextInt();

        String deleteQuery = "DELETE FROM " + tableName + " WHERE id = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(deleteQuery);
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Элемент с ID " + id + " успешно удалён из таблицы " + tableName + ".");
            } else {
                System.out.println("Элемент с ID " + id + " не найден в таблице " + tableName + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка при удалении элемента из таблицы.");
        }
    }
}