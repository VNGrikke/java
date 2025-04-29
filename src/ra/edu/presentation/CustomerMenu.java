package ra.edu.presentation;

import ra.edu.business.model.Customer;
import ra.edu.business.service.Customer.CustomerService;
import ra.edu.util.InputUtil;
import ra.edu.validate.Validator;

import java.util.List;

public class CustomerMenu {
    private final CustomerService customerService;
    private final DisplayMenu displayMenu;

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

            int choice = InputUtil.getChoice(0, 4);
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
            }
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

            String format = "| %-" + idWidth + "s | %-" + nameWidth + "s | %-" + phoneWidth + "s | %-" + emailWidth + "s | %-" + addressWidth + "s |\n";
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

        String name = Validator.promptForNotEmpty("Nhập tên khách hàng: ", "Tên khách hàng");
        String email = Validator.promptForEmail("Nhập email: ");
        if (customers.stream().anyMatch(c -> c.getCustomerEmail().equalsIgnoreCase(email))) {
            System.out.println("\u001B[31mEmail đã được sử dụng, vui lòng nhập lại!\u001B[0m");
            return;
        }
        String phone = Validator.promptForPhoneBasic("Nhập số điện thoại: ");
        if (customers.stream().anyMatch(c -> c.getCustomerPhone().equals(phone))) {
            System.out.println("\u001B[31mSố điện thoại đã được sử dụng, vui lòng nhập lại!\u001B[0m");
            return;
        }
        String address = Validator.promptForNotEmpty("Nhập địa chỉ: ", "Địa chỉ");

        customerService.addCustomer(name, email, phone, address);
    }

    private void updateCustomer() {
        System.out.println("Cập nhật khách hàng:");
        List<Customer> customers = customerService.getCustomerList();
        if (customers.isEmpty()) {
            System.out.println("Danh sách khách hàng trống! Không thể cập nhật.");
            return;
        }

        int id = (int) InputUtil.promptForPositiveNumber("Nhập ID khách hàng: ");
        boolean customerExists = customers.stream().anyMatch(c -> c.getCustomerId() == id);
        if (!customerExists) {
            System.out.println("Không tìm thấy khách hàng với ID: " + id);
            return;
        }

        String name = Validator.promptForNotEmpty("Nhập tên mới: ", "Tên khách hàng");
        String email = Validator.promptForEmail("Nhập email mới: ");
        if (customers.stream().anyMatch(c -> c.getCustomerEmail().equalsIgnoreCase(email) && c.getCustomerId() != id)) {
            System.out.println("\u001B[31mEmail đã được sử dụng, vui lòng nhập lại!\u001B[0m");
            return;
        }
        String phone = Validator.promptForPhoneBasic("Nhập số điện thoại mới: ");
        if (customers.stream().anyMatch(c -> c.getCustomerPhone().equals(phone) && c.getCustomerId() != id)) {
            System.out.println("\u001B[31mSố điện thoại đã được sử dụng, vui lòng nhập lại!\u001B[0m");
            return;
        }
        String address = Validator.promptForNotEmpty("Nhập địa chỉ mới: ", "Địa chỉ");

        customerService.updateCustomer(id, name, email, phone, address);
    }

    private void deleteCustomer() {
        System.out.println("Xóa khách hàng:");
        int id = (int) InputUtil.promptForPositiveNumber("Nhập ID khách hàng: ");
        customerService.deleteCustomer(id);
    }
}