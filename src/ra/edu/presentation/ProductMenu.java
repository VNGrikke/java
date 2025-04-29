package ra.edu.presentation;

import ra.edu.business.dao.Product.ProductDaoImp;
import ra.edu.business.model.Product;
import ra.edu.business.service.Product.ProductService;
import ra.edu.util.InputUtil;
import ra.edu.validate.Validator;

import java.util.List;

public class ProductMenu {
    private final ProductService productService;
    private final DisplayMenu displayMenu;

    public ProductMenu(ProductService productService, DisplayMenu displayMenu) {
        this.productService = productService;
        this.displayMenu = displayMenu;
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ ĐIỆN THOẠI ===");
            System.out.println("1. Danh sách điện thoại");
            System.out.println("2. Thêm mới điện thoại");
            System.out.println("3. Sửa điện thoại");
            System.out.println("4. Xóa điện thoại");
            System.out.println("5. Tìm kiếm điện thoại (hãng/khoảng giá/tồn kho)");
            System.out.println("0. Quay lại");

            int choice = getChoice();
            switch (choice) {
                case 1:
                    ProductDaoImp.displayProductList(productService.getAllPhoneList());
                    break;
                case 2:
                    addPhone();
                    break;
                case 3:
                    updatePhone();
                    break;
                case 4:
                    deletePhone();
                    break;
                case 5:
                    findPhone();
                    break;
                case 0:
                    displayMenu.menuManager();
                    return;
            }
        }
    }

    private int getChoice() {
        return InputUtil.getChoice(0, 5);
    }

    private void addPhone() {
        System.out.println("Thêm mới điện thoại");
        String name = Validator.promptForNotEmpty("Nhập tên điện thoại: ", "Tên điện thoại");
        List<Product> existingProducts = productService.getAllPhoneList();
        if (existingProducts.stream().anyMatch(p -> p.getName().equalsIgnoreCase(name))) {
            System.out.println("\u001B[31mTên sản phẩm đã tồn tại, vui lòng nhập lại!\u001B[0m");
            return;
        }
        double price = Validator.promptForPositiveNumber("Nhập giá (USD): ", "Giá điện thoại");
        String brand = Validator.promptForNotEmpty("Nhập hãng điện thoại: ", "Hãng điện thoại");
        int stock = (int) Validator.promptForPositiveNumber("Nhập số lượng trong kho: ", "Số lượng");
        productService.addPhone(name, price, brand, stock);
    }

    private void updatePhone() {
        System.out.println("Sửa thông tin điện thoại:");
        int id = (int) Validator.promptForPositiveNumber("Nhập ID điện thoại: ", "ID điện thoại");
        List<Product> existingProducts = productService.getAllPhoneList();
        if (existingProducts.stream().noneMatch(p -> p.getProductid() == id)) {
            System.out.println("\u001B[31mKhông tìm thấy điện thoại với ID: " + id + "\u001B[0m");
            return;
        }
        String name = Validator.promptForNotEmpty("Nhập tên điện thoại: ", "Tên điện thoại");
        if (existingProducts.stream().anyMatch(p -> p.getName().equalsIgnoreCase(name) && p.getProductid() != id)) {
            System.out.println("\u001B[31mTên sản phẩm đã tồn tại, vui lòng nhập lại!\u001B[0m");
            return;
        }
        double price = Validator.promptForPositiveNumber("Nhập giá (USD): ", "Giá điện thoại");
        String brand = Validator.promptForNotEmpty("Nhập hãng điện thoại: ", "Hãng điện thoại");
        int stock = (int) Validator.promptForPositiveNumber("Nhập số lượng trong kho: ", "Số lượng");
        productService.updatePhone(id, name, price, brand, stock);
    }

    private void deletePhone() {
        System.out.println("Xóa điện thoại:");
        int id = (int) Validator.promptForPositiveNumber("Nhập ID điện thoại: ", "ID điện thoại");
        productService.deletePhone(id);
    }

    private void findPhone() {
        while (true) {
            System.out.println("\n=== TÌM KIẾM ĐIỆN THOẠI ===");
            System.out.println("1. Theo hãng");
            System.out.println("2. Theo khoảng giá");
            System.out.println("3. Theo tồn kho");
            System.out.println("0. Quay lại");

            int choice = getChoice();
            switch (choice) {
                case 1:
                    findPhoneByBrand();
                    break;
                case 2:
                    findPhoneByPriceRange();
                    break;
                case 3:
                    findPhoneByStock();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void findPhoneByBrand() {
        String brand = Validator.promptForNotEmpty("Nhập hãng điện thoại: ", "Hãng điện thoại");
        List<Product> products = productService.findPhoneByBrand(brand);
        ProductDaoImp.displayProductList(products);
    }

    private void findPhoneByPriceRange() {
        double minPrice = Validator.promptForPositiveNumber("Nhập giá tối thiểu (USD): ", "Giá tối thiểu");
        double maxPrice = Validator.promptForPositiveNumber("Nhập giá tối đa (USD): ", "Giá tối đa");
        List<Product> products = productService.findPhoneByPriceRange(minPrice, maxPrice);
        ProductDaoImp.displayProductList(products);
    }

    private void findPhoneByStock() {
        int minStock = (int) Validator.promptForPositiveNumber("Nhập tồn kho tối thiểu: ", "Tồn kho tối thiểu");
        int maxStock = (int) Validator.promptForPositiveNumber("Nhập tồn kho tối đa: ", "Tồn kho tối đa");
        List<Product> products = productService.findPhoneByStock(minStock, maxStock);
        ProductDaoImp.displayProductList(products);
    }
}