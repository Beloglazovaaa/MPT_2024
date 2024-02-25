///Базовый вариант. Ввести две строки (не менее 50 символов каждая) с клавиатуры. Необходимо
//вывести на экран две введенных ранее строки, подсчитать и вывести размер длины каждой строки,
//объединить данные строки в одну, сравнить данные строки и результат сравнения вывести на
//экран.

import java.util.Scanner;

public class base_2 {
    public static void main(String[] args) {

        int minLength = 5;
        Scanner in = new Scanner(System.in);
        System.out.println("Введите первую строку:");
        String n1 = in.nextLine();
        if (n1.length() < minLength) {
            throw new IllegalArgumentException("Строка должна быть минимум 50 символов");
        }else{
            System.out.println("Строка №1: " + n1);
            System.out.println("Длина строки №1: " + n1.length());
            System.out.println(" ");
        }

        System.out.println("Введите вторую строку:");
        String n2 = in.nextLine();
        if (n2.length() < minLength) {
            throw new IllegalArgumentException("Строка должна быть минимум 50 символов");
        }else {
            System.out.println("Строка №2: " + n2);
            System.out.println("Длина строки №2: " + n2.length());
            System.out.println(" ");
        }

        System.out.println("Объединение двух строк: " + n1 + n2);
        if (n1.length() > n2.length()) {
            System.out.println("Строка №1 " + n1 + " длиннее");
        }else {
            System.out.println("Строка №2 " + n2 + " длиннее");
        }
    }
}