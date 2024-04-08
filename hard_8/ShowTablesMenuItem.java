package hard_8;

import java.sql.*;
import java.util.Scanner;

class ShowTablesMenuItem {
    private final Connection connection;

    public ShowTablesMenuItem(Connection connection) {
        this.connection = connection;
    }

    public void execute() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, null, new String[]{"TABLE"});

            System.out.println("Таблицы в базе данных:");
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                System.out.println(tableName);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выводе таблиц: " + e.getMessage());
        }
    }
}