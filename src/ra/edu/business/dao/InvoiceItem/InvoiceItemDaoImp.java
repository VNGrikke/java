package ra.edu.business.dao.InvoiceItem;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.InvoiceItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceItemDaoImp implements InvoiceItemDAO {

    @Override
    public void addInvoiceItem(int invoiceId, int productId, int quantity, double unitPrice) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) return;

            callSt = conn.prepareCall("{call add_invoice_item(?, ?, ?, ?)}");
            callSt.setInt(1, invoiceId);
            callSt.setInt(2, productId);
            callSt.setInt(3, quantity);
            callSt.setDouble(4, unitPrice);
            callSt.execute();
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi thêm chi tiết hóa đơn: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public List<InvoiceItem> getInvoiceItemsByInvoiceId(int invoiceId) {
        List<InvoiceItem> items = new ArrayList<>();
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (conn == null) return items;

            callSt = conn.prepareCall("SELECT * FROM invoice_items WHERE invoice_id = ?");
            callSt.setInt(1, invoiceId);
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                InvoiceItem item = new InvoiceItem();
                item.setInvoice_item_id(rs.getInt("item_id"));
                item.setProduct_id(rs.getInt("productid"));
                item.setQuantity(rs.getInt("quantity"));
                item.setUnit_price(rs.getDouble("unit_price"));
                items.add(item);
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi lấy danh sách chi tiết hóa đơn: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return items;
    }
}