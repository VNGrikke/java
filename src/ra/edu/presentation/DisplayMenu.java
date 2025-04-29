package ra.edu.presentation;

import ra.edu.business.dao.Admin.AdminDaoImp;
import ra.edu.business.dao.Customer.CustomerDaoImp;
import ra.edu.business.dao.Invoice.InvoiceDaoImp;
import ra.edu.business.dao.InvoiceItem.InvoiceItemDaoImp;
import ra.edu.business.dao.Product.ProductDaoImp;
import ra.edu.business.service.Admin.AdminService;
import ra.edu.business.service.Admin.AdminServiceImp;
import ra.edu.business.service.Customer.CustomerService;
import ra.edu.business.service.Customer.CustomerServiceImp;
import ra.edu.business.service.Invoice.InvoiceService;
import ra.edu.business.service.Invoice.InvoiceServiceImp;
import ra.edu.business.service.Product.ProductService;
import ra.edu.business.service.Product.ProductServiceImp;
import ra.edu.util.InputUtil;

import java.io.Console;

public class DisplayMenu {
    private final AdminService adminService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final InvoiceService invoiceService;
    private final ProductMenu productMenu;
    private final CustomerMenu customerMenu;
    private final InvoiceMenu invoiceMenu;
    private final StatisticMenu statisticMenu;

    public DisplayMenu() {
        this.adminService = new AdminServiceImp(new AdminDaoImp());
        this.productService = new ProductServiceImp(new ProductDaoImp());
        this.customerService = new CustomerServiceImp(new CustomerDaoImp());
        this.invoiceService = new InvoiceServiceImp(new InvoiceDaoImp(), new InvoiceItemDaoImp(), new CustomerDaoImp(), new ProductDaoImp());

        this.productMenu = new ProductMenu(productService, this);
        this.customerMenu = new CustomerMenu(customerService, this);
        this.invoiceMenu = new InvoiceMenu(invoiceService, customerService, this);
        this.statisticMenu = new StatisticMenu(invoiceService, this);
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n=== MENU HỆ THỐNG QUẢN LÝ CỬA HÀNG ĐIỆN THOẠI ===");
            System.out.println("1. Đăng nhập");
            System.out.println("0. Thoát");

            int choice = getChoice();
            switch (choice) {
                case 1:
                    if (login()) {
                        menuManager();
                    }
                    break;
                case 0:
                    System.out.println("Thoát chương trình!");
                    adminService.logout();
                    return;
            }
        }
    }

    public void menuManager() {
        while (true) {
            System.out.println("\n+============= DANH SÁCH QUẢN LÝ =============+");
            System.out.println("| 1 | Quản lý điện thoại                      |");
            System.out.println("| 2 | Quản lý khách hàng                      |");
            System.out.println("| 3 | Quản lý hóa đơn                         |");
            System.out.println("| 4 | Thống kê doanh thu                      |");
            System.out.println("| 0 | Đăng xuất                               |");
            System.out.println("+=============================================+");

            int choice = getChoice();
            switch (choice) {
                case 1:
                    productMenu.displayMenu();
                    break;
                case 2:
                    customerMenu.displayMenu();
                    break;
                case 3:
                    invoiceMenu.displayMenu();
                    break;
                case 4:
                    statisticMenu.displayMenu();
                    break;
                case 0:
                    adminService.logout();
                    return;
            }
        }
    }

    private int getChoice() {
        return InputUtil.getChoice(0, 4);
    }

    private boolean login() {
        Console console = System.console();
        if (console == null) {
            System.out.println("Không thể sử dụng Console. Vui lòng chạy trong terminal.");
            return false;
        }
        String username = InputUtil.promptForNotEmpty("Username: ");
        String password = InputUtil.promptForNotEmpty("Password: ");
        return adminService.login(username, password);
    }
}