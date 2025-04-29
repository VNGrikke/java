package ra.edu.business.service.Product;

import ra.edu.business.dao.Product.ProductDAO;
import ra.edu.business.model.Product;

import java.util.List;

public class ProductServiceImp implements ProductService {
    private final ProductDAO productDAO;

    public ProductServiceImp(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public List<Product> getAllPhoneList() {
        return productDAO.getAllPhoneList();
    }

    @Override
    public void addPhone(String name, double price, String brand, int stock) {
        productDAO.addPhone(name, price, brand, stock);
    }

    @Override
    public void updatePhone(int productId, String name, double price, String brand, int stock) {
        productDAO.updatePhone(productId, name, price, brand, stock);
    }

    @Override
    public void deletePhone(int productId) {
        productDAO.deletePhone(productId);
    }

    @Override
    public List<Product> findPhoneByBrand(String brand) {
        return productDAO.findPhoneByBrand(brand);
    }

    @Override
    public List<Product> findPhoneByPriceRange(double minPrice, double maxPrice) {
        return productDAO.findPhoneByPriceRange(minPrice, maxPrice);
    }

    @Override
    public List<Product> findPhoneByStock(int minStock, int maxStock) {
        return productDAO.findPhoneByStock(minStock, maxStock);
    }
}