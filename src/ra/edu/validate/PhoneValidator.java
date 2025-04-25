package ra.edu.validate;

import ra.edu.exception.ValidationException;

public class PhoneValidator {
    public static void validatePhone(String phone) throws ValidationException {
        try {
            Validator.validatePhoneBasic(phone);
            if (!phone.matches("^0\\d{9,10}$")) {
                throw new ValidationException("Số điện thoại phải bắt đầu bằng 0 và có 10-11 chữ số!");
            }
            String validPrefixes = "^(03|07|08|09)\\d{8}$";
            if (!phone.matches(validPrefixes)) {
                throw new ValidationException("Số điện thoại phải có đầu số hợp lệ (03x, 07x, 08x, 09x)!");
            }
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidationException("Lỗi khi kiểm tra số điện thoại: " + e.getMessage(), e);
        }
    }

    public static String promptForPhone(String prompt) {
        while (true) {
            try {
                String phone = Validator.promptForPhoneBasic(prompt);
                validatePhone(phone);
                return phone;
            } catch (ValidationException e) {
                System.out.println("\u001B[31m" + "Lỗi: " + e.getMessage() + "\u001B[0m");
            }
        }
    }
}