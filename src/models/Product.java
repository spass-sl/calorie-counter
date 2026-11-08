package models;

public class Product {
    private String name;
    private double proteins;
    private double fats;
    private double carbs;

    public Product(String name, double proteins, double fats, double carbs) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название продукта не может быть пустым.");
        }
        if (proteins < 0 || fats < 0 || carbs < 0) {
            throw new IllegalArgumentException("БЖУ не могут иметь отрицательные значения.");
        }
        this.name = name.trim();
        this.proteins = proteins;
        this.fats = fats;
        this.carbs = carbs;
    }

    public String getName() {
        return name;
    }

    public double getProteins() {
        return proteins;
    }

    public double getFats() {
        return fats;
    }

    public double getCarbs() {
        return carbs;
    }

    public double calculateCalories(double grams) {
        if (grams < 0) {
            throw new IllegalArgumentException("Вес продукта не может быть отрицательным.");
        }
        double caloriesPer100g = (this.proteins * 4.1) + (this.fats * 9.29) + (this.carbs * 4.1);
        return caloriesPer100g * (grams / 100.0);
    }

    @Override
    public String toString() {
        return String.format("%s (Б: %.1f, Ж: %.1f, У: %.1f)", name, proteins, fats, carbs);
    }
}