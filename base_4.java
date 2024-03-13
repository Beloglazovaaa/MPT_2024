/* Ввести две строки (не менее 50 символов каждая) с клавиатуры.
Необходимо вывести на экран две введенных ранее строки, вернуть подстроку по индексам (substring(),
перевести все строки в верхний и нижний регистры, найти подстроку (тоже вводить с клавиатуры)
и определить: заканчивается ли строка данной подстрокой (endsWith)).

1. Вывести все таблицы из MySQL.
2. Создать таблицу в MySQL.
3. Возвращение подстроки по индексам, результат сохранить в MySQL с последующим выводом в
консоль.
4. Перевод строк в верхний и нижний регистры, результат сохранить в MySQL с последующим выводом
в консоль.
5. Поиск подстроки и определение окончания подстроки, результат сохранить в MySQL с последующим
выводом в консоль.
6. Сохранить все данные (вышеполученные результаты) из MySQL в Excel и вывести на экран. */

import java.util.Scanner;

public class base_4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ввод первой строки
        String firstString;
        do {
            System.out.print("Введите первую строку (не менее 50 символов): ");
            firstString = scanner.nextLine();
        } while (firstString.length() < 50);

        // Ввод второй строки
        String secondString;
        do {
            System.out.print("Введите вторую строку (не менее 50 символов): ");
            secondString = scanner.nextLine();
        } while (secondString.length() < 50);

        // Вывод обеих строк
        System.out.println("Первая строка: " + firstString);
        System.out.println("Вторая строка: " + secondString);

        // Перевод всех строк в верхний регистр
        String firstStringUpper = firstString.toUpperCase();
        String secondStringUpper = secondString.toUpperCase();
        System.out.println("Первая строка в верхнем регистре: " + firstStringUpper);
        System.out.println("Вторая строка в верхнем регистре: " + secondStringUpper);

        // Перевод всех строк в нижний регистр
        String firstStringLower = firstString.toLowerCase();
        String secondStringLower = secondString.toLowerCase();
        System.out.println("Первая строка в нижнем регистре: " + firstStringLower);
        System.out.println("Вторая строка в нижнем регистре: " + secondStringLower);

        // Ввод начального индекса подстроки для первой строки
        int startIndexFirst;
        do {
            System.out.print("Введите начальный индекс подстроки для первой строки: ");
            startIndexFirst = scanner.nextInt();
        } while (startIndexFirst < 0 || startIndexFirst >= firstString.length());

        // Ввод конечного индекса подстроки для второй строки
        int endIndexFirst;
        do {
            System.out.print("Введите конечный индекс подстроки для первой строки (больше " + startIndexFirst + "): ");
            endIndexFirst = scanner.nextInt();
        } while (endIndexFirst <= startIndexFirst || endIndexFirst > firstString.length());

        // Получение подстроки из второй строки
        String substringFirst = firstString.substring(startIndexFirst, endIndexFirst);
        System.out.println("Подстрока для первой строки с индексами от " + startIndexFirst + " до " + endIndexFirst + ": " + substringFirst);

        // Проверка, заканчивается ли первая строка данной подстрокой
        scanner.nextLine(); // Очистка буфера после ввода чисел
        System.out.print("Введите подстроку для поиска в первой строке: ");
        String searchSubstringFirst = scanner.nextLine();
        boolean endsWithStringFirst = firstString.endsWith(searchSubstringFirst);
        if (endsWithStringFirst) {
            System.out.println("Первая строка заканчивается на введенную подстроку.");
        } else {
            System.out.println("Первая строка не заканчивается на введенную подстроку.");
        }

        // Ввод начального индекса подстроки для второй строки
        int startIndexSecond;
        do {
            System.out.print("Введите начальный индекс подстроки для второй строки: ");
            startIndexSecond = scanner.nextInt();
        } while (startIndexSecond < 0 || startIndexSecond >= secondString.length());

        // Ввод конечного индекса подстроки для второй строки
        int endIndexSecond;
        do {
            System.out.print("Введите конечный индекс подстроки для второй строки (больше " + startIndexSecond + "): ");
            endIndexSecond = scanner.nextInt();
        } while (endIndexSecond <= startIndexSecond || endIndexSecond > secondString.length());

        // Получение подстроки из второй строки
        String substringSecond = secondString.substring(startIndexSecond, endIndexSecond);
        System.out.println("Подстрока для второй строки с индексами от " + startIndexSecond + " до " + endIndexSecond + ": " + substringSecond);

        // Проверка, заканчивается ли вторая строка данной подстрокой
        scanner.nextLine(); // Очистка буфера после ввода чисел
        System.out.print("Введите подстроку для поиска во второй строке: ");
        String searchSubstringSecond = scanner.nextLine();
        boolean endsWithStringSecond = secondString.endsWith(searchSubstringSecond);
        if (endsWithStringSecond) {
            System.out.println("Вторая строка заканчивается на введенную подстроку.");
        } else {
            System.out.println("Вторая строка не заканчивается на введенную подстроку.");
        }

        scanner.close();
    }
}

