package task_14;

import java.util.*;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class listik {
    public List<Integer> random() {
        List<Integer> randomList = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {
            randomList.add(rand.nextInt());
        }
        return randomList;
    }

    protected List<Integer> input() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите количество элементов списка: ");
        int count = scanner.nextInt();
        List<Integer> inputList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            System.out.println("Введите элемент " + (i + 1) + ": ");
            inputList.add(scanner.nextInt());
        }
        return inputList;
    }

    public boolean containsNumber(List<Integer> list, int number) {
        return list.contains(number);
    }
}
