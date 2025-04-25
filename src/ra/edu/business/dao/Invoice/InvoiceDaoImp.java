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
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) return invoices;

            callSt = conn.prepareCall("{call get_invoices()}");
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceId(rs.getInt("invoice_id"));
                invoice.setCustomerId(rs.getInt("customer_id"));
                invoice.setCreated_at(rs.getDate("invoice_date").toLocalDate());
                invoice.setTotal_amount(rs.getDouble("total"));
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi lấy danh sách hóa đơn: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return invoices;
    }

    @Override
    public void addInvoice(int customerId, double totalAmount) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) return;

            callSt = conn.prepareCall("{call add_invoice(?, ?)}");
            callSt.setInt(1, customerId);
            callSt.setDouble(2, totalAmount);
            callSt.execute();
            System.out.println("Thêm hóa đơn thành công!");
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi thêm hóa đơn: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public List<Invoice> searchInvoicesByCustomerName(String customerName) {
        List<Invoice> invoices = new ArrayList<>();
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) return invoices;

            callSt = conn.prepareCall("{call search_invoices_by_customer_name(?)}");
            callSt.setString(1, customerName);
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceId(rs.getInt("invoice_id"));
                invoice.setCustomerId(rs.getInt("customer_id"));
                invoice.setCreated_at(rs.getDate("invoice_date").toLocalDate());
                invoice.setTotal_amount(rs.getDouble("total"));
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi tìm kiếm hóa đơn theo tên khách hàng: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return invoices;
    }

    @Override
    public List<Invoice> searchInvoicesByDate(LocalDate date) {
        List<Invoice> invoices = new ArrayList<>();
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) return invoices;

            callSt = conn.prepareCall("{call search_invoices_by_date(?)}");
            callSt.setDate(1, Date.valueOf(date));
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceId(rs.getInt("invoice_id"));
                invoice.setCustomerId(rs.getInt("customer_id"));
                invoice.setCreated_at(rs.getDate("invoice_date").toLocalDate());
                invoice.setTotal_amount(rs.getDouble("total"));
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi tìm kiếm hóa đơn theo ngày: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return invoices;
    }

    @Override
    public Map<LocalDate, Double> getRevenueByDay() {
        Map<LocalDate, Double> revenueByDay = new TreeMap<>();
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) return revenueByDay;

            callSt = conn.prepareCall(
                    "SELECT invoice_date, SUM(total) as total_revenue " +
                            "FROM Invoices " +
                            "GROUP BY invoice_date " +
                            "ORDER BY invoice_date"
            );
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                LocalDate date = rs.getDate("invoice_date").toLocalDate();
                double totalRevenue = rs.getDouble("total_revenue");
                revenueByDay.put(date, totalRevenue);
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi thống kê doanh thu theo ngày: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return revenueByDay;
    }

    @Override
    public Map<String, Double> getRevenueByMonth() {
        Map<String, Double> revenueByMonth = new TreeMap<>();
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) return revenueByMonth;

            String sql = "SELECT DATE_FORMAT(invoice_date, '%Y-%m') AS month_year, " +
                    "SUM(total) AS total_revenue " +
                    "FROM Invoices " +
                    "GROUP BY DATE_FORMAT(invoice_date, '%Y-%m') " +
                    "ORDER BY month_year";

            callSt = conn.prepareCall(sql);
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                String monthYear = rs.getString("month_year");
                double totalRevenue = rs.getDouble("total_revenue");
                revenueByMonth.put(monthYear, totalRevenue);
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi thống kê doanh thu theo tháng: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return revenueByMonth;
    }


    @Override
    public Map<Integer, Double> getRevenueByYear() {
        Map<Integer, Double> revenueByYear = new TreeMap<>();
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) return revenueByYear;

            callSt = conn.prepareCall(
                    "SELECT YEAR(invoice_date) as year, SUM(total) as total_revenue " +
                            "FROM Invoices " +
                            "GROUP BY YEAR(invoice_date) " +
                            "ORDER BY YEAR(invoice_date)"
            );
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                int year = rs.getInt("year");
                double totalRevenue = rs.getDouble("total_revenue");
                revenueByYear.put(year, totalRevenue);
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi thống kê doanh thu theo năm: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return revenueByYear;
    }
}