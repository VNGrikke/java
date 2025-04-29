package ra.edu.util;

import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    public static int getChoice(int min, int max) {
        while (true) {
            try {
                System.out.print("Nhập lựa chọn: ");
                String input = scanner.nextLine();
                int choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.println("\u001B[31mLựa chọn không hợp lệ! Vui lòng nhập số từ " + min + " đến " + max + ".\u001B[0m");
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31mLựa chọn phải là số! Vui lòng nhập lại.\u001B[0m");
            }
        }
    }

    public static String promptForNotEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input != null && !input.trim().isEmpty()) {
                return input;
            }
            System.out.println("\u001B[31mLỗi: Giá trị không được để trống! Vui lòng nhập lại.\u001B[0m");
        }
    }

    public static double promptForPositiveNumber(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                double value = Double.parseDouble(input);
                if (value > 0) {
                    return value;
                }
                System.out.println("\u001B[31mLỗi: Giá trị phải là số dương! Vui lòng nhập lại.\u001B[0m");
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31mLỗi: Giá trị phải là số hợp lệ! Vui lòng nhập lại.\u001B[0m");
            }
        }
    }
}