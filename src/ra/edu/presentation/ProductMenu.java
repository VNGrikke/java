package ra.edu.presentation;

import ra.edu.business.dao.Product.ProductDaoImp;
import ra.edu.business.model.Product;
import ra.edu.business.service.Product.ProductService;
import ra.edu.validate.Validator;

import java.util.List;

import static ra.edu.business.dao.Product.ProductDaoImp.products;

public class ProductMenu {
    private ProductService productService;
    private DisplayMenu displayMenu;

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
                    productService.getAllPhoneList();
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

    private void addPhone() {
        System.out.println("Thêm mới điện thoại");
        String name;
        boolean isNameValid;
        do {
            name = Validator.promptForNotEmpty("Nhập tên điện thoại: ", "Tên điện thoại");
            isNameValid = true;

            for (Product product : products) {
                if (product.getName().equalsIgnoreCase(name)) {
                    System.out.println("\u001B[31m" + "Tên sản phẩm đã tồn tại, vui lòng nhập lại!" + "\u001B[0m");
                    isNameValid = false;
                    break;
                }
            }
        } while (!isNameValid);

        double price = Validator.promptForPositiveNumber("Nhập giá (USD): ", "Giá điện thoại");
        String brand = Validator.promptForNotEmpty("Nhập hãng điện thoại: ", "Hãng điện thoại");
        int stock = (int) Validator.promptForPositiveNumber("Nhập số lượng trong kho: ", "Số lượng");

        try {
            productService.addPhone(name, price, brand, stock);
        } catch (Exception e) {
            System.out.println("\u001B[31m" + "Lỗi khi thêm điện thoại: " + e.getMessage() + "\u001B[0m");
        }
    }

    private void updatePhone() {
        System.out.println("Sửa thông tin điện thoại:");
        int id = (int) Validator.promptForPositiveNumber("Nhập ID điện thoại: ", "ID điện thoại");

        Product existingProduct = null;
        for (Product product : products) {
            if (product.getProductid() == id) {
                existingProduct = product;
                break;
            }
        }

        if (existingProduct == null) {
            System.out.println("\u001B[31m" + "Không tìm thấy điện thoại với ID: " + id + "\u001B[0m");
            return;
        }

        String name;
        boolean isNameValid;
        do {
            name = Validator.promptForNotEmpty("Nhập tên điện thoại: ", "Tên điện thoại");
            isNameValid = true;
            if (existingProduct.getName().equalsIgnoreCase(name)) {
                break;
            }
            for (Product product : products) {
                if (product.getName().equalsIgnoreCase(name)) {
                    System.out.println("\u001B[31m" + "Tên sản phẩm đã tồn tại, vui lòng nhập lại!" + "\u001B[0m");
                    isNameValid = false;
                    break;
                }
            }
        } while (!isNameValid);

        double price = Validator.promptForPositiveNumber("Nhập giá (USD): ", "Giá điện thoại");
        String brand = Validator.promptForNotEmpty("Nhập hãng điện thoại: ", "Hãng điện thoại");
        int stock = (int) Validator.promptForPositiveNumber("Nhập số lượng trong kho: ", "Số lượng");
        try {
            productService.updatePhone(id, name, price, brand, stock);
            System.out.println("\u001B[32m" + "Cập nhật thành công!" + "\u001B[0m");
        } catch (Exception e) {
            System.out.println("\u001B[31m" + "Lỗi khi cập nhật điện thoại: " + e.getMessage() + "\u001B[0m");
        }
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
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void findPhoneByBrand() {
        String brand = Validator.promptForNotEmpty("Nhập hãng điện thoại: ", "Hãng điện thoại");
        List<Product> products = productService.findPhoneByBrand(brand);
        displayProducts(products);
    }

    private void findPhoneByPriceRange() {
        double minPrice = Validator.promptForPositiveNumber("Nhập giá tối thiểu (USD): ", "Giá tối thiểu");
        double maxPrice = Validator.promptForPositiveNumber("Nhập giá tối đa (USD): ", "Giá tối đa");
        List<Product> products = productService.findPhoneByPriceRange(minPrice, maxPrice);
        displayProducts(products);
    }

    private void findPhoneByStock() {
        int minStock = (int) Validator.promptForPositiveNumber("Nhập tồn kho tối thiểu: ", "Tồn kho tối thiểu");
        int maxStock = (int) Validator.promptForPositiveNumber("Nhập tồn kho tối đa: ", "Tồn kho tối đa");
        List<Product> products = productService.findPhoneByStock(minStock, maxStock);
        displayProducts(products);
    }

    private void displayProducts(List<Product> products) {
        ProductDaoImp.displayProductList(products);
    }
}