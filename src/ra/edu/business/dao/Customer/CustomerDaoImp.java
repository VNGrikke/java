package ra.edu.business.dao.Customer;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImp implements CustomerDAO {
    private static List<Customer> customerList = new ArrayList<>();

    @Override
    public List<Customer> getCustomerList() {
        customerList.clear();
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall("{CALL get_customer_list()}");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setCustomerName(rs.getString("name"));
                customer.setCustomerEmail(rs.getString("email"));
                customer.setCustomerPhone(rs.getString("phone"));
                customer.setCustomerAddress(rs.getString("address"));
                customerList.add(customer);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
        }
        return customerList;
    }

    @Override
    public void addCustomer(String name, String email, String phone, String address) {
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall("{CALL add_customer(?,?,?,?)}")) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.executeUpdate();
            System.out.println("Thêm khách hàng thành công!");
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm khách hàng: " + e.getMessage());
        }
    }

    @Override
    public void updateCustomer(int customerId, String name, String email, String phone, String address) {
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall("{CALL update_customer(?,?,?,?,?)}")) {
            stmt.setInt(1, customerId);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setString(5, address);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cập nhật khách hàng thành công!");
            } else {
                System.out.println("Không tìm thấy khách hàng với ID: " + customerId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật khách hàng: " + e.getMessage());
        }
    }

    @Override
    public void deleteCustomer(int customerId) {
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall("{CALL delete_customer(?)}")) {
            stmt.setInt(1, customerId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Xóa khách hàng thành công!");
            } else {
                System.out.println("Không tìm thấy khách hàng với ID: " + customerId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa khách hàng: " + e.getMessage());
        }
    }

    @Override
    public boolean existsById(int customerId) {
        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM customers WHERE customer_id = ? AND status = TRUE")) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra khách hàng: " + e.getMessage());
            return false;
        }
    }
}