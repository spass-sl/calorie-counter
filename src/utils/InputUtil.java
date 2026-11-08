package utils;

import java.util.Scanner;

public class InputUtil {

    public static String readString(Scanner in, String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    public static String readNonEmptyString(Scanner in, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = in.nextLine().trim();
                if (line.isEmpty()) {
                    throw new IllegalArgumentException("Поле не может быть пустым. Введите значение.");
                }
                return line;
            } catch (IllegalArgumentException e) {
                System.out.println("Исключение IllegalArgumentException: " + e.getMessage());
            }
        }
    }

    public static int readInt(Scanner in, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = in.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Исключение InputMismatchException / NumberFormatException: Ошибка типа данных! Введите целое число.");
            }
        }
    }

    /*public static int readPositiveInt(Scanner in, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = in.nextLine().trim();
                int value = Integer.parseInt(line);
                if (value <= 0) {
                    throw new IllegalArgumentException("Значение должно быть строго больше 0.");
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Исключение InputMismatchException / NumberFormatException: Ошибка типа данных! Введите целое число.");
            } catch (IllegalArgumentException e) {
                System.out.println("Исключение IllegalArgumentException: " + e.getMessage());
            }
        }
    }*/

    public static int readIntInRange(Scanner in, String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = in.nextLine().trim();
                int value = Integer.parseInt(line);
                if (value < min || value > max) {
                    throw new IllegalArgumentException(
                            String.format("Индекс/значение должно быть в диапазоне от %d до %d!", min, max)
                    );
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Исключение InputMismatchException / NumberFormatException: Ошибка типа данных! Введите целое число.");
            } catch (IllegalArgumentException e) {
                System.out.println("Исключение IllegalArgumentException: " + e.getMessage());
            }
        }
    }

    public static double readDoubleInRange(Scanner in, String prompt, double min, double max) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = in.nextLine().trim();
                double value = Double.parseDouble(line);
                if (value < min || value > max) {
                    throw new IllegalArgumentException(
                            String.format("Индекс/значение должно быть в диапазоне от %f до %f!", min, max)
                    );
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Исключение InputMismatchException / NumberFormatException: Ошибка типа данных! Введите целое число.");
            } catch (IllegalArgumentException e) {
                System.out.println("Исключение IllegalArgumentException: " + e.getMessage());
            }
        }
    }

    /*public static double readPositiveDouble(Scanner in, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = in.nextLine().trim().replace(',', '.');
                double value = Double.parseDouble(line);
                if (value <= 0) {
                    throw new IllegalArgumentException("Значение должно быть строго больше 0.");
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Исключение InputMismatchException / NumberFormatException: Ошибка типа данных! Введите число.");
            } catch (IllegalArgumentException e) {
                System.out.println("Исключение IllegalArgumentException: " + e.getMessage());
            }
        }
    }*/
    public static String readGender(Scanner in, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = in.nextLine().trim().toUpperCase();
                if (!line.equals("М") && !line.equals("Ж")) {
                    throw new IllegalArgumentException("Пол должен быть только 'М' или 'Ж'.");
                }
                return line;
            } catch (IllegalArgumentException e) {
                System.out.println("Исключение IllegalArgumentException: " + e.getMessage());
            }
        }
    }
}