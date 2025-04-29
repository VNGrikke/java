package ra.edu.business.dao.Invoice;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Invoice;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class InvoiceDaoImp implements InvoiceDAO {
    @Override
    public List<Invoice> getInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall("{CALL get_invoices()}");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceId(rs.getInt("invoice_id"));
                invoice.setCustomerId(rs.getInt("customer_id"));
                invoice.setCreated_at(rs.getDate("invoice_date").toLocalDate());
                invoice.setTotal_amount(rs.getDouble("total"));
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách hóa đơn: " + e.getMessage());
        }
        return invoices;
    }

    @Override
    public void addInvoice(int customerId, double totalAmount) {
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall("{CALL add_invoice(?,?)}")) {
            conn.setAutoCommit(false);
            try {
                stmt.setInt(1, customerId);
                stmt.setDouble(2, totalAmount);
                stmt.executeUpdate();
                conn.commit();
                System.out.println("Thêm hóa đơn thành công!");
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Lỗi khi thêm hóa đơn: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi quản lý giao dịch: " + e.getMessage());
        }
    }

    @Override
    public List<Invoice> searchInvoicesByCustomerName(String customerName) {
        List<Invoice> invoices = new ArrayList<>();
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall("{CALL search_invoices_by_customer_name(?)}")) {
            stmt.setString(1, customerName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceId(rs.getInt("invoice_id"));
                    invoice.setCustomerId(rs.getInt("customer_id"));
                    invoice.setCreated_at(rs.getDate("invoice_date").toLocalDate());
                    invoice.setTotal_amount(rs.getDouble("total"));
                    invoices.add(invoice);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm hóa đơn theo tên khách hàng: " + e.getMessage());
        }
        return invoices;
    }

    @Override
    public List<Invoice> searchInvoicesByDate(LocalDate date) {
        List<Invoice> invoices = new ArrayList<>();
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall("{CALL search_invoices_by_date(?)}")) {
            stmt.setDate(1, Date.valueOf(date));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceId(rs.getInt("invoice_id"));
                    invoice.setCustomerId(rs.getInt("customer_id"));
                    invoice.setCreated_at(rs.getDate("invoice_date").toLocalDate());
                    invoice.setTotal_amount(rs.getDouble("total"));
                    invoices.add(invoice);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm hóa đơn theo ngày: " + e.getMessage());
        }
        return invoices;
    }

    @Override
    public Map<LocalDate, Double> getRevenueByDay() {
        Map<LocalDate, Double> revenueByDay = new TreeMap<>();
        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT DATE(invoice_date) AS invoice_day, SUM(total) AS total_revenue FROM invoices GROUP BY DATE(invoice_date)")) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate date = rs.getDate("invoice_day").toLocalDate();
                    double total = rs.getDouble("total_revenue");
                    revenueByDay.put(date, total);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy doanh thu theo ngày: " + e.getMessage());
        }
        return revenueByDay;
    }

    @Override
    public Map<String, Double> getRevenueByMonth() {
        Map<String, Double> revenueByMonth = new TreeMap<>();
        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT DATE_FORMAT(invoice_date, '%Y-%m') AS invoice_month, SUM(total) AS total_revenue FROM invoices GROUP BY DATE_FORMAT(invoice_date, '%Y-%m')")) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String month = rs.getString("invoice_month");
                    double total = rs.getDouble("total_revenue");
                    revenueByMonth.put(month, total);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy doanh thu theo tháng: " + e.getMessage());
        }
        return revenueByMonth;
    }

    @Override
    public Map<Integer, Double> getRevenueByYear() {
        Map<Integer, Double> revenueByYear = new TreeMap<>();
        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT YEAR(invoice_date) AS invoice_year, SUM(total) AS total_revenue FROM invoices GROUP BY YEAR(invoice_date)")) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int year = rs.getInt("invoice_year");
                    double total = rs.getDouble("total_revenue");
                    revenueByYear.put(year, total);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy doanh thu theo năm: " + e.getMessage());
        }
        return revenueByYear;
    }
}