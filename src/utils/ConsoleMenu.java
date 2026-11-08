package utils;

import models.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private List<Person> people;
    private List<Product> products;

    public ConsoleMenu() {
        this.people = new ArrayList<>();
        this.products = new ArrayList<>();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        loadProducts();
        initializeDefaultPeople();

        boolean running = true;
        while (running) {
            System.out.println("\nГЛАВНОЕ МЕНЮ");
            System.out.println("a. Работа с людьми (просмотр, добавление, статус похудения)");
            System.out.println("b. Работа с рационом (очистка дня, добавление съеденных калорий)");
            System.out.println("c. Выход");

            String choice = InputUtil.readString(scanner, "Выберите пункт меню (a/b/c): ").toLowerCase();

            try {
                switch (choice) {
                    case "a":
                        peopleMenu(scanner);
                        break;
                    case "b":
                        dietMenu(scanner);
                        break;
                    case "c":
                        System.out.println("Выход из программы...");
                        running = false;
                        break;
                    default:
                        throw new IllegalArgumentException("Неверный пункт меню. Введите 'a', 'b' или 'c'.");
                }
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private void peopleMenu(Scanner scanner) {
        System.out.println("\nМеню людей");
        System.out.println("1. Показать список людей и их статус");
        System.out.println("2. Добавить нового человека");
        int choice = InputUtil.readInt(scanner, "Ваш выбор: ");

        if (choice == 1) {
            if (people.isEmpty()) {
                System.out.println("Список людей пуст.");
                return;
            }
            for (int i = 0; i < people.size(); i++) {
                Person p = people.get(i);
                String status = p.isLosingWeight() ? "ХУДЕЕТ" : "НЕ ХУДЕЕТ";
                System.out.println("[" + i + "] " + p.toString() + " -> Статус: " + status);
            }
        } else if (choice == 2) {
            String name = InputUtil.readNonEmptyString(scanner, "Имя: ");
            double height = InputUtil.readDoubleInRange(scanner, "Рост (см): ", 120, 250);
            double weight = InputUtil.readDoubleInRange(scanner, "Вес (кг): ",35, 300);
            int age = InputUtil.readIntInRange(scanner, "Возраст: ",1, 120);
            String gender = InputUtil.readGender(scanner, "Пол (М/Ж): ");

            System.out.println("Коэффициенты активности:");
            System.out.println("0 - Отсутствует или минимальная\n1 - Средняя 3 раза в неделю\n2 - Средняя 5 раз в неделю\n3 - Интенсивная 5 раз в неделю\n4 - Тренировки каждый день\n5 - Интенсивно каждый день\n6 - Ежедневная физ. работа");
            int activity = InputUtil.readIntInRange(scanner, "Индекс активности (0-6): ", 0, 6);

            Person newPerson = new Person(name, height, weight, age, gender, activity);
            people.add(newPerson);
            System.out.println("Человек успешно добавлен!");
        } else {
            throw new IllegalArgumentException("Выбран несуществующий пункт подменю: " + choice);
        }
    }

    private void dietMenu(Scanner scanner) {
        if (people.isEmpty()) {
            System.out.println("Сначала добавьте людей в меню 'a'.");
            return;
        }

        System.out.println("\nВыберите человека");
        for (int i = 0; i < people.size(); i++) {
            System.out.println("[" + i + "] " + people.get(i).getName());
        }

        int pIndex = InputUtil.readInt(scanner, "Индекс человека: ");
        if (pIndex < 0 || pIndex >= people.size()) {
            throw new IndexOutOfBoundsException("Человека с индексом " + pIndex + " не существует.");
        }

        Person person = people.get(pIndex);

        System.out.println("1. Добавить съеденный продукт");
        System.out.println("2. Начать новый день (сбросить калории)");
        int choice = InputUtil.readInt(scanner, "Ваш выбор: ");

        if (choice == 1) {
            if (products.isEmpty()) {
                System.out.println("Список продуктов пуст.");
                return;
            }

            System.out.println("\nСписок продуктов");
            for (int i = 0; i < products.size(); i++) {
                System.out.println("[" + i + "] " + products.get(i).toString());
            }
            int prodIndex = InputUtil.readInt(scanner, "Индекс продукта: ");

            if (prodIndex < 0 || prodIndex >= products.size()) {
                throw new IndexOutOfBoundsException("Продукта с индексом " + prodIndex + " не существует.");
            }

            double grams = InputUtil.readDoubleInRange(scanner, "Сколько грамм съедено: ", 1, 3000);
            if (grams <= 0) {
                throw new IllegalArgumentException("Количество грамм должно быть строго больше 0.");
            }

            Product selectedProduct = products.get(prodIndex);
            double caloriesEaten = selectedProduct.calculateCalories(grams);
            person.addConsumedCalories(caloriesEaten);

            System.out.printf("Добавлено %.1f ккал. Всего за день: %.1f ккал.\n", caloriesEaten, person.getConsumedCalories());

        } else if (choice == 2) {
            person.newDay();
            System.out.println("Калории сброшены на 0. Начат новый день для " + person.getName());
        } else {
            throw new IllegalArgumentException("Выбран несуществующий пункт подменю: " + choice);
        }
    }


    private void loadProducts() {
        File file = new File("food.csv");


        boolean loaded = tryReadFile(file);

        if (!loaded) {
            if (!file.exists()) {
                System.out.println("Исключение FileNotFoundException: Файл food.csv не найден! Программа будет работать с 5 базовыми продуктами.");
            } else {
                System.out.println("Файл food.csv пуст или поврежден. Загружаются базовые продукты.");
            }
            loadFailProducts();
        } else {
            System.out.println("Продукты успешно загружены из файла food.csv (всего: " + products.size() + ").");
        }
    }

    private boolean tryReadFile(File file) {
        if (!file.exists()) {
            return false;
        }

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, "cp1251");
             BufferedReader reader = new BufferedReader(isr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";");

                if (parts.length >= 4) {
                    try {
                        String name = parts[0].trim();
                        double proteins = Double.parseDouble(parts[1].trim());
                        double fats = Double.parseDouble(parts[2].trim());
                        double carbs = Double.parseDouble(parts[3].trim());

                        products.add(new Product(name, proteins, fats, carbs));
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }

            return !products.isEmpty();

        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private void loadFailProducts() {
        products.clear();
        products.add(new Product("Курица вареная", 25.2, 7.4, 0.0));
        products.add(new Product("Гречневая каша", 3.2, 0.8, 17.1));
        products.add(new Product("Салат Оливье", 5.4, 16.7, 7.0));
        products.add(new Product("Треска отварная", 17.8, 0.7, 0.0));
        products.add(new Product("Борщ с мясом", 4.4, 3.6, 5.5));
    }

    private void initializeDefaultPeople() {
        Person p1 = new Person("Иван", 180, 85, 30, "М", 2);
        Person p2 = new Person("Анна", 165, 60, 25, "Ж", 1);
        Person p3 = new Person("Сергей", 175, 95, 45, "М", 0);
        Person p4 = new Person("Мария", 170, 55, 20, "Ж", 4);

        Product dish1 = products.get(0);
        Product dish2 = products.get(1);
        Product dish3 = products.get(2);

        p1.addConsumedCalories(dish1.calculateCalories(400) + dish2.calculateCalories(300));
        p2.addConsumedCalories(dish1.calculateCalories(200) + dish3.calculateCalories(150));
        p3.addConsumedCalories(dish3.calculateCalories(500) + dish1.calculateCalories(350));
        p4.addConsumedCalories(dish2.calculateCalories(250) + dish1.calculateCalories(200));

        people.add(p1);
        people.add(p2);
        people.add(p3);
        people.add(p4);
    }
}