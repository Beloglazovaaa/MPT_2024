package task_13;

import java.util.*;

public class Listik {
    public List<Map.Entry<Integer, Integer>> random() {
        List<Map.Entry<Integer, Integer>> randomList = new ArrayList<>();
        Random rand = new Random();
        for (int i = 1; i <= 1000; i++) {
            randomList.add(new AbstractMap.SimpleEntry<>(i, rand.nextInt()));
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

    public void deleteRandom(List<Map.Entry<Integer, Integer>> randomList, int id) {
        Iterator<Map.Entry<Integer, Integer>> iterator = randomList.iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            if (entry.getKey() == id) {
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