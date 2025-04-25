package ra.edu.business.dao.InvoiceItem;

import ra.edu.business.model.InvoiceItem;

import java.util.List;

public interface InvoiceItemDAO {
    void addInvoiceItem(int invoiceId, int productId, int quantity, double unitPrice);
    List<InvoiceItem> getInvoiceItemsByInvoiceId(int invoiceId);
}