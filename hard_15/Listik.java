package hard_15;

import java.util.*;
import java.sql.*;

public class Listik {
    protected List<String> elements;

    public Listik() {
        elements = new ArrayList<>();
    }

    public void inputElements() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите 50 элементов:");

        while (elements.size() < 50) {
            String input = scanner.nextLine();
            elements.add(input);
        }
    }

    public List<String> getElements() {
        return elements;
    }
}

class Listik1 extends Listik {

    public void inputElements(String tableName, Connection connection) {
        super.inputElements();
        String listAsString = String.join(" ", elements);
        Set<String> elementSet = new HashSet<>(elements);

        saveToDatabase(tableName, connection, elements, listAsString, elementSet);
    }

    private void saveToDatabase(String tableName, Connection connection, List<String> list, String listAsString, Set<String> set) {
        try {
            String insertQuery = "INSERT INTO " + tableName + " (data_type, data_value) VALUES (?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {

                stmt.setString(1, "list");
                stmt.setString(2, list.toString());
                stmt.executeUpdate();

                stmt.setString(1, "string");
                stmt.setString(2, listAsString);
                stmt.executeUpdate();

                for (String element : set) {
                    stmt.setString(1, "set");
                    stmt.setString(2, element);
                    stmt.executeUpdate();
                }

                System.out.println("Данные успешно сохранены в базу данных.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка при подключении к базе данных.");
        }
    }
}
