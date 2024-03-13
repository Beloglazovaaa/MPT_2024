import java.util.Scanner;

public class base_5 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите первую строку (не менее 50 символов):");
        String firstString = scanner.nextLine();

        System.out.println("Введите вторую строку (не менее 50 символов):");
        String secondString = scanner.nextLine();

        // Вывод введенных строк
        System.out.println("Первая строка: " + firstString);
        System.out.println("Вторая строка: " + secondString);

        // Переворот символов в строках
        StringBuilder reverseFirstString = new StringBuilder(firstString).reverse();
        StringBuilder reverseSecondString = new StringBuilder(secondString).reverse();

        // Объединение строк
        String mergedString = reverseFirstString.toString() + reverseSecondString.toString();

        // Вывод объединенной строки
        System.out.println("Объединенная строка: " + mergedString);

        scanner.close();
    }
}

