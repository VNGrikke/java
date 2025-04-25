package ra.edu.presentation;

import ra.edu.business.service.Invoice.InvoiceService;
import ra.edu.validate.Validator;

import java.time.LocalDate;
import java.util.Map;

public class StatisticMenu {
    private InvoiceService invoiceService;
    private DisplayMenu displayMenu;

    public StatisticMenu(InvoiceService invoiceService, DisplayMenu displayMenu) {
        this.invoiceService = invoiceService;
        this.displayMenu = displayMenu;
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n=== THỐNG KÊ DOANH THU ===");
            System.out.println("1. Thống kê doanh thu theo ngày");
            System.out.println("2. Thống kê doanh thu theo tháng");
            System.out.println("3. Thống kê doanh thu theo năm");
            System.out.println("0. Quay lại");

            int choice = getChoice();
            switch (choice) {
                case 1:
                    displayRevenueByDay();
                    break;
                case 2:
                    displayRevenueByMonth();
                    break;
                case 3:
                    displayRevenueByYear();
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

    private void displayRevenueByDay() {
        Map<LocalDate, Double> revenueByDay = invoiceService.getRevenueByDay();
        if (revenueByDay.isEmpty()) {
            System.out.println("Không có dữ liệu doanh thu!");
        } else {
            System.out.println("Doanh thu theo ngày:");
            System.out.println("+------------+----------------+");
            System.out.println("| Ngày       | Doanh thu      |");
            System.out.println("+------------+----------------+");
            for (Map.Entry<LocalDate, Double> entry : revenueByDay.entrySet()) {
                System.out.printf("| %-10s | %-14.2f |\n", entry.getKey(), entry.getValue());
            }
            System.out.println("+------------+----------------+");
        }
    }

    private void displayRevenueByMonth() {
        Map<String, Double> revenueByMonth = invoiceService.getRevenueByMonth();
        if (revenueByMonth.isEmpty()) {
            System.out.println("Không có dữ liệu doanh thu!");
        } else {
            System.out.println("Doanh thu theo tháng:");
            System.out.println("+------------+----------------+");
            System.out.println("| Tháng      | Doanh thu      |");
            System.out.println("+------------+----------------+");
            for (Map.Entry<String, Double> entry : revenueByMonth.entrySet()) {
                System.out.printf("| %-10s | %-14.2f |\n", entry.getKey(), entry.getValue());
            }
            System.out.println("+------------+----------------+");
        }
    }

    private void displayRevenueByYear() {
        Map<Integer, Double> revenueByYear = invoiceService.getRevenueByYear();
        if (revenueByYear.isEmpty()) {
            System.out.println("Không có dữ liệu doanh thu!");
        } else {
            System.out.println("Doanh thu theo năm:");
            System.out.println("+------+----------------+");
            System.out.println("| Năm  | Doanh thu      |");
            System.out.println("+------+----------------+");
            for (Map.Entry<Integer, Double> entry : revenueByYear.entrySet()) {
                System.out.printf("| %-4d | %-14.2f |\n", entry.getKey(), entry.getValue());
            }
            System.out.println("+------+----------------+");
        }
    }
}
