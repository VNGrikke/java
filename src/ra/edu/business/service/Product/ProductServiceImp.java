package ra.edu.business.service.Product;

import ra.edu.business.dao.Product.ProductDAO;
import ra.edu.business.dao.Product.ProductDaoImp;
import ra.edu.business.model.Product;
import ra.edu.exception.ValidationException;
import ra.edu.validate.Validator;

import java.util.ArrayList;
import java.util.List;

public class ProductServiceImp implements ProductService {
    private ProductDAO productDAO;

    public ProductServiceImp() {
        this.productDAO = new ProductDaoImp();
    }

    @Override
    public List<Product> getAllPhoneList() {
        productDAO.getAllPhoneList();
        return null;
    }

    @Override
    public void addPhone(String name, double price, String brand, int stock) {
        try {
            Validator.validateNotEmpty(name, "Tên điện thoại");
            Validator.validatePositiveNumber(price, "Giá điện thoại");
            Validator.validateNotEmpty(brand, "Hãng điện thoại");
            Validator.validatePositiveNumber(stock, "Số lượng trong kho");
            productDAO.addPhone(name, price, brand, stock);
        } catch (ValidationException e) {
            System.out.println("\u001B[31m" + "Lỗi validation: " + e.getMessage() + "\u001B[0m");
        }
    }

    @Override
    public void updatePhone(int productId, String name, double price, String brand, int stock) {
        try {
            Validator.validatePositiveNumber(productId, "ID điện thoại");
            Validator.validateNotEmpty(name, "Tên điện thoại");
            Validator.validatePositiveNumber(price, "Giá điện thoại");
            Validator.validateNotEmpty(brand, "Hãng điện thoại");
            Validator.validatePositiveNumber(stock, "Số lượng trong kho");
            productDAO.updatePhone(productId, name, price, brand, stock);
        } catch (ValidationException e) {
            System.out.println("\u001B[31m" + "Lỗi validation: " + e.getMessage() + "\u001B[0m");
        }
    }

    @Override
    public void deletePhone(int productId) {
        try {
            Validator.validatePositiveNumber(productId, "ID điện thoại");
            productDAO.deletePhone(productId);
        } catch (ValidationException e) {
            System.out.println("\u001B[31m" + "Lỗi validation: " + e.getMessage() + "\u001B[0m");
        }
    }

    @Override
    public List<Product> findPhoneByBrand(String brand) {
        try {
            Validator.validateNotEmpty(brand, "Hãng điện thoại");
            return productDAO.findPhoneByBrand(brand);
        } catch (ValidationException e) {
            System.out.println("\u001B[31m" + "Lỗi validation: " + e.getMessage() + "\u001B[0m");
            return new ArrayList<>();
        }
    }

    @Override
    public List<Product> findPhoneByPriceRange(double minPrice, double maxPrice) {
        try {
            Validator.validatePositiveNumber(minPrice, "Giá tối thiểu");
            Validator.validatePositiveNumber(maxPrice, "Giá tối đa");
            if (minPrice > maxPrice) {
                throw new ValidationException("Giá tối thiểu phải nhỏ hơn giá tối đa");
            }
            return productDAO.findPhoneByPriceRange(minPrice, maxPrice);
        } catch (ValidationException e) {
            System.out.println("\u001B[31m" + "Lỗi validation: " + e.getMessage() + "\u001B[0m");
            return new ArrayList<>();
        }
    }

    @Override
    public List<Product> findPhoneByStock(int minStock, int maxStock) {
        try {
            Validator.validatePositiveNumber(minStock, "Tồn kho tối thiểu");
            Validator.validatePositiveNumber(maxStock, "Tồn kho tối đa");
            if (minStock > maxStock) {
                throw new ValidationException("Tồn kho tối thiểu phải nhỏ hơn tồn kho tối đa");
            }
            return productDAO.findPhoneByStock(minStock, maxStock);
        } catch (ValidationException e) {
            System.out.println("\u001B[31m" + "Lỗi validation: " + e.getMessage() + "\u001B[0m");
            return new ArrayList<>();
        }
    }
}