package models;

public class Person {
    private String name;
    private double height;
    private double weight;
    private int age;
    private String gender;
    private int activityIndex;
    private double consumedCalories;

    private static final double[] ACTIVITY_COEFFICIENTS = {1.2, 1.38, 1.46, 1.55, 1.64, 1.73, 1.9};

    public Person(String name, double height, double weight, int age, String gender, int activityIndex) {
        setName(name);
        setHeight(height);
        setWeight(weight);
        setAge(age);
        setGender(gender);
        setActivityIndex(activityIndex);
        this.consumedCalories = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым.");
        }
        this.name = name.trim();
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        if (height <= 0) {
            throw new IllegalArgumentException("Рост должен быть строго больше 0.");
        }
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Вес должен быть строго больше 0.");
        }
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age <= 0) {
            throw new IllegalArgumentException("Возраст должен быть строго больше 0.");
        }
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        if (gender == null) {
            throw new IllegalArgumentException("Пол не может быть null.");
        }
        String upperGender = gender.trim().toUpperCase();
        if (!upperGender.equals("М") && !upperGender.equals("Ж")) {
            throw new IllegalArgumentException("Пол должен быть только 'М' или 'Ж'.");
        }
        this.gender = upperGender;
    }

    public int getActivityIndex() {
        return activityIndex;
    }

    public double getActivityCoefficient() {
        return ACTIVITY_COEFFICIENTS[activityIndex];
    }

    public void setActivityIndex(int activityIndex) {
        if (activityIndex < 0 || activityIndex > 6) {
            throw new IllegalArgumentException("Индекс активности должен быть в диапазоне от 0 до 6!");
        }
        this.activityIndex = activityIndex;
    }

    public double getConsumedCalories() {
        return consumedCalories;
    }

    public void setConsumedCalories(double consumedCalories) {
        if (consumedCalories < 0) {
            throw new IllegalArgumentException("Количество калорий не может быть отрицательным.");
        }
        this.consumedCalories = consumedCalories;
    }

    public void addConsumedCalories(double calories) {
        if (calories < 0) {
            throw new IllegalArgumentException("Добавляемое количество калорий не может быть отрицательным.");
        }
        this.consumedCalories += calories;
    }

    public double calculateDCI() {
        double baseFormula = (10 * weight) + (6.25 * height) - (5 * age);
        if (gender.equals("М")) {
            baseFormula += 5;
        } else {
            baseFormula -= 161;
        }
        return baseFormula * ACTIVITY_COEFFICIENTS[activityIndex];
    }

    public boolean isLosingWeight() {
        return consumedCalories < calculateDCI();
    }

    public void newDay() {
        this.consumedCalories = 0;
    }

    @Override
    public String toString() {
        return String.format("%s (Пол: %s, Возраст: %d, Рост: %.1f см, Вес: %.1f кг, Коэфф: %.2f, DCI: %.1f ккал, Съедено: %.1f ккал)",
                name, gender, age, height, weight, getActivityCoefficient(), calculateDCI(), consumedCalories);
    }
}