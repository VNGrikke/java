package ra.edu.business.dao.Product;

import ra.edu.business.model.Product;

import java.util.List;

public interface ProductDAO {
    void getAllPhoneList();
    void addPhone(String name, double price, String brand, int stock);
    void updatePhone(int productId, String name, double price, String brand, int stock);
    void deletePhone(int productId);
    List<Product> findPhoneByBrand(String brand);
    List<Product> findPhoneByPriceRange(double minPrice, double maxPrice);
    List<Product> findPhoneByStock(int minStock, int maxStock);
    boolean existsById(int productId);
    double getPriceById(int productId);
    int getStockById(int productId);
}