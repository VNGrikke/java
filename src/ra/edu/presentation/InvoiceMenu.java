package ra.edu.presentation;

import ra.edu.business.model.Invoice;
import ra.edu.business.model.InvoiceItem;
import ra.edu.business.service.Customer.CustomerService;
import ra.edu.business.service.Invoice.InvoiceService;
import ra.edu.util.InputUtil;
import ra.edu.validate.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceMenu {
    private final InvoiceService invoiceService;
    private final CustomerService customerService;
    private final DisplayMenu displayMenu;

    public InvoiceMenu(InvoiceService invoiceService, CustomerService customerService, DisplayMenu displayMenu) {
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

            int choice = InputUtil.getChoice(0, 3);
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
            }
        }
    }

    private void displayInvoiceList() {
        List<Invoice> invoices = invoiceService.getInvoices();
        if (invoices.isEmpty()) {
            System.out.println("Danh sách hóa đơn trống!");
        } else {
            int idWidth = "ID đơn hàng".length();
            int idCusWidth = "ID khách hàng".length();
            int totalWidth = "Số tiền".length();
            int createWidth = "Ngày tạo".length();

            for (Invoice i : invoices) {
                idWidth = Math.max(idWidth, String.valueOf(i.getInvoiceId()).length());
                idCusWidth = Math.max(idCusWidth, String.valueOf(i.getCustomerId()).length());
                totalWidth = Math.max(totalWidth, String.valueOf(i.getTotal_amount()).length());
                createWidth = Math.max(createWidth, String.valueOf(i.getCreated_at()).length());
            }

            String format = "| %-" + idWidth + "s | %-" + idCusWidth + "s | %-" + totalWidth + "s | %-" + createWidth + "s |\n";
            String line = "+" + "-".repeat(idWidth + 2) + "+" + "-".repeat(idCusWidth + 2) + "+" + "-".repeat(totalWidth + 2) + "+" + "-".repeat(createWidth + 2) + "+";

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

            while (true) {
                int invoiceId = (int) InputUtil.promptForPositiveNumber("Nhập ID hóa đơn để xem chi tiết (0 để quay lại): ");
                if (invoiceId == 0) break;

                Invoice selectedInvoice = invoices.stream()
                        .filter(i -> i.getInvoiceId() == invoiceId)
                        .findFirst()
                        .orElse(null);

                if (selectedInvoice == null) {
                    System.out.println("\u001B[31mKhông tìm thấy hóa đơn với ID: " + invoiceId + "\u001B[0m");
                    continue;
                }

                displayInvoiceDetails(selectedInvoice);
            }
        }
    }

    private void displayInvoiceDetails(Invoice invoice) {
        System.out.println("\n=== CHI TIẾT HÓA ĐƠN ===");
        System.out.println("ID hóa đơn: " + invoice.getInvoiceId());
        System.out.println("ID khách hàng: " + invoice.getCustomerId());
        System.out.println("Ngày tạo: " + invoice.getCreated_at());
        System.out.println("Tổng tiền: " + invoice.getTotal_amount());

        List<InvoiceItem> items = invoiceService.getInvoiceItemsByInvoiceId(invoice.getInvoiceId());
        if (items.isEmpty()) {
            System.out.println("Không có chi tiết hóa đơn!");
        } else {
            int itemIdWidth = "ID chi tiết".length();
            int productIdWidth = "ID sản phẩm".length();
            int quantityWidth = "Số lượng".length();
            int unitPriceWidth = "Đơn giá".length();
            int totalWidth = "Thành tiền".length();

            for (InvoiceItem item : items) {
                itemIdWidth = Math.max(itemIdWidth, String.valueOf(item.getInvoice_item_id()).length());
                productIdWidth = Math.max(productIdWidth, String.valueOf(item.getProduct_id()).length());
                quantityWidth = Math.max(quantityWidth, String.valueOf(item.getQuantity()).length());
                unitPriceWidth = Math.max(unitPriceWidth, String.valueOf(item.getUnit_price()).length());
                double total = item.getQuantity() * item.getUnit_price();
                totalWidth = Math.max(totalWidth, String.valueOf(total).length());
            }

            String format = "| %-" + itemIdWidth + "s | %-" + productIdWidth + "s | %-" + quantityWidth + "s | %-" + unitPriceWidth + "s | %-" + totalWidth + "s |\n";
            String line = "+" + "-".repeat(itemIdWidth + 2) + "+" + "-".repeat(productIdWidth + 2) + "+" + "-".repeat(quantityWidth + 2) + "+" + "-".repeat(unitPriceWidth + 2) + "+" + "-".repeat(totalWidth + 2) + "+";

            System.out.println(line);
            System.out.printf(format, "ID chi tiết", "ID sản phẩm", "Số lượng", "Đơn giá", "Thành tiền");
            System.out.println(line);

            for (InvoiceItem item : items) {
                double total = item.getQuantity() * item.getUnit_price();
                System.out.printf(format,
                        item.getInvoice_item_id(),
                        item.getProduct_id(),
                        item.getQuantity(),
                        item.getUnit_price(),
                        total);
            }
            System.out.println(line);
        }
    }

    private void addInvoice() {
        System.out.println("Thêm mới hóa đơn:");
        int customerId;
        while (true) {
            customerId = (int) InputUtil.promptForPositiveNumber("Nhập ID khách hàng: ");
            if (invoiceService.customerExists(customerId)) {
                break;
            } else {
                System.out.println("\u001B[31mID khách hàng không tồn tại! Vui lòng nhập lại.\u001B[0m");
            }
        }

        LocalDate createdAt = LocalDate.now();
        List<InvoiceItem> items = new ArrayList<>();
        double totalAmount = 0.0;

        while (true) {
            String addMore = InputUtil.promptForNotEmpty("Bạn có muốn thêm chi tiết hóa đơn không? (y/n): ");
            if (addMore.equalsIgnoreCase("n")) {
                if (items.isEmpty()) {
                    System.out.println("\u001B[31mHóa đơn phải có ít nhất một chi tiết! Vui lòng thêm chi tiết.\u001B[0m");
                    continue;
                }
                break;
            }

            int productId;
            double unitPrice;
            while (true) {
                productId = (int) InputUtil.promptForPositiveNumber("Nhập ID sản phẩm: ");
                if (invoiceService.productExists(productId)) {
                    unitPrice = invoiceService.getProductPriceById(productId);
                    if (unitPrice > 0) {
                        System.out.println("Giá sản phẩm: " + unitPrice);
                        break;
                    } else {
                        System.out.println("\u001B[31mKhông thể lấy giá sản phẩm! Vui lòng thử lại.\u001B[0m");
                    }
                } else {
                    System.out.println("\u001B[31mID sản phẩm không tồn tại! Vui lòng nhập lại.\u001B[0m");
                }
            }

            int quantity;
            while (true) {
                quantity = (int) InputUtil.promptForPositiveNumber("Nhập số lượng: ");
                int productStock = invoiceService.getProductStockById(productId);
                if (productStock < quantity) {
                    System.out.println("\u001B[31mSố lượng không đủ! Còn lại: " + productStock + "\u001B[0m");
                } else {
                    InvoiceItem item = new InvoiceItem();
                    item.setProduct_id(productId);
                    item.setQuantity(quantity);
                    item.setUnit_price(unitPrice);
                    items.add(item);
                    totalAmount += quantity * unitPrice;
                    break;
                }
            }
        }

        invoiceService.addInvoice(customerId, totalAmount);
        List<Invoice> invoices = invoiceService.getInvoices();
        int latestInvoiceId = invoices.get(invoices.size() - 1).getInvoiceId();

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

            int choice = InputUtil.getChoice(0, 2);
            if (choice == 0) return;

            switch (choice) {
                case 1:
                    searchByCustomerName();
                    break;
                case 2:
                    searchByDate();
                    break;
            }
        }
    }

    private void searchByCustomerName() {
        String customerName = InputUtil.promptForNotEmpty("Nhập tên khách hàng: ");
        List<Invoice> invoices = invoiceService.searchInvoicesByCustomerName(customerName);
        displaySearchResults(invoices);
    }

    private void searchByDate() {
        LocalDate date = null;
        while (date == null) {
            try {
                String dateInput = InputUtil.promptForNotEmpty("Nhập ngày (dd/MM/yyyy): ");
                date = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("\u001B[31mNgày không đúng định dạng (dd/MM/yyyy)! Vui lòng nhập lại.\u001B[0m");
            }
        }
        List<Invoice> invoices = invoiceService.searchInvoicesByDate(date);
        displaySearchResults(invoices);
    }

    private void displaySearchResults(List<Invoice> invoices) {
        if (invoices.isEmpty()) {
            System.out.println("Không tìm thấy hóa đơn nào!");
        } else {
            int idWidth = "ID đơn hàng".length();
            int idCusWidth = "ID khách hàng".length();
            int totalWidth = "Số tiền".length();
            int createWidth = "Ngày tạo".length();

            for (Invoice i : invoices) {
                idWidth = Math.max(idWidth, String.valueOf(i.getInvoiceId()).length());
                idCusWidth = Math.max(idCusWidth, String.valueOf(i.getCustomerId()).length());
                totalWidth = Math.max(totalWidth, String.valueOf(i.getTotal_amount()).length());
                createWidth = Math.max(createWidth, String.valueOf(i.getCreated_at()).length());
            }

            String format = "| %-" + idWidth + "s | %-" + idCusWidth + "s | %-" + totalWidth + "s | %-" + createWidth + "s |\n";
            String line = "+" + "-".repeat(idWidth + 2) + "+" + "-".repeat(idCusWidth + 2) + "+" + "-".repeat(totalWidth + 2) + "+" + "-".repeat(createWidth + 2) + "+";

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

            while (true) {
                int invoiceId = (int) InputUtil.promptForPositiveNumber("Nhập ID hóa đơn để xem chi tiết (0 để quay lại): ");
                if (invoiceId == 0) break;

                Invoice selectedInvoice = invoices.stream()
                        .filter(i -> i.getInvoiceId() == invoiceId)
                        .findFirst()
                        .orElse(null);

                if (selectedInvoice == null) {
                    System.out.println("\u001B[31mKhông tìm thấy hóa đơn với ID: " + invoiceId + "\u001B[0m");
                    continue;
                }

                displayInvoiceDetails(selectedInvoice);
            }
        }
    }
}