package hard_8;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class InputAndSaveMenuItem {
    private final Connection connection;

    public InputAndSaveMenuItem(Connection connection) {
        this.connection = connection;
    }

    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите количество работников:");
        int numberOfWorkers = scanner.nextInt();
        scanner.nextLine(); // съедаем перевод строки

        // Очистка таблиц перед вводом новых данных
        try {
            clearTables();
        } catch (SQLException e) {
            System.out.println("Ошибка при очистке таблиц: " + e.getMessage());
            return;
        }

        // Ввод и сохранение данных для каждого работника
        for (int i = 0; i < numberOfWorkers; i++) {
            System.out.println("Введите имя для работника " + (i + 1) + ":");
            String name = scanner.nextLine();
            System.out.println("Введите возраст для работника " + (i + 1) + ":");
            int age = scanner.nextInt();
            System.out.println("Введите зарплату для работника " + (i + 1) + ":");
            double salary = scanner.nextDouble();
            scanner.nextLine(); // съедаем перевод строки

            // Создаем объект работника и сохраняем его в базу данных
            Worker worker = new Worker(name, age, salary);
            saveWorkerToMySQL(worker);
        }

        System.out.println("Данные успешно сохранены в MySQL.");
    }

    private void clearTables() throws SQLException {
        // Очистка таблиц Worker и Student
        String clearWorkerQuery = "DELETE FROM Worker";
        String clearStudentQuery = "DELETE FROM Student";

        try (PreparedStatement clearWorkerStatement = connection.prepareStatement(clearWorkerQuery);
             PreparedStatement clearStudentStatement = connection.prepareStatement(clearStudentQuery)) {
            clearWorkerStatement.executeUpdate();
            clearStudentStatement.executeUpdate();
        }
    }

    private void saveWorkerToMySQL(Worker worker) {
        // Сохранение данных о работнике в таблицу Worker
        String insertWorkerQuery = "INSERT INTO Worker (name, age, salary) VALUES (?, ?, ?)";
        // Сохранение данных о студенте в таблицу Student
        String insertStudentQuery = "INSERT INTO Student (name, age) VALUES (?, ?)";

        try (PreparedStatement workerStatement = connection.prepareStatement(insertWorkerQuery);
             PreparedStatement studentStatement = connection.prepareStatement(insertStudentQuery)) {
            // Сохраняем данные о работнике
            workerStatement.setString(1, worker.getName());
            workerStatement.setInt(2, worker.getAge());
            workerStatement.setDouble(3, worker.getSalary());
            workerStatement.executeUpdate();

            // Сохраняем данные о студенте
            studentStatement.setString(1, worker.getName()); // Имя студента будет такое же, как у работника
            studentStatement.setInt(2, worker.getAge()); // Возраст студента будет такой же, как у работника
            studentStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка при сохранении данных в MySQL: " + e.getMessage());
        }
    }
}

