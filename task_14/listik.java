package task_14;

import java.util.*;

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
        System.out.println("Введите минимальное значение диапазона: ");
        int min = scanner.nextInt();
        System.out.println("Введите максимальное значение диапазона: ");
        int max = scanner.nextInt();

        List<Integer> inputList = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            inputList.add(min + rand.nextInt(max - min + 1));
        }
        return inputList;
    }

    public boolean containsNumber(List<Integer> list, int number) {
        return list.contains(number);
    }
}
