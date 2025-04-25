package ra.edu.presentation;


import ra.edu.business.model.Customer;
import ra.edu.business.model.Invoice;
import ra.edu.business.model.InvoiceItem;
import ra.edu.business.service.Customer.CustomerService;
import ra.edu.business.service.Invoice.InvoiceService;

import ra.edu.validate.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceMenu {
    private InvoiceService invoiceService;
    private CustomerService customerService;

    private DisplayMenu displayMenu;

    public InvoiceMenu(InvoiceService invoiceService, DisplayMenu displayMenu) {
        this.invoiceService = invoiceService;
        this.customerService = customerService;
        this.displayMenu = displayMenu;
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ THÔNG TIN MUA BÁN ===");
            System.out.println("1. Danh sách hóa đơn");
            System.out.println("2. Thêm mới hóa đơn");
            System.out.println("3. Tìm kiếm hóa đơn");
            System.out.println("0. Quay lại");

            int choice = getChoice();
            switch (choice) {
                case 1:
                    displayInvoiceList();
                    break;
                case 2:
                    addInvoice();
                    break;
                case 3:
                    searchInvoice();
                    break;

                case 0:
                    displayMenu.menuManager();
                    return;
                default:
                    System.out.println("\u001B[31m" + "Lựa chọn không hợp lệ! Vui lòng nhập lại." + "\u001B[0m");
            }
        }
    }


    private int getChoice() {
        while (true) {
            try {
                String input = Validator.promptForNotEmpty("Nhập lựa chọn: ", "Lựa chọn");
                int choice = Integer.parseInt(input);
                if (choice >= 0 && choice <= 3) {
                    return choice;
                } else {
                    System.out.println("\u001B[31m" + "Lựa chọn không hợp lệ! Vui lòng nhập lại." + "\u001B[0m");
                }
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31m" + "Lựa chọn phải là số! Vui lòng nhập lại." + "\u001B[0m");
            }
        }
    }

    private void displayInvoiceList() {
        List<Invoice> invoices = invoiceService.getInvoices();
        if (invoices.isEmpty()) {
            System.out.println("Danh sách hóa đơn trống!");
        } else {
            int idWidth = "ID đơn hàng".length();
            int idCusWidth = "Tên khách hàng".length();
            int totalWidth = "Số tiền".length();
            int createWith = "Ngày tạo".length();

            for (Invoice i : invoices) {

                idWidth = Math.max(idWidth, String.valueOf(i.getInvoiceId()).length());
                idCusWidth = Math.max(idCusWidth, String.valueOf(i.getCustomerId()).length() );
                totalWidth = Math.max(totalWidth, String.valueOf(i.getTotal_amount()).length());
                createWith = Math.max(createWith, String.valueOf(i.getCreated_at()).length());
            }

            String format = "| %-" + idWidth + "s | %-" + idCusWidth + "s | %-" + totalWidth + "s | %" + createWith + "s |\n";
            String line = "+" + "-".repeat(idWidth + 2) + "+" + "-".repeat(idCusWidth + 2) + "+" + "-".repeat(totalWidth + 2) + "+" + "-".repeat(createWith + 2);

            System.out.println(line);
            System.out.printf(format, "ID đơn hàng", "ID khách hàng", "Số tiền", "Ngày tạo");
            System.out.println(line);

            for (Invoice i : invoices) {
                System.out.printf(format,
                        i.getInvoiceId(),
                        i.getCustomerId(),
                        i.getTotal_amount(),
                        i.getCreated_at());
            }
            System.out.println(line);

        }
    }

    private void addInvoice() {
        System.out.println("Thêm mới hóa đơn:");

        // Nhập và validate customerId
        int customerId;
        while (true) {
            customerId = (int) Validator.promptForPositiveNumber("Nhập ID khách hàng: ", "ID khách hàng");
            if (invoiceService.customerExists(customerId)) {
                break;
            } else {
                System.out.println("\u001B[31m" + "ID khách hàng không tồn tại! Vui lòng nhập lại." + "\u001B[0m");
            }
        }

        // Lấy ngày hiện tại
        LocalDate createdAt = LocalDate.now();

        // Tạo danh sách tạm để lưu chi tiết hóa đơn
        List<InvoiceItem> items = new ArrayList<>();
        double totalAmount = 0.0;

        // Nhập chi tiết hóa đơn
        while (true) {
            String addMore = Validator.promptForNotEmpty("Bạn có muốn thêm chi tiết hóa đơn không? (y/n): ", "Lựa chọn");
            if (addMore.equalsIgnoreCase("n")) {
                if (items.isEmpty()) {
                    System.out.println("\u001B[31m" + "Hóa đơn phải có ít nhất một chi tiết! Vui lòng thêm chi tiết." + "\u001B[0m");
                    continue;
                }
                break;
            }

            // Nhập và validate productId
            int productId;
            double unitPrice;
            while (true) {
                productId = (int) Validator.promptForPositiveNumber("Nhập ID sản phẩm: ", "ID sản phẩm");
                if (invoiceService.productExists(productId)) {
                    unitPrice = invoiceService.getProductPriceById(productId);
                    if (unitPrice > 0) {
                        System.out.println("Giá sản phẩm: " + unitPrice);
                        break;
                    } else {
                        System.out.println("\u001B[31m" + "Không thể lấy giá sản phẩm! Vui lòng thử lại." + "\u001B[0m");
                    }
                } else {
                    System.out.println("\u001B[31m" + "ID sản phẩm không tồn tại! Vui lòng nhập lại." + "\u001B[0m");
                }
            }

            // Kiểm tra số lượng tồn kho
            int quantity;
            while (true) {
                quantity = (int) Validator.promptForPositiveNumber("Nhập số lượng: ", "Số lượng");
                int productStock = invoiceService.getProductStockById(productId);
                if (productStock < quantity) {
                    System.out.println("\u001B[31m" + "Số lượng không đủ! Còn lại: " + productStock + "\u001B[0m");
                } else {
                    InvoiceItem item = new InvoiceItem();
                    item.setProduct_id(productId);
                    item.setQuantity(quantity);
                    item.setUnit_price(unitPrice);
                    items.add(item);

                    // Tính tổng tiền tạm thời
                    totalAmount += quantity * unitPrice;
                    break;
                }
            }
        }

        // Lưu hóa đơn với tổng tiền đã tính
        invoiceService.addInvoice(customerId, totalAmount);

        // Lấy invoiceId mới nhất để thêm chi tiết
        List<Invoice> invoices = invoiceService.getInvoices();
        int latestInvoiceId = invoices.get(invoices.size() - 1).getInvoiceId();

        // Lưu các chi tiết hóa đơn
        for (InvoiceItem item : items) {
            invoiceService.addInvoiceItem(latestInvoiceId, item.getProduct_id(), item.getQuantity(), item.getUnit_price());
        }
    }

    private void searchInvoice() {
        while (true) {
            System.out.println("\n=== TÌM KIẾM HÓA ĐƠN ===");
            System.out.println("1. Tìm kiếm theo tên khách hàng");
            System.out.println("2. Tìm kiếm theo ngày tháng năm");
            System.out.println("0. Quay lại");

            int choice = getChoice();
            if (choice == 0) return;

            switch (choice) {
                case 1:
                    searchByCustomerName();
                    break;
                case 2:
                    searchByDate();
                    break;
                default:
                    System.out.println("\u001B[31m" + "Lựa chọn không hợp lệ! Vui lòng nhập lại." + "\u001B[0m");
            }
        }
    }

    private void searchByCustomerName() {
        String customerName = Validator.promptForNotEmpty("Nhập tên khách hàng: ", "Tên khách hàng");
        List<Invoice> invoices = invoiceService.searchInvoicesByCustomerName(customerName);
        displaySearchResults(invoices);
    }

    private void searchByDate() {
        LocalDate date = null;
        while (date == null) {
            try {
                String dateInput = Validator.promptForNotEmpty("Nhập ngày (dd/MM/yyyy): ", "Ngày");
                date = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("\u001B[31m" + "Ngày không đúng định dạng (dd/MM/yyyy)! Vui lòng nhập lại." + "\u001B[0m");
            }
        }
        List<Invoice> invoices = invoiceService.searchInvoicesByDate(date);
        displaySearchResults(invoices);
    }

    private void displaySearchResults(List<Invoice> invoices) {
        if (invoices.isEmpty()) {
            System.out.println("Không tìm thấy hóa đơn nào!");
        } else {
            System.out.println("Kết quả tìm kiếm:");
            for (Invoice i : invoices) {
                System.out.println("Hóa đơn ID: " + i.getInvoiceId() + " | Khách hàng ID: " + i.getCustomerId() +
                        " | Ngày: " + i.getCreated_at() + " | Tổng tiền: " + i.getTotal_amount());
                // Hiển thị chi tiết hóa đơn
                List<InvoiceItem> items = invoiceService.getInvoiceItemsByInvoiceId(i.getInvoiceId());
                if (items.isEmpty()) {
                    System.out.println("  (Không có chi tiết hóa đơn)");
                } else {
                    System.out.println("  Chi tiết hóa đơn:");
                    System.out.println("  +----+-------------+----------+-------------+");
                    System.out.println("  | ID | Sản phẩm ID | Số lượng | Đơn giá    |");
                    System.out.println("  +----+-------------+----------+-------------+");
                    for (InvoiceItem item : items) {
                        System.out.printf("  | %-2d | %-11d | %-8d | %-10.2f |\n",
                                item.getInvoice_item_id(),
                                item.getProduct_id(),
                                item.getQuantity(),
                                item.getUnit_price());
                    }
                    System.out.println("  +----+-------------+----------+-------------+");

                }
            }
        }
    }

}