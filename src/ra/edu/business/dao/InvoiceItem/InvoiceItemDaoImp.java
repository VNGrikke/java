package ra.edu.business.dao.InvoiceItem;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.InvoiceItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceItemDaoImp implements InvoiceItemDAO {
    @Override
    public void addInvoiceItem(int invoiceId, int productId, int quantity, double unitPrice) {
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall("{CALL add_invoice_item(?,?,?,?)}")) {
            conn.setAutoCommit(false);
            try {
                stmt.setInt(1, invoiceId);
                stmt.setInt(2, productId);
                stmt.setInt(3, quantity);
                stmt.setDouble(4, unitPrice);
                stmt.executeUpdate();
                conn.commit();
                System.out.println("Thêm chi tiết hóa đơn thành công!");
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Lỗi khi thêm chi tiết hóa đơn: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi quản lý giao dịch: " + e.getMessage());
        }
    }

    @Override
    public List<InvoiceItem> getInvoiceItemsByInvoiceId(int invoiceId) {
        List<InvoiceItem> items = new ArrayList<>();
        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM invoice_items WHERE invoice_id = ?")) {
            stmt.setInt(1, invoiceId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    InvoiceItem item = new InvoiceItem();
                    item.setInvoice_item_id(rs.getInt("item_id"));
                    item.setInvoice_id(rs.getInt("invoice_id"));
                    item.setProduct_id(rs.getInt("productid"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnit_price(rs.getDouble("unit_price"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy chi tiết hóa đơn: " + e.getMessage());
        }
        return items;
    }
}