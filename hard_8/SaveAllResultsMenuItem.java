package hard_8;

import java.sql.*;
class SaveAllResultsMenuItem {
    private final Connection connection;

    public SaveAllResultsMenuItem(Connection connection) {
        this.connection = connection;
    }

    public void execute() {
        try {
            // Выполняем запрос для выборки всех данных из таблиц Worker и Student
            String query = "SELECT * FROM Worker";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet workerResultSet = preparedStatement.executeQuery();

            // Выводим данные о работниках
            System.out.println("Данные о работниках из таблицы Worker:");
            while (workerResultSet.next()) {
                String name = workerResultSet.getString("name");
                int age = workerResultSet.getInt("age");
                double salary = workerResultSet.getDouble("salary");
                System.out.println("Имя: " + name + ", Возраст: " + age + ", Зарплата: " + salary);
            }

            // Выполняем запрос для выборки всех данных из таблицы Student
            query = "SELECT * FROM Student";
            preparedStatement = connection.prepareStatement(query);
            ResultSet studentResultSet = preparedStatement.executeQuery();

            // Выводим данные о студентах
            System.out.println("Данные о студентах из таблицы Student:");
            while (studentResultSet.next()) {
                String name = studentResultSet.getString("name");
                int age = studentResultSet.getInt("age");
                System.out.println("Имя: " + name + ", Возраст: " + age);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при сохранении всех результатов в MySQL с последующим выводом в консоль: " + e.getMessage());
        }
    }
}
