package ra.edu.business.dao.Customer;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImp implements CustomerDAO {
    public static List<Customer> customerList = new ArrayList<>();
    @Override
    public List<Customer> getCustomerList() {
        Connection conn = null;
        CallableStatement callSt = null;
        List<Customer> customers = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) return customers;

            callSt = conn.prepareCall("{call get_customer_list()}");
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setCustomerName(rs.getString("name"));
                customer.setCustomerEmail(rs.getString("email"));
                customer.setCustomerPhone(rs.getString("phone"));
                customer.setCustomerAddress(rs.getString("address"));
                customers.add(customer);
            }
            customerList = customers;
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi lấy danh sách khách hàng: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return customers;
    }

    @Override
    public void addCustomer(String name, String email, String phone, String address) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) return;

            callSt = conn.prepareCall("{call add_customer(?, ?, ?, ?)}");
            callSt.setString(1, name);
            callSt.setString(2, email);
            callSt.setString(3, phone);
            callSt.setString(4, address);
            callSt.execute();
            System.out.println("Thêm khách hàng thành công!");
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi thêm khách hàng: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public void updateCustomer(int customerId, String name, String email, String phone, String address) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) return;

            callSt = conn.prepareCall("{call update_customer(?, ?, ?, ?, ?)}");
            callSt.setInt(1, customerId);
            callSt.setString(2, name);
            callSt.setString(3, email);
            callSt.setString(4, phone);
            callSt.setString(5, address);
            int rowsAffected = callSt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cập nhật khách hàng thành công!");
            } else {
                System.out.println("Không tìm thấy khách hàng với ID: " + customerId);
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi cập nhật khách hàng: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public void deleteCustomer(int customerId) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) return;

            callSt = conn.prepareCall("{call delete_customer(?)}");
            callSt.setInt(1, customerId);
            int rowsAffected = callSt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Xóa khách hàng thành công!");
            } else {
                System.out.println("Không tìm thấy khách hàng với ID: " + customerId);
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi xóa khách hàng: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public boolean existsById(int customerId) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) return false;

            callSt = conn.prepareCall("SELECT COUNT(*) FROM Customers WHERE customer_id = ?");
            callSt.setInt(1, customerId);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi kiểm tra khách hàng: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }
}