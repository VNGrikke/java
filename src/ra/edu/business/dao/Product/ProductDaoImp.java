package ra.edu.business.dao.Product;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImp implements ProductDAO {
    private static List<Product> products = new ArrayList<>();

    @Override
    public List<Product> getAllPhoneList() {
        products.clear();
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall("{CALL get_phone_list()}");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductid(rs.getInt("productid"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setBrand(rs.getString("brand"));
                product.setStock(rs.getInt("stock"));
                product.setStatus(rs.getBoolean("status"));
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách sản phẩm: " + e.getMessage());
        }
        return products;
    }

    @Override
    public void addPhone(String name, double price, String brand, int stock) {
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall("{CALL add_phone(?,?,?,?)}")) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setString(3, brand);
            stmt.setInt(4, stock);
            stmt.executeUpdate();
            System.out.println("Thêm sản phẩm thành công!");
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm sản phẩm: " + e.getMessage());
        }
    }

    @Override
    public void updatePhone(int productId, String name, double price, String brand, int stock) {
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall("{CALL update_phone(?,?,?,?,?)}")) {
            stmt.setInt(1, productId);
            stmt.setString(2, name);
            stmt.setDouble(3, price);
            stmt.setString(4, brand);
            stmt.setInt(5, stock);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cập nhật sản phẩm thành công!");
            } else {
                System.out.println("Không tìm thấy sản phẩm với ID: " + productId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật sản phẩm: " + e.getMessage());
        }
    }

    @Override
    public void deletePhone(int productId) {
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall("{CALL delete_phone(?)}")) {
            stmt.setInt(1, productId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Xóa sản phẩm thành công!");
            } else {
                System.out.println("Không tìm thấy sản phẩm với ID: " + productId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa sản phẩm: " + e.getMessage());
        }
    }

    @Override
    public List<Product> findPhoneByBrand(String brand) {
        List<Product> result = new ArrayList<>();
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall("{CALL find_phone_by_brand(?)}")) {
            stmt.setString(1, brand);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductid(rs.getInt("productid"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getDouble("price"));
                    product.setBrand(rs.getString("brand"));
                    product.setStock(rs.getInt("stock"));
                    product.setStatus(rs.getBoolean("status"));
                    result.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm sản phẩm theo nhãn hiệu: " + e.getMessage());
        }
        return result;
    }

    @Override
    public List<Product> findPhoneByPriceRange(double minPrice, double maxPrice) {
        List<Product> result = new ArrayList<>();
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall("{CALL find_phone_by_price_range(?,?)}")) {
            stmt.setDouble(1, minPrice);
            stmt.setDouble(2, maxPrice);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductid(rs.getInt("productid"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getDouble("price"));
                    product.setBrand(rs.getString("brand"));
                    product.setStock(rs.getInt("stock"));
                    product.setStatus(rs.getBoolean("status"));
                    result.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm sản phẩm theo khoảng giá: " + e.getMessage());
        }
        return result;
    }

    @Override
    public List<Product> findPhoneByStock(int minStock, int maxStock) {
        List<Product> result = new ArrayList<>();
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall("{CALL find_phone_by_stock(?,?)}")) {
            stmt.setInt(1, minStock);
            stmt.setInt(2, maxStock);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductid(rs.getInt("productid"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getDouble("price"));
                    product.setBrand(rs.getString("brand"));
                    product.setStock(rs.getInt("stock"));
                    product.setStatus(rs.getBoolean("status"));
                    result.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm sản phẩm theo tồn kho: " + e.getMessage());
        }
        return result;
    }

    @Override
    public boolean existsById(int productId) {
        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM products WHERE productid = ? AND status = TRUE")) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra sản phẩm: " + e.getMessage());
            return false;
        }
    }

    @Override
    public double getPriceById(int productId) {
        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT price FROM products WHERE productid = ? AND status = TRUE")) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("price");
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy giá sản phẩm: " + e.getMessage());
        }
        return 0.0;
    }

    @Override
    public int getStockById(int productId) {
        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT stock FROM products WHERE productid = ? AND status = TRUE")) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stock");
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tồn kho sản phẩm: " + e.getMessage());
        }
        return 0;
    }

    public static void displayProductList(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("Danh sách sản phẩm trống!");
        } else {
            int idWidth = "ID".length();
            int nameWidth = "Tên sản phẩm".length();
            int priceWidth = "Giá (USD)".length();
            int brandWidth = "Nhãn hiệu".length();
            int stockWidth = "Tồn kho".length();

            for (Product p : products) {
                idWidth = Math.max(idWidth, String.valueOf(p.getProductid()).length());
                nameWidth = Math.max(nameWidth, p.getName() != null ? p.getName().length() : 0);
                priceWidth = Math.max(priceWidth, String.valueOf(p.getPrice()).length());
                brandWidth = Math.max(brandWidth, p.getBrand() != null ? p.getBrand().length() : 0);
                stockWidth = Math.max(stockWidth, String.valueOf(p.getStock()).length());
            }

            String format = "| %-" + idWidth + "s | %-" + nameWidth + "s | %-" + priceWidth + "s | %-" + brandWidth + "s | %-" + stockWidth + "s |\n";
            String line = "+" + "-".repeat(idWidth + 2) + "+" + "-".repeat(nameWidth + 2) + "+" + "-".repeat(priceWidth + 2) + "+" + "-".repeat(brandWidth + 2) + "+" + "-".repeat(stockWidth + 2) + "+";

            System.out.println(line);
            System.out.printf(format, "ID", "Tên sản phẩm", "Giá (USD)", "Nhãn hiệu", "Tồn kho");
            System.out.println(line);

            for (Product p : products) {
                System.out.printf(format,
                        p.getProductid(),
                        p.getName() != null ? p.getName() : "(Trống)",
                        p.getPrice(),
                        p.getBrand() != null ? p.getBrand() : "(Trống)",
                        p.getStock());
            }
            System.out.println(line);
        }
    }
}