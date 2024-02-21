/*Базовый вариант. Реализовать программу для выполнения следующих
математических операции с целочисленным, байтовым и вещественным типами данных:
сложение, вычитание, умножение, деление, деление по модулю (остаток),
модуль числа, возведение в степень. Все данные вводятся с клавиатуры
(класс Scanner, System.in,*/

import java.util.Scanner;

public class base_1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите первое число:");
        int num1 = scanner.nextInt();

        System.out.println("Введите второе число:");
        int num2 = scanner.nextInt();

        // Сложение
        int sum = num1 + num2;
        System.out.println("Сумма: " + sum);

        // Вычитание
        int difference = num1 - num2;
        System.out.println("Разность: " + difference);

        // Умножение
        int multiplication = num1 * num2;
        System.out.println("Произведение: " + multiplication);

        // Деление
        if (num2 != 0) {
            double division = (double) num1 / num2;
            System.out.println("Частное: " + division);
        } else {
            System.out.println("Деление на ноль невозможно");
        }

        // Деление по модулю (остаток)
        if (num2 != 0) {
            int modulus = num1 % num2;
            System.out.println("Остаток от деления: " + modulus);
        } else {
            System.out.println("Деление на ноль невозможно");
        }

        // Модуль числа
        int absNum1 = Math.abs(num1);
        int absNum2 = Math.abs(num2);
        System.out.println("Модуль первого числа: " + absNum1);
        System.out.println("Модуль второго числа: " + absNum2);

        // Возведение в степень
        double power = Math.pow(num1, num2);
        System.out.println("Первое число в степени второго числа: " + power);
    }
}