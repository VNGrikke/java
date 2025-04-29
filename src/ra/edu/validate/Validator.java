package ra.edu.validate;

import ra.edu.exception.ValidationException;
import ra.edu.util.InputUtil;

import java.text.SimpleDateFormat;

public class Validator {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    static {
        dateFormat.setLenient(false);
    }

    public static void validateNotEmpty(String value, String fieldName) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " không được để trống!");
        }
    }

    public static void validatePositiveNumber(double value, String fieldName) throws ValidationException {
        if (value <= 0) {
            throw new ValidationException(fieldName + " phải là số dương!");
        }
    }

    public static void validateEmail(String email) throws ValidationException {
        validateNotEmpty(email, "Email");
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new ValidationException("Email không đúng định dạng (ví dụ: user@example.com)!");
        }
    }

    public static void validateDate(String date) throws ValidationException {
        validateNotEmpty(date, "Ngày sinh");
        try {
            dateFormat.parse(date);
        } catch (Exception e) {
            throw new ValidationException("Ngày không hợp lệ: " + e.getMessage());
        }
    }

    public static void validatePhoneBasic(String phone) throws ValidationException {
        validateNotEmpty(phone, "Số điện thoại");
        if (!phone.matches("\\d{10,11}")) {
            throw new ValidationException("Số điện thoại phải có 10-11 chữ số!");
        }
    }

    public static String promptForNotEmpty(String prompt, String fieldName) {
        String value = InputUtil.promptForNotEmpty(prompt);
        try {
            validateNotEmpty(value, fieldName);
            return value;
        } catch (ValidationException e) {
            System.out.println("\u001B[31mLỗi: " + e.getMessage() + "\u001B[0m");
            return promptForNotEmpty(prompt, fieldName);
        }
    }

    public static double promptForPositiveNumber(String prompt, String fieldName) {
        double value = InputUtil.promptForPositiveNumber(prompt);
        try {
            validatePositiveNumber(value, fieldName);
            return value;
        } catch (ValidationException e) {
            System.out.println("\u001B[31mLỗi: " + e.getMessage() + "\u001B[0m");
            return promptForPositiveNumber(prompt, fieldName);
        }
    }

    public static String promptForEmail(String prompt) {
        String email = InputUtil.promptForNotEmpty(prompt);
        try {
            validateEmail(email);
            return email;
        } catch (ValidationException e) {
            System.out.println("\u001B[31mLỗi: " + e.getMessage() + "\u001B[0m");
            return promptForEmail(prompt);
        }
    }

    public static String promptForDate(String prompt) {
        String date = InputUtil.promptForNotEmpty(prompt);
        try {
            validateDate(date);
            return date;
        } catch (ValidationException e) {
            System.out.println("\u001B[31mLỗi: " + e.getMessage() + "\u001B[0m");
            return promptForDate(prompt);
        }
    }

    public static String promptForPhoneBasic(String prompt) {
        String phone = InputUtil.promptForNotEmpty(prompt);
        try {
            validatePhoneBasic(phone);
            return phone;
        } catch (ValidationException e) {
            System.out.println("\u001B[31mLỗi: " + e.getMessage() + "\u001B[0m");
            return promptForPhoneBasic(prompt);
        }
    }
}