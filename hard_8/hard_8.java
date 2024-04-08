package hard_8;
/* Объектно-ориентированное программирование. Сделайте абстрактный класс Student,
в котором будут следующие поля (private): name (имя), age (возраст). Также создайте public-методы
setName, getName, setAge, getAge для передачи и присвоения значений в классе-наследнике Worker. В
данном классе реализуйте метод, который вносит дополнительное private-поле salary (зарплата), а также
методы public getSalary и setSalary для передачи и присвоения значений зарплаты. На выходе мы должны
получить имя и возраст студента, а также его предполагаемую зарплату.

Реализовать программу с интерактивным консольным меню.
Каждый пункт меню должен быть отдельным классом-наследником (подклассом).
1. Вывести все таблицы из базы данных MySQL.
2. Создать таблицу в базе данных MySQL.
3. Ввод с клавиатуры значений ВСЕХ полей, сохранить их в MySQL с последующим выводом в консоль.
4. Сохранение всех результатов в MySQL с последующим выводом в консоль.
5. Сохранить результаты из MySQL в Excel и вывести их в консоль. */

import java.sql.*;

public class hard_8 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3305/";
        String dbName = "DB";
        String username = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Подключение к базе данных успешно!");

            // Выбираем базу данных
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("USE " + dbName);
            }

            // Создание объекта меню
            MenuHandler menuHandler = new MenuHandler(connection);
            // Запуск меню
            menuHandler.runMenu();
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }
}
