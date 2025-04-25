package ra.edu.presentation;

import ra.edu.business.model.Customer;
import ra.edu.business.service.Customer.CustomerService;
import ra.edu.exception.ValidationException;
import ra.edu.validate.CustomerValidator;
import ra.edu.validate.Validator;

import java.util.List;

public class CustomerMenu {
    private CustomerService customerService;
    private DisplayMenu displayMenu;

    public CustomerMenu(CustomerService customerService, DisplayMenu displayMenu) {
        this.customerService = customerService;
        this.displayMenu = displayMenu;
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ KHÁCH HÀNG ===");
            System.out.println("1. Danh sách khách hàng");
            System.out.println("2. Thêm mới khách hàng");
            System.out.println("3. Cập nhật khách hàng");
            System.out.println("4. Xóa khách hàng");
            System.out.println("0. Quay lại");

            int choice = getChoice();
            switch (choice) {
                case 1:
                    displayCustomerList();
                    break;
                case 2:
                    addCustomer();
                    break;
                case 3:
                    updateCustomer();
                    break;
                case 4:
                    deleteCustomer();
                    break;
                case 0:
                    displayMenu.menuManager();
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private int getChoice() {
        try {
            return Integer.parseInt(Validator.promptForNotEmpty("Nhập lựa chọn: ", "Lựa chọn"));
        } catch (NumberFormatException e) {
            return -1;
        } catch (Exception e) {
            return -1;
        }
    }

    private void displayCustomerList() {
        List<Customer> customers = customerService.getCustomerList();
        if (customers.isEmpty()) {
            System.out.println("Danh sách khách hàng trống!");
        } else {
            int idWidth = "ID".length();
            int nameWidth = "Tên khách hàng".length();
            int phoneWidth = "Số điện thoại".length();
            int emailWidth = "Email".length();
            int addressWidth = "Địa chỉ".length();

            for (Customer c : customers) {
                idWidth = Math.max(idWidth, String.valueOf(c.getCustomerId()).length());
                nameWidth = Math.max(nameWidth, c.getCustomerName() != null ? c.getCustomerName().length() : 0);
                phoneWidth = Math.max(phoneWidth, c.getCustomerPhone() != null ? c.getCustomerPhone().length() : 0);
                emailWidth = Math.max(emailWidth, c.getCustomerEmail() != null ? c.getCustomerEmail().length() : 0);
                addressWidth = Math.max(addressWidth, c.getCustomerAddress() != null ? c.getCustomerAddress().length() : 0);
            }

            String format = "| %-" + idWidth + "s | %-" + nameWidth + "s | %-" + phoneWidth + "s | %" + emailWidth + "s | %" + addressWidth + "s |\n";
            String line = "+" + "-".repeat(idWidth + 2) + "+" + "-".repeat(nameWidth + 2) + "+" + "-".repeat(phoneWidth + 2) + "+" + "-".repeat(emailWidth + 2) + "+" + "-".repeat(addressWidth + 2) + "+";

            System.out.println(line);
            System.out.printf(format, "ID", "Tên khách hàng", "Số điện thoại", "Email", "Địa chỉ");
            System.out.println(line);

            for (Customer c : customers) {
                System.out.printf(format,
                        c.getCustomerId(),
                        c.getCustomerName() != null ? c.getCustomerName() : "(Trống)",
                        c.getCustomerPhone() != null ? c.getCustomerPhone() : "(Trống)",
                        c.getCustomerEmail() != null ? c.getCustomerEmail() : "(Trống)",
                        c.getCustomerAddress() != null ? c.getCustomerAddress() : "(Trống)");
            }
            System.out.println(line);
        }
    }

    private void addCustomer() {
        System.out.println("Thêm mới khách hàng:");
        List<Customer> customers = customerService.getCustomerList();

        // Nhập và validate tên
        String name = promptAndValidate("Nhập tên khách hàng: ", "Tên khách hàng", CustomerValidator::validateName);

        // Nhập và validate email, kiểm tra trùng lặp
        String email = promptAndValidateUnique("Nhập email: ", "Email", CustomerValidator::validateEmail, customers,
                (c, input) -> c.getCustomerEmail().equals(input), -1);

        // Nhập và validate số điện thoại, kiểm tra trùng lặp
        String phone = promptAndValidateUnique("Nhập số điện thoại: ", "Số điện thoại", CustomerValidator::validatePhone, customers,
                (c, input) -> c.getCustomerPhone().equals(input), -1);

        // Nhập và validate địa chỉ
        String address = promptAndValidate("Nhập địa chỉ: ", "Địa chỉ", CustomerValidator::validateAddress);

        // Sau khi tất cả thông tin hợp lệ, gọi service để thêm khách hàng
        customerService.addCustomer(name, email, phone, address);
    }

    private void updateCustomer() {
        System.out.println("Cập nhật khách hàng:");
        List<Customer> customers = customerService.getCustomerList();
        if (customers.isEmpty()) {
            System.out.println("Danh sách khách hàng trống! Không thể cập nhật.");
            return;
        }

        // Nhập và kiểm tra ID hợp lệ
        int id = (int) Validator.promptForPositiveNumber("Nhập ID khách hàng: ", "ID khách hàng");
        boolean customerExists = customers.stream().anyMatch(c -> c.getCustomerId() == id);
        if (!customerExists) {
            System.out.println("Không tìm thấy khách hàng với ID: " + id);
            return;
        }

        // Nhập và validate tên
        String name = promptAndValidate("Nhập tên mới: ", "Tên khách hàng", CustomerValidator::validateName);

        // Nhập và validate email, kiểm tra trùng lặp (trừ chính khách hàng đang cập nhật)
        String email = promptAndValidateUnique("Nhập email mới: ", "Email", CustomerValidator::validateEmail, customers,
                (c, input) -> c.getCustomerEmail().equals(input), id);

        // Nhập và validate số điện thoại, kiểm tra trùng lặp (trừ chính khách hàng đang cập nhật)
        String phone = promptAndValidateUnique("Nhập số điện thoại mới: ", "Số điện thoại", CustomerValidator::validatePhone, customers,
                (c, input) -> c.getCustomerPhone().equals(input), id);

        // Nhập và validate địa chỉ
        String address = promptAndValidate("Nhập địa chỉ mới: ", "Địa chỉ", CustomerValidator::validateAddress);

        // Sau khi tất cả thông tin hợp lệ, gọi service để cập nhật khách hàng
        customerService.updateCustomer(id, name, email, phone, address);
    }

    private void deleteCustomer() {
        System.out.println("Xóa khách hàng:");
        int id = (int) Validator.promptForPositiveNumber("Nhập ID khách hàng: ", "ID khách hàng");
        customerService.deleteCustomer(id);
    }

    // Phương thức hỗ trợ để nhập và validate từng trường
    private String promptAndValidate(String prompt, String fieldName, ValidatorFunction validator) {
        while (true) {
            try {
                String input = Validator.promptForNotEmpty(prompt, fieldName);
                validator.validate(input);
                return input;
            } catch (ValidationException e) {
                System.out.println("\u001B[31m" + "Lỗi: " + e.getMessage() + " Vui lòng nhập lại!" + "\u001B[0m");
            }
        }
    }

    // Phương thức hỗ trợ để nhập và validate, đồng thời kiểm tra trùng lặp
    private String promptAndValidateUnique(String prompt, String fieldName, ValidatorFunction validator,
                                           List<Customer> customers, UniqueChecker checker, int excludeId) {
        while (true) {
            String input = promptAndValidate(prompt, fieldName, validator);
            boolean isDuplicate = customers.stream()
                    .anyMatch(c -> checker.check(c, input) && c.getCustomerId() != excludeId);
            if (isDuplicate) {
                System.out.println("\u001B[31m" + "Lỗi: " + fieldName + " đã được sử dụng! Vui lòng nhập lại." + "\u001B[0m");
            } else {
                return input;
            }
        }
    }

    // Interface chức năng để truyền các phương thức validate
    @FunctionalInterface
    private interface ValidatorFunction {
        void validate(String input) throws ValidationException;
    }

    // Interface chức năng để kiểm tra trùng lặp
    @FunctionalInterface
    private interface UniqueChecker {
        boolean check(Customer customer, String input);
    }
}