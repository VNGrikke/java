package ra.edu.validate;

import ra.edu.exception.ValidationException;

import java.util.regex.Pattern;

public class CustomerValidator {

    // Kiểm tra tên không được để trống
    public static void validateName(String name) throws ValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Tên khách hàng không được để trống!");
        }
    }

    // Kiểm tra định dạng email
    public static void validateEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email không được để trống!");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(emailRegex, email)) {
            throw new ValidationException("Email không đúng định dạng (user@domain.com)!");
        }
    }

    // Kiểm tra số điện thoại (10 chữ số, bắt đầu bằng 0)
    public static void validatePhone(String phone) throws ValidationException {
        if (phone == null || phone.trim().isEmpty()) {
            throw new ValidationException("Số điện thoại không được để trống!");
        }
        String phoneRegex = "0[0-9]{9}";
        if (!Pattern.matches(phoneRegex, phone)) {
            throw new ValidationException("Số điện thoại phải có 10 chữ số và bắt đầu bằng số 0!");
        }
    }

    // Kiểm tra địa chỉ không được để trống
    public static void validateAddress(String address) throws ValidationException {
        if (address == null || address.trim().isEmpty()) {
            throw new ValidationException("Địa chỉ không được để trống!");
        }
    }

    // Phương thức tổng hợp để kiểm tra toàn bộ thông tin khách hàng
    public static void validateCustomer(String name, String email, String phone, String address) throws ValidationException {
        validateName(name);
        validateEmail(email);
        validatePhone(phone);
        validateAddress(address);
    }
}