package ra.edu.validate;

import ra.edu.exception.ValidationException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Validator {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    static {
        dateFormat.setLenient(false); // Không cho phép ngày không hợp lệ (ví dụ: 31/04)
    }

    // Validate chuỗi không rỗng
    public static void validateNotEmpty(String value, String fieldName) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " không được để trống!");
        }
    }

    // Validate số dương
    public static void validatePositiveNumber(double value, String fieldName) throws ValidationException {
        if (value <= 0) {
            throw new ValidationException(fieldName + " phải là số dương!");
        }
    }

    // Validate email
    public static void validateEmail(String email) throws ValidationException {
        validateNotEmpty(email, "Email");
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new ValidationException("Email không đúng định dạng (ví dụ: user@example.com)!");
        }
    }

    // Validate ngày tháng
    public static void validateDate(String date) throws ValidationException {
        validateNotEmpty(date, "Ngày sinh");
        try {
            dateFormat.parse(date);
        } catch (Exception e) {
            throw new ValidationException("Ngày không hợp lệ: " + e.getMessage());
        }
    }

    // Validate số điện thoại cơ bản (10-11 chữ số)
    public static void validatePhoneBasic(String phone) throws ValidationException {
        validateNotEmpty(phone, "Số điện thoại");
        if (!phone.matches("\\d{10,11}")) {
            throw new ValidationException("Số điện thoại phải có 10-11 chữ số!");
        }
    }

    // Phương thức prompt cho chuỗi không rỗng
    public static String promptForNotEmpty(String prompt, String fieldName) {
        while (true) {
            try {
                System.out.print(prompt);
                String value = scanner.nextLine();
                validateNotEmpty(value, fieldName);
                return value;
            } catch (ValidationException e) {
                System.out.println("\u001B[31m" + "Lỗi: " + e.getMessage() + "\u001B[0m");
            }
        }
    }

    // Phương thức prompt cho số dương
    public static double promptForPositiveNumber(String prompt, String fieldName) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                double value = Double.parseDouble(input);
                validatePositiveNumber(value, fieldName);
                return value;
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31m" + "Lỗi: " + fieldName + " phải là số hợp lệ!" + "\u001B[0m");
            } catch (ValidationException e) {
                System.out.println("\u001B[31m" + "Lỗi: " + e.getMessage() + "\u001B[0m");
            }
        }
    }

    // Phương thức prompt cho email
    public static String promptForEmail(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String email = scanner.nextLine();
                validateEmail(email);
                return email;
            } catch (ValidationException e) {
                System.out.println("\u001B[31m" + "Lỗi: " + e.getMessage() + "\u001B[0m");
            }
        }
    }

    // Phương thức prompt cho ngày tháng
    public static String promptForDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String date = scanner.nextLine();
                validateDate(date);
                return date;
            } catch (ValidationException e) {
                System.out.println("\u001B[31m" + "Lỗi: " + e.getMessage() + "\u001B[0m");
            }
        }
    }

    // Phương thức prompt cho số điện thoại cơ bản
    public static String promptForPhoneBasic(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String phone = scanner.nextLine();
                validatePhoneBasic(phone);
                return phone;
            } catch (ValidationException e) {
                System.out.println("\u001B[31m" + "Lỗi: " + e.getMessage() + "\u001B[0m");
            }
        }
    }
}