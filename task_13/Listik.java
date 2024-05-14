package task_13;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Listik {
    public List<Integer> random() {
        List<Integer> randomList = new ArrayList<>();
        Random rand = new

                Random();
        for (int i = 0; i < 1000; i++) {
            randomList.add(rand.nextInt());
        }
        return randomList;
    }

    protected List<String> input() {
        List<String> inputList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите 10 строк:");
        for (int i = 0; i < 10; i++) {
            inputList.add(scanner.nextLine());
        }
        return inputList;
    }
}