package task_14;

import java.util.*;
public class listik {
    public List<Map.Entry<Integer, Integer>> random() {
        List<Map.Entry<Integer, Integer>> randomList = new ArrayList<>();
        Random rand = new Random();
        for (int i = 1; i <= 1000; i++) {
            randomList.add(new AbstractMap.SimpleEntry<>(i, rand.nextInt()));
        }
        return randomList;
    }

    protected List<Integer> input() {
        List<Integer> inputList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите количество элементов списка (до 10000): ");
        int count;
        while (true) {
            count = scanner.nextInt();
            if (count > 0 && count <= 10000) {
                break;
            } else {
                System.out.println("Ошибка: введите количество элементов от 1 до 10000: ");
            }
        }
        System.out.println("Введите минимальное значение: ");
        int minValue = scanner.nextInt();
        System.out.println("Введите максимальное значение: ");
        int maxValue = scanner.nextInt();
        System.out.println("Введите элементы списка: ");
        for (int i = 0; i < count; i++) {
            int number;
            while (true) {
                number = scanner.nextInt();
                if (number >= minValue && number <= maxValue) {
                    inputList.add(number);
                    break;
                } else {
                    System.out.println("Ошибка: введите число в диапазоне от " + minValue + " до " + maxValue + ": ");
                }
            }
        }
        return inputList;
    }

    public boolean containsNumber(List<Integer> list, int number) {
        return list.contains(number);
    }

    public void deleteRandom(List<Map.Entry<Integer, Integer>> randomList, int id) {
        Iterator<Map.Entry<Integer, Integer>> iterator = randomList.iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            if (entry.getKey().equals(id)) {
                iterator.remove();
                found = true;
                System.out.println("Элемент с ID " + id + " успешно удален из рандомного списка.");
                break;
            }
        }
        if (!found) {
            System.out.println("Элемент с указанным ID не найден.");
        }
    }
}
