package ra.edu.business.dao.Product;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImp implements ProductDAO {
    public static List<Product> products = new ArrayList<>();

    @Override
    public void getAllPhoneList() {
        Connection conn = null;
        CallableStatement callSt = null;
        List<Product> productList = new ArrayList<>();

        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) {
                System.out.println("\u001B[31m" + "Không thể thiết lập kết nối đến cơ sở dữ liệu" + "\u001B[0m");
                return;
            }

            callSt = conn.prepareCall("{call get_phone_list()}");
            ResultSet rs = callSt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductid(rs.getInt("productid"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setBrand(rs.getString("brand"));
                product.setStock(rs.getInt("stock"));
                product.setStatus(rs.getBoolean("status"));
                productList.add(product);
            }

            products = productList;

            displayProductList(productList);

        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi lấy danh sách: " + e.getMessage() + "\u001B[0m");
        } finally {

            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public void addPhone(String name, double price, String brand, int stock) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) {
                System.out.println("\u001B[31m" + "Không thể thiết lập kết nối đến cơ sở dữ liệu" + "\u001B[0m");
                return;
            }

            callSt = conn.prepareCall("{call add_phone(?, ?, ?, ?)}");
            callSt.setString(1, name);
            callSt.setDouble(2, price);
            callSt.setString(3, brand);
            callSt.setInt(4, stock);
            callSt.execute();
            System.out.println("Thêm điện thoại thành công!");
            getAllPhoneList();
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi thêm điện thoại: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public void updatePhone(int productId, String name, double price, String brand, int stock) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) {
                System.out.println("\u001B[31m" + "Không thể thiết lập kết nối đến cơ sở dữ liệu" + "\u001B[0m");
                return;
            }

            callSt = conn.prepareCall("{call update_phone(?, ?, ?, ?, ?)}");
            callSt.setInt(1, productId);
            callSt.setString(2, name);
            callSt.setDouble(3, price);
            callSt.setString(4, brand);
            callSt.setInt(5, stock);
            int rowsAffected = callSt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cập nhật điện thoại thành công!");
                getAllPhoneList();
            } else {
                System.out.println("Không tìm thấy điện thoại với ID: " + productId);
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi cập nhật điện thoại: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public void deletePhone(int productId) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) {
                System.out.println("\u001B[31m" + "Không thể thiết lập kết nối đến cơ sở dữ liệu" + "\u001B[0m");
                return;
            }

            callSt = conn.prepareCall("{call delete_phone(?)}");
            callSt.setInt(1, productId);
            int rowsAffected = callSt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Xóa điện thoại thành công!");
            } else {
                System.out.println("Không tìm thấy điện thoại với ID: " + productId);
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi xóa điện thoại: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public List<Product> findPhoneByBrand(String brand) {
        Connection conn = null;
        CallableStatement callSt = null;
        List<Product> products = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) {
                System.out.println("\u001B[31m" + "Không thể thiết lập kết nối đến cơ sở dữ liệu" + "\u001B[0m");
                return products;
            }

            callSt = conn.prepareCall("{call find_phone_by_brand(?)}");
            callSt.setString(1, brand);
            ResultSet rs = callSt.executeQuery();
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
            System.out.println("\u001B[31m" + "Lỗi tìm kiếm theo hãng: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return products;
    }

    @Override
    public List<Product> findPhoneByPriceRange(double minPrice, double maxPrice) {
        Connection conn = null;
        CallableStatement callSt = null;
        List<Product> products = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) {
                System.out.println("\u001B[31m" + "Không thể thiết lập kết nối đến cơ sở dữ liệu" + "\u001B[0m");
                return products;
            }

            callSt = conn.prepareCall("{call find_phone_by_price_range(?, ?)}");
            callSt.setDouble(1, minPrice);
            callSt.setDouble(2, maxPrice);
            ResultSet rs = callSt.executeQuery();
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
            System.out.println("\u001B[31m" + "Lỗi tìm kiếm theo khoảng giá: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return products;
    }

    @Override
    public List<Product> findPhoneByStock(int minStock, int maxStock) {
        Connection conn = null;
        CallableStatement callSt = null;
        List<Product> products = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) {
                System.out.println("\u001B[31m" + "Không thể thiết lập kết nối đến cơ sở dữ liệu" + "\u001B[0m");
                return products;
            }

            callSt = conn.prepareCall("{call find_phone_by_stock(?, ?)}");
            callSt.setInt(1, minStock);
            callSt.setInt(2, maxStock);
            ResultSet rs = callSt.executeQuery();
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
            System.out.println("\u001B[31m" + "Lỗi tìm kiếm theo tồn kho: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return products;
    }

    @Override
    public boolean existsById(int productId) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) return false;

            callSt = conn.prepareCall("SELECT COUNT(*) FROM Products WHERE productid = ?");
            callSt.setInt(1, productId);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi kiểm tra sản phẩm: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }

    @Override
    public double getPriceById(int productId) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) return 0.0;

            callSt = conn.prepareCall("SELECT price FROM Products WHERE productid = ?");
            callSt.setInt(1, productId);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi lấy giá sản phẩm: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return 0.0; // Trả về 0 nếu không tìm thấy
    }


    @Override
    public int getStockById(int productId) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) return 0;

            callSt = conn.prepareCall("SELECT stock FROM Products WHERE productid = ?");
            callSt.setInt(1, productId);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                return rs.getInt("stock");
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi lấy số lượng tồn kho: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return 0; // Trả về 0 nếu không tìm thấy
    }

    public static void displayProductList(List<Product> products) {
        int idWidth = "ID".length();
        int nameWidth = "Tên sản phẩm".length();
        int brandWidth = "Nhãn hàng".length();
        int priceWidth = "Giá".length();
        int stockWidth = "Số lượng".length();

        for (Product p : products) {
            idWidth = Math.max(idWidth, String.valueOf(p.getProductid()).length());
            nameWidth = Math.max(nameWidth, p.getName() != null ? p.getName().length() : 0);
            brandWidth = Math.max(brandWidth, p.getBrand() != null ? p.getBrand().length() : 0);
            priceWidth = Math.max(priceWidth, String.format("%.2f", p.getPrice()).length());
            stockWidth = Math.max(stockWidth, String.valueOf(p.getStock()).length());
        }

        String format = "| %-" + idWidth + "s | %-" + nameWidth + "s | %-" + brandWidth + "s | %" + priceWidth + "s | %" + stockWidth + "s |\n";
        String line = "+" + "-".repeat(idWidth + 2) + "+" + "-".repeat(nameWidth + 2) + "+" + "-".repeat(brandWidth + 2) + "+" + "-".repeat(priceWidth + 2) + "+" + "-".repeat(stockWidth + 2) + "+";

        if (products.isEmpty()) {
            System.out.println("Danh sách điện thoại trống!");
        } else {
            System.out.println(line);
            System.out.printf(format, "ID", "Tên sản phẩm", "Nhãn hàng", "Giá", "Số lượng");
            System.out.println(line);

            for (Product product : products) {
                System.out.printf(format,
                        product.getProductid(),
                        product.getName() != null ? product.getName() : "(Trống)",
                        product.getBrand() != null ? product.getBrand() : "(Trống)",
                        String.format("%.2f", product.getPrice()),
                        product.getStock());
            }
            System.out.println(line);
        }
    }
}