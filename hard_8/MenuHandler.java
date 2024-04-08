package hard_8;

import java.sql.Connection;
import java.util.Scanner;

class MenuHandler {
    private final Connection connection;

    public MenuHandler(Connection connection) {
        this.connection = connection;
    }

    public void runMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("1. Вывести все таблицы из базы данных MySQL.");
            System.out.println("2. Создать таблицу в базе данных MySQL.");
            System.out.println("3. Ввод с клавиатуры значений ВСЕХ полей, сохранить их в MySQL с последующим выводом в консоль.");
            System.out.println("4. Сохранение всех результатов в MySQL с последующим выводом в консоль.");
            System.out.println("5. Сохранить результаты из MySQL в Excel и вывести их в консоль.");
            System.out.println("0. Выйти из программы.");
            System.out.println("Выберите действие: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    new ShowTablesMenuItem(connection).execute();
                    break;

                case 2:
                    new CreateTableMenuItem(connection).execute();
                    break;

                case 3:
                    new InputAndSaveMenuItem(connection).execute(); // Вызываем метод execute() для пункта 3
                    break;

                case 4:
                    new SaveAllResultsMenuItem(connection).execute();
                    break;

                case 5:
                    new SaveToExcelMenuItem(connection).execute();
                    break;

                case 0:
                    running = false;
                    break;

                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }
}
